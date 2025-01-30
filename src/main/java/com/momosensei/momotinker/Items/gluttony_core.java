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

public class gluttony_core extends Item {
    public gluttony_core(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> list, @NotNull TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            list.add(Component.translatable("momotinker.item.tooltip.gluttony_core3").withStyle(ChatFormatting.DARK_GREEN));
        }else{
            list.add(net.minecraft.network.chat.Component.translatable("momotinker.item.tooltip.gluttony_core1").withStyle(ChatFormatting.DARK_GREEN));
            list.add(net.minecraft.network.chat.Component.translatable("momotinker.item.tooltip.gluttony_core2").withStyle(ChatFormatting.DARK_GREEN));
        }
        super.appendHoverText(stack, level, list, flag);
    }
}
