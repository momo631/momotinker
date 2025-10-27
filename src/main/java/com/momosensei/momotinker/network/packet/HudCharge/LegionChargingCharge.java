package com.momosensei.momotinker.network.packet.HudCharge;

import com.momosensei.momotinker.gui.hudhelder.LegionDrawTime;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class LegionChargingCharge {
    private float phasePercentage;
    private float progressPercentage;
    public LegionChargingCharge(float phasePercentage, float progressPercentage) {
        this.phasePercentage = phasePercentage;
        this.progressPercentage = progressPercentage;
    }

    public LegionChargingCharge(FriendlyByteBuf buf) {
        this.phasePercentage = buf.readFloat();
        this.progressPercentage = buf.readFloat();
    }

    public static void encode(LegionChargingCharge packet, FriendlyByteBuf buf) {
        buf.writeFloat(packet.phasePercentage);
        buf.writeFloat(packet.progressPercentage);
    }

    public static LegionChargingCharge decode(FriendlyByteBuf buf) {
        return new LegionChargingCharge(buf.readFloat(), buf.readFloat());
    }

    public static void handle(LegionChargingCharge packet, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            LegionDrawTime.setChargingPhasePercentage(packet.phasePercentage);
            LegionDrawTime.setChargingProgressPercentage(packet.progressPercentage);
        });
        supplier.get().setPacketHandled(true);
    }
}
