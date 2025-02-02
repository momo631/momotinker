package com.momosensei.momotinker.Items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class dimensional_prism extends BlockItem {
    public dimensional_prism(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            list.add(Component.translatable("momotinker.block.tooltip.dimensional_prism3").withStyle(ChatFormatting.AQUA));
        }else{
            list.add(Component.translatable("momotinker.block.tooltip.dimensional_prism1").withStyle(ChatFormatting.AQUA));
            list.add(Component.translatable("momotinker.block.tooltip.dimensional_prism2").withStyle(ChatFormatting.AQUA));
        }
        super.appendHoverText(itemstack, world, list, flag);
    }

}