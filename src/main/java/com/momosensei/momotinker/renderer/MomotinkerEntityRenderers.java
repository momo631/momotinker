package com.momosensei.momotinker.renderer;


import com.momosensei.momotinker.register.MomotinkerEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import static com.momosensei.momotinker.Momotinker.MOD_ID;


@EventBusSubscriber(modid = MOD_ID, value = {Dist.CLIENT}, bus = Bus.MOD)
public class MomotinkerEntityRenderers {
    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MomotinkerEntities.trigger_slash_a.get(), triggerSlashRenderer::new);
    }
}

