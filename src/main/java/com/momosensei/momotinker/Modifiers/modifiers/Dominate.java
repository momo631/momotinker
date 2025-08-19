package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;


public class Dominate extends momomodifier {
    public Dominate() {
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        if (modifier.getLevel() > 0) {
            ToolStats.DURABILITY.multiply(builder, Math.pow(1.7, modifier.getLevel()));
            ToolStats.ATTACK_SPEED.multiply(builder, Math.pow(1.7, modifier.getLevel()));
            ToolStats.ATTACK_DAMAGE.multiply(builder, Math.pow(1.7, modifier.getLevel()));
            ToolStats.ACCURACY.multiply(builder, Math.pow(1.7, modifier.getLevel()));
            ToolStats.DRAW_SPEED.multiply(builder, Math.pow(1.7, modifier.getLevel()));
            ToolStats.VELOCITY.multiply(builder, Math.pow(1.7, modifier.getLevel()));
            ToolStats.MINING_SPEED.multiply(builder, Math.pow(1.7, modifier.getLevel()));
            ToolStats.ARMOR.multiply(builder, Math.pow(1.7, modifier.getLevel()));
            ToolStats.ARMOR_TOUGHNESS.multiply(builder, Math.pow(1.7, modifier.getLevel()));
            ToolStats.PROJECTILE_DAMAGE.multiply(builder, Math.pow(1.7, modifier.getLevel()));
            ToolStats.KNOCKBACK_RESISTANCE.multiply(builder, Math.pow(1.7, modifier.getLevel()));
            ToolStats.BLOCK_AMOUNT.multiply(builder, Math.pow(1.7, modifier.getLevel()));
            ToolStats.BLOCK_ANGLE.multiply(builder, Math.pow(1.7, modifier.getLevel()));
        }
    }

    @Override
    public void OnLivingHurt(LivingHurtEvent event) {
        LivingEntity living = event.getEntity();
        Entity entity=event.getSource().getEntity();
        int a =  getAllModifierlevel(living,MomotinkerModifiers.dominate.getId());
        if (entity instanceof LivingEntity attacker&&living instanceof Player player&& a > 0 && attacker.getMaxHealth()<player.getMaxHealth()*a*0.2F) {
            event.setAmount(event.getAmount() * 0.7F);
        }
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player != null) {
            int a =  getAllModifierlevel(player,MomotinkerModifiers.dominate.getId());
            tooltip.add(net.minecraft.network.chat.Component.translatable("modifier.momotinker.tooltip.dominate1").append(player.getMaxHealth()*a*0.2F+"").withStyle(ChatFormatting.RED));
        }
    }
}