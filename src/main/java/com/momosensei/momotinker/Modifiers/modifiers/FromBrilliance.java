package com.momosensei.momotinker.Modifiers.modifiers;

import net.minecraft.network.chat.Component;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;

import static com.momosensei.momotinker.tool.divine_punishment_spear.degenerate;
import static com.momosensei.momotinker.tool.divine_punishment_spear.sanctification;

public class FromBrilliance extends momomodifier {
    public FromBrilliance() {
    }

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
}