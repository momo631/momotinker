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

public class arrogance_proof extends Item {
    public arrogance_proof(Properties properties) {
        super(properties);
    }
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<net.minecraft.network.chat.Component> list, @NotNull TooltipFlag flag) {
        boolean config = MomotinkerConfig.special_acquisition.get();
        boolean configa = MomotinkerConfig.arrogance_proof.get();
        boolean configb = MomotinkerConfig.stage_meteor.get();
        if (Screen.hasShiftDown()&&config&&configa) {
            if (StageMeteor.getStageFloat()==1||!configb) {
                list.add(Component.translatable("item.momotinker.tooltip.arrogance_proof2").withStyle(ChatFormatting.RED));
            }else {
                list.add(Component.translatable("item.momotinker.tooltip.stage_meteor").withStyle(ChatFormatting.GOLD));
            }
        }else{
            if (config&&configa) {
                list.add(net.minecraft.network.chat.Component.translatable("item.momotinker.tooltip.special_acquisition").withStyle(ChatFormatting.RED));
            }
            list.add(net.minecraft.network.chat.Component.translatable("item.momotinker.tooltip.arrogance_proof1").withStyle(ChatFormatting.RED));
        }super.appendHoverText(stack, level, list, flag);
    }
}