package com.momosensei.momotinker.Items;


import com.momosensei.momotinker.mobs.StageMeteor;
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

public class core_of_felony extends Item {
    public core_of_felony(Properties properties) {
        super(properties.fireResistant().stacksTo(16));
    }
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            if (StageMeteor.getStageFloat()==1) {
                list.add(Component.translatable("momotinker.item.tooltip.core_of_felony3").withStyle(ChatFormatting.GRAY));
            }else {
                list.add(Component.translatable("momotinker.item.tooltip.stage_meteor").withStyle(ChatFormatting.GOLD));
            }
        }else {
            list.add(Component.translatable("momotinker.item.tooltip.core_of_felony1").withStyle(ChatFormatting.GRAY));
            list.add(Component.translatable("momotinker.item.tooltip.core_of_felony2").withStyle(ChatFormatting.GRAY));
            list.add(Component.translatable("momotinker.item.tooltip.special_acquisition").withStyle(ChatFormatting.GRAY));
        }
    }

}