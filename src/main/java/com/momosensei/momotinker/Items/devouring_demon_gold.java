package com.momosensei.momotinker.Items;


import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class devouring_demon_gold extends Item {
    public devouring_demon_gold(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> list, @NotNull TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            list.add(Component.translatable("momotinker.item.tooltip.devouring_demon_gold3").withStyle(ChatFormatting.DARK_PURPLE));
        }else{
            list.add(Component.translatable("momotinker.item.tooltip.devouring_demon_gold1").withStyle(ChatFormatting.DARK_PURPLE));
            list.add(Component.translatable("momotinker.item.tooltip.devouring_demon_gold2").withStyle(ChatFormatting.DARK_PURPLE));
        }
        super.appendHoverText(stack, level, list, flag);
    }
}