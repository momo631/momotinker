package com.momosensei.momotinker.Modifiers.modifiers;


import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import javax.annotation.Nullable;
import java.util.List;

import static com.momosensei.momotinker.Modifiers.modifiers.OverCrystalline.crystallization;

public class Crystallization extends momomodifier{
    public Crystallization() {
    }
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(crystallization);
        return null;
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        ModDataNBT tooldata = tool.getPersistentData();
        int a = (int) tooldata.getFloat(crystallization);
        float b = (float) 4 /(a+4);
        int c = (int)1.225^a;
        if (player != null) {
            tooltip.add(net.minecraft.network.chat.Component.translatable(("当前强化点数为") + a).withStyle(ChatFormatting.AQUA));
            tooltip.add(net.minecraft.network.chat.Component.translatable(("当前强化成功率为") + b*100 +("%")).withStyle(ChatFormatting.AQUA));
            tooltip.add(net.minecraft.network.chat.Component.translatable(("当前增伤量为") + c*100 +("%")).withStyle(ChatFormatting.AQUA));
            if (a >= 6 && a < 10) {
                tooltip.add(net.minecraft.network.chat.Component.translatable("当前若强化失败会减1级！").withStyle(ChatFormatting.RED));
            }
            if (a >=10 && a < 14) {
                tooltip.add(net.minecraft.network.chat.Component.translatable("当前若强化失败会减4级！").withStyle(ChatFormatting.RED));
            }
            if (a >= 14) {
                tooltip.add(net.minecraft.network.chat.Component.translatable("小心！！当前强化失败武器会消失！").withStyle(ChatFormatting.RED));
            }
        }
    }
}