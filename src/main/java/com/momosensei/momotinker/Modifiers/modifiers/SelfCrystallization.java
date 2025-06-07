package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;


public class SelfCrystallization extends momomodifier {
    public SelfCrystallization() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        ToolStats.DURABILITY.multiply(builder, Math.pow(1.25,modifier.getLevel()));
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (tool.getDamage() > 0 && entity instanceof Player player&&player.tickCount%4==0) {
            int d = getAllModifierlevel(player, MomotinkerModifiers.selfcrystallization.getId());
            if (tool.getDamage() > d) {
                tool.setDamage(tool.getDamage() - d);
            }else tool.setDamage(0);
        }
    }

    private void livinghurtevent(LivingHurtEvent event) {
        LivingEntity a = event.getEntity();
        Entity a1 = event.getSource().getEntity();
        if (a instanceof Player player) {
            ToolStack tool1 = ToolStack.from(player.getMainHandItem());
            ToolStack tool2 = ToolStack.from(player.getOffhandItem());
            ToolStack tool3 = ToolStack.from(player.getItemBySlot(EquipmentSlot.HEAD));
            ToolStack tool4 = ToolStack.from(player.getItemBySlot(EquipmentSlot.CHEST));
            ToolStack tool5 = ToolStack.from(player.getItemBySlot(EquipmentSlot.LEGS));
            ToolStack tool6 = ToolStack.from(player.getItemBySlot(EquipmentSlot.FEET));
            int d = getAllModifierlevel(player, MomotinkerModifiers.selfcrystallization.getId());
            if (d > 0) {
                int b1 = 0;int b2 = 0;int b3 = 0;int b4 = 0;int b5 = 0;int b6 = 0;
                if (tool1.getModifierLevel(MomotinkerModifiers.selfcrystallization.getId()) > 0) {
                    b1 += (int) (getRemainingDurability(tool1) * 0.002f);
                    tool1.setDamage(tool1.getDamage() + b1);
                }
                if (tool2.getModifierLevel(MomotinkerModifiers.selfcrystallization.getId()) > 0) {
                    b2 += (int) (getRemainingDurability(tool2) * 0.002f);
                    tool2.setDamage(tool2.getDamage() + b2);
                }
                if (tool3.getModifierLevel(MomotinkerModifiers.selfcrystallization.getId()) > 0) {
                    b3 += (int) (getRemainingDurability(tool3) * 0.05f);
                    tool3.setDamage(tool3.getDamage() + b3);
                }
                if (tool4.getModifierLevel(MomotinkerModifiers.selfcrystallization.getId()) > 0) {
                    b4 += (int) (getRemainingDurability(tool4) * 0.05f);
                    tool4.setDamage(tool4.getDamage() + b4);
                }
                if (tool5.getModifierLevel(MomotinkerModifiers.selfcrystallization.getId()) > 0) {
                    b5 += (int) (getRemainingDurability(tool5) * 0.05f);
                    tool5.setDamage(tool5.getDamage() + b5);
                }
                if (tool6.getModifierLevel(MomotinkerModifiers.selfcrystallization.getId()) > 0) {
                    b6 += (int) (getRemainingDurability(tool6) * 0.05f);
                    tool6.setDamage(tool6.getDamage() + b6);
                }
                int b = b1 + b2 + b3 + b4 + b5 + b6;
                if (b * 0.02f >= event.getAmount()) {
                    if (a1 instanceof LivingEntity entity) {
                        entity.hurt(DamageSource.playerAttack(player), (b* 0.02f - event.getAmount())*5f);
                    }
                    event.setAmount(0);
                }else if (b * 0.02f < event.getAmount()){
                    event.setAmount(event.getAmount()-b * 0.02f);
                }
            }
        }
    }
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> builder, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player != null) {
            ToolStack tool1 = ToolStack.from(player.getMainHandItem());
            ToolStack tool2 = ToolStack.from(player.getOffhandItem());
            ToolStack tool3 = ToolStack.from(player.getItemBySlot(EquipmentSlot.HEAD));
            ToolStack tool4 = ToolStack.from(player.getItemBySlot(EquipmentSlot.CHEST));
            ToolStack tool5 = ToolStack.from(player.getItemBySlot(EquipmentSlot.LEGS));
            ToolStack tool6 = ToolStack.from(player.getItemBySlot(EquipmentSlot.FEET));
            int b = 0;
            if (tool1.getModifierLevel(MomotinkerModifiers.selfcrystallization.getId()) > 0) {
                b += (int) (getRemainingDurability(tool1) * 0.002f);
            }
            if (tool2.getModifierLevel(MomotinkerModifiers.selfcrystallization.getId()) > 0) {
                b += (int) (getRemainingDurability(tool2) * 0.002f);
            }
            if (tool3.getModifierLevel(MomotinkerModifiers.selfcrystallization.getId()) > 0) {
                b += (int) (getRemainingDurability(tool3) * 0.05f);
            }
            if (tool4.getModifierLevel(MomotinkerModifiers.selfcrystallization.getId()) > 0) {
                b += (int) (getRemainingDurability(tool4) * 0.05f);
            }
            if (tool5.getModifierLevel(MomotinkerModifiers.selfcrystallization.getId()) > 0) {
                b += (int) (getRemainingDurability(tool5) * 0.05f);
            }
            if (tool6.getModifierLevel(MomotinkerModifiers.selfcrystallization.getId()) > 0) {
                b += (int) (getRemainingDurability(tool6) * 0.05f);
            }
            builder.add(Component.translatable("modifier.momotinker.tooltip.selfcrystallization1").append(String.format("%.0f",b*0.02f)).withStyle(ChatFormatting.AQUA));
        }
    }
}