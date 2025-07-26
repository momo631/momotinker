package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerEffects;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
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
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;


public class ForbiddenFruit extends momomodifier {
    public ForbiddenFruit() {
        MinecraftForge.EVENT_BUS.addListener(this::livingattackevent);
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    public static final ResourceLocation forbiddenfruitpoints = Momotinker.getResource("forbiddenfruitpoints");

    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Override
    public Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(forbiddenfruitpoints);
        return null;
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof Player player&&player.tickCount%20==0) {
            List<LivingEntity> ls0 = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(30));
            for (LivingEntity living : ls0) {
                if (living!=null&&living!=player&&living.getAttribute(Attributes.ARMOR).getValue() < player.getAttribute(Attributes.ARMOR).getValue()){
                    living.addEffect(new MobEffectInstance(MomotinkerEffects.ReduceAllAttributes.get(),100,4));
                }
            }
        }
    }
    private void livingattackevent(LivingAttackEvent event) {
        LivingEntity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player && a != null && getMainhandModifierlevel(player, MomotinkerModifiers.forbiddenfruit.getId()) > 0) {
            ToolStack tool = ToolStack.from(player.getMainHandItem());
            a.getAttribute(Attributes.ARMOR).setBaseValue(a.getAttribute(Attributes.ARMOR).getValue() * 0.8F);
            tool.getPersistentData().putFloat(forbiddenfruitpoints, tool.getPersistentData().getFloat(forbiddenfruitpoints) + (float) (a.getAttribute(Attributes.ARMOR).getValue() * 0.2F));
        }
    }
    @Override
    public void addAttributes(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentSlot equipmentSlot, BiConsumer<Attribute, AttributeModifier> biConsumer) {
        ModDataNBT a = iToolStackView.getPersistentData();
        if (a.getFloat(forbiddenfruitpoints)>0) {
            biConsumer.accept(Attributes.ARMOR, new AttributeModifier(UUID.fromString("B9F145FA-2B39-AF5A-2111-1C0781E0B18C"), Attributes.ARMOR.getDescriptionId(), 0.1*a.getFloat(forbiddenfruitpoints), AttributeModifier.Operation.ADDITION));
        }
    }
    private void livinghurtevent(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (a instanceof Player player&&b instanceof LivingEntity living && getMainhandModifierlevel(player, MomotinkerModifiers.forbiddenfruit.getId()) > 0) {
            if (living.getAttribute(Attributes.ARMOR).getValue() < player.getAttribute(Attributes.ARMOR).getValue()) {
                float c = (float) (player.getAttribute(Attributes.ARMOR).getValue()-living.getAttribute(Attributes.ARMOR).getValue());
                if (c>=475)c=475;
                event.setAmount(event.getAmount()*(1f-c*0.002f));
            }
        }
        if (b instanceof Player player&&a instanceof LivingEntity living && getMainhandModifierlevel(player, MomotinkerModifiers.forbiddenfruit.getId()) > 0) {
            if (living.getAttribute(Attributes.ARMOR).getValue() < player.getAttribute(Attributes.ARMOR).getValue()) {
                float c = (float) (player.getAttribute(Attributes.ARMOR).getValue()-living.getAttribute(Attributes.ARMOR).getValue());
                event.setAmount(event.getAmount()*(1f+c*0.008f));
            }
        }
    }
}