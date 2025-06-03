package com.momosensei.momotinker.Modifiers.modifiers;


import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.*;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import static com.momosensei.momotinker.tool.pocket_watch.backtracking;
import static com.momosensei.momotinker.tool.pocket_watch.transmit;

public class SuperancientMetalsC extends momomodifier {
    public SuperancientMetalsC() {
    }
    public static final ResourceLocation authenticationc = Momotinker.getResource("authenticationc");
    public static final MaterialVariantId id_twilight_purple_gold = MaterialVariantId.create(new MaterialId("momotinker", "twilight_purple_gold"), "default");
    public static final MaterialVariantId id_timetrace_purple_gold = MaterialVariantId.create(new MaterialId("momotinker", "timetrace_purple_gold"), "default");
    public static final MaterialVariantId id_reversetime_purple_gold = MaterialVariantId.create(new MaterialId("momotinker", "reversetime_purple_gold"), "default");

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        if (modifier.getLevel() > 0) {
            ToolStats.DURABILITY.multiply(builder, 1.75);
            ToolStats.ATTACK_SPEED.multiply(builder, 1.5);
            ToolStats.ATTACK_DAMAGE.multiply(builder, 1.5);
            ToolStats.ACCURACY.multiply(builder, 1.5);
            ToolStats.DRAW_SPEED.multiply(builder, 1.5);
            ToolStats.VELOCITY.multiply(builder, 1.5);
            ToolStats.MINING_SPEED.multiply(builder, 1.5);
            ToolStats.ARMOR.multiply(builder, 1.5);
            ToolStats.ARMOR_TOUGHNESS.multiply(builder, 1.5);
            ToolStats.PROJECTILE_DAMAGE.multiply(builder, 1.5);
            ToolStats.KNOCKBACK_RESISTANCE.multiply(builder, 1.5);
            ToolStats.BLOCK_AMOUNT.multiply(builder, 1.5);
            ToolStats.BLOCK_ANGLE.multiply(builder, 1.5);
        }
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        int transmit_limit = MomotinkerConfig.transmit_limit.get();
        int backtracking_limit = MomotinkerConfig.backtracking_limit.get();
        ModDataNBT a = tool.getPersistentData();
        if (a.getInt(transmit) == transmit_limit) {
            setHeatLevel(tool, 1);
        }
        if (a.getInt(backtracking) == backtracking_limit) {
            setHeatLevel(tool, 2);
        }
    }


    public static int getPersistentLevel(IToolStackView tool){
        int a = tool.getPersistentData().getInt(authenticationc);
        return getLevel(a);
    }
    public static int getLevel(int a){
        if (a==1){
            return 1;
        }
        else if (a==2){
            return 2;
        }
        else return 0;
    }
    public static void setHeat(IToolStackView tool,int heat){
        tool.getPersistentData().putInt(authenticationc, heat);
        EvolutionTool((ToolStack) tool);
    }
    public static void setHeatLevel(IToolStackView tool,int heatLevel){
        switch (heatLevel){
            default -> setHeat(tool,0);
            case 1 -> setHeat(tool,1);
            case 2 -> setHeat(tool,2);
        }
    }
    public static void EvolutionTool(ToolStack tool) {
        MaterialNBT materials = tool.getMaterials();
        int Level =Math.min(4,getPersistentLevel(tool));
        for (int i = 0; i < materials.size(); i++) {
            MaterialVariant variant = materials.get(i);
            if (variant.getVariant().getId().getPath().equals("twilight_purple_gold") && variant.getVariant().getId().getNamespace().equals("momotinker")) {
                switch (Level) {
                    default -> tool.replaceMaterial(i, id_twilight_purple_gold);
                    case 1 -> tool.replaceMaterial(i, id_timetrace_purple_gold);
                    case 2 -> tool.replaceMaterial(i, id_reversetime_purple_gold);
                }
            }
        }
    }
}