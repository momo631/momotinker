package com.momosensei.momotinker.network.packet;

import com.momosensei.momotinker.register.MomotinkerToolDefinitions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.function.Supplier;

import static com.momosensei.momotinker.register.MomotinkerTools.*;

public class KeyAInputPKT {
    public int key;

    public KeyAInputPKT(){
    }

    public KeyAInputPKT(int key){
        this.key = key;
    }

    public static void encode(KeyAInputPKT pkt, FriendlyByteBuf buf){
        buf.writeInt(pkt.key);
    }

    public static KeyAInputPKT decode(FriendlyByteBuf buf){
        return new KeyAInputPKT(buf.readInt());
    }

    public static void handlePacket(KeyAInputPKT pkt, Supplier<NetworkEvent.Context> context$) {
        NetworkEvent.Context context = context$.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player!=null&&player.getMainHandItem().is(entropy_burning_cube.get())){
                ItemStack stack = player.getMainHandItem();
                ToolStack tool = ToolStack.from(stack);
                ItemStack itemStack = ToolStack.createTool(entropy_burning_sword.get(), MomotinkerToolDefinitions.ENTROPY_BURNING_SWORD,tool.getMaterials()).createStack();
                ToolStack tools = ToolStack.from(itemStack);
                itemStack.setTag(stack.getTag());
                tools.setUpgrades(tool.getUpgrades());
                tools.setDamage(tool.getDamage());
                tools.getPersistentData().copyFrom(tool.getPersistentData().getCopy());
                player.setItemInHand(InteractionHand.MAIN_HAND, tools.createStack());
            }else
            if (player!=null&&player.getMainHandItem().is(entropy_burning_sword.get())){
                ItemStack stack = player.getMainHandItem();
                ToolStack tool = ToolStack.from(stack);
                ItemStack itemStack = ToolStack.createTool(entropy_burning_riding_spear.get(), MomotinkerToolDefinitions.ENTROPY_BURNING_RIDING_SPEAR,tool.getMaterials()).createStack();
                ToolStack tools = ToolStack.from(itemStack);
                itemStack.setTag(stack.getTag());
                tools.setUpgrades(tool.getUpgrades());
                tools.setDamage(tool.getDamage());
                tools.getPersistentData().copyFrom(tool.getPersistentData().getCopy());
                player.setItemInHand(InteractionHand.MAIN_HAND, tools.createStack());
            }else
            if (player!=null&&player.getMainHandItem().is(entropy_burning_riding_spear.get())){
                ItemStack stack = player.getMainHandItem();
                ToolStack tool = ToolStack.from(stack);
                ItemStack itemStack = ToolStack.createTool(entropy_burning_cannon.get(), MomotinkerToolDefinitions.ENTROPY_BURNING_CANNON,tool.getMaterials()).createStack();
                ToolStack tools = ToolStack.from(itemStack);
                itemStack.setTag(stack.getTag());
                tools.setUpgrades(tool.getUpgrades());
                tools.setDamage(tool.getDamage());
                tools.getPersistentData().copyFrom(tool.getPersistentData().getCopy());
                player.setItemInHand(InteractionHand.MAIN_HAND, tools.createStack());
            }else
            if (player!=null&&player.getMainHandItem().is(entropy_burning_cannon.get())){
                ItemStack stack = player.getMainHandItem();
                ToolStack tool = ToolStack.from(stack);
                ItemStack itemStack = ToolStack.createTool(entropy_burning_cube.get(), MomotinkerToolDefinitions.ENTROPY_BURNING_CUBE,tool.getMaterials()).createStack();
                ToolStack tools = ToolStack.from(itemStack);
                itemStack.setTag(stack.getTag());
                tools.setUpgrades(tool.getUpgrades());
                tools.setDamage(tool.getDamage());
                tools.getPersistentData().copyFrom(tool.getPersistentData().getCopy());
                player.setItemInHand(InteractionHand.MAIN_HAND, tools.createStack());
            }
        });
        context.setPacketHandled(true);
    }
}
