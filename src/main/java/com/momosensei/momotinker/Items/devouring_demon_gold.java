package com.momosensei.momotinker.Items;


import com.momosensei.momotinker.register.MomotinkerConfig;
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
        boolean config = MomotinkerConfig.special_acquisition.get();
        boolean configa = MomotinkerConfig.devouring_demon_gold.get();
        if (Screen.hasShiftDown()&&config&&configa) {
            list.add(Component.translatable("momotinker.item.tooltip.devouring_demon_gold2").withStyle(ChatFormatting.DARK_PURPLE));
        }else{
            if (config&&configa){
                list.add(Component.translatable("momotinker.item.tooltip.special_acquisition").withStyle(ChatFormatting.DARK_PURPLE));
            }
            list.add(Component.translatable("momotinker.item.tooltip.devouring_demon_gold1").withStyle(ChatFormatting.DARK_PURPLE));
        }
        super.appendHoverText(stack, level, list, flag);
    }
}