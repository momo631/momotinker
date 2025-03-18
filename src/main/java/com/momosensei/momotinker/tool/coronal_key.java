package com.momosensei.momotinker.tool;


import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.Iterator;
import java.util.List;

public class coronal_key extends ModifiableItem {
    public coronal_key(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
    }

    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        return !player.isCreative();
    }

    public List<Component> getStatInformation(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        tooltips = this.getDivinePunishmentSpearStats(tool, player, tooltips, key, tooltipFlag);
        return tooltips;
    }
    public List<Component> getDivinePunishmentSpearStats(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
        if (tool.hasTag(TinkerTags.Items.DURABILITY)) {
            builder.add(ToolStats.DURABILITY);
        }
        if (tool.hasTag(TinkerTags.Items.MELEE)) {
            builder.add(ToolStats.ATTACK_DAMAGE);
            builder.add(ToolStats.ATTACK_SPEED);
        }
        builder.addAllFreeSlots();
        Iterator var7 = tool.getModifierList().iterator();
        while(var7.hasNext()) {
            ModifierEntry entry = (ModifierEntry)var7.next();
            ((TooltipModifierHook)entry.getHook(ModifierHooks.TOOLTIP)).addTooltip(tool, entry, player, tooltips, key, tooltipFlag);
        }
        return tooltips;
    }
}