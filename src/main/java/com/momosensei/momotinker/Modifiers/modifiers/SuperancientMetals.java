package com.momosensei.momotinker.Modifiers.modifiers;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.MaterialNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.ArrayList;
import java.util.List;

import static com.momosensei.momotinker.tool.divine_punishment_spear.degenerate;
import static com.momosensei.momotinker.tool.divine_punishment_spear.sanctification;

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
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity livingEntity, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (tool.getPersistentData().getFloat(sanctification)==500){
            EvolutionTool((ToolStack) tool);
        }
        if (tool.getPersistentData().getFloat(degenerate)==1){
            HiddenEvolutionTool((ToolStack) tool);
        }
    }
    public void EvolutionTool(ToolStack tool){
        int length = tool.getMaterials().size();
        List<MaterialVariant> list=new ArrayList<>(List.of());
        for (int i=0;i<length;i++){
            list.add(MaterialVariant.of(MaterialVariantId.create(new MaterialId("momotinker:starry_mysterious_gold"),"default")));
        }
        MaterialNBT nbt = new MaterialNBT(list);
        tool.setMaterials(nbt);
        tool.setDamage(0);
        tool.rebuildStats();
    }
    public void HiddenEvolutionTool(ToolStack tool){
        int length = tool.getMaterials().size();
        List<MaterialVariant> list=new ArrayList<>(List.of());
        for (int i = 0; i < length; i++) {
            list.add(MaterialVariant.of(MaterialVariantId.create(new MaterialId("momotinker:stained_blood_gold"), "default")));
        }
        MaterialNBT nbt = new MaterialNBT(list);
        tool.setMaterials(nbt);
        tool.setDamage(0);
        tool.rebuildStats();
    }
}