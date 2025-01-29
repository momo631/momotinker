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
    public static final StaticModifier<LaoMoChuJi> laomochuji=MODIFIERS.register("laomochuji", LaoMoChuJi::new);   //测试
    public static final StaticModifier<Lethe> lethe=MODIFIERS.register("lethe", Lethe::new);
    public static final StaticModifier<Huangquan> huangquan=MODIFIERS.register("huangquan", Huangquan::new);
    public static final StaticModifier<Red> red=MODIFIERS.register("red", Red::new);
    public static final StaticModifier<Firmheart> firmheart=MODIFIERS.register("firmheart", Firmheart::new);
    public static final StaticModifier<Feast> feast=MODIFIERS.register("feast", Feast::new);
    public static final StaticModifier<OvereatingSin> overeatingsin=MODIFIERS.register("overeatingsin", OvereatingSin::new);
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

}
