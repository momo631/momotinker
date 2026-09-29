package com.momosensei.momotinker.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.momosensei.momotinker.Items.tool.nyarlathotep.createNyarlathotep;
import static com.momosensei.momotinker.util.AttackUtil.getCooldownFunctionFloat;


public class NyarlathotepPacket {
    public final int playerID;
    public NyarlathotepPacket(int id) {
        this.playerID =id;
    }

    public static void encode(NyarlathotepPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.playerID);
    }

    public static NyarlathotepPacket decode(FriendlyByteBuf buf) {
        return new NyarlathotepPacket(buf.readInt());
    }

    public static void handlePacket(NyarlathotepPacket packet, Supplier<NetworkEvent.Context> supplier) {
        if (supplier.get().getDirection().getReceptionSide().isServer()) {
            supplier.get().enqueueWork(() -> {
                ServerPlayer player =supplier.get().getSender();
                if (player !=null&&player.getId()==packet.playerID) {
                    float d = getCooldownFunctionFloat(player, InteractionHand.MAIN_HAND);
                    if (d >= 0.9f) {
                        createNyarlathotep(player, 1, 0);
                    }
                }
            });
        }
        supplier.get().setPacketHandled(true);
    }
}
