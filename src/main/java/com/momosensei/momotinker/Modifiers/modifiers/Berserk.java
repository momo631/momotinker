package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.item.armor.ModifiableArmorItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;


public class Berserk extends momomodifier {
    public Berserk() {
    }
    public static final ResourceLocation berserker = Momotinker.getResource("berserker");

    @Override
    public void OnLivingHurt(LivingHurtEvent event) {
        LivingEntity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player&&a !=null){
            int d = getAllModifierlevel(player,MomotinkerModifiers.berserk.getId());
            float c=0;
            for (ItemStack stack : player.getInventory().armor) {
                if (stack.getItem() instanceof ModifiableArmorItem&&ToolStack.from(stack).getModifierLevel(MomotinkerModifiers.berserk.getId())>0) {
                    c += ToolStack.from(stack).getPersistentData().getFloat(berserker);
                }
            }
            if (isToolStack(player.getMainHandItem()) &&getMainhandModifierlevel(player,MomotinkerModifiers.berserk.getId())>0){
                c += ToolStack.from(player.getMainHandItem()).getPersistentData().getFloat(berserker);
            }
            if (isToolStack(player.getOffhandItem()) &&getOffhandModifierlevel(player,MomotinkerModifiers.berserk.getId())>0){
                c += ToolStack.from(player.getOffhandItem()).getPersistentData().getFloat(berserker);
            }
            if (c >= 1 && d > 0) {
                event.setAmount(event.getAmount() * (1.4F + 0.3F * d));
            }
        }
        if (a instanceof Player player){
            int d = getAllModifierlevel(player,MomotinkerModifiers.berserk.getId());
            float c=0;
            for (ItemStack stack : player.getInventory().armor) {
                if (stack.getItem() instanceof ModifiableArmorItem&&ToolStack.from(stack).getModifierLevel(MomotinkerModifiers.berserk.getId())>0) {
                    c += ToolStack.from(stack).getPersistentData().getFloat(berserker);
                }
            }
            if (isToolStack(player.getMainHandItem()) &&getMainhandModifierlevel(player,MomotinkerModifiers.berserk.getId())>0){
                c += ToolStack.from(player.getMainHandItem()).getPersistentData().getFloat(berserker);
            }
            if (isToolStack(player.getOffhandItem()) &&getOffhandModifierlevel(player,MomotinkerModifiers.berserk.getId())>0){
                c += ToolStack.from(player.getOffhandItem()).getPersistentData().getFloat(berserker);
            }
            if (c >= 1 && d > 0) {
                event.setAmount(event.getAmount() * (1.4F + 0.3F * d));
            }
        }
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        ModDataNBT c = tool.getPersistentData();
        if (player!=null) {
            int a = getAllModifierlevel(player,MomotinkerModifiers.berserk.getId());
            if (c.getFloat(berserker) == 0) {
                tooltip.add(Component.translatable("modifier.momotinker.tooltip.berserk1").withStyle(ChatFormatting.DARK_RED));
            }
            if (c.getFloat(berserker) == 1) {
                tooltip.add(Component.translatable("modifier.momotinker.tooltip.berserk2").append((a*20+140) + "%").withStyle(ChatFormatting.DARK_RED));
            }
        }
    }
}