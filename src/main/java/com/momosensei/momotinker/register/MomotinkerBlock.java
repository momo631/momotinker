package com.momosensei.momotinker.register;

import com.momosensei.momotinker.event.tree.JujubeWoodSaplingGenerator;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

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

}
