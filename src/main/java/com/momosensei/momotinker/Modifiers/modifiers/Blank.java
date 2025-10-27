package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.mobs.CoolTimeB;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.CoolTimeChargeB;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;


public class Blank extends momomodifier {
    public Blank() {
        MinecraftForge.EVENT_BUS.addListener(this::onEntityDeath);
    }
    @Override
    public boolean isNoLevels() {
        return true;
    }

    private void onEntityDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer player&& CoolTimeB.getCoolTime() >= 579) {
            Channel.sendToPlayer(new CoolTimeChargeB(300,false), player);
        }
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player != null&&CoolTimeB.getCoolTime()!=0) {
            tooltip.add(Component.translatable("modifier.momotinker.tooltip.blank1").append(CoolTimeB.getCoolTime()+"s").withStyle(ChatFormatting.GRAY));
        }
    }
}