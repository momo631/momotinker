package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.util.ChunkLoadManager;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static net.minecraft.core.Registry.DIMENSION_REGISTRY;


public class MemoriesOfMountains extends momomodifier {
    public MemoriesOfMountains() {
        MinecraftForge.EVENT_BUS.addListener(this::OnLivingDrop);
        MinecraftForge.EVENT_BUS.addListener(this::OnWorldTick);
    }
    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity living = context.getLivingTarget();
        if (living != null&&living!=context.getAttacker()){
            if (getMemoriesTag(living)==0){
                setMemoriesTag(living,damage);
                Livings.put(living.getUUID(), context.getAttacker());
                if (living instanceof Mob mob) {
                    mob.setPersistenceRequired();
                }
            }
            String s = "momotinker:mountains_memory";
            teleportEntityToDimension(living,s,living.getX(),living.getY(),living.getZ());
            ChunkLoadManager.startForceLoadingForEntity(living);
        }
        return damage;
    }
    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (attacker !=null && projectile instanceof AbstractArrow arrow&&target != null) {
            float f = (float) arrow.getDeltaMovement().length();
            float i = (float) Mth.clamp((double) f * arrow.getBaseDamage(), 0.0D, Float.MAX_VALUE);
            if (target != attacker&&getMemoriesTag(target) == 0) {
                setMemoriesTag(target, i);
                Livings.put(target.getUUID(), attacker);
                if (target instanceof Mob mob) {
                    mob.setPersistenceRequired();
                }
            }
            String s = "momotinker:mountains_memory";
            teleportEntityToDimension(target, s, target.getX(), target.getY(), target.getZ());
            ChunkLoadManager.startForceLoadingForEntity(target);
        }
        return false;
    }
    public static final Map<UUID, LivingEntity> Livings = new HashMap<>();

    public void OnLivingDrop(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level.isClientSide) return;
        String e = "memories_mountains";
        float a = getMemoriesTag(entity);
        if (a > 0 && Livings.get(entity.getUUID()) instanceof Player player && !entity.isAlive()) {
            if (!event.getDrops().isEmpty()) {
                for (var stack : event.getDrops()) {
                    teleportEntityToDimension(stack, player.level.dimension().location().toString(), player.getX(), player.getY() + player.getBbHeight() * 0.5f, player.getZ());
                }
            }
            ChunkLoadManager.stopForceLoadingForEntity(entity);
            Livings.remove(entity.getUUID());
            entity.getPersistentData().remove(e);
        }
    }
    private void OnWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.level.isClientSide) return;
        if (event.level instanceof ServerLevel level) {
            if (level.dimension().location().toString().equals("momotinker:mountains_memory")) {
                for (Entity entity : level.getAllEntities()) {
                    if (entity.level.isClientSide)return;
                    if (!entity.level.dimension().location().toString().equals("momotinker:mountains_memory"))return;
                    if (entity instanceof LivingEntity living) {
                        float a = getMemoriesTag(living) + 1;
                        if (a > 1 && entity.isAlive()) {
                            ChunkLoadManager.startForceLoadingForEntity(living);
                            if (living.tickCount % 20 != 0) return;
                            if (Livings.get(living.getUUID()) instanceof Player player) {
                                living.invulnerableTime = 0;
                                living.hurt(DamageSource.playerAttack(player).bypassEnchantments().bypassInvul().bypassMagic().bypassArmor(), a);
                                living.invulnerableTime = 0;
                            }
                            float b = a * 1.1f;
                            if (b>Float.MAX_VALUE)b=Float.MAX_VALUE;
                            setMemoriesTag(living, b);

                        }
                    }
                }
                //cleanupOrphanedChunks(level.getServer());
            }
        }
    }


    private static void setMemoriesTag(LivingEntity target,float a){
        String s = "memories_mountains";
        var data=target.getPersistentData();
        if (a<=0)a=0;
        data.putFloat(s,a);
    }
    private static float getMemoriesTag(LivingEntity living){
        var nbt=living.getPersistentData();
        String s = "memories_mountains";
        if (nbt.contains(s)){
            return nbt.getFloat(s);
        }
        return 0;
    }
    public static boolean teleportEntityToDimension(Entity entity, String dimensionId, double x, double y, double z) {
        if (entity.level.isClientSide) return false;

        ResourceKey<Level> dimensionKey;
        try {
            dimensionKey = ResourceKey.create(DIMENSION_REGISTRY, getResourceLocation(dimensionId));
        } catch (Exception e) {
            return false;
        }

        if (entity.level.dimension().equals(dimensionKey)) {
            entity.teleportTo(x, y, z);
            return true;
        }

        if (entity.getServer() == null) return false;
        ServerLevel targetLevel = entity.getServer().getLevel(dimensionKey);
        if (targetLevel == null) {
            return false;
        }

        entity.changeDimension(targetLevel, new SimpleTeleporter(new Vec3(x, y, z)));
        return true;
    }

    private static ResourceLocation getResourceLocation(String locationStr) {
        try {
            return new ResourceLocation(locationStr);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid resource location format: " + locationStr, e);
        }
    }

    public static class SimpleTeleporter implements ITeleporter {
        private final Vec3 targetPosition;

        public SimpleTeleporter(Vec3 targetPosition) {
            this.targetPosition = targetPosition;
        }

        @Nullable
        @Override
        public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
            return new PortalInfo(this.targetPosition, Vec3.ZERO, entity.getYRot(), entity.getXRot());
        }

        @Nullable
        @Override
        public Entity placeEntity(Entity entity, ServerLevel currentLevel, ServerLevel destLevel, float yaw, Function<Boolean, Entity> repositionEntity) {
            return repositionEntity.apply(false);
        }
    }
}