package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.Objects;


public class PolarizedSpacetime extends momomodifier {
    public PolarizedSpacetime() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }
    public static int geti(int i,int max,int min){
        if (i>=max) {
            return max;
        }else return Math.max(i, min);
    }
    private void livinghurtevent(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player&&event.getSource()!=null){
            int a=getAllModifierlevel(player,MomotinkerModifiers.polarizedspacetime.getId());
            if (a>0) {
                double b = Objects.requireNonNull(event.getSource().getSourcePosition()).subtract(player.position()).length();
                event.setAmount(event.getAmount()*0.9f*geti((int) Math.floor(b),12+a*2,1)/(12+a*2));
            }
        }
        if (event.getSource().getEntity() instanceof Player player&&event.getEntity()!=null){
            int a=getAllModifierlevel(player,MomotinkerModifiers.polarizedspacetime.getId());
            if (a>0) {
                double b = event.getEntity().position().subtract(player.position()).length();
                event.setAmount(event.getAmount()*(1f+(0.1f+a*0.01f)*geti((int) Math.floor(b),6+a,1)));
            }
        }
    }
}