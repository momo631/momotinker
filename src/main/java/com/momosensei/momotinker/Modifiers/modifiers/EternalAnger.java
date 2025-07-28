package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import com.momosensei.momotinker.util.AttackUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.Util;

import javax.annotation.Nullable;
import java.util.List;


public class EternalAnger extends momomodifier {
    public EternalAnger() {
        MinecraftForge.EVENT_BUS.addListener(this::onEntityDeath);
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    public static final ResourceLocation eternalangerodds = Momotinker.getResource("eternalangerodds");
    public static final ResourceLocation eternalangerpoints = Momotinker.getResource("eternalangerpoints");

    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Nullable
    @Override
    public Component requirementsError(ModifierEntry entry) {
        return Component.translatable("recipe.momotinker.modifier.eternalanger");
    }
    @Override
    public @NotNull List<ModifierEntry> displayModifiers(ModifierEntry entry) {
        return List.of(new ModifierEntry(MomotinkerModifiers.overangersin.getId(),1));
    }
    @Override
    public Component validate(IToolStackView tool, ModifierEntry modifier) {
        if (tool.getModifierLevel(MomotinkerModifiers.overangersin.getId())>0
                &&tool.getModifierLevel(MomotinkerModifiers.slackatmosphere.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.thepinnacleofarrogance.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.filledwithhunger.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.resentmentknives.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.forbiddenfruit.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.gainsalone.getId())==0){
            return null;
        }
        return requirementsError(modifier);
    }
    @Override
    public Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(eternalangerodds);
        iToolStackView.getPersistentData().remove(eternalangerpoints);
        return null;
    }
    private void onEntityDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player && getMainhandModifierlevel(player, MomotinkerModifiers.eternalanger.getId()) > 0) {
            ToolStack tool = ToolStack.from(player.getMainHandItem());
            ModDataNBT data = tool.getPersistentData();
            int a = RANDOM.nextInt(99);
            if (a < data.getFloat(eternalangerodds)) {
                event.setCanceled(true);
                player.setHealth(player.getMaxHealth() * 0.15f);
                List<LivingEntity> ls0 = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(2));
                for (LivingEntity targets : ls0) {
                    if (targets != player && targets != null) {
                        targets.invulnerableTime = 0;
                        AttackUtil.attackEntity(ToolStack.from(player.getMainHandItem()), player, InteractionHand.MAIN_HAND, targets, () -> 1, true, Util.getSlotType(InteractionHand.MAIN_HAND), ToolStack.from(player.getMainHandItem()).getStats().get(ToolStats.ATTACK_DAMAGE), 1f, false, true, true, false);
                        targets.invulnerableTime = 0;
                    }
                }
            }
            if (!player.isDeadOrDying() && data.getFloat(eternalangerodds) > 0) {
                data.putFloat(eternalangerodds, data.getFloat(eternalangerodds) * 0.5f);
                data.putInt(eternalangerpoints, data.getInt(eternalangerpoints) + 6);
            } else if (player.isDeadOrDying()) {
                data.putFloat(eternalangerodds, 300);
                data.putInt(eternalangerpoints, 0);
            }
        }
    }

    private void livinghurtevent(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (a instanceof Player player&& getMainhandModifierlevel(player, MomotinkerModifiers.eternalanger.getId()) > 0){
            ToolStack tool = ToolStack.from(player.getMainHandItem());
            ModDataNBT data = tool.getPersistentData();
            event.setAmount(event.getAmount()*(1f+data.getInt(eternalangerpoints)*0.05f));
            if (data.getFloat(eternalangerodds) == 0) {
                data.putFloat(eternalangerodds, 300);
            }
        }
        if (b instanceof Player player&&a instanceof LivingEntity&& getMainhandModifierlevel(player, MomotinkerModifiers.eternalanger.getId()) > 0){
            ToolStack tool = ToolStack.from(player.getMainHandItem());
            ModDataNBT data = tool.getPersistentData();
            event.setAmount(event.getAmount()*(1f+data.getInt(eternalangerpoints)*0.1f));
            if (data.getFloat(eternalangerodds) == 0) {
                data.putFloat(eternalangerodds, 300);
            }
        }
    }
}