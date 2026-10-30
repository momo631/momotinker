package com.momosensei.momotinker.network;

import com.momosensei.momotinker.network.packet.*;
import com.momosensei.momotinker.network.packet.HudCharge.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import static com.momosensei.momotinker.Momotinker.MOD_ID;

public class Channel {
    static int id = 0;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, "tinker_packet"), () -> "1", "1"::equals, "1"::equals);

    public static void init() {

        INSTANCE.messageBuilder(KeyInputPKT.class,id++, NetworkDirection.PLAY_TO_SERVER).decoder(KeyInputPKT::decode).encoder(KeyInputPKT::encode).consumerMainThread(KeyInputPKT::handlePacket).add();
        INSTANCE.messageBuilder(KeyAInputPKT.class,id++, NetworkDirection.PLAY_TO_SERVER).decoder(KeyAInputPKT::decode).encoder(KeyAInputPKT::encode).consumerMainThread(KeyAInputPKT::handlePacket).add();
        INSTANCE.messageBuilder(TriggerDamagePacket.class,id++, NetworkDirection.PLAY_TO_SERVER).decoder(TriggerDamagePacket::decode).encoder(TriggerDamagePacket::encode).consumerMainThread(TriggerDamagePacket::handlePacket).add();
        INSTANCE.messageBuilder(ItemStackPKT.class,id++, NetworkDirection.PLAY_TO_SERVER).decoder(ItemStackPKT::decode).encoder(ItemStackPKT::encode).consumerMainThread(ItemStackPKT::handlePacket).add();
        INSTANCE.messageBuilder(NyarlathotepPacket.class, id++, NetworkDirection.PLAY_TO_SERVER).decoder(NyarlathotepPacket::decode).encoder(NyarlathotepPacket::encode).consumerMainThread(NyarlathotepPacket::handlePacket).add();
        INSTANCE.messageBuilder(RequestDataPacket.class, id++, NetworkDirection.PLAY_TO_SERVER).decoder(RequestDataPacket::decode).encoder(RequestDataPacket::encode).consumerMainThread(RequestDataPacket::handlePacket).add();

        INSTANCE.messageBuilder(RandomRecipeStagePacket.class, id++, NetworkDirection.PLAY_TO_CLIENT).decoder(RandomRecipeStagePacket::decode).encoder(RandomRecipeStagePacket::encode).consumerMainThread(RandomRecipeStagePacket::handlePacket).add();
        INSTANCE.messageBuilder(RecipeRefreshPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT).decoder(RecipeRefreshPacket::decode).encoder(RecipeRefreshPacket::encode).consumerMainThread(RecipeRefreshPacket::handlePacket).add();
        INSTANCE.messageBuilder(GungnirItemPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT).decoder(GungnirItemPacket::decode).encoder(GungnirItemPacket::encode).consumerMainThread(GungnirItemPacket::handlePacket).add();
        INSTANCE.messageBuilder(GungnirTrajectoryPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT).decoder(GungnirTrajectoryPacket::decode).encoder(GungnirTrajectoryPacket::encode).consumerMainThread(GungnirTrajectoryPacket::handlePacket).add();

        INSTANCE.messageBuilder(ToolsTimeCharge.class,id++, NetworkDirection.PLAY_TO_CLIENT).decoder(ToolsTimeCharge::new).encoder(ToolsTimeCharge::toByte).consumerMainThread(ToolsTimeCharge::handle).add();
        INSTANCE.messageBuilder(CensoredCharge.class,id++, NetworkDirection.PLAY_TO_CLIENT).decoder(CensoredCharge::new).encoder(CensoredCharge::toByte).consumerMainThread(CensoredCharge::handle).add();
        INSTANCE.messageBuilder(SignifiCharge.class,id++, NetworkDirection.PLAY_TO_CLIENT).decoder(SignifiCharge::new).encoder(SignifiCharge::toByte).consumerMainThread(SignifiCharge::handle).add();
        INSTANCE.messageBuilder(NyarlathotepChargingCharge.class,id++, NetworkDirection.PLAY_TO_CLIENT).decoder(NyarlathotepChargingCharge::new).encoder(NyarlathotepChargingCharge::toByte).consumerMainThread(NyarlathotepChargingCharge::handle).add();
        INSTANCE.messageBuilder(NyarlathotepCooldownCharge.class,id++, NetworkDirection.PLAY_TO_CLIENT).decoder(NyarlathotepCooldownCharge::new).encoder(NyarlathotepCooldownCharge::toByte).consumerMainThread(NyarlathotepCooldownCharge::handle).add();
        INSTANCE.messageBuilder(IncarnonTimeCharge.class,id++, NetworkDirection.PLAY_TO_CLIENT).decoder(IncarnonTimeCharge::new).encoder(IncarnonTimeCharge::toByte).consumerMainThread(IncarnonTimeCharge::handle).add();

        INSTANCE.messageBuilder(CoolTimeChargeA.class,id++, NetworkDirection.PLAY_TO_CLIENT).decoder(CoolTimeChargeA::new).encoder(CoolTimeChargeA::toByte).consumerMainThread(CoolTimeChargeA::handle).add();
        INSTANCE.messageBuilder(CoolTimeChargeB.class,id++, NetworkDirection.PLAY_TO_CLIENT).decoder(CoolTimeChargeB::new).encoder(CoolTimeChargeB::toByte).consumerMainThread(CoolTimeChargeB::handle).add();
        INSTANCE.messageBuilder(CoolTimeChargeC.class,id++, NetworkDirection.PLAY_TO_CLIENT).decoder(CoolTimeChargeC::new).encoder(CoolTimeChargeC::toByte).consumerMainThread(CoolTimeChargeC::handle).add();
        INSTANCE.messageBuilder(StageMeteorCharge.class,id++, NetworkDirection.PLAY_TO_CLIENT).decoder(StageMeteorCharge::new).encoder(StageMeteorCharge::toByte).consumerMainThread(StageMeteorCharge::handle).add();
//        INSTANCE.messageBuilder(SUpdateTinkerStationRecipePacket.class,id++, NetworkDirection.PLAY_TO_CLIENT).decoder(SUpdateTinkerStationRecipePacket::new).encoder(SUpdateTinkerStationRecipePacket::encode).consumerMainThread(SUpdateTinkerStationRecipePacket::handle).add();

        INSTANCE.messageBuilder(IncarnonAdjustPacket.class, id++, NetworkDirection.PLAY_TO_SERVER).decoder(IncarnonAdjustPacket::new).encoder(IncarnonAdjustPacket::toByte).consumerMainThread(IncarnonAdjustPacket::handle).add();
        INSTANCE.messageBuilder(IncarnonOpenMenuPacket.class, id++, NetworkDirection.PLAY_TO_SERVER).decoder(IncarnonOpenMenuPacket::new).encoder(IncarnonOpenMenuPacket::toByte).consumerMainThread(IncarnonOpenMenuPacket::handle).add();
//        INSTANCE.messageBuilder(STinkerStationRenamePacket.class,id++, NetworkDirection.PLAY_TO_SERVER).decoder(STinkerStationRenamePacket::new).encoder(STinkerStationRenamePacket::encode).consumerMainThread(STinkerStationRenamePacket::handle).add();
//        INSTANCE.messageBuilder(STinkerStationSelectionPacket.class,id++, NetworkDirection.PLAY_TO_SERVER).decoder(STinkerStationSelectionPacket::new).encoder(STinkerStationSelectionPacket::encode).consumerMainThread(STinkerStationSelectionPacket::handle).add();
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
}
