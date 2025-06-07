package com.momosensei.momotinker.entity;

import com.momosensei.momotinker.register.MomotinkerBlock;
import com.momosensei.momotinker.register.MomotinkerConfig;
import com.momosensei.momotinker.register.MomotinkerEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MeteorEntity extends Projectile {
    private static final EntityDataAccessor<Byte> EXPLOSION_POWER = SynchedEntityData.defineId(MeteorEntity.class, EntityDataSerializers.BYTE);
    public MeteorEntity(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }
    public MeteorEntity(Level level, double x, double y, double z, Vec3 movement){
        this(MomotinkerEntities.meteor_entity.get(), level);
        this.setPos(x,y,z);
        this.setDeltaMovement(movement);
    }

    @Override
    public void tick() {
        super.tick();
        this.tickCount++;
        if (this.tickCount>1200) this.discard();
        if (this.getDeltaMovement().length() > 1) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
        }
        this.setDeltaMovement(this.getDeltaMovement().add(0, -0.25, 0));
        HitResult hitresult = this.level.clip(new ClipContext(this.position(), this.position().add(this.getDeltaMovement()), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(this.level,this,this.position(), this.position().add(this.getDeltaMovement()),this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1),this::canHitEntity);
        super.move(MoverType.SELF,this.getDeltaMovement());
        if (entityhitresult != null && entityhitresult.getType() != HitResult.Type.MISS) {
            hitresult = entityhitresult;
        }
        if (this.level instanceof ServerLevel serverLevel){
            serverLevel.sendParticles(ParticleTypes.FLAME, this.getX(), this.getY()-1.5, this.getZ(), 2, 0, 0, 0, 6);
        }
        if (hitresult.getType()!= HitResult.Type.MISS){
            this.onHit(hitresult);
        }
        if (this.onGround){
            this.onHit(new BlockHitResult(this.position(), Direction.UP,this.blockPosition().below(),false));
        }
    }

    public void meteorExplode(){
        boolean config = MomotinkerConfig.explosion_destroys_limit.get();
        if (!this.level.isClientSide) {
            Explosion.BlockInteraction blockInteraction;
            if (config) {
                blockInteraction=Explosion.BlockInteraction.DESTROY;
            }else {
                blockInteraction=Explosion.BlockInteraction.NONE;
            }
            Explosion explosion =this.level.explode(this, this.getX(), this.getY(), this.getZ(), this.getEntityData().get(EXPLOSION_POWER) * 0.1f, true, blockInteraction);
            List<Player> players = explosion.getHitPlayers().keySet().stream().toList();
            BlockPos blockPos= BlockPos.of(BlockPos.asLong((int) this.getX(), (int) this.getY(), (int) this.getZ()));
            if ((this.level.getBlockState(blockPos).isAir() || this.level.getBlockState(blockPos).is(Blocks.FIRE) || !(this.level.getFluidState(blockPos).is(Fluids.EMPTY)))) {
                this.level.setBlockAndUpdate(blockPos, MomotinkerBlock.meteor_nucleus_block.get().defaultBlockState());
            }
            for (Player player:players){
                if (player!=null){
                    player.invulnerableTime =0;
                    player.hurt(DamageSource.explosion(explosion),120);
                }
            }
        }
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);
        meteorExplode();
    }
    @Override
    protected void onHitEntity(EntityHitResult p_37259_) {
        super.onHitEntity(p_37259_);
        meteorExplode();
    }
    @Override
    protected void defineSynchedData() {
        this.entityData.define(EXPLOSION_POWER,(byte)0);
    }

    public void setExplosionPower(byte power){
        this.entityData.set(EXPLOSION_POWER,power);
    }
}
