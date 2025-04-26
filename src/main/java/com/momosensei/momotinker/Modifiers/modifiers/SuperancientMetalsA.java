package com.momosensei.momotinker.Modifiers.modifiers;


import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerConfig;
import net.minecraft.resources.ResourceLocation;
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

import static com.momosensei.momotinker.tool.divine_punishment_spear.degenerate;
import static com.momosensei.momotinker.tool.divine_punishment_spear.sanctification;

public class SuperancientMetalsA extends momomodifier {
    public SuperancientMetalsA() {
    }

    public static final ResourceLocation authenticationa = Momotinker.getResource("authenticationa");
    public static final MaterialVariantId id_dim_dark_gold = MaterialVariantId.create(new MaterialId("momotinker","dim_dark_gold"),"default");
    public static final MaterialVariantId id_stained_blood_gold = MaterialVariantId.create(new MaterialId("momotinker","stained_blood_gold"),"default");
    public static final MaterialVariantId id_starry_mysterious_gold = MaterialVariantId.create(new MaterialId("momotinker","starry_mysterious_gold"),"default");

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        if (modifier.getLevel() > 0) {
            ToolStats.DURABILITY.multiply(builder, 1.4);
            ToolStats.ATTACK_SPEED.multiply(builder, 1.4);
            ToolStats.ATTACK_DAMAGE.multiply(builder, 1.4);
            ToolStats.ACCURACY.multiply(builder, 1.4);
            ToolStats.DRAW_SPEED.multiply(builder, 1.4);
            ToolStats.VELOCITY.multiply(builder, 1.4);
            ToolStats.MINING_SPEED.multiply(builder, 1.4);
            ToolStats.ARMOR.multiply(builder, 1.4);
            ToolStats.ARMOR_TOUGHNESS.multiply(builder, 1.4);
            ToolStats.PROJECTILE_DAMAGE.multiply(builder, 1.4);
            ToolStats.KNOCKBACK_RESISTANCE.multiply(builder, 1.4);
            ToolStats.BLOCK_AMOUNT.multiply(builder, 1.4);
            ToolStats.BLOCK_ANGLE.multiply(builder, 1.4);
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
        int sanctification_limit = MomotinkerConfig.sanctification_limit.get();
        int degenerate_limit = MomotinkerConfig.degenerate_limit.get();
        if (tool.getPersistentData().getFloat(degenerate)==degenerate_limit){
            setHeatLevel(tool,1);
        }
        if (tool.getPersistentData().getFloat(sanctification)==sanctification_limit){
            setHeatLevel(tool,2);
        }
    }
    public static int getPersistentLevel(IToolStackView tool){
        int a = tool.getPersistentData().getInt(authenticationa);
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
        tool.getPersistentData().putInt(authenticationa, heat);
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
        int Level =Math.min(2,getPersistentLevel(tool));
        for (int i = 0; i < materials.size(); i++) {
            MaterialVariant variant = materials.get(i);
            if (variant.getVariant().getId().getPath().equals("dim_dark_gold") && variant.getVariant().getId().getNamespace().equals("momotinker")) {
                switch (Level) {
                    default -> tool.replaceMaterial(i, id_dim_dark_gold);
                    case 1 -> tool.replaceMaterial(i, id_stained_blood_gold);
                    case 2 -> tool.replaceMaterial(i, id_starry_mysterious_gold);
                }
            }
        }
    }
}