package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerEffects;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;


public class ForbiddenFruit extends momomodifier {
    public ForbiddenFruit() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    public static final ResourceLocation forbiddenfruitpoints = Momotinker.getResource("forbiddenfruitpoints");

    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Nullable
    @Override
    public Component requirementsError(ModifierEntry entry) {
        return Component.translatable("recipe.momotinker.modifier.forbiddenfruit");
    }
    @Override
    public @NotNull List<ModifierEntry> displayModifiers(ModifierEntry entry) {
        return List.of(new ModifierEntry(MomotinkerModifiers.overindulgencesin.getId(),1));
    }
    @Override
    public Component validate(IToolStackView tool, ModifierEntry modifier) {
        if (tool.getModifierLevel(MomotinkerModifiers.overindulgencesin.getId())>0
                &&tool.getModifierLevel(MomotinkerModifiers.eternalanger.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.slackatmosphere.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.thepinnacleofarrogance.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.filledwithhunger.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.resentmentknives.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.gainsalone.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.compassionateeverything.getId()) == 0) {
            return null;
        }
        return requirementsError(modifier);
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof Player player&&player.tickCount%20==0) {
            List<LivingEntity> ls0 = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(30));
            for (LivingEntity living : ls0) {
                if (living!=null&&living!=player&&living.getAttributeValue(Attributes.ARMOR) < player.getAttributeValue(Attributes.ARMOR)){
                    living.addEffect(new MobEffectInstance(MomotinkerEffects.ReduceAllAttributes.get(),100,4));
                }
            }
        }
    }
    
    @Override
    public void addAttributes(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentSlot equipmentSlot, BiConsumer<Attribute, AttributeModifier> biConsumer) {
        ModDataNBT a = iToolStackView.getPersistentData();
        if (a.getFloat(forbiddenfruitpoints)>0) {
            biConsumer.accept(Attributes.ARMOR, new AttributeModifier(UUID.fromString("B9F145FA-2B39-AF5A-2111-1C0781E0B18C"), Attributes.ARMOR.getDescriptionId(), 0.1*a.getFloat(forbiddenfruitpoints), AttributeModifier.Operation.ADDITION));
        }
    }
    private void livinghurtevent(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (a instanceof Player player&&b instanceof LivingEntity living && getMainhandModifierlevel(player, MomotinkerModifiers.forbiddenfruit.getId()) > 0) {
            if (living.getAttributeValue(Attributes.ARMOR) < player.getAttributeValue(Attributes.ARMOR)) {
                float c = (float) (player.getAttributeValue(Attributes.ARMOR)-living.getAttributeValue(Attributes.ARMOR));
                if (c>=475)c=475;
                event.setAmount(event.getAmount()*(1f-c*0.002f));
            }
        }
        if (b instanceof Player player&&a instanceof LivingEntity living && getMainhandModifierlevel(player, MomotinkerModifiers.forbiddenfruit.getId()) > 0) {
            if (living.getAttributeValue(Attributes.ARMOR) < player.getAttributeValue(Attributes.ARMOR)) {
                float c = (float) (player.getAttributeValue(Attributes.ARMOR)-living.getAttributeValue(Attributes.ARMOR));
                event.setAmount(event.getAmount()*(1f+c*0.006f));
            }
        }
    }
    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity attacker =context.getAttacker();
        if (attacker instanceof Player player&&context.getLivingTarget()!=null&&getMainhandModifierlevel(player,MomotinkerModifiers.forbiddenfruit.getId())>0&&!context.isExtraAttack()){
            context.getLivingTarget().getAttribute(Attributes.ARMOR).setBaseValue(context.getLivingTarget().getAttributeValue(Attributes.ARMOR) * 0.6F);
            tool.getPersistentData().putFloat(forbiddenfruitpoints, tool.getPersistentData().getFloat(forbiddenfruitpoints) + (float) (context.getLivingTarget().getAttributeValue(Attributes.ARMOR) * 0.1F));
        }
        return damage;
    }
    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker, @javax.annotation.Nullable LivingEntity target) {
        if (attacker instanceof ServerPlayer player && projectile instanceof AbstractArrow arrow&&target!=null){
            if (getAllModifierlevel(player,MomotinkerModifiers.forbiddenfruit.getId())>0){
                ToolStack tool = ToolStack.from(player.getMainHandItem());
                target.getAttribute(Attributes.ARMOR).setBaseValue(target.getAttributeValue(Attributes.ARMOR) * 0.8F);
                tool.getPersistentData().putFloat(forbiddenfruitpoints, tool.getPersistentData().getFloat(forbiddenfruitpoints) + (float) (target.getAttributeValue(Attributes.ARMOR) * 0.05F));
            }
        }
        return false;
    }
}