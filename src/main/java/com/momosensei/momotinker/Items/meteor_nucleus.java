package com.momosensei.momotinker.Items;


import com.momosensei.momotinker.register.MomotinkerConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class meteor_nucleus extends Item {
    public meteor_nucleus(Properties properties) {
        super(properties);
    }
    @Override
    public boolean isFireResistant() {
        return true;
    }
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> list, @NotNull TooltipFlag flag) {
        boolean config = MomotinkerConfig.special_acquisition.get();
        boolean configa = MomotinkerConfig.meteor_nucleus.get();
        list.add(Component.translatable("momotinker.item.tooltip.meteor_nucleus3").withStyle(ChatFormatting.GOLD));
        if (config&&configa) {
            list.add(Component.translatable("momotinker.item.tooltip.meteor_nucleus4").withStyle(ChatFormatting.GOLD));
        }
    }
}