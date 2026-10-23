package com.momosensei.momotinker.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.*;

public class ChunkLoadManager {
    private static final Map<UUID, ChunkPos> entityChunkMap = new HashMap<>();

    public static boolean forceChunk(ServerLevel level, ChunkPos chunkPos, boolean force) {
        if (level == null) {
            return false;
        }
        return level.setChunkForced(chunkPos.x, chunkPos.z, force);
    }

    /**
     * 开始跟踪实体并强制加载其区块
     * @param entity          目标实体
     * @param dimensionKey    目标维度（可为 null，表示使用实体当前所在维度）
     */
    public static void startForceLoadingForEntity(Entity entity, @Nullable ResourceKey<Level> dimensionKey) {
        if (entity.level().isClientSide) {
            return;
        }
        UUID entityId = entity.getUUID();
        ChunkPos chunkPos = new ChunkPos(entity.blockPosition());

        MinecraftServer server = entity.getServer();
        if (server == null) return;

        ResourceKey<Level> targetDimension = dimensionKey != null ? dimensionKey : entity.level().dimension();
        ServerLevel targetLevel = server.getLevel(targetDimension);
        if (targetLevel == null) {
            return;
        }

        if (entityChunkMap.containsKey(entityId)) {
            ChunkPos oldChunkPos = entityChunkMap.get(entityId);
            if (!oldChunkPos.equals(chunkPos)) {
                stopForceLoadingForEntity(entity, targetDimension);
            } else {
                return;
            }
        }

        if (forceChunk(targetLevel, chunkPos, true)) {
            entityChunkMap.put(entityId, chunkPos);
        }
    }

    /**
     * 停止为实体强制加载区块
     * @param entity          目标实体
     * @param dimensionKey    目标维度（可为 null，表示使用实体当前所在维度）
     */
    public static void stopForceLoadingForEntity(Entity entity, @Nullable ResourceKey<Level> dimensionKey) {
        if (entity.level().isClientSide) {
            return;
        }
        UUID entityId = entity.getUUID();

        if (!entityChunkMap.containsKey(entityId)) {
            return;
        }

        ChunkPos chunkPos = entityChunkMap.get(entityId);

        MinecraftServer server = entity.getServer();
        if (server == null) return;

        ResourceKey<Level> targetDimension = dimensionKey != null ? dimensionKey : entity.level().dimension();
        ServerLevel targetLevel = server.getLevel(targetDimension);
        if (targetLevel == null) return;

        entityChunkMap.remove(entityId);

        if (hasOtherTrackedEntitiesInChunk(chunkPos)) {
            return;
        }

        forceChunk(targetLevel, chunkPos, false);
    }

    private static boolean hasOtherTrackedEntitiesInChunk(ChunkPos chunkPos) {
        for (ChunkPos pos : entityChunkMap.values()) {
            if (pos.equals(chunkPos)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 清理孤儿区块
     * @param server        MinecraftServer 实例
     * @param dimensionKey  目标维度（可为 null，表示无法清理时直接返回）
     */
    public static void cleanupOrphanedChunks(@Nullable MinecraftServer server, @Nullable ResourceKey<Level> dimensionKey) {
        if (server == null) return;
        if (dimensionKey == null) return;

        ServerLevel targetLevel = server.getLevel(dimensionKey);
        if (targetLevel == null) return;

        Set<ChunkPos> chunksToCheck = new HashSet<>(entityChunkMap.values());

        for (ChunkPos chunkPos : chunksToCheck) {
            if (!hasValidEntityForChunk(chunkPos, server, dimensionKey)) {
                forceChunk(targetLevel, chunkPos, false);
                removeAllEntitiesForChunk(chunkPos);
            }
        }
    }

    private static boolean hasValidEntityForChunk(ChunkPos chunkPos, MinecraftServer server, ResourceKey<Level> dimensionKey) {
        Iterator<Map.Entry<UUID, ChunkPos>> iterator = entityChunkMap.entrySet().iterator();
        boolean hasValidEntity = false;

        ServerLevel targetLevel = server.getLevel(dimensionKey);
        if (targetLevel == null) {
            return false;
        }

        while (iterator.hasNext()) {
            Map.Entry<UUID, ChunkPos> entry = iterator.next();
            UUID entityId = entry.getKey();
            ChunkPos entityChunk = entry.getValue();
            if (entityChunk.equals(chunkPos)) {
                Entity entity = targetLevel.getEntity(entityId);
                if (entity == null || !entity.isAlive()) {
                    iterator.remove();
                } else {
                    hasValidEntity = true;
                }
            }
        }
        return hasValidEntity;
    }

    // 移除指定区块的所有实体记录
    private static void removeAllEntitiesForChunk(ChunkPos chunkPos) {
        entityChunkMap.entrySet().removeIf(entry -> entry.getValue().equals(chunkPos));
    }
}
