package com.momosensei.momotinker.Modifiers.PlasmaSaber;

import com.momosensei.momotinker.Modifiers.modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.item.armor.ModifiableArmorItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.BiConsumer;

public class WildHearts extends momomodifier {
    public WildHearts() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    private static final ResourceLocation wildheart = Momotinker.getResource("wildheart");

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(wildheart);
        return null;
    }

    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
        ModDataNBT b = tool.getPersistentData();
        b.putFloat(wildheart, 20);
        switch (slot) {
            case HEAD -> {
                consumer.accept(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("2CCC9F8F-AA2E-358A-F245-1C8567706FD4"), Attributes.MAX_HEALTH.getDescriptionId(), b.getFloat(wildheart), AttributeModifier.Operation.ADDITION));
            }
            case CHEST -> {
                consumer.accept(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("D7228C65-75D1-B3B9-A990-3FE5DABE4A56"), Attributes.MAX_HEALTH.getDescriptionId(), b.getFloat(wildheart), AttributeModifier.Operation.ADDITION));
            }
            case LEGS -> {
                consumer.accept(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("06261F6C-B538-D59E-989D-76A921C3DEBE"), Attributes.MAX_HEALTH.getDescriptionId(), b.getFloat(wildheart), AttributeModifier.Operation.ADDITION));
            }
            case FEET -> {
                consumer.accept(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("A48A0C39-E10F-9D86-1DFB-BA0D78372FD3"), Attributes.MAX_HEALTH.getDescriptionId(), b.getFloat(wildheart), AttributeModifier.Operation.ADDITION));
            }
        }
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof ServerPlayer player) {
            for (ItemStack stack : player.getInventory().armor) {
                if (stack.getItem() instanceof ModifiableArmorItem) {
                    ToolStack tool = ToolStack.from(stack);
                    if (tool.getModifierLevel(this) > 0 && entity.tickCount % 160 == 0 && entity.getMaxHealth() > 80) {
                        entity.addEffect(new MobEffectInstance(MomotinkerEffects.WildHeart.get(), 160));
                    }
                }
            }
        }
    }

    private void livinghurtevent(LivingHurtEvent event) {
        LivingEntity living = event.getEntity();
        Entity entity =event.getSource().getEntity();
        if (living.getEffect(MomotinkerEffects.WildHeart.get()) != null && entity != null && event.getSource() != null && living.hasEffect(MomotinkerEffects.WildHeart.get())) {
            living.removeEffect(MomotinkerEffects.WildHeart.get());
        }
    }
}