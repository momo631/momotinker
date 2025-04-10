package com.momosensei.momotinker.network.packet;

import com.momosensei.momotinker.mobs.PlayerChargeBoolean;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerCharge {
    private final int id;
    public PlayerCharge(int id) {;
        this.id =id;
    }
    public PlayerCharge(FriendlyByteBuf buf){
        this.id =buf.readInt();
    }

    public void toByte(FriendlyByteBuf buf){
        buf.writeInt(this.id);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            PlayerChargeBoolean.setPlayerChargeBoolean(id);
        });
        supplier.get().setPacketHandled(true);
    }
}
