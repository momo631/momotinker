package com.momosensei.momotinker;

import com.momosensei.momotinker.event.LivingEvents;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.register.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;


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
        MinecraftForge.EVENT_BUS.register(new LivingEvents());
        MinecraftForge.EVENT_BUS.register(this);
        MomotinkerItem.ITEMS.register(eventBus);
        MomotinkerModifiers.MODIFIERS.register(eventBus);
        MomotinkerFluid.FLUIDS.register(eventBus);
        MomotinkerBlock.BLOCK.register(eventBus);
        MomotinkerEffects.EFFECT.register(eventBus);
        MomotinkerEntities.ENTITIES.register(eventBus);

    }
    //Resourcelocation
    public static ResourceLocation getResource(String id) {
        return new ResourceLocation("momotinker", id);
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
}
