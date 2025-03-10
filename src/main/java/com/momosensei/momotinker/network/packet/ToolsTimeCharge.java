package com.momosensei.momotinker.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.momosensei.momotinker.mobs.ToolsTimeDrawtime.setPercentage;


public class ToolsTimeCharge {
    private float percentage;
    public ToolsTimeCharge(float percentage) {;
        this.percentage =percentage;
    }
    public ToolsTimeCharge(FriendlyByteBuf buf){
        this.percentage =buf.readFloat();
    }

    public static void encode(ToolsTimeCharge packet, FriendlyByteBuf buf) {
        buf.writeFloat(packet.percentage);
    }

    public static ToolsTimeCharge decode(FriendlyByteBuf buf) {
        return new ToolsTimeCharge(buf.readFloat());
    }

    public static void handle(ToolsTimeCharge packet, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            setPercentage(packet.percentage);
        });
        supplier.get().setPacketHandled(true);
    }
}
