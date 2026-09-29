package com.momosensei.momotinker.mobs;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClientCache {
    private static final Map<ResourceLocation, Integer> STAGE_CACHE = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, Map<Integer, ItemStack>> DISPLAY_CACHE = new ConcurrentHashMap<>();
    private static final Set<ResourceLocation> PENDING_REQUESTS = ConcurrentHashMap.newKeySet();

    public static int getStage(ResourceLocation id) {
        return STAGE_CACHE.getOrDefault(id, 0);
    }

    public static boolean hasStage(ResourceLocation id) {
        return STAGE_CACHE.containsKey(id);
    }

    public static Map<Integer, ItemStack> getDisplay(ResourceLocation id) {
        return DISPLAY_CACHE.get(id);
    }

    public static void update(ResourceLocation id, int stage, Map<Integer, ItemStack> displays) {
        STAGE_CACHE.put(id, stage);
        DISPLAY_CACHE.put(id, displays);
        PENDING_REQUESTS.remove(id);
    }

    public static boolean trySetPending(ResourceLocation id) {
        return PENDING_REQUESTS.add(id);
    }

    public static void clear(ResourceLocation id) {
        STAGE_CACHE.remove(id);
        DISPLAY_CACHE.remove(id);
        PENDING_REQUESTS.remove(id);
    }
}