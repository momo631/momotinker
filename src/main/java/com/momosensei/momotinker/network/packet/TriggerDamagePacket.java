package com.momosensei.momotinker.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TriggerDamagePacket {
    public int a;

    public TriggerDamagePacket(){
    }

    public TriggerDamagePacket(int a){
        this.a = a;
    }

    public static void encode(TriggerDamagePacket a, FriendlyByteBuf buf){
        buf.writeInt(a.a);
    }

    public static TriggerDamagePacket decode(FriendlyByteBuf buf){
        return new TriggerDamagePacket(buf.readInt());
    }

    public static void handlePacket(TriggerDamagePacket pkt, Supplier<NetworkEvent.Context> context$) {
        NetworkEvent.Context context = context$.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                player.hurt(player.damageSources().fellOutOfWorld(), 1.0f);
            }
        });
        context.setPacketHandled(true);
    }
}
