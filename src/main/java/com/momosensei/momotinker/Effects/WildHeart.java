package com.momosensei.momotinker.Effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class WildHeart extends StaticEffect{
    public WildHeart() {
        super(MobEffectCategory.BENEFICIAL, 16769263);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        if (living.tickCount % 20 == 0 && living instanceof ServerPlayer ) {
            if (living.getHealth() < living.getMaxHealth()) {
                living.heal(living.getMaxHealth() * 0.1F);
            }
        }
    }
}
