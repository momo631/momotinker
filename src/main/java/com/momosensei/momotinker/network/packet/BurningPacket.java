package com.momosensei.momotinker.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.momosensei.momotinker.entity.SpearCreate.createBurning;


public class BurningPacket {
    public final int playerID;
    public BurningPacket(int id) {
        this.playerID =id;
    }

    public static void encode(BurningPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.playerID);
    }

    public static BurningPacket decode(FriendlyByteBuf buf) {
        return new BurningPacket(buf.readInt());
    }

    public static void handle(BurningPacket packet, Supplier<NetworkEvent.Context> supplier) {
        if (supplier.get().getDirection().getReceptionSide().isServer()) {
            supplier.get().enqueueWork(() -> {
                ServerPlayer player =supplier.get().getSender();
                if (player !=null&&player.getId()==packet.playerID) {
                    createBurning(player);
                }
            });
        }
        supplier.get().setPacketHandled(true);
    }
}
