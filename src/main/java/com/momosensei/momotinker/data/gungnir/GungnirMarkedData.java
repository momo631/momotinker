package com.momosensei.momotinker.data.gungnir;

import com.momosensei.momotinker.Momotinker;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;

public class GungnirMarkedData extends SavedData {

    private final Map<UUID, UUID> marks = new TreeMap<>();
    private final Map<UUID, Integer> slots = new TreeMap<>();

    private static final String KEY_MARKS       = Momotinker.getResource("gungnir_marked_marks").toString();
    private static final String KEY_PLAYER      = Momotinker.getResource("gungnir_marked_player").toString();
    private static final String KEY_ENTITY      = Momotinker.getResource("gungnir_marked_entity").toString();
    private static final String KEY_SLOTS       = Momotinker.getResource("gungnir_marked_slots").toString();
    private static final String KEY_SLOT_PLAYER = Momotinker.getResource("gungnir_slot_player").toString();
    private static final String KEY_SLOT_VALUE  = Momotinker.getResource("gungnir_slot_value").toString();

    public static final ResourceLocation GUNGNIR_MARKED = Momotinker.getResource("gungnir_marked");


    private static final int MAX_SLOT = 41;


    public static GungnirMarkedData load(CompoundTag tag) {
        GungnirMarkedData d = new GungnirMarkedData();

        ListTag markList = tag.getList(KEY_MARKS, Tag.TAG_COMPOUND);
        for (int i = 0; i < markList.size(); i++) {
            CompoundTag entry = markList.getCompound(i);
            UUID playerId = parseUuid(entry.getString(KEY_PLAYER));
            UUID entityId = parseUuid(entry.getString(KEY_ENTITY));
            if (playerId == null || entityId == null) continue;
            d.marks.putIfAbsent(playerId, entityId);
        }

        ListTag slotList = tag.getList(KEY_SLOTS, Tag.TAG_COMPOUND);
        for (int i = 0; i < slotList.size(); i++) {
            CompoundTag entry = slotList.getCompound(i);
            UUID playerId = parseUuid(entry.getString(KEY_SLOT_PLAYER));
            if (playerId == null) continue;
            if (!entry.contains(KEY_SLOT_VALUE, Tag.TAG_INT)) continue;
            int slot = entry.getInt(KEY_SLOT_VALUE);
            if (slot < 0 || slot >= MAX_SLOT) continue;
            d.slots.putIfAbsent(playerId, slot);
        }

        return d;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag) {
        ListTag markList = new ListTag();
        marks.forEach((playerId, entityId) -> {
            CompoundTag entry = new CompoundTag();
            entry.putString(KEY_PLAYER, playerId.toString());
            entry.putString(KEY_ENTITY, entityId.toString());
            markList.add(entry);
        });
        tag.put(KEY_MARKS, markList);

        ListTag slotList = new ListTag();
        slots.forEach((playerId, slot) -> {
            CompoundTag entry = new CompoundTag();
            entry.putString(KEY_SLOT_PLAYER, playerId.toString());
            entry.putInt(KEY_SLOT_VALUE, slot);
            slotList.add(entry);
        });
        tag.put(KEY_SLOTS, slotList);

        return tag;
    }

    private static UUID parseUuid(String s) {
        if (s == null || s.isEmpty()) return null;
        try {
            return UUID.fromString(s);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    @Nullable
    public UUID getPlayerForEntity(UUID entityId) {
        for (Map.Entry<UUID, UUID> e : marks.entrySet()) {
            if (e.getValue().equals(entityId)) {
                return e.getKey();
            }
        }
        return null;
    }

    public boolean hasMark(UUID playerId) {
        return marks.containsKey(playerId);
    }

    @Nullable
    public UUID getMark(UUID playerId) {
        return marks.get(playerId);
    }

    public void setMark(UUID playerId, UUID entityId) {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(entityId, "entityId");
        UUID old = marks.put(playerId, entityId);
        if (!entityId.equals(old)) setDirty();
    }

    public void clearMark(UUID playerId) {
        if (marks.remove(playerId) != null) setDirty();
    }


    public boolean isFired(UUID playerId) {
        return slots.containsKey(playerId);
    }

    public int getSlot(UUID playerId) {
        return slots.getOrDefault(playerId, -1);
    }

    public void setSlot(UUID playerId, int slot) {
        Objects.requireNonNull(playerId, "playerId");
        if (slot < 0 || slot >= MAX_SLOT) {
            clearSlot(playerId);
            return;
        }
        Integer old = slots.put(playerId, slot);
        if (!Integer.valueOf(slot).equals(old)) setDirty();
    }

    public void clearSlot(UUID playerId) {
        if (slots.remove(playerId) != null) setDirty();
    }

    public void clearAll() {
        if (!marks.isEmpty() || !slots.isEmpty()) {
            marks.clear();
            slots.clear();
            setDirty();
        }
    }
    public void purgeOrphanMarks() {
        if (marks.keySet().removeIf(pid -> !slots.containsKey(pid))) {
            setDirty();
        }
    }
    public static GungnirMarkedData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(
                GungnirMarkedData::load, GungnirMarkedData::new, GUNGNIR_MARKED.toString());
    }
}