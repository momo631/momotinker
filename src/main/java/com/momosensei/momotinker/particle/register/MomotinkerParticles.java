package com.momosensei.momotinker.particle.register;


import com.momosensei.momotinker.test.testb.UltimateSlashStrikeParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.momosensei.momotinker.Momotinker.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MomotinkerParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MOD_ID);
    public static final RegistryObject<SimpleParticleType> ultimate_slash_strike = PARTICLE_TYPES.register("ultimate_slash_strike", () -> new SimpleParticleType(true));

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientSetup(RegisterParticleProvidersEvent event) {
       Minecraft.getInstance().particleEngine.register(MomotinkerParticles.ultimate_slash_strike.get(), UltimateSlashStrikeParticle.Provider::new);
    }
}
