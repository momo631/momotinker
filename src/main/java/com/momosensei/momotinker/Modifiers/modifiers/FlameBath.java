package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.List;


public class FlameBath extends momomodifier {
    public FlameBath() {
    }
    
    public static final ResourceLocation flamebathcooldown = Momotinker.getResource("flamebathcooldown");
    @Override
    public @javax.annotation.Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(flamebathcooldown);
        return null;
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof ServerPlayer player) {
            ModDataNBT a = tool.getPersistentData();
            if (entity.tickCount % 20 == 0 && a.getInt(flamebathcooldown) > 0) {
                a.putInt(flamebathcooldown, a.getInt(flamebathcooldown) - 1);
            }
            if (player.level() instanceof ServerLevel serverLevel) {
                for (int i = 0; i <= 360; i++) {
                    double rad = i * 0.017453292519943295;
                    double r = 4D;
                    double x = r * Math.cos(rad);
                    double z = r * Math.sin(rad);
                    int c = 5 / (a.getInt(flamebathcooldown) - 235);
                    if (c < 4) {
                        serverLevel.sendParticles(ParticleTypes.FLAME, player.getX(), player.getY(), player.getZ(), c, x / 2, r / 2, z / 2, 2);
                    }
                    if (c > 4) {
                        serverLevel.sendParticles(ParticleTypes.LAVA, player.getX(), player.getY(), player.getZ(), 3, x / 2, r / 2, z / 2, 1);
                        serverLevel.sendParticles(ParticleTypes.FLAME, player.getX(), player.getY(), player.getZ(), 4, x, r, z, 0.5);
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