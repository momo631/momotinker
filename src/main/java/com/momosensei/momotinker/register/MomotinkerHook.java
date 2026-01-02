package com.momosensei.momotinker.register;

import com.momosensei.momotinker.Modifiers.hook.SweepAttackModifierHook;
import com.momosensei.momotinker.Momotinker;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.module.ModuleHook;

public class MomotinkerHook {
    public static final ModuleHook<SweepAttackModifierHook> SWEEP_ATTACK = ModifierHooks.register(Momotinker.getResource("sweep_attack"), SweepAttackModifierHook.class, SweepAttackModifierHook.AllMerger::new, new SweepAttackModifierHook(){});

}
