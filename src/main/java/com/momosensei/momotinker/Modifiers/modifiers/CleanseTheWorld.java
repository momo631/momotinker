package com.momosensei.momotinker.Modifiers.modifiers;


import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ValidateModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.RequirementsModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;
import java.util.List;

import static com.momosensei.momotinker.tool.divine_punishment_spear.cleansespawncooldown;

public class CleanseTheWorld extends momomodifier implements RequirementsModifierHook , ValidateModifierHook {
    public CleanseTheWorld() {
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
        return Component.translatable("recipe.momotinker.modifier.cleansetheworld");
    }

    @Override
    public @NotNull List<ModifierEntry> displayModifiers(ModifierEntry entry) {
        return List.of(new ModifierEntry(MomotinkerModifiers.breakthroughstars.getId(),1));
    }

    @Override
    public Component validate(IToolStackView tool, ModifierEntry modifier) {
        if (tool.getModifierLevel(MomotinkerModifiers.breakthroughstars.getId())>0){
            return null;
        }
        return requirementsError(modifier);
    }
    @Override
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(cleansespawncooldown);
        return null;
    }
    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof ServerPlayer &&entity.tickCount%20==0&&iToolStackView.getPersistentData().getInt(cleansespawncooldown)!=0) {
            iToolStackView.getPersistentData().putInt(cleansespawncooldown,iToolStackView.getPersistentData().getInt(cleansespawncooldown)-1);
        }
    }
}