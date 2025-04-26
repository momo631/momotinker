package com.momosensei.momotinker.register;

import com.momosensei.momotinker.Modifiers.modifiers.*;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

import static com.momosensei.momotinker.Momotinker.MOD_ID;
public class MomotinkerModifiers {

    public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(MOD_ID);
    //词条的注册在这里
    //这里用1234来对应我举例这个的例子的四个名位置（laomochuji）
    //1，4填类名
    //2不要和其他模组的重复，可大写可小写,用于引用
    //3只能小写，并且影响你zh_cn的键名
    //一般建议2,3写一样的
//    public static final StaticModifier<LaoMoChuJi> laomochuji=MODIFIERS.register("laomochuji", LaoMoChuJi::new);   //测试
    public static final StaticModifier<Lethe> lethe=MODIFIERS.register("lethe", Lethe::new);
    public static final StaticModifier<Huangquan> huangquan=MODIFIERS.register("huangquan", Huangquan::new);
    public static final StaticModifier<Red> red=MODIFIERS.register("red", Red::new);
    public static final StaticModifier<Firmheart> firmheart=MODIFIERS.register("firmheart", Firmheart::new);
    public static final StaticModifier<Feast> feast=MODIFIERS.register("feast", Feast::new);
    public static final StaticModifier<OverEatingSin> overeatingsin=MODIFIERS.register("overeatingsin", OverEatingSin::new);
    public static final StaticModifier<WildHearts> wildhearts=MODIFIERS.register("wildhearts", WildHearts::new);
    public static final StaticModifier<InfiniteVitality> infinitevitality=MODIFIERS.register("infinitevitality", InfiniteVitality::new);
    public static final StaticModifier<IntendingPlunder> intendingplunder=MODIFIERS.register("intendingplunder", IntendingPlunder::new);
    public static final StaticModifier<OverweightingWealthSin> overweightingwealthsin=MODIFIERS.register("overweightingwealthsin", OverweightingWealthSin::new);
    public static final StaticModifier<VehementDesire> vehementdesire=MODIFIERS.register("vehementdesire", VehementDesire::new);
    public static final StaticModifier<OverIndulgenceSin> overindulgencesin=MODIFIERS.register("overindulgencesin", OverIndulgenceSin::new);
    public static final StaticModifier<Dominate> dominate=MODIFIERS.register("dominate", Dominate::new);
    public static final StaticModifier<OverDisdainSin> overdisdainsin=MODIFIERS.register("overdisdainsin", OverDisdainSin::new);
    public static final StaticModifier<DrinkBlood> drinkblood=MODIFIERS.register("drinkblood", DrinkBlood::new);
    public static final StaticModifier<OverAngerSin> overangersin=MODIFIERS.register("overangersin", OverAngerSin::new);
    public static final StaticModifier<Unstained> unstained=MODIFIERS.register("unstained", Unstained::new);
    public static final StaticModifier<OverCowardiceSin> overcowardicesin=MODIFIERS.register("overcowardicesin", OverCowardiceSin::new);
    public static final StaticModifier<GrudgeOthers> grudgeothers=MODIFIERS.register("grudgeothers", GrudgeOthers::new);
    public static final StaticModifier<OverEnvySin> overenvysin=MODIFIERS.register("overenvysin", OverEnvySin::new);
    public static final StaticModifier<FallingStars> fallingstars=MODIFIERS.register("fallingstars", FallingStars::new);
    public static final StaticModifier<UnknownReturnee> unknownreturnee=MODIFIERS.register("unknownreturnee", UnknownReturnee::new);
    public static final StaticModifier<Test> test=MODIFIERS.register("test", Test::new);
    public static final StaticModifier<OverCrystalline> overcrystalline=MODIFIERS.register("overcrystalline", OverCrystalline::new);
    public static final StaticModifier<Crystallization> crystallization=MODIFIERS.register("crystallization", Crystallization::new);
    public static final StaticModifier<FromBrilliance> frombrilliance=MODIFIERS.register("frombrilliance", FromBrilliance::new);
    public static final StaticModifier<SuperancientMetalsA> superancientmetalsa=MODIFIERS.register("superancientmetalsa", SuperancientMetalsA::new);
    public static final StaticModifier<SuperancientMetalsRealA> superancientmetalsreala=MODIFIERS.register("superancientmetalsreala", SuperancientMetalsRealA::new);
    public static final StaticModifier<CrimsonQueen> crimsonqueen=MODIFIERS.register("crimsonqueen", CrimsonQueen::new);
    public static final StaticModifier<Berserk> berserk=MODIFIERS.register("berserk", Berserk::new);
    public static final StaticModifier<Yamato> yamato=MODIFIERS.register("yamato", Yamato::new);
    public static final StaticModifier<StarCloudChain> starcloudchain=MODIFIERS.register("starcloudchain", StarCloudChain::new);
    public static final StaticModifier<BreakthroughStars> breakthroughstars=MODIFIERS.register("breakthroughstars", BreakthroughStars::new);
    public static final StaticModifier<CleanseTheWorld> cleansetheworld=MODIFIERS.register("cleansetheworld", CleanseTheWorld::new);
    public static final StaticModifier<ShadowOfDamnation> shadowofdamnation=MODIFIERS.register("shadowofdamnation", ShadowOfDamnation::new);
    public static final StaticModifier<DrinkingDemon> drinkingdemon=MODIFIERS.register("drinkingdemon", DrinkingDemon::new);
    public static final StaticModifier<SweetAfterTaste> sweetaftertaste=MODIFIERS.register("sweetaftertaste", SweetAfterTaste::new);
    public static final StaticModifier<ExpandedEquipment> expandedequipment=MODIFIERS.register("expandedequipment", ExpandedEquipment::new);
    public static final StaticModifier<ProjectionOfSuffering> projectionofsuffering=MODIFIERS.register("projectionofsuffering", ProjectionOfSuffering::new);
    public static final StaticModifier<UltimateDarkness> ultimatedarkness=MODIFIERS.register("ultimatedarkness", UltimateDarkness::new);
    public static final StaticModifier<Origin> origin=MODIFIERS.register("origin", Origin::new);
    public static final StaticModifier<Significance> significance=MODIFIERS.register("significance", Significance::new);
    public static final StaticModifier<Blank> blank=MODIFIERS.register("blank", Blank::new);
    public static final StaticModifier<FlameBath> flamebath=MODIFIERS.register("flamebath", FlameBath::new);
    public static final StaticModifier<StarfallArrow> starfallarrow=MODIFIERS.register("starfallarrow", StarfallArrow::new);
    public static final StaticModifier<ExplosiveSword> explosivesword=MODIFIERS.register("explosivesword", ExplosiveSword::new);
    public static final StaticModifier<BlessingOfMeteor> blessingofmeteor=MODIFIERS.register("blessingofmeteor", BlessingOfMeteor::new);
    public static final StaticModifier<SuperancientMetalsB> superancientmetalsb=MODIFIERS.register("superancientmetalsb", SuperancientMetalsB::new);
    public static final StaticModifier<SuperancientMetalsRealB> superancientmetalsrealb=MODIFIERS.register("superancientmetalsrealb", SuperancientMetalsRealB::new);
    public static final StaticModifier<AbyssalResonance> abyssalresonance=MODIFIERS.register("abyssalresonance", AbyssalResonance::new);
    public static final StaticModifier<ThermonuclearZone> thermonuclearzone=MODIFIERS.register("thermonuclearzone", ThermonuclearZone::new);
    public static final StaticModifier<SelfCrystallization> selfcrystallization=MODIFIERS.register("selfcrystallization", SelfCrystallization::new);
    public static final StaticModifier<LifeProgram> lifeprogram=MODIFIERS.register("lifeprogram", LifeProgram::new);

}
