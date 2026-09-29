package com.momosensei.momotinker.Modifiers.modifiers.UniverseModifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.util.Cutter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;


public class Doomsday extends momomodifier {
    public Doomsday() {
    }
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void onLeftClickEntity(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, Entity entity) {
        Cutter.AttackEntity(level, entity,level.damageSources().playerAttack(player));
    }
}