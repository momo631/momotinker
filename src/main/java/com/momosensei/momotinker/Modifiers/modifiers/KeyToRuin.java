package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import com.momosensei.momotinker.register.MomotinkerToolDefinitions;
import com.momosensei.momotinker.register.MomotinkerTools;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierTraitHook;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;


public class KeyToRuin extends momomodifier {
    public KeyToRuin() {
    }
    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Nullable
    @Override
    public Component requirementsError(ModifierEntry entry) {
        return Component.translatable("recipe.momotinker.modifier.key_to_ruin");
    }
    @Override
    public @NotNull List<ModifierEntry> displayModifiers(ModifierEntry entry) {
        return List.of(new ModifierEntry(MomotinkerModifiers.frombrilliance.getId(),1));
    }
    @Override
    public Component validate(IToolStackView tool, ModifierEntry modifier) {
        if (tool.getItem()==MomotinkerTools.entropy_burning_cube.get()||tool.getItem()==MomotinkerTools.legion.get()){
            return null;
        }
        return requirementsError(modifier);
    }
    @Override
    public void addTraits(IToolContext context, ModifierEntry modifier, ModifierTraitHook.TraitBuilder builder, boolean firstEncounter) {
        for (ModifierEntry entry : context.getModifierList()) {
            if (entry.getModifier() != modifier.getModifier()&&firstEncounter) {
                builder.add(entry.getId(),entry.getLevel());
            }
        }
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (stack.getItem() == MomotinkerTools.entropy_burning_cube.get()&&entity instanceof Player player){
            ItemStack item = createNewTool(stack,MomotinkerTools.legion.get(), MomotinkerToolDefinitions.LEGION);
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                if (player.getInventory().getItem(i) == stack) {
                    player.getInventory().setItem(i, item);
                    break;
                }
            }
        }
    }
    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (tool.getItem()==MomotinkerTools.legion.get()) {
            tooltip.add(Component.translatable("modifier.momotinker.tooltip.key_to_ruin1").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC));
            tooltip.add(Component.translatable("modifier.momotinker.tooltip.key_to_ruin2").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.DARK_GRAY));
            tooltip.add(Component.translatable("modifier.momotinker.tooltip.key_to_ruin3").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC));
        }
    }

    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        for (ModifierEntry entry1 : tool.getModifierList()) {
            if (entry1.getModifier() != modifier.getModifier()) {
                damage = entry1.getHook(ModifierHooks.MELEE_DAMAGE).getMeleeDamage(tool, modifier, context, baseDamage, damage);
            }
        }
        return damage;
    }
    @Override
    public void modifierDamageDealt(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, LivingEntity entity, DamageSource damageSource, float amount, boolean isDirectDamage) {
        for (ModifierEntry entry : tool.getModifierList()) {
            if (entry.getModifier() != modifier.getModifier()) {
                entry.getHook(ModifierHooks.DAMAGE_DEALT).onDamageDealt(tool, modifier, context, slotType, entity, damageSource, amount * 0.25f, isDirectDamage);
            }
        }
    }
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        for (ModifierEntry entry : tool.getModifierList()) {
            if (entry.getModifier() != modifier.getModifier()) {
                entry.getHook(ModifierHooks.MELEE_HIT).afterMeleeHit(tool, modifier, context, damageDealt * 0.25f);
            }
        }
    }
//    @Override
//    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
//        for (ModifierEntry entry : tool.getModifierList()) {
//            if (entry.getModifier() != modifier.getModifier()) {
//                knockback = entry.getHook(ModifierHooks.MELEE_HIT).beforeMeleeHit(tool, modifier, context, damage, baseKnockback, knockback);
//            }
//        }
//        return knockback;
//    }

    @Override
    public void failedMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageAttempted) {
        for (ModifierEntry entry : tool.getModifierList()) {
            if (entry.getModifier() != modifier.getModifier()) {
                entry.getHook(ModifierHooks.MELEE_HIT).failedMeleeHit(tool, modifier, context, damageAttempted);
            }
        }
    }
}