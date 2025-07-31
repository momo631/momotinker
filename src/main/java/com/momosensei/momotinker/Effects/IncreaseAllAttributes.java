package com.momosensei.momotinker.Effects;

import com.momosensei.momotinker.register.MomotinkerEffects;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import org.jetbrains.annotations.NotNull;

public class IncreaseAllAttributes extends StaticEffect{
    public IncreaseAllAttributes() {
        super(MobEffectCategory.BENEFICIAL, 16769263);
        super.addAttributeModifier(Attributes.ARMOR, "A57019CC-EBFF-8475-6B63-FDF502F8A1ED", +0.02, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.ARMOR_TOUGHNESS, "55BB9283-51D4-84B9-A22A-222A77E26474", +0.02, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.ATTACK_DAMAGE, "D9502C62-465E-1A8B-6E30-778A090E202C", +0.02, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.ATTACK_SPEED, "8DD23672-723E-389B-9E9C-4D2C2FF20448", +0.02, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.MOVEMENT_SPEED, "260B64D9-37BB-82C2-467D-91519CF8546B", +0.02, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.MAX_HEALTH, "E6E589E7-80EC-5CF0-1F49-6A63C8759288", +0.02, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, "6718FD81-892B-8317-D29A-CF7571B6C64E", +0.02, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.LUCK, "B6E9E661-58B9-2775-721D-46116A21B263", +0.02, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.FOLLOW_RANGE, "B9A50B26-EBD2-4AE7-F3FE-D4780FC1B9AA", +0.02, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(Attributes.FLYING_SPEED, "9E7BA20C-3AB6-0663-1D89-4B09C7066669", +0.02, AttributeModifier.Operation.MULTIPLY_TOTAL);
        MinecraftForge.EVENT_BUS.addListener(this::RemoveMobEffect);
    }
    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
    public @NotNull String getDescriptionId () {
        return "effect.momotinker.increaseallattributes";
    }
    private void RemoveMobEffect(MobEffectEvent.Remove event) {
        if (event.getEffect()==MomotinkerEffects.IncreaseAllAttributes.get()){
            event.setCanceled(true);
        }
    }
}
