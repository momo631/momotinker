package com.momosensei.momotinker.Items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class rhythm_slime extends Item {
    public rhythm_slime(Properties properties) {
        super(properties.fireResistant());
    }
    @Override
    public boolean isFireResistant() {
        return true;
    }
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("item.momotinker.tooltip.rhythm_slime1").withStyle(ChatFormatting.LIGHT_PURPLE));
        super.appendHoverText(stack, level, list, flag);
    }
}
