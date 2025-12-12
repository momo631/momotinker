package com.momosensei.momotinker.util;

import com.momosensei.momotinker.mixins.AttributeInstanceAccessor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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
        float newHealth = currentHealth - value;
        living.getEntityData().set(DATA_HEALTH_ID, newHealth);
        if (living.getHealth() <= 0.0F) {
            living.die(player.damageSources().playerAttack(player));
        }
    }

    public static void setCachedValue(AttributeInstance attribute, double value) {
        ((AttributeInstanceAccessor) attribute).setCachedValue(value);
    }

    public static double getCachedValue(AttributeInstance attribute) {
        return ((AttributeInstanceAccessor) attribute).getCachedValue();
    }


    public static void tryModifyHealth(LivingEntity entity, float newHealth) {
        float health = entity.getHealth();
        for (Class<?> currentClass = entity.getClass(); currentClass != LivingEntity.class.getSuperclass(); currentClass = currentClass.getSuperclass()) {
            for (var field : currentClass.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    try {
                        field.setAccessible(true);
                        Object value = field.get(entity);
                        Class<?> fieldType = field.getType();
                        float testValue = health - 1;
                        if (fieldType == float.class || fieldType == Float.class) {
                            field.set(entity, testValue);
                        } else if (fieldType == double.class || fieldType == Double.class) {
                            field.set(entity, (double) testValue);
                        } else if (fieldType == int.class || fieldType == Integer.class) {
                            field.set(entity, (int) testValue);
                        } else if (fieldType == long.class || fieldType == Long.class) {
                            field.set(entity, (long) testValue);
                        } else if (fieldType == short.class || fieldType == Short.class) {
                            field.set(entity, (short) testValue);
                        } else if (fieldType == byte.class || fieldType == Byte.class) {
                            field.set(entity, (byte) testValue);
                        } else if (fieldType == String.class) {
                            field.set(entity, Float.toString(testValue));
                        }
                        if (entity.getHealth() == testValue) {
                            if (fieldType == float.class || fieldType == Float.class) {
                                field.set(entity, newHealth);
                            } else if (fieldType == double.class || fieldType == Double.class) {
                                field.set(entity, (double) newHealth);
                            } else if (fieldType == int.class || fieldType == Integer.class) {
                                field.set(entity, (int) newHealth);
                            } else if (fieldType == String.class) {
                                field.set(entity, Float.toString(newHealth));
                            }
                        } else {
                            field.set(entity, value);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        for (var key : entity.getEntityData().itemsById.keySet()) {
            @SuppressWarnings("unchecked")
            SynchedEntityData.DataItem<Object> item = (SynchedEntityData.DataItem<Object>) entity.getEntityData().itemsById.get(key.intValue());
            var value = item.getValue();
            if (value instanceof Number) {
                item.setValue(health - 1);
                if (entity.getHealth() == (health - 1)) {
                    item.setValue(newHealth);
                } else {
                    item.setValue(value);
                }
            } else if (value instanceof String) {
                item.setValue(Float.toString(health - 1));
                if (entity.getHealth() == (health - 1)) {
                    item.setValue(Float.toString(newHealth));
                } else {
                    item.setValue(value);
                }
            }
        }
    }
}
