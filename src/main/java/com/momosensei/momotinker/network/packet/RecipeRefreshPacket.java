package com.momosensei.momotinker.network.packet;

import com.momosensei.momotinker.mobs.ClientCache;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class RecipeRefreshPacket {
    private final ResourceLocation recipeId;
    private final Map<Integer, ItemStack> displays;
    private final int stage;

    public RecipeRefreshPacket(ResourceLocation recipeId, Map<Integer, ItemStack> displays, int stage) {
        this.recipeId = recipeId;
        this.displays = displays;
        this.stage = stage;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(recipeId);
        buf.writeInt(stage);
        buf.writeInt(displays.size());
        for (Map.Entry<Integer, ItemStack> entry : displays.entrySet()) {
            buf.writeInt(entry.getKey());
            buf.writeItemStack(entry.getValue(), false);
        }
    }

    public static RecipeRefreshPacket decode(FriendlyByteBuf buf) {
        ResourceLocation recipeId = buf.readResourceLocation();
        int stage = buf.readInt();
        int size = buf.readInt();
        Map<Integer, ItemStack> displays = new HashMap<>();
        for (int i = 0; i < size; i++) {
            int slot = buf.readInt();
            ItemStack stack = buf.readItem();
            displays.put(slot, stack);
        }
        return new RecipeRefreshPacket(recipeId, displays, stage);
    }

    public static void handlePacket(RecipeRefreshPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientCache.update(packet.recipeId, packet.stage, packet.displays);
        });
        context.setPacketHandled(true);
    }
}
