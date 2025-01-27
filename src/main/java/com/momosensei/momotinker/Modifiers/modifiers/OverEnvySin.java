package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;


public class OverEnvySin extends momomodifier {
    public OverEnvySin() {
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player != null) {
            int a = ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.MAINHAND), MomotinkerModifiers.overenvysin.getId());
            tooltip.add(net.minecraft.network.chat.Component.translatable("目前增伤可提供最大攻击力为" + (a*a*100)).withStyle(ChatFormatting.YELLOW));
        }
    }
}