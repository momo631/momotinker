package com.momosensei.momotinker.tool;


import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.HudCharge.ToolsTimeCharge;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.modifiers.ability.interaction.BlockingModifier;
import slimeknights.tconstruct.tools.modifiers.upgrades.ranged.ScopeModifier;

import java.util.Iterator;
import java.util.List;

import static com.momosensei.momotinker.Modifiers.modifiers.CrimsonQueen.crimsonlayers;
import static com.momosensei.momotinker.Modifiers.modifiers.CrimsonQueen.crimsontime;
import static com.momosensei.momotinker.entity.MomotinkerEntitiesCreate.createSlash;
import static slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook.KEY_DRAWTIME;
import static slimeknights.tconstruct.library.tools.stat.ToolStats.ACCURACY;

public class trigger_blade extends ModifiableItem {
    public trigger_blade(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
    }

    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        return !player.isCreative();
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return BlockingModifier.blockWhileCharging(ToolStack.from(stack), UseAnim.CUSTOM);
    }
    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int chargeRemaining) {
        if ( living instanceof ServerPlayer player) {
            int a = ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.crimsonqueen.getId());
            int b = ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.yamato.getId());
            int drawTime = (int) (50/ ConditionalStatModifierHook.getModifiedStat(ToolStack.from(stack),player,ToolStats.ATTACK_SPEED));
            int drawTime1 = (int) (25/ ConditionalStatModifierHook.getModifiedStat(ToolStack.from(stack),player,ToolStats.ATTACK_SPEED));

            if (a==0) {
                if (b==0) {
                    float perc = Mth.clamp((float) (this.getUseDuration(stack) - chargeRemaining) / drawTime, 0, 1);
                    Channel.sendToPlayer(new ToolsTimeCharge(perc), player);
                }
                if (b>0) {
                    float perc = Mth.clamp((float) (this.getUseDuration(stack) - chargeRemaining) / drawTime1, 0, 1);
                    Channel.sendToPlayer(new ToolsTimeCharge(perc), player);
                }
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int duration) {
        ScopeModifier.stopScoping(livingEntity);
        ToolStack tool = ToolStack.from(stack);
        if (tool.isBroken()){
            tool.getPersistentData().remove(KEY_DRAWTIME);
            return;
        }
        if (livingEntity instanceof ServerPlayer player) {
            ModDataNBT dataNBT = ToolStack.from(stack).getPersistentData();
            int i = this.getUseDuration(stack) - duration;
            int a = ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.crimsonqueen.getId());
            int b = ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.yamato.getId());
            int drawTime = (int) (50/ ConditionalStatModifierHook.getModifiedStat(ToolStack.from(stack),player,ToolStats.ATTACK_SPEED));
            int drawTime1 = (int) (25/ ConditionalStatModifierHook.getModifiedStat(ToolStack.from(stack),player,ToolStats.ATTACK_SPEED));

            if (a==0) {
                if (b==0&&i >= drawTime) {
                    createSlash(player);
                }else
                if (b>0&&i >= drawTime1){
                    createSlash(player);
                    if (i>drawTime1+4){
                        player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 25);
                    }
                }
                Channel.sendToPlayer(new ToolsTimeCharge(0),player);
            }
            if (a>0) {
                if ( i >= 0 && i <= 9) {
                    if (dataNBT.getFloat(crimsonlayers)<3){
                        dataNBT.putFloat(crimsonlayers, dataNBT.getFloat(crimsonlayers) + 1);
                        dataNBT.putFloat(crimsontime, 25);
                    }else
                    if (dataNBT.getFloat(crimsonlayers) == 3) {
                        dataNBT.putFloat(crimsontime, 25);
                    }
                }
                if ( i >= 3 && i <= 6) {
                    dataNBT.putFloat(crimsonlayers, 3);
                    dataNBT.putFloat(crimsontime, 25);
                }
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            ToolDamageUtil.damageAnimated(tool,1,player);
        }
        tool.getPersistentData().remove(KEY_DRAWTIME);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ToolStack tool = ToolStack.from(stack);
        int a = ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.crimsonqueen.getId());
        int b = ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.yamato.getId());
        int drawTime = (int) (50/ ConditionalStatModifierHook.getModifiedStat(tool,player,ToolStats.ATTACK_SPEED));
        int drawTime1 = (int) (25/ ConditionalStatModifierHook.getModifiedStat(tool,player,ToolStats.ATTACK_SPEED));

        if (b==0&&a==0) {
            tool.getPersistentData().putInt(KEY_DRAWTIME, drawTime);
        }else
        if (b>0&&a==0) {
            tool.getPersistentData().putInt(KEY_DRAWTIME, drawTime1);
        }
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

    public boolean hurtEnemy(ItemStack stack, LivingEntity entity, LivingEntity player) {
        stack.hurtAndBreak(1, player, (player1) -> {
            player1.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    public boolean mineBlock(ItemStack stack, Level level, BlockState blockState, BlockPos blockPos, LivingEntity entity) {
        if ((double) blockState.getDestroySpeed(level, blockPos) != 0.0D) {
            stack.hurtAndBreak(2, entity, (entity1) -> {
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
        if (tool.hasTag(TinkerTags.Items.DURABILITY)) {
            builder.add(ToolStats.DURABILITY);
        }
        if (tool.hasTag(TinkerTags.Items.MELEE)) {
            builder.add(ToolStats.ATTACK_DAMAGE);
            builder.add(ToolStats.ATTACK_SPEED);
        }
            builder.add(ToolStats.VELOCITY);
            builder.add(ACCURACY);
            builder.addAllFreeSlots();

        if (!checkOffHand(player)){
            builder.add(Component.translatable("momotinker.tool.tooltip.offhand_hastool").withStyle(ChatFormatting.RED));
        }

        Iterator var7 = tool.getModifierList().iterator();
        while(var7.hasNext()) {
            ModifierEntry entry = (ModifierEntry)var7.next();
            entry.getHook(ModifierHooks.TOOLTIP).addTooltip(tool, entry, player, tooltips, key, tooltipFlag);
        }
        return tooltips;
    }
}