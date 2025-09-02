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

public class lightning_strike_wood extends BlockItem {
    public lightning_strike_wood(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public boolean isFireResistant() {
        return true;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        boolean config = MomotinkerConfig.special_acquisition.get();
        boolean configa = MomotinkerConfig.lightning_strike_wood.get();
        boolean configb = MomotinkerConfig.stage_meteor.get();
        if (Screen.hasShiftDown()&&config&&configa) {
            if (StageMeteor.getStageFloat()==1||!configb) {
                list.add(Component.translatable("block.momotinker.tooltip.lightning_strike_wood2"));
            }else {
                list.add(Component.translatable("item.momotinker.tooltip.stage_meteor").withStyle(ChatFormatting.GOLD));
            }
        }else{
            list.add(Component.translatable("block.momotinker.tooltip.lightning_strike_wood1"));
            if (config&&configa){
                list.add(Component.translatable("item.momotinker.tooltip.special_acquisition"));
            }
        }
        super.appendHoverText(itemstack, world, list, flag);
    }

}