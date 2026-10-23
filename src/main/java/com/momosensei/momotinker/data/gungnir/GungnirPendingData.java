package com.momosensei.momotinker.data.gungnir;

import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerTools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

public class GungnirPendingData extends SavedData {

    private static final String KEY_ENTRIES    = Momotinker.getResource("gungnir_pending_entries").toString();
    private static final String KEY_PLAYER     = Momotinker.getResource("gungnir_pending_player").toString();
    private static final String KEY_ITEM       = Momotinker.getResource("gungnir_pending_item").toString();
    private static final String KEY_UNTIL      = Momotinker.getResource("gungnir_pending_until").toString();
    private static final String KEY_ANCHOR     = Momotinker.getResource("gungnir_pending_anchor").toString();
    private static final String KEY_TOKEN      = Momotinker.getResource("gungnir_pending_token").toString();

    public static final ResourceLocation GUNGNIR_PENDING = Momotinker.getResource("gungnir_pending");

    private static final int MAX_SLOT = 41;

    public static final int NO_ANCHOR = -1;

    public static final class Entry {
        public ItemStack original;
        public long disabledUntil;
        public int anchorSlot;
        public UUID token;

        public Entry(ItemStack original, long disabledUntil, int anchorSlot, UUID token) {
            this.original = original;
            this.disabledUntil = disabledUntil;
            this.anchorSlot = anchorSlot;
            this.token = token;
        }

        public ItemStack copyOriginal() {
            return original.copy();
        }
    }

    private final Map<UUID, Entry> pending = new TreeMap<>();

    public static GungnirPendingData load(CompoundTag tag) {
        GungnirPendingData d = new GungnirPendingData();
        ListTag list = tag.getList(KEY_ENTRIES, Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag e = list.getCompound(i);

            UUID pid = parseUuid(e.getString(KEY_PLAYER));
            if (pid == null) continue;

            if (!e.contains(KEY_ITEM, Tag.TAG_COMPOUND)) continue;
            ItemStack stack = ItemStack.of(e.getCompound(KEY_ITEM));
            if (stack.isEmpty() || stack.getItem() != MomotinkerTools.gungnir.get()) continue;

            long until = e.getLong(KEY_UNTIL);

            int anchor = NO_ANCHOR;
            if (e.contains(KEY_ANCHOR, Tag.TAG_INT)) {
                int a = e.getInt(KEY_ANCHOR);
                if (a >= 0 && a < MAX_SLOT) anchor = a;
            }
            UUID token;
            if (e.hasUUID(KEY_TOKEN)) {
                token = e.getUUID(KEY_TOKEN);
            } else {
                token = UUID.randomUUID();
            }

            d.pending.putIfAbsent(pid, new Entry(stack, until, anchor, token));
        }
        return d;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        pending.forEach((pid, entry) -> {
            CompoundTag e = new CompoundTag();
            e.putString(KEY_PLAYER, pid.toString());
            e.put(KEY_ITEM, entry.original.save(new CompoundTag()));
            e.putLong(KEY_UNTIL, entry.disabledUntil);
            e.putInt(KEY_ANCHOR, entry.anchorSlot);
            e.putUUID(KEY_TOKEN, entry.token);
            list.add(e);
        });
        tag.put(KEY_ENTRIES, list);
        return tag;
    }

    private static UUID parseUuid(String s) {
        if (s == null || s.isEmpty()) return null;
        try {
            return UUID.fromString(s);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    @Nullable
    public Entry get(UUID playerId) {
        return pending.get(playerId);
    }

    public boolean has(UUID playerId) {
        return pending.containsKey(playerId);
    }

    public int size() {
        return pending.size();
    }

    public Set<UUID> players() {
        return Collections.unmodifiableSet(pending.keySet());
    }

    public Entry put(UUID playerId, ItemStack original, long disabledUntil, int anchorSlot) {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(original, "original");
        if (original.isEmpty()) {
            throw new IllegalArgumentException("original stack must not be empty");
        }
        int slot = (anchorSlot >= 0 && anchorSlot < MAX_SLOT) ? anchorSlot : NO_ANCHOR;
        Entry e = new Entry(original.copy(), disabledUntil, slot, UUID.randomUUID());
        pending.put(playerId, e);
        setDirty();
        return e;
    }

    @Deprecated
    @Nullable
    public Entry put(UUID playerId, ItemStack original, long disabledUntil) {
        return put(playerId, original, disabledUntil, NO_ANCHOR);
    }

    @Nullable
    public Entry remove(UUID playerId) {
        Entry e = pending.remove(playerId);
        if (e != null) setDirty();
        return e;
    }

    @Nullable
    public Entry removeIfToken(UUID playerId, @Nullable UUID token) {
        Entry e = pending.get(playerId);
        if (e == null) return null;
        if (token != null && !token.equals(e.token)) return null;
        pending.remove(playerId);
        setDirty();
        return e;
    }

    public void clearAll() {
        if (!pending.isEmpty()) {
            pending.clear();
            setDirty();
        }
    }

    public List<UUID> collectExpired(long gameTime) {
        List<UUID> out = new ArrayList<>();
        pending.forEach((pid, e) -> {
            if (gameTime >= e.disabledUntil) out.add(pid);
        });
        return out;
    }

    public boolean isExpired(UUID playerId, long gameTime) {
        Entry e = pending.get(playerId);
        return e != null && gameTime >= e.disabledUntil;
    }

    public static GungnirPendingData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(
                GungnirPendingData::load, GungnirPendingData::new, GUNGNIR_PENDING.toString());
    }
}