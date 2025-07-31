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

import static com.momosensei.momotinker.Momotinker.getResource;


public class PullAEntity extends Projectile {
    public ToolStack tool;
    private boolean isPullingBlock = false;
    private boolean isPullingEntity = false;
    public PullAEntity(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
        this.setNoGravity(true);
    }
    public PullAEntity(Level level, double x, double y, double z, Vec3 movement){
        this(MomotinkerEntities.pull_a_entity.get(), level);
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
        if (this.tickCount>200) this.discard();
        for (int dx=-1;dx<=1;dx++){
            for (int dz=-1;dz<=1;dz++){
                if (!level().hasChunk(this.chunkPosition().x+dx,this.chunkPosition().z+dz)){
                    this.discard();
                    return;
                }
            }
        }
        if (!isPullingBlock&&!isPullingEntity){
            if (this.onGround()){
                this.onHit(new BlockHitResult(this.position(), Direction.UP,this.blockPosition().below(),false));
            }
            HitResult hitresult = this.level().clip(new ClipContext(this.position(), this.position().add(this.getDeltaMovement()), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(this.level(),this,this.position(), this.position().add(this.getDeltaMovement()),this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(2),this::canHitEntity);
            if (entityhitresult != null && entityhitresult.getType() != HitResult.Type.MISS) {
                hitresult = entityhitresult;
            }
            if (hitresult.getType()!= HitResult.Type.MISS){
                this.onHit(hitresult);
            }
            Vec3 movement =this.getDeltaMovement();
            this.setPos(movement.x+this.getX(),movement.y+this.getY(),movement.z+this.getZ());
            if (getMold(movement)<=2){
                this.setDeltaMovement(movement.scale(5));
            }
        }
        if (this.getOwner() instanceof Player player) {
            Entity origin = this;
            if (isPullingBlock||isPullingEntity) {
                double pullSpeed = 0.3D;
                if (isPullingEntity) {
                    pullSpeed += 0.1D;
                    if (player.position().subtract(origin.position()).length()<1.75)this.discard();
                }else {
                    if (player.position().subtract(origin.position()).length()<4)this.discard();
                }
                Vec3 distance = origin.position().subtract(player.position().add(0, player.getBbHeight() / 2, 0));
                Vec3 motion = distance.normalize().scale(distance.length() < 50 ? (pullSpeed * distance.length()) / 50 : pullSpeed);
                if (Math.abs(distance.y) < 0.1D)
                    motion = new Vec3(motion.x, 0, motion.z);
                if (new Vec3(distance.x, 0, distance.z).length() < new Vec3(player.getBbWidth() / 2, 0, player.getBbWidth() / 2).length() / 1.4)
                    motion = new Vec3(0, motion.y, 0);
                player.setDeltaMovement(player.getDeltaMovement().add(motion));
                player.hurtMarked = true;
                player.fallDistance=0;
                //if (player.getUseItem().finishUsingItem(this.level,player).is(MomotinkerItem.pneumatic_sword.get()))this.discard();
            }
            if (player.position().subtract(origin.position()).length()>150)this.discard();
            if (player.isShiftKeyDown())this.discard();
            if (player.isDeadOrDying())this.discard();
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);
        this.isPullingBlock = true;
    }
    @Override
    protected void onHitEntity(@NotNull EntityHitResult p_37259_) {
        super.onHitEntity(p_37259_);
        if(!this.level().isClientSide && getOwner() instanceof Player player && p_37259_.getEntity() != player) {
            if((p_37259_.getEntity() instanceof LivingEntity || p_37259_.getEntity() instanceof EnderDragonPart)&& !(p_37259_.getEntity() instanceof PullAEntity)) {
                this.isPullingEntity = true;
                this.setDeltaMovement(this.getDeltaMovement().scale(0));
                this.setPos(p_37259_.getEntity().position().add(0,p_37259_.getEntity().getBbHeight()*0.8f,0));
                if (!this.tool.getPersistentData().getBoolean(getResource("cansnick"))) {
                    this.tool.getPersistentData().putBoolean(getResource("cansnick"), true);
                }
            }
        }
    }

    @Override
    protected void defineSynchedData() {
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        isPullingBlock = tag.getBoolean("isPullingBlock");
        isPullingEntity = tag.getBoolean("isPullingEntity");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isPullingBlock", isPullingBlock);
        tag.putBoolean("isPullingEntity", isPullingEntity);
    }
}
