package com.momosensei.momotinker.tool;


import com.momosensei.momotinker.entity.Boxentity.BoxEntity;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.BoxPacket;
import com.momosensei.momotinker.network.packet.ToolsTimeCharge;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.modifiers.ability.interaction.BlockingModifier;
import slimeknights.tconstruct.tools.modifiers.upgrades.ranged.ScopeModifier;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static com.momosensei.momotinker.entity.MomotinkerEntitiesCreate.getboxType;
import static com.momosensei.momotinker.util.AttackUtil.getCooldownFunctionFloat;
import static slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook.KEY_DRAWTIME;

public class box extends ModifiableItem {
    public box(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
        MinecraftForge.EVENT_BUS.addListener(this::LeftClick);
        MinecraftForge.EVENT_BUS.addListener(this::LeftClickBlock);
    }

    public static void createBox(ServerPlayer player,int quantity,int form) {
        if (!(ToolStack.from(player.getMainHandItem()).getItem() instanceof box) || player.getAttackStrengthScale(0) != 1 ) {
            return;
        }
        ToolStack tool=ToolStack.from(player.getMainHandItem());
        if (tool.isBroken()) {
            return;
        }
        Level level = player.getLevel();
        EntityType<BoxEntity> entityType = getboxType();

        Random random = new Random();
        double minDistance = 1;
        double maxDistance = 3;
        if (form==0){
            minDistance=0.8;
            maxDistance=2;
        }
        for (int i = 0; i < quantity; i++) {
            BoxEntity box = new BoxEntity(entityType, level);
            box.setOwner(player);
            box.noPhysics = true;
            box.setForm(form);
            box.setSpawnRotation(player.getYRot(), player.getXRot());

            double x, y, z;

            if (form == 2) {
                double radius = 2.0;
                double angleStep = 2 * Math.PI / quantity;
                double currentAngle = i * angleStep;

                x = player.getX() + Math.cos(currentAngle) * radius;
                y = player.getY() + 0.7 * player.getBbHeight();
                z = player.getZ() + Math.sin(currentAngle) * radius;

                box.setPos(x, y, z);
                box.getPersistentData().putInt("OrbitIndex", i);
                box.getPersistentData().putInt("TotalOrbiters", quantity);
                box.getPersistentData().putDouble("BaseOrbitAngle", 0);
            } else {
                double distance = minDistance + random.nextDouble() * (maxDistance - minDistance);
                double angle = random.nextDouble() * 2 * Math.PI;

                x = player.getX() + Math.cos(angle) * distance;
                y = player.getY() + 0.7 * player.getBbHeight() + random.nextDouble() * 2;
                z = player.getZ() + Math.sin(angle) * distance;

                box.setPos(x, y, z);
            }
            level.addFreshEntity(box);
        }
        ToolDamageUtil.damageAnimated(tool, 1, player, InteractionHand.MAIN_HAND);
    }
//    @Override
//    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
//        boolean retval = super.onEntitySwing(stack, entity);
//        if (entity instanceof ServerPlayer player&&player.getMainHandItem().getItem() instanceof box) {
//            float d = getCooldownFunctionFloat(player, InteractionHand.MAIN_HAND);
//            if (d>0.9f) {
//                createBox(player, 1, 0);
//            }
//        }
//        return retval;
//    }

    private void LeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        Player player=event.getEntity();
        if (player != null && player.getMainHandItem().getItem() instanceof box) {
            Channel.sendToServer(new BoxPacket(player.getId()));
        }
    }
    private void LeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player=event.getEntity();
        if (player instanceof ServerPlayer serverPlayer&&player.getMainHandItem().getItem() instanceof box) {
            float d = getCooldownFunctionFloat(serverPlayer, InteractionHand.MAIN_HAND);
            if (d>0.9f) {
                createBox(serverPlayer, 1, 0);
            }
        }
    }
    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity target) {
        if (player instanceof ServerPlayer serverPlayer){
            float d = getCooldownFunctionFloat(serverPlayer, InteractionHand.MAIN_HAND);
            if (d>0.9f) {
                createBox(serverPlayer, 1, 0);
            }
        }
        return super.onLeftClickEntity(stack, player, target);
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
            int i = this.getUseDuration(stack) - duration;
            float perc = Mth.clamp((float) i / (120 / tool.getStats().get(ToolStats.ATTACK_SPEED)),0,1);
            if (perc>=0.25f) {
                int a = (int) Math.floor(perc * 4);
                createBox(player, a + 1, 1);
                Channel.sendToPlayer(new ToolsTimeCharge(0), player);
                player.awardStat(Stats.ITEM_USED.get(this));
                ToolDamageUtil.damageAnimated(tool, 1, player);
            }
        }
        tool.getPersistentData().remove(KEY_DRAWTIME);
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int chargeRemaining) {
        if ( living instanceof ServerPlayer player) {
            int i = this.getUseDuration(stack) - chargeRemaining;
            float perc = Mth.clamp((float) i / (120 / ToolStack.from(stack).getStats().get(ToolStats.ATTACK_SPEED)),0,1);
            Channel.sendToPlayer(new ToolsTimeCharge(perc), player);
        }
    }
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

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return BlockingModifier.blockWhileCharging(ToolStack.from(stack), UseAnim.BLOCK);
    }
    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        return !player.isCreative();
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
    public List<Component> getStatInformation(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        tooltips = this.getStats(tool, player, tooltips, key, tooltipFlag);
        return tooltips;
    }
    public List<Component> getStats(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
        builder.add(ToolStats.DURABILITY);
        builder.add(ToolStats.ATTACK_DAMAGE);
        builder.add(ToolStats.ATTACK_SPEED);
        builder.addAllFreeSlots();
        Iterator var7 = tool.getModifierList().iterator();
        while(var7.hasNext()) {
            ModifierEntry entry = (ModifierEntry)var7.next();
            entry.getHook(ModifierHooks.TOOLTIP).addTooltip(tool, entry, player, tooltips, key, tooltipFlag);
        }
        return tooltips;
    }
}