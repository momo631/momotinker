package com.momosensei.momotinker.Effects;


import com.momosensei.momotinker.register.MomotinkerEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;


public class None extends StaticEffect {


    public None() {
        super(MobEffectCategory.NEUTRAL, 16769263);
        super.addAttributeModifier(Attributes.MAX_HEALTH, "6D5C96FF-02EA-E875-A69F-25E17953AF39", -0.06, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
    public @NotNull String getDescriptionId () {
        return "effect.momotinker.none";
    }
    public boolean isDurationEffectTick (int duration, int amplifier) {
        return true;
    }
    public void addAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap map, int level) {
        super.addAttributeModifiers(entity, map, level);
        if (entity.getHealth() > entity.getMaxHealth()) {
            entity.setHealth(entity.getMaxHealth());
        }
    }
    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        Collection<MobEffectInstance> harmeffect = living.getActiveEffects();
        for (int i = 0; i < harmeffect.size(); ++i) {
            MobEffectInstance effect = (MobEffectInstance) harmeffect.stream().toList().get(i);
            MobEffect mobEffect = effect.getEffect();
            if (mobEffect != MomotinkerEffects.None.get()) {
                living.removeEffect(mobEffect);
            }
        }
    }
}
