package com.momosensei.momotinker.tool;


import com.momosensei.momotinker.entity.LegionEntity.LegionEntity;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.HudCharge.LegionChargingCharge;
import com.momosensei.momotinker.network.packet.HudCharge.LegionCooldownCharge;
import com.momosensei.momotinker.network.packet.LegionPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.modifiers.ability.interaction.BlockingModifier;
import slimeknights.tconstruct.tools.modifiers.upgrades.ranged.ScopeModifier;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.momosensei.momotinker.Momotinker.getResource;
import static com.momosensei.momotinker.entity.MomotinkerEntitiesCreate.getboxType;
import static com.momosensei.momotinker.util.AttackUtil.getCooldownFunctionFloat;
import static slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook.KEY_DRAWTIME;

public class legion extends ModifiableItem {
    public legion(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
        MinecraftForge.EVENT_BUS.addListener(this::LeftClick);
        MinecraftForge.EVENT_BUS.addListener(this::LeftClickBlock);
    }

    public static void createLegion(ServerPlayer player,int quantity,int form) {
        if (!(ToolStack.from(player.getMainHandItem()).getItem() instanceof legion) || player.getAttackStrengthScale(0) != 1 ) {
            return;
        }
        ToolStack tool=ToolStack.from(player.getMainHandItem());
        if (tool.isBroken()) {
            return;
        }
        Level level = player.getLevel();
        EntityType<LegionEntity> entityType = getboxType();

        Random random = new Random();
        double minDistance = 1;
        double maxDistance = 3;
        if (form==0){
            minDistance=0.8;
            maxDistance=2;
        }
        for (int i = 0; i < quantity; i++) {
            LegionEntity legion = new LegionEntity(entityType, level);
            legion.setOwner(player);
            legion.noPhysics = true;
            legion.setForm(form);
            legion.setSpawnRotation(player.getYRot(), player.getXRot());

            double x, y, z;

            if (form == 2) {
                double radius = 2.0;
                double angleStep = 2 * Math.PI / quantity;
                double currentAngle = i * angleStep;

                x = player.getX() + Math.cos(currentAngle) * radius;
                y = player.getY() + 0.7 * player.getBbHeight();
                z = player.getZ() + Math.sin(currentAngle) * radius;

                legion.setPos(x, y, z);
                legion.getPersistentData().putInt("OrbitIndex", i);
                legion.getPersistentData().putInt("TotalOrbiters", quantity);
                legion.getPersistentData().putDouble("BaseOrbitAngle", 0);
            } else {
                double distance = minDistance + random.nextDouble() * (maxDistance - minDistance);
                double angle = random.nextDouble() * 2 * Math.PI;

                x = player.getX() + Math.cos(angle) * distance;
                y = player.getY() + 0.7 * player.getBbHeight() + random.nextDouble() * 2;
                z = player.getZ() + Math.sin(angle) * distance;

                legion.setPos(x, y, z);
            }
            if (level instanceof ServerLevel level1){
                level1.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 1, 0, 0, 0, 1);
            }
            level.addFreshEntity(legion);
        }
        ToolDamageUtil.damageAnimated(tool, 1, player, InteractionHand.MAIN_HAND);
    }
    public static final ResourceLocation legion_cooldown = getResource("legion_cooldown");
    public static final ResourceLocation legion_on = getResource("legion_on");
    private final Map<UUID, Integer> stageCache = new ConcurrentHashMap<>();

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        InventoryTickModifierHook.heldInventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        ModDataNBT data = ToolStack.from(stack).getPersistentData();
        if (entityIn instanceof Player player && player.tickCount % 20 == 0) {
            if (data.getBoolean(legion_on)) {
                if (data.getFloat(legion_cooldown) > 0) {
                    data.putFloat(legion_cooldown, data.getFloat(legion_cooldown) - 5);
                } else if (data.getFloat(legion_cooldown) == 0) {
                    data.putBoolean(legion_on, false);
                }
            } else {
                if (data.getFloat(legion_cooldown) < 60) {
                    data.putFloat(legion_cooldown, data.getFloat(legion_cooldown) + 1);
                }
            }
            if (data.getFloat(legion_cooldown) < 0) {
                data.putFloat(legion_cooldown, 0);
            }
            if (data.getFloat(legion_cooldown) > 60) {
                data.putFloat(legion_cooldown, 60);
            }
        }

        float perc = Mth.clamp(data.getFloat(legion_cooldown) / 60, 0, 1);
        int currentStage = (int)Math.floor(perc * 8);
        if (entityIn instanceof ServerPlayer player1&&stack == player1.getMainHandItem()) {
            UUID playerId = player1.getUUID();
            if (stageCache.getOrDefault(playerId, -1) != currentStage) {
                Channel.sendToPlayer(new LegionCooldownCharge(perc), player1);
                stageCache.put(playerId, currentStage);
            }
        }
    }

    private void LeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        Player player=event.getEntity();
        if (player != null && player.getMainHandItem().getItem() instanceof legion) {
            Channel.sendToServer(new LegionPacket(player.getId()));
        }
    }
    private void LeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player=event.getEntity();
        if (player instanceof ServerPlayer serverPlayer&&player.getMainHandItem().getItem() instanceof legion) {
            float d = getCooldownFunctionFloat(serverPlayer, InteractionHand.MAIN_HAND);
            if (d>0.9f) {
                createLegion(serverPlayer, 1, 0);
            }
        }
    }
    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity target) {
        if (player instanceof ServerPlayer serverPlayer){
            float d = getCooldownFunctionFloat(serverPlayer, InteractionHand.MAIN_HAND);
            if (d>0.9f) {
                createLegion(serverPlayer, 1, 0);
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
            float perc = Mth.clamp((float) i / (240 / tool.getStats().get(ToolStats.ATTACK_SPEED)),0,1);
            if (perc>=0.2f) {
                int a = (int) Math.floor(perc * 5);
                createLegion(player, a, 1);
            }
            Channel.sendToPlayer(new LegionChargingCharge(0,0), player);
            player.awardStat(Stats.ITEM_USED.get(this));
            ToolDamageUtil.damageAnimated(tool, 1, player);
        }
        tool.getPersistentData().remove(KEY_DRAWTIME);
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int chargeRemaining) {
        if ( living instanceof ServerPlayer player) {
            int i = this.getUseDuration(stack) - chargeRemaining;
            float phase = Mth.clamp((float) i / (240 / ToolStack.from(stack).getStats().get(ToolStats.ATTACK_SPEED)),0,1);
            float progress = (phase%0.2F)*5F;
            if (phase>=1F)progress=1F;
            Channel.sendToPlayer(new LegionChargingCharge(phase,progress), player);
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
        return BlockingModifier.blockWhileCharging(ToolStack.from(stack), UseAnim.BOW);
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