package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.register.MomotinkerEffects;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

import javax.annotation.Nonnull;
import java.util.List;


public class Huangquan extends momomodifier {
    public Huangquan() {
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void LivingAttackEvent(LivingAttackEvent event) {
        Entity a = event.getSource().getEntity();
        if (a instanceof Player player &&player.getEffect(MomotinkerEffects.End.get())!=null &&player.hasEffect(MomotinkerEffects.End.get())) {
            if (ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.MAINHAND), MomotinkerModifiers.huangquan.getId()) > 0) {
                event.getEntity().invulnerableTime = 0;
                event.getSource().bypassArmor().bypassMagic().bypassInvul().bypassEnchantments();
                event.getEntity().invulnerableTime = 0;
            }
        }
    }

    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity attacker =context.getAttacker();
        if (attacker instanceof Player player){
            float var = (player.getMaxHealth() - player.getHealth())*6;
            if (player.getHealth()<player.getMaxHealth()*0.6F) {
                if (!(player.hasEffect(MomotinkerEffects.End.get()))) {
                    return damage + var;
                }
                if (player.getEffect(MomotinkerEffects.End.get())!=null && player.hasEffect(MomotinkerEffects.End.get())){
                    return damage + var*2;
                }
            }
        }
        return damage;
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker, @javax.annotation.Nullable LivingEntity target) {
        if (attacker instanceof Player player && projectile instanceof AbstractArrow arrow){
            float var = (player.getMaxHealth() - player.getHealth())*6;
            if (target != null&&player.getHealth()<player.getMaxHealth()*0.6F) {
                if (!(player.hasEffect(MomotinkerEffects.End.get()))) {
                    arrow.setBaseDamage(arrow.getBaseDamage() + var*0.25);
                }
                if (player.getEffect(MomotinkerEffects.End.get())!=null &&player.hasEffect(MomotinkerEffects.End.get())) {
                    arrow.setBaseDamage(arrow.getBaseDamage() + var*0.5);
                }
            }
        }
        return false;
    }


    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<net.minecraft.network.chat.Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player != null) {
            float var = player.getMaxHealth() - player.getHealth();
            tooltip.add(net.minecraft.network.chat.Component.translatable("当前已损失生命" + var).withStyle(ChatFormatting.DARK_RED));
            tooltip.add(net.minecraft.network.chat.Component.translatable("已实际提升伤害" + var*6 + "攻击力").withStyle(ChatFormatting.DARK_RED));
            tooltip.add(net.minecraft.network.chat.Component.translatable("按下特殊技能按键引领众生至彼岸").withStyle(ChatFormatting.DARK_RED));
        }
    }
}
