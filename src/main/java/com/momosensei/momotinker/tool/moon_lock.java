package com.momosensei.momotinker.tool;


import com.momosensei.momotinker.register.MomotinkerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.modifiers.ability.interaction.BlockingModifier;

import java.util.Iterator;
import java.util.List;

import static slimeknights.tconstruct.TConstruct.RANDOM;

public class moon_lock extends ModifiableItem {
    public moon_lock(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
        MinecraftForge.EVENT_BUS.addListener(this::onProjectileImpact);
    }

    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        return !player.isCreative();
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

    private void livinghurtevent(LivingHurtEvent event) {
        Entity b = event.getEntity();
        if (b instanceof Player player&& player.getUseItem().is(MomotinkerItem.moon_lock.get())){
            event.setAmount(event.getAmount()*0.5F);
        }
    }

    public void onProjectileImpact(ProjectileImpactEvent event) {
        if (event.getRayTraceResult() instanceof EntityHitResult result) {
            if (result.getEntity() instanceof Player player) {
                if (!player.level.isClientSide) {
                    boolean trigger = player.getUseItem().is(MomotinkerItem.moon_lock.get());
                    int a = RANDOM.nextInt(10);
                    if (trigger&&a<5) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int chargeRemaining) {
        if (living instanceof ServerPlayer player&&player.level instanceof ServerLevel serverLevel&&player.tickCount%10==0) {
            for (int i = 0; i <= 360; i++) {
                double rad = i * 0.017453292519943295;
                double r = 0.5D;
                double x = r * Math.cos(rad);
                double z = r * Math.sin(rad);
                serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, player.getX()+x, player.getY(), player.getZ()+z, 1/2, 0, 0.5, 0, 5);
            }
        }
    }
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return BlockingModifier.blockWhileCharging(ToolStack.from(stack), UseAnim.BLOCK);
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
            ((TooltipModifierHook)entry.getHook(ModifierHooks.TOOLTIP)).addTooltip(tool, entry, player, tooltips, key, tooltipFlag);
        }
        return tooltips;
    }
}