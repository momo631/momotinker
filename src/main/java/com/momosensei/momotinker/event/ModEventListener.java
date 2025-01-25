package com.momosensei.momotinker.event;

import com.momosensei.momotinker.capability.healpercentage.PlayerHealPercentageProvider;
import com.momosensei.momotinker.gui.overlay.PlayerHealPercentageOverlay;
import com.momosensei.momotinker.key.key;
import com.momosensei.momotinker.network.Channel;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,modid = "momotinker")
public class ModEventListener {

    @SubscribeEvent
    public static void registerCapability(RegisterCapabilitiesEvent event) {
        event.register(PlayerHealPercentageProvider.class);
    }

    @SubscribeEvent
    public static void registerOverlay(RegisterGuiOverlaysEvent event){
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(),"player_heal_percentage", PlayerHealPercentageOverlay.PLAYER_HEAL_PERCENTAGE);
    }

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(key.KeyBinding.KEY);
        Channel.register();
    }
}
