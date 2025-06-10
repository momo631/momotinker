package com.momosensei.momotinker.Items;


import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class reversetime_purple_gold extends Item {
    public reversetime_purple_gold(Properties properties) {
        super(properties);
    }
    @Override
    public boolean isFireResistant() {
        return true;
    }
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("momotinker.item.tooltip.reversetime_purple_gold1"));
        list.add(Component.translatable("momotinker.item.tooltip.reversetime_purple_gold2"));
        super.appendHoverText(stack, level, list, flag);
    }

}