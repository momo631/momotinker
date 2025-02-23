package com.momosensei.momotinker.register;

import com.momosensei.momotinker.entity.TriggerSlashEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.deferred.EntityTypeDeferredRegister;

import static com.momosensei.momotinker.Momotinker.MOD_ID;
import static com.momosensei.momotinker.tool.trigger_blade.getSlash;

public class MomotinkerEntities {

    public static final EntityTypeDeferredRegister ENTITIES = new EntityTypeDeferredRegister(MOD_ID);
    public static final RegistryObject<EntityType<TriggerSlashEntity>> trigger_slash_a = registerTriggerSlash("trigger_slash_a",MomotinkerEntities.trigger_slash_a,9);
   public static RegistryObject<EntityType<TriggerSlashEntity>> registerTriggerSlash(String name, RegistryObject<EntityType<TriggerSlashEntity>> Type, int index){
        return ENTITIES.register(name, () -> EntityType.Builder.<TriggerSlashEntity>of((entityType, level)-> new TriggerSlashEntity(entityType, level,getSlash(index)), MobCategory.MISC).sized(3F, 0.1F).setTrackingRange(4).setUpdateInterval(10).setCustomClientFactory((spawnEntity, world) -> new TriggerSlashEntity(Type.get(), world,getSlash(index))).setShouldReceiveVelocityUpdates(true));
    }
}