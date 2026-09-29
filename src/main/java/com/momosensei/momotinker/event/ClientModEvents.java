package com.momosensei.momotinker.event;

import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.gui.screen.IncarnonScreen;
import com.momosensei.momotinker.key.key;
import com.momosensei.momotinker.register.MomotinkerMenus;
import com.momosensei.momotinker.test.testa.MyModels;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import slimeknights.tconstruct.library.client.model.TinkerItemProperties;

import static com.momosensei.momotinker.register.MomotinkerTools.*;

@Mod.EventBusSubscriber(modid = Momotinker.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD,  value = {Dist.CLIENT})
public class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(MomotinkerMenus.Incarnon_menu.get(), IncarnonScreen::new);
//            MenuScreens.register(MomotinkerMenus.special_tinker_station.get(), STinkerStationScreen::new);

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
    @SubscribeEvent
    static void setupClient(final FMLClientSetupEvent event) {
        MyModels.LoadOtherModel();
    }

//    @SubscribeEvent
//    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
//        BlockEntityRendererProvider<TableBlockEntity> tableRenderer = InventoryBlockEntityRenderer::new;
//        event.registerBlockEntityRenderer(MomotinkerBlock.special_tinker_station, tableRenderer);
//    }
}
