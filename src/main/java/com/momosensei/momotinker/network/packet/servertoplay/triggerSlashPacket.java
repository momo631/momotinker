package com.momosensei.momotinker.network.packet.servertoplay;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.momosensei.momotinker.tool.trigger_blade.createSlash;


public class triggerSlashPacket {
    public final int playerID;
    public triggerSlashPacket(int id) {
        this.playerID =id;
    }

    public static void encode(triggerSlashPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.playerID);
    }

    public static triggerSlashPacket decode(FriendlyByteBuf buf) {
        return new triggerSlashPacket(buf.readInt());
    }

    public static void handle(triggerSlashPacket packet, Supplier<NetworkEvent.Context> supplier) {
        if (supplier.get().getDirection().getReceptionSide().isServer()) {
            supplier.get().enqueueWork(() -> {
                ServerPlayer player =supplier.get().getSender();
                if (player !=null&&player.getId()==packet.playerID) {
                    createSlash(player);
                }
            });
        }
        supplier.get().setPacketHandled(true);
    }
}
