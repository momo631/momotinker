package com.momosensei.momotinker.network.packet;

import com.momosensei.momotinker.mobs.CoolTimeC;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class CoolTimeChargeC {
    private final float cooltime;
    public CoolTimeChargeC(float cooltime) {;
        this.cooltime =cooltime;
    }
    public CoolTimeChargeC(FriendlyByteBuf buf){
        this.cooltime =buf.readFloat();
    }

    public void toByte(FriendlyByteBuf buf){
        buf.writeFloat(this.cooltime);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            CoolTimeC.setCoolTime(cooltime);
        });
        supplier.get().setPacketHandled(true);
    }
}
