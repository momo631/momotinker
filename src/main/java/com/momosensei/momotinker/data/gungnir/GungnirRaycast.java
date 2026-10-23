package com.momosensei.momotinker.data.gungnir;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class GungnirRaycast {

    private GungnirRaycast() {
    }

    public static LivingEntity findNearestToRay(Player player, float a) {
        List<LivingEntity> candidates = player.level().getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(a),
                living -> living != player && living.isAlive()
        );
        if (candidates.isEmpty()) return null;

        Vec3 rayStart = new Vec3(player.getX(), player.getY() + 0.5 * player.getBbHeight(), player.getZ());
        Vec3 rayDir = getUnitizedVec3(player.getLookAngle().scale(a));
        double maxDist = getMold(player.getLookAngle().scale(a));
        if (maxDist < 1e-6) return null;
        Vec3 rayEnd = rayStart.add(rayDir.scale(maxDist));

        record Hit(LivingEntity e, double dHit) {
        }
        record Miss(LivingEntity e, double dLine) {
        }

        List<Hit> hitList = new ArrayList<>();
        List<Miss> missList = new ArrayList<>();

        for (LivingEntity e : candidates) {
            AABB box = e.getBoundingBox();
            Optional<Vec3> hitOpt = box.clip(rayStart, rayEnd);
            if (hitOpt.isPresent()) {
                hitList.add(new Hit(e, rayStart.distanceTo(hitOpt.get())));
                continue;
            }
            Vec3 center = box.getCenter();
            Vec3 v = center.subtract(rayStart);
            double t = v.dot(rayDir);
            if (t < 0 || t > maxDist) {
                continue;
            }
            double dLine = rayDir.cross(v).length();
            missList.add(new Miss(e, dLine));
        }

        if (!hitList.isEmpty()) {
            hitList.sort(Comparator.comparingDouble(Hit::dHit));
            return hitList.get(0).e();
        }

        missList.sort(Comparator.comparingDouble(Miss::dLine));
        return missList.isEmpty() ? null : missList.get(0).e();
    }

    public static double getMold(Vec3 vec3) {
        if (vec3 != null) {
            return Math.pow(Math.pow(vec3.x, 2) + Math.pow(vec3.y, 2) + Math.pow(vec3.z, 2), 0.5);
        }
        return 0;
    }

    public static Vec3 getUnitizedVec3(Vec3 vec3) {
        if (getMold(vec3) != 0) {
            double i = getMold(vec3);
            return new Vec3(vec3.x / i, vec3.y / i, vec3.z / i);
        }
        return new Vec3(0, 0, 0);
    }
}
