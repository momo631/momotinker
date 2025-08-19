package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;


public class RareCrystals extends momomodifier {
    public RareCrystals() {
    }
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        float a = 0;
        for (int i=0;i<context.getMaterials().size();i++){
            MaterialVariant variant = context.getMaterials().get(i);
            if (variant.getVariant().getId().getPath().equals("dragon_jade") && variant.getVariant().getId().getNamespace().equals("momotinker")) {
                a+=1;
            }
        }
        float b=1f - (a /context.getMaterials().size());
        if (b==0){
            b=0.01f;
        }
        ToolStats.DURABILITY.multiply(builder, b);
        ToolStats.ATTACK_SPEED.multiply(builder, b);
        ToolStats.ATTACK_DAMAGE.multiply(builder, b);
        ToolStats.ACCURACY.multiply(builder, b);
        ToolStats.DRAW_SPEED.multiply(builder, b);
        ToolStats.VELOCITY.multiply(builder, b);
        ToolStats.MINING_SPEED.multiply(builder, b);
        ToolStats.ARMOR.multiply(builder, b);
        ToolStats.ARMOR_TOUGHNESS.multiply(builder, b);
        ToolStats.PROJECTILE_DAMAGE.multiply(builder, b);
        ToolStats.KNOCKBACK_RESISTANCE.multiply(builder, b);
        ToolStats.BLOCK_AMOUNT.multiply(builder, b);
        ToolStats.BLOCK_ANGLE.multiply(builder, b);
    }
}