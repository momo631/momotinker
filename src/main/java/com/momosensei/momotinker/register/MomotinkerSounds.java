package com.momosensei.momotinker.register;

import com.momosensei.momotinker.Momotinker;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Momotinker.MOD_ID)
public class MomotinkerSounds {
    private MomotinkerSounds() {
    }
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Momotinker.MOD_ID);
    private static RegistryObject<SoundEvent> create(String name) {
        return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(Momotinker.MOD_ID, name)));
    }
    public static final RegistryObject<SoundEvent> AAAA = create("aaaa");

}
