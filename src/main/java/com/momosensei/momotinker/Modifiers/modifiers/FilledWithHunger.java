package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;


public class FilledWithHunger extends momomodifier {
    public FilledWithHunger() {
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Nullable
    @Override
    public Component requirementsError(ModifierEntry entry) {
        return Component.translatable("recipe.momotinker.modifier.filledwithhunger");
    }
    @Override
    public @NotNull List<ModifierEntry> displayModifiers(ModifierEntry entry) {
        return List.of(new ModifierEntry(MomotinkerModifiers.overeatingsin.getId(),1));
    }
    @Override
    public Component validate(IToolStackView tool, ModifierEntry modifier) {
        if (tool.getModifierLevel(MomotinkerModifiers.overeatingsin.getId())>0
                &&tool.getModifierLevel(MomotinkerModifiers.eternalanger.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.slackatmosphere.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.thepinnacleofarrogance.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.resentmentknives.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.forbiddenfruit.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.gainsalone.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.compassionateeverything.getId())==0){
            return null;
        }
        return requirementsError(modifier);
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof Player player&&player.tickCount%5==0) {
            player.getFoodData().setExhaustion(player.getFoodData().getExhaustionLevel() * 2f);
        }
    }

    @Override
    public void OnEntityDeath(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player && event.getEntity() != null && getAllModifierlevel(player, MomotinkerModifiers.filledwithhunger.getId()) > 0) {
            player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() + 3 + RANDOM.nextInt(5));
            player.getFoodData().setSaturation(player.getFoodData().getSaturationLevel() + 2 + RANDOM.nextInt(3));
        }
        if (event.getEntity() instanceof Player player && getAllModifierlevel(player, MomotinkerModifiers.filledwithhunger.getId()) > 0) {
            if(player.getFoodData().getSaturationLevel()>20){
                player.getFoodData().setSaturation(20);
                event.setCanceled(true);
                player.setHealth(player.getMaxHealth());
            }else
            if(player.getFoodData().getSaturationLevel()<=20&&player.getFoodData().getFoodLevel()>20){
                player.getFoodData().setFoodLevel(20);
                event.setCanceled(true);
                player.setHealth(player.getMaxHealth());
            }
        }
    }

    @Override
    public void OnLivingHurt(LivingHurtEvent event) {
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

    }
    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity attacker =context.getAttacker();
        if (attacker instanceof Player player&&context.getLivingTarget()!=null&&getAllModifierlevel(player,MomotinkerModifiers.filledwithhunger.getId())>0){
            if(player.getFoodData().getSaturationLevel()>20){
                return damage+(player.getFoodData().getSaturationLevel()-20)*0.1f;
            }else
            if(player.getFoodData().getFoodLevel()>20){
                return damage+(player.getFoodData().getFoodLevel()-20)*0.06f;
            }
        }
        return damage;
    }
    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker, @javax.annotation.Nullable LivingEntity target) {
        if (attacker instanceof ServerPlayer player && projectile instanceof AbstractArrow arrow&&target!=null){
            if (getAllModifierlevel(player,MomotinkerModifiers.filledwithhunger.getId())>0){
                if(player.getFoodData().getSaturationLevel()>20){
                    arrow.setBaseDamage(arrow.getBaseDamage()*(1F + (player.getFoodData().getSaturationLevel()-20)*0.05f));
                }else
                if(player.getFoodData().getFoodLevel()>20){
                    arrow.setBaseDamage(arrow.getBaseDamage()*(1F + (player.getFoodData().getFoodLevel()-20)*0.03f));
                }
            }
        }
        return false;
    }
}