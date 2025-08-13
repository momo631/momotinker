package com.momosensei.momotinker.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ItemStackPKT {
    public ItemStack itemStack;

    public ItemStackPKT(ItemStack itemStack){
        this.itemStack = itemStack;
    }

    public static void encode(ItemStackPKT pkt, FriendlyByteBuf buf){
        buf.writeItemStack(pkt.itemStack,true);
    }

    public static ItemStackPKT decode(FriendlyByteBuf buf){
        return new ItemStackPKT(buf.readItem());
    }

    public static void handlePacket(ItemStackPKT pkt, Supplier<NetworkEvent.Context> context$) {
        NetworkEvent.Context context = context$.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player!=null) {
                ItemHandlerHelper.giveItemToPlayer(player,pkt.itemStack);
            }
        });
        context.setPacketHandled(true);
    }
}
