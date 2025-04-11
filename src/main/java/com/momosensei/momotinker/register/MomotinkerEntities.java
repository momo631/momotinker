package com.momosensei.momotinker.register;

import com.momosensei.momotinker.entity.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.deferred.EntityTypeDeferredRegister;

import static com.momosensei.momotinker.Momotinker.MOD_ID;
import static com.momosensei.momotinker.entity.MomotinkerEntitiesCreate.getSpear;
import static com.momosensei.momotinker.tool.trigger_blade.getSlash;

public class MomotinkerEntities {

    public static final EntityTypeDeferredRegister ENTITIES = new EntityTypeDeferredRegister(MOD_ID);

    public static final RegistryObject<EntityType<TriggerSlashEntity>> trigger_slash_a = registerTriggerSlash("trigger_slash_a",MomotinkerEntities.trigger_slash_a,1);
    public static final RegistryObject<EntityType<SpearEntity>> spear_entity = registerSpearEntity("spear_entity",MomotinkerEntities.spear_entity,2);
    public static final RegistryObject<EntityType<CleanseEntity>> cleanse_entity = ENTITIES.register("cleanse_entity", () -> EntityType.Builder.<CleanseEntity>of(CleanseEntity::new, MobCategory.MISC).sized(30F, 60F).setTrackingRange(4).setUpdateInterval(1).setCustomClientFactory((spawnEntity, world) -> new CleanseEntity(world,spawnEntity.getPosX(),spawnEntity.getPosY(),spawnEntity.getPosZ(),spawnEntity.getEntity().getDeltaMovement())).setShouldReceiveVelocityUpdates(true));
    public static final RegistryObject<EntityType<RayEntity>> ray_entity = ENTITIES.register("ray_entity", () -> EntityType.Builder.of(RayEntity::new, MobCategory.MISC).sized(0.75F, 0.75F).setTrackingRange(4).setUpdateInterval(5).setCustomClientFactory((spawnEntity, world) -> new RayEntity(MomotinkerEntities.ray_entity.get(), world)).setShouldReceiveVelocityUpdates(true));
    public static final RegistryObject<EntityType<MeteorEntity>> meteor_entity = ENTITIES.register("meteor_entity", () -> EntityType.Builder.<MeteorEntity>of(MeteorEntity::new, MobCategory.MISC).sized(1F, 1F).setTrackingRange(4).setUpdateInterval(1).setCustomClientFactory((spawnEntity, world) -> new MeteorEntity(world,spawnEntity.getPosX(),spawnEntity.getPosY(),spawnEntity.getPosZ(),spawnEntity.getEntity().getDeltaMovement())).setShouldReceiveVelocityUpdates(true));
    public static final RegistryObject<EntityType<StarfallEntity>> starfall_entity = ENTITIES.register("starfall_entity", () -> EntityType.Builder.<StarfallEntity>of(StarfallEntity::new, MobCategory.MISC).sized(1F, 1F).setTrackingRange(4).setUpdateInterval(1).setCustomClientFactory((spawnEntity, world) -> new StarfallEntity(world,spawnEntity.getPosX(),spawnEntity.getPosY(),spawnEntity.getPosZ(),spawnEntity.getEntity().getDeltaMovement())).setShouldReceiveVelocityUpdates(true));

    public static RegistryObject<EntityType<TriggerSlashEntity>> registerTriggerSlash(String name, RegistryObject<EntityType<TriggerSlashEntity>> Type, int index){
        return ENTITIES.register(name, () -> EntityType.Builder.<TriggerSlashEntity>of((entityType, level)-> new TriggerSlashEntity(entityType, level,getSlash(index)), MobCategory.MISC).sized(3F, 0.1F).setTrackingRange(4).setUpdateInterval(10).setCustomClientFactory((spawnEntity, world) -> new TriggerSlashEntity(Type.get(), world,getSlash(index))).setShouldReceiveVelocityUpdates(true));
    }
    public static RegistryObject<EntityType<SpearEntity>> registerSpearEntity(String name, RegistryObject<EntityType<SpearEntity>> Type, int index){
        return ENTITIES.register(name, () -> EntityType.Builder.<SpearEntity>of((entityType, level)-> new SpearEntity(entityType, level,getSpear(index)), MobCategory.MISC).sized(1F, 1F).setTrackingRange(4).setUpdateInterval(1).setCustomClientFactory((spawnEntity, world) -> new SpearEntity(Type.get(), world,getSpear(index))).setShouldReceiveVelocityUpdates(true));
    }

}