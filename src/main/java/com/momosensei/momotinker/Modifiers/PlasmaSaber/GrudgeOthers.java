package com.momosensei.momotinker.Modifiers.PlasmaSaber;

import com.momosensei.momotinker.Modifiers.modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

import javax.annotation.Nonnull;


public class GrudgeOthers extends momomodifier {
    public GrudgeOthers() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    private void livinghurtevent(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        Entity holder = event.getSource().getEntity();
        if ( holder instanceof Player player && entity != null &&!(entity instanceof Player)) {
            if (ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.MAINHAND), MomotinkerModifiers.grudgeothers.getId())>0) {
                if (!player.hasItemInSlot(EquipmentSlot.HEAD)){
                    ItemStack a = new ItemStack(entity.getItemBySlot(EquipmentSlot.HEAD).getItem());
                    ModifierUtil.dropItem(holder, a);
                    entity.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                }
                if (!player.hasItemInSlot(EquipmentSlot.CHEST)){
                    ItemStack b = new ItemStack(entity.getItemBySlot(EquipmentSlot.CHEST).getItem());
                    ModifierUtil.dropItem(holder, b);
                    entity.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
                }
                if (!player.hasItemInSlot(EquipmentSlot.LEGS)){
                    ItemStack c = new ItemStack(entity.getItemBySlot(EquipmentSlot.LEGS).getItem());
                    ModifierUtil.dropItem(holder, c);
                    entity.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
                }
                if (!player.hasItemInSlot(EquipmentSlot.FEET)){
                    ItemStack d = new ItemStack(entity.getItemBySlot(EquipmentSlot.FEET).getItem());
                    ModifierUtil.dropItem(holder, d);
                    entity.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
                }
            }
        }
    }

    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity target = context.getLivingTarget();
        LivingEntity attacker =context.getAttacker();
        if (attacker instanceof Player player&&target != null && modifier.getLevel() > 0) {
            float a = modifier.getLevel();
            float b = target.getMaxHealth() - attacker.getMaxHealth();
            if (b > 0 && b < a*1000){
                return damage + b*a/10;
            }
            if (b > a*1000){
                return damage + a*1000*a/10;
            }
            if (b < 0 && b > -damage){
                return damage + b*a/20;
            }
            if (b < -damage){
                return damage * 0;
            }
        }
        return damage;
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker, @javax.annotation.Nullable LivingEntity target) {
        if (target != null && modifier.getLevel() > 0) {
            if (attacker instanceof Player player && projectile instanceof AbstractArrow arrow ) {
                float a = modifier.getLevel();
                float b = target.getMaxHealth() - attacker.getMaxHealth();
                if (b > 0 && b < a*1000){
                    arrow.setBaseDamage(arrow.getBaseDamage() + b*a/10);
                }
                if (b > a*1000){
                    arrow.setBaseDamage(arrow.getBaseDamage() + a*1000*a/10);
                }
                if (b < 0 && b > -arrow.getBaseDamage()){
                    arrow.setBaseDamage(arrow.getBaseDamage() + b*a/20);
                }
                if (b < -arrow.getBaseDamage()){
                    arrow.setBaseDamage(arrow.getBaseDamage() * 0);
                }
            }
        }
        return false;
    }
}