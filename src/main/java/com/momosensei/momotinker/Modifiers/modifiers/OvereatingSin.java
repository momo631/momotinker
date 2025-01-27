package com.momosensei.momotinker.Modifiers.modifiers;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

public class OvereatingSin extends momomodifier {
    public OvereatingSin() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghealevent);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    private void livinghealevent(LivingHealEvent event) {
        LivingEntity living = event.getEntity();
        if (living instanceof Player player) {
            for (ItemStack stack:player.getInventory().items) {
            if (stack.getItem() instanceof ModifiableItem) {
                ToolStack tool = ToolStack.from(stack);
                    if (tool.getModifierLevel(this)>0){
                        event.setAmount(event.getAmount() * 0.5F);
                    }
                }
            break;
            }
        }
    }
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player != null) {
            tooltip.add(net.minecraft.network.chat.Component.translatable("已减少治疗效果" + (50)+"%").withStyle(ChatFormatting.DARK_GREEN));
        }
    }
}