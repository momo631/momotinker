package com.momosensei.momotinker.network.packet;

import com.momosensei.momotinker.data.gungnir.GungnirTrajectoryClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GungnirTrajectoryPacket {
    private final Vec3 from;
    private final Vec3 to;
    private final float duration;

    public GungnirTrajectoryPacket(Vec3 from, Vec3 to, float duration) {
        this.from = from;
        this.to = to;
        this.duration = duration;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(from.x); buf.writeDouble(from.y); buf.writeDouble(from.z);
        buf.writeDouble(to.x);   buf.writeDouble(to.y);   buf.writeDouble(to.z);
        buf.writeFloat(duration);
    }

    public static GungnirTrajectoryPacket decode(FriendlyByteBuf buf) {
        return new GungnirTrajectoryPacket(
                new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()),
                new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()),
                buf.readFloat()
        );
    }

    public static void handlePacket(GungnirTrajectoryPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        GungnirTrajectoryClient.add(msg.from, msg.to, msg.duration))
        );
        ctx.get().setPacketHandled(true);
    }
}