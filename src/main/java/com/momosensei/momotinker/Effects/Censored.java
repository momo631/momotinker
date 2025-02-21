package com.momosensei.momotinker.Effects;

import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.CensoredCharge;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class Censored extends StaticEffect {
    public Censored() {
        super(MobEffectCategory.NEUTRAL, 16769263);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        List<LivingEntity> list = living.level().getEntitiesOfClass(LivingEntity.class, living.getBoundingBox().inflate(10));
        for (LivingEntity entity : list) {
            if (entity != null) {
                Channel.sendToClient(new CensoredCharge(5));
            }
        }
    }
}