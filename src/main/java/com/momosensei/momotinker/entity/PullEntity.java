package com.momosensei.momotinker.entity;


import com.momosensei.momotinker.register.MomotinkerEntities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;


public class PullEntity extends Projectile {
    public ToolStack tool;
    private boolean isPulling = false;
    public PullEntity(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
        this.setNoGravity(true);
    }
    public PullEntity(Level level, double x, double y, double z, Vec3 movement){
        this(MomotinkerEntities.pull_entity.get(), level);
        this.setPos(x,y,z);
        this.setDeltaMovement(movement);
        this.setNoGravity(true);
    }
    public void setToolstack(ToolStack tool){
        this.tool =tool;
    }
    public static double getMold(Vec3 vec3){
        if(vec3!=null){
            return Math.pow(Math.pow(vec3.x, 2)+Math.pow(vec3.y, 2)+Math.pow(vec3.z, 2),0.5);
        }
        return 0;
    }
    @Override
    public void tick() {
        this.tickCount++;
        if (this.tickCount>80) this.discard();
        if (this.onGround){
            this.onHit(new BlockHitResult(this.position(), Direction.UP,this.blockPosition().below(),false));
        }
        HitResult hitresult = this.level.clip(new ClipContext(this.position(), this.position().add(this.getDeltaMovement()), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(this.level,this,this.position(), this.position().add(this.getDeltaMovement()),this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(2),this::canHitEntity);
        if (entityhitresult != null && entityhitresult.getType() != HitResult.Type.MISS) {
            hitresult = entityhitresult;
        }
        if (hitresult.getType()!= HitResult.Type.MISS){
            this.onHit(hitresult);
        }
        if (!isPulling){
            Vec3 movement =this.getDeltaMovement();
            this.setPos(movement.x+this.getX(),movement.y+this.getY(),movement.z+this.getZ());
            if (getMold(movement)<=2){
                this.setDeltaMovement(movement.scale(7));
            }
        }
        if (this.getOwner() instanceof Player player&& isPulling) {
            Entity origin = this;
            double brakeZone = 6D;
            double pullSpeed = 2D;
            Vec3 distance = origin.position().subtract(player.position().add(0, player.getBbHeight() / 2, 0));
            Vec3 motion = distance.normalize().scale(distance.length() < brakeZone ? (pullSpeed * distance.length()) / brakeZone : pullSpeed);
            if (Math.abs(distance.y) < 0.1D)
                motion = new Vec3(motion.x, 0, motion.z);
            if (new Vec3(distance.x, 0, distance.z).length() < new Vec3(player.getBbWidth() / 2, 0, player.getBbWidth() / 2).length() / 1.4)
                motion = new Vec3(0, motion.y, 0);
            player.setDeltaMovement(motion);
            player.hurtMarked = true;
            if (origin.position().subtract(player.position()).length()<0.1)this.discard();
            if (player.isShiftKeyDown())this.discard();
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);
        this.isPulling = true;
    }
    @Override
    protected void onHitEntity(@NotNull EntityHitResult p_37259_) {
        super.onHitEntity(p_37259_);
        if(!this.level.isClientSide && getOwner() instanceof Player player && p_37259_.getEntity() != player) {
            if((p_37259_.getEntity() instanceof LivingEntity || p_37259_.getEntity() instanceof EnderDragonPart)) {
                this.isPulling = true;
                this.setPos(p_37259_.getEntity().position());
            }
        }
    }

    @Override
    protected void defineSynchedData() {
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        isPulling = tag.getBoolean("isPulling");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isPulling", isPulling);
    }
}
