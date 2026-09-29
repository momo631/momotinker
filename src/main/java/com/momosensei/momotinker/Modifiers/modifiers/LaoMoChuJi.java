package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import static com.momosensei.momotinker.util.RandomRecipeManager.getRevelationFruit;

public class LaoMoChuJi extends momomodifier {
    public LaoMoChuJi() {
    }
    public static final String doomsday = Momotinker.getResource("doomsday").toString();

    @Override
    public void onLeftClickEntity(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, Entity entity) {
        player.getInventory().add(getRevelationFruit(doomsday,5));
    }
}