package com.momosensei.momotinker.event;

import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.data.gungnir.GungnirTrajectoryClient;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Momotinker.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE,  value = {Dist.CLIENT})
public class ClientForgeEvent {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            GungnirTrajectoryClient.tick();
        }
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;
        Minecraft mc = Minecraft.getInstance();
        GungnirTrajectoryClient.render(
                event.getPoseStack(),
                mc.renderBuffers().bufferSource(),
                event.getPartialTick());
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) {
            GungnirTrajectoryClient.clear();
        }
    }

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        GungnirTrajectoryClient.clear();
    }
}
