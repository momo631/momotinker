package com.momosensei.momotinker;


import com.momosensei.momotinker.event.LivingEvents;
import com.momosensei.momotinker.key.key;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.register.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.client.model.TinkerItemProperties;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;

import java.util.Objects;

import static com.momosensei.momotinker.register.MomotinkerTools.*;

@Mod(Momotinker.MOD_ID)
@Mod.EventBusSubscriber(
        bus = Mod.EventBusSubscriber.Bus.MOD
)

public class Momotinker {
    public static final String MOD_ID = "momotinker"; //是你的模组名，需要英文
    public Momotinker() {
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();
        IEventBus eventBus = context.getModEventBus();
        eventBus.addListener(this::commonSetup);
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
        MinecraftForge.EVENT_BUS.register(new LivingEvents());
        //MinecraftForge.EVENT_BUS.register(new ItemSightEventHandler());

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MomotinkerConfig.Itemspec, "MomotinkerItem.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MomotinkerConfig.Modifierspec, "MomotinkerModifier.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MomotinkerConfig.Toolspec, "MomotinkerTool.toml");

        //GeckoLib.initialize();
    }
    //Resourcelocation
    public static ResourceLocation getResource(String id) {
        return new ResourceLocation("momotinker", id);
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

    private void commonSetup(FMLCommonSetupEvent event) {
        Channel.init();
    }

    //生成键名用的
    public static String makeDescriptionId(String type, String name) {
        return type + ".momotinker." + name;
    }

    public static ResourceLocation id(@NotNull String path) {
        return new ResourceLocation(Momotinker.MOD_ID, path);
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD,  value = {Dist.CLIENT})
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                TinkerItemProperties.registerBrokenProperty(trigger_blade.get());
                TinkerItemProperties.registerBrokenProperty(divine_punishment_spear.get());
                TinkerItemProperties.registerBrokenProperty(entropy_burning_cube.get());
                TinkerItemProperties.registerBrokenProperty(entropy_burning_cannon.get());
                TinkerItemProperties.registerBrokenProperty(entropy_burning_riding_spear.get());
                TinkerItemProperties.registerBrokenProperty(entropy_burning_sword.get());
                TinkerItemProperties.registerBrokenProperty(eclipse_container.get());
                TinkerItemProperties.registerBrokenProperty(moon_lock.get());
                TinkerItemProperties.registerBrokenProperty(coronal_key.get());
                TinkerItemProperties.registerBrokenProperty(pocket_watch.get());
                TinkerItemProperties.registerBrokenProperty(chain_sword.get());
                TinkerItemProperties.registerBrokenProperty(pneumatic_sword.get());

                TinkerItemProperties.registerToolProperties(trigger_blade.get());
                TinkerItemProperties.registerToolProperties(divine_punishment_spear.get());
                TinkerItemProperties.registerToolProperties(entropy_burning_cube.get());
                TinkerItemProperties.registerToolProperties(entropy_burning_riding_spear.get());
                TinkerItemProperties.registerToolProperties(entropy_burning_cannon.get());
                TinkerItemProperties.registerToolProperties(moon_lock.get());
                TinkerItemProperties.registerToolProperties(coronal_key.get());
                TinkerItemProperties.registerToolProperties(eclipse_container.get());
                TinkerItemProperties.registerToolProperties(pocket_watch.get());
                TinkerItemProperties.registerToolProperties(chain_sword.get());
                TinkerItemProperties.registerToolProperties(pneumatic_sword.get());
             //   TinkerItemProperties.registerToolProperties(aa.get());
            });
        }
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(key.KeyBinding.KEY);
            event.register(key.KeyBinding.KEYA);
        }
    }
}
