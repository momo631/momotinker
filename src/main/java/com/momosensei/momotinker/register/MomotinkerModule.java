package com.momosensei.momotinker.register;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class MomotinkerModule {

    public static void initRegisters() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

    }
}
