package com.momosensei.momotinker.test.testa;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChunkDataManager {
    private static final String DATA_DIR_NAME = "chunk_data";

    public static UUID recordChunkToFile(ServerLevel level, ServerPlayer player) throws IOException {
        ChunkPos chunkPos = new ChunkPos(player.blockPosition());
        LevelChunk chunk = level.getChunkSource().getChunk(chunkPos.x, chunkPos.z, false);

        CompoundTag data = new CompoundTag();
        data.putInt("chunkX", chunkPos.x);
        data.putInt("chunkZ", chunkPos.z);
        data.putString("playerFacing", player.getDirection().getName());

        // 记录区块原点坐标
        BlockPos chunkOrigin = new BlockPos(
                chunkPos.getMinBlockX(),
                level.getMinBuildHeight(),
                chunkPos.getMinBlockZ()
        );

        CompoundTag originTag = new CompoundTag();
        originTag.putInt("x", chunkOrigin.getX());
        originTag.putInt("y", chunkOrigin.getY());
        originTag.putInt("z", chunkOrigin.getZ());
        data.put("chunkOrigin", originTag);

        ListTag blocks = new ListTag();
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();

        for (int y = minY; y < maxY; y++) {
            for (int x = chunkPos.getMinBlockX(); x <= chunkPos.getMaxBlockX(); x++) {
                for (int z = chunkPos.getMinBlockZ(); z <= chunkPos.getMaxBlockZ(); z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = chunk.getBlockState(pos);

                    CompoundTag blockTag = new CompoundTag();
                    blockTag.putInt("x", x);
                    blockTag.putInt("y", y);
                    blockTag.putInt("z", z);
                    blockTag.putString("block", ForgeRegistries.BLOCKS.getKey(state.getBlock()).toString());

                    blocks.add(blockTag);
                }
            }
        }

        data.put("blocks", blocks);

        UUID fileId = UUID.randomUUID();
        Path dataDir = getDataDirectory(level);
        Files.createDirectories(dataDir);

        Path filePath = dataDir.resolve(fileId.toString() + ".nbt");
        NbtIo.writeCompressed(data, filePath.toFile());

        return fileId;
    }

    public static int loadChunkDataToQueue(ServerLevel level, UUID fileId, BlockPos targetOrigin) throws IOException {
        Path dataDir = getDataDirectory(level);
        Path filePath = dataDir.resolve(fileId.toString() + ".nbt");

        CompoundTag data = NbtIo.readCompressed(filePath.toFile());

        Direction facing = Direction.byName(data.getString("playerFacing"));
        if (facing == null) facing = Direction.NORTH;

        ChunkPos recordedChunkPos = new ChunkPos(data.getInt("chunkX"), data.getInt("chunkZ"));

        CompoundTag originTag = data.getCompound("chunkOrigin");
        BlockPos recordedOrigin = new BlockPos(
                originTag.getInt("x"),
                originTag.getInt("y"),
                originTag.getInt("z")
        );

        // 创建放置上下文
        PlacementContextManager.BlockPlacementContext context =
                new PlacementContextManager.BlockPlacementContext(recordedOrigin, facing, fileId, recordedChunkPos);
        PlacementContextManager.addContext(fileId, context);

        // 加载方块数据到队列
        ListTag blocks = data.getList("blocks", CompoundTag.TAG_COMPOUND);
        List<BlockPlacementQueue.BlockPlacementTask> tasks = new ArrayList<>();

        for (int i = 0; i < blocks.size(); i++) {
            CompoundTag blockTag = blocks.getCompound(i);
            BlockPos pos = new BlockPos(
                    blockTag.getInt("x"),
                    blockTag.getInt("y"),
                    blockTag.getInt("z")
            );

            String blockId = blockTag.getString("block");
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockId));

            if (block != null) {
                BlockState state = block.defaultBlockState();
                tasks.add(new BlockPlacementQueue.BlockPlacementTask(pos, state, blockId));
            }
        }

        BlockPlacementQueue.getInstance().addChunkTasks(tasks);
        return tasks.size();
    }

    private static Path getDataDirectory(ServerLevel level) {
        return level.getServer().getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT)
                .resolve(DATA_DIR_NAME);
    }
}