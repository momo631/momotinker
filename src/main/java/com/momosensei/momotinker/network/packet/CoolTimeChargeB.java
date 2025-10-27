package com.momosensei.momotinker.network.packet;

import com.momosensei.momotinker.mobs.CoolTimeB;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class CoolTimeChargeB {
    private final int cooltime;
    private final boolean owner;
    public CoolTimeChargeB(int cooltime,boolean owner) {;
        this.cooltime =cooltime;
        this.owner =owner;
    }
    public CoolTimeChargeB(FriendlyByteBuf buf){
        this.cooltime =buf.readInt();
        this.owner =buf.readBoolean();
    }

    public void toByte(FriendlyByteBuf buf){
        buf.writeInt(this.cooltime);
        buf.writeBoolean(this.owner);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            CoolTimeB.setCoolTime(cooltime);
            CoolTimeB.setOwner(owner);
        });
        supplier.get().setPacketHandled(true);
    }
}
