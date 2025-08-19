package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nonnull;


public class GrudgeOthers extends momomodifier {
    public GrudgeOthers() {
        MinecraftForge.EVENT_BUS.addListener(this::onEntityDeath);
    }

    @Override
    public void OnLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        Entity holder = event.getSource().getEntity();
        if (holder instanceof Player player && entity instanceof Mob mob) {
            if (ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.MAINHAND), MomotinkerModifiers.grudgeothers.getId())>0) {
                if (!player.hasItemInSlot(EquipmentSlot.HEAD)){
                    //mob.setDropChance(EquipmentSlot.HEAD, 1);
                    ModifierUtil.dropItem(event.getEntity(), ToolStack.copyFrom(mob.getItemBySlot(EquipmentSlot.HEAD)).createStack());
                    mob.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                }
                if (!player.hasItemInSlot(EquipmentSlot.CHEST)){
                    ModifierUtil.dropItem(event.getEntity(), ToolStack.copyFrom(mob.getItemBySlot(EquipmentSlot.CHEST)).createStack());
                    mob.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
                }
                if (!player.hasItemInSlot(EquipmentSlot.LEGS)){
                    ModifierUtil.dropItem(event.getEntity(), ToolStack.copyFrom(mob.getItemBySlot(EquipmentSlot.LEGS)).createStack());
                    mob.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
                }
                if (!player.hasItemInSlot(EquipmentSlot.FEET)){
                    ModifierUtil.dropItem(event.getEntity(), ToolStack.copyFrom(mob.getItemBySlot(EquipmentSlot.FEET)).createStack());
                    mob.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
                }
            }
        }
    }
    private void onEntityDeath(LivingDeathEvent event) {
        LivingEntity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player && a instanceof Mob mob) {
            if (getMainhandModifierlevel(player,MomotinkerModifiers.grudgeothers.getId())>0) {
                if (!mob.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()){
                    mob.setDropChance(EquipmentSlot.MAINHAND, 1);
                }
                if (!mob.getItemBySlot(EquipmentSlot.OFFHAND).isEmpty()){
                    mob.setDropChance(EquipmentSlot.OFFHAND, 1);
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
            }else
            if (b > a*1000){
                return damage + a*1000*a/10;
            }else
            if (b < 0 && b > -damage){
                return damage + b*a/20;
            }else
            if (b < -damage){
                return 0;
            }
        }
        return damage;
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker, @javax.annotation.Nullable LivingEntity target) {
        if (target != null && modifier.getLevel() > 0) {
            if (attacker instanceof Player player && projectile instanceof AbstractArrow arrow ) {
                float a = modifier.getLevel();
                float b = target.getMaxHealth() - attacker.getMaxHealth();
                if (b > 0 && b < a*1000){
                    arrow.setBaseDamage(arrow.getBaseDamage() + b*a/10);
                }else
                if (b > a*1000){
                    arrow.setBaseDamage(arrow.getBaseDamage() + a*1000*a/10);
                }else
                if (b < 0 && b > -arrow.getBaseDamage()){
                    arrow.setBaseDamage(arrow.getBaseDamage() + b*a/20);
                }else
                if (b < -arrow.getBaseDamage()){
                    arrow.setBaseDamage(1);
                }
            }
        }
        return false;
    }
}