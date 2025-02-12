package com.momosensei.momotinker.renderer;


import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;


@EventBusSubscriber(modid = Momotinker.MOD_ID, value = {Dist.CLIENT}, bus = Bus.MOD)
public class MomotinkerEntityRenderers {

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MomotinkerEntities.trigger_slash_a.get(), triggerSlashRenderer::new);
    }
}

