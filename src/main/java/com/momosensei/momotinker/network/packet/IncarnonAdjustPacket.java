package com.momosensei.momotinker.network.packet;

import com.momosensei.momotinker.Modifiers.modifiers.IncarnonModifiers.IncarnonModifier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.function.Supplier;

public class IncarnonAdjustPacket {
    public final int slot;
    public final int a;
    public final int b;
    public final int c;
    public final int d;

    public IncarnonAdjustPacket(int slot, int a, int b, int c, int d) {
        this.slot = slot;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public IncarnonAdjustPacket(FriendlyByteBuf buf) {
        this.slot = buf.readInt();
        this.a = buf.readInt();
        this.b = buf.readInt();
        this.c = buf.readInt();
        this.d = buf.readInt();
    }

    public void toByte(FriendlyByteBuf buf) {
        buf.writeInt(this.slot);
        buf.writeInt(this.a);
        buf.writeInt(this.b);
        buf.writeInt(this.c);
        buf.writeInt(this.d);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        ServerPlayer player = context.getSender();

        context.enqueueWork(() -> {
            if (player != null && slot >= 0 && slot < player.getInventory().getContainerSize()) {
                ToolStack tool = ToolStack.from(player.getInventory().getItem(slot));
                tool.getPersistentData().putInt(IncarnonModifier.incarnon_a, a);
                tool.getPersistentData().putInt(IncarnonModifier.incarnon_b, b);
                tool.getPersistentData().putInt(IncarnonModifier.incarnon_c, c);
                tool.getPersistentData().putInt(IncarnonModifier.incarnon_d, d);
                tool.rebuildStats();
            }
        });
        return true;
    }
}
