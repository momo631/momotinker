package com.momosensei.momotinker.Modifiers.modifiers;


import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;
import java.util.List;

public class DrinkingDemon extends momomodifier {
    public DrinkingDemon() {
    }
    public static final ResourceLocation defenseenchant = Momotinker.getResource("defenseenchant");
    public static final ResourceLocation meleeenchant = Momotinker.getResource("meleeenchant");
    public static final ResourceLocation projectileenchant = Momotinker.getResource("projectileenchant");
    public static final ResourceLocation toolsenchant = Momotinker.getResource("toolsenchant");
    public static final ResourceLocation curseenchant = Momotinker.getResource("curseenchant");

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(defenseenchant);
        iToolStackView.getPersistentData().remove(meleeenchant);
        iToolStackView.getPersistentData().remove(projectileenchant);
        iToolStackView.getPersistentData().remove(toolsenchant);
        iToolStackView.getPersistentData().remove(curseenchant);
        return null;
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        int a = tool.getPersistentData().getInt(defenseenchant) + tool.getPersistentData().getInt(meleeenchant) + tool.getPersistentData().getInt(projectileenchant) + tool.getPersistentData().getInt(toolsenchant) + tool.getPersistentData().getInt(curseenchant);
        int c = tool.getPersistentData().getInt(meleeenchant);
        int d = tool.getPersistentData().getInt(projectileenchant);
        int e = tool.getPersistentData().getInt(toolsenchant);
        float f = (1-((float) 5 / (tool.getPersistentData().getInt(curseenchant) + 5)))*100;
        float g = (float) 5 /(tool.getPersistentData().getInt(curseenchant)+5);
        if (player != null) {
            if (player.totalExperience < a * 50 * g) {
                tooltip.add(Component.translatable("modifier.momotinker.tooltip.drinkingdemon1").withStyle(ChatFormatting.RED));
            }
            if (player.totalExperience > a * 50 * g) {
                tooltip.add(Component.translatable("modifier.momotinker.tooltip.drinkingdemon2").withStyle(ChatFormatting.GREEN));
            }
            tooltip.add(Component.translatable("modifier.momotinker.tooltip.drinkingdemon3").append((tool.getPersistentData().getInt(curseenchant)) + ";").append(Component.translatable("modifier.momotinker.tooltip.drinkingdemon4")).append((f) + "%").withStyle(ChatFormatting.DARK_PURPLE));
            int b1=  ToolStack.from(player.getItemBySlot(EquipmentSlot.HEAD)).getPersistentData().getInt(defenseenchant)
                    +ToolStack.from(player.getItemBySlot(EquipmentSlot.CHEST)).getPersistentData().getInt(defenseenchant)
                    +ToolStack.from(player.getItemBySlot(EquipmentSlot.LEGS)).getPersistentData().getInt(defenseenchant)
                    +ToolStack.from(player.getItemBySlot(EquipmentSlot.FEET)).getPersistentData().getInt(defenseenchant);
            float b = (1-((float) 80 / (b1 + 80)))*100;
            if (tool.hasTag(TinkerTags.Items.ARMOR)) {
                tooltip.add(Component.translatable("modifier.momotinker.tooltip.drinkingdemon5").append((b1) + ";").append(Component.translatable("modifier.momotinker.tooltip.drinkingdemon6")).append((b) + "%").withStyle(ChatFormatting.DARK_PURPLE));
            }
            if (!tool.hasTag(TinkerTags.Items.ARMOR)) {
                tooltip.add(Component.translatable("modifier.momotinker.tooltip.drinkingdemon7").append((c) + ";").append(Component.translatable("modifier.momotinker.tooltip.drinkingdemon8")).append((c) + "%").withStyle(ChatFormatting.DARK_PURPLE));
                tooltip.add(Component.translatable("modifier.momotinker.tooltip.drinkingdemon9").append((d) + ";").append(Component.translatable("modifier.momotinker.tooltip.drinkingdemon10")).append((d * 0.5) + "%").withStyle(ChatFormatting.DARK_PURPLE));
                tooltip.add(Component.translatable("modifier.momotinker.tooltip.drinkingdemon11").append((e) + ";").append(Component.translatable("modifier.momotinker.tooltip.drinkingdemon12")).append((e) + "%").withStyle(ChatFormatting.DARK_PURPLE));
            }
        }
    }
}