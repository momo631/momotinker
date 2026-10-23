package com.momosensei.momotinker.data.gungnir;

import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GungnirItemCache {
    private static final Map<Integer, ItemStack> CACHE = new ConcurrentHashMap<>();

    public static void put(int entityId, ItemStack stack) {
        if (stack.isEmpty()) CACHE.remove(entityId);
        else CACHE.put(entityId, stack.copy());
    }

    public static ItemStack get(int entityId) {
        ItemStack s = CACHE.get(entityId);
        return s == null ? ItemStack.EMPTY : s;
    }

    public static void remove(int entityId) {
        CACHE.remove(entityId);
    }

    public static void clear() {
        CACHE.clear();
    }
}