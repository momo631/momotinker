package com.momosensei.momotinker.Items;


import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class nihilism extends Item {
    public nihilism(Properties properties) {
        super(properties);
    }
    @Override
    public boolean isFireResistant() {
        return true;
    }
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> list, @NotNull TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            list.add(Component.translatable("momotinker.item.tooltip.nihilism2"));
        }else{
            list.add(Component.translatable("momotinker.item.tooltip.nihilism1"));
            list.add(Component.translatable("momotinker.item.tooltip.special_acquisition"));
        }
        super.appendHoverText(stack, level, list, flag);
    }

}