package com.momosensei.momotinker.network.packet;

import com.momosensei.momotinker.mobs.CoolTimeB;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class CoolTimeChargeB {
    private final int cooltime;
    public CoolTimeChargeB(int cooltime) {;
        this.cooltime =cooltime;
    }
    public CoolTimeChargeB(FriendlyByteBuf buf){
        this.cooltime =buf.readInt();
    }

    public void toByte(FriendlyByteBuf buf){
        buf.writeInt(this.cooltime);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            CoolTimeB.setCoolTime(cooltime);
        });
        supplier.get().setPacketHandled(true);
    }
}
