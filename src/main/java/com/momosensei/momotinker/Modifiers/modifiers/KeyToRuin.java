package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import com.momosensei.momotinker.register.MomotinkerToolDefinitions;
import com.momosensei.momotinker.register.MomotinkerTools;
import com.momosensei.momotinker.util.AttackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierTraitHook;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.Util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;


public class KeyToRuin extends momomodifier {
    public KeyToRuin() {
    }
    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Nullable
    @Override
    public Component requirementsError(ModifierEntry entry) {
        return Component.translatable("recipe.momotinker.modifier.key_to_ruin");
    }
    @Override
    public @NotNull List<ModifierEntry> displayModifiers(ModifierEntry entry) {
        return List.of(new ModifierEntry(MomotinkerModifiers.frombrilliance.getId(),1));
    }
    @Override
    public Component validate(IToolStackView tool, ModifierEntry modifier) {
        if (tool.getItem()==MomotinkerTools.entropy_burning_cube.get()
                ||tool.getItem()==MomotinkerTools.nyarlathotep.get()
                ||tool.getItem()==MomotinkerTools.pocket_watch.get()
                ||tool.getItem()==MomotinkerTools.nodens.get()){
            return null;
        }
        return requirementsError(modifier);
    }
    @Override
    public void addTraits(IToolContext context, ModifierEntry modifier, ModifierTraitHook.TraitBuilder builder, boolean firstEncounter) {
        for (ModifierEntry entry : context.getModifierList()) {
            if (entry.getModifier() != modifier.getModifier()&&firstEncounter) {
                builder.add(entry.getId(),entry.getLevel());
            }
        }
    }


    public static final ResourceLocation collaborative_cooldown = Momotinker.getResource("collaborative_cooldown");
    @Override
    public Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(collaborative_cooldown);
        return null;
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (entity instanceof Player player){
            if (stack.getItem() == MomotinkerTools.entropy_burning_cube.get()) {
                Evolution(player, stack, MomotinkerTools.nyarlathotep.get(), MomotinkerToolDefinitions.NYARLATHOTEP);
            }
            if (stack.getItem() == MomotinkerTools.pocket_watch.get()) {
                Evolution(player, stack, MomotinkerTools.nodens.get(), MomotinkerToolDefinitions.NODENS);
            }
            if (stack.getItem() == MomotinkerTools.nodens.get()) {
                ModDataNBT data = tool.getPersistentData();
                if (data.getFloat(collaborative_cooldown)>0){
                    data.putFloat(collaborative_cooldown,data.getFloat(collaborative_cooldown)-1);
                }
                if (data.getFloat(collaborative_cooldown)<0){
                    data.putFloat(collaborative_cooldown,0);
                }
            }
        }
    }
    public void Evolution(Player player,ItemStack stack, Item item, ToolDefinition definition) {
        ItemStack item1 = createNewTool(stack,item, definition);
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            if (player.getInventory().getItem(i) == stack) {
                player.getInventory().setItem(i, item1);
                break;
            }
        }
    }
    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("modifier.momotinker.tooltip.key_to_ruin_1").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC));
        if (tool.getItem()==MomotinkerTools.nyarlathotep.get()) {
            tooltip.add(Component.translatable("modifier.momotinker.tooltip.key_to_ruin_nyarlathotep").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC));
        }
        if (tool.getItem()==MomotinkerTools.nodens.get()) {
            tooltip.add(Component.translatable("modifier.momotinker.tooltip.key_to_ruin_nodens").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC));
        }
        tooltip.add(Component.translatable("modifier.momotinker.tooltip.key_to_ruin_2").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC));

    }


    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        if (tool==MomotinkerTools.nyarlathotep.get()) {
            for (ModifierEntry entry1 : tool.getModifierList()) {
                if (entry1.getModifier() != modifier.getModifier()) {
                    damage = entry1.getHook(ModifierHooks.MELEE_DAMAGE).getMeleeDamage(tool, modifier, context, baseDamage, damage);
                }
            }
        }
        return damage;
    }
    @Override
    public void modifierDamageDealt(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, LivingEntity entity, DamageSource damageSource, float amount, boolean isDirectDamage) {
        if (tool==MomotinkerTools.nyarlathotep.get()) {
            for (ModifierEntry entry : tool.getModifierList()) {
                if (entry.getModifier() != modifier.getModifier()) {
                    entry.getHook(ModifierHooks.DAMAGE_DEALT).onDamageDealt(tool, modifier, context, slotType, entity, damageSource, amount * 0.25f, isDirectDamage);
                }
            }
        }
    }
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (tool==MomotinkerTools.nyarlathotep.get()) {
            for (ModifierEntry entry : tool.getModifierList()) {
                if (entry.getModifier() != modifier.getModifier()) {
                    entry.getHook(ModifierHooks.MELEE_HIT).afterMeleeHit(tool, modifier, context, damageDealt * 0.25f);
                }
            }
        }
    }
//    @Override
//    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
//        if (tool==MomotinkerTools.nyarlathotep.get()) {
//            for (ModifierEntry entry : tool.getModifierList()) {
//                if (entry.getModifier() != modifier.getModifier()) {
//                    knockback = entry.getHook(ModifierHooks.MELEE_HIT).beforeMeleeHit(tool, modifier, context, damage, baseKnockback, knockback);
//                }
//            }
//        }
//        return knockback;
//    }
    @Override
    public void failedMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageAttempted) {
        if (tool==MomotinkerTools.nyarlathotep.get()) {
            for (ModifierEntry entry : tool.getModifierList()) {
                if (entry.getModifier() != modifier.getModifier()) {
                    entry.getHook(ModifierHooks.MELEE_HIT).failedMeleeHit(tool, modifier, context, damageAttempted);
                }
            }
        }
    }
    private static final ThreadLocal<Boolean> IN_HANDLER = ThreadLocal.withInitial(() -> false);

    @Override
    public void OnLivingHurt(LivingHurtEvent event) {
        if (IN_HANDLER.get()) return;
        IN_HANDLER.set(true);
        try {
            LivingEntity a = event.getEntity();
            Entity b = event.getSource().getEntity();
            if (b instanceof Player player && a != null) {
                for (int j = 0; j < player.getInventory().items.size(); j++) {
                    ItemStack stack = player.getInventory().getItem(j);
                    if (stack.getItem() != MomotinkerTools.nodens.get()) continue;
                    ToolStack tool = ToolStack.from(stack);
                    ModDataNBT data = tool.getPersistentData();
                    if (data.getFloat(collaborative_cooldown) == 0) {
                        data.putFloat(collaborative_cooldown, 20);
                        a.invulnerableTime = 0;
                        AttackUtil.attackEntity(tool, player, InteractionHand.MAIN_HAND, a,
                                () -> 1, true, Util.getSlotType(InteractionHand.MAIN_HAND),
                                tool.getStats().get(ToolStats.ATTACK_DAMAGE),
                                0.25f, false, true, true, false);
                        a.invulnerableTime = 0;
                    }
                }
            }
        } finally {
            IN_HANDLER.set(false);
        }
    }
}