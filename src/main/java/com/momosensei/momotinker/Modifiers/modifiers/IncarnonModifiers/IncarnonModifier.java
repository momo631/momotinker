package com.momosensei.momotinker.Modifiers.modifiers.IncarnonModifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;

public class IncarnonModifier extends momomodifier {

    public static final ResourceLocation incarnon_phase = Momotinker.getResource("incarnon_phase");
    public static final ResourceLocation incarnon_a = Momotinker.getResource("incarnon_a");
    public static final ResourceLocation incarnon_b = Momotinker.getResource("incarnon_b");
    public static final ResourceLocation incarnon_c = Momotinker.getResource("incarnon_c");
    public static final ResourceLocation incarnon_d = Momotinker.getResource("incarnon_d");

    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Override
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(incarnon_a);
        iToolStackView.getPersistentData().remove(incarnon_b);
        iToolStackView.getPersistentData().remove(incarnon_c);
        iToolStackView.getPersistentData().remove(incarnon_d);
        return null;
    }
    public static boolean isModifiable(ItemStack stack){
        return stack.getItem() instanceof IModifiable;
    }
}
