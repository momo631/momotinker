package com.momosensei.momotinker.network.packet;

import com.momosensei.momotinker.mobs.CoolTimeA;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class CoolTimeChargeA {
    private final int cooltime;
    public CoolTimeChargeA(int cooltime) {;
        this.cooltime =cooltime;
    }
    public CoolTimeChargeA(FriendlyByteBuf buf){
        this.cooltime =buf.readInt();
    }

    public void toByte(FriendlyByteBuf buf){
        buf.writeInt(this.cooltime);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            CoolTimeA.setCoolTime(cooltime);
        });
        supplier.get().setPacketHandled(true);
    }
}
