package com.momosensei.momotinker.Modifiers.modifiers;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.momosensei.momotinker.Modifiers.momomodifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


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
            context.getLivingTarget().hurt(LegacyDamageSource.directMagic(context.getAttacker().level()),damage*0.6f);
            context.getLivingTarget().invulnerableTime=0;
            return damage*0.6f;
        }
        return damage;
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target, boolean notBlocked) {
        if (attacker !=null && projectile instanceof AbstractArrow arrow){
            if (target != null) {
                arrow.setBaseDamage(arrow.getBaseDamage()*0.6f);
                float f = (float) arrow.getDeltaMovement().length();
                float i = (float) Mth.clamp((double) f * arrow.getBaseDamage(), 0.0D, Float.MAX_VALUE);
                target.invulnerableTime=0;
                target.hurt(LegacyDamageSource.directMagic(attacker.level()), i);
                target.invulnerableTime=0;
            }
        }
        return false;
    }

}