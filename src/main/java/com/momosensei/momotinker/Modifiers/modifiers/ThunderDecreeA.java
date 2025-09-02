package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraftforge.event.entity.living.LivingHurtEvent;


public class ThunderDecreeA extends momomodifier {
    public ThunderDecreeA() {
    }
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void OnLivingHurt(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (a instanceof LivingEntity living) {
            int c = getArmorModifierlevel(living, MomotinkerModifiers.thunder_decree_a.getId());
            if (c > 0 && b instanceof LivingEntity living1) {
                DamageSource damageSource=DamageSource.mobAttack(living).setMagic();
                float d = 0;
                if (event.getSource().isMagic()) {
                    damageSource.bypassMagic();
                }
                if (living1.getMobType()==MobType.UNDEAD){
                    d = (living1.getMaxHealth()+living.getMaxHealth())*0.2f;
                }
                living1.invulnerableTime=0;
                living1.hurt(damageSource, event.getAmount() * 0.4f+d);
                living1.invulnerableTime=0;
            }
        }
    }
}