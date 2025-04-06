package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import static com.momosensei.momotinker.util.PenetratingDamage.reflectionPenetratingDamage;


public class Origin extends momomodifier {
    public Origin() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    private void livinghurtevent(LivingAttackEvent event) {
        LivingEntity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof ServerPlayer player && a != null){
            int c = getMainhandModifierlevel(player,MomotinkerModifiers.origin.getId());
            if (c>0){
                event.getSource().bypassArmor().bypassMagic().bypassInvul().bypassEnchantments();
                if (a.getHealth()>a.getMaxHealth()){
                    a.setHealth(a.getMaxHealth());
                }
                if (a.getMaxHealth()<player.getMaxHealth()){
                    reflectionPenetratingDamage(a,player,a.getMaxHealth());
                    a.onRemovedFromWorld();
                    a.setPos(Double.NaN, Double.NaN, Double.NaN);
                }
                a.getAttribute(Attributes.MAX_HEALTH).setBaseValue(a.getMaxHealth()*(1f-0.1f*c));
            }
        }
    }
}