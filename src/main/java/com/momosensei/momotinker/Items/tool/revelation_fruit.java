package com.momosensei.momotinker.Items.tool;


import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.util.RandomRecipeManager;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class revelation_fruit extends Item {
    public revelation_fruit(Properties properties) {
        super(properties.stacksTo(16).rarity(Rarity.EPIC).fireResistant().food(
                new FoodProperties.Builder().alwaysEat().build()));
    }

    public static final String revelation = Momotinker.getResource("revelation").toString();
    public static final String revelation_limit = Momotinker.getResource("revelation_limit").toString();

//    @Override
//    public void onLeftClickEntity(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, Entity entity) {
//        player.getInventory().add(getRevelationFruit(doomsday,5));
//    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 30;
    }
    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
        player.startUsingItem(hand);
        String a = stack.getOrCreateTag().getString(revelation);
        int b = stack.getOrCreateTag().getInt(revelation_limit);
        if (tag.getInt(a)<b) {
            return InteractionResultHolder.pass(stack);
        }
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        ItemStack retval = super.finishUsingItem(stack, level, living);
        if (living instanceof Player player) {
            CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
            String a = stack.getOrCreateTag().getString(revelation);
            int b = stack.getOrCreateTag().getInt(revelation_limit);
            if (tag.getInt(a)<b) {
                tag.putInt(a, tag.getInt(a)+1);
            }
            else {
                tag.putInt(a, 0);
            }
            player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
            if (player instanceof ServerPlayer player1) {
                RandomRecipeManager.refreshAllClientDisplays(player1);
            }
        }
        return retval;
    }
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> list, @NotNull TooltipFlag flag) {
        String a = null;
        if (stack.getTag() != null) {
            a = stack.getTag().getString(revelation);
        }
        list.add((Component.translatable("item.momotinker.tooltip.revelation_fruit1")).withStyle(ChatFormatting.LIGHT_PURPLE));
        if (a!=null) {
            list.add((Component.translatable("item.momotinker.tooltip.revelation_fruit2").append(a)
                    .append(Component.translatable("item.momotinker.tooltip.revelation_fruit3")).withStyle(ChatFormatting.LIGHT_PURPLE)));
        }
        super.appendHoverText(stack, level, list, flag);
    }
}