package com.momosensei.momotinker.network;

import com.momosensei.momotinker.network.packet.*;
import com.momosensei.momotinker.network.packet.HudCharge.CensoredCharge;
import com.momosensei.momotinker.network.packet.HudCharge.LegionChargingCharge;
import com.momosensei.momotinker.network.packet.HudCharge.LegionCooldownCharge;
import com.momosensei.momotinker.network.packet.HudCharge.ToolsTimeCharge;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.momosensei.momotinker.Momotinker.MOD_ID;

public class Channel {
    static int id = 0;
    private static int packetId=0;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, "tinker_packet"), () -> "1", "1"::equals, "1"::equals);

    private static int id(){
        return packetId++;
    }

    public static void init() {

        INSTANCE.messageBuilder(KeyInputPKT.class,id++, NetworkDirection.PLAY_TO_SERVER).decoder(KeyInputPKT::decode).encoder(KeyInputPKT::encode).consumerMainThread(KeyInputPKT::handlePacket).add();
        INSTANCE.messageBuilder(KeyAInputPKT.class,id++, NetworkDirection.PLAY_TO_SERVER).decoder(KeyAInputPKT::decode).encoder(KeyAInputPKT::encode).consumerMainThread(KeyAInputPKT::handlePacket).add();
        INSTANCE.messageBuilder(ItemStackPKT.class,id++, NetworkDirection.PLAY_TO_SERVER).decoder(ItemStackPKT::decode).encoder(ItemStackPKT::encode).consumerMainThread(ItemStackPKT::handlePacket).add();
        INSTANCE.messageBuilder(LegionPacket.class, id++, NetworkDirection.PLAY_TO_SERVER).decoder(LegionPacket::decode).encoder(LegionPacket::encode).consumerMainThread(LegionPacket::handlePacket).add();

        INSTANCE.messageBuilder(ToolsTimeCharge.class,id++, NetworkDirection.PLAY_TO_CLIENT).decoder(ToolsTimeCharge::new).encoder(ToolsTimeCharge::encode).consumerMainThread(ToolsTimeCharge::handle).add();
        INSTANCE.messageBuilder(CensoredCharge.class,id++, NetworkDirection.PLAY_TO_CLIENT).decoder(CensoredCharge::new).encoder(CensoredCharge::toByte).consumerMainThread(CensoredCharge::handle).add();
        INSTANCE.messageBuilder(SignifiCharge.class,id++, NetworkDirection.PLAY_TO_CLIENT).decoder(SignifiCharge::new).encoder(SignifiCharge::toByte).consumerMainThread(SignifiCharge::handle).add();

        INSTANCE.messageBuilder(LegionChargingCharge.class,id++, NetworkDirection.PLAY_TO_CLIENT).decoder(LegionChargingCharge::new).encoder(LegionChargingCharge::encode).consumerMainThread(LegionChargingCharge::handle).add();
        INSTANCE.messageBuilder(LegionCooldownCharge.class,id++, NetworkDirection.PLAY_TO_CLIENT).decoder(LegionCooldownCharge::new).encoder(LegionCooldownCharge::encode).consumerMainThread(LegionCooldownCharge::handle).add();

        INSTANCE.messageBuilder(CoolTimeChargeA.class,id++, NetworkDirection.PLAY_TO_CLIENT).decoder(CoolTimeChargeA::new).encoder(CoolTimeChargeA::toByte).consumerMainThread(CoolTimeChargeA::handle).add();
        INSTANCE.messageBuilder(CoolTimeChargeB.class,id++, NetworkDirection.PLAY_TO_CLIENT).decoder(CoolTimeChargeB::new).encoder(CoolTimeChargeB::toByte).consumerMainThread(CoolTimeChargeB::handle).add();
        INSTANCE.messageBuilder(CoolTimeChargeC.class,id++, NetworkDirection.PLAY_TO_CLIENT).decoder(CoolTimeChargeC::new).encoder(CoolTimeChargeC::toByte).consumerMainThread(CoolTimeChargeC::handle).add();
        INSTANCE.messageBuilder(StageMeteorCharge.class,id++, NetworkDirection.PLAY_TO_CLIENT).decoder(StageMeteorCharge::new).encoder(StageMeteorCharge::toByte).consumerMainThread(StageMeteorCharge::handle).add();

    }

    public static <MSG> void sendToServer(MSG msg){
        INSTANCE.sendToServer(msg);
    }

    public static <MSG> void sendToPlayer(MSG msg, ServerPlayer player){
        INSTANCE.send(PacketDistributor.PLAYER.with(()->player),msg);
    }
    public static <MSG> void sendToClient(MSG msg){
        INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
    }

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, MOD_ID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    private static int messageID = 0;
    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
        messageID++;
    }

    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public static void queueServerWork(int tick, Runnable action) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
            workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
    }

    @SubscribeEvent
    public void tick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
            workQueue.forEach(work -> {
                work.setValue(work.getValue() - 1);
                if (work.getValue() == 0)
                    actions.add(work);
            });
            actions.forEach(e -> e.getKey().run());
            workQueue.removeAll(actions);
        }
    }
}
