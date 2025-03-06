package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerEffects;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;


public class Unstained extends momomodifier {
    public Unstained() {
        MinecraftForge.EVENT_BUS.addListener(this::livingattackevent);
        MinecraftForge.EVENT_BUS.addListener(this::WhenEffectRemove);
        MinecraftForge.EVENT_BUS.addListener(this::MobEffectEvent);
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        Collection<MobEffectInstance> harmeffect = entity.getActiveEffects();
        for (int i = 0; i < harmeffect.size(); i++) {
            MobEffectInstance effect = harmeffect.stream().toList().get(i);
            MobEffect mobEffect = effect.getEffect();
            if (mobEffect.getCategory() != MobEffectCategory.BENEFICIAL) {
                entity.removeEffect(mobEffect);
            }else if (mobEffect != MomotinkerEffects.LostSoul.get()){
                entity.removeEffect(mobEffect);
            }else if (mobEffect != MomotinkerEffects.Arrogant.get()){
                entity.removeEffect(mobEffect);
            }
        }
    }
    public void MobEffectEvent(MobEffectEvent.Applicable event) {
        if (event.getEntity() != null && event.getEntity() instanceof Player player) {
            int a =  getArmorModifierlevel(player,MomotinkerModifiers.unstained.getId());
            if (a > 0&&!event.getEffectInstance().getEffect().isBeneficial()) {
                event.setResult(Event.Result.DENY);
            }
        }
    }

    public void WhenEffectRemove(MobEffectEvent.Remove event) {
        if (event.getEntity()!=null&&event.getEntity() instanceof Player player) {
            int a =  getArmorModifierlevel(player,MomotinkerModifiers.unstained.getId());
            if (a>0&&event.getEffectInstance() != null) {
                if (event.getEffectInstance().getEffect().isBeneficial()) {
                    event.setCanceled(true);
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