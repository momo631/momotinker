package com.momosensei.momotinker.entity;

import com.momosensei.momotinker.register.MomotinkerEntities;
import com.momosensei.momotinker.util.attackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
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
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.utils.Util;

import java.util.List;

public class CleanseEntity extends Projectile {
    public ToolStack tool;
    public float damage=0;
    public int a = 0;
    private static final EntityDataAccessor<Byte> EXPLOSION_POWER = SynchedEntityData.defineId(CleanseEntity.class, EntityDataSerializers.BYTE);
    public CleanseEntity(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }
    public CleanseEntity(Level level, double x, double y, double z, Vec3 movement){
        this(MomotinkerEntities.cleanse_entity.get(), level);
        this.setPos(x,y,z);
        this.setDeltaMovement(movement);
        this.noPhysics = true;
    }

    @Override
    public void tick() {
        if (this.tool==null&&this.getOwner() instanceof Player player){
            this.tool=ToolStack.from(player.getMainHandItem());
        }
        Entity entity =this.getOwner();
        if (entity==null){
            return;
        }
        super.tick();
        this.tickCount++;
        if (this.tickCount>1200) this.discard();
        if (this.getDeltaMovement().length() > 1) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
        }
        if (a==0) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, -0.01, 0));
        }else if (a>0){
            this.setDeltaMovement(this.getDeltaMovement().add(0, -0.3, 0));
        }
        HitResult hitresult = this.level().clip(new ClipContext(this.position(), this.position().add(this.getDeltaMovement()), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(this.level(),this,this.position(), this.position().add(this.getDeltaMovement()),this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(10),this::canHitEntity);
        super.move(MoverType.SELF,this.getDeltaMovement());
        if (entityhitresult != null && entityhitresult.getType() != HitResult.Type.MISS) {
            hitresult = entityhitresult;
        }
        if (this.level() instanceof ServerLevel serverLevel&&tickCount%10==0){
            for (int i = 0; i <= 360; i++) {
                double rad = i * 0.017453292519943295;
                double r = 15D;
                double x = r * Math.cos(rad);
                double z = r * Math.sin(rad);
                if (a == 0) {
                    serverLevel.sendParticles(ParticleTypes.FLAME, this.getX(), this.getY() - 23, this.getZ(), 9 / 10, x, r, z, 0.1);
                } else if (a > 0) {
                    serverLevel.sendParticles(ParticleTypes.FLAME, this.getX(), this.getY() - 23, this.getZ(), 9 / 10, x, r, z, 0.3);
                }
            }
        }
        if (hitresult.getType()!= HitResult.Type.MISS){
            this.onHit(hitresult);
        }
        if (entity instanceof Player player) {
            if (a==0) {
                List<Mob> lis = this.level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(10));
                for (Mob mob : lis) {
                    if (mob != null ) {
                        mob.invulnerableTime = 0;
                        attackUtil.attackEntity(this.tool, player, InteractionHand.MAIN_HAND, mob, () -> 1, true, Util.getSlotType(InteractionHand.MAIN_HAND), this.damage,0.5f ,false, true, true,true);
                        mob.invulnerableTime = 0;
                    }
                }
            }else if (a>0){
                List<Mob> lis = this.level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(7));
                for (Mob mob : lis) {
                    if (mob != null ) {
                        mob.invulnerableTime = 0;
                        attackUtil.attackEntity(this.tool, player, InteractionHand.MAIN_HAND, mob, () -> 1, true, Util.getSlotType(InteractionHand.MAIN_HAND), this.damage, 0.6f ,false, true, true,true);
                        mob.invulnerableTime = 0;
                    }
                }
            }
        }
        if (this.onGround()){
            this.onHit(new BlockHitResult(this.position(), Direction.UP,this.blockPosition().below(),false));
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult p_37259_) {
        super.onHitEntity(p_37259_);
        if (a==0) {
            meteorExplode();
        }
    }

    public void setToolstack(ToolStack tool){
        this.tool =tool;
    }

    public void setint(int a){
        this.a =a;
    }


    public void meteorExplode(){
        if (!this.level().isClientSide) {
            if (this.tool==null&&this.getOwner() instanceof Player player){
                this.tool=ToolStack.from(player.getMainHandItem());
            }
            Explosion explosion =this.level().explode(this, this.getX(), this.getY(), this.getZ(), this.getEntityData().get(EXPLOSION_POWER) * 0.1f, true, Level.ExplosionInteraction.BLOCK);
            List<BlockPos> list = explosion.getToBlow();
            List<Player> players = explosion.getHitPlayers().keySet().stream().toList();
            boolean generatedPress =false;
            for (BlockPos blockPos : list) {
                if ((this.level().getBlockState(blockPos).isAir() || this.level().getBlockState(blockPos).is(Blocks.FIRE) || !(this.level().getFluidState(blockPos).is(Fluids.EMPTY))) && this.level().getBlockState(blockPos.below()).isCollisionShapeFullBlock(this.level(), blockPos)) {
                    if (!generatedPress) {
                        //this.level.setBlockAndUpdate(blockPos, Blocks.MAGMA_BLOCK.defaultBlockState());
                        generatedPress = true;
                    }
                }
            }
            for (Player player:players){
                if (player!=null){
                    player.invulnerableTime =0;
                    player.hurt(player.level().damageSources().explosion(explosion),1);
                }
            }
            List<Mob> lis = this.level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(22));
            for (Mob mob : lis) {
                if (mob != null&&this.getOwner() instanceof Player player) {
                    mob.invulnerableTime = 0;
                    attackUtil.attackEntity(this.tool, player, InteractionHand.MAIN_HAND, mob, ()->1, true, Util.getSlotType(InteractionHand.MAIN_HAND), this.damage, 20f,false, true, true,false);
                    mob.invulnerableTime = 0;
                }
            }
        }
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);
        if (a==0) {
            meteorExplode();
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(EXPLOSION_POWER,(byte)0);
    }

    public void setExplosionPower(byte power){
        this.entityData.set(EXPLOSION_POWER,power);
    }
}
