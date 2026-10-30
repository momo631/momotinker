package com.momosensei.momotinker.data.gungnir;

import com.momosensei.momotinker.Items.tool.gungnir.gungnir;
import com.momosensei.momotinker.Items.tool.gungnir.gungnir_disabled_item;
import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.GungnirItemPacket;
import com.momosensei.momotinker.register.MomotinkerTools;
import com.momosensei.momotinker.util.AttackUtil;
import com.momosensei.momotinker.util.ChunkLoadManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.Util;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.momosensei.momotinker.util.AttackUtil.attackDamage;

public final class GungnirState {

    private GungnirState() {}

    private static final Set<UUID> SETTLING = ConcurrentHashMap.newKeySet();

    public static void clearItemNbt(LivingEntity entity) {
        entity.getPersistentData().remove(gungnir.its_gungnir_key);
        if (!entity.level().isClientSide) {
            GungnirItemPacket packet = new GungnirItemPacket(entity.getId(), ItemStack.EMPTY);
            Channel.INSTANCE.send(
                    PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity),
                    packet
            );
        }
    }

    public static void setItemNbt(LivingEntity entity, ItemStack stack) {
        entity.getPersistentData().put(gungnir.its_gungnir_key, stack.save(new CompoundTag()));
        if (!entity.level().isClientSide) {
            GungnirItemPacket packet = new GungnirItemPacket(entity.getId(), stack);
            Channel.INSTANCE.send(
                    PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity),
                    packet
            );
        }
    }
    public static void markEntity(LivingEntity entity, ItemStack stack, UUID ownerId) {
        setItemNbt(entity, stack);
        if (entity.level() instanceof ServerLevel sl) {
            Player player = sl.getServer().getPlayerList().getPlayer(ownerId);
            if (momomodifier.isToolStack(stack) && player != null) {
                ToolStack tool = ToolStack.from(stack);
                float a = attackDamage(tool, player, InteractionHand.MAIN_HAND, entity, () -> 1, true, EquipmentSlot.MAINHAND, 1f);
                setGungnirDamageNbt(entity, a+1);
            }
            if (!entity.level().isClientSide) {
                GungnirMarkedData.get(sl.getServer()).setMark(ownerId, entity.getUUID());
            }
        }
    }

    public static void clearEntityMark(LivingEntity entity) {
        clearItemNbt(entity);
        ChunkLoadManager.stopForceLoadingForEntity(entity, entity.level().dimension());
    }

    public static void setGungnirDamageNbt(LivingEntity target,float a){
        var data=target.getPersistentData();
        if (a<=0)a=0;
        data.putFloat(gungnir.its_gungnir_damage_key,a);
    }
    public static float getGungnirDamageNbt(LivingEntity living){
        var nbt=living.getPersistentData();
        if (nbt.contains(gungnir.its_gungnir_damage_key)){
            return nbt.getFloat(gungnir.its_gungnir_damage_key);
        }
        return 0;
    }

    public static boolean disable(ServerPlayer sp, int slot) {
        ItemStack original = sp.getInventory().getItem(slot);
        if (original.isEmpty()) return false;

        MinecraftServer server = sp.getServer();
        if (server == null) return false;

        long until = server.overworld().getGameTime()
                + Math.max(1, gungnir_disabled_item.DISABLED_DURATION);

        GungnirPendingData.Entry entry = GungnirPendingData.get(server)
                .put(sp.getUUID(), original, until, slot);

        ItemStack disabled = gungnir_disabled_item.createForDisabledToken(entry.token);
        try {
            sp.getInventory().setItem(slot, disabled);
            sp.getInventory().setChanged();
            sp.containerMenu.broadcastChanges();
        } catch (Throwable t) {
            GungnirPendingData.get(server).remove(sp.getUUID());
            sp.getInventory().setItem(slot, original);
            sp.getInventory().setChanged();
            sp.containerMenu.broadcastChanges();
            return false;
        }
        return true;
    }

    public static boolean restore(ServerPlayer sp, InteractionHand hand, int preferredSlot) {
        if (preferredSlot >= 0 && preferredSlot < sp.getInventory().getContainerSize()) {
            if (restoreAt(sp, preferredSlot, sp.getInventory().getItem(preferredSlot))) return true;
        }
        for (int i = 0; i < sp.getInventory().getContainerSize(); i++) {
            if (restoreAt(sp, i, sp.getInventory().getItem(i))) return true;
        }
        if (hand != null) {
            ItemStack inHand = sp.getItemInHand(hand);
            if (inHand.getItem() instanceof gungnir_disabled_item) {
                ItemStack original = takePendingOriginal(sp,inHand);
                if (original != null && !original.isEmpty()) {
                    sp.setItemInHand(hand, original);
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean restoreAt(ServerPlayer sp, int slot, ItemStack stack) {
        if (!(stack.getItem() instanceof gungnir_disabled_item)) return false;
        ItemStack original = takePendingOriginal(sp, stack);
        if (original == null || original.isEmpty()) return false;

        sp.getInventory().setItem(slot, original);
        sp.getInventory().setChanged();
        sp.containerMenu.broadcastChanges();
        return true;
    }
    public static boolean restoreFromPending(ServerPlayer sp) {
        MinecraftServer server = sp.getServer();
        if (server == null) return false;
        GungnirPendingData.Entry e = GungnirPendingData.get(server).get(sp.getUUID());
        if (e == null || e.original.isEmpty()) return false;

        var inv = sp.getInventory();
        int anchor = e.anchorSlot;
        if (anchor >= 0 && anchor < inv.getContainerSize()) {
            ItemStack cur = inv.getItem(anchor);
            if (cur.getItem() instanceof gungnir_disabled_item) {
                if (takePendingOriginal(sp, cur) != null) {
                    inv.setItem(anchor, e.original);
                    inv.setChanged();
                    sp.containerMenu.broadcastChanges();
                    return true;
                }
            }
        }
        for (int i = 0; i < inv.getContainerSize(); i++) {
            if (restoreAt(sp, i, inv.getItem(i))) return true;
        }
        ItemStack original = takePendingOriginal(sp, null);
        if (original == null || original.isEmpty()) return false;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            if (inv.getItem(i).isEmpty()) {
                inv.setItem(i, original);
                inv.setChanged();
                sp.containerMenu.broadcastChanges();
                return true;
            }
        }
        int sel = inv.selected;
        ItemStack old = inv.getItem(sel);
        inv.setItem(sel, original);
        if (!old.isEmpty()) sp.drop(old, false);
        inv.setChanged();
        sp.containerMenu.broadcastChanges();
        return true;
    }

    private static ItemStack takePendingOriginal(ServerPlayer sp, @Nullable ItemStack observedDisabled) {
        MinecraftServer server = sp.getServer();
        if (server == null) return null;
        GungnirPendingData pending = GungnirPendingData.get(server);
        GungnirPendingData.Entry e = pending.get(sp.getUUID());
        if (e == null || e.original.isEmpty()) return null;
        if (e.original.getItem() != MomotinkerTools.gungnir.get()) {
            pending.remove(sp.getUUID());
            return null;
        }
        if (observedDisabled != null && observedDisabled.getItem() instanceof gungnir_disabled_item) {
            UUID token = gungnir_disabled_item.getDisabledToken(observedDisabled);
            if (token != null && !token.equals(e.token)) {
                return null;
            }
        }
        pending.remove(sp.getUUID());
        return e.original;
    }

    public static ItemStack extractOriginal(ItemStack disabled) {
        CompoundTag tag = disabled.getTag();
        if (tag == null) return null;
        String key = gungnir_disabled_item.original_gungnir.toString();
        if (!tag.contains(key)) return null;
        ItemStack original = ItemStack.of(tag.getCompound(key));
        if (original.isEmpty() || original.getItem() != MomotinkerTools.gungnir.get()) {
            return null;
        }
        return original;
    }

    public static boolean settle(ServerPlayer sp, InteractionHand hand, boolean dealDamage) {
        MinecraftServer server = sp.getServer();
        if (server == null) return false;
        UUID playerId = sp.getUUID();
        GungnirMarkedData data = GungnirMarkedData.get(server);
        GungnirPendingData pending = GungnirPendingData.get(server);

        boolean hasMark = data.hasMark(playerId);
        boolean hasPending = pending.has(playerId);
        boolean wasFired = data.isFired(playerId);
        if (!wasFired && !hasMark && !hasPending) return true;

        if (!SETTLING.add(playerId)) return false;
        try {
            int preferredSlot = data.getSlot(playerId);
            boolean needRestore = wasFired || hasPending;
            boolean restored = !needRestore || restore(sp, hand, preferredSlot);
            if (!restored) restored = restoreFromPending(sp);

            if (needRestore && !restored) {
                GungnirPendingData.Entry e = pending.remove(playerId);
                if (e != null && !e.original.isEmpty()) {
                    sp.drop(e.original.copy(), false);
                }
                if (dealDamage) damageAndClearMarked(sp, hand, data, playerId);
                else clearMarkedNoDamage(server, data, playerId);
                data.clearSlot(playerId);
                return false;
            }

            if (dealDamage) damageAndClearMarked(sp, hand, data, playerId);
            else clearMarkedNoDamage(server, data, playerId);
            data.clearSlot(playerId);
            return true;
        } finally {
            SETTLING.remove(playerId);
        }
    }

    private static void damageAndClearMarked(ServerPlayer sp, InteractionHand hand,GungnirMarkedData data, UUID playerId) {
        UUID marked = data.getMark(playerId);
        if (marked != null) {
            LivingEntity living = findLivingAnyLevel(sp.getServer(), marked);
            if (living != null) {
                ItemStack stack = GungnirClientHelper.resolveGungnirStack(living);
                if (momomodifier.isToolStack(stack)) {
                    ToolStack tool = ToolStack.from(stack);
                    float a = getGungnirDamageNbt(living);
                    living.invulnerableTime = 0;
                    AttackUtil.attackEntity(tool, sp, InteractionHand.MAIN_HAND, living, () -> 1,
                            false, Util.getSlotType(InteractionHand.MAIN_HAND),
                            tool.getStats().get(ToolStats.ATTACK_DAMAGE)+a, 0.4f,
                            false, true, true, false);
                    clearEntityMark(living);
                    if (hand!=null) {
                        GungnirTrajectoryClient.addGungnirTrajectory(sp, living, hand);
                    }
                }
            }
        }
        data.clearMark(playerId);
    }

    private static void clearMarkedNoDamage(MinecraftServer server, GungnirMarkedData data, UUID playerId) {
        UUID marked = data.getMark(playerId);
        if (marked != null) {
            LivingEntity living = findLivingAnyLevel(server, marked);
            if (living != null && !(living instanceof Player)) {
                ChunkLoadManager.stopForceLoadingForEntity(living, living.level().dimension());
                clearEntityMark(living);
            }
        }
        data.clearMark(playerId);
    }

    private static LivingEntity findLivingAnyLevel(MinecraftServer server, UUID uuid) {
        if (server == null) return null;
        for (ServerLevel lvl : server.getAllLevels()) {
            Entity e = lvl.getEntity(uuid);
            if (e instanceof LivingEntity living) return living;
        }
        return null;
    }


    public static void onEntityRemoved(LivingEntity entity) {
        if (entity.level().isClientSide) return;
        if (entity.getPersistentData().get(gungnir.its_gungnir_key) == null) return;
        clearEntityMark(entity);
    }
}
