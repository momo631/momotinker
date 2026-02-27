package com.momosensei.momotinker.register;

import com.momosensei.momotinker.Modifiers.modifiers.*;
import com.momosensei.momotinker.Modifiers.modifiers.IncarnonModifiers.SwordIncarnon;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

import static com.momosensei.momotinker.Momotinker.MOD_ID;
public class MomotinkerModifiers {

    public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(MOD_ID);

    public static final StaticModifier<LaoMoChuJi> laomochuji=MODIFIERS.register("laomochuji", LaoMoChuJi::new);   //测试

    public static final StaticModifier<Lethe> lethe=MODIFIERS.register("lethe", Lethe::new);
    public static final StaticModifier<Huangquan> huangquan=MODIFIERS.register("huangquan", Huangquan::new);
    public static final StaticModifier<Red> red=MODIFIERS.register("red", Red::new);
    public static final StaticModifier<Firmheart> firmheart=MODIFIERS.register("firmheart", Firmheart::new);
    public static final StaticModifier<WildHearts> wildhearts=MODIFIERS.register("wildhearts", WildHearts::new);
    public static final StaticModifier<InfiniteVitality> infinitevitality=MODIFIERS.register("infinitevitality", InfiniteVitality::new);

    public static final StaticModifier<Feast> feast=MODIFIERS.register("feast", Feast::new);
    public static final StaticModifier<OverEatingSin> overeatingsin=MODIFIERS.register("overeatingsin", OverEatingSin::new);
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
    public static final StaticModifier<SacrificeToSpirit> sacrificetospirit=MODIFIERS.register("sacrificetospirit", SacrificeToSpirit::new);
    public static final StaticModifier<OverSadSin> oversadsin=MODIFIERS.register("oversadsin", OverSadSin::new);

    public static final StaticModifier<FallingStars> fallingstars=MODIFIERS.register("fallingstars", FallingStars::new);
    public static final StaticModifier<UnknownReturnee> unknownreturnee=MODIFIERS.register("unknownreturnee", UnknownReturnee::new);
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
    public static final StaticModifier<ShortTermInvestments> shortterminvestments=MODIFIERS.register("shortterminvestments", ShortTermInvestments::new);
    public static final StaticModifier<LongTermInvestments> longterminvestments=MODIFIERS.register("longterminvestments", LongTermInvestments::new);
    public static final StaticModifier<SuperancientMetalsC> superancientmetalsc=MODIFIERS.register("superancientmetalsc", SuperancientMetalsC::new);
    public static final StaticModifier<SuperancientMetalsRealC> superancientmetalsrealc=MODIFIERS.register("superancientmetalsrealc", SuperancientMetalsRealC::new);
    public static final StaticModifier<PolarizedSpacetime> polarizedspacetime=MODIFIERS.register("polarizedspacetime", PolarizedSpacetime::new);
    public static final StaticModifier<TimeEchoes> timeechoes=MODIFIERS.register("timeechoes", TimeEchoes::new);
    public static final StaticModifier<Resonance> resonance=MODIFIERS.register("resonance", Resonance::new);
    public static final StaticModifier<SlimeResonance> slimeresonance=MODIFIERS.register("slimeresonance", SlimeResonance::new);
    public static final StaticModifier<SlimeRhythm> slimerhythm=MODIFIERS.register("slimerhythm", SlimeRhythm::new);
    public static final StaticModifier<SlimeRhythmA> slimerhythma=MODIFIERS.register("slimerhythma", SlimeRhythmA::new);
    public static final StaticModifier<RareCrystals> rare_crystals=MODIFIERS.register("rare_crystals", RareCrystals::new);
    public static final StaticModifier<DragonSource> dragon_source=MODIFIERS.register("dragon_source", DragonSource::new);
    public static final StaticModifier<AncientBloodline> ancient_bloodline=MODIFIERS.register("ancient_bloodline", AncientBloodline::new);
    public static final StaticModifier<MaterialReforging> material_reforging=MODIFIERS.register("material_reforging", MaterialReforging::new);

    public static final StaticModifier<EternalAnger> eternalanger=MODIFIERS.register("eternalanger", EternalAnger::new);
    public static final StaticModifier<SlackAtmosphere> slackatmosphere=MODIFIERS.register("slackatmosphere", SlackAtmosphere::new);
    public static final StaticModifier<ThePinnacleOfArrogance> thepinnacleofarrogance=MODIFIERS.register("thepinnacleofarrogance", ThePinnacleOfArrogance::new);
    public static final StaticModifier<FilledWithHunger> filledwithhunger=MODIFIERS.register("filledwithhunger", FilledWithHunger::new);
    public static final StaticModifier<ResentmentKnives> resentmentknives=MODIFIERS.register("resentmentknives", ResentmentKnives::new);
    public static final StaticModifier<ForbiddenFruit> forbiddenfruit=MODIFIERS.register("forbiddenfruit", ForbiddenFruit::new);
    public static final StaticModifier<GainsAlone> gainsalone=MODIFIERS.register("gainsalone", GainsAlone::new);
    public static final StaticModifier<CompassionateEverything> compassionateeverything=MODIFIERS.register("compassionateeverything", CompassionateEverything::new);

    public static final StaticModifier<KeyToRuin> key_to_ruin=MODIFIERS.register("key_to_ruin", KeyToRuin::new);

    public static final StaticModifier<SuppressingEvil> suppressing_evil=MODIFIERS.register("suppressing_evil", SuppressingEvil::new);
    public static final StaticModifier<SuppressingEvilA> suppressing_evil_a=MODIFIERS.register("suppressing_evil_a", SuppressingEvilA::new);

    public static final StaticModifier<WardOffEvil> ward_off_evil=MODIFIERS.register("ward_off_evil", WardOffEvil::new);
    public static final StaticModifier<ThunderDecree> thunder_decree=MODIFIERS.register("thunder_decree", ThunderDecree::new);
    public static final StaticModifier<WardOffEvilA> ward_off_evil_a=MODIFIERS.register("ward_off_evil_a", WardOffEvilA::new);
    public static final StaticModifier<ThunderDecreeA> thunder_decree_a=MODIFIERS.register("thunder_decree_a", ThunderDecreeA::new);

    public static final StaticModifier<YearsInWeiqi> years_in_weiqi=MODIFIERS.register("years_in_weiqi", YearsInWeiqi::new);

    public static final StaticModifier<MemoriesOfMountains> memories_of_mountains=MODIFIERS.register("memories_of_mountains", MemoriesOfMountains::new);

    public static final StaticModifier<SwordIncarnon> sword_incarnon=MODIFIERS.register("sword_incarnon", SwordIncarnon::new);


}
