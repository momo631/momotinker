package com.momosensei.momotinker.Modifiers.modifiers;


import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

public class StarCloudChain extends momomodifier {
    public StarCloudChain() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    private void livinghurtevent(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player&&a instanceof LivingEntity living){
            int c = getAllModifierlevel(player,MomotinkerModifiers.starcloudchain.getId());
            if (c>0&&!living.getTags().contains("starchain")) {
                living.addTag("starchain");
            }
            if (c>0) {
                List<LivingEntity> list = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(5 + 3 * c));
                for (LivingEntity entity : list) {
                    if (entity != null&&entity.getTags().contains("starchain")) {
                        entity.hurt(DamageSource.playerAttack(player),event.getAmount()*(0.2F+0.1F*c));
                    }
                }
            }
        }
        if (a instanceof Player player&&b instanceof LivingEntity living){
            int c = getAllModifierlevel(player,MomotinkerModifiers.starcloudchain.getId());
            if (c>0&&!living.getTags().contains("starchain")) {
                living.addTag("starchain");
            }
            if (c>0) {
                List<LivingEntity> list = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(5 + 3 * c));
                for (LivingEntity entity : list) {
                    if (entity != null&&entity.getTags().contains("starchain")) {
                        entity.hurt(DamageSource.playerAttack(player),event.getAmount()*(0.2F+0.1F*c));
                    }
                }
            }
        }
    }
}