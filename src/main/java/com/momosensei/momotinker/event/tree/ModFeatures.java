package com.momosensei.momotinker.event.tree;

import com.momosensei.momotinker.register.MomotinkerBlock;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;

import static com.momosensei.momotinker.event.tree.MomotinkerStructures.jujube_wood_tree;


public final class ModFeatures {
    public ModFeatures(){}

    public static void register(RegistrySetBuilder builder) {
        builder.add(Registries.CONFIGURED_FEATURE, ModFeatures::registerConfiguredFeatures);

    }
    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?,?>> context, ResourceKey<ConfiguredFeature<?,?>> key, F feature, FC config) {
        context.register(key, new ConfiguredFeature<>(feature, config));
    }
    private static void registerConfiguredFeatures(BootstapContext<ConfiguredFeature<?,?>> context) {
        register(context, jujube_wood_tree, Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(MomotinkerBlock.jujube_wood_log.get()),
                        new ForkingTrunkPlacer(5, 2, 2),
                        BlockStateProvider.simple(MomotinkerBlock.jujube_wood_leaves.get()),
                        new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
                        new TwoLayersFeatureSize(1, 0, 2))
                        .ignoreVines().build());
    }
}
