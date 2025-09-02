package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraftforge.event.entity.living.LivingHurtEvent;


public class SuppressingEvilA extends momomodifier {
    public SuppressingEvilA() {
    }

    @Override
    public void OnLivingHurt(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (a instanceof LivingEntity living){
            int c = getArmorModifierlevel(living, MomotinkerModifiers.suppressing_evil_a.getId());
            if (c>0){
                float d = c*0.2f;if (d>0.95f)d=0.95f;
                float e = c*0.1f;if (e>0.95f)e=0.95f;
                if (b instanceof LivingEntity living1&&living1.getMobType()==MobType.UNDEAD){
                    event.setAmount(event.getAmount()*(1f-d));
                }
                if (event.getSource()==living.level().damageSources().magic()){
                    event.setAmount(event.getAmount()*(1f-e));
                }
            }
        }
    }
}