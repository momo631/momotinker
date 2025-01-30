package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.item.armor.ModifiableArmorItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class WildHearts extends momomodifier {
    public WildHearts() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    private static final ResourceLocation wildheart = Momotinker.getResource("wildheart");
    private static final ResourceLocation wildheartcooldown = Momotinker.getResource("wildheartcooldown");

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(wildheart);
        iToolStackView.getPersistentData().remove(wildheartcooldown);
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
                    if (tool.getModifierLevel(this) > 0 ) {
                        ModDataNBT a = iToolStackView.getPersistentData();
                        if (entity.getMaxHealth() > 80&&a.getFloat(wildheartcooldown)==0) {
                            entity.addEffect(new MobEffectInstance(MomotinkerEffects.WildHeart.get(), 20));
                        }
                        if (entity.tickCount % 20 == 0 &&a.getFloat(wildheartcooldown)>0.1){
                            a.putFloat(wildheartcooldown,a.getFloat(wildheartcooldown)-1);
                        }
                    }
                }
            }
        }
    }

    private void livinghurtevent(LivingHurtEvent event) {
        LivingEntity living = event.getEntity();
        ModDataNBT a = ToolStack.from(living.getItemBySlot(EquipmentSlot.HEAD)).getPersistentData();
        ModDataNBT b = ToolStack.from(living.getItemBySlot(EquipmentSlot.CHEST)).getPersistentData();
        ModDataNBT c = ToolStack.from(living.getItemBySlot(EquipmentSlot.LEGS)).getPersistentData();
        ModDataNBT d = ToolStack.from(living.getItemBySlot(EquipmentSlot.FEET)).getPersistentData();
        if (living instanceof Player player) {
            if (living.getEffect(MomotinkerEffects.WildHeart.get()) != null && living.hasEffect(MomotinkerEffects.WildHeart.get())) {
                living.removeEffect(MomotinkerEffects.WildHeart.get());
                a.putFloat(wildheartcooldown,8);
                b.putFloat(wildheartcooldown,8);
                c.putFloat(wildheartcooldown,8);
                d.putFloat(wildheartcooldown,8);
            }
            if (a.getFloat(wildheartcooldown)>0){
                a.putFloat(wildheartcooldown,8);
            }
            if (b.getFloat(wildheartcooldown)>0){
                b.putFloat(wildheartcooldown,8);
            }
            if (c.getFloat(wildheartcooldown)>0){
                c.putFloat(wildheartcooldown,8);
            }
            if (d.getFloat(wildheartcooldown)>0){
                d.putFloat(wildheartcooldown,8);
            }
        }
    }
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player != null) {
            ModDataNBT tooldata = tool.getPersistentData();
            tooltip.add(net.minecraft.network.chat.Component.translatable("[狂徒之心]的冷却还剩" + (tooldata.getFloat(wildheartcooldown))+"秒").withStyle(ChatFormatting.GREEN));
        }
    }
}