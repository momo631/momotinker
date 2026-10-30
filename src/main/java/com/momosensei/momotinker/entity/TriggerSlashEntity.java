package com.momosensei.momotinker.entity;


import com.momosensei.momotinker.util.AttackUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.utils.Util;

import java.util.List;
import java.util.Random;


public class TriggerSlashEntity extends Projectile {
    public ToolStack tool;
    public float damage=0;
    public int yamatoLevel=0;
    public float damagemultiplier=0;
    public float angle ;
    public float range = 0f;

    public TriggerSlashEntity(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
        this.angle = new Random().nextFloat(20,60);
    }

    public void setToolstack(ToolStack tool){
        this.tool =tool;
    }
    public void setYamatoLevel(int yamatoLevel){
        this.yamatoLevel = yamatoLevel;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        if (this.tool == null && this.getOwner() instanceof Player player) {
            this.tool = ToolStack.from(player.getMainHandItem());
        }
        super.tick();
        if (yamatoLevel == 0) {
            if (this.tickCount >= 2) { this.discard(); return; }
        } else if (yamatoLevel > 0) {
            if (this.tickCount >= 7) { this.discard(); return; }
        }
        Entity owner = this.getOwner();
        if (!(owner instanceof Player player)) return;

        Vec3 fwd = this.getDeltaMovement();
        if (fwd.lengthSqr() < 1e-6) return;
        fwd = fwd.normalize();

        AABB aabb = buildSlashAABB(player, fwd);

        List<Entity> ls0 = this.level().getEntitiesOfClass(Entity.class, aabb);
        for (Entity targets : ls0) {
            if (targets != getOwner()) {
                targets.invulnerableTime = 0;
                AttackUtil.attackEntity(this.tool, player, InteractionHand.MAIN_HAND, targets, () -> 1, true, Util.getSlotType(InteractionHand.MAIN_HAND), this.damage, this.damagemultiplier, false, true, true, true);
                targets.invulnerableTime = 0;
            }
        }
    }

    private AABB buildSlashAABB(Player player, Vec3 fwd) {
        float bbW = player.getBbWidth();
        float bbH = player.getBbHeight();

        float fwdExtent  = 0.9f * range + bbW * 3.5f;
        float sideExtent = bbW*0.8f + 0.8f * range;
        float upExtent   = bbH*0.4f + 0.6f * range;

        Vec3 center = this.position().add(fwd.scale(fwdExtent * 0.5f + bbW*1.2f));

        return buildAlignedAABB(center, fwd, fwdExtent * 0.5f, sideExtent, upExtent);
    }

    private static AABB buildAlignedAABB(Vec3 center, Vec3 fwd, float halfLen, float halfWidth, float halfHeight) {
        Vec3 worldUp = new Vec3(0, 1, 0);
        Vec3 right = fwd.cross(worldUp);
        if (right.lengthSqr() < 1e-6) right = new Vec3(1, 0, 0);
        right = right.normalize();
        Vec3 up = right.cross(fwd).normalize();

        double ex = Math.abs(fwd.x) * halfLen + Math.abs(right.x) * halfWidth + Math.abs(up.x) * halfHeight;
        double ey = Math.abs(fwd.y) * halfLen + Math.abs(right.y) * halfWidth + Math.abs(up.y) * halfHeight;
        double ez = Math.abs(fwd.z) * halfLen + Math.abs(right.z) * halfWidth + Math.abs(up.z) * halfHeight;

        return new AABB(
                center.x - ex, center.y - ey, center.z - ez,
                center.x + ex, center.y + ey, center.z + ez
        );
    }
}
