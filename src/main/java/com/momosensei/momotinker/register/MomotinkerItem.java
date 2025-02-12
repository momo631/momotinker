package com.momosensei.momotinker.register;

import com.momosensei.momotinker.Items.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.momosensei.momotinker.Momotinker.MOD_ID;


public class MomotinkerItem {
    public MomotinkerItem() {
    }
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    //物品锭什么的注册照着复制就行，如果有单独类改new Item的Item字段为你的类名，tab为创造模式显示栏，在哪个栏位显示
    public static final RegistryObject<Item> laomo = ITEMS.register("laomo", () -> new Item(MomotinkerTables.material()));
    public static final RegistryObject<Item> interdimensional_crystal = ITEMS.register("interdimensional_crystal", () -> new interdimensional_crystal(MomotinkerTables.material()));
    public static final RegistryObject<Item> arriving_at_the_other_shore = ITEMS.register("arriving_at_the_other_shore", () -> new arriving_at_the_other_shore(MomotinkerTables.material()));
    public static final RegistryObject<Item> spirit_visage = ITEMS.register("spirit_visage", () -> new spirit_visage(MomotinkerTables.material()));
    public static final RegistryObject<Item> heartsteel = ITEMS.register("heartsteel", () -> new heartsteel(MomotinkerTables.material()));
    public static final RegistryObject<Item> gluttony_core = ITEMS.register("gluttony_core", () -> new gluttony_core(MomotinkerTables.material()));
    public static final RegistryObject<Item> greedy_contract = ITEMS.register("greedy_contract", () -> new greedy_contract(MomotinkerTables.material()));
    public static final RegistryObject<Item> lust_mirror = ITEMS.register("lust_mirror", () -> new lust_mirror(MomotinkerTables.material()));
    public static final RegistryObject<Item> arrogance_proof = ITEMS.register("arrogance_proof", () -> new arrogance_proof(MomotinkerTables.material()));
    public static final RegistryObject<Item> rage_stone_statue = ITEMS.register("rage_stone_statue", () -> new rage_stone_statue(MomotinkerTables.material()));
    public static final RegistryObject<Item> lazy_grail = ITEMS.register("lazy_grail", () -> new lazy_grail(MomotinkerTables.material()));
    public static final RegistryObject<Item> jealous_notes = ITEMS.register("jealous_notes", () -> new jealous_notes(MomotinkerTables.material()));
    public static final RegistryObject<Item> trigger_slash_a = ITEMS.register("trigger_slash_a", () -> new Item(new Item.Properties()));

    //方块类要这样,略有变动

    public static final RegistryObject<BlockItem> Laomo_block = ITEMS.register("laomo_block", () -> new BlockItem(MomotinkerBlock.Laomo_block.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> dimensional_prism = ITEMS.register("dimensional_prism", () -> new dimensional_prism(MomotinkerBlock.dimensional_prism.get(), new Item.Properties()));

}