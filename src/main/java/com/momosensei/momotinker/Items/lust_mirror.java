package com.momosensei.momotinker.Items;


import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class lust_mirror extends Item {
    public lust_mirror(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<net.minecraft.network.chat.Component> list, @NotNull TooltipFlag flag) {
        list.add(net.minecraft.network.chat.Component.translatable("momotinker.item.tooltip.lust_mirror1").withStyle(ChatFormatting.DARK_PURPLE));
        super.appendHoverText(stack, level, list, flag);
    }
}