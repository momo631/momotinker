package com.momosensei.momotinker.event;


import com.momosensei.momotinker.capability.ender.EnderProvider;
import com.momosensei.momotinker.gui.hud.CensoredHUD;
import com.momosensei.momotinker.gui.hud.LegionHUD;
import com.momosensei.momotinker.gui.hud.ToolsTimeHUD;
import com.momosensei.momotinker.gui.overlay.MomotinkerOverlay;
import com.momosensei.momotinker.register.MomotinkerEntities;
import com.momosensei.momotinker.renderer.*;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
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
            event.registerAboveAll( "legion_phase_hud", LegionHUD.Legion_Phase_HUD);
            event.registerAboveAll( "legion_progress_hud", LegionHUD.Legion_Progress_HUD);
            event.registerAboveAll( "legion_cooldown_hud", LegionHUD.Legion_Cooldown_HUD);
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
        event.registerEntityRenderer(MomotinkerEntities.pull_a_entity.get(), PullAEntityRenderer::new);
        event.registerEntityRenderer(MomotinkerEntities.pull_b_entity.get(), PullBEntityRenderer::new);
        event.registerEntityRenderer(MomotinkerEntities.box_entity.get(), LegionRenderer::new);


        //event.registerEntityRenderer(MomotinkerEntities.mountain_painter.get(), NoopRenderer::new);
    }
}
