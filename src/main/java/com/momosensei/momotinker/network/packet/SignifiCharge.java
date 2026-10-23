package com.momosensei.momotinker.network.packet;

import com.momosensei.momotinker.data.SkillState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class SignifiCharge {
    private final float signifidata;
    public SignifiCharge(float signifidata) {;
        this.signifidata =signifidata;
    }
    public SignifiCharge(FriendlyByteBuf buf){
        this.signifidata =buf.readFloat();
    }

    public void toByte(FriendlyByteBuf buf){
        buf.writeFloat(this.signifidata);
    }

    public boolean handle( Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context =supplier.get();
        context.enqueueWork(()->{
            SkillState.Signifidata.setSignifidata(signifidata);
        });
        return true;
    }
}
