package com.momosensei.momotinker.register;

import net.minecraftforge.common.ForgeConfigSpec;

public class MomotinkerConfig {
    public static final ForgeConfigSpec.Builder builder=new ForgeConfigSpec.Builder()
            .comment("Modifier Setting")
            .push("Modifier");

    public static final ForgeConfigSpec.BooleanValue arriving_at_the_other_shore=builder.comment("")
            .define("is_special_drop_or_acquisition",true);
    public static final ForgeConfigSpec.BooleanValue jealous_notes=builder.comment("")
            .define("is_special_drop_or_acquisition",true);
    public static final ForgeConfigSpec.BooleanValue heartsteel=builder.comment("")
            .define("is_special_drop_or_acquisition",true);
    public static final ForgeConfigSpec.BooleanValue arrogance_proof=builder.comment("")
            .define("is_special_drop_or_acquisition",true);
    public static final ForgeConfigSpec.BooleanValue interdimensional_crystal=builder.comment("")
            .define("is_special_drop_or_acquisition",true);
    public static final ForgeConfigSpec.BooleanValue gluttony_core=builder.comment("")
            .define("is_special_drop_or_acquisition",true);
    public static final ForgeConfigSpec.BooleanValue rage_stone_statue=builder.comment("")
            .define("is_special_drop_or_acquisition",true);
    public static final ForgeConfigSpec.BooleanValue lust_mirror=builder.comment("")
            .define("is_special_drop_or_acquisition",true);
    public static final ForgeConfigSpec.BooleanValue spirit_visage=builder.comment("")
            .define("is_special_drop_or_acquisition",true);
    public static final ForgeConfigSpec.BooleanValue lazy_grail=builder.comment("")
            .define("is_special_drop_or_acquisition",true);
    public static final ForgeConfigSpec.BooleanValue greedy_contract=builder.comment("")
            .define("is_special_drop_or_acquisition",true);


    public static final ForgeConfigSpec spec=builder.pop().build();
}
