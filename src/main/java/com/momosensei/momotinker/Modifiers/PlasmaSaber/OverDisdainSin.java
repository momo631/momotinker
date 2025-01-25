package com.momosensei.momotinker.Modifiers.PlasmaSaber;

import com.momosensei.momotinker.Modifiers.modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerEffects;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;


public class OverDisdainSin extends momomodifier {
    public OverDisdainSin() {
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        int a = ModifierUtil.getModifierLevel(entity.getItemBySlot(EquipmentSlot.MAINHAND), MomotinkerModifiers.overdisdainsin.getId())
                + ModifierUtil.getModifierLevel(entity.getItemBySlot(EquipmentSlot.OFFHAND), MomotinkerModifiers.overdisdainsin.getId())
                + ModifierUtil.getModifierLevel(entity.getItemBySlot(EquipmentSlot.HEAD), MomotinkerModifiers.overdisdainsin.getId())
                + ModifierUtil.getModifierLevel(entity.getItemBySlot(EquipmentSlot.CHEST), MomotinkerModifiers.overdisdainsin.getId())
                + ModifierUtil.getModifierLevel(entity.getItemBySlot(EquipmentSlot.LEGS), MomotinkerModifiers.overdisdainsin.getId())
                + ModifierUtil.getModifierLevel(entity.getItemBySlot(EquipmentSlot.FEET), MomotinkerModifiers.overdisdainsin.getId());
        if (a > 0) {
            entity.addEffect(new MobEffectInstance(MomotinkerEffects.Arrogant.get(), 400, a - 1));
        }
        if (a > 0 && entity.getEffect(MomotinkerEffects.Arrogant.get()) != null&& a < entity.getEffect(MomotinkerEffects.Arrogant.get()).getAmplifier()){
            entity.removeEffect(MomotinkerEffects.Arrogant.get());
        }
    }
}