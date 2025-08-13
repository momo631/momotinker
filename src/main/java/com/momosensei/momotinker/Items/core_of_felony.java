package com.momosensei.momotinker.Items;


import com.momosensei.momotinker.mobs.StageMeteor;
import com.momosensei.momotinker.renderer.RainbowFont;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class core_of_felony extends Item {
    public core_of_felony(Properties properties) {
        super(properties.fireResistant().stacksTo(16));
    }
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            if (StageMeteor.getStageFloat()==1) {
                list.add(Component.translatable("item.momotinker.tooltip.core_of_felony3").withStyle(ChatFormatting.GRAY));
            }else {
                list.add(Component.translatable("item.momotinker.tooltip.stage_meteor").withStyle(ChatFormatting.GOLD));
            }
        }else {
//            list.add(Component.literal(RainbowText.makeColour2(Component.translatable("item.momotinker.tooltip.core_of_felony1").withStyle(ChatFormatting.ITALIC).getString())));
//            list.add(Component.literal(RainbowText.makeColour2(Component.translatable("item.momotinker.tooltip.core_of_felony2").withStyle(ChatFormatting.ITALIC).getString())));
//            list.add(Component.literal(RainbowText.makeColour2(Component.translatable("item.momotinker.tooltip.special_acquisition").getString())));
            list.add((Component.translatable("item.momotinker.tooltip.core_of_felony1")));
            list.add((Component.translatable("item.momotinker.tooltip.core_of_felony2")));
            list.add((Component.translatable("item.momotinker.tooltip.special_acquisition")));
        }
    }
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            public @NotNull Font getFont(ItemStack stack, IClientItemExtensions.FontContext context) {
                return RainbowFont.getFont();
            }
        });
    }
}