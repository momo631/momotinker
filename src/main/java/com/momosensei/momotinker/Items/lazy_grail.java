package com.momosensei.momotinker.Items;


import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class lazy_grail extends Item {
    public lazy_grail(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<net.minecraft.network.chat.Component> list, @NotNull TooltipFlag flag) {
//        if (Screen.hasShiftDown()) {
//            list.add(Component.translatable("momotinker.item.tooltip.lazy_grail3").withStyle(ChatFormatting.GOLD));
//        }else{
            list.add(net.minecraft.network.chat.Component.translatable("momotinker.item.tooltip.lazy_grail1").withStyle(ChatFormatting.GOLD));
//            list.add(net.minecraft.network.chat.Component.translatable("momotinker.item.tooltip.lazy_grail2").withStyle(ChatFormatting.GOLD));
//        }
        super.appendHoverText(stack, level, list, flag);
    }

}