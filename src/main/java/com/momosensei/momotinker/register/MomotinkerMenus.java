package com.momosensei.momotinker.register;

import com.momosensei.momotinker.menu.IncarnonMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.momosensei.momotinker.Momotinker.MOD_ID;

public class MomotinkerMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES,MOD_ID);

    public static final RegistryObject<MenuType<IncarnonMenu>> Incarnon_menu = MENUS.register("incarnon_menu",()-> IForgeMenuType.create(IncarnonMenu::new));

}
