package com.momosensei.momotinker.entity.LegionEntity;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.momosensei.momotinker.Momotinker.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OrbitDefenseSystem {
    private static final Map<UUID, Integer> playerOrbitCounts = new HashMap<>();

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof LegionEntity legion && legion.getForm() == 2) {
            if (legion.getOwner() instanceof ServerPlayer player) {
                incrementOrbitCount(player);
            }
        }
    }
    
    @SubscribeEvent
    public static void onEntityLeaveLevel(EntityLeaveLevelEvent event) {
        if (event.getEntity() instanceof LegionEntity legion && legion.getForm() == 2) {
            if (legion.getOwner() instanceof ServerPlayer player) {
                decrementOrbitCount(player);
            }
        }
    }
    
    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            int orbitCount = playerOrbitCounts.getOrDefault(player.getUUID(), 0);
            if (orbitCount > 0) {
                applyDamageReduction(event, orbitCount);
            }
        }
    }

    private static void incrementOrbitCount(ServerPlayer player) {
        UUID playerId = player.getUUID();
        int newCount = playerOrbitCounts.getOrDefault(playerId, 0) + 1;
        playerOrbitCounts.put(playerId, newCount);
    }

    private static void decrementOrbitCount(ServerPlayer player) {
        UUID playerId = player.getUUID();
        int newCount = Math.max(0, playerOrbitCounts.getOrDefault(playerId, 0) - 1);
        playerOrbitCounts.put(playerId, newCount);
    }

    private static void applyDamageReduction(LivingHurtEvent event, int orbitCount) {
        float originalDamage = event.getAmount();
        float reductionPercent = (float) Math.pow(0.85,orbitCount);
        float reducedDamage = originalDamage * (1 - reductionPercent);
        event.setAmount(reducedDamage);
    }

    public static void interceptProjectiles(Entity owner,Entity entity) {
        List<Projectile> nearbyProjectiles = entity.level.getEntitiesOfClass(Projectile.class, entity.getBoundingBox().inflate(0.4,1.4,0.4));
        for (Projectile projectile : nearbyProjectiles) {
            if (shouldInterceptProjectile(owner,projectile)) {
                reflectProjectile(entity,owner,projectile);
            }
        }
    }

    public static boolean shouldInterceptProjectile(Entity owner,Projectile projectile) {
        if (projectile.getOwner() == owner) {
            return false;
        }
        // 检查弹射物是否朝向玩家（从外向内）
        if (!isProjectileMovingTowardOwner(owner,projectile)) {
            return false;
        }
        return Math.random() < 0.6;
    }

    // 判断弹射物是否朝向玩家（从外向内）
    public static boolean isProjectileMovingTowardOwner(Entity owner,Projectile projectile) {
        if (owner == null) return false;
        Vec3 projectilePos = projectile.position();
        Vec3 ownerPos = owner.position();
        Vec3 projectileMotion = projectile.getDeltaMovement();
        Vec3 toOwner = ownerPos.subtract(projectilePos).normalize();
        double dotProduct = projectileMotion.normalize().dot(toOwner);
        return dotProduct > 0.6;
    }

    // 反弹弹射物
    public static void reflectProjectile(Entity entity,Entity owner,Projectile projectile) {
        Vec3 interceptPos = entity.position();
        Vec3 projectilePos = projectile.position();

        Vec3 reflectDirection = projectilePos.subtract(interceptPos).normalize();

        Entity originalShooter = projectile.getOwner();
        if (originalShooter != null && originalShooter.isAlive()) {
            Vec3 shooterPos = originalShooter.position();
            Vec3 toShooter = shooterPos.subtract(interceptPos).normalize();
            reflectDirection = new Vec3(toShooter.x, toShooter.y, toShooter.z).normalize();
        }
        // 设置反弹速度
        double originalSpeed = projectile.getDeltaMovement().length();
        double reflectSpeed = originalSpeed * 1.5;
        projectile.setDeltaMovement(reflectDirection.scale(reflectSpeed));
        // 改变所有者
        projectile.setOwner(owner);
    }

    public static int getOrbitCount(ServerPlayer player) {
        return playerOrbitCounts.getOrDefault(player.getUUID(), 0);
    }
}