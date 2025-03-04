package com.momosensei.momotinker.entity;


import com.momosensei.momotinker.util.attackUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
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

import java.util.ArrayList;
import java.util.List;


public class RayEntity extends Projectile {
    public Vec3 rayVec3 =new Vec3(0,0,0);
    public float damage =0;
    public int time =0;
    public ToolStack tool =null;
    public float scale =1;
    public List<AABB> aabbList =new ArrayList<>(List.of());

    public RayEntity(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }

    @Override
    protected void defineSynchedData() {
    }
    @Override
    public boolean isNoGravity() {
        return true;
    }
    public static double getMold(Vec3 vec3){
        if(vec3!=null){
            return Math.pow(Math.pow(vec3.x, 2)+Math.pow(vec3.y, 2)+Math.pow(vec3.z, 2),0.5);
        }
        return 0;
    }
    public static Vec3 getUnitizedVec3(Vec3 vec3){
        if(getMold(vec3)!=0){
            double i = getMold(vec3);
            return new Vec3(vec3.x/i, vec3.y/i, vec3.z/i);
        }
        return new Vec3(0,0,0);
    }
    @Override
    public void tick() {
        time++;
        Level world = this.level;
        if (time >=6) {
            this.discard();
        }
        if (time >= 1) {
            if (time == 1) {
                int range = (int) getMold(rayVec3);
                Vec3 vec3 = getUnitizedVec3(rayVec3);
                Vec3 pos = new Vec3(this.getX(), this.getY(), this.getZ());
                if (range > 0) {
                    for (int i = 0;i < range;i ++) {
                        double x = pos.x + i * vec3.x;
                        double y = pos.y + 0.5 * this.getBbHeight() + i * vec3.y;
                        double z = pos.z + i * vec3.z;
                        ((ServerLevel) world).sendParticles(ParticleTypes.FLAME, x, y, z, 8, 0, 0, 0, 0.02);
                        AABB aabb = new AABB(x + 1.25 * scale, y + 0.5 + 1.25 * scale, z + 1.25 * scale, x - 1.25 * scale, y - 1.25 * scale + 0.5, z - 1.25 * scale);
                        aabbList.add(aabb);
                    }
                }
            }
            List<Entity> ls1 = new ArrayList<>(List.of());
            if (!aabbList.isEmpty()) {
                for (AABB aabb : aabbList) {
                    List<Entity> ls0 = this.level.getEntitiesOfClass(Entity.class, aabb.inflate(-0.7));
                    for (Entity target : ls0) {
                        if (target != null && !(target instanceof Player) && target != this.getOwner()&& this.getOwner() instanceof Player player&& !ls1.contains(target)) {
                            target.invulnerableTime = 0;
                            attackUtil.attackEntity(this.tool, player, InteractionHand.MAIN_HAND, target, () -> 1, true, Util.getSlotType(InteractionHand.MAIN_HAND), this.damage, false, true, true, true);
                            target.invulnerableTime = 0;
                            ls1.add(target);
                        }
                    }
                }
            }
        }
    }
}
