package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.register.MomotinkerEffects;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
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
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

import javax.annotation.Nonnull;
import java.util.List;

public class VehementDesire extends momomodifier {
    public VehementDesire() {
    }

    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity target = context.getLivingTarget();
        LivingEntity attacker =context.getAttacker();
        if (target != null && modifier.getLevel() > 0) {
            if (!target.hasEffect(MomotinkerEffects.LostSoul.get())) {
                target.addEffect(new MobEffectInstance(MomotinkerEffects.LostSoul.get(), 400, modifier.getLevel() - 1, false, false));
            }
            if (target.getEffect(MomotinkerEffects.LostSoul.get()) != null && target.getEffect(MomotinkerEffects.LostSoul.get()).getDuration() < 400) {
                target.addEffect(new MobEffectInstance(MomotinkerEffects.LostSoul.get(), 400, modifier.getLevel() - 1, false, false));
            }
        }
        if (attacker instanceof Player player&&target != null&& modifier.getLevel() > 0){
            float a = modifier.getLevel() * 0.4F;
            float b = (float) target.getArmorValue() / modifier.getLevel() * 30;
            if (b < 1) {
                return damage * (1F + a - b);
            }
        }
        return damage;
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker, @javax.annotation.Nullable LivingEntity target) {
        if (target != null && modifier.getLevel() > 0) {
            if (!target.hasEffect(MomotinkerEffects.LostSoul.get())) {
                target.addEffect(new MobEffectInstance(MomotinkerEffects.LostSoul.get(), 400, modifier.getLevel() - 1, false, false));
            }
            if (target.getEffect(MomotinkerEffects.LostSoul.get()) != null && target.getEffect(MomotinkerEffects.LostSoul.get()).getDuration() < 400) {
                target.addEffect(new MobEffectInstance(MomotinkerEffects.LostSoul.get(), 400, modifier.getLevel() - 1, false, false));
            }
            if (attacker instanceof Player player && projectile instanceof AbstractArrow arrow) {
                float a = modifier.getLevel() * 0.4F;
                float b = (float) target.getArmorValue() / modifier.getLevel() * 30;
                if (b < 1) {
                    arrow.setBaseDamage(arrow.getBaseDamage()*(1F + a - b));
                }
            }
        }
        return false;
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player != null) {
            float a = ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.MAINHAND), MomotinkerModifiers.vehementdesire.getId());
            tooltip.add(net.minecraft.network.chat.Component.translatable("目前最高增伤为" + (a*40)+"%").withStyle(ChatFormatting.DARK_PURPLE));
            tooltip.add(net.minecraft.network.chat.Component.translatable("目标护甲为" + (a*30)+"时增伤为0%").withStyle(ChatFormatting.DARK_PURPLE));
        }
    }
}