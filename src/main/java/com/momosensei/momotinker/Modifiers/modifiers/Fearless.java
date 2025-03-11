package com.momosensei.momotinker.Modifiers.modifiers;


import com.momosensei.momotinker.Modifiers.momomodifier;
import net.minecraft.network.chat.Component;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ValidateModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.RequirementsModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;

public class Fearless extends momomodifier implements RequirementsModifierHook , ValidateModifierHook {
    public Fearless() {
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.REQUIREMENTS,ModifierHooks.VALIDATE);
    }
    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Nullable
    @Override
    public Component requirementsError(ModifierEntry entry) {
        return Component.translatable("recipe.momotinker.modifier.fearless");
    }

    @Override
    public Component validate(IToolStackView tool, ModifierEntry modifier) {
        if (tool.getUpgrades().getModifiers().isEmpty()){
            return null;
        }
        return requirementsError(modifier);
    }
}