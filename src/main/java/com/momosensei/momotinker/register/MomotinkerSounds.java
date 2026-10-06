package com.momosensei.momotinker.register;


import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.momosensei.momotinker.Momotinker.MOD_ID;

public class MomotinkerSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, MOD_ID);

    public static RegistryObject<SoundEvent> register(String name, Supplier<SoundEvent> supplier){
        return SOUNDS.register(name, supplier);
    }
    public static void register(IEventBus modBus){
        SOUNDS.register(modBus);
    }

    public static final Supplier<SoundEvent> NodensReadySounds = register("nodens_ready_sounds",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MOD_ID, "nodens_ready_sounds")));
}

