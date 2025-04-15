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

public class arrogance_proof extends Item {
    public arrogance_proof(Properties properties) {
        super(properties);
    }
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<net.minecraft.network.chat.Component> list, @NotNull TooltipFlag flag) {
        boolean config = MomotinkerConfig.special_acquisition.get();
        boolean configb = MomotinkerConfig.arrogance_proof.get();
        if (Screen.hasShiftDown()&&config&&configb) {
            list.add(Component.translatable("momotinker.item.tooltip.arrogance_proof2").withStyle(ChatFormatting.RED));
        }else{
            if (config&&configb) {
                list.add(net.minecraft.network.chat.Component.translatable("item.tooltip.special_acquisition").withStyle(ChatFormatting.RED));
            }
            list.add(net.minecraft.network.chat.Component.translatable("momotinker.item.tooltip.arrogance_proof1").withStyle(ChatFormatting.RED));
        }super.appendHoverText(stack, level, list, flag);
    }
}