package com.momosensei.momotinker.entity;


import com.momosensei.momotinker.util.attackUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.utils.Util;

import java.util.List;


public class BurningEntity extends Projectile {
    public final ItemStack Burning;
    public Vec3 offset =new Vec3(0,0,0);
    public ToolStack tool;
    public float damage=0;
    public double SCALE =Math.max(0.1, getMold(this.getDeltaMovement()));

    public BurningEntity(EntityType<? extends Projectile> p_37248_, Level p_37249_, ItemStack burning) {
        super(p_37248_, p_37249_);
        this.Burning = burning;
    }
    public ItemStack getBurning(){
        return this.Burning;
    }
    public static double getMold(Vec3 vec3){
        if(vec3!=null){
            return Math.pow(Math.pow(vec3.x, 2)+Math.pow(vec3.y, 2)+Math.pow(vec3.z, 2),0.05);
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
        if (this.tickCount >= 21) {
                this.discard();
                return;
        }
        Entity entity =this.getOwner();
        if (entity==null){
            return;
        }
        if (entity instanceof Player player) {
            Vec3 vec3 = new Vec3(rayVec3.x, rayVec3.y, rayVec3.z);
            double x = player.getX();
            double y = player.getY() + 0.5 * player.getBbHeight();
            double z = player.getZ();
            double dx = vec3.x * SCALE+offset.x;
            double dy = vec3.y * SCALE+offset.y;
            double dz = vec3.z * SCALE+offset.z;
            this.setPos(x + dx, y + dy, z + dz+3);
            AABB aabb = this.getBoundingBox().expandTowards(vec3.scale(2)).expandTowards(vec3.scale(-1)).expandTowards(new Vec3(0,dy,0).cross(vec3)).expandTowards(new Vec3(0,-dy,0).cross(vec3));
            List<Entity> ls0 = this.level.getEntitiesOfClass(Entity.class, aabb);
            for (Entity targets : ls0) {
                if (targets!=getOwner()) {
                    targets.invulnerableTime = 0;
                    attackUtil.attackEntity(this.tool, player, InteractionHand.MAIN_HAND, targets, ()->1, true, Util.getSlotType(InteractionHand.MAIN_HAND), this.damage, false, true, true, true);
                    targets.invulnerableTime = 0;
                }
            }
        }
    }
}
