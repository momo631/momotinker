package com.momosensei.momotinker.Modifiers.PlasmaSaber;

import com.momosensei.momotinker.Modifiers.modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;


public class OverAngerSin extends momomodifier {
    public OverAngerSin() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    private void livinghurtevent(LivingHurtEvent event) {
        LivingEntity living = event.getEntity();
        int a = ModifierUtil.getModifierLevel(living.getItemBySlot(EquipmentSlot.MAINHAND), MomotinkerModifiers.overangersin.getId());
        if (living instanceof Player player&& a > 0) {
            event.setAmount(event.getAmount() * (1F + a*0.1F));
        }
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player != null) {
            int a = ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.MAINHAND), MomotinkerModifiers.overangersin.getId());
            tooltip.add(net.minecraft.network.chat.Component.translatable("格外承受伤害" + (a*10)+"%").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

}