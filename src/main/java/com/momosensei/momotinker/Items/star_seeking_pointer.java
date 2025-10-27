package com.momosensei.momotinker.Items;

import com.momosensei.momotinker.register.MomotinkerBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class star_seeking_pointer extends Item {
    private final Block targetBlock;

    public star_seeking_pointer(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.EPIC).fireResistant());
        this.targetBlock = MomotinkerBlock.meteor_nucleus_block.get();
    }
    @Override
    public int getUseDuration(ItemStack stack) {
        return 1;
    }
    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }
    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        ItemStack retval = super.finishUsingItem(stack, level, living);
        if (living instanceof Player player) {
            updateCompass(stack, level, player);
        }
        return retval;
    }
    private void updateCompass(ItemStack stack, Level world, Player player) {
        BlockPos nearestPos = findNearestBlockOptimized(world, player.blockPosition());
        CompoundTag tag = stack.getOrCreateTag();

        if (nearestPos != null) {
            tag.putInt("targetX", nearestPos.getX());
            tag.putInt("targetY", nearestPos.getY());
            tag.putInt("targetZ", nearestPos.getZ());
            tag.putBoolean("hasTarget", true);
        } else {
            tag.putBoolean("hasTarget", false);
        }
    }

    private BlockPos findNearestBlockOptimized(Level world, BlockPos center) {
        int maxRadius = 128;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int radius = 1; radius <= maxRadius; radius++) {
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        if (Math.abs(x) != radius && Math.abs(y) != radius && Math.abs(z) != radius) {
                            continue;
                        }

                        mutablePos.set(center.getX() + x, center.getY() + y, center.getZ() + z);
                        if (world.getBlockState(mutablePos).getBlock() == MomotinkerBlock.meteor_nucleus_block.get()) {
                            return mutablePos.immutable();
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(Component.translatable("item.momotinker.tooltip.star_seeking_pointer1").withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.translatable("item.momotinker.tooltip.star_seeking_pointer2").withStyle(ChatFormatting.LIGHT_PURPLE));

        CompoundTag tag = stack.getTag();
        if (tag != null && tag.getBoolean("hasTarget")) {
            int x = tag.getInt("targetX");
            int y = tag.getInt("targetY");
            int z = tag.getInt("targetZ");
            tooltip.add(Component.translatable("item.momotinker.tooltip.star_seeking_pointer3").append(Component.literal(x + "," + y + "," + z + ")").withStyle(ChatFormatting.GOLD)).withStyle(ChatFormatting.LIGHT_PURPLE));
        } else {
            tooltip.add(Component.translatable("item.momotinker.tooltip.star_seeking_pointer4").withStyle(ChatFormatting.LIGHT_PURPLE));

        }
    }
}
