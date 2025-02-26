package com.momosensei.momotinker.register;

import com.momosensei.momotinker.entity.BurningEntity;
import com.momosensei.momotinker.entity.SpearEntity;
import com.momosensei.momotinker.entity.TriggerSlashEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.deferred.EntityTypeDeferredRegister;

import static com.momosensei.momotinker.Momotinker.MOD_ID;
import static com.momosensei.momotinker.entity.SpearCreate.getBurning;
import static com.momosensei.momotinker.entity.SpearCreate.getSpear;
import static com.momosensei.momotinker.tool.trigger_blade.getSlash;

public class MomotinkerEntities {

    public static final EntityTypeDeferredRegister ENTITIES = new EntityTypeDeferredRegister(MOD_ID);

    public static final RegistryObject<EntityType<TriggerSlashEntity>> trigger_slash_a = registerTriggerSlash("trigger_slash_a",MomotinkerEntities.trigger_slash_a,1);
    public static final RegistryObject<EntityType<SpearEntity>> spear_entity = registerSpearEntity("spear_entity",MomotinkerEntities.spear_entity,2);
    public static final RegistryObject<EntityType<BurningEntity>> burning_entity = registerBurningEntity("burning_entity",MomotinkerEntities.burning_entity,3);

    public static RegistryObject<EntityType<TriggerSlashEntity>> registerTriggerSlash(String name, RegistryObject<EntityType<TriggerSlashEntity>> Type, int index){
        return ENTITIES.register(name, () -> EntityType.Builder.<TriggerSlashEntity>of((entityType, level)-> new TriggerSlashEntity(entityType, level,getSlash(index)), MobCategory.MISC).sized(3F, 0.1F).setTrackingRange(4).setUpdateInterval(10).setCustomClientFactory((spawnEntity, world) -> new TriggerSlashEntity(Type.get(), world,getSlash(index))).setShouldReceiveVelocityUpdates(true));
    }
    public static RegistryObject<EntityType<SpearEntity>> registerSpearEntity(String name, RegistryObject<EntityType<SpearEntity>> Type, int index){
        return ENTITIES.register(name, () -> EntityType.Builder.<SpearEntity>of((entityType, level)-> new SpearEntity(entityType, level,getSpear(index)), MobCategory.MISC).sized(3F, 0.1F).setTrackingRange(4).setUpdateInterval(10).setCustomClientFactory((spawnEntity, world) -> new SpearEntity(Type.get(), world,getSpear(index))).setShouldReceiveVelocityUpdates(true));
    }
    public static RegistryObject<EntityType<BurningEntity>> registerBurningEntity(String name, RegistryObject<EntityType<BurningEntity>> Type, int index){
        return ENTITIES.register(name, () -> EntityType.Builder.<BurningEntity>of((entityType, level)-> new BurningEntity(entityType, level,getBurning(index)), MobCategory.MISC).sized(3F, 0.1F).setTrackingRange(4).setUpdateInterval(10).setCustomClientFactory((spawnEntity, world) -> new BurningEntity(Type.get(), world,getBurning(index))).setShouldReceiveVelocityUpdates(true));
    }
}