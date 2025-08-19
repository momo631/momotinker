package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;
import java.util.Random;


public class MaterialReforging extends momomodifier {
    public MaterialReforging() {
    }
    public static final ResourceLocation reforging_odds = Momotinker.getResource("reforging_odds");
    public static final ResourceLocation reforging_points = Momotinker.getResource("reforging_points");

    @Override
    public Component validate(IToolStackView tool, ModifierEntry modifier) {
        if (tool.getModifierLevel(this)<=5&&tool.getModifierLevel(MomotinkerModifiers.rare_crystals.getId())==0){
            tool.getPersistentData().putFloat(reforging_odds,tool.getPersistentData().getFloat(reforging_odds)+1);
        }
        else if (tool.getModifierLevel(MomotinkerModifiers.rare_crystals.getId())>0){
            tool.getPersistentData().putFloat(reforging_odds,tool.getPersistentData().getFloat(reforging_odds)+2);
        }
        float a =tool.getPersistentData().getFloat(reforging_odds);
        float b = 0.05f+a*0.025f;
        float c = 0.2f+a*0.05f;
        Random random=new Random();
        if (tool.getModifierLevel(MomotinkerModifiers.rare_crystals.getId())>0){
            b*=2;
            c*=2;
        }
        float f = random.nextFloat(b+1f,c+1f);
        tool.getPersistentData().putFloat(reforging_points,f);
        return null;
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        float f =context.getPersistentData().getFloat(reforging_points);
        if (f!=0) {
            ToolStats.DURABILITY.multiply(builder, f);
            ToolStats.ATTACK_SPEED.multiply(builder, f);
            ToolStats.ATTACK_DAMAGE.multiply(builder, f);
            ToolStats.ACCURACY.multiply(builder, f);
            ToolStats.DRAW_SPEED.multiply(builder, f);
            ToolStats.VELOCITY.multiply(builder, f);
            ToolStats.MINING_SPEED.multiply(builder, f);
            ToolStats.ARMOR.multiply(builder, f);
            ToolStats.ARMOR_TOUGHNESS.multiply(builder, f);
            ToolStats.PROJECTILE_DAMAGE.multiply(builder, f);
            ToolStats.KNOCKBACK_RESISTANCE.multiply(builder, f);
            ToolStats.BLOCK_AMOUNT.multiply(builder, f);
            ToolStats.BLOCK_ANGLE.multiply(builder, f);
        }
    }
    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        float f = tool.getPersistentData().getFloat(reforging_points)-1f;
        float a = tool.getPersistentData().getFloat(reforging_odds);
        float b = 0.05f+a*0.025f;
        float c = 0.2f+a*0.05f;
        if (tool.getModifierLevel(MomotinkerModifiers.rare_crystals.getId())>0){
            b*=2;
            c*=2;
        }
        if (player != null) {
            tooltip.add(Component.translatable("modifier.momotinker.tooltip.material_reforging1").append(String.format("%.0f",f*100)+"%;").append(Component.translatable("modifier.momotinker.tooltip.material_reforging2")).append(String.format("%.0f",b*100)+"%,").append(String.format("%.0f",c*100)+"%]").withStyle(ChatFormatting.DARK_PURPLE));

        }
    }
}