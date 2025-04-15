package com.momosensei.momotinker;

import com.momosensei.momotinker.event.LivingEvents;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.register.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.client.model.TinkerItemProperties;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;

import static com.momosensei.momotinker.register.MomotinkerItem.*;


@Mod(Momotinker.MOD_ID)
@Mod.EventBusSubscriber(
        bus = Mod.EventBusSubscriber.Bus.MOD
)

public class Momotinker {
    public static final String MOD_ID = "momotinker"; //是你的模组名，需要英文
    public Momotinker() {
        //注册表之类的东西
        //如果你新稿了别的注册表记得这边填一下
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        MomotinkerItem.ITEMS.register(eventBus);
        MomotinkerItem.ARMOR.register(eventBus);
        MomotinkerModifiers.MODIFIERS.register(eventBus);
        MomotinkerFluid.FLUIDS.register(eventBus);
        MomotinkerBlock.BLOCK.register(eventBus);
        MomotinkerEffects.EFFECT.register(eventBus);
        MomotinkerEntities.ENTITIES.register(eventBus);
        MomotinkerSounds.SOUNDS.register(eventBus);
        MomotinkerLootModifiers.register(eventBus);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MomotinkerConfig.spec);
        MinecraftForge.EVENT_BUS.register(new LivingEvents());
    }

    //Resourcelocation
    public static ResourceLocation getResource(String id) {
        return new ResourceLocation("momotinker", id);
    }

    public static ResourceLocation getResourceLocation(String id) {
        return new ResourceLocation(id);
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

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
             //   TinkerItemProperties.registerToolProperties(aa.get());
            });
        }
    }
}
