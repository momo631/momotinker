package com.momosensei.momotinker.tool;


import com.momosensei.momotinker.register.MomotinkerTools;
import com.momosensei.momotinker.util.attackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.Util;
import slimeknights.tconstruct.tools.modifiers.ability.interaction.BlockingModifier;
import slimeknights.tconstruct.tools.modifiers.upgrades.ranged.ScopeModifier;

import java.util.Iterator;
import java.util.List;

import static com.momosensei.momotinker.Modifiers.modifiers.ProjectionOfSuffering.disaster;
import static slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook.KEY_DRAWTIME;

public class eclipse_container extends ModifiableItem {
    public eclipse_container(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }
    private void livinghurtevent(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player&&a!=null&&player.getMainHandItem().is(MomotinkerTools.eclipse_container.get())){
            if (!checkOffHand(player)) {
                event.setAmount(0.1F * event.getAmount());
            }
        }
        if (a instanceof Player player&& player.getUseItem().is(MomotinkerTools.eclipse_container.get())){
            ModDataNBT c = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
            if (c.getInt(disaster)>0) {
                event.setAmount(event.getAmount() * 0.5F);
            }
        }
    }
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ToolStack tool = ToolStack.from(stack);
        tool.getPersistentData().putInt(KEY_DRAWTIME,60);
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
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int chargeRemaining) {
        ToolStack tool = ToolStack.from(stack);
        if (living instanceof ServerPlayer player) {
            if (!checkOffHand(player)){
                return;
            }
            int t = this.getUseDuration(stack) - chargeRemaining;
            float perc = Mth.clamp((float) t / 60,0,1);
            if (tool.getPersistentData().getInt(disaster)>0&&perc >= 1){
                int a = (int)(tool.getStats().get(ToolStats.ATTACK_SPEED)*2+5);
                tool.getPersistentData().putInt(disaster, tool.getPersistentData().getInt(disaster) - 2);
                if (a<30&&a>=1) {
                    List<Entity> list = player.level().getEntitiesOfClass(Entity.class, player.getBoundingBox().inflate(a));
                    List<Entity> list1 = player.level().getEntitiesOfClass(Entity.class, player.getBoundingBox().inflate(a-0.5));
                    for (Entity entity : list) {
                        if (entity != null && entity != player) {
                            Vec3 vec = entity.position().subtract(living.position()).normalize().scale(-0.15);
                            entity.push(vec.x, vec.y, vec.z);
                        }
                    }
                    if (player.tickCount%20==1) {
                        for (Entity entity : list1) {
                            if (entity instanceof LivingEntity && entity != player) {
                                attackUtil.attackEntity(tool, player, InteractionHand.MAIN_HAND, entity, () -> 1, true, Util.getSlotType(InteractionHand.MAIN_HAND), tool.getStats().get(ToolStats.ATTACK_DAMAGE) * 0.2f, false, true, false, true);
                            }
                        }
                    }
                    if (player.level() instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.DRAGON_BREATH, player.getX(), player.getY()+player.getBbHeight()*0.5f, player.getZ(), (int)(a*0.6F)+22, a, a, a, 0.5);
                    }
                }else if (a>=30) {
                    List<Entity> list = player.level().getEntitiesOfClass(Entity.class, player.getBoundingBox().inflate(30));
                    List<Entity> list1 = player.level().getEntitiesOfClass(Entity.class, player.getBoundingBox().inflate(29.5));
                    for (Entity entity : list) {
                        if (entity != null && entity != player) {
                            Vec3 vec = entity.position().subtract(living.position()).normalize().scale(-0.15);
                            entity.push(vec.x, vec.y, vec.z);
                        }
                    }
                    if (player.tickCount%20==1) {
                        for (Entity entity : list1) {
                            if (entity instanceof LivingEntity && entity != player) {
                                attackUtil.attackEntity(tool, player, InteractionHand.MAIN_HAND, entity, () -> 1, true, Util.getSlotType(InteractionHand.MAIN_HAND), tool.getStats().get(ToolStats.ATTACK_DAMAGE) * 0.2f, false, true, false, true);
                            }
                        }
                    }
                    if (player.level() instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.DRAGON_BREATH, player.getX(), player.getY()+player.getBbHeight()*0.5f, player.getZ(), 40, 30, 30, 30, 0.5);
                    }
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
            player.awardStat(Stats.ITEM_USED.get(this));
            ToolDamageUtil.damageAnimated(tool,1,player);
            tool.getPersistentData().remove(KEY_DRAWTIME);
        }
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
    public static boolean checkOffHand(Player player){
        return player!=null&& !player.hasItemInSlot(EquipmentSlot.OFFHAND);
    }
    public List<Component> getStatInformation(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        tooltips = this.getDivinePunishmentSpearStats(tool, player, tooltips, key, tooltipFlag);
        return tooltips;
    }
    public List<Component> getDivinePunishmentSpearStats(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
        if (tool.hasTag(TinkerTags.Items.DURABILITY)) {
            builder.add(ToolStats.DURABILITY);
        }
        if (tool.hasTag(TinkerTags.Items.MELEE)) {
            builder.add(ToolStats.ATTACK_DAMAGE);
            builder.add(ToolStats.ATTACK_SPEED);
        }
        if (!checkOffHand(player)){
            builder.add(Component.translatable("momotinker.tool.tooltip.offhand_hastool").withStyle(ChatFormatting.RED));
        }
        builder.add(Component.translatable("item.momotinker.tooltip.eclipse_container1").append((int)(tool.getStats().get(ToolStats.ATTACK_SPEED)*2+5)+"").withStyle(ChatFormatting.DARK_GRAY));
        builder.addAllFreeSlots();
        Iterator var7 = tool.getModifierList().iterator();
        while(var7.hasNext()) {
            ModifierEntry entry = (ModifierEntry)var7.next();
            entry.getHook(ModifierHooks.TOOLTIP).addTooltip(tool, entry, player, tooltips, key, tooltipFlag);
        }
        return tooltips;
    }
}