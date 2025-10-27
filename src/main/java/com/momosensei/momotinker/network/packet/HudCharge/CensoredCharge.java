package com.momosensei.momotinker.network.packet.HudCharge;

import com.momosensei.momotinker.gui.hudhelder.CensoredDataTime;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class CensoredCharge {
    private final float censoreddata;
    public CensoredCharge(float censoreddata) {;
        this.censoreddata =censoreddata;
    }
    public CensoredCharge(FriendlyByteBuf buf){
        this.censoreddata =buf.readFloat();
    }

    public void toByte(FriendlyByteBuf buf){
        buf.writeFloat(this.censoreddata);
    }

    public boolean handle( Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context =supplier.get();
        context.enqueueWork(()->{
            CensoredDataTime.setCensoreddata(censoreddata);
        });
        return true;
    }
}
