package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerEffects;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Unstained extends momomodifier {
    public Unstained() {
        MinecraftForge.EVENT_BUS.addListener(this::livingattackevent);
    }
    private static final List<MobEffect> BENEFICIAL_EFFECTS = new ArrayList<>(List.of(
            MobEffects.LUCK, MobEffects.DIG_SPEED, MobEffects.FIRE_RESISTANCE, MobEffects.MOVEMENT_SPEED,
            MobEffects.DAMAGE_RESISTANCE, MobEffects.JUMP,MobEffects.DAMAGE_BOOST, MobEffects.HEAL,
            MobEffects.REGENERATION,MobEffects.WATER_BREATHING,MobEffects.INVISIBILITY, MobEffects.NIGHT_VISION,
            MobEffects.HEALTH_BOOST,MobEffects.ABSORPTION,MobEffects.SATURATION, MobEffects.SLOW_FALLING,
            MobEffects.CONDUIT_POWER,MobEffects.DOLPHINS_GRACE
    ));
    public static List<MobEffect> getBeneficialEffectsByCopy(){
        return new ArrayList<>(BENEFICIAL_EFFECTS);
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        Collection<MobEffectInstance> harmeffect = entity.getActiveEffects();
        int a = getArmorModifierlevel(entity,MomotinkerModifiers.unstained.getId());
        for (int i = 0; i < harmeffect.size(); i++) {
            MobEffectInstance effect = harmeffect.stream().toList().get(i);
            MobEffect mobEffect = effect.getEffect();
            if (mobEffect.getCategory() != MobEffectCategory.BENEFICIAL&&mobEffect != MomotinkerEffects.LostSoul.get() && mobEffect != MomotinkerEffects.Arrogant.get()) {
                entity.removeEffect(mobEffect);
            }
            for (MobEffect beneficialeffect : getBeneficialEffectsByCopy()) {
                if (a > 0  && mobEffect==beneficialeffect && effect.getDuration() < 10) {
                    entity.addEffect(new MobEffectInstance(mobEffect, 10, effect.getAmplifier()));
                }
            }
        }
    }

    private void livingattackevent(LivingAttackEvent event) {
        LivingEntity living = event.getEntity();
        int a =  getArmorModifierlevel(living,MomotinkerModifiers.unstained.getId());
        int b = RANDOM.nextInt(10);
        if (event.getEntity() instanceof Player &&(a>0 && a<8 && b<a)||a>8 && b<8) {
            event.setCanceled(true);
        }
    }
}