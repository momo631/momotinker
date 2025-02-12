package com.momosensei.momotinker.register;

import com.momosensei.momotinker.Momotinker;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import slimeknights.mantle.registration.deferred.SynchronizedDeferredRegister;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;


public abstract class MomotinkerMod {
  protected MomotinkerMod() {
  }

  protected static final ItemDeferredRegisterExtension ITEMS = new ItemDeferredRegisterExtension(Momotinker.MOD_ID);
  protected static final SynchronizedDeferredRegister<CreativeModeTab> CREATIVE_TABS = SynchronizedDeferredRegister.create(Registries.CREATIVE_MODE_TAB, Momotinker.MOD_ID);
  protected static final Item.Properties UNSTACKABLE_PROPS = new Item.Properties().stacksTo(1);

  public static void initRegisters() {
    IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    ITEMS.register(bus);
    CREATIVE_TABS.register(bus);
  }
}
