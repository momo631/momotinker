package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;


public class YearsInWeiqi extends momomodifier {
    public YearsInWeiqi() {
    }

    public static final ResourceLocation in_weiqi = Momotinker.getResource("in_weiqi");
    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Override
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(in_weiqi);
        return null;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (world.isClientSide) return;
        if (entity instanceof Player player && getMainhandModifierlevel(player, MomotinkerModifiers.years_in_weiqi.getId()) != 0) {
            ModDataNBT data = ToolStack.from(player.getMainHandItem()).getPersistentData();
            if (stack != player.getMainHandItem()) tool.getPersistentData().putInt(in_weiqi, 0);
            player.getFoodData().setExhaustion(player.getFoodData().getExhaustionLevel() * (0.25f+0.01f*data.getInt(in_weiqi)));
            if (player.getFoodData().getSaturationLevel()<=0)return;
            AABB bBox = player.getBoundingBox().inflate(8);
            int bonusTicks = 1 + data.getInt(in_weiqi);
            if (player.tickCount%20==0&&data.getInt(in_weiqi) < 361) {
                data.putInt(in_weiqi, data.getInt(in_weiqi) + 1);
            }
            speedUpRandomTicksOptimized(world, bonusTicks, bBox);
        }
    }
    private void speedUpRandomTicksOptimized(Level level, int bonusTicks, AABB box) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        BlockPos.betweenClosedStream(
                BlockPos.containing(box.minX, box.minY, box.minZ),
                BlockPos.containing(box.maxX, box.maxY, box.maxZ)
        ).forEach(pos -> {
            BlockState blockState = level.getBlockState(pos);

            if (blockState.isRandomlyTicking() && level.random.nextInt(40) == 0) {
                for (int i = 0; i < bonusTicks; i++) {
                    blockState.randomTick(serverLevel, pos, level.random);
                }
            }
        });
    }
}