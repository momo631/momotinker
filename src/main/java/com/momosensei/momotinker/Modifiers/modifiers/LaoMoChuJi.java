package com.momosensei.momotinker.Modifiers.modifiers;


import com.momosensei.momotinker.Modifiers.modifiers.IncarnonModifiers.IncarnonModifier;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.IncarnonOpenMenuPacket;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class LaoMoChuJi extends IncarnonModifier {
    public LaoMoChuJi() {
    }

    @Override
    public boolean overrideOtherStackedOnMe(IToolStackView tool, ModifierEntry modifier, ItemStack held, Slot slot, Player player, SlotAccess access) {
        if (player.level().isClientSide) {
            Channel.sendToServer(new IncarnonOpenMenuPacket(slot.getSlotIndex()));
        }
        return true;
    }

}