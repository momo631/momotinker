package com.momosensei.momotinker.event;

import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;

public class AncientPeachSaplingGenerator extends AbstractTreeGrower {
    public AncientPeachSaplingGenerator(){}
    @Nullable
    @Override
    public Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
//        return BuiltinRegistries.CONFIGURED_FEATURE.getHolderOrThrow(ModFeatures.ANCIENT_PEACH_TREE);
        return ModFeatures.ANCIENT_PEACH_TREE.getHolder().orElseThrow();
    }
}
