package com.momosensei.momotinker.network.packet.HudCharge;

import com.momosensei.momotinker.gui.hudhelder.NyarlathotepDrawTime;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class NyarlathotepChargingCharge {
    private float phasePercentage;
    private float progressPercentage;
    public NyarlathotepChargingCharge(float phasePercentage, float progressPercentage) {
        this.phasePercentage = phasePercentage;
        this.progressPercentage = progressPercentage;
    }

    public NyarlathotepChargingCharge(FriendlyByteBuf buf) {
        this.phasePercentage = buf.readFloat();
        this.progressPercentage = buf.readFloat();
    }

    public static void encode(NyarlathotepChargingCharge packet, FriendlyByteBuf buf) {
        buf.writeFloat(packet.phasePercentage);
        buf.writeFloat(packet.progressPercentage);
    }

    public static NyarlathotepChargingCharge decode(FriendlyByteBuf buf) {
        return new NyarlathotepChargingCharge(buf.readFloat(), buf.readFloat());
    }

    public static void handle(NyarlathotepChargingCharge packet, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            NyarlathotepDrawTime.setChargingPhasePercentage(packet.phasePercentage);
            NyarlathotepDrawTime.setChargingProgressPercentage(packet.progressPercentage);
        });
        supplier.get().setPacketHandled(true);
    }
}
