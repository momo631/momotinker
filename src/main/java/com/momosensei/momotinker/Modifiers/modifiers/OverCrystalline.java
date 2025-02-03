package com.momosensei.momotinker.Modifiers.modifiers;


import com.momosensei.momotinker.Momotinker;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class OverCrystalline extends momomodifier {
    public OverCrystalline() {
    }
    public static final ResourceLocation crystallization = Momotinker.getResource("crystallization");
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(crystallization);
        return null;
    }

    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity attacker =context.getAttacker();
        ModDataNBT c = tool.getPersistentData();
        if (attacker instanceof ServerPlayer player){
            int a = (int)c.getFloat(crystallization);
            int b = (int)1.225^a;
            return damage * b;
        }
        return damage;
    }
    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker, @javax.annotation.Nullable LivingEntity target) {
        if (target != null&&attacker instanceof ServerPlayer player && projectile instanceof AbstractArrow arrow){
            int a = (int)persistentData.getFloat(crystallization);
            int b = (int)1.1^a;
            arrow.setBaseDamage(arrow.getBaseDamage() * b);
        }
        return false;
    }
}