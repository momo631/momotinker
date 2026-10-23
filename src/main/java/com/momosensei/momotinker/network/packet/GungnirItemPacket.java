package com.momosensei.momotinker.network.packet;

import com.momosensei.momotinker.Items.tool.gungnir.gungnir;
import com.momosensei.momotinker.data.gungnir.GungnirItemCache;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GungnirItemPacket {
    private final int entityId;
    private final ItemStack stack;

    public GungnirItemPacket(int entityId, ItemStack stack) {
        this.entityId = entityId;
        this.stack = stack;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeItem(stack);
    }

    public static GungnirItemPacket decode(FriendlyByteBuf buf) {
        int id = buf.readInt();
        ItemStack stack = buf.readItem();
        return new GungnirItemPacket(id, stack);
    }

    public static void handlePacket(GungnirItemPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if (level == null) return;
            Entity e = level.getEntity(msg.entityId);
            if (!(e instanceof LivingEntity living)) return;

            if (msg.stack.isEmpty()) {
                living.getPersistentData().remove(gungnir.its_gungnir_key);
                GungnirItemCache.remove(msg.entityId);
            } else {
                living.getPersistentData().put(gungnir.its_gungnir_key, msg.stack.save(new CompoundTag()));
                GungnirItemCache.put(msg.entityId, msg.stack);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}