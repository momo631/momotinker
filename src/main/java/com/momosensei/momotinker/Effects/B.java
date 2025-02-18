package com.momosensei.momotinker.Effects;

import com.momosensei.momotinker.register.MomotinkerEffects;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class B extends StaticEffect {
    public B() {
        super(MobEffectCategory.NEUTRAL, 16769263);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        List<LivingEntity> list = living.level.getEntitiesOfClass(LivingEntity.class, living.getBoundingBox().inflate(10));
        for (LivingEntity entity : list) {
            if (entity != null) {
                entity.addEffect(new MobEffectInstance(MomotinkerEffects.C.get(),20));
            }
        }
    }
}