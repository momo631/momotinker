package com.momosensei.momotinker.Effects;

import com.momosensei.momotinker.register.MomotinkerEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
            if (living instanceof ServerPlayer player && player.level instanceof ServerLevel serverLevel && living.getEffect(MomotinkerEffects.FallingPreparation.get()) != null) {
                for (int i = 0; i <= 360; i++) {
                    double rad = i * 0.017453292519943295;
                    double r = 4D;
                    double x = r * Math.cos(rad);
                    double z = r * Math.sin(rad);
                    int a = 1 / living.getEffect(MomotinkerEffects.FallingPreparation.get()).getDuration();
                    serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, player.getX(), player.getY(), player.getZ(), a, x / 2, r / 2, z / 2, 2);
                }
            }
        }
    }
    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
