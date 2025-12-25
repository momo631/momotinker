package com.momosensei.momotinker.tool;


import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.HudCharge.ToolsTimeCharge;
import com.momosensei.momotinker.register.MomotinkerConfig;
import com.momosensei.momotinker.register.MomotinkerTools;
import com.momosensei.momotinker.util.AttackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.modifiers.ability.interaction.BlockingModifier;

import java.util.Iterator;
import java.util.List;

import static com.momosensei.momotinker.Modifiers.momomodifier.getbonus;
import static com.momosensei.momotinker.tool.entropy_burning_cube.*;
import static slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook.KEY_DRAWTIME;

public class entropy_burning_riding_spear extends ModifiableItem {
    public entropy_burning_riding_spear(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    private void livinghurtevent(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player&&a!=null&&player.getMainHandItem().is(MomotinkerTools.entropy_burning_riding_spear.get())){
            if (!checkOffHand(player)) {
                event.setAmount(0.4F * event.getAmount());
            }
        }
    }
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ToolStack tool = ToolStack.from(stack);
        int drawTime = (int) (20/ ConditionalStatModifierHook.getModifiedStat(tool,player,ToolStats.ATTACK_SPEED));
        tool.getPersistentData().putInt(KEY_DRAWTIME,drawTime);
        player.startUsingItem(hand);
        if (!checkOffHand(player)){
            return InteractionResultHolder.fail(stack);
        }
        if (tool.isBroken()){
            return InteractionResultHolder.fail(stack);
        }
        if (!tool.isBroken()) {
            return InteractionResultHolder.pass(stack);
        }
        return InteractionResultHolder.consume(stack);
    }
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int duration) {
        AttackUtil.stopScoping(livingEntity);
        ToolStack tool = ToolStack.from(stack);
        int hadal_limit = MomotinkerConfig.hadal_limit.get();
        int i = this.getUseDuration(stack) - duration;
        float perc = Mth.clamp((float) i / (20 / tool.getStats().get(ToolStats.ATTACK_SPEED)),0,1);
        if (tool.isBroken()){
            tool.getPersistentData().remove(KEY_DRAWTIME);
            return;
        }
        if (livingEntity instanceof Player player1) {
            if (perc >= 1){
                player1.hasImpulse = true;
                player1.startAutoSpinAttack(2);
                int a =3;
                if (tool.getPersistentData().getInt(hadal)==hadal_limit&&player1.isInWater()||player1.level().isRaining()) {
                    a+=1;
                }
                player1.setDeltaMovement(player1.getLookAngle().scale(a));
                player1.invulnerableTime = 20;
                player1.fallDistance = 0;
            }
            if (livingEntity instanceof ServerPlayer player) {
                Channel.sendToPlayer(new ToolsTimeCharge(0), player);
                player1.awardStat(Stats.ITEM_USED.get(this));
                ToolDamageUtil.damageAnimated(tool, 1, player);
            }
        }
        tool.getPersistentData().remove(KEY_DRAWTIME);
    }
    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int chargeRemaining) {
        ToolStack tool = ToolStack.from(stack);
        if (living instanceof ServerPlayer player) {
            float perc = Mth.clamp((float) (this.getUseDuration(stack) - chargeRemaining) / (20 / tool.getStats().get(ToolStats.ATTACK_SPEED)),0,1);
            Channel.sendToPlayer(new ToolsTimeCharge(perc), player);
        }
    }
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return BlockingModifier.blockWhileCharging(ToolStack.from(stack), UseAnim.BOW);
    }

    public boolean mineBlock(ItemStack stack, Level level, BlockState blockState, BlockPos blockPos, LivingEntity entity) {
        if ((double) blockState.getDestroySpeed(level, blockPos) != 0.0D) {
            stack.hurtAndBreak(0, entity, (entity1) -> {
                entity1.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        return true;
    }

    public static boolean checkOffHand(Player player){
        return player!=null&& !player.hasItemInSlot(EquipmentSlot.OFFHAND);
    }
    public List<Component> getStatInformation(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        tooltips = this.getStats(tool, player, tooltips, key, tooltipFlag);
        return tooltips;
    }
    public List<Component> getStats(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
        ModDataNBT a = tool.getPersistentData();
        int hadal_limit = MomotinkerConfig.hadal_limit.get();
        int stellarcore_limit = MomotinkerConfig.stellarcore_limit.get();
        int crystallized_limit = MomotinkerConfig.crystallized_limit.get();
        int liverization_limit = MomotinkerConfig.liverization_limit.get();
        if (tool.hasTag(TinkerTags.Items.DURABILITY)) {
            builder.add(ToolStats.DURABILITY);
        }
        if (tool.hasTag(TinkerTags.Items.MELEE)) {
            builder.add(ToolStats.ATTACK_DAMAGE);
            builder.add(ToolStats.ATTACK_SPEED);
        }
        if (player != null) {
            float bonus = getbonus(player,2500);
            if (a.getInt(hadal)==hadal_limit){
                bonus*=2;
            }
            builder.add(Component.translatable("item.momotinker.tooltip.entropy_burning_riding_spear1").append(String.format("%.0f",bonus)+"%"));
        }
        builder.addAllFreeSlots();
        if (!checkOffHand(player)){
            builder.add(Component.translatable("momotinker.tool.tooltip.offhand_hastool").withStyle(ChatFormatting.RED));
        }

        if (a.getInt(hadal)<hadal_limit&&a.getInt(stellarcore)<stellarcore_limit&&a.getInt(crystallized)<crystallized_limit&&a.getInt(liverization)<liverization_limit) {
            builder.add(Component.translatable("item.momotinker.tooltip.hadal").append(hadal_limit + "").append(Component.translatable("item.momotinker.tooltip.hadal1")).append(a.getInt(hadal) + "").withStyle(ChatFormatting.GOLD));
        }
        if (a.getInt(hadal)==hadal_limit) {
            builder.add(Component.translatable("item.momotinker.tooltip.hadal2").withStyle(ChatFormatting.DARK_BLUE));
        }
        if (a.getInt(liverization)>=liverization_limit) {
            if (a.getInt(liverization)==liverization_limit) {
                builder.add(Component.translatable("item.momotinker.tooltip.liverization").withStyle(ChatFormatting.RED));
                builder.add(Component.translatable("item.momotinker.tooltip.liverization1").withStyle(ChatFormatting.RED));
            }
            if (a.getInt(liverization)>liverization_limit) {
                builder.add(Component.translatable("item.momotinker.tooltip.liverization2").withStyle(ChatFormatting.GRAY));
            }
        }
        if (a.getInt(stellarcore)==stellarcore_limit||a.getInt(crystallized)==crystallized_limit) {
            builder.add(Component.translatable("item.momotinker.tooltip.other").withStyle(ChatFormatting.GRAY));
        }
        Iterator var7 = tool.getModifierList().iterator();
        while(var7.hasNext()) {
            ModifierEntry entry = (ModifierEntry)var7.next();
            entry.getHook(ModifierHooks.TOOLTIP).addTooltip(tool, entry, player, tooltips, key, tooltipFlag);
        }
        return tooltips;
    }
}