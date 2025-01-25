package com.momosensei.momotinker.Effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

public class Arrogant extends StaticEffect{
    public Arrogant() {
        super(MobEffectCategory.HARMFUL, 16769263);
        super.addAttributeModifier(Attributes.MAX_HEALTH, "6D453622-9A7E-B663-70A0-896591F44E56", -0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
    public @NotNull String getDescriptionId () {
        return "effect.momotinker.arrogant";
    }
    public boolean isDurationEffectTick (int duration, int amplifier) {
        return true;
    }
}
