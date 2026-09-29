package com.momosensei.momotinker.test.testc;
/*
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;
import slimeknights.mantle.network.packet.IThreadsafePacket;

public class STinkerStationRenamePacket implements IThreadsafePacket {
  private final String name;
  public STinkerStationRenamePacket(String string) {
    this.name = string;
  }
  public STinkerStationRenamePacket(FriendlyByteBuf buf) {
    this.name = buf.readUtf(Short.MAX_VALUE);
  }

  @Override
  public void encode(FriendlyByteBuf buf) {
    buf.writeUtf(name);
  }

  @Override
  public void handleThreadsafe(Context context) {
    ServerPlayer sender = context.getSender();
    if (sender != null && sender.containerMenu instanceof STinkerStationContainerMenu station) {
      STinkerStationBlockEntity tile = station.getTile();
      if (tile != null) {
        station.getTile().setItemName(name);
      }
    }
  }
}

 */
