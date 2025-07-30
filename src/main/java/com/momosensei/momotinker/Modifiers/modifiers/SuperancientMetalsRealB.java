package com.momosensei.momotinker.Modifiers.modifiers;


import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerConfig;
import com.momosensei.momotinker.register.MomotinkerEffects;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import com.momosensei.momotinker.util.AttackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.Util;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.BiConsumer;

import static com.momosensei.momotinker.tool.divine_punishment_spear.degenerate;
import static com.momosensei.momotinker.tool.divine_punishment_spear.sanctification;
import static com.momosensei.momotinker.tool.entropy_burning_cube.*;
import static com.momosensei.momotinker.tool.pocket_watch.*;

public class SuperancientMetalsRealB extends momomodifier {
    public SuperancientMetalsRealB() {
        MinecraftForge.EVENT_BUS.addListener(this::livingattackevent);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST,this::onEntityDeath);
    }
    public static final ResourceLocation degeneratedeath = Momotinker.getResource("degeneratedeath");

    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Override
    public @javax.annotation.Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(degeneratedeath);
        return null;
    }
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        int crystallized_limit = MomotinkerConfig.crystallized_limit.get();
        if (modifier.getLevel() > 0) {
            ToolStats.ATTACK_SPEED.multiply(builder, 1.6);
            ToolStats.ATTACK_DAMAGE.multiply(builder, 1.6);
            ToolStats.ACCURACY.multiply(builder, 1.6);
            ToolStats.DRAW_SPEED.multiply(builder, 1.6);
            ToolStats.VELOCITY.multiply(builder, 1.6);
            ToolStats.MINING_SPEED.multiply(builder, 1.6);
            ToolStats.ARMOR.multiply(builder, 1.6);
            ToolStats.ARMOR_TOUGHNESS.multiply(builder, 1.6);
            ToolStats.PROJECTILE_DAMAGE.multiply(builder, 1.6);
            ToolStats.KNOCKBACK_RESISTANCE.multiply(builder, 1.6);
            ToolStats.BLOCK_AMOUNT.multiply(builder, 1.6);
            ToolStats.BLOCK_ANGLE.multiply(builder, 1.6);
            if (context.getPersistentData().getInt(crystallized)==crystallized_limit){
                ToolStats.DURABILITY.multiply(builder, 3);
            }else ToolStats.DURABILITY.multiply(builder, 1.6);
        }
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        int stellarcore_limit = MomotinkerConfig.stellarcore_limit.get();
        int liverization_limit = MomotinkerConfig.liverization_limit.get();
        int transmit_limit = MomotinkerConfig.transmit_limit.get();
        ModDataNBT a = tool.getPersistentData();
        if (tool.getDamage() > 0) {
            tool.setDamage(tool.getDamage() - 1);
        }
        if (tool.getDamage() == 0 && entity instanceof Player player && player.tickCount % 10 == 0&&player.getHealth()<player.getMaxHealth()) {
            player.heal(1);
        }
        if (tool.getDamage() < 0) {
            tool.setDamage(0);
        }
        if (a.getInt(liverization)>=liverization_limit) {
            if (tool.getDamage() > 0) {
                tool.setDamage(tool.getDamage() - 4);
            }
            if (entity instanceof Player player&& player.tickCount % 5 == 0&&player.getHealth()<player.getMaxHealth()) {
                if (player.getMaxHealth()*0.005f<1) {
                    player.heal(1);
                }else if (player.getMaxHealth()*0.005f>1) {
                    player.heal(player.getMaxHealth() * 0.005f);
                }
            }
        }
        if (a.getInt(stellarcore) == stellarcore_limit && entity instanceof Player player &&ToolStack.from(player.getMainHandItem()).getPersistentData().getInt(stellarcore)==stellarcore_limit
                && getMainhandModifierlevel(player, MomotinkerModifiers.superancientmetalsrealb.getId()) > 0&&player.tickCount%20==0){
            int b = (int) player.getAttackRange();
            List<LivingEntity> ls0 = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(b+2, b+2, b+2));
            for (LivingEntity targets : ls0) {
                if (targets != player) {
                    targets.setSecondsOnFire(100);
                    Vec3 c=new Vec3(player.getX(),player.getY(),player.getZ());
                    Vec3 d=new Vec3(targets.getX(),targets.getY(),targets.getZ());
                    float e = (float) c.distanceTo(d);
                    if (e>2&&e<b+2){
                        AttackUtil.attackEntity(tool, player, InteractionHand.MAIN_HAND, targets, () -> 1, true, Util.getSlotType(InteractionHand.MAIN_HAND), tool.getStats().get(ToolStats.ATTACK_DAMAGE), (b+2-e)*0.1f, false, true, true, false);
                    }else
                    if (e<2) {
                        AttackUtil.attackEntity(tool, player, InteractionHand.MAIN_HAND, targets, () -> 1, true, Util.getSlotType(InteractionHand.MAIN_HAND), tool.getStats().get(ToolStats.ATTACK_DAMAGE), (b+2)*0.1f, false, true, true, false);
                    }
                }
            }
        }
        if (a.getInt(transmit)==transmit_limit&&entity instanceof Player player&&player.tickCount%20==0) {
            List<LivingEntity> list = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(12));
            for (LivingEntity living : list) {
                if (living != null&&living!=player) {
                    if (living.getEffect(MomotinkerEffects.Stiffening.get())!=null&&living.hasEffect(MomotinkerEffects.Stiffening.get())) {
                        int b = living.getEffect(MomotinkerEffects.Stiffening.get()).getAmplifier();
                        if (b<20) {
                            living.addEffect(new MobEffectInstance(MomotinkerEffects.Stiffening.get(), 400, b+1));
                        }else if (b==20){
                            living.addEffect(new MobEffectInstance(MomotinkerEffects.Stiffening.get(), 400, 20));
                        }
                    }else if (!living.hasEffect(MomotinkerEffects.Stiffening.get())){
                        living.addEffect(new MobEffectInstance(MomotinkerEffects.Stiffening.get(), 400, 0));
                    }
                }
            }
        }
    }
    @Override
    public void addAttributes(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentSlot equipmentSlot, BiConsumer<Attribute, AttributeModifier> biConsumer) {
        ModDataNBT a = iToolStackView.getPersistentData();
        int hadal_limit = MomotinkerConfig.hadal_limit.get();
        int stellarcore_limit = MomotinkerConfig.stellarcore_limit.get();
        int degenerate_limit = MomotinkerConfig.degenerate_limit.get();
        if (a.getInt(hadal) == hadal_limit) {
            biConsumer.accept(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("3002EBA2-F66B-8549-B203-4C0D7152299B"), Attributes.MOVEMENT_SPEED.getDescriptionId(), 0.5f, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
        if (a.getInt(stellarcore)==stellarcore_limit) {
            biConsumer.accept(Attributes.ARMOR, new AttributeModifier(UUID.fromString("A256E90F-0383-E39E-3FBC-2C11F94160E8"), Attributes.ARMOR.getDescriptionId(), 0.4f, AttributeModifier.Operation.MULTIPLY_TOTAL));
            biConsumer.accept(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(UUID.fromString("6A5B426C-6342-1E6D-53F1-4D8918CFEB6A"), Attributes.ARMOR_TOUGHNESS.getDescriptionId(), 0.3f, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
        if (a.getInt(degenerate)==degenerate_limit&&a.getInt(degeneratedeath)>0){
            biConsumer.accept(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("EC681DE9-2FF1-8D57-2E6A-0D8F43A6565D"), Attributes.ATTACK_SPEED.getDescriptionId(), a.getInt(degeneratedeath)*0.0025f, AttributeModifier.Operation.MULTIPLY_TOTAL));
            biConsumer.accept(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("E9B7C9E2-E9BD-D7B8-AAF5-F122364C8A1E"), Attributes.ATTACK_DAMAGE.getDescriptionId(), a.getInt(degeneratedeath)*0.25, AttributeModifier.Operation.ADDITION));
        }
    }
    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity attacker =context.getAttacker();
        int stellarcore_limit = MomotinkerConfig.stellarcore_limit.get();
        int crystallized_limit = MomotinkerConfig.crystallized_limit.get();
        int crystallized_hurt_limit = MomotinkerConfig.crystallized_hurt_limit.get();
        int sanctification_limit = MomotinkerConfig.sanctification_limit.get();
        if (attacker instanceof Player player){
            ModDataNBT a = tool.getPersistentData();
            if (a.getInt(crystallized)==crystallized_limit) {
                int b = (int) ((tool.getStats().getInt(ToolStats.DURABILITY)-tool.getDamage()) * 0.0002f);
                if (b >= crystallized_hurt_limit) {
                    b = crystallized_hurt_limit;
                }
                tool.setDamage(tool.getDamage() + b);
                return damage * (1f + 0.025f * b);
            }
            if (a.getInt(stellarcore)==stellarcore_limit&&context.getLivingTarget()!=null&&(context.getLivingTarget().getMobType()==MobType.WATER||context.getLivingTarget().isOnFire())){
                return damage*1.5f;
            }
            Random random = new Random();
            if (a.getInt(sanctification)==sanctification_limit&&!context.isExtraAttack()&&context.getLivingTarget()!=null&&random.nextInt(2) == 0) {
                Lightning(tool, player, context.getLivingTarget(), player.level, damage, 0.5F);
            }
        }
        return damage;
    }

    public void Lightning(IToolStackView tool,Player player,LivingEntity living,Level level,float damage,float damagemultiplier){
        if (!level.isClientSide) {
            LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
            if (lightning != null) {
                lightning.setVisualOnly(true);
                lightning.getTags().add(player.getStringUUID());
                lightning.setDamage(damage);
                lightning.setPos(living.position());
                level.addFreshEntity(lightning);
                List<LivingEntity> lis = level.getEntitiesOfClass(LivingEntity.class, lightning.getBoundingBox().inflate(1));
                for (LivingEntity entity : lis) {
                    if (entity != null&&entity!=player) {
                        AttackUtil.attackEntity(tool, player, InteractionHand.MAIN_HAND, entity, ()->1, true, Util.getSlotType(InteractionHand.MAIN_HAND), damage, damagemultiplier,false, true, true,false);
                    }
                }
            }
        }
    }

    private void livingattackevent(LivingAttackEvent event) {
        int hadal_limit = MomotinkerConfig.hadal_limit.get();
        int b = RANDOM.nextInt(100);
        if (event.getEntity() instanceof Player player) {
            float bonus = getbonus(player, 2500);
            int d = Math.round(bonus);
            ModDataNBT c = ToolStack.from(player.getMainHandItem()).getPersistentData();
            if (c.getInt(hadal) == hadal_limit&&getAllModifierlevel(player, MomotinkerModifiers.superancientmetalsrealb.getId())>0) {
                if (d >= 50) {
                    d = 50;
                }
                if (b < d) {
                    event.setCanceled(true);
                }
            }
        }
    }
    private void onEntityDeath(LivingDeathEvent event) {
        int liverization_limit = MomotinkerConfig.liverization_limit.get();
        int degenerate_limit = MomotinkerConfig.degenerate_limit.get();
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (a instanceof Player player) {
            for (int j = 0; j < player.getInventory().items.size(); j++) {
                ItemStack stack = player.getInventory().getItem(j);
                ToolStack tool = ToolStack.from(stack);
                int c = (int) (tool.getStats().getInt(ToolStats.DURABILITY)*0.99f);
                if (tool.getPersistentData().getInt(liverization) >= liverization_limit&&tool.getModifierLevel(MomotinkerModifiers.superancientmetalsrealb.getId())>0
                &&tool.getStats().getInt(ToolStats.DURABILITY) - tool.getDamage()>c) {
                    event.setCanceled(true);
                    player.setHealth(player.getMaxHealth() * 0.1f);
                    tool.setDamage(c);
                    break;
                }
            }
        }
        if (b instanceof Player player&& a != null){
            ToolStack tool = ToolStack.from(player.getMainHandItem());
            ModDataNBT c = tool.getPersistentData();
            if (tool.getModifierLevel(MomotinkerModifiers.superancientmetalsrealb.getId())>0&&c.getInt(degenerate)==degenerate_limit){
                c.putInt(degeneratedeath,c.getInt(degeneratedeath)+1);
            }
        }
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> builder, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        int hadal_limit = MomotinkerConfig.hadal_limit.get();
        int stellarcore_limit = MomotinkerConfig.stellarcore_limit.get();
        int crystallized_limit = MomotinkerConfig.crystallized_limit.get();
        int liverization_limit = MomotinkerConfig.liverization_limit.get();
        int sanctification_limit = MomotinkerConfig.sanctification_limit.get();
        int degenerate_limit = MomotinkerConfig.degenerate_limit.get();
        int transmit_limit = MomotinkerConfig.transmit_limit.get();
        int backtracking_limit = MomotinkerConfig.backtracking_limit.get();
        ModDataNBT a = tool.getPersistentData();
        if (a.getInt(hadal)==hadal_limit&&player!=null) {
            float bonus = getbonus(player, 2500);
            int d = Math.round(bonus);
            if (d>=50) {
                d=50;
            }
            builder.add(Component.translatable("modifier.momotinker.tooltip.hadal2").withStyle(ChatFormatting.DARK_BLUE));
            builder.add(Component.translatable("modifier.momotinker.tooltip.hadal21").append(d+"%").withStyle(ChatFormatting.DARK_BLUE));
        }
        if (a.getInt(stellarcore)==stellarcore_limit) {
            builder.add(Component.translatable("modifier.momotinker.tooltip.stellarcore2").withStyle(ChatFormatting.GOLD));
        }
        if (a.getInt(crystallized)==crystallized_limit) {
            int crystallized_hurt_limit = MomotinkerConfig.crystallized_hurt_limit.get();
            int b = (int) ((tool.getStats().getInt(ToolStats.DURABILITY)-tool.getDamage())*0.0002f);
            if (b>crystallized_hurt_limit){
                b=crystallized_hurt_limit;
            }
            builder.add(Component.translatable("modifier.momotinker.tooltip.crystallized2").withStyle(ChatFormatting.AQUA));
            builder.add(Component.translatable("modifier.momotinker.tooltip.crystallized21").append(b*2.5+"%").withStyle(ChatFormatting.AQUA));
        }
        if (a.getInt(liverization)>=liverization_limit) {
            builder.add(Component.translatable("modifier.momotinker.tooltip.liverization2").withStyle(ChatFormatting.RED));
        }
        if (a.getInt(sanctification)==sanctification_limit) {
            builder.add(Component.translatable("modifier.momotinker.tooltip.sanctification3").withStyle(ChatFormatting.YELLOW));
        }
        if (a.getInt(degenerate)==degenerate_limit) {
            builder.add(Component.translatable("modifier.momotinker.tooltip.degenerate3").withStyle(ChatFormatting.DARK_RED));
        }
        if (a.getInt(transmit)==transmit_limit) {
            builder.add(Component.translatable("modifier.momotinker.tooltip.transmit4").withStyle(ChatFormatting.GREEN));
        }
        if (a.getInt(backtracking)==backtracking_limit) {
            builder.add(Component.translatable("modifier.momotinker.tooltip.backtracking4").append(getcooltime(player, (ToolStack) tool,400,120,8)*0.5f+"s").withStyle(ChatFormatting.DARK_PURPLE));
        }
    }
}