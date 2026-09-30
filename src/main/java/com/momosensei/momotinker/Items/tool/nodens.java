package com.momosensei.momotinker.Items.tool;

import com.momosensei.momotinker.Momotinker;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.modifiers.ability.interaction.BlockingModifier;

import java.util.Iterator;
import java.util.List;

import static com.momosensei.momotinker.Modifiers.momomodifier.isToolStack;
import static com.momosensei.momotinker.network.packet.KeyInputPKT.key_input_cooldown;

public class nodens extends ModifiableItem {
    public nodens(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerAttack);
    }
    public static final ResourceLocation nodens_ready = Momotinker.getResource("nodens_ready");

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ToolStack tool = ToolStack.from(stack);
        player.startUsingItem(hand);
        if (tool.isBroken()){
            return InteractionResultHolder.fail(stack);
        }
        if (!tool.isBroken()) {
            return InteractionResultHolder.pass(stack);
        }
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        ItemStack retval = super.finishUsingItem(stack, level, living);
        ToolStack tool = ToolStack.from(retval);
        if (tool.isBroken()) {
            return retval;
        }
        if (living instanceof Player player) {
            player.getPersistentData().putBoolean(nodens_ready.toString(), true);
            for (int j = 0; j < player.getInventory().items.size(); j++) {
                ItemStack stack1 = player.getInventory().getItem(j);
                if (player.getCooldowns().isOnCooldown(stack1.getItem())){
                    player.getCooldowns().removeCooldown(stack1.getItem());
                }
                if (isToolStack(stack1)) {
                    ToolStack tool1 = ToolStack.from(stack1);
                    ModDataNBT data = tool1.getPersistentData();
                    for (ResourceLocation key : key_input_cooldown){
                        if (!data.contains(key, Tag.TAG_FLOAT)) continue;
                        float value = data.getFloat(key);
                        if (value > 0) {
                            data.putFloat(key, Mth.floor(value * 0.5f));
                        }
                    }
                }
            }
        }
        int a = (int) (tool.getStats().getInt(ToolStats.DURABILITY) * 0.1f) + 1000;
        if (tool.getStats().getInt(ToolStats.DURABILITY) - tool.getDamage() < a) {
            tool.setDamage(tool.getStats().getInt(ToolStats.DURABILITY));
        } else if (tool.getStats().getInt(ToolStats.DURABILITY) - tool.getDamage() > a) {
            tool.setDamage(tool.getDamage() + a);
        }
        return retval;
    }

    public void onPlayerAttack(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (player != null&&player.getPersistentData().getBoolean(nodens_ready.toString())) {
            player.getPersistentData().remove(nodens_ready.toString());
        }
    }

    public int getUseDuration(ItemStack stack) {
        return 1;
    }
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return BlockingModifier.blockWhileCharging(ToolStack.from(stack), UseAnim.BLOCK);
    }

    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        return !player.isCreative();
    }

    public List<Component> getStatInformation(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        tooltips = this.getStats(tool, player, tooltips, key, tooltipFlag);
        return tooltips;
    }
    public List<Component> getStats(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
        if (tool.hasTag(TinkerTags.Items.DURABILITY)) {
            builder.add(ToolStats.DURABILITY);
        }
        if (tool.hasTag(TinkerTags.Items.MELEE)) {
            builder.add(ToolStats.ATTACK_DAMAGE);
            builder.add(ToolStats.ATTACK_SPEED);
        }
        builder.addAllFreeSlots();
        Iterator var7 = tool.getModifierList().iterator();
        while(var7.hasNext()) {
            ModifierEntry entry = (ModifierEntry)var7.next();
            entry.getHook(ModifierHooks.TOOLTIP).addTooltip(tool, entry, player, tooltips, key, tooltipFlag);
        }
        return tooltips;
    }
}
