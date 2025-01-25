package com.momosensei.momotinker.Effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

public class LostSoul extends StaticEffect{
    public LostSoul() {
        super(MobEffectCategory.HARMFUL, 16769263);
        super.addAttributeModifier(Attributes.ARMOR, "C4F2EEAE-84DF-C663-A29A-4633E7133AB9", -0.2, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
    public @NotNull String getDescriptionId () {
        return "effect.momotinker.lostsoul";
    }
    public boolean isDurationEffectTick (int duration, int amplifier) {
        return true;
    }
}
