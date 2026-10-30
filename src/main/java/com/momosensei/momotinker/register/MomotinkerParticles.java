package com.momosensei.momotinker.register;

import com.mojang.serialization.Codec;
import com.momosensei.momotinker.particle.SlashStrikeOptions;
import com.momosensei.momotinker.particle.SlashTrailOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import static com.momosensei.momotinker.Momotinker.MOD_ID;

public class MomotinkerParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MOD_ID);

    public static final RegistryObject<ParticleType<SlashStrikeOptions>> SLASH_STRIKE_TYPE =
            PARTICLE_TYPES.register("slash_strike",
                    () -> new ParticleType<>(true, SlashStrikeOptions.DESERIALIZER) {
                        @Override
                        public @NotNull Codec<SlashStrikeOptions> codec() {
                            return SlashStrikeOptions.CODEC;
                        }
                    });

    public static final RegistryObject<ParticleType<SlashTrailOptions>> SLASH_TRAIL_TYPE =
            PARTICLE_TYPES.register("slash_trail",
                    () -> new ParticleType<>(true, SlashTrailOptions.DESERIALIZER) {
                        @Override
                        public @NotNull Codec<SlashTrailOptions> codec() {
                            return SlashTrailOptions.CODEC;
                        }
                    });

//    public static final RegistryObject<SimpleParticleType> SLASH_STRIKE_TYPE =
//            PARTICLE_TYPES.register("slash_strike", () -> new SimpleParticleType(true));
//
//    public static final RegistryObject<SimpleParticleType> SLASH_TRAIL_TYPE =
//            PARTICLE_TYPES.register("slash_trail", () -> new SimpleParticleType(true));
}
