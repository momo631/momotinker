package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.living.LivingHurtEvent;


public class WardOffEvilA extends momomodifier {
    public WardOffEvilA() {
    }

    @Override
    public void OnLivingHurt(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (a instanceof LivingEntity living){
            int c = getArmorModifierlevel(living, MomotinkerModifiers.ward_off_evil_a.getId());
            if (c>0){
                float d = c*0.2f;if (d>0.95f)d=0.95f;
                if (b instanceof Mob || b instanceof Monster||event.getSource().isMagic()){
                    event.setAmount(event.getAmount()*(1f-d));
                }else
                if (b instanceof LivingEntity living1&&living1.getMobType()==MobType.UNDEAD){
                    event.setAmount(event.getAmount()*0.95f);
                    if (event.getAmount()<living.getMaxHealth()*0.25f){
                        event.setAmount(0);
                    }
                }
            }
        }
    }
}