package com.momosensei.momotinker.event.tree;

import com.momosensei.momotinker.register.MomotinkerModule;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.momosensei.momotinker.Momotinker.MOD_ID;
import static com.momosensei.momotinker.Momotinker.getResource;

@SuppressWarnings("unused")
public class MomotinkerStructures extends MomotinkerModule {
    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, MOD_ID);
    public MomotinkerStructures() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        FEATURES.register(bus);

    }
    protected static <T> ResourceKey<T> key(ResourceKey<? extends Registry<T>> registry, String name) {
        return ResourceKey.create(registry, getResource(name));
    }
    public static final ResourceKey<ConfiguredFeature<?, ?>> jujube_wood_tree = key(Registries.CONFIGURED_FEATURE, "jujube_wood_tree");;

}
