package com.momosensei.momotinker.Effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class StaticEffect extends MobEffect {
    //这个文件是个超类，用于方便后面添加药水，后面有需要再动
    protected StaticEffect(MobEffectCategory type, int color) {
        super(type, color);
    }
    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration > 0;
    }


}
