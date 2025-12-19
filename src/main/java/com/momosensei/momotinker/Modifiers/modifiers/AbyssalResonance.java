package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.BiConsumer;


public class AbyssalResonance extends momomodifier {
    public AbyssalResonance() {
    }

    @Override
    public void addAttributes(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> biConsumer) {
        switch (slot) {
            case MAINHAND -> {
                biConsumer.accept(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("62298B65-944E-9883-F100-499498F69258"), Attributes.MOVEMENT_SPEED.getDescriptionId(), 0.2F, AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
            case OFFHAND -> {
                biConsumer.accept(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("C1FFC811-8B50-FEBA-C1F1-4730D47F48B8"), Attributes.MOVEMENT_SPEED.getDescriptionId(), 0.2F, AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
            case HEAD -> {
                biConsumer.accept(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("9D962FCA-40C2-D492-9D81-33434D1A410C"), Attributes.MOVEMENT_SPEED.getDescriptionId(), 0.2F, AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
            case CHEST -> {
                biConsumer.accept(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("8C051A23-A5DD-D339-3E13-888D4EFC0F4B"), Attributes.MOVEMENT_SPEED.getDescriptionId(), 0.2F, AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
            case LEGS -> {
                biConsumer.accept(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("B59CEF0B-566E-A144-01BF-F3F3D000B02B"), Attributes.MOVEMENT_SPEED.getDescriptionId(), 0.2F, AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
            case FEET -> {
                biConsumer.accept(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("6137F434-4B0B-40E0-0A27-138BFB368A74"), Attributes.MOVEMENT_SPEED.getDescriptionId(), 0.2F, AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
        }
    }

    @Override
    public void OnLivingHurt(LivingHurtEvent event) {
        Entity a = event.getEntity();
        if (a instanceof Player player) {
            int c = getAllModifierlevel(player, MomotinkerModifiers.abyssalresonance.getId());
            if (c > 0 && player.isInWater() || player.level.isRaining()) {
                float bonus = getbonus(player, 400);
                int d = Math.round(bonus) + 2 * c;
                if (d < event.getAmount()) {
                    event.setAmount(event.getAmount() - d);
                } else if (d >= event.getAmount()) {
                    event.setAmount(0);
                }
            }
        }
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (!world.isRaining()&&entity instanceof Player player&&player.tickCount%2000==0){
            Random random = new Random();
            if (random.nextInt(5) == 0&&!world.isClientSide){
                world.setRainLevel(1F);
            }
        }
    }
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> builder, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player != null) {
            int c = getAllModifierlevel(player, MomotinkerModifiers.abyssalresonance.getId());
            float bonus = getbonus(player, 400);
            int d = Math.round(bonus) + 2 * c;
            builder.add(Component.translatable("modifier.momotinker.tooltip.abyssalresonance1").append(d+"").withStyle(ChatFormatting.DARK_BLUE));
        }
    }
}