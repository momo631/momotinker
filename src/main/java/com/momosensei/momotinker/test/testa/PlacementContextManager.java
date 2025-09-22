package com.momosensei.momotinker.test.testa;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ChunkPos;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlacementContextManager {
    private static final Map<UUID, BlockPlacementContext> contexts = new HashMap<>();

    public static class BlockPlacementContext {
        public final BlockPos recordedOrigin;
        public final Direction initialFacing;
        public final UUID blueprintId;
        public final ChunkPos recordedChunkPos;

        public BlockPlacementContext(BlockPos recordedOrigin, Direction initialFacing,
                                     UUID blueprintId, ChunkPos recordedChunkPos) {
            this.recordedOrigin = recordedOrigin;
            this.initialFacing = initialFacing;
            this.blueprintId = blueprintId;
            this.recordedChunkPos = recordedChunkPos;
        }
    }

    public static void addContext(UUID blueprintId, BlockPlacementContext context) {
        contexts.put(blueprintId, context);
    }

    public static BlockPlacementContext getContext(UUID blueprintId) {
        return contexts.get(blueprintId);
    }

    public static void removeContext(UUID blueprintId) {
        contexts.remove(blueprintId);
    }
}
