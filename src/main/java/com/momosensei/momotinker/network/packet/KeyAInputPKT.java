package com.momosensei.momotinker.network.packet;

import com.momosensei.momotinker.register.MomotinkerItem;
import com.momosensei.momotinker.register.MomotinkerToolDefinitions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import slimeknights.tconstruct.library.tools.nbt.MaterialNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.function.Supplier;

import static com.momosensei.momotinker.Modifiers.modifiers.ProjectionOfSuffering.disaster;

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
            if (player!=null&&player.getMainHandItem().is(MomotinkerItem.entropy_burning_cube.get())){
                ItemStack stack = player.getMainHandItem();
                ToolStack tool = ToolStack.from(stack);
                ItemStack itemStack = ToolStack.createTool(MomotinkerItem.entropy_burning_sword.get(), MomotinkerToolDefinitions.ENTROPY_BURNING_SWORD,tool.getMaterials()).createStack();
                ToolStack tools = ToolStack.from(itemStack);
                itemStack.setTag(stack.getTag());
                tools.setUpgrades(tool.getUpgrades());
                tools.setDamage(tool.getDamage());
                tools.getPersistentData().copyFrom(tool.getPersistentData().getCopy());
                player.setItemInHand(InteractionHand.MAIN_HAND, tools.createStack());
            }else
            if (player!=null&&player.getMainHandItem().is(MomotinkerItem.entropy_burning_sword.get())){
                ItemStack stack = player.getMainHandItem();
                ToolStack tool = ToolStack.from(stack);
                ItemStack itemStack = ToolStack.createTool(MomotinkerItem.entropy_burning_riding_spear.get(), MomotinkerToolDefinitions.ENTROPY_BURNING_RIDING_SPEAR,tool.getMaterials()).createStack();
                ToolStack tools = ToolStack.from(itemStack);
                itemStack.setTag(stack.getTag());
                tools.setUpgrades(tool.getUpgrades());
                tools.setDamage(tool.getDamage());
                tools.getPersistentData().copyFrom(tool.getPersistentData().getCopy());
                player.setItemInHand(InteractionHand.MAIN_HAND, tools.createStack());
            }else
            if (player!=null&&player.getMainHandItem().is(MomotinkerItem.entropy_burning_riding_spear.get())){
                ItemStack stack = player.getMainHandItem();
                ToolStack tool = ToolStack.from(stack);
                ItemStack itemStack = ToolStack.createTool(MomotinkerItem.entropy_burning_cannon.get(), MomotinkerToolDefinitions.ENTROPY_BURNING_CANNON,tool.getMaterials()).createStack();
                ToolStack tools = ToolStack.from(itemStack);
                itemStack.setTag(stack.getTag());
                tools.setUpgrades(tool.getUpgrades());
                tools.setDamage(tool.getDamage());
                tools.getPersistentData().copyFrom(tool.getPersistentData().getCopy());
                player.setItemInHand(InteractionHand.MAIN_HAND, tools.createStack());
            }else
            if (player!=null&&player.getMainHandItem().is(MomotinkerItem.entropy_burning_cannon.get())){
                ItemStack stack = player.getMainHandItem();
                ToolStack tool = ToolStack.from(stack);
                ItemStack itemStack = ToolStack.createTool(MomotinkerItem.entropy_burning_cube.get(), MomotinkerToolDefinitions.ENTROPY_BURNING_CUBE,tool.getMaterials()).createStack();
                ToolStack tools = ToolStack.from(itemStack);
                itemStack.setTag(stack.getTag());
                tools.setUpgrades(tool.getUpgrades());
                tools.setDamage(tool.getDamage());
                tools.getPersistentData().copyFrom(tool.getPersistentData().getCopy());
                player.setItemInHand(InteractionHand.MAIN_HAND, tools.createStack());
            }

            if (player!=null) {
                ItemStack stack1 = player.getMainHandItem();
                ItemStack stack2 = player.getOffhandItem();
                ToolStack tool1 = ToolStack.from(stack1);
                ToolStack tool2 = ToolStack.from(stack2);
                ItemStack stack3 = ToolStack.createTool(MomotinkerItem.eclipse_container.get(), MomotinkerToolDefinitions.CHARGING_AXE, MaterialNBT.builder().build()).createStack();
                ItemStack stack4 = ToolStack.createTool(MomotinkerItem.coronal_key.get(), MomotinkerToolDefinitions.CHARGING_SWORD, MaterialNBT.builder().build()).createStack();
                ItemStack stack5 = ToolStack.createTool(MomotinkerItem.moon_lock.get(), MomotinkerToolDefinitions.CHARGING_SHIELD, MaterialNBT.builder().build()).createStack();
                ToolStack tool3 = ToolStack.from(stack3);
                ToolStack tool4 = ToolStack.from(stack4);
                ToolStack tool5 = ToolStack.from(stack5);
                tool3.setMaterials(tool3.getMaterials().replaceMaterial(0,tool1.getMaterial(0).getVariant()));
                tool3.setMaterials(tool3.getMaterials().replaceMaterial(1,tool1.getMaterial(1).getVariant()));
                tool3.setMaterials(tool3.getMaterials().replaceMaterial(2,tool1.getMaterial(2).getVariant()));
                tool3.setMaterials(tool3.getMaterials().replaceMaterial(3,tool2.getMaterial(0).getVariant()));
                tool3.setMaterials(tool3.getMaterials().replaceMaterial(4,tool2.getMaterial(1).getVariant()));
                tool3.setMaterials(tool3.getMaterials().replaceMaterial(5,tool2.getMaterial(2).getVariant()));

                tool4.setMaterials(tool4.getMaterials().replaceMaterial(0,tool1.getMaterial(0).getVariant()));
                tool4.setMaterials(tool4.getMaterials().replaceMaterial(1,tool1.getMaterial(1).getVariant()));
                tool4.setMaterials(tool4.getMaterials().replaceMaterial(2,tool1.getMaterial(2).getVariant()));

                tool5.setMaterials(tool5.getMaterials().replaceMaterial(0,tool1.getMaterial(3).getVariant()));
                tool5.setMaterials(tool5.getMaterials().replaceMaterial(1,tool1.getMaterial(4).getVariant()));
                tool5.setMaterials(tool5.getMaterials().replaceMaterial(2,tool1.getMaterial(5).getVariant()));

                stack3.setTag(stack1.getTag());
                stack4.setTag(stack1.getTag());

                tool3.setUpgrades(tool1.getUpgrades());
                tool4.setUpgrades(tool1.getUpgrades());

                tool3.setDamage(tool1.getDamage());
                tool4.setDamage(tool1.getDamage());

                tool3.getPersistentData().copyFrom(tool1.getPersistentData().getCopy());
                tool4.getPersistentData().copyFrom(tool1.getPersistentData().getCopy());

                tool3.rebuildStats();
                tool4.rebuildStats();
                tool5.rebuildStats();
                if (player.getMainHandItem().is(MomotinkerItem.coronal_key.get())&&player.getOffhandItem().is(MomotinkerItem.moon_lock.get())&&tool1.getPersistentData().getInt(disaster)==600) {
                    player.setItemInHand(InteractionHand.MAIN_HAND, tool3.createStack());
                    player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                }else if (player.getMainHandItem().is(MomotinkerItem.eclipse_container.get())&&player.getOffhandItem().is(ItemStack.EMPTY.getItem())){
                    if (tool1.getPersistentData().getInt(disaster)>0){
                        tool1.getPersistentData().putInt(disaster,0);
                    }
                    player.setItemInHand(InteractionHand.MAIN_HAND, tool4.createStack());
                    player.setItemInHand(InteractionHand.OFF_HAND, tool5.createStack());
                }
            }
        });
        context.setPacketHandled(true);
    }
}
