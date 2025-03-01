package com.momosensei.momotinker.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.momosensei.momotinker.entity.SpearCreate.createSpear;


public class SpearEntityPacket {
    public final int playerID;
    public SpearEntityPacket(int id) {
        this.playerID =id;
    }

    public static void encode(SpearEntityPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.playerID);
    }

    public static SpearEntityPacket decode(FriendlyByteBuf buf) {
        return new SpearEntityPacket(buf.readInt());
    }

    public static void handle(SpearEntityPacket packet, Supplier<NetworkEvent.Context> supplier) {
        if (supplier.get().getDirection().getReceptionSide().isServer()) {
            supplier.get().enqueueWork(() -> {
                ServerPlayer player =supplier.get().getSender();
                if (player !=null&&player.getId()==packet.playerID) {
                    createSpear(player);
                }
            });
        }
        supplier.get().setPacketHandled(true);
    }
}
