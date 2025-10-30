package com.momosensei.momotinker.network.packet;

import com.momosensei.momotinker.register.MomotinkerConfig;
import com.momosensei.momotinker.register.MomotinkerItem;
import com.momosensei.momotinker.register.MomotinkerToolDefinitions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.item.armor.ModifiableArmorItem;
import slimeknights.tconstruct.library.tools.nbt.MaterialNBT;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;
import java.util.function.Supplier;

import static com.momosensei.momotinker.Modifiers.modifiers.ProjectionOfSuffering.disaster;
import static com.momosensei.momotinker.Modifiers.momomodifier.createNewTool;
import static com.momosensei.momotinker.Momotinker.getResource;
import static com.momosensei.momotinker.entity.MomotinkerEntitiesCreate.createPull;
import static com.momosensei.momotinker.tool.entropy_burning_cube.liverization;
import static com.momosensei.momotinker.tool.legion.*;

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
            if (player!=null&&player.getOffhandItem().is(ItemStack.EMPTY.getItem())) {
                if (player.getMainHandItem().is(MomotinkerItem.entropy_burning_cube.get())) {
                    ItemStack stack = createNewTool(player.getMainHandItem(),MomotinkerItem.entropy_burning_sword.get(),MomotinkerToolDefinitions.ENTROPY_BURNING_SWORD);
                    player.setItemInHand(InteractionHand.MAIN_HAND,stack);
                } else if (player.getMainHandItem().is(MomotinkerItem.entropy_burning_sword.get())) {
                    ItemStack stack = createNewTool(player.getMainHandItem(),MomotinkerItem.entropy_burning_riding_spear.get(),MomotinkerToolDefinitions.ENTROPY_BURNING_RIDING_SPEAR);
                    player.setItemInHand(InteractionHand.MAIN_HAND,stack);
                } else if (player.getMainHandItem().is(MomotinkerItem.entropy_burning_riding_spear.get())) {
                    ItemStack stack = createNewTool(player.getMainHandItem(),MomotinkerItem.entropy_burning_cannon.get(),MomotinkerToolDefinitions.ENTROPY_BURNING_CANNON);
                    player.setItemInHand(InteractionHand.MAIN_HAND,stack);
                } else if (player.getMainHandItem().is(MomotinkerItem.entropy_burning_cannon.get())) {
                    ItemStack stack = createNewTool(player.getMainHandItem(),MomotinkerItem.entropy_burning_cube.get(),MomotinkerToolDefinitions.ENTROPY_BURNING_CUBE);
                    player.setItemInHand(InteractionHand.MAIN_HAND,stack);
                }
            }
            int liverization_limit = MomotinkerConfig.liverization_limit.get();
            if (player!=null&&player.getMainHandItem().is(MomotinkerItem.entropy_burning_cube.get())
                    &&(player.getOffhandItem().getItem() instanceof ModifiableItem||player.getOffhandItem().getItem() instanceof ModifiableArmorItem)) {
                ToolStack tool = ToolStack.from(player.getMainHandItem());
                ToolStack tool1 = ToolStack.from(player.getOffhandItem());
                if (tool1.getMaterials().size()<=3&&tool1.getMaterials().size()>=0&&tool.getPersistentData().getInt(liverization)==liverization_limit) {
                    List<ModifierEntry> list1 = tool1.getModifiers().getModifiers();
                    for (ModifierEntry modifier : list1) {
                        tool.setUpgrades(tool.getUpgrades().withModifier(modifier.getId(),modifier.getLevel()));
                    }
                    tool.getPersistentData().putInt(liverization,tool.getPersistentData().getInt(liverization)+1);
                    tool.rebuildStats();
                    player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                }
            }

            if (player!=null&&(player.getMainHandItem().is(MomotinkerItem.coronal_key.get())&&player.getOffhandItem().is(MomotinkerItem.moon_lock.get())
                    ||player.getMainHandItem().is(MomotinkerItem.eclipse_container.get())&&player.getOffhandItem().is(ItemStack.EMPTY.getItem()))) {
                ItemStack stack1 = player.getMainHandItem();
                ItemStack stack2 = player.getOffhandItem();
                ToolStack tool1 = ToolStack.from(stack1);
                ToolStack tool2 = ToolStack.from(stack2);
                ItemStack stack3 = ToolStack.createTool(MomotinkerItem.eclipse_container.get(), MomotinkerToolDefinitions.ECLIPSE_CONTAINER, MaterialNBT.builder().build()).createStack();
                ItemStack stack4 = ToolStack.createTool(MomotinkerItem.coronal_key.get(), MomotinkerToolDefinitions.CORONAL_KEY, MaterialNBT.builder().build()).createStack();
                ItemStack stack5 = ToolStack.createTool(MomotinkerItem.moon_lock.get(), MomotinkerToolDefinitions.MOON_LOCK, MaterialNBT.builder().build()).createStack();
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

            if (player!=null&&(player.getMainHandItem().is(MomotinkerItem.pneumatic_sword.get())||player.getOffhandItem().is(MomotinkerItem.pneumatic_sword.get()))) {
                ModDataNBT data1 = ToolStack.from(player.getMainHandItem()).getPersistentData();
                ModDataNBT data2 = ToolStack.from(player.getOffhandItem()).getPersistentData();
                if (player.getMainHandItem().is(MomotinkerItem.pneumatic_sword.get())&&player.getOffhandItem().is(MomotinkerItem.pneumatic_sword.get())&&data1.getFloat(getResource("pullcool"))==0){
                    data1.putFloat(getResource("pullcool"),3);
                    createPull(player);
                }else
                if (player.getMainHandItem().is(MomotinkerItem.pneumatic_sword.get())&&!player.getOffhandItem().is(MomotinkerItem.pneumatic_sword.get())&&data1.getFloat(getResource("pullcool"))==0){
                    data1.putFloat(getResource("pullcool"),8);
                    createPull(player);
                }else
                if (!player.getMainHandItem().is(MomotinkerItem.pneumatic_sword.get())&&player.getOffhandItem().is(MomotinkerItem.pneumatic_sword.get())&&data2.getFloat(getResource("pullcool"))==0){
                    data2.putFloat(getResource("pullcool"),8);
                    createPull(player);
                }
            }

            if (player!=null&&(player.getMainHandItem().is(MomotinkerItem.legion.get()))) {
                ToolStack tool = ToolStack.from(player.getMainHandItem());
                ModDataNBT data = tool.getPersistentData();
                if (!data.getBoolean(legion_on)&&data.getFloat(legion_cooldown)==60) {
                    data.putBoolean(legion_on,true);
                    float perc = Mth.clamp(tool.getStats().get(ToolStats.ATTACK_SPEED) / 60, 0, 1);
                    int a = (int) Math.floor(perc * 10);
                    if (a < 1) a = 1;
                    if (a > 10) a = 10;
                    createLegion(player, a + 2, 2);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
