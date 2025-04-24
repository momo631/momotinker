package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerConfig;
import com.momosensei.momotinker.register.MomotinkerItem;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.momosensei.momotinker.tool.divine_punishment_spear.degenerate;
import static com.momosensei.momotinker.tool.divine_punishment_spear.sanctification;
import static com.momosensei.momotinker.tool.entropy_burning_cube.*;

public class FromBrilliance extends momomodifier {
    public FromBrilliance() {
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(sanctification);
        iToolStackView.getPersistentData().remove(degenerate);
        iToolStackView.getPersistentData().remove(hadal);
        iToolStackView.getPersistentData().remove(stellarcore);
        iToolStackView.getPersistentData().remove(crystallized);
        iToolStackView.getPersistentData().remove(liverization);
        return null;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        int sanctification_limit = MomotinkerConfig.sanctification_limit.get();
        int degenerate_limit = MomotinkerConfig.degenerate_limit.get();
        int hadal_limit = MomotinkerConfig.hadal_limit.get();
        int stellarcore_limit = MomotinkerConfig.stellarcore_limit.get();
        int crystallized_limit = MomotinkerConfig.crystallized_limit.get();
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
    }

    public float getbonus(float speed, int status) {
        return speed * status;
    }

    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity attacker = context.getAttacker();
        int hadal_limit = MomotinkerConfig.hadal_limit.get();
        int stellarcore_limit = MomotinkerConfig.stellarcore_limit.get();
        int d = MomotinkerConfig.entropy_burning_riding_spear_limit.get();
        if (attacker instanceof Player player && context.getLivingTarget() != null) {
            ModDataNBT c = ToolStack.from(player.getMainHandItem()).getPersistentData();
            if (player.getMainHandItem().is(MomotinkerItem.entropy_burning_riding_spear.get())) {
                float speed = (float) player.getDeltaMovement().length();
                float bonus;
                bonus = getbonus(speed, 5) * 100;
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
            if (player.getMainHandItem().is(MomotinkerItem.entropy_burning_sword.get())) {
                if (c.getInt(stellarcore) == stellarcore_limit) {
                    context.getLivingTarget().invulnerableTime = 0;
                    context.getLivingTarget().hurt(DamageSource.LAVA, damage * 0.25f);
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
                builder.add(Component.translatable("modifier.momotinker.tooltip.stellarcore1").withStyle(ChatFormatting.YELLOW));
            }
            if (a.getInt(crystallized)==crystallized_limit) {
                builder.add(Component.translatable("modifier.momotinker.tooltip.crystallized1").withStyle(ChatFormatting.AQUA));
            }
            if (a.getInt(liverization)>=liverization_limit) {
                builder.add(Component.translatable("modifier.momotinker.tooltip.liverization1").withStyle(ChatFormatting.RED));
            }
        }
    }
}