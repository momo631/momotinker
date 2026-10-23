package com.momosensei.momotinker.Items.tool.gungnir;

import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.data.gungnir.GungnirPendingData;
import com.momosensei.momotinker.data.gungnir.GungnirState;
import com.momosensei.momotinker.register.MomotinkerItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class gungnir_disabled_item extends Item {

    public static final ResourceLocation original_gungnir = Momotinker.getResource("original_gungnir");
    public static final int DISABLED_DURATION = 120;

    public gungnir_disabled_item(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.EPIC).fireResistant());
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerLogin);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerTick);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerClone);
        MinecraftForge.EVENT_BUS.addListener(this::onItemToss);
        MinecraftForge.EVENT_BUS.addListener(this::onServerTick);
    }

    public void onItemToss(ItemTossEvent event) {
        ItemStack stack = event.getEntity().getItem();
        if (!(stack.getItem() instanceof gungnir_disabled_item)) return;
        event.setCanceled(true);
        if (event.getPlayer() instanceof ServerPlayer sp&&!sp.getInventory().add(stack.copy())) {
            sp.drop(stack.copy(), false);
        }
    }
    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }

    public static final ResourceLocation gungnir_disabled_token = Momotinker.getResource("gungnir_disabled_token");

    public static ItemStack createForDisabledToken(UUID token) {
        ItemStack stack = new ItemStack(MomotinkerItem.gungnir_disabled_item.get());
        CompoundTag tag = stack.getOrCreateTag();
        tag.putUUID(gungnir_disabled_token.toString(), token);
        return stack;
    }

    public static UUID getDisabledToken(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.hasUUID(gungnir_disabled_token.toString())) return null;
        return tag.getUUID(gungnir_disabled_token.toString());
    }
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer sp) {
            MinecraftServer server = sp.getServer();
            if (server == null) return;
            long now = server.overworld().getGameTime();
            GungnirPendingData pending = GungnirPendingData.get(server);
            if (!pending.has(sp.getUUID())) return;
            if (!pending.isExpired(sp.getUUID(), now)) return;
            GungnirState.settle(sp, null, true);
        }
    }

    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.player instanceof ServerPlayer sp)) return;
        MinecraftServer server = sp.getServer();
        if (server == null) return;

        long now = server.overworld().getGameTime();
        GungnirPendingData pending = GungnirPendingData.get(server);
        GungnirPendingData.Entry e = pending.get(sp.getUUID());

        if (e != null) {
            if (now % 20 != 0) return;
            checkPendingAlive(sp, e, pending);
        } else {
            if (now % 5 != 0) return;
            cleanupOrphanDisabled(sp);
        }
    }

    private void checkPendingAlive(ServerPlayer sp, GungnirPendingData.Entry e, GungnirPendingData pending) {
        var inv = sp.getInventory();
        boolean found = false;
        boolean changed = false;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack s = inv.getItem(i);
            if (!(s.getItem() instanceof gungnir_disabled_item)) continue;

            UUID token = gungnir_disabled_item.getDisabledToken(s);
            if (token != null && token.equals(e.token)) {
                found = true;
            } else {
                inv.setItem(i, ItemStack.EMPTY);
                changed = true;
            }
        }

        if (changed) {
            inv.setChanged();
            sp.containerMenu.broadcastChanges();
        }

        if (!found) {
            GungnirState.settle(sp, null, false);
        }
    }

    private void cleanupOrphanDisabled(ServerPlayer sp) {
        var inv = sp.getInventory();
        boolean changed = false;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack s = inv.getItem(i);
            if (s.getItem() instanceof gungnir_disabled_item) {
                inv.setItem(i, ItemStack.EMPTY);
                changed = true;
            }
        }
        if (changed) {
            inv.setChanged();
            sp.containerMenu.broadcastChanges();
        }
    }
    public void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;
        if (!(event.getEntity() instanceof ServerPlayer sp)) return;
        MinecraftServer server = sp.getServer();
        if (server == null) return;
        if (!GungnirPendingData.get(server).has(sp.getUUID())) return;
        GungnirState.settle(sp, null, true);
    }
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;
        long now = server.overworld().getGameTime();
        GungnirPendingData pending = GungnirPendingData.get(server);
        List<UUID> expired = pending.collectExpired(now);
        if (expired.isEmpty()) return;
        for (UUID pid : expired) {
            ServerPlayer sp = server.getPlayerList().getPlayer(pid);
            if (sp == null) continue;
            GungnirState.settle(sp, null, true);
        }
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) return InteractionResultHolder.success(stack);
        if (!(player instanceof ServerPlayer sp)) return InteractionResultHolder.fail(stack);
        GungnirState.settle(sp, hand, true);
        return InteractionResultHolder.success(sp.getItemInHand(hand));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 1;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }
}