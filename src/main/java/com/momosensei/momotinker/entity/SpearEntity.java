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


public class SpearEntity extends Projectile {
    public final ItemStack Spear;
    public ToolStack tool;
    public float damage=0;

    public SpearEntity(EntityType<? extends Projectile> p_37248_, Level p_37249_, ItemStack spear) {
        super(p_37248_, p_37249_);
        this.Spear = spear;
    }
    public ItemStack getSpear(){
        return this.Spear;
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
        super.tick();
        if (this.tickCount >= 200) {
            this.discard();
            return;
        }
        Entity entity =this.getOwner();
        if (entity==null){
            return;
        }
        Vec3 movement =this.getDeltaMovement();
        this.setPos(movement.x+this.getX(),movement.y+this.getY(),movement.z+this.getZ());
        double angle =((this.tickCount * 100 % 360)*Math.PI)/180;
        Vec3 anglevec =new Vec3(Math.sin(angle),0,Math.cos(angle)).scale(2);
        if (getMold(movement)<=2){
            this.setDeltaMovement(movement.scale(5));
        }
        if (entity instanceof Player player) {
            AABB aabb =new AABB(this.getX()+anglevec.x,this.getY(),this.getZ()+anglevec.z,this.getX(),this.getY(),this.getZ()).inflate(1.75);
            List<Entity> ls0 = this.level.getEntitiesOfClass(Entity.class, aabb);
            for (Entity targets : ls0) {
                if (targets!=getOwner()) {
                    attackUtil.attackEntity(this.tool, player, InteractionHand.MAIN_HAND, targets, ()->1, true, Util.getSlotType(InteractionHand.MAIN_HAND), this.damage, 1f,false, true, false);
                }
            }
        }
    }
}
