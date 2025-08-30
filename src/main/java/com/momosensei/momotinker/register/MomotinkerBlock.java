package com.momosensei.momotinker.register;

import com.momosensei.momotinker.event.AncientPeachSaplingGenerator;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.momosensei.momotinker.Momotinker.MOD_ID;

public class MomotinkerBlock {
    public static final DeferredRegister<Block> BLOCK = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final RegistryObject<Block> Laomo_block = BLOCK.register("laomo_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLUE_WOOL)));
    public static final RegistryObject<Block> dimensional_prism = BLOCK.register("dimensional_prism",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLUE_WOOL)));
    public static final RegistryObject<Block> meteor_nucleus_block = BLOCK.register("meteor_nucleus_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLUE_WOOL)));

    public static final RegistryObject<Block> ancient_peach_log = BLOCK.register("ancient_peach_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> ancient_peach_leaves = BLOCK.register("ancient_peach_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> ancient_peach_sapling = BLOCK.register("ancient_peach_sapling",
            () -> new SaplingBlock(new AncientPeachSaplingGenerator(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));


}
