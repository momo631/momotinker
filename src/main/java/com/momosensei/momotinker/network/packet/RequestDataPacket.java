package com.momosensei.momotinker.network.packet;

import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.util.RandomRecipeManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

import static com.momosensei.momotinker.Momotinker.getResource;
import static com.momosensei.momotinker.util.RandomRecipeManager.getShortId;

public class RequestDataPacket {
    private final ResourceLocation recipeId;

    public RequestDataPacket(ResourceLocation recipeId) {
        this.recipeId = recipeId;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(recipeId);
    }

    public static RequestDataPacket decode(FriendlyByteBuf buf) {
        return new RequestDataPacket(buf.readResourceLocation());
    }


    public static void handlePacket(RequestDataPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
                Map<Integer, ItemStack> displays = RandomRecipeManager.getFixedDisplay(packet.recipeId);
                int value = tag.getInt(getResource(getShortId(packet.recipeId)).toString());
                Channel.sendToPlayer(new SyncDataPacket(packet.recipeId, value, displays), player);
            }
        });
        context.setPacketHandled(true);
    }

}