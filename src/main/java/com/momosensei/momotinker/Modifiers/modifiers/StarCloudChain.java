package com.momosensei.momotinker.Modifiers.modifiers;


import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;

import java.util.List;

public class StarCloudChain extends momomodifier {
    public StarCloudChain() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    private void livinghurtevent(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player&&a instanceof Mob){
            int c = ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.starcloudchain.getId());
            if (c>0&&!a.getTags().contains("starchain")) {
                a.addTag("starchain");
            }
            if (c>0) {
                List<Mob> list = player.level.getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(10 + 5 * c));
                for (Mob mob : list) {
                    if (mob != null&&mob.getTags().contains("starchain")) {
                        mob.hurt(DamageSource.playerAttack(player),event.getAmount()*(0.5F+0.1F*c));
                    }
                }
            }
        }
    }
}