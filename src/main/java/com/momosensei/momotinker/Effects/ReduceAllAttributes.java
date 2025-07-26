package com.momosensei.momotinker.Effects;

import com.momosensei.momotinker.register.MomotinkerEffects;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import org.jetbrains.annotations.NotNull;

public class ReduceAllAttributes extends StaticEffect{
    public ReduceAllAttributes() {
        super(MobEffectCategory.HARMFUL, 16769263);
        super.addAttributeModifier(Attributes.ARMOR, "0081FCE3-8810-C3AC-9E53-BBEFF2F38C28", -0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.ARMOR_TOUGHNESS, "45E0E6E5-68B6-32E5-2926-750C2C25B0BC", -0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.ATTACK_DAMAGE, "4BE2E132-ADEE-75FB-B871-8777C5DD3FD0", -0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.ATTACK_SPEED, "6049503C-2A99-C50A-913D-BD25E267FC33", -0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.MOVEMENT_SPEED, "E2C6339E-F5ED-16E9-6E12-4AFD6AD40C5F", -0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.MAX_HEALTH, "D2C88A5F-6683-0688-A3A8-464621F99694", -0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, "9D923A7E-361D-6BEC-E302-AA85C6F855C8", -0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.LUCK, "D57A2D7A-6936-CF65-2E93-135D7CD604BC", -0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.FOLLOW_RANGE, "937ADEB2-9902-2A47-CAFC-05A0609B74E1", -0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.FLYING_SPEED, "86D9BAE8-A48F-B238-D759-6A6EEE96DF04", -0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
        MinecraftForge.EVENT_BUS.addListener(this::RemoveMobEffect);
    }
    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
    public @NotNull String getDescriptionId () {
        return "effect.momotinker.reduceallattributes";
    }
    private void RemoveMobEffect(MobEffectEvent.Remove event) {
        if (event.getEffect()==MomotinkerEffects.ReduceAllAttributes.get()){
            event.setCanceled(true);
        }
    }
}
