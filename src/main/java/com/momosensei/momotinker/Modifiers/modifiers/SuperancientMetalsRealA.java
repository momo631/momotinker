package com.momosensei.momotinker.Modifiers.modifiers;


import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerConfig;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.*;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

import static com.momosensei.momotinker.tool.divine_punishment_spear.degenerate;
import static com.momosensei.momotinker.tool.divine_punishment_spear.sanctification;
import static com.momosensei.momotinker.tool.entropy_burning_cube.*;

public class SuperancientMetalsRealA extends momomodifier {
    public SuperancientMetalsRealA() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST,this::onEntityDeath);
    }
    public static final ResourceLocation stellarcorepoints = Momotinker.getResource("stellarcorepoints");
    public static final ResourceLocation crystallizedpoints = Momotinker.getResource("crystallizedpoints");
    public static final ResourceLocation liverizationpointa = Momotinker.getResource("liverizationpointa");
    public static final ResourceLocation liverizationpointb = Momotinker.getResource("liverizationpointb");

    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Override
    public @javax.annotation.Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(stellarcorepoints);
        iToolStackView.getPersistentData().remove(crystallizedpoints);
        iToolStackView.getPersistentData().remove(liverizationpointa);
        iToolStackView.getPersistentData().remove(liverizationpointb);
        return null;
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        if (modifier.getLevel() > 0) {
            ToolStats.DURABILITY.multiply(builder, 1.8);
            ToolStats.ATTACK_SPEED.multiply(builder, 1.8);
            ToolStats.ATTACK_DAMAGE.multiply(builder, 1.8);
            ToolStats.ACCURACY.multiply(builder, 1.8);
            ToolStats.DRAW_SPEED.multiply(builder, 1.8);
            ToolStats.VELOCITY.multiply(builder, 1.8);
            ToolStats.MINING_SPEED.multiply(builder, 1.8);
            ToolStats.ARMOR.multiply(builder, 1.8);
            ToolStats.ARMOR_TOUGHNESS.multiply(builder, 1.8);
            ToolStats.PROJECTILE_DAMAGE.multiply(builder, 1.8);
            ToolStats.KNOCKBACK_RESISTANCE.multiply(builder, 1.8);
            ToolStats.BLOCK_AMOUNT.multiply(builder, 1.8);
            ToolStats.BLOCK_ANGLE.multiply(builder, 1.8);
        }
    }
    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity livingEntity) {
        if (modifier.getLevel()>0){
            return (int) (amount * 0.1f);
        }
        return amount;
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        int stellarcore_limit = MomotinkerConfig.stellarcore_limit.get();
        int crystallized_limit = MomotinkerConfig.crystallized_limit.get();
        int liverization_limit = MomotinkerConfig.liverization_limit.get();
        ModDataNBT a = tool.getPersistentData();
        if (a.getInt(stellarcore)==stellarcore_limit) {
            if (entity instanceof Player player&&a.getInt(stellarcorepoints)!=player.getArmorValue()){
                a.putInt(stellarcorepoints,player.getArmorValue());
            }
        }
        if (a.getInt(crystallized)==crystallized_limit) {
            int amount =tool.getStats().getInt(ToolStats.DURABILITY);
            int b =((int) Math.log10(amount))-3;
            if (b>=0&&b!=a.getInt(crystallizedpoints)){
                a.putInt(crystallizedpoints, b);
            }
        }
        if (a.getInt(liverization)>=liverization_limit) {
            if (a.getInt(liverizationpointb)<20) {
                a.putInt(liverizationpointb, 20);
            }
            if (a.getInt(liverizationpointb)>10&&a.getInt(liverizationpointa)==a.getInt(liverizationpointb)){
                ToolDataNBT b =ToolStack.from(stack).getPersistentData();
                b.addSlots(SlotType.ABILITY, 1);
                b.addSlots(SlotType.DEFENSE, 1);
                b.addSlots(SlotType.UPGRADE, 1);
                a.putInt(liverizationpointa,0);
                a.putInt(liverizationpointb,a.getInt(liverizationpointb)*2);
            }
        }
    }
    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity attacker = context.getAttacker();
        int hadal_limit = MomotinkerConfig.hadal_limit.get();
        int crystallized_limit = MomotinkerConfig.crystallized_limit.get();
        if (attacker instanceof Player player) {
            ModDataNBT a = tool.getPersistentData();
            if (a.getInt(crystallized)==crystallized_limit&&a.getInt(crystallizedpoints)>0) {
                float b= (float) Math.pow(1.15,a.getInt(crystallizedpoints));
                return damage*b;
            }
            if (a.getInt(hadal) == hadal_limit&&context.getLivingTarget()!=null) {
                context.getLivingTarget().invulnerableTime = 0;
                context.getLivingTarget().hurt(player.level().damageSources().dragonBreath(), context.getLivingTarget().getMaxHealth() * 0.05f);
                context.getLivingTarget().invulnerableTime = 0;
            }
        }
        return damage;
    }
    private void onEntityDeath(LivingDeathEvent event) {
        int liverization_limit = MomotinkerConfig.liverization_limit.get();
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player&& a != null){
            ToolStack tool = ToolStack.from(player.getMainHandItem());
            ModDataNBT c = tool.getPersistentData();
            if (tool.getModifierLevel(MomotinkerModifiers.superancientmetalsreala.getId())>0&&c.getInt(liverization)>=liverization_limit){
                if (c.getInt(liverizationpointa)<c.getInt(liverizationpointb)){
                    c.putInt(liverizationpointa,c.getInt(liverizationpointa)+1);
                }
            }
        }
    }

    private void livinghurtevent(LivingHurtEvent event) {
        int sanctification_limit = MomotinkerConfig.sanctification_limit.get();
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player && a != null) {
            ModDataNBT c = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
            if (c.getInt(sanctification) == sanctification_limit && a instanceof Mob mob && !mob.getTags().contains("beconquered")) {
                mob.addTag("beconquered");
            }
        }
        if (a != null && a.getTags().contains("beconquered")) {
            event.setAmount(event.getAmount() * 1.8F);
        }
    }
    @Override
    public LegacyDamageSource modifyDamageSource(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical, LegacyDamageSource source) {
        int degenerate_limit = MomotinkerConfig.degenerate_limit.get();
        ModDataNBT c = tool.getPersistentData();
        if (attacker instanceof Player player && target instanceof LivingEntity entity ) {
            if (tool.getModifierLevel(MomotinkerModifiers.superancientmetalsreala.getId()) > 0&&c.getInt(degenerate) == degenerate_limit) {
                entity.invulnerableTime=0;
                return source.setBypassArmor();
            }
        }
        return source;
    }

    @Override
    public LegacyDamageSource modifyArrowDamageSource(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, AbstractArrow arrow, @Nullable LivingEntity attacker, @NotNull Entity target, LegacyDamageSource source) {
        int degenerate_limit = MomotinkerConfig.degenerate_limit.get();
        if (attacker instanceof Player player&& arrow!=null && target instanceof LivingEntity entity ) {
            ModDataNBT c = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
            if (modifiers.getLevel(MomotinkerModifiers.superancientmetalsreala.getId()) > 0&&c.getInt(degenerate) == degenerate_limit) {
                entity.invulnerableTime=0;
                return source.setBypassArmor();
            }
        }
        return source;
    }
    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifierEntry, EquipmentSlot equipmentSlot, BiConsumer<Attribute, AttributeModifier> biConsumer) {
        ModDataNBT a = tool.getPersistentData();
        int stellarcore_limit = MomotinkerConfig.stellarcore_limit.get();
        if (a.getInt(stellarcore)==stellarcore_limit) {
            biConsumer.accept(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("30324209-D3A4-4BDB-988F-81F683850386"), Attributes.MAX_HEALTH.getDescriptionId(), a.getInt(stellarcorepoints)*0.6f, AttributeModifier.Operation.ADDITION));
            biConsumer.accept(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(UUID.fromString("F25D09BF-FD6D-0ACE-AD38-05916FF9096A"), Attributes.ARMOR_TOUGHNESS.getDescriptionId(), a.getInt(stellarcorepoints)*0.3f, AttributeModifier.Operation.ADDITION));
        }
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> builder, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        int sanctification_limit = MomotinkerConfig.sanctification_limit.get();
        int degenerate_limit = MomotinkerConfig.degenerate_limit.get();
        int hadal_limit = MomotinkerConfig.hadal_limit.get();
        int stellarcore_limit = MomotinkerConfig.stellarcore_limit.get();
        int crystallized_limit = MomotinkerConfig.crystallized_limit.get();
        int liverization_limit = MomotinkerConfig.liverization_limit.get();
        ModDataNBT a = tool.getPersistentData();
        if (a.getInt(degenerate)==degenerate_limit) {
            builder.add(Component.translatable("modifier.momotinker.tooltip.degenerate2").withStyle(ChatFormatting.DARK_RED));
        }
        if (a.getInt(sanctification)==sanctification_limit) {
            builder.add(Component.translatable("modifier.momotinker.tooltip.sanctification2").withStyle(ChatFormatting.YELLOW));
        }
        if (a.getInt(hadal)==hadal_limit) {
            builder.add(Component.translatable("modifier.momotinker.tooltip.hadal3").withStyle(ChatFormatting.DARK_BLUE));
        }
        if (a.getInt(stellarcore)==stellarcore_limit) {
            builder.add(Component.translatable("modifier.momotinker.tooltip.stellarcore3").withStyle(ChatFormatting.YELLOW));
        }
        if (a.getInt(crystallized)==crystallized_limit) {
            float b= (float) Math.pow(1.15,a.getInt(crystallizedpoints));
            builder.add(Component.translatable("modifier.momotinker.tooltip.crystallized3").withStyle(ChatFormatting.AQUA));
            builder.add(Component.translatable("modifier.momotinker.tooltip.crystallized31").append(b*100+"%").withStyle(ChatFormatting.AQUA));
        }
        if (a.getInt(liverization)>=liverization_limit) {
           builder.add(Component.translatable("modifier.momotinker.tooltip.liverization3").withStyle(ChatFormatting.RED));
           builder.add(Component.translatable("modifier.momotinker.tooltip.liverization31").append(a.getInt(liverizationpointa)+",").append(Component.translatable("modifier.momotinker.tooltip.liverization32")).append(a.getInt(liverizationpointb)+"").withStyle(ChatFormatting.RED));
        }
    }
}