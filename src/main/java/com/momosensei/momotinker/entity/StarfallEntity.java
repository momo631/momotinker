package com.momosensei.momotinker.entity;

import com.momosensei.momotinker.register.MomotinkerConfig;
import com.momosensei.momotinker.register.MomotinkerEntities;
import com.momosensei.momotinker.util.AttackUtil;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.utils.Util;

import java.util.List;

public class StarfallEntity extends Projectile {
    public ToolStack tool;
    public float damage=0;
    public float damagemultiplier=0;
    public StarfallEntity(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }
    public StarfallEntity(Level level, double x, double y, double z, Vec3 movement){
        this(MomotinkerEntities.starfall_entity.get(), level);
        this.setPos(x,y,z);
        this.setDeltaMovement(movement);
    }

    @Override
    public void tick() {
        super.tick();
        this.tickCount++;
        if (this.tickCount>1200) this.discard();
        this.setDeltaMovement(this.getDeltaMovement().add(0, -0.35, 0));
        HitResult hitresult = this.level().clip(new ClipContext(this.position(), this.position().add(this.getDeltaMovement()), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(this.level(),this,this.position(), this.position().add(this.getDeltaMovement()),this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(2),this::canHitEntity);
        super.move(MoverType.SELF,this.getDeltaMovement());
        if (entityhitresult != null && entityhitresult.getType() != HitResult.Type.MISS) {
            hitresult = entityhitresult;
        }
        if (this.level() instanceof ServerLevel serverLevel){
            serverLevel.sendParticles(ParticleTypes.FLAME, this.getX(), this.getY()-1.5, this.getZ(), 2, 0, 0,0, 6);
        }
        if (hitresult.getType()!= HitResult.Type.MISS){
            this.onHit(hitresult);
        }
        if (this.onGround()){
            this.onHit(new BlockHitResult(this.position(), Direction.UP,this.blockPosition().below(),false));
        }
    }

    public void setToolstack(ToolStack tool){
        this.tool =tool;
    }
    public void Explode(){
        boolean config = MomotinkerConfig.explosion_destroys_limit.get();
        if (!this.level().isClientSide) {
            Level.ExplosionInteraction explosionInteraction;
            boolean a;
            if (config) {
                explosionInteraction=Level.ExplosionInteraction.BLOCK;
                a=true;
            }else {
                explosionInteraction=Level.ExplosionInteraction.NONE;
                a=false;
            }
            Explosion explosion =this.level().explode(this, this.getX(), this.getY(), this.getZ(), 5, a, explosionInteraction);
            List<LivingEntity> lis = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(8));
            for (LivingEntity entity : lis) {
                if (entity != null&&this.getOwner() instanceof Player player&&entity!=this.getOwner()) {
                    entity.invulnerableTime = 0;
                    AttackUtil.attackEntity(this.tool, player, InteractionHand.MAIN_HAND, entity, ()->1, true, Util.getSlotType(InteractionHand.MAIN_HAND), this.damage, this.damagemultiplier,false, true, true,false);
                }
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);
        Explode();
        this.discard();
    }
    @Override
    protected void onHitEntity(EntityHitResult p_37259_) {
        super.onHitEntity(p_37259_);
        Explode();
        this.discard();
    }
    @Override
    protected void defineSynchedData() {
    }
}
