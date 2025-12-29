package com.momosensei.momotinker.Modifiers.modifiers.IncarnonModifiers;


import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.IncarnonOpenMenuPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.HashMap;
import java.util.Map;

public class SwordIncarnon extends IncarnonModifier {
    public SwordIncarnon() {
        super();
    }
    static {
        Map<String, Component> IncarnonTexts = new HashMap<>();
        IncarnonTexts.put("phase_1_ability_1", Component.translatable("modifier.momotinker.tooltip.sword_incarnon1"));
        IncarnonTexts.put("phase_1_ability_2", Component.translatable("modifier.momotinker.tooltip.sword_incarnon2"));

        IncarnonTexts.put("phase_2_ability_1", Component.translatable("modifier.momotinker.tooltip.sword_incarnon3"));
        IncarnonTexts.put("phase_2_ability_2", Component.translatable("modifier.momotinker.tooltip.sword_incarnon4"));

        IncarnonTexts.put("phase_3_ability_1", Component.translatable("modifier.momotinker.tooltip.sword_incarnon5"));
        IncarnonTexts.put("phase_3_ability_2", Component.translatable("modifier.momotinker.tooltip.sword_incarnon6"));

        IncarnonTexts.put("phase_4_ability_1", Component.translatable("modifier.momotinker.tooltip.sword_incarnon7"));
        IncarnonTexts.put("phase_4_ability_2", Component.translatable("modifier.momotinker.tooltip.sword_incarnon8"));
        IncarnonTexts.put("phase_4_ability_3", Component.translatable("modifier.momotinker.tooltip.sword_incarnon9"));

        registerModifierTexts(SwordIncarnon.class, IncarnonTexts);
    }

    private static final String catalogue = "incarnon_sword";
    private static final Component title = Component.translatable("modifier.momotinker.tooltip.sword_incarnon0");

    @Override
    public boolean overrideOtherStackedOnMe(IToolStackView tool, ModifierEntry modifier, ItemStack held, Slot slot, Player player, SlotAccess access) {
        if (player.level().isClientSide) {
            Channel.sendToServer(new IncarnonOpenMenuPacket(slot.getSlotIndex(),catalogue,getTextsForClass(SwordIncarnon.class),title));
        }
        return true;
    }

}