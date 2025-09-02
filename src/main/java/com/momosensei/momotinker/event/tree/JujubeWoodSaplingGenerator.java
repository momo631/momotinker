package com.momosensei.momotinker.event.tree;

import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;

public class JujubeWoodSaplingGenerator extends AbstractTreeGrower {
    public JujubeWoodSaplingGenerator(){}
    @Nullable
    @Override
    public Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
//        return BuiltinRegistries.CONFIGURED_FEATURE.getHolderOrThrow(ModFeatures.jujube_wood_TREE);
        return ModFeatures.jujube_wood_TREE.getHolder().orElseThrow();
    }
}
