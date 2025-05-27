package com.momosensei.momotinker.Modifiers.modifiers;


import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerConfig;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.level.BlockEvent;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ValidateModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.RequirementsModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;
import java.util.List;

import static com.momosensei.momotinker.Momotinker.getResource;
import static com.momosensei.momotinker.Momotinker.getResourceLocation;

public class ShortTermInvestments extends momomodifier implements RequirementsModifierHook, ValidateModifierHook {
    public ShortTermInvestments() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
        MinecraftForge.EVENT_BUS.addListener(this::onEntityDeath);
        MinecraftForge.EVENT_BUS.addListener(this::onItemEvent);
    }
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.REQUIREMENTS,ModifierHooks.VALIDATE);
    }
    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Nullable
    @Override
    public Component requirementsError(ModifierEntry entry) {
        return Component.translatable("recipe.momotinker.modifier.shortterminvestments");
    }

    @Override
    public @NotNull List<ModifierEntry> displayModifiers(ModifierEntry entry) {
        return List.of(new ModifierEntry(MomotinkerModifiers.intendingplunder.getId(),1));
    }

    @Override
    public Component validate(IToolStackView tool, ModifierEntry modifier) {
        if (tool.getModifierLevel(MomotinkerModifiers.intendingplunder.getId())>0){
            return null;
        }
        return requirementsError(modifier);
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        ModDataNBT data = tool.getPersistentData();
        if (player != null) {
            tooltip.add(net.minecraft.network.chat.Component.translatable("modifier.momotinker.tooltip.shortterminvestments1").append(data.getString(getResourceLocation("shorttermname"))).withStyle(ChatFormatting.BLUE));
            tooltip.add(net.minecraft.network.chat.Component.translatable("modifier.momotinker.tooltip.shortterminvestments2").append(data.getFloat(getResource("shorttermindex"))+"").withStyle(ChatFormatting.BLUE));

        }
    }

    private void livinghurtevent(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        int increase = MomotinkerConfig.shortterminvestments_increase_ratio.get();
        int reduce = MomotinkerConfig.shortterminvestments_reduce_ratio.get();
        int d = RANDOM.nextInt(100);
        if (a instanceof Player player){
            int c=getMainhandModifierlevel(player,MomotinkerModifiers.shortterminvestments.getId());
            ModDataNBT data= ToolStack.from(player.getMainHandItem()).getPersistentData();
            if (c>0&&!data.getString(getResourceLocation("shorttermname")).isEmpty()){
                if (d<increase){
                    data.putFloat(getResource("shorttermindex"), data.getFloat(getResource("shorttermindex"))*1.1f);
                }else if (d>=100-reduce){
                    data.putFloat(getResource("shorttermindex"), data.getFloat(getResource("shorttermindex"))*0.95f);
                }
            }
        }
        if (b instanceof Player player&&a!=null){
            int c=getMainhandModifierlevel(player,MomotinkerModifiers.shortterminvestments.getId());
            ModDataNBT data= ToolStack.from(player.getMainHandItem()).getPersistentData();
            if (c>0&&!data.getString(getResourceLocation("shorttermname")).isEmpty()){
                if (d<increase){
                    data.putFloat(getResource("shorttermindex"), data.getFloat(getResource("shorttermindex"))*1.1f);
                }else if (d>=100-reduce){
                    data.putFloat(getResource("shorttermindex"), data.getFloat(getResource("shorttermindex"))*0.95f);
                }
            }
        }
    }
    private void onEntityDeath(LivingDeathEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        int increase = MomotinkerConfig.shortterminvestments_increase_ratio.get();
        int reduce = MomotinkerConfig.shortterminvestments_reduce_ratio.get();
        int d = RANDOM.nextInt(100);
        if (a instanceof Player player){
            int c=getMainhandModifierlevel(player,MomotinkerModifiers.shortterminvestments.getId());
            ModDataNBT data= ToolStack.from(player.getMainHandItem()).getPersistentData();
            if (c>0&&!data.getString(getResourceLocation("shorttermname")).isEmpty()){
                if (d<increase){
                    data.putFloat(getResource("shorttermindex"), data.getFloat(getResource("shorttermindex"))*1.3f);
                }else if (d>=100-reduce){
                    data.putFloat(getResource("shorttermindex"), data.getFloat(getResource("shorttermindex"))*0.85f);
                }
            }
        }
        if (b instanceof Player player&&a!=null){
            int c=getMainhandModifierlevel(player,MomotinkerModifiers.shortterminvestments.getId());
            ModDataNBT data= ToolStack.from(player.getMainHandItem()).getPersistentData();
            if (c>0&&!data.getString(getResourceLocation("shorttermname")).isEmpty()){
                if (d<increase){
                    data.putFloat(getResource("shorttermindex"), data.getFloat(getResource("shorttermindex"))*1.4f);
                }else if (d>=100-reduce){
                    data.putFloat(getResource("shorttermindex"), data.getFloat(getResource("shorttermindex"))*0.8f);
                }
            }
        }
    }
    private void onItemEvent(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        int increase = MomotinkerConfig.shortterminvestments_increase_ratio.get();
        int reduce = MomotinkerConfig.shortterminvestments_reduce_ratio.get();
        int d = RANDOM.nextInt(100);
        if (player!=null&& event.getState()!=null){
            int a=getMainhandModifierlevel(player,MomotinkerModifiers.shortterminvestments.getId());
            ModDataNBT data= ToolStack.from(player.getMainHandItem()).getPersistentData();
            if (a>0&&!data.getString(getResourceLocation("shorttermname")).isEmpty()){
                if (d<increase){
                    data.putFloat(getResource("shorttermindex"), data.getFloat(getResource("shorttermindex"))*1.2f);
                }else if (d>=100-reduce){
                    data.putFloat(getResource("shorttermindex"), data.getFloat(getResource("shorttermindex"))*0.9f);
                }
            }
        }
    }
}