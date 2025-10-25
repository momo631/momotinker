package com.momosensei.momotinker.network.packet.HudCharge;

import com.momosensei.momotinker.gui.hudhelder.LegionDrawTime;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class LegionCooldownCharge {
    private float percentage;
    public LegionCooldownCharge(float percentage) {;
        this.percentage =percentage;
    }
    public LegionCooldownCharge(FriendlyByteBuf buf){
        this.percentage =buf.readFloat();
    }

    public static void encode(LegionCooldownCharge packet, FriendlyByteBuf buf) {
        buf.writeFloat(packet.percentage);
    }

    public static LegionCooldownCharge decode(FriendlyByteBuf buf) {
        return new LegionCooldownCharge(buf.readFloat());
    }

    public static void handle(LegionCooldownCharge packet, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            LegionDrawTime.setCooldownPercentage(packet.percentage);
        });
        supplier.get().setPacketHandled(true);
    }
}
