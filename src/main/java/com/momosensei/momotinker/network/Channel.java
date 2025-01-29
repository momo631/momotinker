package com.momosensei.momotinker.network;

import com.momosensei.momotinker.network.packet.servertoplay.KeyInputPKT;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class Channel {

    private static SimpleChannel INSTACNE;

    private static int packetId=0;

    private static int id(){
        return packetId++;
    }
    public static void register(){
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation("momotinker","messages"))
                .networkProtocolVersion(()->"1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();
        INSTACNE = net;

        net.messageBuilder(KeyInputPKT.class,id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(KeyInputPKT::decode)
                .encoder(KeyInputPKT::encode)
                .consumerMainThread(KeyInputPKT::handlePacket)
                .add();
    }
    public static <MSG> void sendToServer(MSG message){
        INSTACNE.sendToServer(message);
    }
}
