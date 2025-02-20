package com.momosensei.momotinker.Modifiers.modifiers;


import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class SuperancientMetals extends momomodifier{
    public SuperancientMetals() {
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        if (modifier.getLevel() > 0) {
            ToolStats.DURABILITY.multiply(builder, 1.3);
            ToolStats.ATTACK_SPEED.multiply(builder, 1.3);
            ToolStats.ATTACK_DAMAGE.multiply(builder, 1.3);
            ToolStats.ACCURACY.multiply(builder, 1.3);
            ToolStats.DRAW_SPEED.multiply(builder, 1.3);
            ToolStats.VELOCITY.multiply(builder, 1.3);
            ToolStats.MINING_SPEED.multiply(builder, 1.3);
            ToolStats.ARMOR.multiply(builder, 1.3);
            ToolStats.ARMOR_TOUGHNESS.multiply(builder, 1.3);
            ToolStats.PROJECTILE_DAMAGE.multiply(builder, 1.3);
            ToolStats.KNOCKBACK_RESISTANCE.multiply(builder, 1.3);
            ToolStats.BLOCK_AMOUNT.multiply(builder, 1.3);
            ToolStats.BLOCK_ANGLE.multiply(builder, 1.3);
        }
    }
    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity livingEntity) {
        if (modifier.getLevel()>0){
            return (int) (amount * 0.5f);
        }
        return amount;
    }
}