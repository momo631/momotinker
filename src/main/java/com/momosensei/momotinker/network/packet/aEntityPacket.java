package com.momosensei.momotinker.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.momosensei.momotinker.entity.SpearCreate.createRayExplosion;


public class aEntityPacket {
    public final int playerID;
    public aEntityPacket(int id) {
        this.playerID =id;
    }

    public static void encode(aEntityPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.playerID);
    }

    public static aEntityPacket decode(FriendlyByteBuf buf) {
        return new aEntityPacket(buf.readInt());
    }

    public static void handle(aEntityPacket packet, Supplier<NetworkEvent.Context> supplier) {
        if (supplier.get().getDirection().getReceptionSide().isServer()) {
            supplier.get().enqueueWork(() -> {
                ServerPlayer player =supplier.get().getSender();
                if (player !=null&&player.getId()==packet.playerID) {
                    createRayExplosion(player);
                }
            });
        }
        supplier.get().setPacketHandled(true);
    }
}
