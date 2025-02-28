package com.momosensei.momotinker.Modifiers.modifiers;


import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.RequirementsModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;

import javax.annotation.Nullable;
import java.util.List;

public class CleanseTheWorld extends momomodifier implements RequirementsModifierHook {
    public CleanseTheWorld() {
    }
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        builder.addHook(this, ModifierHooks.REQUIREMENTS);
    }
    @Nullable
    @Override
    public Component requirementsError(ModifierEntry entry) {
        return Component.translatable("recipe.momotinker.modifier.cleansetheworld");
    }
    @Override
    public @NotNull List<ModifierEntry> displayModifiers(ModifierEntry entry) {
        return List.of(new ModifierEntry(MomotinkerModifiers.breakthroughstars.getId(),1));
    }
    @Override
    public boolean isNoLevels() {
        return true;
    }
}