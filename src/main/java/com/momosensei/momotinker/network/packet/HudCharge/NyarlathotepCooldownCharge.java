package com.momosensei.momotinker.network.packet.HudCharge;

import com.momosensei.momotinker.gui.hudhelder.NyarlathotepDrawTime;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class NyarlathotepCooldownCharge {
    private float percentage;
    public NyarlathotepCooldownCharge(float percentage) {;
        this.percentage =percentage;
    }
    public NyarlathotepCooldownCharge(FriendlyByteBuf buf){
        this.percentage =buf.readFloat();
    }

    public static void encode(NyarlathotepCooldownCharge packet, FriendlyByteBuf buf) {
        buf.writeFloat(packet.percentage);
    }

    public static NyarlathotepCooldownCharge decode(FriendlyByteBuf buf) {
        return new NyarlathotepCooldownCharge(buf.readFloat());
    }

    public static void handle(NyarlathotepCooldownCharge packet, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            NyarlathotepDrawTime.setCooldownPercentage(packet.percentage);
        });
        supplier.get().setPacketHandled(true);
    }
}
