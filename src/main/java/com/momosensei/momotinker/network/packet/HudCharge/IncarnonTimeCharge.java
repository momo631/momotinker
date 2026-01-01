package com.momosensei.momotinker.network.packet.HudCharge;

import com.momosensei.momotinker.gui.hudhelder.IncarnonDrawTime;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class IncarnonTimeCharge {
    private float percentage;
    public IncarnonTimeCharge(float percentage) {;
        this.percentage =percentage;
    }
    public IncarnonTimeCharge(FriendlyByteBuf buf){
        this.percentage =buf.readFloat();
    }

    public static void encode(IncarnonTimeCharge packet, FriendlyByteBuf buf) {
        buf.writeFloat(packet.percentage);
    }

    public static IncarnonTimeCharge decode(FriendlyByteBuf buf) {
        return new IncarnonTimeCharge(buf.readFloat());
    }

    public static void handle(IncarnonTimeCharge packet, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            IncarnonDrawTime.setPercentage(packet.percentage);
        });
        supplier.get().setPacketHandled(true);
    }
}
