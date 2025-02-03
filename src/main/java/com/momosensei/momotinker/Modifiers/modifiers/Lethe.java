package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.register.MomotinkerEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import java.util.Collection;

public class Lethe extends momomodifier {
    public Lethe() {
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        Collection<MobEffectInstance> harmeffect = entity.getActiveEffects();
        for (int i = 0; i < harmeffect.size(); ++i) {
            MobEffectInstance effect = harmeffect.stream().toList().get(i);
            MobEffect mobEffect = effect.getEffect();
            if (mobEffect != MomotinkerEffects.End.get()) {
                entity.removeEffect(mobEffect);
            }
        }
    }
    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        LivingEntity target = context.getLivingTarget();
        if (target != null &&modifier.getLevel() > 0) {
            if (target.getEffect(MomotinkerEffects.None.get()) != null && target.getEffect(MomotinkerEffects.None.get()).getAmplifier() < modifier.getLevel() + 5) {
                target.addEffect(new MobEffectInstance(MomotinkerEffects.None.get(), 300, target.getEffect(MomotinkerEffects.None.get()).getAmplifier() + 1, false, false));
            } else target.addEffect(new MobEffectInstance(MomotinkerEffects.None.get(), 300, 1*modifier.getLevel(), false, false));
            if (target.getEffect(MomotinkerEffects.None.get()) != null &&target.getEffect(MomotinkerEffects.None.get()).getDuration() < 300) {
                target.addEffect(new MobEffectInstance(MomotinkerEffects.None.get(), 300, target.getEffect(MomotinkerEffects.None.get()).getAmplifier()*1, false, false));
            }
        }
        return knockback;
    }
    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker, @javax.annotation.Nullable LivingEntity target) {
        if (target != null && modifier.getLevel() > 0) {
            if (target.getEffect(MomotinkerEffects.None.get()) != null && target.getEffect(MomotinkerEffects.None.get()).getAmplifier() < modifier.getLevel() + 5) {
                target.addEffect(new MobEffectInstance(MomotinkerEffects.None.get(), 300, target.getEffect(MomotinkerEffects.None.get()).getAmplifier() + 1, false, false));
            } else target.addEffect(new MobEffectInstance(MomotinkerEffects.None.get(), 300, modifier.getLevel()-1, false, false));
            if (target.getEffect(MomotinkerEffects.None.get()) != null && target.getEffect(MomotinkerEffects.None.get()).getDuration() < 300) {
                target.addEffect(new MobEffectInstance(MomotinkerEffects.None.get(), 300, target.getEffect(MomotinkerEffects.None.get()).getAmplifier()*1, false, false));
            }
        }
        return false;
    }
}