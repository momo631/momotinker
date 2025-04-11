package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.item.armor.ModifiableArmorItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.List;


public class FlameBath extends momomodifier {
    public FlameBath() {
    }
    
    public static final ResourceLocation flamebathcooldown = Momotinker.getResource("flamebathcooldown");

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof ServerPlayer player) {
            for (ItemStack stack : player.getInventory().armor) {
                if (stack.getItem() instanceof ModifiableArmorItem&&ModifierUtil.getModifierLevel(stack, MomotinkerModifiers.flamebath.getId())>0) {
                    ModDataNBT a = iToolStackView.getPersistentData();
                    if (a.getInt(flamebathcooldown) < 0) {
                        a.putInt(flamebathcooldown, 0);
                    }
                    if (entity.tickCount % 20 == 0 && a.getInt(flamebathcooldown) > 0) {
                        a.putInt(flamebathcooldown, a.getInt(flamebathcooldown) - 1);
                        break;
                    }
                }
            }
        }
    }
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        ModDataNBT tooldata = tool.getPersistentData();
        if (player != null&&tooldata.getInt(flamebathcooldown)!=0) {
            tooltip.add(net.minecraft.network.chat.Component.translatable("modifier.momotinker.tooltip.flamebath1").append(tooldata.getInt(flamebathcooldown)+"s").withStyle(ChatFormatting.GOLD));
        }
    }
}