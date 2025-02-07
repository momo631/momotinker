package com.momosensei.momotinker.network;

import com.momosensei.momotinker.network.packet.servertoplay.KeyInputPKT;
import com.momosensei.momotinker.network.packet.servertoplay.triggerSlashPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import static com.momosensei.momotinker.Momotinker.MOD_ID;

public class Channel {


    static int id = 0;
    private static int packetId=0;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, "tinker_packet"), () -> "1", "1"::equals, "1"::equals);

    private static int id(){
        return packetId++;
    }

    public static void init() {
        INSTANCE.registerMessage(id++, triggerSlashPacket.class, triggerSlashPacket::encode, triggerSlashPacket::decode, triggerSlashPacket::handle);
        INSTANCE.messageBuilder(KeyInputPKT.class,id(), NetworkDirection.PLAY_TO_SERVER).decoder(KeyInputPKT::decode).encoder(KeyInputPKT::encode).consumerMainThread(KeyInputPKT::handlePacket).add();
    }

    public static <MSG> void sendToServer(MSG msg){
        INSTANCE.sendToServer(msg);
    }

    public static <MSG> void sendToPlayer(MSG msg, ServerPlayer player){
        INSTANCE.send(PacketDistributor.PLAYER.with(()->player),msg);
    }
}
