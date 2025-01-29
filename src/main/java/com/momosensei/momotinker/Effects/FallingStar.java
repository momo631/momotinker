package com.momosensei.momotinker.Effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class FallingStar extends StaticEffect{
    public FallingStar() {
        super(MobEffectCategory.BENEFICIAL, 16769263);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        living.hasImpulse = true;
        if (living instanceof Player player) {
            player.startAutoSpinAttack(2);
            player.setDeltaMovement(player.getLookAngle().scale(10));
            player.invulnerableTime = 20;
        }
        living.fallDistance = 0;
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
