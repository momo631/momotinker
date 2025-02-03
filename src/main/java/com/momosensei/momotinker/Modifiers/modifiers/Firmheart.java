package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class Firmheart extends momomodifier {
    public Firmheart() {
    }

    private static final ResourceLocation swallow = Momotinker.getResource("swallow");

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(swallow);
        return null;
    }
    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity target = context.getLivingTarget();
        LivingEntity attacker =context.getAttacker();
        if (target != null&&target.getEffect(MomotinkerEffects.BeSwallow.get())!=null&&attacker instanceof Player player&&modifier.getLevel() > 0 && target.getEffect(MomotinkerEffects.BeSwallow.get()).getAmplifier() == 4) {
            ModDataNBT tooldata = ToolStack.from(attacker.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
            tooldata.putFloat(swallow, attacker.getMaxHealth() * 0.02F + tooldata.getFloat(swallow));
            target.removeEffect(MomotinkerEffects.BeSwallow.get());
            target.addEffect(new MobEffectInstance(MomotinkerEffects.BeSwallowed.get(), 1200));
            return damage + (attacker.getMaxHealth() * 4F);
        }
        return damage;
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (target == null) {
            return true;
        }
        if (target.getEffect(MomotinkerEffects.BeSwallow.get())!=null&&attacker instanceof Player player && projectile instanceof AbstractArrow arrow&&modifier.getLevel() > 0&& target.getEffect(MomotinkerEffects.BeSwallow.get()).getAmplifier() == 4) {
            ModDataNBT tooldata = ToolStack.from(attacker.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
            tooldata.putFloat(swallow, attacker.getMaxHealth() * 0.01F + tooldata.getFloat(swallow));
            target.removeEffect(MomotinkerEffects.BeSwallow.get());
            target.addEffect(new MobEffectInstance(MomotinkerEffects.BeSwallowed.get(), 1200));
            arrow.setBaseDamage(arrow.getBaseDamage()+(attacker.getMaxHealth() * 2F));
        }
        return false;
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof ServerPlayer && modifierEntry.getLevel() > 0) {
            for (LivingEntity e : entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate((double) 10.0F), (ex) -> ex instanceof Enemy)) {
                if (!e.hasEffect(MomotinkerEffects.BeSwallow.get()) && !e.hasEffect(MomotinkerEffects.BeSwallowed.get())) {
                    e.addEffect(new MobEffectInstance(MomotinkerEffects.BeSwallow.get(), 120, 0, false, false));
                }
            }
        }
        if (entity.tickCount % 20 == 0 && entity instanceof ServerPlayer && modifierEntry.getLevel() > 0) {
            for (LivingEntity e : entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate((double) 10.0F), (ex) -> ex instanceof Enemy)) {
                if (e.getEffect(MomotinkerEffects.BeSwallow.get())!=null&&!e.hasEffect(MomotinkerEffects.BeSwallowed.get()) && e.hasEffect(MomotinkerEffects.BeSwallow.get())&& e.getEffect(MomotinkerEffects.BeSwallow.get()).getAmplifier() < 4 ) {
                    e.addEffect(new MobEffectInstance(MomotinkerEffects.BeSwallow.get(), 120, e.getEffect(MomotinkerEffects.BeSwallow.get()).getAmplifier() + 1, false, false));
                }
            }
        }
        if (entity.tickCount % 20 == 0 && entity instanceof ServerPlayer && modifierEntry.getLevel() > 0) {
            for (LivingEntity e : entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate((double) 10.0F), (ex) -> ex instanceof Enemy)) {
                if (e.getEffect(MomotinkerEffects.BeSwallow.get())!=null&&!e.hasEffect(MomotinkerEffects.BeSwallowed.get()) && e.getEffect(MomotinkerEffects.BeSwallow.get()).getAmplifier() == 4) {
                    e.addEffect(new MobEffectInstance(MomotinkerEffects.BeSwallow.get(), 120, 4, false, false));
                }
            }
        }
    }

    @Override
    public void addAttributes(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentSlot equipmentSlot, BiConsumer<Attribute, AttributeModifier> biConsumer) {
        ModDataNBT tooldata = iToolStackView.getPersistentData();
        biConsumer.accept(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("318662D1-A6BF-38C8-5AB6-04B7F7BCC619"), Attributes.MAX_HEALTH.getDescriptionId(), (double)tooldata.getFloat(swallow), AttributeModifier.Operation.ADDITION));
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<net.minecraft.network.chat.Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player != null) {
            tooltip.add(net.minecraft.network.chat.Component.translatable("现阶段每次可提升生命值为" + (player.getMaxHealth()*0.02F)).withStyle(ChatFormatting.DARK_GREEN));
            tooltip.add(net.minecraft.network.chat.Component.translatable("现阶段充能伤害为" + (player.getMaxHealth()*4F)+"（远程减半）").withStyle(ChatFormatting.DARK_GREEN));
        }
    }
}


