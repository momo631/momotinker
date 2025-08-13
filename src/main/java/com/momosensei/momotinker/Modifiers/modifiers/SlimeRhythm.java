package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.*;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;

import javax.annotation.Nonnull;

import static slimeknights.tconstruct.common.TinkerTags.Modifiers.OVERSLIME_FRIEND;


public class SlimeRhythm extends momomodifier {
    public SlimeRhythm() {

    }

    public static final ResourceLocation rhythmobbsa = Momotinker.getResource("rhythmobbsa");
    public static final ResourceLocation rhythmobbsb = Momotinker.getResource("rhythmobbsb");

    public static final ResourceLocation rhythmpoints = Momotinker.getResource("rhythmpoints");

    @Override
    public Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(rhythmobbsa);
        iToolStackView.getPersistentData().remove(rhythmobbsb);
        iToolStackView.getPersistentData().remove(rhythmpoints);
        return null;
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        ModDataNBT a = tool.getPersistentData();
        a.putInt(rhythmobbsa,a.getInt(rhythmobbsa)+1);
        if (a.getInt(rhythmobbsa)<0){
            a.putInt(rhythmobbsa,0);
        }
        if (a.getInt(rhythmobbsb)<0){
            a.putInt(rhythmobbsb,0);
        }
        if (a.getInt(rhythmpoints)<0){
            a.putInt(rhythmpoints,0);
        }
    }
    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        ModDataNBT a = tool.getPersistentData();
        OverslimeModifier overslime = TinkerModifiers.overslime.get();
        ModifierEntry entry = tool.getModifier(TinkerModifiers.overslime.getId());
        if (a.getInt(rhythmobbsa)!=0){
            int b=Math.abs(a.getInt(rhythmobbsa)-a.getInt(rhythmobbsb));
            if (b<=6) {
                a.putInt(rhythmpoints,a.getInt(rhythmpoints)+1);
                if (entry.getLevel() > 0 && overslime.getShield(tool) < overslime.getShieldCapacity(tool, entry)) {
                    overslime.addOverslime(tool, entry, (int) (1+Math.floor(modifier.getLevel()*0.5f+0.25f*a.getInt(rhythmpoints))));
                }
            }else {
                a.putInt(rhythmpoints,0);
            }
            a.putInt(rhythmobbsb, a.getInt(rhythmobbsa));
            a.putInt(rhythmobbsa, 0);
        }
        if (tool.getPersistentData().getInt(rhythmpoints) >= 24) {
            if (context.getAttacker().level instanceof ServerLevel level){
                level.sendParticles(ParticleTypes.ITEM_SLIME, context.getAttacker().getX(), context.getAttacker().getY()+context.getAttacker().getBbHeight()*0.6f, context.getAttacker().getZ(), 8, 0.5, 0.5, 0.5, 2);
            }
            for (ModifierEntry entry1 : tool.getModifierList()) {
                if (entry1.getModifier() != modifier.getModifier()&&entry1.matches(OVERSLIME_FRIEND)) {
                    damage = entry1.getHook(ModifierHooks.MELEE_DAMAGE).getMeleeDamage(tool, modifier, context, baseDamage, damage);
                }
            }
        }
        return damage;
    }
    @Override
    public void modifierDamageDealt(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, LivingEntity entity, DamageSource damageSource, float amount, boolean isDirectDamage) {
        if (tool.getPersistentData().getInt(rhythmpoints) >= 24) {
            for (ModifierEntry entry : tool.getModifierList()) {
                if (entry.getModifier() != modifier.getModifier()&&entry.matches(OVERSLIME_FRIEND)) {
                    entry.getHook(ModifierHooks.DAMAGE_DEALT).onDamageDealt(tool, modifier, context, slotType, entity, damageSource, amount * 0.25f, isDirectDamage);
                }
            }
        }
    }
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (tool.getPersistentData().getInt(rhythmpoints) >= 24) {
            for (ModifierEntry entry : tool.getModifierList()) {
                if (entry.getModifier() != modifier.getModifier()&&entry.matches(OVERSLIME_FRIEND)) {
                    entry.getHook(ModifierHooks.MELEE_HIT).afterMeleeHit(tool, modifier, context, damageDealt*0.25f);
                }
            }
        }
    }
    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        if (tool.getPersistentData().getInt(rhythmpoints) >= 24) {
            for (ModifierEntry entry : tool.getModifierList()) {
                if (entry.getModifier() != modifier.getModifier()&&entry.matches(OVERSLIME_FRIEND)) {
                    knockback = entry.getHook(ModifierHooks.MELEE_HIT).beforeMeleeHit(tool, modifier, context, damage, baseKnockback, knockback);
                }
            }
        }
        return knockback;
    }
    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker, @javax.annotation.Nullable LivingEntity target) {
        if (attacker !=null && projectile instanceof AbstractArrow arrow&&target != null){
            ToolStack tool = ToolStack.from( attacker.getMainHandItem());
            ModDataNBT a = tool.getPersistentData();
            if (a.getInt(rhythmpoints)>=24){
                if (attacker.level instanceof ServerLevel level){
                    level.sendParticles(ParticleTypes.ITEM_SLIME, attacker.getX(), attacker.getY()+attacker.getBbHeight()*0.6f, attacker.getZ(), 8, 0.5, 0.5, 0.5, 2);
                }
                boolean ret = false;
                for (ModifierEntry entry1 : tool.getModifierList()) {
                    if (entry1.getModifier() != modifier.getModifier()&&entry1.matches(OVERSLIME_FRIEND)) {
                        ret |=entry1.getHook(ModifierHooks.PROJECTILE_HIT).onProjectileHitEntity(modifiers, persistentData, modifier, projectile, hit, attacker, target);
                    }
                }
                return ret;
            }
        }
        return false;
    }
    @Override
    public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, Projectile projectile, @javax.annotation.Nullable AbstractArrow arrow, NamespacedNBT persistentData, boolean primary) {
        if ( projectile instanceof AbstractArrow) {
            ModDataNBT a = tool.getPersistentData();
            OverslimeModifier overslime = TinkerModifiers.overslime.get();
            ModifierEntry entry = tool.getModifier(TinkerModifiers.overslime.getId());
            if (a.getInt(rhythmobbsa) != 0) {
                int b = Math.abs(a.getInt(rhythmobbsa) - a.getInt(rhythmobbsb));
                if (b <= 6) {
                    a.putInt(rhythmpoints, a.getInt(rhythmpoints) + 1);
                    if (entry.getLevel() > 0 && overslime.getShield(tool) < overslime.getShieldCapacity(tool, entry)) {
                        overslime.addOverslime(tool, entry, (int) (1 + Math.floor(modifier.getLevel() * 0.5f + 0.25f * a.getInt(rhythmpoints))));
                    }
                } else {
                    a.putInt(rhythmpoints, 0);
                }
                a.putInt(rhythmobbsb, a.getInt(rhythmobbsa));
                a.putInt(rhythmobbsa, 0);
            }
            if (a.getInt(rhythmpoints)>=24){
                for (ModifierEntry entry1 : tool.getModifierList()) {
                    if (entry1.getModifier() != modifier.getModifier()&&entry1.matches(OVERSLIME_FRIEND)) {
                        entry1.getHook(ModifierHooks.PROJECTILE_LAUNCH).onProjectileLaunch(tool, modifier, shooter, projectile, arrow, persistentData, primary);
                    }
                }
            }
        }
    }
}