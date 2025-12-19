package com.momosensei.momotinker.Modifiers.modifiers;


import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

import static com.momosensei.momotinker.Momotinker.getResource;

public class StarCloudChain extends momomodifier {
    public StarCloudChain() {

    }
    private static final String star_chain = getResource("star_chain").toString();
    @Override
    public void OnLivingHurt(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player&&a instanceof LivingEntity living){
            int c = getAllModifierlevel(player,MomotinkerModifiers.starcloudchain.getId());
            if (c>0) {
                if (!isStarChain(living)) {
                    setStarChain(living);
                }
                List<LivingEntity> list = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(5 + 3 * c));
                for (LivingEntity entity : list) {
                    if (entity != null&&entity!=player&&isStarChain(entity)) {
                        entity.hurt(player.level().damageSources().playerAttack(player),event.getAmount()*(0.2F+0.1F*c));
                    }
                }
            }
        }
        if (a instanceof Player player&&b instanceof LivingEntity living){
            int c = getAllModifierlevel(player,MomotinkerModifiers.starcloudchain.getId());
            if (c>0) {
                if (!isStarChain(living)) {
                    setStarChain(living);
                }
                List<LivingEntity> list = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(5 + 3 * c));
                for (LivingEntity entity : list) {
                    if (entity != null&&entity!=player&&isStarChain(entity)) {
                        entity.hurt(player.level().damageSources().playerAttack(player),event.getAmount()*(0.2F+0.1F*c));
                    }
                }
            }
        }
    }
    private static boolean isStarChain(LivingEntity living){
        var data=living.getPersistentData();
        return data.getBoolean(star_chain);
    }
    private static void setStarChain(LivingEntity living){
        var data =living.getPersistentData();
        data.putBoolean(star_chain, true);
    }
}