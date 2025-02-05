package com.momosensei.momotinker.entity;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.damagesource.DamageType;

import static com.momosensei.momotinker.entity.MomoDamageTypes.OVER;
import static slimeknights.tconstruct.TConstruct.prefix;

public class MomoDamageTypeProvider implements RegistrySetBuilder.RegistryBootstrap<DamageType> {
    private MomoDamageTypeProvider() {}
    public static void register(RegistrySetBuilder builder) {
        builder.add(Registries.DAMAGE_TYPE, new MomoDamageTypeProvider());
    }
    @Override
    public void run(BootstapContext<DamageType> context) {
        context.register(OVER, new DamageType(prefix("over"), 1f));
    }

}
