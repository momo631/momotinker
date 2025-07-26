package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;


public class FilledWithHunger extends momomodifier {
    public FilledWithHunger() {
        MinecraftForge.EVENT_BUS.addListener(this::onEntityDeath);
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof Player player) {
            player.getFoodData().setExhaustion(player.getFoodData().getExhaustionLevel() * 5f);
        }
    }

    private void onEntityDeath(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player && event.getEntity() != null && getAllModifierlevel(player, MomotinkerModifiers.filledwithhunger.getId()) > 0) {
            player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() + 6 + RANDOM.nextInt(6));
            player.getFoodData().setSaturation(player.getFoodData().getSaturationLevel() + 4 + RANDOM.nextInt(4));
        }
        if (event.getEntity() instanceof Player player && getAllModifierlevel(player, MomotinkerModifiers.filledwithhunger.getId()) > 0) {
            if(player.getFoodData().getSaturationLevel()>20){
                player.getFoodData().setSaturation(20);
                event.setCanceled(true);
                player.heal(player.getMaxHealth());
            }else
            if(player.getFoodData().getSaturationLevel()<=20&&player.getFoodData().getFoodLevel()>20){
                player.getFoodData().setFoodLevel(20);
                event.setCanceled(true);
                player.heal(player.getMaxHealth());
            }
        }
    }

    private void livinghurtevent(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (a instanceof Player player&&getAllModifierlevel(player,MomotinkerModifiers.filledwithhunger.getId())>0){
            if(player.getFoodData().getSaturationLevel()>20){
                player.getFoodData().setSaturation(player.getFoodData().getSaturationLevel()-(int) (event.getAmount()));
                event.setAmount(0);
            }else
            if(player.getFoodData().getSaturationLevel()<=20&&player.getFoodData().getFoodLevel()>20){
                player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel()-(int) (event.getAmount()));
                event.setAmount(0);
            }
        }
        if (b instanceof Player player&&getAllModifierlevel(player,MomotinkerModifiers.filledwithhunger.getId())>0){
            if(player.getFoodData().getSaturationLevel()>20){
                event.setAmount(event.getAmount()+(player.getFoodData().getSaturationLevel()-20)*0.06f);
            }else
            if(player.getFoodData().getFoodLevel()>20){
                event.setAmount(event.getAmount()+(player.getFoodData().getFoodLevel()-20)*0.03f);
            }
        }
    }
}