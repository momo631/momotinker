package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierTraitHook;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;

import static slimeknights.tconstruct.common.TinkerTags.Modifiers.OVERSLIME_FRIEND;


public class SlimeResonance extends momomodifier{
    public SlimeResonance() {
    }

    @Override
    public void addTraits(IToolContext context, ModifierEntry modifier, ModifierTraitHook.TraitBuilder builder, boolean firstEncounter) {
        for (ModifierEntry entry : context.getModifierList()) {
            if (entry.getModifier() != modifier.getModifier()&&entry.matches(OVERSLIME_FRIEND)&&firstEncounter) {
                builder.add(entry.getId(),modifier.getLevel()*2);
            }
        }
    }
}