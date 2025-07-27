package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;


public class ThePinnacleOfArrogance extends momomodifier {
    public ThePinnacleOfArrogance() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    private void livinghurtevent(LivingHurtEvent event) {
        Entity a = event.getEntity();
        if (a instanceof Player player&&getAllModifierlevel(player, MomotinkerModifiers.thepinnacleofarrogance.getId())>0){
            if (event.getAmount()<player.getMaxHealth()*0.05f){
                event.setAmount(player.getMaxHealth()*0.05f);
            }
            if (event.getAmount()>player.getMaxHealth()*0.25f&&event.getAmount()<player.getMaxHealth()*25f){
                event.setAmount(player.getMaxHealth()*0.25f);
            }
        }
    }
}