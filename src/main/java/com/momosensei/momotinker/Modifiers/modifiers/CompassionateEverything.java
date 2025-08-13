package com.momosensei.momotinker.Modifiers.modifiers;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

import static com.momosensei.momotinker.util.PenetratingDamage.reflectionPenetratingDamage;


public class CompassionateEverything extends momomodifier {
    public CompassionateEverything() {
        MinecraftForge.EVENT_BUS.addListener(this::livingattackevent);
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }
    public static final ResourceLocation compassionateeverythingpoints = Momotinker.getResource("compassionateeverythingpoints");

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(compassionateeverythingpoints);
        return null;
    }

    @Nullable
    @Override
    public Component requirementsError(ModifierEntry entry) {
        return Component.translatable("recipe.momotinker.modifier.compassionateeverything");
    }

    @Override
    public @NotNull List<ModifierEntry> displayModifiers(ModifierEntry entry) {
        return List.of(new ModifierEntry(MomotinkerModifiers.oversadsin.getId(), 1));
    }

    @Override
    public Component validate(IToolStackView tool, ModifierEntry modifier) {
        if (tool.getModifierLevel(MomotinkerModifiers.oversadsin.getId()) > 0
                && tool.getModifierLevel(MomotinkerModifiers.eternalanger.getId()) == 0
                && tool.getModifierLevel(MomotinkerModifiers.slackatmosphere.getId()) == 0
                && tool.getModifierLevel(MomotinkerModifiers.thepinnacleofarrogance.getId()) == 0
                && tool.getModifierLevel(MomotinkerModifiers.filledwithhunger.getId()) == 0
                && tool.getModifierLevel(MomotinkerModifiers.resentmentknives.getId()) == 0
                && tool.getModifierLevel(MomotinkerModifiers.forbiddenfruit.getId()) == 0
                && tool.getModifierLevel(MomotinkerModifiers.gainsalone.getId()) == 0) {
            return null;
        }
        return requirementsError(modifier);
    }

    private void livingattackevent(LivingAttackEvent event) {
        LivingEntity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player&&a!=null&&getMainhandModifierlevel(player,MomotinkerModifiers.compassionateeverything.getId())>0){
            ToolStack tool = ToolStack.from(player.getMainHandItem());
            ModDataNBT data = tool.getPersistentData();
            if (data.getFloat(compassionateeverythingpoints)>a.getMaxHealth()){
                reflectionPenetratingDamage(a,player,a.getMaxHealth()*0.25f);
                data.putFloat(compassionateeverythingpoints,data.getFloat(compassionateeverythingpoints)-a.getMaxHealth());
            }
        }
    }
    @Override
    public LegacyDamageSource modifyDamageSource(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical, LegacyDamageSource source) {
        if (attacker !=null&& target !=null && getMainhandModifierlevel(attacker,MomotinkerModifiers.compassionateeverything.getId())>0) {
            return source.setBypassArmor().setBypassInvul().setBypassInvulnerableTime().setBypassMagic().setBypassEnchantment().setBypassShield();
        }
        return source;
    }

    @Override
    public LegacyDamageSource modifyArrowDamageSource(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, AbstractArrow arrow, @org.jetbrains.annotations.Nullable LivingEntity attacker, @NotNull Entity target, LegacyDamageSource source) {
        if (attacker !=null && arrow != null && getMainhandModifierlevel(attacker, MomotinkerModifiers.compassionateeverything.getId()) > 0) {
            return source.setBypassArmor().setBypassInvul().setBypassInvulnerableTime().setBypassMagic().setBypassEnchantment().setBypassShield();
        }
        return source;
    }
    private void livinghurtevent(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player&&a instanceof LivingEntity&& getMainhandModifierlevel(player, MomotinkerModifiers.compassionateeverything.getId()) > 0){
            ToolStack tool = ToolStack.from(player.getMainHandItem());
            ModDataNBT data = tool.getPersistentData();
            data.putFloat(compassionateeverythingpoints,data.getFloat(compassionateeverythingpoints)+event.getAmount());
            event.setAmount(0);
        }
    }
    @Override
    public void addAttributes(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentSlot equipmentSlot, BiConsumer<Attribute, AttributeModifier> biConsumer) {
        ModDataNBT a = iToolStackView.getPersistentData();
        if (a.getFloat(compassionateeverythingpoints)>0) {
            biConsumer.accept(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("93DE1B9F-3DEE-45E6-F333-9FD1E1E4214D"), Attributes.MAX_HEALTH.getDescriptionId(), a.getFloat(compassionateeverythingpoints), AttributeModifier.Operation.ADDITION));
        }
    }
}