package com.momosensei.momotinker.Modifiers.modifiers;


import com.momosensei.momotinker.Modifiers.momomodifier;
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

public class Crystallization extends momomodifier {
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
        float c = (float)Math.pow(1.225,a);
        float d = (float)Math.pow(1.1,a);
        if (player != null) {
            tooltip.add(Component.translatable("modifier.momotinker.tooltip.crystallization1").append(a+"").withStyle(ChatFormatting.AQUA));
            tooltip.add(Component.translatable("modifier.momotinker.tooltip.crystallization2").append(String.format("%.0f",b*100) +"%").withStyle(ChatFormatting.AQUA));
            tooltip.add(Component.translatable("modifier.momotinker.tooltip.crystallization3").append(String.format("%.0f",c*100) +"%(").append(Component.translatable("modifier.momotinker.tooltip.crystallization7").append(String.format("%.0f",d*100)+"%)")).withStyle(ChatFormatting.AQUA));
            if (a >= 6 && a < 10) {
                tooltip.add(Component.translatable("modifier.momotinker.tooltip.crystallization4").withStyle(ChatFormatting.RED));
            }
            if (a >=10 && a < 14) {
                tooltip.add(Component.translatable("modifier.momotinker.tooltip.crystallization5").withStyle(ChatFormatting.RED));
            }
            if (a >= 14) {
                tooltip.add(Component.translatable("modifier.momotinker.tooltip.crystallization6").withStyle(ChatFormatting.RED));
            }
        }
    }
}