package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import javax.annotation.Nonnull;


public class WardOffEvil extends momomodifier {
    public WardOffEvil() {
    }

    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        if (context.getLivingTarget() != null) {
            if (context.getLivingTarget() instanceof Mob || context.getLivingTarget() instanceof Monster) {
                return damage * (1f + 0.25f * modifier.getLevel());
            }
            if (context.getLivingTarget().getMobType() == MobType.UNDEAD) {
                return damage * (1f + 0.75f * modifier.getLevel());
            }
        }
        return damage;
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker, @javax.annotation.Nullable LivingEntity target) {
        if (attacker !=null && projectile instanceof AbstractArrow arrow){
            if (target != null) {
                if (target instanceof Mob || target instanceof Monster) {
                    arrow.setBaseDamage(arrow.getBaseDamage() * (1f + 0.25f * modifier.getLevel()));
                }
                if (target.getMobType() == MobType.UNDEAD) {
                    arrow.setBaseDamage(arrow.getBaseDamage() * (1f + 0.75f * modifier.getLevel()));
                }
            }
        }
        return false;
    }
}