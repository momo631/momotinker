package com.momosensei.momotinker.event;


import com.momosensei.momotinker.capability.ender.EnderProvider;
import com.momosensei.momotinker.gui.overlay.CensoredHUD;
import com.momosensei.momotinker.gui.overlay.MomotinkerOverlay;
import com.momosensei.momotinker.gui.overlay.ToolsTimeHUD;
import com.momosensei.momotinker.key.key;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.register.MomotinkerEntities;
import com.momosensei.momotinker.renderer.*;
import net.minecraft.client.renderer.entity.NoopRenderer;
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
            event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "ender", MomotinkerOverlay::render);
            event.registerAboveAll( "censored_hud", CensoredHUD.CENSORED);
            event.registerAboveAll( "tools_time_hud", ToolsTimeHUD.TOOS_TIME_HUD);
        }
    }
    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MomotinkerEntities.trigger_slash_a.get(), triggerSlashRenderer::new);
        event.registerEntityRenderer(MomotinkerEntities.spear_entity.get(), SpearEntityRenderer::new);
        event.registerEntityRenderer(MomotinkerEntities.cleanse_entity.get(), CleanseEntityRenderer::new);
        event.registerEntityRenderer(MomotinkerEntities.ray_entity.get(), NoopRenderer::new);
        event.registerEntityRenderer(MomotinkerEntities.meteor_entity.get(), MeteorEntityRenderer::new);
        event.registerEntityRenderer(MomotinkerEntities.starfall_entity.get(), StarfallEntityRenderer::new);
    }

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(key.KeyBinding.KEY);
        event.register(key.KeyBinding.KEYA);
        Channel.init();
    }
}
