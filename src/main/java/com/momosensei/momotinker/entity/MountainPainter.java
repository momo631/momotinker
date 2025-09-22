package com.momosensei.momotinker.entity;


/*
public class MountainPainter extends Projectile {

    private UUID blueprintId;
    private int blocksPerTick = 10;


    public MountainPainter(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
        setupEntity();
    }

    public MountainPainter(Level level, double x, double y, double z, UUID blueprintId) {
        this(MomotinkerEntities.mountain_painter.get(), level);
        this.setPos(x, y, z);
        this.blueprintId = blueprintId;
    }

    private void setupEntity() {
        this.noPhysics = true;
        this.setInvulnerable(true);
        this.setNoGravity(true);
        this.setInvisible(true);
        this.setSilent(true);
    }
    @Override
    public void tick() {
        super.tick();

        if (!this.level.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) this.level;
            BlockPlacementQueue queue = BlockPlacementQueue.getInstance();
            PlacementContextManager.BlockPlacementContext context = PlacementContextManager.getContext(blueprintId);

            if (context != null && queue.hasTasks() && queue.isProcessing()) {

                for (int i = 0; i < blocksPerTick; i++) {
                    BlockPlacementQueue.BlockPlacementTask task = queue.getNextTask();
                    if (task == null) break;

                    BlockPos targetPos = calculateTargetPosition(task.originalPos, this.blockPosition(), context);

                    // 使用安全的放置方法
                    safeSetBlock(serverLevel, targetPos, task.state);
                }
                if (!queue.hasTasks()) {
                    finishBuilding(serverLevel);
                }
            }
        }
    }
    private boolean safeSetBlock(ServerLevel serverLevel, BlockPos targetPos, BlockState state) {
        // 检查区块是否加载
        if (!serverLevel.isLoaded(targetPos)) {
            // 尝试加载区块
            ChunkPos chunkPos = new ChunkPos(targetPos);
            serverLevel.getChunkSource().getChunk(chunkPos.x, chunkPos.z, true);

            if (!serverLevel.isLoaded(targetPos)) {
                System.out.println("无法加载区块: " + chunkPos);
                return false;
            }
        }

        // 检查坐标是否有效
        if (targetPos.getY() < serverLevel.getMinBuildHeight() ||
                targetPos.getY() >= serverLevel.getMaxBuildHeight()) {
            System.out.println("无效的Y坐标: " + targetPos.getY());
            return false;
        }

        // 检查方块是否可以在此位置存在
        if (!state.canSurvive(serverLevel, targetPos)) {
            System.out.println("方块无法在位置存活: " + targetPos);
            // 可以尝试使用默认状态
            state = state.getBlock().defaultBlockState();
        }

        // 放置方块
        boolean success = serverLevel.setBlock(targetPos, state, Block.UPDATE_ALL);

        if (!success) {
            System.out.println("方块放置失败: " + targetPos);
        }

        return success;
    }
    private BlockPos calculateTargetPosition(BlockPos storedPos, BlockPos targetOrigin, PlacementContextManager.BlockPlacementContext context) {
        // 1. 计算相对于记录原点的偏移
        int dx = storedPos.getX() - context.recordedOrigin.getX();
        int dy = storedPos.getY() - context.recordedOrigin.getY();
        int dz = storedPos.getZ() - context.recordedOrigin.getZ();

        // 2. 根据初始朝向正确旋转偏移
        int rotatedDx = dx;
        int rotatedDz = dz;

        switch (context.initialFacing) {
            case EAST:  // 东 → 顺时针90度
                rotatedDx = dz;     // 正确：X = 原Z
                rotatedDz = -dx;    // 正确：Z = -原X
                break;
            case SOUTH: // 南 → 180度
                rotatedDx = -dx;    // 正确：X = -原X
                rotatedDz = -dz;    // 正确：Z = -原Z
                break;
            case WEST:  // 西 → 逆时针90度
                rotatedDx = -dz;    // 正确：X = -原Z
                rotatedDz = dx;     // 正确：Z = 原X
                break;
            case NORTH: // 北 → 不变
            default:
                rotatedDx = dx;
                rotatedDz = dz;
                break;
        }

        // 3. 计算目标位置
        int targetX = targetOrigin.getX() + rotatedDx;
        int targetY = targetOrigin.getY() + dy;
        int targetZ = targetOrigin.getZ() + rotatedDz;

        // 4. 确保坐标在有效范围内（可选但推荐）
        ServerLevel serverLevel = (ServerLevel) this.level;
        if (targetY < serverLevel.getMinBuildHeight()) {
            targetY = serverLevel.getMinBuildHeight();
        } else if (targetY >= serverLevel.getMaxBuildHeight()) {
            targetY = serverLevel.getMaxBuildHeight() - 1;
        }

        return new BlockPos(targetX, targetY, targetZ);
    }

    private void finishBuilding(ServerLevel serverLevel) {
        BlockPlacementQueue queue = BlockPlacementQueue.getInstance();
        serverLevel.getServer().sendSystemMessage(
                net.minecraft.network.chat.Component.literal("方块放置完成！")
        );
        PlacementContextManager.removeContext(blueprintId);
        this.discard();
        queue.stopProcessing();
    }

    @Override
    protected void defineSynchedData() {}
    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compound) {
        if (compound.hasUUID("blueprintId")) this.blueprintId = compound.getUUID("blueprintId");
        this.blocksPerTick = compound.getInt("blocksPerTick");
    }
    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compound) {
        if (this.blueprintId != null) compound.putUUID("blueprintId", this.blueprintId);
        compound.putInt("blocksPerTick", this.blocksPerTick);
    }
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return (Packet<ClientGamePacketListener>) NetworkHooks.getEntitySpawningPacket(this);
    }

    public void setBlocksPerTick(int count) { this.blocksPerTick = Math.max(1, count); }
}
*/