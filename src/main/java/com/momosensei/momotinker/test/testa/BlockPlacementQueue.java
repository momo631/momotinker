package com.momosensei.momotinker.test.testa;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BlockPlacementQueue {
    private static BlockPlacementQueue instance;

    public static BlockPlacementQueue getInstance() {
        if (instance == null) instance = new BlockPlacementQueue();
        return instance;
    }

    public static class BlockPlacementTask {
        public final BlockPos originalPos;
        public final BlockState state;
        public final String blockId;

        public BlockPlacementTask(BlockPos originalPos, BlockState state, String blockId) {
            this.originalPos = originalPos;
            this.state = state;
            this.blockId = blockId;
        }
    }

    private final Queue<BlockPlacementTask> placementQueue = new LinkedList<>();
    private boolean isProcessing = false;
    private int totalBlocks = 0;
    private int placedBlocks = 0;

    public void addChunkTasks(List<BlockPlacementTask> tasks) {
        placementQueue.addAll(tasks);
        this.totalBlocks = tasks.size();
        this.placedBlocks = 0;
    }

    public BlockPlacementTask getNextTask() {
        BlockPlacementTask task = placementQueue.poll();
        if (task != null) {
            placedBlocks++;
        }
        return task;
    }

    public boolean hasTasks() {
        return !placementQueue.isEmpty();
    }

    public int getQueueSize() {
        return placementQueue.size();
    }

    public void clearQueue() {
        placementQueue.clear();
        isProcessing = false;
        totalBlocks = 0;
        placedBlocks = 0;
    }

    public void startProcessing() {
        isProcessing = true;
    }

    public void stopProcessing() {
        isProcessing = false;
    }

    public boolean isProcessing() {
        return isProcessing;
    }

    // 进度查询方法
    public int getTotalBlocks() {
        return totalBlocks;
    }

    public int getPlacedBlocks() {
        return placedBlocks;
    }

    public int getRemainingBlocks() {
        return totalBlocks - placedBlocks;
    }

    public float getProgress() {
        if (totalBlocks == 0) return 0.0f;
        return (float) placedBlocks / totalBlocks;
    }

    public String getStatus() {
        if (!isProcessing) {
            return "当前没有进行中的建造任务";
        }

        if (totalBlocks == 0) {
            return "建造任务已初始化，等待开始...";
        }

        float progress = getProgress() * 100;
        return String.format("建造状态: 运行中\n进度: %.1f%% (%d/%d)\n剩余方块: %d",
                progress, placedBlocks, totalBlocks, getRemainingBlocks());
    }
}