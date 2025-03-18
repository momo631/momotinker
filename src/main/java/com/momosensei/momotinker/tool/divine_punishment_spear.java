package com.momosensei.momotinker.tool;


import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.entity.CleanseEntity;
import com.momosensei.momotinker.entity.MomotinkerEntitiesCreate;
import com.momosensei.momotinker.event.CleanseSpawnEvent;
import com.momosensei.momotinker.mobs.CoolTime;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.CoolTimeCharge;
import com.momosensei.momotinker.network.packet.SpearEntityPacket;
import com.momosensei.momotinker.network.packet.ToolsTimeCharge;
import com.momosensei.momotinker.register.MomotinkerConfig;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import com.momosensei.momotinker.register.MomotinkerTools;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
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

import static com.momosensei.momotinker.Modifiers.modifiers.BreakthroughStars.breakthroughstar;
import static slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook.KEY_DRAWTIME;

public class divine_punishment_spear extends ModifiableItem {
    public divine_punishment_spear(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST,this::onEntityDeath);
    }
    int sanctification_limit = MomotinkerConfig.sanctification_limit.get();
    int degenerate_limit = MomotinkerConfig.degenerate_limit.get();
    public static final ResourceLocation sanctification = Momotinker.getResource("sanctification");
    public static final ResourceLocation degenerate = Momotinker.getResource("degenerate");

    private void onEntityDeath(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player&&event.getEntity()!=null) {
            if (player.getItemBySlot(EquipmentSlot.MAINHAND).is(MomotinkerTools.divine_punishment_spear.get())&& ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.MAINHAND), MomotinkerModifiers.frombrilliance.getId())>0){
                ModDataNBT a = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
                if (event.getEntity().getMobType() == MobType.UNDEAD&&a.getFloat(sanctification)<sanctification_limit&&a.getFloat(degenerate)<degenerate_limit){
                    a.putFloat(sanctification, a.getFloat(sanctification) + 1);
                }
                if (event.getEntity() instanceof Villager&&a.getFloat(sanctification)<sanctification_limit&&a.getFloat(degenerate)<degenerate_limit){
                    a.putFloat(degenerate,a.getFloat(degenerate) + 1);
                }
            }
        }
    }

    private void livinghurtevent(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        int sanctification_limit = MomotinkerConfig.sanctification_limit.get();
        int degenerate_limit = MomotinkerConfig.degenerate_limit.get();
        if (b instanceof Player player&&a!=null&&player.getMainHandItem().is(MomotinkerTools.divine_punishment_spear.get())){
            ModDataNBT c = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
            if (!checkOffHand(player)) {
                event.setAmount(0.5F * event.getAmount());
            }
            if (ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.MAINHAND), MomotinkerModifiers.frombrilliance.getId())>0) {
                if (c.getFloat(sanctification) == sanctification_limit) {
                    a.hurt(LegacyDamageSource.indirectMagic(player).setBypassMagic(), event.getAmount() * 0.25F);
                }
                if (c.getFloat(degenerate) == degenerate_limit) {
                    if (player.getItemBySlot(EquipmentSlot.MAINHAND).getDamageValue() == 0) {
                        player.heal(event.getAmount() * 0.5F);
                    }
                    if (player.getItemBySlot(EquipmentSlot.MAINHAND).getDamageValue() > 0 && player.getItemBySlot(EquipmentSlot.MAINHAND).getDamageValue() != 0) {
                        player.getItemBySlot(EquipmentSlot.MAINHAND).setDamageValue((int) (player.getItemBySlot(EquipmentSlot.MAINHAND).getDamageValue() - (event.getAmount() * 0.01F)));
                    }
                }
            }
        }
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
            stack.hurtAndBreak(1, entity, (entity1) -> {
                entity1.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        return true;
    }
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ToolStack tool = ToolStack.from(stack);
        int a = ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.breakthroughstars.getId());
        int b = ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.cleansetheworld.getId());
        if (a==0&&b==0) {
            tool.getPersistentData().putInt(KEY_DRAWTIME, 20);
        }else
        if (a>0&&b==0) {
            tool.getPersistentData().putInt(KEY_DRAWTIME, 10);
        }else
        if (b>0) {
            tool.getPersistentData().putInt(KEY_DRAWTIME, 100);
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
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int duration) {
        ScopeModifier.stopScoping(livingEntity);
        ToolStack tool = ToolStack.from(stack);
        int i = this.getUseDuration(stack) - duration;
        if (tool.isBroken()){
            tool.getPersistentData().remove(KEY_DRAWTIME);
            if (livingEntity instanceof ServerPlayer player){
                Channel.sendToPlayer(new ToolsTimeCharge(0), player);
            }
            return;
        }
        if (CoolTime.getCoolTime() != 0) {
            tool.getPersistentData().remove(KEY_DRAWTIME);
            if (livingEntity instanceof ServerPlayer player){
                Channel.sendToPlayer(new ToolsTimeCharge(0), player);
            }
            return;
        }
        if (livingEntity instanceof Player player) {
            int a = ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.breakthroughstars.getId());
            int b = ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.cleansetheworld.getId());
            int c = ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.shadowofdamnation.getId());
            player.awardStat(Stats.ITEM_USED.get(this));
            if (a==0&&i >= 20){
                player.hasImpulse = true;
                player.startAutoSpinAttack(2);
                player.setDeltaMovement(player.getLookAngle().scale(4));
                player.invulnerableTime = 20;
                player.fallDistance = 0;
            }
            ToolDamageUtil.damageAnimated(tool,1,player);
            if (livingEntity instanceof ServerPlayer player1){
                Channel.sendToPlayer(new ToolsTimeCharge(0), player1);
                if (a>0&&b==0&&i>=10){
                    player.giveExperiencePoints(-tool.getPersistentData().getInt(breakthroughstar));
                    Channel.INSTANCE.sendToServer(new SpearEntityPacket(player.getId()));
                }
                if (a>0&&b>0&&i>=100) {
                    player.giveExperiencePoints(-tool.getPersistentData().getInt(breakthroughstar)*5);
                    Vec2 pos = new Vec2((float) (player.getX()), (float) (player.getZ()));
                    CleanseSpawnEvent event1 = new CleanseSpawnEvent(new Vec3(pos.x, player.getY() + 100, pos.y));
                    MinecraftForge.EVENT_BUS.post(event1);
                    if (!event1.isCanceled()) {
                        double x =player.getLookAngle().x;
                        double z =player.getLookAngle().z;
                        CleanseEntity entity = new CleanseEntity(level, pos.x, player.getY() + 100, pos.y, new Vec3(x*0.2,0,z*0.2));
                        entity.noPhysics = true;
                        entity.setOwner(player);
                        entity.setToolstack(tool);
                        entity.setint(c);
                        entity.damage = MomotinkerEntitiesCreate.getDamageMultiplier(tool) * 50;
                        entity.setExplosionPower((byte) 120);
                        level.addFreshEntity(entity);
                    }
                    int d = MomotinkerConfig.cleansetheworld_limit.get();
                    Channel.sendToClient(new CoolTimeCharge(d));
                }
            }
        }
        tool.getPersistentData().remove(KEY_DRAWTIME);
    }
    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int chargeRemaining) {
        if (living instanceof ServerPlayer player) {
            int a = ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.breakthroughstars.getId());
            int b = ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.cleansetheworld.getId());
            if (a==0&&b==0) {
                float perc = Mth.clamp((float) (this.getUseDuration(stack) - chargeRemaining) / 20, 0, 1);
                Channel.sendToPlayer(new ToolsTimeCharge(perc), player);
            }else
            if (a>0&&b==0) {
                float perc = Mth.clamp((float) (this.getUseDuration(stack) - chargeRemaining) / 10, 0, 1);
                Channel.sendToPlayer(new ToolsTimeCharge(perc), player);
            }else
            if (b>0) {
                float perc = Mth.clamp((float) (this.getUseDuration(stack) - chargeRemaining) / 100, 0, 1);
                Channel.sendToPlayer(new ToolsTimeCharge(perc), player);
            }
        }
    }
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return BlockingModifier.blockWhileCharging(ToolStack.from(stack), UseAnim.SPEAR);
    }
    public static boolean checkOffHand(Player player){
        return player!=null&& !player.hasItemInSlot(EquipmentSlot.OFFHAND);
    }
    public List<Component> getStatInformation(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        tooltips = this.getDivinePunishmentSpearStats(tool, player, tooltips, key, tooltipFlag);
        return tooltips;
    }
    public List<Component> getDivinePunishmentSpearStats(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
        ModDataNBT a = tool.getPersistentData();
        if (tool.hasTag(TinkerTags.Items.DURABILITY)) {
            builder.add(ToolStats.DURABILITY);
        }
        if (tool.hasTag(TinkerTags.Items.MELEE)) {
            builder.add(ToolStats.ATTACK_DAMAGE);
            builder.add(ToolStats.ATTACK_SPEED);
        }
        builder.addAllFreeSlots();
        if (a.getFloat(sanctification)<sanctification_limit&&a.getFloat(degenerate)<degenerate_limit) {
            builder.add(Component.translatable("古代神兵任务:击杀"+sanctification_limit+"只亡灵生物。当前击杀数为" + a.getFloat(sanctification)).withStyle(ChatFormatting.GOLD));
        }
        if (a.getFloat(sanctification)==sanctification_limit) {
            builder.add(Component.translatable("古代神兵任务已完成！此工具将格外造成25%魔法伤害且此伤害无视魔法防御").withStyle(ChatFormatting.YELLOW));
        }
        if (a.getFloat(degenerate)==degenerate_limit) {
            builder.add(Component.translatable("古代神兵隐藏任务已完成！此工具造成伤害会恢复耐久，若耐久为满则恢复使用者生命").withStyle(ChatFormatting.DARK_RED));
        }
        if (CoolTime.getCoolTime()!=0){
            builder.add(Component.translatable("“荡涤天地”冷却还剩"+CoolTime.getCoolTime()+"秒").withStyle(ChatFormatting.YELLOW));
        }
        if (!checkOffHand(player)){
            builder.add(Component.translatable("momotinker.tool.tooltip.offhand_hastool").withStyle(ChatFormatting.RED));
        }

        Iterator var7 = tool.getModifierList().iterator();
        while(var7.hasNext()) {
            ModifierEntry entry = (ModifierEntry)var7.next();
            ((TooltipModifierHook)entry.getHook(ModifierHooks.TOOLTIP)).addTooltip(tool, entry, player, tooltips, key, tooltipFlag);
        }
        return tooltips;
    }
}