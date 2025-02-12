package com.momosensei.momotinker.Effects;

import com.momosensei.momotinker.register.MomotinkerEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class B extends StaticEffect {
    public B() {
        super(MobEffectCategory.NEUTRAL, 16769263);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        if (living instanceof ServerPlayer player && player.level() instanceof ServerLevel serverLevel && living.getEffect(MomotinkerEffects.B.get()) != null) {
            for (int i =0; i <= 360; i++) {
                double rad = i * 0.017453292519943295;
                double r = 4D;
                double x = r * Math.cos(rad);
                double z = r * Math.sin(rad);
                double y = r * Math.sin(rad)*Math.cos(rad);
                int a = 100 / living.getEffect(MomotinkerEffects.B.get()).getDuration();
                serverLevel.sendParticles(ParticleTypes.DRAGON_BREATH, player.getX()+x, player.getY(), player.getZ()+z, 2, 0, 0,  0, 1);

            }
        }
    }
}
