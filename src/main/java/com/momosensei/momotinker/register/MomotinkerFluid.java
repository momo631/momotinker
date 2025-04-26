package com.momosensei.momotinker.register;

import com.momosensei.momotinker.Momotinker;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import slimeknights.mantle.registration.deferred.FluidDeferredRegister;
import slimeknights.mantle.registration.object.FlowingFluidObject;
import slimeknights.mantle.registration.object.FluidObject;

import static com.momosensei.momotinker.Momotinker.MOD_ID;
import static slimeknights.tconstruct.fluids.block.BurningLiquidBlock.createBurning;

public class MomotinkerFluid{
    public static final FluidDeferredRegister FLUIDS = new FluidDeferredRegister(MOD_ID);
    private static FluidType.Properties hot(String name,int temp,boolean gas) {
        return FluidType.Properties.create().density(gas?-2000:2000).viscosity(10000).temperature(temp)
                .descriptionId(Momotinker.makeDescriptionId("fluid", name))
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_FILL)
                .motionScale(0.0023333333333333335D)
                .canSwim(false).canDrown(false)
                .pathType(BlockPathTypes.LAVA).adjacentPathType(null);
    }

    private static FluidType.Properties cool(String name) {
        return cool().descriptionId(Momotinker.makeDescriptionId("fluid", name)).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_FILL);
    }

    private static FluidType.Properties cool() {
        return FluidType.Properties.create().sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_FILL);
    }

    private static FlowingFluidObject<ForgeFlowingFluid> register(String name, int temp,int lightLevel,boolean gas) {
        return FLUIDS.register(name).type(hot(name,temp,gas)).block(createBurning(MapColor.COLOR_GRAY, lightLevel,5,0)).bucket().flowing();
    }

    public static final FluidObject<ForgeFlowingFluid> molten_laomo = register("molten_laomo", 5867,10,false);
    public static final FluidObject<ForgeFlowingFluid> molten_arriving_at_the_other_shore = register("molten_arriving_at_the_other_shore", 5866,10,false);
    public static final FluidObject<ForgeFlowingFluid> molten_spirit_visage = register("molten_spirit_visage", 5865,10,false);
    public static final FluidObject<ForgeFlowingFluid> molten_heartsteel = register("molten_heartsteel", 5864,10,false);
    public static final FluidObject<ForgeFlowingFluid> molten_gluttony_core = register("molten_gluttony_core", 5863,10,false);
    public static final FluidObject<ForgeFlowingFluid> molten_greedy_contract = register("molten_greedy_contract", 5862,10,false);
    public static final FluidObject<ForgeFlowingFluid> molten_lust_mirror = register("molten_lust_mirror", 5861,10,false);
    public static final FluidObject<ForgeFlowingFluid> molten_arrogance_proof = register("molten_arrogance_proof", 5860,10,false);
    public static final FluidObject<ForgeFlowingFluid> molten_rage_stone_statue = register("molten_rage_stone_statue", 5859,10,false);
    public static final FluidObject<ForgeFlowingFluid> molten_lazy_grail = register("molten_lazy_grail", 5858,10,false);
    public static final FluidObject<ForgeFlowingFluid> molten_jealous_notes = register("molten_jealous_notes", 5857,10,false);
    public static final FluidObject<ForgeFlowingFluid> molten_interdimensional_crystal = register("molten_interdimensional_crystal", 5856,10,false);
    public static final FluidObject<ForgeFlowingFluid> molten_dimensional_prism = register("molten_dimensional_prism", 5855,10,false);
    public static final FluidObject<ForgeFlowingFluid> molten_dim_dark_gold = register("molten_dim_dark_gold", 5855,10,false);
    public static final FluidObject<ForgeFlowingFluid> molten_stained_blood_gold = register("molten_stained_blood_gold", 5855,10,false);
    public static final FluidObject<ForgeFlowingFluid> molten_starry_mysterious_gold = register("molten_starry_mysterious_gold", 5855,10,false);
    public static final FluidObject<ForgeFlowingFluid> molten_devouring_demon_gold = register("molten_devouring_demon_gold", 5855,10,false);
    public static final FluidObject<ForgeFlowingFluid> molten_nihilism = register("molten_nihilism", 5855,10,false);
    public static final FluidObject<ForgeFlowingFluid> molten_meteor_nucleus = register("molten_meteor_nucleus", 12000,10,false);
    public static final FluidObject<ForgeFlowingFluid> molten_ashen_platinum = register("molten_ashen_platinum", 1500,10,false);
    public static final FluidObject<ForgeFlowingFluid> molten_hadal_platinum = register("molten_hadal_platinum", 1500,10,false);
    public static final FluidObject<ForgeFlowingFluid> molten_stellar_core_platinum = register("molten_stellar_core_platinum", 1500,10,false);
    public static final FluidObject<ForgeFlowingFluid> molten_crystallized_platinum = register("molten_crystallized_platinum", 1500,10,false);
    public static final FluidObject<ForgeFlowingFluid> molten_living_platinum = register("molten_living_platinum", 1500,10,false);

}
