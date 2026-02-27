package com.momosensei.momotinker.network.packet;

import com.momosensei.momotinker.menu.IncarnonMenu;
import com.momosensei.momotinker.register.MomotinkerMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.Map;
import java.util.function.Supplier;

public class IncarnonOpenMenuPacket {
    public final int slot;
    public final String catalogue;
    private final Map<String, Component> hoverTexts;
    public final Component title;

    public IncarnonOpenMenuPacket(int slot, String catalogue, Map<String, Component> hoverTexts, Component title) {
        this.slot = slot;
        this.catalogue = catalogue;
        this.hoverTexts = hoverTexts;
        this.title = title;
    }
    public IncarnonOpenMenuPacket(FriendlyByteBuf buf){
        this.slot = buf.readInt();
        this.catalogue = buf.readUtf();
        this.hoverTexts = buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readComponent);
        this.title = buf.readComponent();
    }
    public void toByte(FriendlyByteBuf buf){
        buf.writeInt(this.slot);
        buf.writeUtf(this.catalogue);
        buf.writeMap(this.hoverTexts, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeComponent );
        buf.writeComponent(this.title);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        ServerPlayer player = context.getSender();
        context.enqueueWork(()->{
            if (player!=null) {
                ItemStack stack = player.getInventory().getItem(this.slot);
                NetworkHooks.openScreen(player,
                        new SimpleMenuProvider(
                                (id,inventory,p)->new IncarnonMenu(MomotinkerMenus.Incarnon_menu.get(),inventory,id,stack,slot,catalogue,hoverTexts,title),
                                this.title),
                        buf ->{
                            buf.writeVarInt(slot);
                            buf.writeUtf(catalogue);
                            buf.writeMap(hoverTexts, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeComponent);
                            buf.writeComponent(title);
                        }
                );
            }
        });
        return true;
    }
}
