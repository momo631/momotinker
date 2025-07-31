package com.momosensei.momotinker.Items;

import com.momosensei.momotinker.mobs.StageMeteor;
import com.momosensei.momotinker.register.MomotinkerConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class dimensional_prism extends BlockItem {
    public dimensional_prism(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public boolean isFireResistant() {
        return true;
    }
    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        boolean config = MomotinkerConfig.special_acquisition.get();
        boolean configa = MomotinkerConfig.dimensional_prism.get();
        boolean configb = MomotinkerConfig.stage_meteor.get();
        if (Screen.hasShiftDown()&&config&&configa) {
            if (StageMeteor.getStageFloat()==1||!configb) {
                list.add(Component.translatable("momotinker.block.tooltip.dimensional_prism2").withStyle(ChatFormatting.AQUA));
            }else {
                list.add(Component.translatable("momotinker.item.tooltip.stage_meteor").withStyle(ChatFormatting.GOLD));
            }
        }else{
            list.add(Component.translatable("momotinker.block.tooltip.dimensional_prism1").withStyle(ChatFormatting.AQUA));
            if (config&&configa){
                list.add(Component.translatable("momotinker.item.tooltip.special_acquisition").withStyle(ChatFormatting.AQUA));
            }
        }
        super.appendHoverText(itemstack, world, list, flag);
    }

}