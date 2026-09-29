package com.momosensei.momotinker.Items.other;


import com.momosensei.momotinker.mobs.StageMeteor;
import com.momosensei.momotinker.register.MomotinkerConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.momosensei.momotinker.event.LivingEvents.meteor_nucleus_amount;
import static com.momosensei.momotinker.event.LivingEvents.meteor_nucleus_unlock;

public class interdimensional_crystal extends Item {
    public interdimensional_crystal(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 20;
    }
    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CUSTOM;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        boolean config_all = MomotinkerConfig.special_acquisition.get();
        boolean config = MomotinkerConfig.meteor_nucleus.get();
        if (!config||!config_all) {
            return InteractionResultHolder.fail(stack);
        }
        CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
        boolean meteor_unlocked = tag.getBoolean(meteor_nucleus_unlock);
        if (!meteor_unlocked) {
            return InteractionResultHolder.fail(stack);
        }
        return InteractionResultHolder.consume(stack);
    }
    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        ItemStack stack1 = super.finishUsingItem(stack, level, living);
        if (living instanceof Player player) {
            MeteorAmountIncrease(player);
        }
        return stack1;
    }

    private void MeteorAmountIncrease(Player player) {
        boolean config_all = MomotinkerConfig.special_acquisition.get();
        if (!config_all)return;
        boolean config = MomotinkerConfig.meteor_nucleus.get();
        if (config) {
            CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
            boolean meteor_unlocked = tag.getBoolean(meteor_nucleus_unlock);
            if (meteor_unlocked) {
                tag.putInt(meteor_nucleus_amount, 3);
                player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
                player.sendSystemMessage(Component.translatable("item.momotinker.tooltip.meteor_nucleus7").withStyle(ChatFormatting.GOLD));
            }
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<net.minecraft.network.chat.Component> list, @NotNull TooltipFlag flag) {
        boolean config_all = MomotinkerConfig.special_acquisition.get();
        boolean configa = MomotinkerConfig.interdimensional_crystal.get();
        boolean config = MomotinkerConfig.meteor_nucleus.get();
        if (Screen.hasShiftDown()&&config_all&&configa) {
            list.add(Component.translatable("item.momotinker.tooltip.interdimensional_crystal2").withStyle(ChatFormatting.LIGHT_PURPLE));
        } else {
            list.add(Component.translatable("item.momotinker.tooltip.interdimensional_crystal1").withStyle(ChatFormatting.LIGHT_PURPLE));
            if (config_all && configa) {
                list.add(Component.translatable("item.momotinker.tooltip.special_acquisition").withStyle(ChatFormatting.LIGHT_PURPLE));
            }
        }
        if (config&&config_all&&configa&&StageMeteor.getStageFloat()==1) {
            list.add(Component.translatable("item.momotinker.tooltip.interdimensional_crystal3").withStyle(ChatFormatting.GOLD));
        }
        super.appendHoverText(stack, level, list, flag);
    }

}