package com.momosensei.momotinker.Modifiers.modifiers;


import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.level.BlockEvent;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.momosensei.momotinker.Momotinker.getResource;
import static com.momosensei.momotinker.Momotinker.getResourceLocation;

public class ShortTermInvestments extends momomodifier {
    public ShortTermInvestments() {

        MinecraftForge.EVENT_BUS.addListener(this::onEntityDeath);
        MinecraftForge.EVENT_BUS.addListener(this::onItemEvent);
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
        if (tool.getModifierLevel(MomotinkerModifiers.intendingplunder.getId())>0&&tool.getModifierLevel(MomotinkerModifiers.longterminvestments.getId())==0){
            return null;
        }
        return requirementsError(modifier);
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        ModDataNBT data = tool.getPersistentData();
        if (player != null) {
            tooltip.add(net.minecraft.network.chat.Component.translatable("modifier.momotinker.tooltip.terminvestments1").append(data.getString(getResourceLocation("termname"))).withStyle(ChatFormatting.BLUE));
            tooltip.add(net.minecraft.network.chat.Component.translatable("modifier.momotinker.tooltip.terminvestments2").append(data.getFloat(getResource("termindex"))+"").withStyle(ChatFormatting.BLUE));

        }
    }
    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity attacker = context.getAttacker();
        int d = RANDOM.nextInt(100);
        if (attacker instanceof Player player&&!context.isExtraAttack()) {
            int c=getMainhandModifierlevel(player,MomotinkerModifiers.shortterminvestments.getId());
            ModDataNBT data= tool.getPersistentData();
            if (c>0&&!data.getString(getResourceLocation("termname")).isEmpty()){
                if (d<25){
                    data.putFloat(getResource("termindex"), data.getFloat(getResource("termindex"))*1.1f);
                }else if (d>=100-50){
                    data.putFloat(getResource("termindex"), data.getFloat(getResource("termindex"))*0.95f);
                }
            }
        }
        return damage;
    }
    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker, @javax.annotation.Nullable LivingEntity target) {
        int d = RANDOM.nextInt(100);
        if (attacker instanceof Player player&& projectile instanceof AbstractArrow arrow) {
            int c=getMainhandModifierlevel(player,MomotinkerModifiers.shortterminvestments.getId());
            ModDataNBT data= ToolStack.from(player.getMainHandItem()).getPersistentData();
            if (c>0&&!data.getString(getResourceLocation("termname")).isEmpty()){
                if (d<25){
                    data.putFloat(getResource("termindex"), data.getFloat(getResource("termindex"))*1.1f);
                }else if (d>=100-50){
                    data.putFloat(getResource("termindex"), data.getFloat(getResource("termindex"))*0.95f);
                }
            }
        }
        return false;
    }
    @Override
    public void OnLivingHurt(LivingHurtEvent event) {
        Entity a = event.getEntity();
        //int increase = MomotinkerConfig.shortterminvestments_increase_ratio.get();
        //int reduce = MomotinkerConfig.shortterminvestments_reduce_ratio.get();
        int d = RANDOM.nextInt(100);
        if (a instanceof Player player){
            int c=getMainhandModifierlevel(player,MomotinkerModifiers.shortterminvestments.getId());
            ModDataNBT data= ToolStack.from(player.getMainHandItem()).getPersistentData();
            if (c>0&&!data.getString(getResourceLocation("termname")).isEmpty()){
                if (d<25){
                    data.putFloat(getResource("termindex"), data.getFloat(getResource("termindex"))*1.1f);
                }else if (d>=100-50){
                    data.putFloat(getResource("termindex"), data.getFloat(getResource("termindex"))*0.95f);
                }
            }
        }
    }
    private void onEntityDeath(LivingDeathEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        //int increase = MomotinkerConfig.shortterminvestments_increase_ratio.get();
        //int reduce = MomotinkerConfig.shortterminvestments_reduce_ratio.get();
        int d = RANDOM.nextInt(100);
        if (a instanceof Player player){
            int c=getMainhandModifierlevel(player,MomotinkerModifiers.shortterminvestments.getId());
            ModDataNBT data= ToolStack.from(player.getMainHandItem()).getPersistentData();
            if (c>0&&!data.getString(getResourceLocation("termname")).isEmpty()){
                if (d<25){
                    data.putFloat(getResource("termindex"), data.getFloat(getResource("termindex"))*1.3f);
                }else if (d>=100-50){
                    data.putFloat(getResource("termindex"), data.getFloat(getResource("termindex"))*0.85f);
                }
            }
        }
        if (b instanceof Player player&&a!=null){
            int c=getMainhandModifierlevel(player,MomotinkerModifiers.shortterminvestments.getId());
            ModDataNBT data= ToolStack.from(player.getMainHandItem()).getPersistentData();
            if (c>0&&!data.getString(getResourceLocation("termname")).isEmpty()){
                if (d<25){
                    data.putFloat(getResource("termindex"), data.getFloat(getResource("termindex"))*1.4f);
                }else if (d>=100-50){
                    data.putFloat(getResource("termindex"), data.getFloat(getResource("termindex"))*0.8f);
                }
            }
        }
    }
    private void onItemEvent(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        //int increase = MomotinkerConfig.shortterminvestments_increase_ratio.get();
        //int reduce = MomotinkerConfig.shortterminvestments_reduce_ratio.get();
        int d = RANDOM.nextInt(100);
        if (player!=null&& event.getState()!=null){
            int a=getMainhandModifierlevel(player,MomotinkerModifiers.shortterminvestments.getId());
            ModDataNBT data= ToolStack.from(player.getMainHandItem()).getPersistentData();
            if (a>0&&!data.getString(getResourceLocation("termname")).isEmpty()){
                if (d<25){
                    data.putFloat(getResource("termindex"), data.getFloat(getResource("termindex"))*1.2f);
                }else if (d>=100-50){
                    data.putFloat(getResource("termindex"), data.getFloat(getResource("termindex"))*0.9f);
                }
            }
        }
    }
}