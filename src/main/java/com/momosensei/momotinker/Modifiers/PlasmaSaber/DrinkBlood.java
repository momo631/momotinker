package com.momosensei.momotinker.Modifiers.PlasmaSaber;

import com.momosensei.momotinker.Modifiers.modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

import javax.annotation.Nonnull;
import java.util.List;


public class DrinkBlood extends momomodifier {
    public DrinkBlood() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity target = context.getLivingTarget();
        LivingEntity attacker =context.getAttacker();
        if (attacker instanceof Player player&&target != null&& modifier.getLevel() > 0) {
            float a = modifier.getLevel();
            if (attacker.getHealth() > attacker.getMaxHealth()*0.100001f){
                attacker.hurt(DamageSource.OUT_OF_WORLD.bypassArmor().bypassMagic().bypassInvul().bypassEnchantments(), attacker.getMaxHealth()*0.1f);
                return damage * (1F + a*0.6F);
            }
            if (attacker.getHealth() < attacker.getMaxHealth()*0.1f){
                return damage * (1F + a*0.1F);
            }
        }
        return damage;
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker, @javax.annotation.Nullable LivingEntity target) {
        if (target != null && modifier.getLevel() > 0) {
            if (attacker instanceof Player player && projectile instanceof AbstractArrow arrow ) {
                float a = modifier.getLevel();
                arrow.setBaseDamage(arrow.getBaseDamage()*(1F + a*0.1F));
            }
        }
        return false;
    }

    private void livinghurtevent(LivingHurtEvent event) {
        Entity entity=event.getSource().getEntity();
        if (entity instanceof LivingEntity attacker){
            int a = ModifierUtil.getModifierLevel(attacker.getItemBySlot(EquipmentSlot.MAINHAND), MomotinkerModifiers.drinkblood.getId());
            if (attacker.getHealth() > attacker.getMaxHealth()*0.1f && a > 0){
                attacker.heal(event.getAmount() * a * 0.3f);
            }
            if (attacker.getHealth() < attacker.getMaxHealth()*0.1f && a > 0){
                attacker.heal(event.getAmount() * a * 0.1f);
            }
        }
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player != null) {
            int a = ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.MAINHAND), MomotinkerModifiers.drinkblood.getId());
            tooltip.add(net.minecraft.network.chat.Component.translatable("当前每次攻击消耗血量为" + (player.getMaxHealth()*a*0.1f)).withStyle(ChatFormatting.DARK_GRAY));
            tooltip.add(net.minecraft.network.chat.Component.translatable("未衰减状态时的增伤为" + (a*60)+"%（衰减状态为"+(a*10)+"%）").withStyle(ChatFormatting.DARK_GRAY));
            tooltip.add(net.minecraft.network.chat.Component.translatable("可恢复实际伤害" + (a*30)+"%（衰减状态为"+(a*10)+"%）").withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}