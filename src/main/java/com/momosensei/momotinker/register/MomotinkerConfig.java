package com.momosensei.momotinker.register;

import net.minecraftforge.common.ForgeConfigSpec;

public class MomotinkerConfig {
    public static final ForgeConfigSpec.Builder builder=new ForgeConfigSpec.Builder().comment("Acquisition Setting")
            .push("Item");

    public static final ForgeConfigSpec.BooleanValue special_acquisition=builder.comment("special_acquisition")
            .define("recipe special acquisition is it enabled",true);
    public static final ForgeConfigSpec.BooleanValue arriving_at_the_other_shore=builder.comment("arriving_at_the_other_shore")
            .define("arriving_at_the_other_shore is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue jealous_notes=builder.comment("jealous_notes")
            .define("jealous_notes is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue heartsteel=builder.comment("heartsteel")
            .define("heartsteel is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue arrogance_proof=builder.comment("arrogance_proof")
            .define("arrogance_proof is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue interdimensional_crystal=builder.comment("interdimensional_crystal")
            .define("interdimensional_crystal is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue gluttony_core=builder.comment("gluttony_core")
            .define("gluttony_core is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue rage_stone_statue=builder.comment("rage_stone_statue")
            .define("rage_stone_statue is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue lust_mirror=builder.comment("lust_mirror")
            .define("lust_mirror is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue spirit_visage=builder.comment("spirit_visage")
            .define("spirit_visage is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue lazy_grail=builder.comment("lazy_grail")
            .define("lazy_grail is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue greedy_contract=builder.comment("greedy_contract")
            .define("greedy_contract is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue dimensional_prism=builder.comment("dimensional_prism")
            .define("dimensional_prism is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue devouring_demon_gold=builder.comment("devouring_demon_gold")
            .define("devouring_demon_gold is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue meteor_nucleus=builder.comment("meteor_nucleus")
            .define("meteor_nucleus is special acquisition",true);



    public static final ForgeConfigSpec.IntValue heartsteel_life_limit=builder.comment("heartsteel_life_limit")
            .defineInRange("heartsteel life limit max",1000000,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue spirit_visage_limit=builder.comment("spirit_visage_life_limit")
            .defineInRange("spirit_visage life limit max",80,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue entropy_burning_riding_spear_limit=builder.comment("entropy_burning_riding_spear_limit")
            .defineInRange("entropy_burning_riding_spear limit max",150,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue entropy_burning_cannon_limit=builder.comment("entropy_burning_cannon_limit")
            .defineInRange("entropy_burning_cannon limit max",200,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue breakthroughstar_limit=builder.comment("breakthroughstar_limit")
            .defineInRange("breakthroughstar limit max",100,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue cleansetheworld_limit=builder.comment("cleansetheworld_limit")
            .defineInRange("cleansetheworld cooldown time limit",600,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue crystallized_hurt_limit=builder.comment("crystallized_hurt_limit")
            .defineInRange("crystallized hurt limit",60,0,Integer.MAX_VALUE);

    public static final ForgeConfigSpec.IntValue sanctification_limit=builder.comment("divine_punishment_spear_limit")
            .defineInRange("divine_punishment_spear sanctification limit",500,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue degenerate_limit=builder.comment("divine_punishment_spear_limit")
            .defineInRange("divine_punishment_spear degenerate limit",100,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue hadal_limit=builder.comment("entropy_burning_riding_spear_limit")
            .defineInRange("entropy_burning_riding_spear hadal limit",400,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue stellarcore_limit=builder.comment("entropy_burning_sword_limit")
            .defineInRange("entropy_burning_sword stellarcore limit",400,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue crystallized_limit=builder.comment("entropy_burning_cannon_limit")
            .defineInRange("entropy_burning_cannon crystallized limit",20,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue liverization_limit=builder.comment("entropy_burning_cube_limit")
            .defineInRange("entropy_burning_cube liverization limit",100,0,Integer.MAX_VALUE);

    public static final ForgeConfigSpec.BooleanValue explosion_destroys_limit=builder.comment("explosion_destroys_limit")
            .define("explosion destroys limit",true);


    public static final ForgeConfigSpec spec=builder.pop().build();
}
