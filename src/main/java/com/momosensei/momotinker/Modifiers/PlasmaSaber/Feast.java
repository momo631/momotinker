package com.momosensei.momotinker.Modifiers.PlasmaSaber;

import com.momosensei.momotinker.Modifiers.modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.BiConsumer;

public class Feast extends momomodifier {
    public Feast() {
        MinecraftForge.EVENT_BUS.addListener(this::LivingDeathEvent);
    }

    private static final ResourceLocation edible = Momotinker.getResource("edible");


    @Override
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(edible);
        return null;
    }

    private void LivingDeathEvent(LivingDeathEvent event) {
        LivingEntity b = event.getEntity();
        LivingEntity player = Minecraft.getInstance().player;
        if (b != null && player != null && event.getSource()!=null && event.getSource().isCreativePlayer()){
            ModDataNBT tool = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
            if (b.getMaxHealth() * (0.1F + (0.05F * (ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.feast.getId())))) > tool.getFloat(edible)) {
                tool.putFloat(edible, b.getMaxHealth() * (0.1F + (0.05F * (ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.feast.getId())))));
            }
            if (b.getMaxHealth() * (0.1F + (0.05F * (ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.feast.getId())))) < tool.getFloat(edible)) {
                tool.putFloat(edible, tool.getFloat(edible));
            }
        }
    }

    @Override
    public void addAttributes(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentSlot equipmentSlot, BiConsumer<Attribute, AttributeModifier> biConsumer) {
        ModDataNBT tool = iToolStackView.getPersistentData();
        biConsumer.accept(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("C5820D30-0F3F-D729-A24D-D4F0720BF4E5"), Attributes.MAX_HEALTH.getDescriptionId(), (double)tool.getFloat(edible), AttributeModifier.Operation.ADDITION));
    }
}
