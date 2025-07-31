package com.momosensei.momotinker.network.packet;

import com.momosensei.momotinker.mobs.StageMeteor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class StageMeteorCharge {
    private final float stagefloat;
    public StageMeteorCharge(float stagefloat) {;
        this.stagefloat =stagefloat;
    }
    public StageMeteorCharge(FriendlyByteBuf buf){
        this.stagefloat =buf.readFloat();
    }

    public void toByte(FriendlyByteBuf buf){
        buf.writeFloat(this.stagefloat);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            StageMeteor.setStageFloat(stagefloat);
        });
        supplier.get().setPacketHandled(true);
    }
}
