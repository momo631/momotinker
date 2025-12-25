package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
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


public class SuppressingEvil extends momomodifier {
    public SuppressingEvil() {
    }

    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        if (context.getLivingTarget() != null && context.getLivingTarget().getMobType() == MobType.UNDEAD){
            return damage*(1f+0.25f*modifier.getLevel());
        }
        return damage;
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target, boolean notBlocked) {
        if (attacker !=null && projectile instanceof AbstractArrow arrow){
            if (target != null&& target.getMobType() == MobType.UNDEAD) {
                arrow.setBaseDamage(arrow.getBaseDamage()*(1f+0.25f*modifier.getLevel()));
            }
        }
        return false;
    }
}