package com.momosensei.momotinker.register;

import com.momosensei.momotinker.event.tree.JujubeWoodSaplingGenerator;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.deferred.BlockEntityTypeDeferredRegister;
import slimeknights.tconstruct.common.registration.BlockDeferredRegisterExtension;

import java.util.function.Function;

import static com.momosensei.momotinker.Momotinker.MOD_ID;
public class MomotinkerBlock {
    public static final DeferredRegister<Block> BLOCK = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final RegistryObject<Block> Laomo_block = BLOCK.register("laomo_block",
            () -> new Block(BlockBehaviour.Properties.of()));
    public static final RegistryObject<Block> dimensional_prism = BLOCK.register("dimensional_prism",
            () -> new Block(BlockBehaviour.Properties.of()));
    public static final RegistryObject<Block> meteor_nucleus_block = BLOCK.register("meteor_nucleus_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(9.0F, 10.0F).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> jujube_wood_log = BLOCK.register("jujube_wood_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG).strength(2.0F, 3.0F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> jujube_wood_leaves = BLOCK.register("jujube_wood_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> jujube_wood_sapling = BLOCK.register("jujube_wood_sapling",
            () -> new SaplingBlock(new JujubeWoodSaplingGenerator(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> jujube_wood_planks = BLOCK.register("jujube_wood_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> lightning_strike_wood = BLOCK.register("lightning_strike_wood",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG).strength(5.0F, 6.0F).requiresCorrectToolForDrops()));

    public static final BlockEntityTypeDeferredRegister BLOCK_ENTITIES = new BlockEntityTypeDeferredRegister(MOD_ID);
    public static final BlockDeferredRegisterExtension BLOCKS = new BlockDeferredRegisterExtension(MOD_ID);
    protected static final Item.Properties ITEM_PROPS = new Item.Properties();
    protected static final Function<Block, ? extends BlockItem> BLOCK_ITEM = (b) -> new BlockItem(b, ITEM_PROPS);

    protected static BlockBehaviour.Properties builder(SoundType soundType) {
        return Block.Properties.of().sound(soundType);
    }

    protected static BlockBehaviour.Properties builder(MapColor color, SoundType soundType) {
        return builder(soundType).mapColor(color);
    }

//    public static final ItemObject<TableBlock> tinkerStation;

//    static {
//        Block.Properties METAL_TABLE = builder(MapColor.COLOR_GRAY, SoundType.ANVIL).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().strength(5.0F, 1200.0F).noOcclusion();
//        Function<Block, BlockItem> blockItem = block -> new AnvilBlockItem(block, ITEM_PROPS, TinkerToolParts.fakeStorageBlockItem);
//        tinkerStation = BLOCKS.register("s_tinker_station", () -> new STinkerStationBlock(METAL_TABLE, 6), blockItem);
//    }

//    public static final ItemObject<TableBlock> tinkersAnvil, scorchedAnvil;
//
//    static {
//        Block.Properties METAL_TABLE = builder(MapColor.COLOR_GRAY, SoundType.ANVIL).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().strength(5.0F, 1200.0F).noOcclusion();
//        Function<Block, BlockItem> blockItem = block -> new AnvilBlockItem(block, ITEM_PROPS, TinkerToolParts.fakeStorageBlockItem);
//        tinkersAnvil = BLOCKS.register("s_tinkers_anvil", () -> new TinkersAnvilBlock(METAL_TABLE, 6), blockItem);
//        scorchedAnvil = BLOCKS.register("s_scorched_anvil", () -> new ScorchedAnvilBlock(METAL_TABLE, 6), blockItem);
//    }

//    public static final RegistryObject<BlockEntityType<STinkerStationBlockEntity>> StinkerStationTile = BLOCK_ENTITIES.register("s_tinker_station", STinkerStationBlockEntity::new, builder ->
//            builder.add(tinkerStation.get()));


//
//    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
//
//    public static final RegistryObject<Block> special_tinker = registerBlock("special_tinker_station",
//            () -> new SpecialTinkerStationBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE),6));
//
//    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> blockSupplier) {
//        RegistryObject<T> block = BLOCKS.register(name, blockSupplier);
//        registerBlockItem(name, block);
//        return block;
//    }
//
//    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
//        return MomotinkerItem.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
//    }
//    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
//            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MOD_ID);
//
//    public static final RegistryObject<BlockEntityType<SpecialTinkerStationBlockEntity>> special_tinker_station =
//            BLOCK_ENTITIES.register("special_tinker_station",
//                    () -> BlockEntityType.Builder.of(SpecialTinkerStationBlockEntity::new, special_tinker.get()).build(null));


}
