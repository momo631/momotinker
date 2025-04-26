package com.momosensei.momotinker.Modifiers.modifiers;


import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.*;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import static com.momosensei.momotinker.tool.entropy_burning_cube.*;

public class SuperancientMetalsB extends momomodifier {
    public SuperancientMetalsB() {
    }

    public static final ResourceLocation authenticationb = Momotinker.getResource("authenticationb");
    public static final MaterialVariantId id_ashen_platinum = MaterialVariantId.create(new MaterialId("momotinker", "ashen_platinum"), "default");
    public static final MaterialVariantId id_hadal_platinum = MaterialVariantId.create(new MaterialId("momotinker", "hadal_platinum"), "default");
    public static final MaterialVariantId id_stellar_core_platinum = MaterialVariantId.create(new MaterialId("momotinker", "stellar_core_platinum"), "default");
    public static final MaterialVariantId id_crystallized_platinum = MaterialVariantId.create(new MaterialId("momotinker", "crystallized_platinum"), "default");
    public static final MaterialVariantId id_living_platinum = MaterialVariantId.create(new MaterialId("momotinker", "living_platinum"), "default");

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        if (modifier.getLevel() > 0) {
            ToolStats.DURABILITY.multiply(builder, 1.25);
            ToolStats.ATTACK_SPEED.multiply(builder, 1.25);
            ToolStats.ATTACK_DAMAGE.multiply(builder, 1.25);
            ToolStats.ACCURACY.multiply(builder, 1.25);
            ToolStats.DRAW_SPEED.multiply(builder, 1.25);
            ToolStats.VELOCITY.multiply(builder, 1.25);
            ToolStats.MINING_SPEED.multiply(builder, 1.25);
            ToolStats.ARMOR.multiply(builder, 1.25);
            ToolStats.ARMOR_TOUGHNESS.multiply(builder, 1.25);
            ToolStats.PROJECTILE_DAMAGE.multiply(builder, 1.25);
            ToolStats.KNOCKBACK_RESISTANCE.multiply(builder, 1.25);
            ToolStats.BLOCK_AMOUNT.multiply(builder, 1.25);
            ToolStats.BLOCK_ANGLE.multiply(builder, 1.25);
        }
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (tool.getDamage()>0&&entity instanceof Player player&&player.tickCount%10==0){
            tool.setDamage(tool.getDamage()-1);
        }
        if (tool.getDamage()==0&&entity instanceof Player player&&player.tickCount%40==0&&player.getHealth()<player.getMaxHealth()){
            player.heal(1);
        }
        int hadal_limit = MomotinkerConfig.hadal_limit.get();
        int stellarcore_limit = MomotinkerConfig.stellarcore_limit.get();
        int crystallized_limit = MomotinkerConfig.crystallized_limit.get();
        int liverization_limit = MomotinkerConfig.liverization_limit.get();
        ModDataNBT a = tool.getPersistentData();
        if (a.getInt(hadal) == hadal_limit) {
            setHeatLevel(tool, 1);
        }
        if (a.getInt(stellarcore) == stellarcore_limit) {
            setHeatLevel(tool, 2);
        }
        if (a.getInt(crystallized) == crystallized_limit) {
            setHeatLevel(tool, 3);
        }
        if (a.getInt(liverization) >= liverization_limit) {
            setHeatLevel(tool, 4);
        }
    }

    
    public static int getPersistentLevel(IToolStackView tool){
        int a = tool.getPersistentData().getInt(authenticationb);
        return getLevel(a);
    }
    public static int getLevel(int a){
        if (a==1){
            return 1;
        }
        else if (a==2){
            return 2;
        }
        else if (a==3){
            return 3;
        }
        else if (a==4){
            return 4;
        }
        else return 0;
    }
    public static void setHeat(IToolStackView tool,int heat){
        tool.getPersistentData().putInt(authenticationb, heat);
        EvolutionTool((ToolStack) tool);
    }
    public static void setHeatLevel(IToolStackView tool,int heatLevel){
        switch (heatLevel){
            default -> setHeat(tool,0);
            case 1 -> setHeat(tool,1);
            case 2 -> setHeat(tool,2);
            case 3 -> setHeat(tool,3);
            case 4 -> setHeat(tool,4);
        }
    }
    public static void EvolutionTool(ToolStack tool) {
        MaterialNBT materials = tool.getMaterials();
        int Level =Math.min(4,getPersistentLevel(tool));
        for (int i = 0; i < materials.size(); i++) {
            MaterialVariant variant = materials.get(i);
            if (variant.getVariant().getId().getPath().equals("ashen_platinum") && variant.getVariant().getId().getNamespace().equals("momotinker")) {
                switch (Level) {
                    default -> tool.replaceMaterial(i, id_ashen_platinum);
                    case 1 -> tool.replaceMaterial(i, id_hadal_platinum);
                    case 2 -> tool.replaceMaterial(i, id_stellar_core_platinum);
                    case 3 -> tool.replaceMaterial(i, id_crystallized_platinum);
                    case 4 -> tool.replaceMaterial(i, id_living_platinum);
                }
            }
        }
    }
}