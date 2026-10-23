package com.momosensei.momotinker.network.packet;

import com.momosensei.momotinker.data.SkillState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class CoolTimeChargeC {
    private final int cooltime;
    public CoolTimeChargeC(int cooltime) {;
        this.cooltime =cooltime;
    }
    public CoolTimeChargeC(FriendlyByteBuf buf){
        this.cooltime =buf.readInt();
    }

    public void toByte(FriendlyByteBuf buf){
        buf.writeInt(this.cooltime);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            SkillState.C.setCoolTime(cooltime);
        });
        supplier.get().setPacketHandled(true);
    }
}
