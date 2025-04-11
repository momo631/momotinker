package com.momosensei.momotinker.register;

import com.momosensei.momotinker.Effects.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.momosensei.momotinker.Momotinker.MOD_ID;
public class MomotinkerEffects  {
    public static final DeferredRegister<MobEffect> EFFECT = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MOD_ID);

    //注册固定这个格式就行
    public static final RegistryObject<MobEffect> HongWen = EFFECT.register("hongwen", HongWen::new);
    public static final RegistryObject<MobEffect> None = EFFECT.register("none", None::new);
    public static final RegistryObject<MobEffect> End = EFFECT.register("end", End::new);
    public static final RegistryObject<MobEffect> BeSwallow = EFFECT.register("beswallow",  BeSwallow::new);
    public static final RegistryObject<MobEffect> BeSwallowed = EFFECT.register("beswallowed",  BeSwallowed::new);
    public static final RegistryObject<MobEffect> WildHeart = EFFECT.register("wildheart",  WildHeart::new);
    public static final RegistryObject<MobEffect> LostSoul = EFFECT.register("lostsoul",  LostSoul::new);
    public static final RegistryObject<MobEffect> Arrogant = EFFECT.register("arrogant",  Arrogant::new);
    public static final RegistryObject<MobEffect> FallingPreparation = EFFECT.register("fallingpreparation",  FallingPreparation::new);
    public static final RegistryObject<MobEffect> FallingStar = EFFECT.register("fallingstar",  FallingStar::new);
    public static final RegistryObject<MobEffect> FlameBathArmor = EFFECT.register("flamebatharmor",  FlameBathArmor::new);
    public static final RegistryObject<MobEffect> Censored = EFFECT.register("censored",  Censored::new);
    public static final RegistryObject<MobEffect> C = EFFECT.register("c",  C::new);

}
