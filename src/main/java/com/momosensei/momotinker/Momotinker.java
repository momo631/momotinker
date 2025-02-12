package com.momosensei.momotinker;


import com.momosensei.momotinker.entity.MomoDamageTypeProvider;
import com.momosensei.momotinker.entity.MomoDamageTypeTagProvider;
import com.momosensei.momotinker.event.LivingEvents;
import com.momosensei.momotinker.register.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.utils.Util;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod(Momotinker.MOD_ID)
@Mod.EventBusSubscriber(
        bus = Mod.EventBusSubscriber.Bus.MOD
)

public class Momotinker {
    public static final String MOD_ID = "momotinker"; //是你的模组名，需要英文
    public Momotinker(FMLJavaModLoadingContext context) {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus eventBus = context.getModEventBus();
        MinecraftForge.EVENT_BUS.register(new LivingEvents());
        MinecraftForge.EVENT_BUS.register(this);
        MomotinkerItem.ITEMS.register(eventBus);
        MomotinkerModifiers.MODIFIERS.register(eventBus);
        MomotinkerFluid.FLUIDS.register(eventBus);
        MomotinkerBlock.BLOCK.register(eventBus);
        MomotinkerEffects.EFFECT.register(eventBus);
        MomotinkerEntities.ENTITIES.register(eventBus);
        bus.register(new MomotinkerTools());
        MomotinkerTables.initRegisters();

    }
    @SubscribeEvent
    static void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        RegistrySetBuilder registrySetBuilder = new RegistrySetBuilder();
        MomoDamageTypeProvider.register(registrySetBuilder);
        boolean server = event.includeServer();
        DatapackBuiltinEntriesProvider datapackRegistryProvider = new DatapackBuiltinEntriesProvider(packOutput, lookupProvider, registrySetBuilder, Set.of(MOD_ID));
        generator.addProvider(server, new MomoDamageTypeTagProvider(packOutput, datapackRegistryProvider.getRegistryProvider(), existingFileHelper));
    }
    //Resourcelocation
    public static ResourceLocation getResource(String id) {
        return new ResourceLocation("momotinker", id);
    }
    public static <T> TinkerDataCapability.TinkerDataKey<T> createKey(String name) {
        return TinkerDataCapability.TinkerDataKey.of(getResource(name));
    }
    public static MutableComponent makeTranslation(String base, String name) {
        return Component.translatable(makeTranslationKey(base, name));
    }
    public static String makeTranslationKey(String base, String name) {
        return Util.makeTranslationKey(base, getResource(name));
    }
    //生成键名用的
    public static String makeDescriptionId(String type, String name) {
        return type + ".momotinker." + name;
    }
}
