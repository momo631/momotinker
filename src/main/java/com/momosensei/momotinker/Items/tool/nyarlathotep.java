package com.momosensei.momotinker.Items.tool;


import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.entity.NyarlathotepEntity.NyarlathotepEntity;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.HudCharge.NyarlathotepChargingCharge;
import com.momosensei.momotinker.network.packet.HudCharge.NyarlathotepCooldownCharge;
import com.momosensei.momotinker.network.packet.NyarlathotepPacket;
import com.momosensei.momotinker.register.MomotinkerTags;
import com.momosensei.momotinker.util.AttackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.MaterialNBT;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.ToolDefinitions;
import slimeknights.tconstruct.tools.modifiers.ability.interaction.BlockingModifier;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.momosensei.momotinker.Momotinker.getResource;
import static com.momosensei.momotinker.entity.MomotinkerEntitiesCreate.getNyarlathotepType;
import static com.momosensei.momotinker.util.AttackUtil.getCooldownFunctionFloat;
import static slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook.KEY_DRAWTIME;

public class nyarlathotep extends ModifiableItem {
    public nyarlathotep(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
        MinecraftForge.EVENT_BUS.addListener(this::LeftClick);
        MinecraftForge.EVENT_BUS.addListener(this::LeftClickBlock);
    }

    public static void createNyarlathotep(ServerPlayer player,int quantity,int form) {
        if (!(ToolStack.from(player.getMainHandItem()).getItem() instanceof nyarlathotep) || player.getAttackStrengthScale(0) != 1 ) {
            return;
        }
        ToolStack tool=ToolStack.from(player.getMainHandItem());
        if (tool.isBroken()) {
            return;
        }
        Level level = player.level();
        EntityType<NyarlathotepEntity> entityType = getNyarlathotepType();

        Random random = new Random();
        double minDistance = 1;
        double maxDistance = 3;
        if (form==0){
            minDistance=0.8;
            maxDistance=2;
        }
        for (int i = 0; i < quantity; i++) {
            NyarlathotepEntity nyarlathotep = new NyarlathotepEntity(entityType, level);
            nyarlathotep.setOwner(player);
            nyarlathotep.noPhysics = true;
            nyarlathotep.setForm(form);
            nyarlathotep.setSpawnRotation(player.getYRot(), player.getXRot());

            double x, y, z;

            if (form == 2) {
                double radius = 2.0;
                double angleStep = 2 * Math.PI / quantity;
                double currentAngle = i * angleStep;

                x = player.getX() + Math.cos(currentAngle) * radius;
                y = player.getY() + 0.7 * player.getBbHeight();
                z = player.getZ() + Math.sin(currentAngle) * radius;

                nyarlathotep.setPos(x, y, z);
                nyarlathotep.getPersistentData().putInt("OrbitIndex", i);
                nyarlathotep.getPersistentData().putInt("TotalOrbiters", quantity);
                nyarlathotep.getPersistentData().putDouble("BaseOrbitAngle", 0);
            } else {
                double distance = minDistance + random.nextDouble() * (maxDistance - minDistance);
                double angle = random.nextDouble() * 2 * Math.PI;

                x = player.getX() + Math.cos(angle) * distance;
                y = player.getY() + 0.7 * player.getBbHeight() + random.nextDouble() * 2;
                z = player.getZ() + Math.sin(angle) * distance;

                nyarlathotep.setPos(x, y, z);
            }
            if (level instanceof ServerLevel level1){
                level1.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 1, 0, 0, 0, 1);
            }
            level.addFreshEntity(nyarlathotep);
        }
        ToolDamageUtil.damageAnimated(tool, 1, player, InteractionHand.MAIN_HAND);
    }
    public static final ResourceLocation nyarlathotep_cooldown = getResource("nyarlathotep_cooldown");
    public static final ResourceLocation nyarlathotep_on = getResource("nyarlathotep_on");
    private final Map<UUID, Integer> stageCache = new ConcurrentHashMap<>();
    public static final ResourceLocation nyarlathotep_disguise = Momotinker.getResource("nyarlathotep_disguise");
    public static final ResourceLocation nyarlathotep_disguise_tool = Momotinker.getResource("nyarlathotep_disguise_tool");

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        InventoryTickModifierHook.heldInventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        ModDataNBT data = ToolStack.from(stack).getPersistentData();
        Random random = new Random();
        if (entityIn instanceof Player player && player.tickCount % 20 == 0) {
            if (data.getBoolean(nyarlathotep_on)) {
                if (data.getFloat(nyarlathotep_cooldown) > 0) {
                    data.putFloat(nyarlathotep_cooldown, data.getFloat(nyarlathotep_cooldown) - 5);
                } else if (data.getFloat(nyarlathotep_cooldown) == 0) {
                    data.putBoolean(nyarlathotep_on, false);
                }
            } else {
                if (data.getFloat(nyarlathotep_cooldown) < 60) {
                    data.putFloat(nyarlathotep_cooldown, data.getFloat(nyarlathotep_cooldown) + 1);
                }
            }
            if (data.getFloat(nyarlathotep_cooldown) < 0) {
                data.putFloat(nyarlathotep_cooldown, 0);
            }
            if (data.getFloat(nyarlathotep_cooldown) > 60) {
                data.putFloat(nyarlathotep_cooldown, 60);
            }

            if (data.getFloat(nyarlathotep_disguise)==0 && random.nextInt(100) <= 1) {
                startDisguise(ToolStack.from(stack), 60);
            }
            if (data.getFloat(nyarlathotep_disguise)>0){
                data.putFloat(nyarlathotep_disguise,data.getFloat(nyarlathotep_disguise)-1);
            }
            if (data.getFloat(nyarlathotep_disguise) < 0) {
                data.putFloat(nyarlathotep_disguise, 0);
            }
        }

        float perc = Mth.clamp(data.getFloat(nyarlathotep_cooldown) / 60, 0, 1);
        int currentStage = (int)Math.floor(perc * 8);
        if (entityIn instanceof ServerPlayer player1&&stack == player1.getMainHandItem()) {
            UUID playerId = player1.getUUID();
            if (stageCache.getOrDefault(playerId, -1) != currentStage) {
                Channel.sendToPlayer(new NyarlathotepCooldownCharge(perc), player1);
                stageCache.put(playerId, currentStage);
            }
        }
    }

    private void LeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        Player player=event.getEntity();
        if (player != null && player.getMainHandItem().getItem() instanceof nyarlathotep) {
            Channel.sendToServer(new NyarlathotepPacket(player.getId()));
        }
    }
    private void LeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player=event.getEntity();
        if (player instanceof ServerPlayer serverPlayer&&player.getMainHandItem().getItem() instanceof nyarlathotep) {
            float d = getCooldownFunctionFloat(serverPlayer, InteractionHand.MAIN_HAND);
            if (d>0.9f) {
                createNyarlathotep(serverPlayer, 1, 0);
            }
        }
    }
    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity target) {
        if (player instanceof ServerPlayer serverPlayer){
            float d = getCooldownFunctionFloat(serverPlayer, InteractionHand.MAIN_HAND);
            if (d>0.9f) {
                createNyarlathotep(serverPlayer, 1, 0);
            }
        }
        return super.onLeftClickEntity(stack, player, target);
    }
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int duration) {
        AttackUtil.stopScoping(livingEntity);
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
                createNyarlathotep(player, a, 1);
            }
            Channel.sendToPlayer(new NyarlathotepChargingCharge(0,0), player);
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
            Channel.sendToPlayer(new NyarlathotepChargingCharge(phase,progress), player);
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

    public static void startDisguise(ToolStack originalStack, float duration) {
        ModDataNBT data = originalStack.getPersistentData();
        MaterialNBT materials = originalStack.getMaterials();

        ItemStack chosen = null;
        int maxAttempts = 20;
        for (int i = 0; i < maxAttempts; i++) {
            ToolStack result = NyarlathotepEntity.getRandomTools(materials);
            chosen = result.createStack();
            if (isValidToolStack(result)) {
                break;
            }
            if (i == maxAttempts - 1) {
                chosen = ToolStack.from(ToolStack.createTool(TinkerTools.sword.get(), ToolDefinitions.SWORD, materials).createStack()).createStack();
            }
        }

        data.put(nyarlathotep_disguise_tool, chosen.save(new CompoundTag()));
        data.putFloat(nyarlathotep_disguise, duration);
    }
    private static boolean isValidToolStack(ToolStack toolStack) {
        if (toolStack == null
                ||toolStack.getStats().get(ToolStats.ATTACK_DAMAGE)==0
                ||(!toolStack.hasTag(TinkerTags.Items.INTERACTABLE_RIGHT))
                ||toolStack.hasTag(MomotinkerTags.Items.LEGION)) return false;
        ItemStack itemStack = toolStack.createStack();
        return !itemStack.isEmpty() && itemStack.getItem() != Items.AIR;
    }
}
