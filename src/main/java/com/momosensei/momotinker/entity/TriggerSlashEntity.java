package com.momosensei.momotinker.entity;


import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;


public class TriggerSlashEntity extends Projectile {
    public final ItemStack Slash;
    public Vec3 offset =new Vec3(0,0,0);
    public ToolStack tool;
    public float damage=0;
    public float angle ;

    public TriggerSlashEntity(EntityType<? extends Projectile> p_37248_, Level p_37249_, ItemStack slash) {
        super(p_37248_, p_37249_);
        this.Slash = slash;
        this.angle = 10;
    }
    public ItemStack getSlash(){
        return this.Slash;
    }
    public static double getMold(Vec3 vec3){
        if(vec3!=null){
            return Math.pow(Math.pow(vec3.x, 2)+Math.pow(vec3.y, 2)+Math.pow(vec3.z, 2),0.5);
        }
        return 0;
    }
    public void setToolstack(ToolStack tool){
        this.tool =tool;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        if (this.tool==null&&this.getOwner() instanceof Player player){
            this.tool=ToolStack.from(player.getMainHandItem());
        }
        Vec3 rayVec3 =this.getDeltaMovement();
        super.tick();
        if (this.tickCount>=2){
            this.discard();
            return;
        }
        Entity entity =this.getOwner();
        if (entity==null){
            return;
        }
        if (entity instanceof Player player) {
            Vec3 vec3 = new Vec3(rayVec3.x, rayVec3.y, rayVec3.z);
            double dy = vec3.y +offset.y+0.5;
            AABB aabb = this.getBoundingBox().expandTowards(vec3.scale(2)).expandTowards(vec3.scale(-1)).expandTowards(new Vec3(0,dy,0).cross(vec3)).expandTowards(new Vec3(0,-dy,0).cross(vec3));
            List<Entity> ls0 = this.level.getEntitiesOfClass(Entity.class, aabb);
            for (Entity targets : ls0) {
                targets.invulnerableTime = 0;
                targets.hurt(DamageSource.playerAttack(player),damage);
                targets.invulnerableTime = 0;
            }
        }
    }
}
