package com.momosensei.momotinker.entity.Boxentity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.momosensei.momotinker.Momotinker.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OrbitDefenseSystem {
    private static final Map<UUID, Integer> playerOrbitCounts = new HashMap<>();

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof BoxEntity box && box.getForm() == 2) {
            if (box.getOwner() instanceof ServerPlayer player) {
                incrementOrbitCount(player);
            }
        }
    }
    
    @SubscribeEvent
    public static void onEntityLeaveLevel(EntityLeaveLevelEvent event) {
        if (event.getEntity() instanceof BoxEntity box && box.getForm() == 2) {
            if (box.getOwner() instanceof ServerPlayer player) {
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
    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        if (event.getRayTraceResult() instanceof EntityHitResult entityHit) {
            if (entityHit.getEntity() instanceof ServerPlayer player) {
                int orbitCount = playerOrbitCounts.getOrDefault(player.getUUID(), 0);
                if (orbitCount > 0 && shouldReflectProjectile(orbitCount)) {
                    reflectProjectile(event, player, orbitCount);
                    event.setCanceled(true);
                }
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
        float reductionPercent = (float) Math.pow(0.95,orbitCount);
        float reducedDamage = originalDamage * (1 - reductionPercent);
        event.setAmount(reducedDamage);
    }

    private static boolean shouldReflectProjectile(int orbitCount) {
        double baseChance = Math.min(orbitCount * 0.08+0.04, 1);
        return Math.random() < baseChance;
    }

    // 反弹弹射物
    private static void reflectProjectile(ProjectileImpactEvent event, ServerPlayer player, int orbitCount) {
        Entity projectile = event.getProjectile();

        Vec3 playerPos = player.position();
        Vec3 projectilePos = projectile.position();
        Vec3 reflectDirection = projectilePos.subtract(playerPos).normalize();

        double reflectSpeed = getReflectSpeed(projectile, orbitCount);
        projectile.setDeltaMovement(reflectDirection.scale(reflectSpeed));

        resetProjectileProperties(projectile, player);
    }

    // 获取反弹速度
    private static double getReflectSpeed(Entity projectile, int orbitCount) {
        double speedBonus = orbitCount * 0.2;
        return projectile.getDeltaMovement().length() * 1.1 * (1 + speedBonus);
    }

    // 重置弹射物属性
    private static void resetProjectileProperties(Entity projectile, ServerPlayer player) {
        if (projectile instanceof AbstractArrow arrow) {
            arrow.setOwner(player);
            arrow.setCritArrow(true);
        }

        if (projectile instanceof ThrowableProjectile throwable) {
            throwable.setOwner(player);
        }

        projectile.setNoGravity(true);
        if (projectile instanceof Projectile proj) {
            proj.setNoGravity(true);
        }

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    public void run() {
                        if (projectile.isAlive()) {
                            ((ServerLevel) projectile.level).sendParticles(ParticleTypes.GLOW,
                                    projectile.getX(), projectile.getY(), projectile.getZ(),
                                    5, 0.2, 0.2, 0.2, 0.05);
                            projectile.setNoGravity(false);
                        }
                    }
                },
                1000
        );
    }

    public static int getOrbitCount(ServerPlayer player) {
        return playerOrbitCounts.getOrDefault(player.getUUID(), 0);
    }
}