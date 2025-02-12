package com.momosensei.momotinker.tool;


import com.momosensei.momotinker.entity.TriggerSlashEntity;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.servertoplay.TriggerBladeCharge;
import com.momosensei.momotinker.network.packet.servertoplay.triggerSlashPacket;
import com.momosensei.momotinker.register.MomotinkerEntities;
import com.momosensei.momotinker.register.MomotinkerItem;
import com.momosensei.momotinker.register.MomotinkerToolDefinitions;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.Iterator;
import java.util.List;

import static slimeknights.tconstruct.TConstruct.RANDOM;
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
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int chargeRemaining) {
        if ( living instanceof ServerPlayer player) {
            float perc = Mth.clamp((float) (this.getUseDuration(stack) - chargeRemaining) / 30,0,1);
            Channel.sendToPlayer(new TriggerBladeCharge(perc),player);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int duration) {
        if (livingEntity instanceof ServerPlayer player) {
            int i = this.getUseDuration(stack) - duration;
            if (i >= 30) {
                Channel.INSTANCE.sendToServer(new triggerSlashPacket(player.getId()));
            }
            Channel.sendToPlayer(new TriggerBladeCharge(0),player);
        }
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        ToolStack tool = ToolStack.from(stack);
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

    public static void createSlash(ServerPlayer player){
        if (!(player.getMainHandItem().getItem() instanceof trigger_blade)||player.getAttackStrengthScale(0)!=1||!checkOffHand(player)){
            return;
        }
        ToolStack tool = ToolStack.from(player.getMainHandItem());
        if (tool.isBroken()){
            return;
        }
        float damage = getDamageMultiplier(tool);
        ItemStack color = getSlash(tool.getStats().getInt(MomotinkerToolDefinitions.SLASH_COLOR));
        Level level =player.level();
        EntityType<TriggerSlashEntity> entityType = getSlashType(tool.getStats().getInt(MomotinkerToolDefinitions.SLASH_COLOR));
        TriggerSlashEntity slash =new TriggerSlashEntity(entityType,level,color);
        double x =player.getLookAngle().x;
        double y =player.getLookAngle().y;
        double z =player.getLookAngle().z;
        slash.damage=damage;
        slash.setOwner(player);
        slash.setToolstack(tool);
        slash.noPhysics = false;
        slash.setDeltaMovement(player.getLookAngle());
        slash.setPos(player.getX()+x*2,player.getY()+0.7*player.getBbHeight()+y*1.5,player.getZ()+z*2);
        level.addFreshEntity(slash);
        ToolDamageUtil.damageAnimated(tool,1,player, InteractionHand.MAIN_HAND);
    }
    public static ItemStack getSlash(int index){
        return new ItemStack(MomotinkerItem.trigger_slash_a.get());
    }

    public static EntityType<TriggerSlashEntity> getSlashType(int index){
        return MomotinkerEntities.trigger_slash_a.get();
    }
    public static float getDamageMultiplier(ToolStack tool){
        float b = RANDOM.nextInt((int) (tool.getStats().get(ACCURACY)*100));
        return tool.getStats().get(ToolStats.ATTACK_DAMAGE)*(1F+0.005F*b+0.2F*tool.getStats().get(ToolStats.VELOCITY));
    }
    public static boolean checkOffHand(Player player){
        return player!=null&& !(player.getOffhandItem().getItem() instanceof IModifiable);
    }
    public List<Component> getStatInformation(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        tooltips = this.getTriggerBladeStats(tool, player, tooltips, key, tooltipFlag);
        return tooltips;
    }
    public List<Component> getTriggerBladeStats(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
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