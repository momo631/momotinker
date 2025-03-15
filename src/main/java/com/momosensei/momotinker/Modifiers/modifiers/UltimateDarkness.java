package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.ToolsTimeCharge;
import com.momosensei.momotinker.register.MomotinkerItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import static com.momosensei.momotinker.Modifiers.modifiers.ProjectionOfSuffering.disaster;


public class UltimateDarkness extends momomodifier {
    public UltimateDarkness() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    private void livinghurtevent(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player && a != null && player.getMainHandItem().is(MomotinkerItem.eclipse_container.get())) {
            ModDataNBT c = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
            if (c.getInt(disaster)==0){
                event.setAmount(event.getAmount()*0.1f);
            }
            if (c.getInt(disaster)>0){
                event.setAmount(event.getAmount()*1.6f);
            }
        }
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        ModDataNBT a = tool.getPersistentData();
        if (entity instanceof ServerPlayer player&&entity.getItemBySlot(EquipmentSlot.MAINHAND).is(MomotinkerItem.eclipse_container.get())){
            if (a.getInt(disaster) > 0) {
                a.putInt(disaster, a.getInt(disaster) - 1);
            }
            float perc = Mth.clamp((float)a.getInt(disaster) / 600, 0, 1);
            Channel.sendToPlayer(new ToolsTimeCharge(perc),player);
        }
        if (!entity.getItemBySlot(EquipmentSlot.MAINHAND).is(MomotinkerItem.coronal_key.get()) && !entity.getItemBySlot(EquipmentSlot.MAINHAND).is(MomotinkerItem.eclipse_container.get())) {
            a.putInt(disaster,a.getInt(disaster)-6);
        }
    }
}