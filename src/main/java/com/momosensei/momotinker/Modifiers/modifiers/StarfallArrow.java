package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.entity.StarfallEntity;
import com.momosensei.momotinker.event.EntitySpawnEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import static slimeknights.tconstruct.library.tools.stat.ToolStats.ACCURACY;


public class StarfallArrow extends momomodifier {
    public StarfallArrow() {
    }
    @Override
    public boolean isNoLevels() {
        return true;
    }

    public static float getDamageMultiplier(ToolStack tool) {
        float b = RANDOM.nextInt((int) (tool.getStats().get(ACCURACY) * 100));
        return (1F + 0.005F * b + 0.2F * tool.getStats().get(ToolStats.VELOCITY));
    }
    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker, @javax.annotation.Nullable LivingEntity target) {
        if (attacker instanceof ServerPlayer player && projectile instanceof AbstractArrow arrow&&target!=null){
            Level level=player.level;
            ToolStack tool= ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND));
            double x =target.getX();
            double y =target.getY();
            double z =target.getZ();
            EntitySpawnEvent event1 = new EntitySpawnEvent(new Vec3(x, y + 100, z));
            MinecraftForge.EVENT_BUS.post(event1);
            if (!event1.isCanceled()) {
                StarfallEntity entity = new StarfallEntity(level, x, y + 100, z, new Vec3(0,0,0));
                entity.noPhysics = true;
                entity.setOwner(player);
                entity.setToolstack(tool);
                entity.damagemultiplier = getDamageMultiplier(tool)*0.5f;
                entity.damage = (float) (tool.getStats().get(ToolStats.ATTACK_DAMAGE)+arrow.getBaseDamage());
                entity.setExplosionPower((byte)50);
                level.addFreshEntity(entity);
            }
        }
        return false;
    }
}