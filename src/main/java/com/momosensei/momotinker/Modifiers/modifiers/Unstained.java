package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.register.MomotinkerEffects;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;


public class Unstained extends momomodifier {
    public Unstained() {
        MinecraftForge.EVENT_BUS.addListener(this::livingattackevent);
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        Collection<MobEffectInstance> harmeffect = entity.getActiveEffects();
        for (int i = 0; i < harmeffect.size(); i++) {
            MobEffectInstance effect = harmeffect.stream().toList().get(i);
            MobEffect mobEffect = effect.getEffect();
            if ((mobEffect.getCategory() != MobEffectCategory.BENEFICIAL)||(mobEffect != MomotinkerEffects.LostSoul.get())||(mobEffect != MomotinkerEffects.Arrogant.get())) {
                entity.removeEffect(mobEffect);
            }
        }
    }

    private void livingattackevent(LivingAttackEvent event) {
        LivingEntity living = event.getEntity();
        int a =   ModifierUtil.getModifierLevel(living.getItemBySlot(EquipmentSlot.MAINHAND), MomotinkerModifiers.unstained.getId())
                + ModifierUtil.getModifierLevel(living.getItemBySlot(EquipmentSlot.OFFHAND), MomotinkerModifiers.unstained.getId())
                + ModifierUtil.getModifierLevel(living.getItemBySlot(EquipmentSlot.HEAD), MomotinkerModifiers.unstained.getId())
                + ModifierUtil.getModifierLevel(living.getItemBySlot(EquipmentSlot.CHEST), MomotinkerModifiers.unstained.getId())
                + ModifierUtil.getModifierLevel(living.getItemBySlot(EquipmentSlot.LEGS), MomotinkerModifiers.unstained.getId())
                + ModifierUtil.getModifierLevel(living.getItemBySlot(EquipmentSlot.FEET), MomotinkerModifiers.unstained.getId());
        int b = RANDOM.nextInt(10);
        if ((a>0 && a<8 && b<a)||a>8 && b<8) {
            event.setCanceled(true);
        }
    }
}