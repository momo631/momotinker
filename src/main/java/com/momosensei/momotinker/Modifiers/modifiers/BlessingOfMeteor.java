package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;


public class BlessingOfMeteor extends momomodifier {
    public BlessingOfMeteor() {
    }
    @Override
    public void addVolatileData(IToolContext context, ModifierEntry modifier, ModDataNBT modDataNBT) {
        modDataNBT.addSlots(SlotType.ABILITY, modifier.getLevel());
        modDataNBT.addSlots(SlotType.DEFENSE, modifier.getLevel());
        modDataNBT.addSlots(SlotType.UPGRADE, modifier.getLevel());
    }
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        ToolStats.DURABILITY.multiply(builder,Math.pow(1.4,modifier.getLevel()));
        ToolStats.ATTACK_SPEED.multiply(builder,Math.pow(1.4,modifier.getLevel()));
        ToolStats.ATTACK_DAMAGE.multiply(builder,Math.pow(1.4,modifier.getLevel()));
        ToolStats.ACCURACY.multiply(builder,Math.pow(1.4,modifier.getLevel()));
        ToolStats.DRAW_SPEED.multiply(builder,Math.pow(1.4,modifier.getLevel()));
        ToolStats.VELOCITY.multiply(builder,Math.pow(1.4,modifier.getLevel()));
        ToolStats.MINING_SPEED.multiply(builder,Math.pow(1.4,modifier.getLevel()));
        ToolStats.ARMOR.multiply(builder,Math.pow(1.4,modifier.getLevel()));
        ToolStats.ARMOR_TOUGHNESS.multiply(builder,Math.pow(1.4,modifier.getLevel()));
        ToolStats.PROJECTILE_DAMAGE.multiply(builder,Math.pow(1.4,modifier.getLevel()));
        ToolStats.KNOCKBACK_RESISTANCE.multiply(builder,Math.pow(1.4,modifier.getLevel()));
        ToolStats.BLOCK_AMOUNT.multiply(builder,Math.pow(1.4,modifier.getLevel()));
        ToolStats.BLOCK_ANGLE.multiply(builder,Math.pow(1.4,modifier.getLevel()));
    }
}