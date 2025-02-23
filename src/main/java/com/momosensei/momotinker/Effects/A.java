package com.momosensei.momotinker.Effects;

import com.momosensei.momotinker.register.MomotinkerEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class A extends StaticEffect {
    public A() {
        super(MobEffectCategory.BENEFICIAL, 16769263);
        super.addAttributeModifier(Attributes.ARMOR, "F62FAE71-81FF-C7C6-321A-9EAE5730DA42", +1, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.ARMOR_TOUGHNESS, "E512D0A8-EF5A-1052-2E84-A8ED78069C72", +0.6, AttributeModifier.Operation.MULTIPLY_TOTAL);

    }
    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        if (living instanceof ServerPlayer player && player.level() instanceof ServerLevel serverLevel&&living.getEffect(MomotinkerEffects.A.get())!=null) {
            for (int i = 0; i <= 360; i++) {
                double rad = i * 0.017453292519943295;
                double r = 4D;
                double x = r * Math.cos(rad);
                double z = r * Math.sin(rad);
                if (living.getEffect(MomotinkerEffects.A.get()).getDuration()>6000&&living.getEffect(MomotinkerEffects.A.get()).getDuration()!=6000) {
                    int a = 100 / (living.getEffect(MomotinkerEffects.A.get()).getDuration() - 6000);
                    if (a < 10) {
                        serverLevel.sendParticles(ParticleTypes.DRAGON_BREATH, player.getX(), player.getY(), player.getZ(), a, x / 2, r / 2, z / 2, 2);
                    }
                    if (a > 10 && (living.getEffect(MomotinkerEffects.A.get()).getDuration() - 6000) <= 100) {
                        serverLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, player.getX(), player.getY(), player.getZ(), 15, x / 4, r / 4, z / 4, 0.5);
                    }
                }
                if (living.getEffect(MomotinkerEffects.A.get()).getDuration()>0&&living.tickCount%100==0){
                    serverLevel.sendParticles(ParticleTypes.DRAGON_BREATH, player.getX(), player.getY(), player.getZ(), 2, 0, 0, 0, 1.5);
                    List<Mob> list = player.level().getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(4));
                    for (Mob mob : list) {
                        if (mob != null) {
                            Vec3 vec = mob.position().subtract(living.position()).normalize().scale(0.004+0.003*amplifier);
                            mob.push(vec.x, vec.y, vec.z);
                        }
                    }
                }
            }
        }
    }
}
