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

public class arriving_at_the_other_shore extends Item {
    public arriving_at_the_other_shore(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<net.minecraft.network.chat.Component> list, @NotNull TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            list.add(Component.translatable("momotinker.item.tooltip.arriving_at_the_other_shore3").withStyle(ChatFormatting.DARK_RED));
        }else{
            list.add(net.minecraft.network.chat.Component.translatable("momotinker.item.tooltip.arriving_at_the_other_shore1").withStyle(ChatFormatting.DARK_GRAY));
            list.add(net.minecraft.network.chat.Component.translatable("momotinker.item.tooltip.arriving_at_the_other_shore2").withStyle(ChatFormatting.DARK_RED));
        }
        super.appendHoverText(stack, level, list, flag);
    }

}