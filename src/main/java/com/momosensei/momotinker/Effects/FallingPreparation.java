package com.momosensei.momotinker.Effects;

import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class FallingPreparation extends StaticEffect {
    public FallingPreparation() {
        super(MobEffectCategory.NEUTRAL, 16769263);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        if (living instanceof Player) {
            living.hasImpulse = true;
            if (living.isOnGround()) {
                living.setPos(living.position().add(0, 1, 0));
            }
            living.setDeltaMovement(new Vec3(
                    Mth.lerp(.5f, living.getDeltaMovement().x, 0),
                    Mth.lerp(.5f, living.getDeltaMovement().y, 2),
                    Mth.lerp(.5f, living.getDeltaMovement().z, 0)
            ));
            living.invulnerableTime = 20;
        }
    }
    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
