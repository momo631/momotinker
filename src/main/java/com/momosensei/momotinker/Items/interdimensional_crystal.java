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

public class interdimensional_crystal extends Item {
    public interdimensional_crystal(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<net.minecraft.network.chat.Component> list, @NotNull TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            list.add(Component.translatable("momotinker.item.tooltip.interdimensional_crystal3").withStyle(ChatFormatting.LIGHT_PURPLE));
        }else{
            list.add(net.minecraft.network.chat.Component.translatable("momotinker.item.tooltip.interdimensional_crystal1").withStyle(ChatFormatting.LIGHT_PURPLE));
            list.add(net.minecraft.network.chat.Component.translatable("momotinker.item.tooltip.interdimensional_crystal2").withStyle(ChatFormatting.LIGHT_PURPLE));
        }
        super.appendHoverText(stack, level, list, flag);
    }

}