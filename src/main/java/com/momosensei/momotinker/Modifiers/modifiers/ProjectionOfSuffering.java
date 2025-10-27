package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.HudCharge.ToolsTimeCharge;
import com.momosensei.momotinker.register.MomotinkerTools;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;


public class ProjectionOfSuffering extends momomodifier {
    public ProjectionOfSuffering() {

    }

    public static final ResourceLocation disaster = Momotinker.getResource("disaster");
    public static final ResourceLocation disasterpremonition = Momotinker.getResource("disasterpremonition");

    @Override
    public boolean isNoLevels() {
        return true;
    }
    
    @Override
    public @Nullable Component onRemoved(IToolStackView tool, Modifier modifier) {
        tool.getPersistentData().remove(disaster);
        tool.getPersistentData().remove(disasterpremonition);
        return null;
    }
    
    @Override
    public void OnLivingHurt(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player && a != null && player.getMainHandItem().is(MomotinkerTools.coronal_key.get())) {
            ModDataNBT c = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
            c.putInt(disasterpremonition, 8);
            if (c.getInt(disaster)>0){
                event.setAmount(event.getAmount() * (1f + c.getInt(disaster) * 0.001f));
            }
        }
        if (a instanceof Player player && b != null && player.getMainHandItem().is(MomotinkerTools.coronal_key.get())) {
            ModDataNBT c = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
            c.putInt(disasterpremonition, 8);
        }
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        ModDataNBT a = tool.getPersistentData();
        if (entity instanceof ServerPlayer player&&entity.getItemBySlot(EquipmentSlot.MAINHAND).is(MomotinkerTools.coronal_key.get())) {
            if (a.getInt(disasterpremonition) > 0) {
                if (a.getInt(disaster) < 600) {
                    a.putInt(disaster, a.getInt(disaster) + 2);
                } else if (a.getInt(disaster) >= 600) {
                    a.putInt(disaster, 600);
                }

                if (entity.tickCount % 20 == 0) {
                    a.putInt(disasterpremonition, a.getInt(disasterpremonition) - 1);
                }

            }else
            if (a.getInt(disasterpremonition) == 0&&a.getInt(disaster) > 0) {
                a.putInt(disaster, a.getInt(disaster) - 3);
            }
            float perc = Mth.clamp((float)a.getInt(disaster) / 600, 0, 1);
            Channel.sendToPlayer(new ToolsTimeCharge(perc),player);
        }
        if (a.getInt(disaster) > 0&&!entity.getItemBySlot(EquipmentSlot.MAINHAND).is(MomotinkerTools.coronal_key.get()) && !entity.getItemBySlot(EquipmentSlot.MAINHAND).is(MomotinkerTools.eclipse_container.get())) {
            a.putInt(disaster,a.getInt(disaster)-6);
        }
        if (a.getInt(disaster)<0){
            a.putInt(disaster, 0);
        }
    }
}