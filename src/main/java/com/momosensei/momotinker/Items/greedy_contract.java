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

public class greedy_contract extends Item{
    public greedy_contract(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> list, @NotNull TooltipFlag flag) {
        boolean config = MomotinkerConfig.special_acquisition.get();
        boolean configa = MomotinkerConfig.greedy_contract.get();
        if (Screen.hasShiftDown() && config && configa) {
            list.add(Component.translatable("momotinker.item.tooltip.greedy_contract2").withStyle(ChatFormatting.BLUE));
        } else {
            list.add(net.minecraft.network.chat.Component.translatable("momotinker.item.tooltip.greedy_contract1").withStyle(ChatFormatting.BLUE));
            if (config && configa) {
                list.add(net.minecraft.network.chat.Component.translatable("item.tooltip.special_acquisition").withStyle(ChatFormatting.BLUE));
            }
        }
        super.appendHoverText(stack, level, list, flag);
    }
}
