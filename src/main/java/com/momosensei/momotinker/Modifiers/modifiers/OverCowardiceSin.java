package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import javax.annotation.Nonnull;
import java.util.List;


public class OverCowardiceSin extends momomodifier {
    public OverCowardiceSin() {
    }

    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity target = context.getLivingTarget();
        LivingEntity attacker =context.getAttacker();
        int a =   ModifierUtil.getModifierLevel(attacker.getItemBySlot(EquipmentSlot.MAINHAND), MomotinkerModifiers.overcowardicesin.getId())
                + ModifierUtil.getModifierLevel(attacker.getItemBySlot(EquipmentSlot.OFFHAND), MomotinkerModifiers.overcowardicesin.getId())
                + ModifierUtil.getModifierLevel(attacker.getItemBySlot(EquipmentSlot.HEAD), MomotinkerModifiers.overcowardicesin.getId())
                + ModifierUtil.getModifierLevel(attacker.getItemBySlot(EquipmentSlot.CHEST), MomotinkerModifiers.overcowardicesin.getId())
                + ModifierUtil.getModifierLevel(attacker.getItemBySlot(EquipmentSlot.LEGS), MomotinkerModifiers.overcowardicesin.getId())
                + ModifierUtil.getModifierLevel(attacker.getItemBySlot(EquipmentSlot.FEET), MomotinkerModifiers.overcowardicesin.getId());
        if (target != null && a > 0 && a<8) {
            return damage * (1F + a * -0.05F);
        }
        if (target != null && a > 8) {
            return damage * (1F + 8 * -0.05F);
        }
        return damage;
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker, @javax.annotation.Nullable LivingEntity target) {
        if (target!=null&&attacker != null && projectile instanceof AbstractArrow arrow) {
            int a =   ModifierUtil.getModifierLevel(attacker.getItemBySlot(EquipmentSlot.MAINHAND), MomotinkerModifiers.overcowardicesin.getId())
                    + ModifierUtil.getModifierLevel(attacker.getItemBySlot(EquipmentSlot.OFFHAND), MomotinkerModifiers.overcowardicesin.getId())
                    + ModifierUtil.getModifierLevel(attacker.getItemBySlot(EquipmentSlot.HEAD), MomotinkerModifiers.overcowardicesin.getId())
                    + ModifierUtil.getModifierLevel(attacker.getItemBySlot(EquipmentSlot.CHEST), MomotinkerModifiers.overcowardicesin.getId())
                    + ModifierUtil.getModifierLevel(attacker.getItemBySlot(EquipmentSlot.LEGS), MomotinkerModifiers.overcowardicesin.getId())
                    + ModifierUtil.getModifierLevel(attacker.getItemBySlot(EquipmentSlot.FEET), MomotinkerModifiers.overcowardicesin.getId());
            if (a>0&&a<8) {
                arrow.setBaseDamage(arrow.getBaseDamage() * (1F + a * -0.05F));
            }
            if (a > 8) {
                arrow.setBaseDamage(arrow.getBaseDamage() * (1F + 8 * -0.05F));
            }
        }
        return false;
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player != null) {
            int a = ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.MAINHAND), MomotinkerModifiers.overcowardicesin.getId())
                    + ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.OFFHAND), MomotinkerModifiers.overcowardicesin.getId())
                    + ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.HEAD), MomotinkerModifiers.overcowardicesin.getId())
                    + ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.CHEST), MomotinkerModifiers.overcowardicesin.getId())
                    + ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.LEGS), MomotinkerModifiers.overcowardicesin.getId())
                    + ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.FEET), MomotinkerModifiers.overcowardicesin.getId());
            if (a>0&&a<8) {
                tooltip.add(net.minecraft.network.chat.Component.translatable("当前已增加闪避几率" + (a * 10) + "%").withStyle(ChatFormatting.GOLD));
                tooltip.add(net.minecraft.network.chat.Component.translatable("当前已减少造成伤害" + (a * 5) + "%").withStyle(ChatFormatting.GOLD));
            }
            if (a>8) {
                tooltip.add(net.minecraft.network.chat.Component.translatable("当前已增加闪避几率" + (80) + "%").withStyle(ChatFormatting.GOLD));
                tooltip.add(net.minecraft.network.chat.Component.translatable("当前已减少造成伤害" + (40) + "%").withStyle(ChatFormatting.GOLD));
            }
        }
    }
}