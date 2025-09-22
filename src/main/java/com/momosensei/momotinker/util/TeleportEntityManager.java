package com.momosensei.momotinker.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nullable;
import java.util.function.Function;

import static net.minecraft.core.registries.Registries.DIMENSION;

public class TeleportEntityManager {
    public static boolean teleportEntityToDimension(Entity entity, String dimensionId, double x, double y, double z) {
        if (entity.level().isClientSide) return false;

        ResourceKey<Level> dimensionKey;
        try {
            dimensionKey = ResourceKey.create(DIMENSION, getResourceLocation(dimensionId));
        } catch (Exception e) {
            return false;
        }

        if (entity.level().dimension().equals(dimensionKey)) {
            entity.teleportTo(x, y, z);
            return true;
        }

        if (entity.getServer() == null) return false;
        ServerLevel targetLevel = entity.getServer().getLevel(dimensionKey);
        if (targetLevel == null) {
            return false;
        }

        entity.changeDimension(targetLevel, new SimpleTeleporter(new Vec3(x, y, z)));
        return true;
    }

    private static ResourceLocation getResourceLocation(String locationStr) {
        try {
            return new ResourceLocation(locationStr);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid resource location format: " + locationStr, e);
        }
    }

    public static class SimpleTeleporter implements ITeleporter {
        private final Vec3 targetPosition;

        public SimpleTeleporter(Vec3 targetPosition) {
            this.targetPosition = targetPosition;
        }

        @Nullable
        @Override
        public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
            return new PortalInfo(this.targetPosition, Vec3.ZERO, entity.getYRot(), entity.getXRot());
        }

        @Nullable
        @Override
        public Entity placeEntity(Entity entity, ServerLevel currentLevel, ServerLevel destLevel, float yaw, Function<Boolean, Entity> repositionEntity) {
            return repositionEntity.apply(false);
        }
    }
}
