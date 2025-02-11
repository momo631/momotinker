package com.momosensei.momotinker.network.packet.servertoplay;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.momosensei.momotinker.mobs.TriggerBladeDrawtime.setPercentage;


public class TriggerBladeCharge {
    private float percentage;
    public TriggerBladeCharge(float percentage) {;
        this.percentage =percentage;
    }
    public TriggerBladeCharge(FriendlyByteBuf buf){
        this.percentage =buf.readFloat();
    }

    public static void encode(TriggerBladeCharge packet, FriendlyByteBuf buf) {
        buf.writeFloat(packet.percentage);
    }

    public static TriggerBladeCharge decode(FriendlyByteBuf buf) {
        return new TriggerBladeCharge(buf.readFloat());
    }

    public static void handle(TriggerBladeCharge packet, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            setPercentage(packet.percentage);
        });
        supplier.get().setPacketHandled(true);
    }
}
