package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class InfiniteVitality extends momomodifier {
    public InfiniteVitality() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghealevent);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    private void livinghealevent(LivingHealEvent event) {
        LivingEntity living = event.getEntity();
        if (living instanceof Player player) {
            int d = getAllModifierlevel(player, MomotinkerModifiers.infinitevitality.getId());
            if (d>0) {
                event.setAmount(event.getAmount() * 1.25F);
            }
        }
    }
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player != null) {
            tooltip.add(net.minecraft.network.chat.Component.translatable("modifier.momotinker.tooltip.infinitevitality1").append(25+"%").withStyle(ChatFormatting.GREEN));
        }
    }
}