package com.momosensei.momotinker.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.*;

import static net.minecraft.core.registries.Registries.DIMENSION;

public class ChunkLoadManager {
    public static boolean forceChunk(ServerLevel level, ChunkPos chunkPos, boolean force) {
        if (level == null) {
            return false;
        }

        return level.setChunkForced(chunkPos.x, chunkPos.z, force);
    }

    private static final Map<UUID, ChunkPos> entityChunkMap = new HashMap<>();

    /**
     * 开始跟踪实体并强制加载其区块
     *
     * @param entity 目标实体
     */
    public static void startForceLoadingForEntity(Entity entity) {
        if (entity.level().isClientSide) {
            return;
        }
        UUID entityId = entity.getUUID();
        ChunkPos chunkPos = new ChunkPos(entity.blockPosition());
        final String DIMENSION_ID = "momotinker:mountains_memory";
        ResourceLocation dimensionLocation = new ResourceLocation(DIMENSION_ID);
        ResourceKey<Level> memoryDimension = ResourceKey.create(DIMENSION, dimensionLocation);
        MinecraftServer server = entity.getServer();
        if (server == null) return;

        ServerLevel targetLevel = server.getLevel(memoryDimension);
        if (targetLevel == null) {
            return;
        }

        if (entityChunkMap.containsKey(entityId)) {
            ChunkPos oldChunkPos = entityChunkMap.get(entityId);
            if (!oldChunkPos.equals(chunkPos)) {
                stopForceLoadingForEntity(entity);
            } else {
                return;
            }
        }

        // 强制加载区块
        if (forceChunk(targetLevel, chunkPos, true)) {
            entityChunkMap.put(entityId, chunkPos);
        }
    }

    /**
     * 停止为实体强制加载区块
     *
     * @param entity 目标实体
     */
    public static void stopForceLoadingForEntity(Entity entity) {
        if (entity.level().isClientSide) {
            return;
        }
        UUID entityId = entity.getUUID();

        if (!entityChunkMap.containsKey(entityId)) {
            return;
        }

        ChunkPos chunkPos = entityChunkMap.get(entityId);
        final String DIMENSION_ID = "momotinker:mountains_memory";
        ResourceLocation dimensionLocation = new ResourceLocation(DIMENSION_ID);
        ResourceKey<Level> dimension = ResourceKey.create(DIMENSION, dimensionLocation);
        MinecraftServer server = entity.getServer();
        if (server == null) return;

        ServerLevel targetLevel = server.getLevel(dimension);
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
    public static void cleanupOrphanedChunks(@Nullable MinecraftServer server) {
        if (server == null) return;

        final String DIMENSION_ID = "momotinker:mountains_memory";
        ResourceLocation dimensionLocation = new ResourceLocation(DIMENSION_ID);
        ResourceKey<Level> memoryDimension = ResourceKey.create(DIMENSION, dimensionLocation);
        ServerLevel targetLevel = server.getLevel(memoryDimension);

        if (targetLevel == null) return;

        // 收集所有需要检查的区块
        Set<ChunkPos> chunksToCheck = new HashSet<>(entityChunkMap.values());

        // 检查每个区块是否还有对应的实体存在
        for (ChunkPos chunkPos : chunksToCheck) {
            if (!hasValidEntityForChunk(chunkPos, server)) {
                // 没有有效实体对应此区块，取消强制加载
                forceChunk(targetLevel, chunkPos, false);

                // 同时移除所有指向此区块的实体记录
                removeAllEntitiesForChunk(chunkPos);
            }
        }
    }

    private static boolean hasValidEntityForChunk(ChunkPos chunkPos, MinecraftServer server) {
        Iterator<Map.Entry<UUID, ChunkPos>> iterator = entityChunkMap.entrySet().iterator();
        boolean hasValidEntity = false;

        while (iterator.hasNext()) {
            Map.Entry<UUID, ChunkPos> entry = iterator.next();
            UUID entityId = entry.getKey();
            ChunkPos entityChunk = entry.getValue();
            final String DIMENSION_ID = "momotinker:mountains_memory";
            ResourceLocation dimensionLocation = new ResourceLocation(DIMENSION_ID);
            if (entityChunk.equals(chunkPos)) {
                Entity entity = server.getLevel(ResourceKey.create(DIMENSION,dimensionLocation)).getEntity(entityId);
                // 如果实体不存在或已经死亡，则移除记录
                if (entity == null || !entity.isAlive()) {
                    iterator.remove();
                } else {
                    hasValidEntity = true; // 至少有一个有效实体
                }
            }
        }
        return hasValidEntity;
    }

    // 移除指定区块的所有实体记录
    private static void removeAllEntitiesForChunk(ChunkPos chunkPos) {
        entityChunkMap.entrySet().removeIf(entry -> entry.getValue().equals(chunkPos));
    }

//    public static final String CHUNK_LOADED_KEY = "ChunkLoaded";
//    // 跟踪活跃的区块加载
//    public static final Map<UUID, LoadedChunkInfo> ACTIVE_LOADS = new HashMap<>();

    // 区块加载信息记录
//    public static class LoadedChunkInfo {
//        public final ChunkPos chunkPos;
//        public final ServerLevel level;
//
//        public LoadedChunkInfo(ChunkPos chunkPos, ServerLevel level) {
//            this.chunkPos = chunkPos;
//            this.level = level;
//        }
//    }

//    public static boolean forceLoadEntityChunk(LivingEntity entity) {
//        if (entity.level().isClientSide()) return false;
//        ServerLevel serverLevel = (ServerLevel) entity.level();
//        UUID entityUUID = entity.getUUID();
//        CompoundTag nbt = entity.getPersistentData();
//
//        if (nbt.getBoolean(CHUNK_LOADED_KEY)) {
//            return true;
//        }
//        BlockPos entityPos = entity.blockPosition();
//        ChunkPos chunkPos = new ChunkPos(entityPos);
//        try {
//            boolean success = ForgeChunkManager.forceChunk(serverLevel, MOD_ID, entity, chunkPos.x, chunkPos.z, true, true
//            );
//            if (success) {
//                // 更新状态
//                nbt.putBoolean(CHUNK_LOADED_KEY, true);
//                ACTIVE_LOADS.put(entityUUID, new LoadedChunkInfo(chunkPos, serverLevel));
//                return true;
//            }
//
//        } catch (Exception e) {
//
//        }
//        return false;
//    }
//
//    public static boolean unforceLoadEntityChunk(LivingEntity entity) {
//        if (entity.level().isClientSide()) return false;
//        ServerLevel serverLevel = (ServerLevel) entity.level();
//        UUID entityUUID = entity.getUUID();
//        CompoundTag nbt = entity.getPersistentData();
//
//        if (!nbt.getBoolean(CHUNK_LOADED_KEY)) {
//            return true;
//        }
//        LoadedChunkInfo loadInfo = ACTIVE_LOADS.get(entityUUID);
//        try {
//            boolean success;
//            if (loadInfo != null) {
//                // 使用记录的区块位置进行卸载
//                success = ForgeChunkManager.forceChunk(
//                        loadInfo.level, MOD_ID, entity,
//                        loadInfo.chunkPos.x, loadInfo.chunkPos.z, false, false
//                );
//            } else {
//                // 没有记录信息，尝试使用当前位置卸载
//                BlockPos entityPos = entity.blockPosition();
//                ChunkPos chunkPos = new ChunkPos(entityPos);
//
//                success = ForgeChunkManager.forceChunk(
//                        serverLevel, MOD_ID, entity, chunkPos.x, chunkPos.z, false, false
//                );
//            }
//
//            if (success) {
//                // 更新状态
//                nbt.putBoolean(CHUNK_LOADED_KEY, false);
//                ACTIVE_LOADS.remove(entityUUID);
//                return true;
//            } else {
//                return false;
//            }
//
//        } catch (Exception e) {
//        }
//        return false;
//    }
//
//    public static void updateChunkLoadPosition(LivingEntity entity, LoadedChunkInfo oldLoadInfo, ChunkPos newChunkPos) {
//        ServerLevel serverLevel = (ServerLevel) entity.level();
//        try {
//            // 卸载旧区块
//            boolean unloadSuccess = ForgeChunkManager.forceChunk(
//                    oldLoadInfo.level, MOD_ID, entity,
//                    oldLoadInfo.chunkPos.x, oldLoadInfo.chunkPos.z, false, false
//            );
//
//            if (!unloadSuccess) {
//                return;
//            }
//
//            // 加载新区块
//            boolean loadSuccess = ForgeChunkManager.forceChunk(
//                    serverLevel, MOD_ID, entity, newChunkPos.x, newChunkPos.z, true, true
//            );
//
//            if (loadSuccess) {
//                ACTIVE_LOADS.put(entity.getUUID(), new LoadedChunkInfo(newChunkPos, serverLevel));
//
//            }
//        } catch (Exception e) {
//
//        }
//    }
//
//    /**
//     * 清理方法：当生物异常消失时手动清理
//     */
//    public static void cleanupOrphanedLoads() {
//        for (Map.Entry<UUID, LoadedChunkInfo> entry : ACTIVE_LOADS.entrySet()) {
//            UUID entityUUID = entry.getKey();
//            LoadedChunkInfo loadInfo = entry.getValue();
//
//            // 检查生物是否还存在
//            Entity entity = loadInfo.level.getEntity(entityUUID);
//            if (entity == null || !entity.isAlive()) {
//                try {
//                    // 尝试清理孤儿加载
//                    ForgeChunkManager.forceChunk(
//                            loadInfo.level, MOD_ID, entity,
//                            loadInfo.chunkPos.x, loadInfo.chunkPos.z, false, false
//                    );
//                    ACTIVE_LOADS.remove(entityUUID);
//
//                } catch (Exception e) {
//
//                }
//            }
//        }
//    }
}
