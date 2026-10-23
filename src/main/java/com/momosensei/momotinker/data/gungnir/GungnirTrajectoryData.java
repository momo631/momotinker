package com.momosensei.momotinker.data.gungnir;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;


public class GungnirTrajectoryData {
    public final Vec3 from;
    public final Vec3 to;
    public final long startGameTime;
    public final float duration;
    public final ResourceKey<Level> dimension;

    public GungnirTrajectoryData(Vec3 from, Vec3 to, long startGameTime, float duration,
                                 ResourceKey<Level> dimension) {
        this.from = from;
        this.to = to;
        this.startGameTime = startGameTime;
        this.duration = duration;
        this.dimension = dimension;
    }

    public float progress(long now, float partialTick) {
        return Mth.clamp((now - startGameTime + partialTick) / duration, 0F, 1F);
    }

    public boolean expired(long now) {
        return now - startGameTime >= duration;
    }
}