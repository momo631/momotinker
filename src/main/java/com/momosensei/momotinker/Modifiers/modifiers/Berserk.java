package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;


public class Berserk extends momomodifier {
    public Berserk() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }
    public static final ResourceLocation berserker = Momotinker.getResource("berserker");

    private void livinghurtevent(LivingHurtEvent event) {
        LivingEntity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player&&a !=null){
            int d =ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.berserk.getId());
            ModDataNBT c = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
            if (c.getFloat(berserker)==1&&d>0){
                event.setAmount(event.getAmount()*(1.4F+0.2F*d));
            }
        }
        if (a instanceof Player player){
            int d =ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.berserk.getId());
            ModDataNBT c = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
            if (c.getFloat(berserker)==1&&d>0){
                event.setAmount(event.getAmount()*(1.4F+0.2F*d));
            }
        }
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        ModDataNBT c = tool.getPersistentData();
        if (player!=null) {
            int a = ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.berserk.getId());
            if (c.getFloat(berserker) == 0) {
                tooltip.add(Component.translatable("未开启狂暴化").withStyle(ChatFormatting.DARK_RED));
            }
            if (c.getFloat(berserker) == 1) {
                tooltip.add(Component.translatable("开启狂暴化！当前造成伤害与承受伤害修正为" + (a*20+140) + "%").withStyle(ChatFormatting.DARK_RED));
            }
        }
    }
}