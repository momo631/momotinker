package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

import javax.annotation.Nonnull;


public class ThunderDecree extends momomodifier {
    public ThunderDecree() {
    }
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        if (context.getLivingTarget() != null){
            context.getLivingTarget().invulnerableTime=0;
            context.getLivingTarget().hurt(DamageSource.MAGIC,damage*0.6f);
            context.getLivingTarget().invulnerableTime=0;
            return damage*0.6f;
        }
        return damage;
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker, @javax.annotation.Nullable LivingEntity target) {
        if (attacker !=null && projectile instanceof AbstractArrow arrow){
            if (target != null) {
                arrow.setBaseDamage(arrow.getBaseDamage()*0.6f);
                float f = (float) arrow.getDeltaMovement().length();
                float i = (float) Mth.clamp((double) f * arrow.getBaseDamage(), 0.0D, Float.MAX_VALUE);
                target.invulnerableTime=0;
                target.hurt(DamageSource.MAGIC, i);
                target.invulnerableTime=0;
            }
        }
        return false;
    }

}