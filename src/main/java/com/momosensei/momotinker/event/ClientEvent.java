package com.momosensei.momotinker.event;

import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.capability.ender.EnderProvider;
import com.momosensei.momotinker.key.key;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.KeyAInputPKT;
import com.momosensei.momotinker.network.packet.KeyInputPKT;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Momotinker.MOD_ID, value = {Dist.CLIENT})
public class ClientEvent {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (key.KeyBinding.KEY.consumeClick()) {
            Channel.INSTANCE.sendToServer(new KeyInputPKT());
        }
        if (key.KeyBinding.KEYA.consumeClick()) {
            Channel.INSTANCE.sendToServer(new KeyAInputPKT());
        }
    }
    public static final ModelLayerLocation HELM_LAYER = register("helm", "main");
    private static ModelLayerLocation register(String model, String layer) {
        return new ModelLayerLocation(new ResourceLocation("momotinker", model), layer);
    }
    @SubscribeEvent
    public static void registerCapability(RegisterCapabilitiesEvent event) {
        event.register(EnderProvider.class);
    }
/*
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(HELM_LAYER, HelmModel::createArmorLayer);
    }*/
}
