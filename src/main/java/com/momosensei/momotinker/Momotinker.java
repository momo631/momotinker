package com.momosensei.momotinker;


import com.momosensei.momotinker.event.LivingEvents;
import com.momosensei.momotinker.event.tree.ModFeatures;
import com.momosensei.momotinker.event.tree.MomotinkerStructures;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.particle.register.MomotinkerParticles;
import com.momosensei.momotinker.register.*;
import com.momosensei.momotinker.test.testa.PostPasses;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.layout.StationSlotLayoutLoader;

import java.util.Objects;

//import static com.momosensei.momotinker.register.MomotinkerBlock.tinkerStation;

@Mod(Momotinker.MOD_ID)
@Mod.EventBusSubscriber(
        bus = Mod.EventBusSubscriber.Bus.MOD
)

public class Momotinker {
    public static final String MOD_ID = "momotinker";
    public static final Logger LOG = LogManager.getLogger(MOD_ID);
    public Momotinker() {
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();
        IEventBus eventBus = context.getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        MomotinkerItem.ITEMS.register(eventBus);
        MomotinkerModifiers.MODIFIERS.register(eventBus);
        MomotinkerFluid.FLUIDS.register(eventBus);
        MomotinkerBlock.BLOCK.register(eventBus);
        MomotinkerEffects.EFFECT.register(eventBus);
        MomotinkerEntities.ENTITIES.register(eventBus);
        MomotinkerLootModifiers.register(eventBus);
        eventBus.register(new MomotinkerTools());
        MomotinkerTables.initRegisters();
        MomotinkerTags.init();
        MinecraftForge.EVENT_BUS.register(new LivingEvents());

        MinecraftForge.EVENT_BUS.register(new MomotinkerStructures());
        MomotinkerModule.initRegisters();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MomotinkerConfig.Itemspec, "MomotinkerItem.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MomotinkerConfig.Modifierspec, "MomotinkerModifier.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MomotinkerConfig.Toolspec, "MomotinkerTool.toml");

        MomotinkerMenus.MENUS.register(eventBus);
        MomotinkerMenus.MENU.register(eventBus);

        MomotinkerParticles.PARTICLE_TYPES.register(eventBus);
        //GeckoLib.initialize();
        if(FMLEnvironment.dist == Dist.CLIENT){
            eventBus.addListener(PostPasses::register);
        }

        MomotinkerBlock.BLOCK_ENTITIES.register(eventBus);
        MomotinkerBlock.BLOCKS.register(eventBus);
    }
    //Resourcelocation
    public static ResourceLocation getResource(String id) {
        return new ResourceLocation(MOD_ID, id);
    }

    public static ResourceLocation getResourceLocation(String id) {
        return new ResourceLocation(id);
    }

    public static String ItemString(Item item) {
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).getNamespace() + ":" + Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).getPath();
    }

    public static <T> TinkerDataCapability.TinkerDataKey<T> createKey(String name) {
        return TinkerDataCapability.TinkerDataKey.of(getResource(name));
    }
    @SubscribeEvent
    void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            StationSlotLayoutLoader loader = StationSlotLayoutLoader.getInstance();
//            loader.registerRequiredLayout(tinkerStation.getId());
//            loader.registerRequiredLayout(tinkersAnvil.getId());
//            loader.registerRequiredLayout(scorchedAnvil.getId());
        });
    }
    @SubscribeEvent
    public static void onFMLCommonSetup(FMLCommonSetupEvent event) {
        Channel.init();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> MomotinkerSlots::init);
    }
    public static String makeDescriptionId(String type, String name) {
        return type + ".momotinker." + name;
    }

    public static ResourceLocation id(@NotNull String path) {
        return new ResourceLocation(Momotinker.MOD_ID, path);
    }
    @SubscribeEvent
    static void gatherData(final GatherDataEvent event) {
        RegistrySetBuilder registrySetBuilder = new RegistrySetBuilder();
        ModFeatures.register(registrySetBuilder);

    }

}
