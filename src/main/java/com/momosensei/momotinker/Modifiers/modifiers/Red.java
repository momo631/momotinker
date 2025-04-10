package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import javax.annotation.Nullable;
import java.util.List;


public class Red extends momomodifier {
    public Red() {
    }
    public static final ResourceLocation ender = Momotinker.getResource("ender");

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(ender);
        return null;
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof ServerPlayer) {
            for (LivingEntity e : entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate((double) 20.0F), (ex) -> ex instanceof Enemy)) {
                e.addEffect(new MobEffectInstance(MobEffects.GLOWING, 120));
            }
        }
        if (entity instanceof ServerPlayer) {
            ModDataNBT tooldata = iToolStackView.getPersistentData();
            if (entity.tickCount % 20 == 0&&tooldata.getFloat(ender)>0.1){
                tooldata.putFloat(ender,tooldata.getFloat(ender)-1);
            }
        }
    }
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        ModDataNBT tooldata = tool.getPersistentData();
        if (player != null&&tooldata.getFloat(ender)!=0) {
            tooltip.add(net.minecraft.network.chat.Component.translatable("modifier.momotinker.tooltip.red1").append(tooldata.getFloat(ender)+"s").withStyle(ChatFormatting.DARK_RED));
        }
    }
}




