package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;
import java.util.List;


public class ThePinnacleOfArrogance extends momomodifier {
    public ThePinnacleOfArrogance() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Nullable
    @Override
    public Component requirementsError(ModifierEntry entry) {
        return Component.translatable("recipe.momotinker.modifier.thepinnacleofarrogance");
    }
    @Override
    public @NotNull List<ModifierEntry> displayModifiers(ModifierEntry entry) {
        return List.of(new ModifierEntry(MomotinkerModifiers.overdisdainsin.getId(),1));
    }
    @Override
    public Component validate(IToolStackView tool, ModifierEntry modifier) {
        if (tool.getModifierLevel(MomotinkerModifiers.overdisdainsin.getId())>0
                &&tool.getModifierLevel(MomotinkerModifiers.eternalanger.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.slackatmosphere.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.filledwithhunger.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.resentmentknives.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.forbiddenfruit.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.gainsalone.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.compassionateeverything.getId())==0){
            return null;
        }
        return requirementsError(modifier);
    }

    private void livinghurtevent(LivingHurtEvent event) {
        Entity a = event.getEntity();
        if (a instanceof Player player&&getAllModifierlevel(player, MomotinkerModifiers.thepinnacleofarrogance.getId())>0){
            if (event.getAmount()<player.getMaxHealth()*0.05f){
                event.setAmount(player.getMaxHealth()*0.05f);
            }
            if (event.getAmount()>player.getMaxHealth()*0.15f&&event.getAmount()<player.getMaxHealth()*25f){
                event.setAmount(player.getMaxHealth()*0.15f);
            }
        }
    }
}