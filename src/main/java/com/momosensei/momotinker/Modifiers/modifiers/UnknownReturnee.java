package com.momosensei.momotinker.Modifiers.modifiers;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

import javax.annotation.Nonnull;

public class UnknownReturnee extends momomodifier {
    public UnknownReturnee() {
    }
    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity target = context.getLivingTarget();
        LivingEntity attacker =context.getAttacker();
        if (attacker instanceof Player player&&target != null&& modifier.getLevel() > 0) {
            float a = modifier.getLevel();
            int b = RANDOM.nextInt(100);
            if (b>10){
                return damage * (1F + b*0.01F);
            }
            if (b<10){
                return damage * (1F + 10*0.01F);
            }
            return damage * (1F - a*0.2F);
        }
        return damage;
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker, @javax.annotation.Nullable LivingEntity target) {
        if (target != null && modifier.getLevel() > 0) {
            if (attacker instanceof Player player && projectile instanceof AbstractArrow arrow ) {
                float a = modifier.getLevel();
                int b = RANDOM.nextInt(100);
                if (b>10){
                    arrow.setBaseDamage(arrow.getBaseDamage() * (1F + b*0.01F));
                }
                if (b<10){
                    arrow.setBaseDamage(arrow.getBaseDamage() * (1F + 10*0.01F));
                }
                arrow.setBaseDamage(arrow.getBaseDamage()*(1F - a*0.2F));
            }
        }
        return false;
    }
}