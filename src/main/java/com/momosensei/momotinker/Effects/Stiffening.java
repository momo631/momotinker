package com.momosensei.momotinker.Effects;

import com.momosensei.momotinker.register.MomotinkerEffects;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import org.jetbrains.annotations.NotNull;

public class Stiffening extends StaticEffect{
    public Stiffening() {
        super(MobEffectCategory.HARMFUL, 16769263);
        super.addAttributeModifier(Attributes.ATTACK_SPEED, "66D409E6-A837-CA41-55E5-981E2B506FF6", -0.05, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.MOVEMENT_SPEED, "E9814A10-0E1A-8E87-4871-C8FE3452F694", -0.05, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, "27A88D26-39BA-8CE5-78C2-C834ACF00620", 0.05, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.FLYING_SPEED, "E8F71636-7CF0-159E-F086-342B435D1878", -0.05, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.JUMP_STRENGTH, "3EDBD454-BC1B-E9D2-B48C-24FFF75D74F5", -0.05, AttributeModifier.Operation.MULTIPLY_TOTAL);
        MinecraftForge.EVENT_BUS.addListener(this::RemoveMobEffect);
    }
    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
    public @NotNull String getDescriptionId () {
        return "effect.momotinker.stiffening";
    }
    private void RemoveMobEffect(MobEffectEvent.Remove event) {
        if (event.getEffect()==MomotinkerEffects.Stiffening.get()){
            event.setCanceled(true);
        }
    }
}
