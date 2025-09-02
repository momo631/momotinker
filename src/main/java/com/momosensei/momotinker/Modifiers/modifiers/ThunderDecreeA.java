package com.momosensei.momotinker.Modifiers.modifiers;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
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
                LegacyDamageSource damageSource= LegacyDamageSource.directMagic(living.level());
                float d = 0;
                if (event.getSource()==living.level().damageSources().magic()) {
                    damageSource.setMagic();
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