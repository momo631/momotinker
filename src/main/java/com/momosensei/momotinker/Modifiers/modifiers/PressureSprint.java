package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;


public class PressureSprint extends momomodifier {
    public PressureSprint() {
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof ServerPlayer player) {

        }
    }
}