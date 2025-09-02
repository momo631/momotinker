package com.momosensei.momotinker.event.tree;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;

public class JujubeWoodSaplingGenerator extends AbstractTreeGrower {
    public JujubeWoodSaplingGenerator(){}
    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean largeHive) {
        return MomotinkerStructures.jujube_wood_tree;
    }
}
