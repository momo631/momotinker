package com.momosensei.momotinker.Effects;

import com.momosensei.momotinker.register.MomotinkerEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class A extends StaticEffect {
    public A() {
        super(MobEffectCategory.NEUTRAL, 16769263);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        if (living instanceof ServerPlayer player && player.level() instanceof ServerLevel serverLevel&&living.getEffect(MomotinkerEffects.A.get())!=null) {
            for (int i = 0; i <= 360; i++) {
                double rad = i * 0.017453292519943295;
                double r = 4D;
                double x = r * Math.cos(rad);
                double z = r * Math.sin(rad);
                int a = 100 / living.getEffect(MomotinkerEffects.A.get()).getDuration();
                if (a < 10) {
                    serverLevel.sendParticles(ParticleTypes.DRAGON_BREATH, player.getX(), player.getY(), player.getZ(), a, x / 2, r / 2, z / 2, 2);
                }
                if (a > 10) {
                    serverLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, player.getX(), player.getY(), player.getZ(), 15, x / 2, r / 2, z / 2, 2);
                }
                
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
