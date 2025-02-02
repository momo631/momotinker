package com.momosensei.momotinker.Items;


import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class rage_stone_statue extends Item {
    public rage_stone_statue(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<net.minecraft.network.chat.Component> list, @NotNull TooltipFlag flag) {
//        if (Screen.hasShiftDown()) {
//            list.add(Component.translatable("momotinker.item.tooltip.rage_stone_statue3").withStyle(ChatFormatting.DARK_GRAY));
//        }else{
            list.add(net.minecraft.network.chat.Component.translatable("momotinker.item.tooltip.rage_stone_statue1").withStyle(ChatFormatting.DARK_GRAY));
//            list.add(net.minecraft.network.chat.Component.translatable("momotinker.item.tooltip.rage_stone_statue2").withStyle(ChatFormatting.DARK_GRAY));
//        }
        super.appendHoverText(stack, level, list, flag);
    }
}