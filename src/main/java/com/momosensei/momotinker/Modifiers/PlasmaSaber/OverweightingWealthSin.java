package com.momosensei.momotinker.Modifiers.PlasmaSaber;

import com.momosensei.momotinker.Modifiers.modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.BiConsumer;


public class OverweightingWealthSin extends momomodifier {
    public OverweightingWealthSin() {
    }

    private static final ResourceLocation wealthsin = Momotinker.getResource("wealthsin");

    @Override
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(wealthsin);
        return null;
    }

    @Override
    public void addAttributes(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentSlot equipmentSlot, BiConsumer<Attribute, AttributeModifier> biConsumer) {
        ModDataNBT a = iToolStackView.getPersistentData();
        a.putFloat(wealthsin, modifierEntry.getLevel() * -0.2F);
        biConsumer.accept(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("8B238F4C-4F5D-A930-FC77-FD9D3294CCC4"), Attributes.ATTACK_SPEED.getDescriptionId(), a.getFloat(wealthsin), AttributeModifier.Operation.MULTIPLY_BASE));
    }
}