package com.momosensei.momotinker.util;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.phys.AABB;

import java.lang.invoke.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess.UNSAFE;
import static net.minecraftforge.fml.util.ObfuscationReflectionHelper.findMethod;

public class Cutter {
    public static final Map<Integer, Float> HEALTH_OFFSET = new ConcurrentHashMap<>();
    public static final Map<Integer, Integer> CLIENT_DEATHTICKS = new ConcurrentHashMap<>();
    public static final Map<Integer, Integer> SERVER_DEATHTICKS = new ConcurrentHashMap<>();
    public static final Set<String> RELOADED_CLASSES = ConcurrentHashMap.newKeySet();

    private static final Map<String, MethodHandle> ORIGINAL_METHODS = new ConcurrentHashMap<>();
    private static final Map<String, MethodHandle> MODIFIED_METHODS = new ConcurrentHashMap<>();

    public static void hurtEnemy(LivingEntity enemy, LivingEntity source) {
        if (source instanceof ServerPlayer) {
            HEALTH_OFFSET.putIfAbsent(enemy.getId(), 0.0F);
            HEALTH_OFFSET.put(enemy.getId(), enemy.getMaxHealth());
            reload(enemy.getClass());
        }
    }

    public static float getModifyHealth(LivingEntity entity, float health) {
        return Math.max(0.0F, HEALTH_OFFSET.containsKey(entity.getId()) ?
                health - HEALTH_OFFSET.get(entity.getId()) : health);
    }

    public static boolean onTickStart(LivingEntity entity) {
        if (HEALTH_OFFSET.containsKey(entity.getId()) &&
                HEALTH_OFFSET.get(entity.getId()) >= entity.getMaxHealth()) {

            if (entity.level().isClientSide()) {
                CLIENT_DEATHTICKS.putIfAbsent(entity.getId(), 0);
                entity.deathTime = CLIENT_DEATHTICKS.get(entity.getId());
                CLIENT_DEATHTICKS.put(entity.getId(), CLIENT_DEATHTICKS.get(entity.getId()) + 1);
                if (CLIENT_DEATHTICKS.get(entity.getId()) >= 20) {
                    killEntity(entity);
                    CLIENT_DEATHTICKS.remove(entity.getId());
                }
            } else {
                SERVER_DEATHTICKS.putIfAbsent(entity.getId(), 0);
                entity.deathTime = SERVER_DEATHTICKS.get(entity.getId());
                SERVER_DEATHTICKS.put(entity.getId(), SERVER_DEATHTICKS.get(entity.getId()) + 1);
                if (SERVER_DEATHTICKS.get(entity.getId()) >= 20) {
                    entity.level().broadcastEntityEvent(entity, (byte) 60);
                    killEntity(entity);
                    SERVER_DEATHTICKS.remove(entity.getId());
                }
            }

            for (Map.Entry<Integer, Float> entry : new HashMap<>(HEALTH_OFFSET).entrySet()) {
                if (!CLIENT_DEATHTICKS.containsKey(entry.getKey()) && !SERVER_DEATHTICKS.containsKey(entry.getKey())) {
                    HEALTH_OFFSET.remove(entry.getKey());
                }
            }

            return false;
        } else {
            return true;
        }
    }

    public static void reload(Class<?> clazz) {
        if (clazz == null) {
            return;
        }

        for (Class<?> current = clazz;
             current != null && current != Object.class && current != LivingEntity.class.getSuperclass();
             current = current.getSuperclass()) {

            try {
                String className = current.getName();
                if (!RELOADED_CLASSES.contains(className)) {
                    System.out.println("Reloading class: " + className);

                    boolean success = modifyClassWithMethodHandles(current);
                    if (success) {
                        RELOADED_CLASSES.add(className);
                        System.out.println("Successfully reloaded: " + className);
                    } else {
                        System.err.println("Failed to reload: " + className);
                    }
                }
            } catch (Throwable e) {
                System.err.println("Error reloading class " + current.getName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static boolean modifyClassWithMethodHandles(Class<?> clazz) {
        try {
            boolean modified = false;

            modified |= modifyGetHealthMethod(clazz);

            modified |= modifyTickMethod(clazz);

            return modified;
        } catch (Throwable e) {
            return false;
        }
    }

    private static boolean modifyGetHealthMethod(Class<?> clazz) {
        try {
            Method getHealthMethod = clazz.getDeclaredMethod("getHealth");
            String methodKey = clazz.getName() + "#getHealth";

            MethodHandle originalHandle = MethodHandles.lookup().unreflect(getHealthMethod);
            ORIGINAL_METHODS.put(methodKey, originalHandle);

            MethodHandle modifiedHandle = MethodHandles.filterReturnValue(
                    originalHandle,
                    createGetHealthFilter(clazz)
            );

            MODIFIED_METHODS.put(methodKey, modifiedHandle);

            replaceMethodCalls(clazz, "getHealth", modifiedHandle);

            return true;
        } catch (NoSuchMethodException e) {
            return false;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static MethodHandle createGetHealthFilter(Class<?> clazz) {
        try {
            MethodHandle filter = MethodHandles.lookup().findStatic(
                    Cutter.class,
                    "filterGetHealth",
                    MethodType.methodType(float.class, float.class, LivingEntity.class)
            );

            return MethodHandles.insertArguments(filter, 1, (LivingEntity) null);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to create getHealth filter", e);
        }
    }

    public static float filterGetHealth(float originalHealth, LivingEntity entity) {
        return getModifyHealth(entity, originalHealth);
    }

    private static boolean modifyTickMethod(Class<?> clazz) {
        try {
            Method tickMethod = clazz.getDeclaredMethod("tick");
            String methodKey = clazz.getName() + "#tick";

            MethodHandle originalHandle = MethodHandles.lookup().unreflect(tickMethod);
            ORIGINAL_METHODS.put(methodKey, originalHandle);

            MethodHandle wrapperHandle = createTickWrapper(originalHandle, clazz);
            MODIFIED_METHODS.put(methodKey, wrapperHandle);

            replaceMethodCalls(clazz, "tick", wrapperHandle);

            return true;
        } catch (NoSuchMethodException e) {
            return false;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static MethodHandle createTickWrapper(MethodHandle originalHandle, Class<?> clazz) {
        try {
            MethodHandle onTickStartHandle = MethodHandles.lookup().findStatic(
                    Cutter.class,
                    "onTickStart",
                    MethodType.methodType(boolean.class, LivingEntity.class)
            );
            return MethodHandles.filterArguments(originalHandle, 0, createTickFilter(onTickStartHandle));
        } catch (Throwable e) {
            throw new RuntimeException("Failed to create tick wrapper", e);
        }
    }

    private static MethodHandle createTickFilter(MethodHandle onTickStartHandle) {
        try {
            MethodHandle conditionChecker = MethodHandles.lookup().findStatic(
                    Cutter.class,
                    "checkTickCondition",
                    MethodType.methodType(void.class, LivingEntity.class, MethodHandle.class)
            );

            return MethodHandles.insertArguments(conditionChecker, 1, onTickStartHandle);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to create tick filter", e);
        }
    }

    public static void checkTickCondition(LivingEntity entity, MethodHandle onTickStartHandle) {
        try {
            onTickStartHandle.invokeExact(entity);
        } catch (Throwable e) {
        }
    }

    private static void replaceMethodCalls(Class<?> clazz, String methodName, MethodHandle newHandle) {
        try {
            CallSite callSite = new ConstantCallSite(newHandle);
        } catch (Throwable e) {
        }
    }

    public static void restoreOriginalMethods(Class<?> clazz) {
        try {
            String getHealthKey = clazz.getName() + "#getHealth";
            String tickKey = clazz.getName() + "#tick";

            if (ORIGINAL_METHODS.containsKey(getHealthKey)) {
                replaceMethodCalls(clazz, "getHealth", ORIGINAL_METHODS.get(getHealthKey));
                ORIGINAL_METHODS.remove(getHealthKey);
                MODIFIED_METHODS.remove(getHealthKey);
            }

            if (ORIGINAL_METHODS.containsKey(tickKey)) {
                replaceMethodCalls(clazz, "tick", ORIGINAL_METHODS.get(tickKey));
                ORIGINAL_METHODS.remove(tickKey);
                MODIFIED_METHODS.remove(tickKey);
            }

            RELOADED_CLASSES.remove(clazz.getName());

        } catch (Throwable e) {
            System.err.println("Error restoring original methods: " + e.getMessage());
        }
    }

    private static class TickInterruptedException extends RuntimeException {
        public TickInterruptedException(String message) {
            super(message);
        }
    }
    public static void AttackEntity(Level world,Entity entity) {
        try {
            if (!world.isClientSide) {
                ServerLevel serverLevel = (ServerLevel)world;
                for(Entity entityIn : new ArrayList<>(StreamSupport.stream(serverLevel.getAllEntities().spliterator(), false).collect(Collectors.toList()))) {
                    if (entityIn==entity) {
                        killEntity(entityIn);
                    }
                }
            } else {
                ClientLevel clientWorld = (ClientLevel)world;
                clientWorld.minecraft.gui.getBossOverlay().reset();
                for(Entity clientEntity : new ArrayList<>(StreamSupport.stream(clientWorld.entitiesForRendering().spliterator(), false).collect(Collectors.toList()))) {
                    if (clientEntity==entity) {
                        killEntity(clientEntity);
                    }
                }
            }
        } catch (Throwable throwable) {
        }
    }
    public static void Attack(Level world) {
        try {
            if (!world.isClientSide) {
                ServerLevel serverLevel = (ServerLevel)world;
                for(Entity entityIn : new ArrayList<>(StreamSupport.stream(serverLevel.getAllEntities().spliterator(), false).collect(Collectors.toList()))) {
                    killEntity(entityIn);
                }
            } else {
                ClientLevel clientWorld = (ClientLevel)world;
                clientWorld.minecraft.gui.getBossOverlay().reset();
                for(Entity clientEntity : new ArrayList<>(StreamSupport.stream(clientWorld.entitiesForRendering().spliterator(), false).collect(Collectors.toList()))) {
                    killEntity(clientEntity);
                }
            }
        } catch (Throwable throwable) {
        }
    }
    public static void killEntity(Entity entity) {
        killEntity(entity, true);
    }

    public static void killEntity(Entity entity, boolean ignoredSSDeath) {
        try {
            if (entity != null && !(entity instanceof Player) && !(entity instanceof ItemEntity)) {
                Level world = entity.level;
                if (entity instanceof LivingEntity livingEntity && !entity.level().isClientSide()) {
                    try {
                        invoke(livingEntity, "m_6668_", livingEntity.damageSources().generic());
                        livingEntity.captureDrops(new ArrayList<>());
                    } catch (Throwable ignored) {
                    }
                }

                entity.isAddedToWorld = false;
                entity.removalReason = Entity.RemovalReason.DISCARDED;
                entity.invulnerable = true;
                entity.onGround = false;
                entity.canUpdate = false;
                entity.discard();
                double x = entity.getX();
                double y = entity.getY();
                double z = entity.getZ();
                entity.bb = new AABB(x, y, z, x, y, z);

                entity.passengers = ImmutableList.<Entity>builder().build();

                entity.updateDynamicGameEventListener(DynamicGameEventListener::remove);
                entity.invalidateCaps();

                if (entity instanceof LivingEntity livingEntity) {
                    livingEntity.activeEffects = new HashMap<>();
                    livingEntity.deathTime = 0;
                    livingEntity.dead = true;
                }

                entity.levelCallback = EntityInLevelCallback.NULL;
                
                if (world instanceof ClientLevel clientWorld) {
                    if (entity.isMultipartEntity()) {
                        clientWorld.partEntities.remove(entity.getId());
                    }

                    synchronized(clientWorld.entityStorage) {
                        clientWorld.entityStorage.callbacks.onDestroyed(entity);
                        clientWorld.entityStorage.callbacks.onTickingEnd(entity);
                        clientWorld.entityStorage.callbacks.onTrackingEnd(entity);
                    }

                    synchronized(clientWorld.entityStorage.entityStorage.byId) {
                        clientWorld.entityStorage.entityStorage.byId = new Int2ObjectLinkedOpenHashMap<>(clientWorld.entityStorage.entityStorage.byId);
                        clientWorld.entityStorage.entityStorage.byId.remove(entity.getId());
                    }

                    synchronized(clientWorld.entityStorage.entityStorage.byUuid) {
                        clientWorld.entityStorage.entityStorage.byUuid = new HashMap<>(clientWorld.entityStorage.entityStorage.byUuid);
                        clientWorld.entityStorage.entityStorage.byUuid.remove(entity.getUUID());
                    }
                } else if (world instanceof ServerLevel serverWorld) {
                    synchronized(serverWorld.getChunkSource().chunkMap.entityMap) {
                        serverWorld.getChunkSource().chunkMap.entityMap.remove(entity.getId());
                    }

                    serverWorld.entityManager.knownUuids.remove(entity.getUUID());
                    EntitySection<Entity> section = serverWorld.entityManager.sectionStorage.getSection(SectionPos.asLong(entity.blockPosition()));
                    if (section != null) {
                        synchronized(section.storage.allInstances) {
                            section.storage.byClass.getOrDefault(entity.getClass(), new ArrayList<>()).remove(entity);
                            section.storage.allInstances.remove(entity);
                        }
                    }

                    synchronized(serverWorld.entityTickList.active) {
                        serverWorld.entityTickList.active = new Int2ObjectLinkedOpenHashMap<>(serverWorld.entityTickList.active);
                        serverWorld.entityTickList.active.remove(entity.getId());
                    }

                    synchronized(serverWorld.entityManager.visibleEntityStorage.byId) {
                        serverWorld.entityManager.visibleEntityStorage.byId = new Int2ObjectLinkedOpenHashMap<>(serverWorld.entityManager.visibleEntityStorage.byId);
                        serverWorld.entityManager.visibleEntityStorage.byId.remove(entity.getId());
                    }

                    synchronized(serverWorld.entityManager.visibleEntityStorage.byUuid) {
                        serverWorld.entityManager.visibleEntityStorage.byUuid = new HashMap<>(serverWorld.entityManager.visibleEntityStorage.byUuid);
                        serverWorld.entityManager.visibleEntityStorage.byUuid.remove(entity.getUUID());
                    }
                }

            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static final MethodHandles.Lookup LOOKUP = getLookup();

    public static Object invoke(Object target, String name, Object... args) {
        Class<?> klass = target instanceof Class ? (Class)target : target.getClass();
        Class<?>[] argTypes = new Class[args.length];

        for(int i = 0; i < args.length; ++i) {
            argTypes[i] = args[i].getClass();
        }

        try {
            Method method = findMethod(klass, name, argTypes);
            MethodHandle methodHandle = LOOKUP.unreflect(method);
            if (target instanceof Class) {
                return methodHandle.invokeWithArguments(args);
            } else {
                Object[] combinedArgs = new Object[args.length + 1];
                combinedArgs[0] = target;
                System.arraycopy(args, 0, combinedArgs, 1, args.length);
                return methodHandle.invokeWithArguments(combinedArgs);
            }
        } catch (Throwable e) {
            return null;
        }
    }

    public static MethodHandles.Lookup getLookup() {
        try {
            return (MethodHandles.Lookup)UNSAFE.getObjectVolatile(UNSAFE.staticFieldBase(MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP")), UNSAFE.staticFieldOffset(MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP")));
        } catch (Exception var31) {
            try {
                Constructor<MethodHandles.Lookup> c = MethodHandles.Lookup.class.getDeclaredConstructor();
                c.setAccessible(true);
                return (MethodHandles.Lookup)c.newInstance();
            } catch (Throwable var3) {
                var3.printStackTrace();
                return null;
            }
        }
    }
}
