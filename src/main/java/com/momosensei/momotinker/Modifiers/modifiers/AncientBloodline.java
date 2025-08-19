package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;


public class AncientBloodline extends momomodifier {
    public AncientBloodline() {
    }
    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Override
    public void OnLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() != null) {
            if (getAllModifierlevel(event.getEntity(), MomotinkerModifiers.dragon_source.getId())> 0) {
                float a = (float) Math.pow(0.9f,(float) event.getEntity().level().getGameTime()/24000);
                if (a<0.05f){
                    a=0.05f;
                }
                event.setAmount(event.getAmount()*a);
            }
            if (event.getSource().getEntity() instanceof LivingEntity living){
                if (getAllModifierlevel(living, MomotinkerModifiers.dragon_source.getId())> 0) {
                    float a = (float) Math.pow(1.02f,(float) event.getEntity().level().getGameTime()/24000);
                    event.setAmount(event.getAmount()*a);
                }
            }
        }
    }
    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player != null) {
            float a = (float) Math.pow(0.9f,(float)player.level().getGameTime()/24000);
            if (a<0.05f){
                a=0.05f;
            }
            float b = (float) Math.pow(1.02f,(float)player.level().getGameTime()/24000);
            tooltip.add(Component.translatable("modifier.momotinker.tooltip.ancient_bloodline1").append(String.format("%.0f",(b-1f)*100)+"%;").append(Component.translatable("modifier.momotinker.tooltip.ancient_bloodline2")).append(String.format("%.0f",(1f-a)*100)+"%").withStyle(ChatFormatting.DARK_PURPLE));

        }
    }
}