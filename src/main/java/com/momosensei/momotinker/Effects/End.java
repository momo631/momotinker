package com.momosensei.momotinker.Effects;

import com.momosensei.momotinker.register.MomotinkerEffects;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHealEvent;

public class End extends StaticEffect{
    public End() {
        super(MobEffectCategory.BENEFICIAL, 16769263);
        MinecraftForge.EVENT_BUS.addListener(this::livinghealevent);
    }


    public void applyEffectTick(LivingEntity living, int amplifier) {
        if (living.getHealth() > 1) {
            living.setHealth(1);
        }

        if (living instanceof Player&&living.hasEffect(MomotinkerEffects.End.get())) {
            for (LivingEntity e : living.level().getEntitiesOfClass(LivingEntity.class, living.getBoundingBox().inflate(80.0F), (ex) -> ex instanceof Enemy)) {
                e.addEffect(new MobEffectInstance(MomotinkerEffects.None.get(), 300, 12, false, false));
            }
        }
    }

    private void livinghealevent(LivingHealEvent event) {
        LivingEntity living = event.getEntity();
        if (living instanceof Player&&living.hasEffect(MomotinkerEffects.End.get())){
            event.setAmount(event.getAmount() * 0);
        }
    }
}
