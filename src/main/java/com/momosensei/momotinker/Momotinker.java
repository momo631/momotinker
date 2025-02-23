package com.momosensei.momotinker;


import com.momosensei.momotinker.event.LivingEvents;
import com.momosensei.momotinker.register.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;

import static slimeknights.tconstruct.TConstruct.makeTranslationKey;

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
        MomotinkerLootModifiers.register(eventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MomotinkerConfig.spec);
        bus.register(new MomotinkerTools());
        MomotinkerTables.initRegisters();

    }
    //Resourcelocation
    public static ResourceLocation getResource(String id) {
        return new ResourceLocation("momotinker", id);
    }
    public static <T> TinkerDataCapability.TinkerDataKey<T> createKey(String name) {
        return TinkerDataCapability.TinkerDataKey.of(getResource(name));
    }
    //生成键名用的
    public static String makeDescriptionId(String type, String name) {
        return type + ".momotinker." + name;
    }

    public static MutableComponent makeTranslation(String base, String name) {
        return Component.translatable(makeTranslationKey(base, name));
    }
}
