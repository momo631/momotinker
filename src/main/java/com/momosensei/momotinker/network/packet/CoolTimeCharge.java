package com.momosensei.momotinker.network.packet;

import com.momosensei.momotinker.mobs.CoolTime;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class CoolTimeCharge {
    private final int cooltime;
    public CoolTimeCharge(int cooltime) {;
        this.cooltime =cooltime;
    }
    public CoolTimeCharge(FriendlyByteBuf buf){
        this.cooltime =buf.readInt();
    }

    public void toByte(FriendlyByteBuf buf){
        buf.writeInt(this.cooltime);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            CoolTime.setCoolTime(cooltime);
        });
        supplier.get().setPacketHandled(true);
    }
}
