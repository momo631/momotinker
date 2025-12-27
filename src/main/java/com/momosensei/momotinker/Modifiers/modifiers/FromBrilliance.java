package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerConfig;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import com.momosensei.momotinker.register.MomotinkerTools;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierTraitHook;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.momosensei.momotinker.tool.divine_punishment_spear.degenerate;
import static com.momosensei.momotinker.tool.divine_punishment_spear.sanctification;
import static com.momosensei.momotinker.tool.entropy_burning_cube.*;
import static com.momosensei.momotinker.tool.pocket_watch.backtracking;
import static com.momosensei.momotinker.tool.pocket_watch.transmit;

public class FromBrilliance extends momomodifier {
    public FromBrilliance() {
    }

    @Override
    public boolean isNoLevels() {
        return true;
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
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(sanctification);
        iToolStackView.getPersistentData().remove(degenerate);
        iToolStackView.getPersistentData().remove(hadal);
        iToolStackView.getPersistentData().remove(stellarcore);
        iToolStackView.getPersistentData().remove(crystallized);
        iToolStackView.getPersistentData().remove(liverization);
        iToolStackView.getPersistentData().remove(backtracking);
        return null;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        int sanctification_limit = MomotinkerConfig.sanctification_limit.get();
        int degenerate_limit = MomotinkerConfig.degenerate_limit.get();
        int hadal_limit = MomotinkerConfig.hadal_limit.get();
        int stellarcore_limit = MomotinkerConfig.stellarcore_limit.get();
        int crystallized_limit = MomotinkerConfig.crystallized_limit.get();
        int backtracking_limit = MomotinkerConfig.backtracking_limit.get();
        int transmit_limit = MomotinkerConfig.transmit_limit.get();
        ModDataNBT a = tool.getPersistentData();
        if (a.getInt(degenerate) > degenerate_limit) {
            a.putInt(degenerate, degenerate_limit);
        }
        if (a.getInt(sanctification) > sanctification_limit) {
            a.putInt(sanctification, sanctification_limit);
        }
        if (a.getInt(hadal) > hadal_limit) {
            a.putInt(hadal, hadal_limit);
        }
        if (a.getInt(stellarcore) > stellarcore_limit) {
            a.putInt(stellarcore, stellarcore_limit);
        }
        if (a.getInt(crystallized) > crystallized_limit) {
            a.putInt(crystallized, crystallized_limit);
        }
        if (a.getInt(backtracking) > backtracking_limit) {
            a.putInt(backtracking, backtracking_limit);
        }
        if (a.getInt(transmit) > transmit_limit) {
            a.putInt(transmit, transmit_limit);
        }
    }

    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity attacker = context.getAttacker();
        int hadal_limit = MomotinkerConfig.hadal_limit.get();
        int stellarcore_limit = MomotinkerConfig.stellarcore_limit.get();
        int d = MomotinkerConfig.entropy_burning_riding_spear_limit.get();
        if (attacker instanceof Player player && context.getLivingTarget() != null&&isToolStack(player.getMainHandItem())) {
            ModDataNBT c = ToolStack.from(player.getMainHandItem()).getPersistentData();
            if (player.getMainHandItem().is(MomotinkerTools.entropy_burning_riding_spear.get())) {
                float bonus = getbonus(player,2500);
                if (c.getInt(hadal) == hadal_limit) {
                    if (bonus < 3 * d) {
                        return damage * (1f + 2 * bonus * 0.01f);
                    } else if (bonus > 3 * d) {
                        return damage * (1f + 3 * d * 0.01f);
                    }
                } else {
                    if (bonus < d) {
                        return damage * (1f + bonus * 0.01f);
                    } else if (bonus > d) {
                        return damage * (1f + d * 0.01f);
                    }
                }
            }
            if (player.getMainHandItem().is(MomotinkerTools.entropy_burning_sword.get())) {
                if (c.getInt(stellarcore) == stellarcore_limit) {
                    float e = (float) (player.getArmorValue()*0.5f+player.getAttributeValue(Attributes.ARMOR_TOUGHNESS)*0.25f);
                    context.getLivingTarget().invulnerableTime = 0;
                    context.getLivingTarget().hurt(player.level().damageSources().lava(), damage * 0.05f+e);
                    context.getLivingTarget().invulnerableTime = 0;
                }
            }
        }
        return damage;
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> builder, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        int hadal_limit = MomotinkerConfig.hadal_limit.get();
        int stellarcore_limit = MomotinkerConfig.stellarcore_limit.get();
        int crystallized_limit = MomotinkerConfig.crystallized_limit.get();
        int liverization_limit = MomotinkerConfig.liverization_limit.get();
        int sanctification_limit = MomotinkerConfig.sanctification_limit.get();
        int degenerate_limit = MomotinkerConfig.degenerate_limit.get();
        int backtracking_limit = MomotinkerConfig.backtracking_limit.get();
        int transmit_limit = MomotinkerConfig.transmit_limit.get();
        ModDataNBT a = tool.getPersistentData();
        if ((tool.getModifierLevel(MomotinkerModifiers.superancientmetalsreala.getId())>0)||(tool.getModifierLevel(MomotinkerModifiers.superancientmetalsrealb.getId())>0)){
            if (a.getInt(degenerate)==degenerate_limit) {
                builder.add(Component.translatable("modifier.momotinker.tooltip.degenerate1").withStyle(ChatFormatting.DARK_RED));
            }
            if (a.getInt(sanctification)==sanctification_limit) {
                builder.add(Component.translatable("modifier.momotinker.tooltip.sanctification1").withStyle(ChatFormatting.YELLOW));
            }
            if (a.getInt(hadal)==hadal_limit) {
                builder.add(Component.translatable("modifier.momotinker.tooltip.hadal1").withStyle(ChatFormatting.DARK_BLUE));
            }
            if (a.getInt(stellarcore)==stellarcore_limit) {
                builder.add(Component.translatable("modifier.momotinker.tooltip.stellarcore1").withStyle(ChatFormatting.GOLD));
            }
            if (a.getInt(crystallized)==crystallized_limit) {
                builder.add(Component.translatable("modifier.momotinker.tooltip.crystallized1").withStyle(ChatFormatting.AQUA));
            }
            if (a.getInt(liverization)>=liverization_limit) {
                builder.add(Component.translatable("modifier.momotinker.tooltip.liverization1").withStyle(ChatFormatting.RED));
            }
            if (a.getInt(transmit)==transmit_limit) {
                builder.add(Component.translatable("modifier.momotinker.tooltip.transmit1").withStyle(ChatFormatting.GREEN));
            }
            if (a.getInt(backtracking)==backtracking_limit) {
                builder.add(Component.translatable("modifier.momotinker.tooltip.backtracking1").withStyle(ChatFormatting.DARK_PURPLE));
            }
        }
    }
}