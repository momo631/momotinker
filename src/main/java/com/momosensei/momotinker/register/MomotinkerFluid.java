package com.momosensei.momotinker.register;

import com.momosensei.momotinker.Momotinker;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import slimeknights.mantle.registration.deferred.FluidDeferredRegister;
import slimeknights.mantle.registration.object.FlowingFluidObject;
import slimeknights.mantle.registration.object.FluidObject;

import static com.momosensei.momotinker.Momotinker.MOD_ID;

public class MomotinkerFluid{
    public static final FluidDeferredRegister FLUIDS = new FluidDeferredRegister(MOD_ID);
    private static FluidType.Properties hot(String name) {
        return FluidType.Properties.create().density(2000).viscosity(10000).temperature(1000).descriptionId(Momotinker.makeDescriptionId("fluid", name)).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_FILL);
    }

    private static FluidType.Properties cool(String name) {
        return cool().descriptionId(Momotinker.makeDescriptionId("fluid", name)).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_FILL);
    }

    private static FluidType.Properties cool() {
        return FluidType.Properties.create().sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_FILL);
    }

    private static FlowingFluidObject<ForgeFlowingFluid> register(String name, int temp) {
        return FLUIDS.register(name).type(hot(name).temperature(temp).lightLevel(12)).block(MapColor.NONE,15).bucket().flowing();
    }

    //复制这行，只改<ForgeFlowingFluid>后的命名空间，和register里面的"xxxx"即可,temp一般用不上，瞎填就行
    //材质和model什么的在数据包(resourcepacks）部分
    public static final FluidObject<ForgeFlowingFluid> molten_laomo = register("molten_laomo", 5867);
    public static final FluidObject<ForgeFlowingFluid> molten_arriving_at_the_other_shore = register("molten_arriving_at_the_other_shore", 5866);
    public static final FluidObject<ForgeFlowingFluid> molten_spirit_visage = register("molten_spirit_visage", 5865);
    public static final FluidObject<ForgeFlowingFluid> molten_heartsteel = register("molten_heartsteel", 5864);
    public static final FluidObject<ForgeFlowingFluid> molten_gluttony_core = register("molten_gluttony_core", 5863);
    public static final FluidObject<ForgeFlowingFluid> molten_greedy_contract = register("molten_greedy_contract", 5862);
    public static final FluidObject<ForgeFlowingFluid> molten_lust_mirror = register("molten_lust_mirror", 5861);
    public static final FluidObject<ForgeFlowingFluid> molten_arrogance_proof = register("molten_arrogance_proof", 5860);
    public static final FluidObject<ForgeFlowingFluid> molten_rage_stone_statue = register("molten_rage_stone_statue", 5859);
    public static final FluidObject<ForgeFlowingFluid> molten_lazy_grail = register("molten_lazy_grail", 5858);
    public static final FluidObject<ForgeFlowingFluid> molten_jealous_notes = register("molten_jealous_notes", 5857);
    public static final FluidObject<ForgeFlowingFluid> molten_interdimensional_crystal = register("molten_interdimensional_crystal", 5856);
    public static final FluidObject<ForgeFlowingFluid> molten_dimensional_prism = register("molten_dimensional_prism", 5855);

}
