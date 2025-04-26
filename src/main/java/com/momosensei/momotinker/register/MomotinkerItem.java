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
    public static final RegistryObject<Item> dim_dark_gold = ITEMS.register("dim_dark_gold", () -> new dim_dark_gold(MomotinkerTables.material()));
    public static final RegistryObject<Item> stained_blood_gold = ITEMS.register("stained_blood_gold", () -> new stained_blood_gold(MomotinkerTables.material()));
    public static final RegistryObject<Item> starry_mysterious_gold = ITEMS.register("starry_mysterious_gold", () -> new starry_mysterious_gold(MomotinkerTables.material()));
    public static final RegistryObject<Item> devouring_demon_gold = ITEMS.register("devouring_demon_gold", () -> new devouring_demon_gold(MomotinkerTables.material()));
    public static final RegistryObject<Item> nihilism = ITEMS.register("nihilism", () -> new nihilism(MomotinkerTables.material()));
    public static final RegistryObject<Item> meteor_nucleus = ITEMS.register("meteor_nucleus", () -> new meteor_nucleus(MomotinkerTables.material()));
    public static final RegistryObject<Item> ashen_platinum = ITEMS.register("ashen_platinum", () -> new ashen_platinum(MomotinkerTables.material()));
    public static final RegistryObject<Item> hadal_platinum = ITEMS.register("hadal_platinum", () -> new hadal_platinum(MomotinkerTables.material()));
    public static final RegistryObject<Item> stellar_core_platinum = ITEMS.register("stellar_core_platinum", () -> new stellar_core_platinum(MomotinkerTables.material()));
    public static final RegistryObject<Item> crystallized_platinum = ITEMS.register("crystallized_platinum", () -> new crystallized_platinum(MomotinkerTables.material()));
    public static final RegistryObject<Item> living_platinum = ITEMS.register("living_platinum", () -> new living_platinum(MomotinkerTables.material()));

    public static final RegistryObject<Item> trigger_slash_a = ITEMS.register("trigger_slash_a", ()->new Item(new Item.Properties()));
    public static final RegistryObject<Item> spear_entity = ITEMS.register("spear_entity", ()->new Item(new Item.Properties()));
    public static final RegistryObject<Item> cleanse_item = ITEMS.register("cleanse_item",()-> new Item(new Item.Properties()));

    //方块类要这样,略有变动

    public static final RegistryObject<BlockItem> Laomo_block = ITEMS.register("laomo_block", () -> new BlockItem(MomotinkerBlock.Laomo_block.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> dimensional_prism = ITEMS.register("dimensional_prism", () -> new dimensional_prism(MomotinkerBlock.dimensional_prism.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> meteor_nucleus_block = ITEMS.register("meteor_nucleus_block", () -> new meteor_nucleus_block(MomotinkerBlock.meteor_nucleus_block.get(), new Item.Properties()));

}