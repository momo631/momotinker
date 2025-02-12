package com.momosensei.momotinker.event;

import com.momosensei.momotinker.capability.healpercentage.EnderProvider;
import com.momosensei.momotinker.gui.overlay.EnderOverlay;
import com.momosensei.momotinker.key.key;
import com.momosensei.momotinker.mobs.TriggerBladeHUD;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.register.MomotinkerEntities;
import com.momosensei.momotinker.renderer.triggerSlashRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,modid = "momotinker")
public class ModEventListener {

    @SubscribeEvent
    public static void registerCapability(RegisterCapabilitiesEvent event) {
        event.register(EnderProvider.class);
    }

    @SubscribeEvent
    public static void registerOverlay(RegisterGuiOverlaysEvent event) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "ender", EnderOverlay.ENDER);
            event.registerAboveAll( "trigger_blade_hud", TriggerBladeHUD.TRIGGER_BLADE_HUD);
        }
    }
    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MomotinkerEntities.trigger_slash_a.get(), triggerSlashRenderer::new);
    }

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(key.KeyBinding.KEY);
        Channel.init();
    }
}
