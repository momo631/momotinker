package com.momosensei.momotinker.event;

import com.momosensei.momotinker.register.MomotinkerBlock;
import com.momosensei.momotinker.register.MomotinkerModule;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public final class ModFeatures extends MomotinkerModule {
    public ModFeatures(){
    }

    public static final RegistryObject<ConfiguredFeature<TreeConfiguration, Feature<TreeConfiguration>>> ANCIENT_PEACH_TREE = CONFIGURED_FEATURES.registerSupplier(
        "ancient_peach_tree", () -> Feature.TREE,
        () -> new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(MomotinkerBlock.ancient_peach_log.get()),
//                new CherryTrunkPlacer(7, 1, 0,
//                        new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder().add(ConstantInt.of(1), 1).add(ConstantInt.of(2), 1).add(ConstantInt.of(3), 1).build()), UniformInt.of(2, 4), UniformInt.of(-4, -3), UniformInt.of(-1, 0)),
                new ForkingTrunkPlacer(5, 2, 2),
                BlockStateProvider.simple(MomotinkerBlock.ancient_peach_leaves.get()),
//                new CherryFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), ConstantInt.of(5), 0.25F, 0.5F, 0.16666667F, 0.33333334F),
                new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
                new TwoLayersFeatureSize(1, 0, 2))
                .ignoreVines().build());

}
