package com.momosensei.momotinker.data.gungnir;

import com.momosensei.momotinker.Items.tool.gungnir.gungnir;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class GungnirTrajectoryCache {
    public static final String KEY_TO = gungnir.its_gungnir_trajectory_key;

    private GungnirTrajectoryCache() {}

    public static void setTrajectoryTo(LivingEntity entity, Vec3 to) {
        CompoundTag d = entity.getPersistentData();
        d.putDouble(KEY_TO + "X", to.x);
        d.putDouble(KEY_TO + "Y", to.y);
        d.putDouble(KEY_TO + "Z", to.z);
    }

    public static Vec3 getTrajectoryTo(LivingEntity entity) {
        CompoundTag d = entity.getPersistentData();
        if (!d.contains(KEY_TO + "X")) return null;
        return new Vec3(
                d.getDouble(KEY_TO + "X"),
                d.getDouble(KEY_TO + "Y"),
                d.getDouble(KEY_TO + "Z"));
    }

    public static void clearTrajectoryTo(LivingEntity entity) {
        CompoundTag d = entity.getPersistentData();
        d.remove(KEY_TO + "X");
        d.remove(KEY_TO + "Y");
        d.remove(KEY_TO + "Z");
    }
}
