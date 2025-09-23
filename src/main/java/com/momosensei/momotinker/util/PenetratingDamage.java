package com.momosensei.momotinker.util;

import com.momosensei.momotinker.mixins.AttributeInstanceAccessor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;

import static com.momosensei.momotinker.Momotinker.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PenetratingDamage {

    public static final EntityDataAccessor<Float> DATA_HEALTH_ID = getHealthDataAccessor();

    private static EntityDataAccessor<Float> getHealthDataAccessor() {
        try {
            Field field = LivingEntity.class.getDeclaredField("f_20961_");
            field.setAccessible(true);
            Object value = field.get(null);

            if (value instanceof EntityDataAccessor) {
                return (EntityDataAccessor<Float>) value;
            } else {
                System.err.println("DATA_HEALTH_ID is not of type EntityDataAccessor");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void reflectionPenetratingDamage(Entity target, Player player, float value) {
        if (!(target instanceof LivingEntity living)) return;
        if (DATA_HEALTH_ID == null) return;
        float currentHealth = living.getEntityData().get(DATA_HEALTH_ID);
        float newHealth = currentHealth-value;
        living.getEntityData().set(DATA_HEALTH_ID, newHealth);
        if (living.getHealth() <= 0.0F) {
            living.die(DamageSource.playerAttack(player));
        }
    }

    public static void setCachedValue(AttributeInstance attribute, double value) {
        ((AttributeInstanceAccessor) attribute).setCachedValue(value);
    }

    public static double getCachedValue(AttributeInstance attribute) {
        return ((AttributeInstanceAccessor) attribute).getCachedValue();
    }

}
