package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;


public class LifeProgram extends momomodifier {
    public LifeProgram() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghealevent);
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    private void livinghealevent(LivingHealEvent event) {
        LivingEntity living = event.getEntity();
        if (living instanceof Player player) {
            int d = getAllModifierlevel(player, MomotinkerModifiers.lifeprogram.getId());
            if (d > 0) {
                event.setAmount(event.getAmount() * (1.4f + 0.2f * d));
                if (player.getHealth() < player.getMaxHealth() * 0.5f) {
                    event.setAmount(event.getAmount() * 2f);
                } else if (player.getHealth() < player.getMaxHealth() * 0.25f) {
                    event.setAmount(event.getAmount() * 4f);
                }
            }
        }
    }

    private void livinghurtevent(LivingHurtEvent event) {
        LivingEntity a = event.getEntity();
        if (a instanceof Player player) {
            int d = getAllModifierlevel(player, MomotinkerModifiers.lifeprogram.getId());
            if (d>0&&event.getAmount()>player.getMaxHealth()*0.9f){
                event.setAmount(player.getMaxHealth()*0.9f);
            }
        }
    }
}