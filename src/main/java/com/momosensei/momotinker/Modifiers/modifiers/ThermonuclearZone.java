package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;


public class ThermonuclearZone extends momomodifier {
    public ThermonuclearZone() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }
    public static final ResourceLocation zonepoints = Momotinker.getResource("zonepoints");
    @Override
    public Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(zonepoints);
        return null;
    }
    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifierEntry, EquipmentSlot equipmentSlot, BiConsumer<Attribute, AttributeModifier> biConsumer) {
        ModDataNBT a = tool.getPersistentData();
        if (a.getInt(zonepoints)>0) {
            biConsumer.accept(Attributes.ARMOR, new AttributeModifier(UUID.fromString("E84AD7B7-CA23-5C0B-0A57-9E1BEB8C365B"), Attributes.ARMOR.getDescriptionId(), 2*a.getInt(zonepoints), AttributeModifier.Operation.ADDITION));
        }
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (entity instanceof Player player) {
            int d = getAllModifierlevel(player, MomotinkerModifiers.thermonuclearzone.getId());
            if (d > 0) {
                int b = (int) player.getAttackRange();
                int c = 0;
                List<LivingEntity> ls0 = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(b + 2, b + 2, b + 2));
                for (LivingEntity targets : ls0) {
                    if (targets != player && targets != null&&targets.isOnFire()) {
                        c+=1;
                    }
                }
                tool.getPersistentData().putInt(zonepoints, c*(2+Math.round(d*0.5f)));
            }
        }
    }
    private void livinghurtevent(LivingHurtEvent event) {
        LivingEntity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (a instanceof Player player&&b instanceof LivingEntity entity) {
            int d = getAllModifierlevel(player, MomotinkerModifiers.thermonuclearzone.getId());
            if (d>0) {
                int c = (int) (player.getArmorValue() + Math.round(player.getAttributeValue(Attributes.ARMOR_TOUGHNESS)));
                entity.hurt(DamageSource.playerAttack(player), event.getAmount() * 0.01F * c);
            }
        }
    }
}