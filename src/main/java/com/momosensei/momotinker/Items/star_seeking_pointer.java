package com.momosensei.momotinker.Items;

import com.momosensei.momotinker.register.MomotinkerBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;

public class star_seeking_pointer extends Item {
    private final Block targetBlock;

    public star_seeking_pointer(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.EPIC).fireResistant());
        this.targetBlock = MomotinkerBlock.meteor_nucleus_block.get();
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack,world, entity, slot, selected);
        if (!world.isClientSide) {
            if (entity instanceof Player player) {
                updateCompass(stack, world, player);
            }
        }
    }

    private void updateCompass(ItemStack stack, Level world, Player player) {
        // 查找最近的targetBlock
        BlockPos nearestPos = findNearestBlock(world, player.blockPosition(), targetBlock);

        if (nearestPos != null) {
            // 存储目标位置到NBT
            stack.getOrCreateTag().putInt("targetX", nearestPos.getX());
            stack.getOrCreateTag().putInt("targetY", nearestPos.getY());
            stack.getOrCreateTag().putInt("targetZ", nearestPos.getZ());
            stack.getOrCreateTag().putBoolean("hasTarget", true);
        } else {
            stack.getOrCreateTag().putBoolean("hasTarget", false);
        }
    }

    private BlockPos findNearestBlock(Level world, BlockPos center, Block targetBlock) {
        int radius = 128; // 搜索半径
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        BlockPos nearestPos = null;
        double nearestDistance = Double.MAX_VALUE;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    mutablePos.set(center.getX() + x, center.getY() + y, center.getZ() + z);
                    if (world.getBlockState(mutablePos).getBlock() == targetBlock) {
                        double distance = center.distSqr(mutablePos);
                        if (distance < nearestDistance) {
                            nearestDistance = distance;
                            nearestPos = mutablePos.immutable();
                        }
                    }
                }
            }
        }

        return nearestPos;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(Component.translatable("momotinker.item.tooltip.star_seeking_pointer1").withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.translatable("momotinker.item.tooltip.star_seeking_pointer2").withStyle(ChatFormatting.LIGHT_PURPLE));
        if (stack.hasTag() && stack.getTag().getBoolean("hasTarget")) {
            int x = stack.getTag().getInt("targetX");
            int y = stack.getTag().getInt("targetY");
            int z = stack.getTag().getInt("targetZ");
            tooltip.add(Component.translatable("momotinker.item.tooltip.star_seeking_pointer3").append(x+",").append(y+",").append(z+")").withStyle(ChatFormatting.LIGHT_PURPLE));
        } else {
            tooltip.add(Component.translatable("momotinker.item.tooltip.star_seeking_pointer4").withStyle(ChatFormatting.LIGHT_PURPLE));

        }
    }
}
