package com.momosensei.momotinker.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.momosensei.momotinker.tool.legion.createBox;
import static com.momosensei.momotinker.util.AttackUtil.getCooldownFunctionFloat;


public class BoxPacket {
    public final int playerID;
    public BoxPacket(int id) {
        this.playerID =id;
    }

    public static void encode(BoxPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.playerID);
    }

    public static BoxPacket decode(FriendlyByteBuf buf) {
        return new BoxPacket(buf.readInt());
    }

    public static void handlePacket(BoxPacket packet, Supplier<NetworkEvent.Context> supplier) {
        if (supplier.get().getDirection().getReceptionSide().isServer()) {
            supplier.get().enqueueWork(() -> {
                ServerPlayer player =supplier.get().getSender();
                if (player !=null&&player.getId()==packet.playerID) {
                    float d = getCooldownFunctionFloat(player, InteractionHand.MAIN_HAND);
                    if (d >= 0.9f) {
                        createBox(player, 1, 0);
                    }
                }
            });
        }
        supplier.get().setPacketHandled(true);
    }
}
