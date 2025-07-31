package com.momosensei.momotinker.Items;


import com.momosensei.momotinker.mobs.StageMeteor;
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

public class lust_mirror extends Item {
    public lust_mirror(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<net.minecraft.network.chat.Component> list, @NotNull TooltipFlag flag) {
        boolean config = MomotinkerConfig.special_acquisition.get();
        boolean configa = MomotinkerConfig.lust_mirror.get();
        boolean configb = MomotinkerConfig.stage_meteor.get();
        if (Screen.hasShiftDown()&&config&&configa) {
            if (StageMeteor.getStageFloat() == 1 || !configb) {
                list.add(Component.translatable("momotinker.item.tooltip.lust_mirror2").withStyle(ChatFormatting.DARK_PURPLE));
            }else {
                list.add(Component.translatable("momotinker.item.tooltip.stage_meteor").withStyle(ChatFormatting.GOLD));
            }
        }else {
            list.add(net.minecraft.network.chat.Component.translatable("momotinker.item.tooltip.lust_mirror1").withStyle(ChatFormatting.DARK_PURPLE));
            if (config && configa) {
                list.add(net.minecraft.network.chat.Component.translatable("momotinker.item.tooltip.special_acquisition").withStyle(ChatFormatting.DARK_PURPLE));
            }
        }
        super.appendHoverText(stack, level, list, flag);
    }
}