package com.momosensei.momotinker.test.testa;

/*
public class ChunkBuildCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("chunkbuild")
//                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("record")
                        .executes(ChunkBuildCommands::recordChunk)
                )
                .then(Commands.literal("build")
                        .executes(context -> startBuilding(context, 20))
                        .then(Commands.argument("speed", IntegerArgumentType.integer(1, 400))
                                .executes(context -> startBuilding(context, IntegerArgumentType.getInteger(context, "speed")))
                        )
                )
                .then(Commands.literal("stop")
                        .executes(ChunkBuildCommands::stopBuilding)
                )
                .then(Commands.literal("status")
                        .executes(ChunkBuildCommands::showStatus)
                )
        );
    }

    private static int recordChunk(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        try {
            UUID fileId = ChunkDataManager.recordChunkToFile((ServerLevel) player.level, player);
            ChunkPos chunkPos = new ChunkPos(player.blockPosition());

            ItemStack blueprint = ChunkBlueprintItem.createBlueprintItem(fileId, chunkPos);

            if (player.getInventory().add(blueprint)) {
                context.getSource().sendSuccess(
                        net.minecraft.network.chat.Component.literal("区块已记录为蓝图！"),
                        true
                );
            }
            return Command.SINGLE_SUCCESS;
        } catch (Exception e) {
            context.getSource().sendFailure(net.minecraft.network.chat.Component.literal("记录失败: " + e.getMessage()));
            return 0;
        }
    }

    private static int startBuilding(CommandContext<CommandSourceStack> context, int blocksPerTick) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ServerLevel level = (ServerLevel) player.level;

        ItemStack blueprint = player.getMainHandItem();
        UUID fileId = ChunkBlueprintItem.getBlueprintFileId(blueprint);

        if (fileId == null) {
            context.getSource().sendFailure(net.minecraft.network.chat.Component.literal("请手持蓝图物品"));
            return 0;
        }

        try {
            // 使用玩家所在区块的原点作为目标原点
            ChunkPos targetChunkPos = new ChunkPos(player.blockPosition());
            BlockPos targetOrigin = new BlockPos(
                    targetChunkPos.getMinBlockX(),
                    level.getMinBuildHeight(),
                    targetChunkPos.getMinBlockZ()
            );

            int blockCount = ChunkDataManager.loadChunkDataToQueue(level, fileId, targetOrigin);

            BlockPlacementQueue.getInstance().startProcessing();

            MountainPainter placer = new MountainPainter(level,
                    targetOrigin.getX() + 0.5,
                    targetOrigin.getY(),
                    targetOrigin.getZ() + 0.5,
                    fileId);

            placer.setBlocksPerTick(blocksPerTick);
            level.addFreshEntity(placer);

            context.getSource().sendSuccess(
                    net.minecraft.network.chat.Component.literal("开始建造！每tick放置 " + blocksPerTick + " 个方块"),
                    true
            );

            return Command.SINGLE_SUCCESS;
        } catch (Exception e) {
            context.getSource().sendFailure(net.minecraft.network.chat.Component.literal("建造失败: " + e.getMessage()));
            return 0;
        }
    }

    private static int stopBuilding(CommandContext<CommandSourceStack> context) {
        BlockPlacementQueue.getInstance().clearQueue();
        context.getSource().sendSuccess(
                net.minecraft.network.chat.Component.literal("已停止建造"),
                true
        );
        return Command.SINGLE_SUCCESS;
    }

    private static int showStatus(CommandContext<CommandSourceStack> context) {
        BlockPlacementQueue queue = BlockPlacementQueue.getInstance();

        String status = queue.getStatus();
        context.getSource().sendSuccess(
                net.minecraft.network.chat.Component.literal(status),
                false
        );

        return Command.SINGLE_SUCCESS;
    }
}
*/