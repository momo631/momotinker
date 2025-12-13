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

public class compassion_mask extends Item {
    public compassion_mask(Properties properties) {
        super(properties);
    }
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        boolean config = MomotinkerConfig.special_acquisition.get();
        boolean configa = MomotinkerConfig.compassion_mask.get();
        boolean configb = MomotinkerConfig.stage_meteor.get();
        if (Screen.hasShiftDown()&&config&&configa) {
            if (StageMeteor.getStageFloat()==1) {
                list.add(Component.translatable("item.momotinker.tooltip.compassion_mask2").withStyle(ChatFormatting.GRAY));
            }else {
                list.add(Component.translatable("item.momotinker.tooltip.stage_meteor").withStyle(ChatFormatting.GOLD));
            }
        }else {
            list.add(Component.translatable("item.momotinker.tooltip.compassion_mask1").withStyle(ChatFormatting.GRAY));
            if (config&&configa) {
                list.add(Component.translatable("item.momotinker.tooltip.special_acquisition").withStyle(ChatFormatting.GRAY));
            }
        }
    }
}