package com.momosensei.momotinker.register;

import net.minecraftforge.common.ForgeConfigSpec;

public class MomotinkerConfig {
    public static final ForgeConfigSpec.Builder item=new ForgeConfigSpec.Builder().comment("Acquisition Setting")
            .push("Item");

    public static final ForgeConfigSpec.BooleanValue special_acquisition=item.comment("special_acquisition")
            .define("recipe special acquisition is it enabled",true);
    public static final ForgeConfigSpec.BooleanValue stage_meteor=item.comment("stage_meteor")
            .define("Whether to join the stage:Meteor(This invalid for Compassion Mask)",false);
    public static final ForgeConfigSpec.BooleanValue arriving_at_the_other_shore=item.comment("arriving_at_the_other_shore")
            .define("arriving_at_the_other_shore is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue jealous_notes=item.comment("jealous_notes")
            .define("jealous_notes is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue heartsteel=item.comment("heartsteel")
            .define("heartsteel is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue arrogance_proof=item.comment("arrogance_proof")
            .define("arrogance_proof is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue interdimensional_crystal=item.comment("interdimensional_crystal")
            .define("interdimensional_crystal is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue gluttony_core=item.comment("gluttony_core")
            .define("gluttony_core is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue rage_stone_statue=item.comment("rage_stone_statue")
            .define("rage_stone_statue is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue lust_mirror=item.comment("lust_mirror")
            .define("lust_mirror is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue spirit_visage=item.comment("spirit_visage")
            .define("spirit_visage is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue lazy_grail=item.comment("lazy_grail")
            .define("lazy_grail is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue greedy_contract=item.comment("greedy_contract")
            .define("greedy_contract is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue compassion_mask=item.comment("compassion_mask")
            .define("compassion_mask is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue dimensional_prism=item.comment("dimensional_prism")
            .define("dimensional_prism is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue devouring_demon_gold=item.comment("devouring_demon_gold")
            .define("devouring_demon_gold is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue meteor_nucleus=item.comment("meteor_nucleus")
            .define("meteor_nucleus is special acquisition",true);
    public static final ForgeConfigSpec.BooleanValue dragon_jade=item.comment("dragon_jade")
            .define("dragon_jade is special acquisition",true);

    public static final ForgeConfigSpec Itemspec=item.pop().build();


    public static final ForgeConfigSpec.Builder modifier=new ForgeConfigSpec.Builder().comment("Acquisition Setting")
            .push("Modifier");

    public static final ForgeConfigSpec.IntValue heartsteel_life_limit=modifier.comment("heartsteel_life_limit")
            .defineInRange("heartsteel life limit max",1000000,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue spirit_visage_limit=modifier.comment("spirit_visage_life_limit")
            .defineInRange("spirit_visage life limit max",80,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue entropy_burning_riding_spear_limit=modifier.comment("entropy_burning_riding_spear_limit")
            .defineInRange("entropy_burning_riding_spear limit max",150,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue entropy_burning_cannon_limit=modifier.comment("entropy_burning_cannon_limit")
            .defineInRange("entropy_burning_cannon limit max",200,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue breakthroughstar_limit=modifier.comment("breakthroughstar_limit")
            .defineInRange("breakthroughstar limit max",100,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue cleansetheworld_limit=modifier.comment("cleansetheworld_limit")
            .defineInRange("cleansetheworld cooldown time limit",600,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue crystallized_hurt_limit=modifier.comment("crystallized_hurt_limit")
            .defineInRange("crystallized hurt limit",60,0,Integer.MAX_VALUE);
    //public static final ForgeConfigSpec.IntValue shortterminvestments_increase_ratio=modifier.comment("shortterminvestments_increase_ratio")
    //        .defineInRange("shortterminvestments increase ratio",25,0,Integer.MAX_VALUE);
    //public static final ForgeConfigSpec.IntValue shortterminvestments_reduce_ratio=modifier.comment("shortterminvestments_reduce_ratio")
    //        .defineInRange("shortterminvestments reduce_ratio",50,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.BooleanValue shortterminvestments_only_minecraft=modifier.comment("shortterminvestments_only_minecraft")
            .define("shortterminvestments only valid for the minecraft' item",true);
    public static final ForgeConfigSpec.BooleanValue explosion_destroys_limit=modifier.comment("explosion_destroys_limit")
            .define("explosion destroys limit",true);

    public static final ForgeConfigSpec Modifierspec=modifier.pop().build();


    public static final ForgeConfigSpec.Builder tool=new ForgeConfigSpec.Builder().comment("Acquisition Setting")
            .push("Tool");

    public static final ForgeConfigSpec.IntValue sanctification_limit=tool.comment("divine_punishment_spear_limit")
            .defineInRange("divine_punishment_spear sanctification limit",500,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue degenerate_limit=tool.comment("divine_punishment_spear_limit")
            .defineInRange("divine_punishment_spear degenerate limit",100,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue hadal_limit=tool.comment("entropy_burning_riding_spear_limit")
            .defineInRange("entropy_burning_riding_spear hadal limit",400,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue stellarcore_limit=tool.comment("entropy_burning_sword_limit")
            .defineInRange("entropy_burning_sword stellarcore limit",400,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue crystallized_limit=tool.comment("entropy_burning_cannon_limit")
            .defineInRange("entropy_burning_cannon crystallized limit",20,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue liverization_limit=tool.comment("entropy_burning_cube_limit")
            .defineInRange("entropy_burning_cube liverization limit",100,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue transmit_limit=tool.comment("pocket_watch_limit")
            .defineInRange("pocket_watch transmit limit",12,0,Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue backtracking_limit=tool.comment("pocket_watch_limit")
            .defineInRange("pocket_watch backtracking limit",12,0,Integer.MAX_VALUE);

    public static final ForgeConfigSpec Toolspec=tool.pop().build();

}
