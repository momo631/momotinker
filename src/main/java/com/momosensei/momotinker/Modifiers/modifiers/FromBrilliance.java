package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import javax.annotation.Nullable;

import static com.momosensei.momotinker.tool.divine_punishment_spear.degenerate;
import static com.momosensei.momotinker.tool.divine_punishment_spear.sanctification;

public class FromBrilliance extends momomodifier {
    public FromBrilliance() {
    }

    int sanctification_limit = MomotinkerConfig.sanctification_limit.get();
    int degenerate_limit = MomotinkerConfig.degenerate_limit.get();

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(sanctification);
        iToolStackView.getPersistentData().remove(degenerate);
        return null;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        ModDataNBT a = tool.getPersistentData();
        if (a.getFloat(degenerate)>degenerate_limit){
            a.putFloat(degenerate,degenerate_limit);
        }
        if (a.getFloat(sanctification)>sanctification_limit){
            a.putFloat(sanctification,sanctification_limit);
        }
    }
}