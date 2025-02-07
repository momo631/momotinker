package com.momosensei.momotinker.register;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import slimeknights.tconstruct.library.client.model.TinkerItemProperties;


@Mod.EventBusSubscriber(
        modid = "momotinker",
        value = {Dist.CLIENT},
        bus = Mod.EventBusSubscriber.Bus.MOD
)

public class MomotinkerToolUtil {
    public MomotinkerToolUtil() {
    }

    public static void clientSetupEvent(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            TinkerItemProperties.registerToolProperties(MomotinkerItem.trigger_blade.get().asItem());
        });
    }
}
