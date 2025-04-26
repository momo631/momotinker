package com.momosensei.momotinker.Modifiers;


import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.ModifyDamageSourceModifierHook;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ProcessLootModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.DamageDealtModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.EntityInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.KeybindInteractModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BlockBreakModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.BowAmmoModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileLaunchModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.*;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public abstract class momomodifier extends Modifier implements MeleeDamageModifierHook, MeleeHitModifierHook, DamageDealtModifierHook,
        BowAmmoModifierHook, ProjectileHitModifierHook, ProjectileLaunchModifierHook,KeybindInteractModifierHook, ProcessLootModifierHook,
        EquipmentChangeModifierHook, InventoryTickModifierHook, OnAttackedModifierHook, TooltipModifierHook, AttributesModifierHook,
        ModifyDamageModifierHook, ModifierRemovalHook, BlockBreakModifierHook, EntityInteractionModifierHook, ToolStatsModifierHook,
        ToolDamageModifierHook, ModifyDamageSourceModifierHook, VolatileDataModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.DAMAGE_DEALT, ModifierHooks.MELEE_DAMAGE, ModifierHooks.MELEE_HIT);
        builder.addHook(this, ModifierHooks.BOW_AMMO, ModifierHooks.PROJECTILE_HIT, ModifierHooks.PROJECTILE_LAUNCH);
        builder.addHook(this, ModifierHooks.EQUIPMENT_CHANGE, ModifierHooks.INVENTORY_TICK, ModifierHooks.ON_ATTACKED);
        builder.addHook(this, ModifierHooks.TOOLTIP, ModifierHooks.REMOVE, ModifierHooks.MODIFY_DAMAGE);
        builder.addHook(this, ModifierHooks.BLOCK_BREAK, ModifierHooks.ENTITY_INTERACT, ModifierHooks.TOOL_STATS);
        builder.addHook(this, ModifierHooks.ARMOR_INTERACT, ModifierHooks.ATTRIBUTES, ModifierHooks.PROCESS_LOOT);
        builder.addHook(this, ModifierHooks.TOOL_DAMAGE, ModifierHooks.VOLATILE_DATA,EtSTLibHooks.MODIFY_DAMAGE_SOURCE);
    }

    public momomodifier() {
        MinecraftForge.EVENT_BUS.addListener(this::LivingHurtEvent);
        MinecraftForge.EVENT_BUS.addListener(this::LivingAttackEvent);
        MinecraftForge.EVENT_BUS.addListener(this::LivingDamageEvent);
    }

    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity livingEntity) {
        return amount;
    }

    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
    }

    public void processLoot(IToolStackView tool, ModifierEntry modifier, List<ItemStack> list, LootContext context) {
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        return this.onGetMeleeDamage(tool, modifier, context, baseDamage, damage);
    }

    @Override
    public void onDamageDealt(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, LivingEntity entity, DamageSource damageSource, float amount, boolean isDirectDamage) {
        this.modifierDamageDealt(tool, modifier, context, slotType, entity, damageSource, amount, isDirectDamage);
    }

    public float onGetMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        return damage;
    }

    public void modifierDamageDealt(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, LivingEntity entity, DamageSource damageSource, float amount, boolean isDirectDamage) {
    }

    public boolean isNoLevels() {
        return false;
    }

    public @NotNull Component getDisplayName(int level) {
        return this.isNoLevels() ? super.getDisplayName() : super.getDisplayName(level);
    }

    @Override
    public ItemStack findAmmo(IToolStackView tool, ModifierEntry modifiers, LivingEntity livingEntity, ItemStack itemStack, Predicate<ItemStack> predicate) {
        return this.modifierFindAmmo(tool, modifiers, livingEntity, itemStack, predicate);
    }

    @Override
    public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifiers, LivingEntity livingEntity, Projectile projectile, @Nullable AbstractArrow abstractArrow, ModDataNBT ModDataNBT, boolean primary) {
        this.modifierOnProjectileLaunch(tool, modifiers, livingEntity, projectile, abstractArrow, ModDataNBT, primary);
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker, @javax.annotation.Nullable LivingEntity target) {
        return this.modifierOnProjectileHitEntity(modifiers, persistentData, modifier, projectile, hit, attacker, target);
    }

    public ItemStack modifierFindAmmo(IToolStackView tool, ModifierEntry modifiers, LivingEntity livingEntity, ItemStack itemStack, Predicate<ItemStack> predicate) {
        return itemStack;
    }

    public void modifierOnProjectileLaunch(IToolStackView tool, ModifierEntry modifiers, LivingEntity livingEntity, Projectile projectile, @Nullable AbstractArrow abstractArrow, ModDataNBT ModDataNBT, boolean primary) {
    }

    public boolean modifierOnProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker, @javax.annotation.Nullable LivingEntity target) {
        return false;
    }

    @Override
    public void onEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        this.modifierOnEquip(tool, modifier, context);
    }

    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        this.modifierOnUnequip(tool, modifier, context);
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        this.modifierOnInventoryTick(iToolStackView, modifierEntry, level, entity, index, b, b1, itemStack);
    }

    @Override
    public void onAttacked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        this.modifierOnAttacked(tool, modifier, context, slotType, source, amount, isDirectDamage);
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        return this.modifierDamageTaken(tool, modifier, context, slotType, source, amount, isDirectDamage);
    }

    @Override
    public void afterBlockBreak(IToolStackView tool, ModifierEntry modifier, ToolHarvestContext context) {
        this.modifierAfterBlockBreak(tool, modifier, context);
    }

    @Override
    public InteractionResult beforeEntityUse(IToolStackView tool, ModifierEntry modifier, Player player, Entity target, InteractionHand hand, InteractionSource source) {
        return this.modifierBeforeEntityUse(tool, modifier, player, target, hand, source);
    }

    @Override
    public InteractionResult afterEntityUse(IToolStackView tool, ModifierEntry modifier, Player player, LivingEntity target, InteractionHand hand, InteractionSource source) {
        return this.modifierAfterEntityUse(tool, modifier, player, target, hand, source);
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        this.modifierAddToolStats(context, modifier, builder);
    }
    @Override
    public void addVolatileData(IToolContext context, ModifierEntry modifier, ToolDataNBT volatileData) {
        this.modifieraddVolatileData(context, modifier, volatileData);
    }

    private void modifieraddVolatileData(IToolContext context, ModifierEntry modifier, ModDataNBT modDataNBT) {
    }

    public void modifierOnEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
    }

    public void modifierOnUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
    }

    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
    }

    public void modifierOnAttacked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
    }

    public float modifierDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        return amount;
    }

    public void modifierAfterBlockBreak(IToolStackView tool, ModifierEntry modifier, ToolHarvestContext context) {
    }

    public InteractionResult modifierBeforeEntityUse(IToolStackView tool, ModifierEntry modifier, Player player, Entity target, InteractionHand hand, InteractionSource source) {
        return InteractionResult.PASS;
    }

    public InteractionResult modifierAfterEntityUse(IToolStackView tool, ModifierEntry modifier, Player player, LivingEntity target, InteractionHand hand, InteractionSource source) {
        return InteractionResult.PASS;
    }

    public void modifierAddToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {

    }

    @Nullable
    @Override
    public Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        this.onModifierRemoved(iToolStackView);
        return null;
    }

    public void onModifierRemoved(IToolStackView tool) {
    }

    public void LivingHurtEvent(LivingHurtEvent target) {
    }

    public void LivingAttackEvent(LivingAttackEvent target) {
    }

    public void LivingDamageEvent(LivingDamageEvent target) {
    }

    @Override
    public boolean startInteract(IToolStackView tool, ModifierEntry modifier, Player player, EquipmentSlot slot, TooltipKey keyModifier) {
        return KeybindInteractModifierHook.super.startInteract(tool, modifier, player, slot, keyModifier);
    }

    public static int getAllModifierlevel(LivingEntity entity, ModifierId modifierId) {
        return ModifierUtil.getModifierLevel(entity.getItemBySlot(EquipmentSlot.MAINHAND), modifierId)
                + ModifierUtil.getModifierLevel(entity.getItemBySlot(EquipmentSlot.OFFHAND), modifierId)
                + ModifierUtil.getModifierLevel(entity.getItemBySlot(EquipmentSlot.HEAD), modifierId)
                + ModifierUtil.getModifierLevel(entity.getItemBySlot(EquipmentSlot.CHEST), modifierId)
                + ModifierUtil.getModifierLevel(entity.getItemBySlot(EquipmentSlot.LEGS), modifierId)
                + ModifierUtil.getModifierLevel(entity.getItemBySlot(EquipmentSlot.FEET), modifierId);
    }

    public static int getArmorModifierlevel(LivingEntity entity, ModifierId modifierId) {
        return ModifierUtil.getModifierLevel(entity.getItemBySlot(EquipmentSlot.HEAD), modifierId)
                + ModifierUtil.getModifierLevel(entity.getItemBySlot(EquipmentSlot.CHEST), modifierId)
                + ModifierUtil.getModifierLevel(entity.getItemBySlot(EquipmentSlot.LEGS), modifierId)
                + ModifierUtil.getModifierLevel(entity.getItemBySlot(EquipmentSlot.FEET), modifierId);
    }

    public static int getMainhandModifierlevel(LivingEntity entity, ModifierId modifierId) {
        return ModifierUtil.getModifierLevel(entity.getItemBySlot(EquipmentSlot.MAINHAND), modifierId);
    }

    public static int getOffhandModifierlevel(LivingEntity entity, ModifierId modifierId) {
        return ModifierUtil.getModifierLevel(entity.getItemBySlot(EquipmentSlot.OFFHAND), modifierId);
    }

    public static float getRemainingDurability(IToolStackView tool) {
        return (tool.getStats().getInt(ToolStats.DURABILITY)-tool.getDamage());
    }

    public static float getbonus(LivingEntity entity, int status) {
        return (float) (entity.getAttributeValue(Attributes.MOVEMENT_SPEED) * entity.getDeltaMovement().length() * status);
    }
}