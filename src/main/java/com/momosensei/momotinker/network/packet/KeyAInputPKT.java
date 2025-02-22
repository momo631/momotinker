package com.momosensei.momotinker.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class KeyAInputPKT {
    public int key;

    public KeyAInputPKT(){
    }

    public KeyAInputPKT(int key){
        this.key = key;
    }

    public static void encode(KeyAInputPKT pkt, FriendlyByteBuf buf){
        buf.writeInt(pkt.key);
    }

    public static KeyAInputPKT decode(FriendlyByteBuf buf){
        return new KeyAInputPKT(buf.readInt());
    }

    public static void handlePacket(KeyAInputPKT pkt, Supplier<NetworkEvent.Context> context$) {
        NetworkEvent.Context context = context$.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();

        });
        context.setPacketHandled(true);
    }
}
