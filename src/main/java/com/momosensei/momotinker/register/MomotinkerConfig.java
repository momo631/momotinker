package com.momosensei.momotinker.register;

import net.minecraftforge.common.ForgeConfigSpec;

public class MomotinkerConfig {
    public static final ForgeConfigSpec.Builder builder=new ForgeConfigSpec.Builder().comment("Acquisition Setting")
            .push("Item");

    public static final ForgeConfigSpec.BooleanValue arriving_at_the_other_shore=builder.comment("arriving_at_the_other_shore")
            .define("arriving_at_the_other_shore_is_special_acquisition",true);
    public static final ForgeConfigSpec.BooleanValue jealous_notes=builder.comment("jealous_notes")
            .define("jealous_notes_is_special_acquisition",true);
    public static final ForgeConfigSpec.BooleanValue heartsteel=builder.comment("heartsteel")
            .define("heartsteel_is_special_acquisition",true);
    public static final ForgeConfigSpec.IntValue heartsteel_life_limit=builder.comment("heartsteel_life_limit")
            .defineInRange("heartsteel_life_limit_max",1000000,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.BooleanValue arrogance_proof=builder.comment("arrogance_proof")
            .define("arrogance_proof_is_special_acquisition",true);
    public static final ForgeConfigSpec.BooleanValue interdimensional_crystal=builder.comment("interdimensional_crystal")
            .define("interdimensional_crystal_is_special_acquisition",true);
    public static final ForgeConfigSpec.BooleanValue gluttony_core=builder.comment("gluttony_core")
            .define("gluttony_core_is_special_acquisition",true);
    public static final ForgeConfigSpec.BooleanValue rage_stone_statue=builder.comment("rage_stone_statue")
            .define("rage_stone_statue_is_special_acquisition",true);
    public static final ForgeConfigSpec.BooleanValue lust_mirror=builder.comment("lust_mirror")
            .define("lust_mirror_is_special_acquisition",true);
    public static final ForgeConfigSpec.BooleanValue spirit_visage=builder.comment("spirit_visage")
            .define("spirit_visage_is_special_acquisition",true);
    public static final ForgeConfigSpec.IntValue spirit_visage_limit=builder.comment("spirit_visage_life_limit")
            .defineInRange("spirit_visage_life_limit_max",80,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.BooleanValue lazy_grail=builder.comment("lazy_grail")
            .define("lazy_grail_is_special_acquisition",true);
    public static final ForgeConfigSpec.BooleanValue greedy_contract=builder.comment("greedy_contract")
            .define("greedy_contract_is_special_acquisition",true);
    public static final ForgeConfigSpec.IntValue breakthroughstar_limit=builder.comment("breakthroughstar_limit")
            .defineInRange("breakthroughstar_limit_max",100,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue entropy_burning_riding_spear_limit=builder.comment("entropy_burning_riding_spear_limit")
            .defineInRange("entropy_burning_riding_spear_limit_max",150,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue entropy_burning_cannon_limit=builder.comment("entropy_burning_cannon_limit")
            .defineInRange("entropy_burning_cannon_limit_max",200,0,Integer.MAX_VALUE);



    public static final ForgeConfigSpec spec=builder.pop().build();
}
