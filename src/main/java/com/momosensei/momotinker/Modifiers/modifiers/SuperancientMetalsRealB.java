package com.momosensei.momotinker.Modifiers.modifiers;


import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerConfig;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import com.momosensei.momotinker.util.attackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
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
import java.util.UUID;
import java.util.function.BiConsumer;

import static com.momosensei.momotinker.tool.entropy_burning_cube.*;

public class SuperancientMetalsRealB extends momomodifier {
    public SuperancientMetalsRealB() {
        MinecraftForge.EVENT_BUS.addListener(this::livingattackevent);
    }

    @Override
    public boolean isNoLevels() {
        return true;
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
                tool.setDamage(tool.getDamage() - 3);
            }
            if (entity instanceof Player player&& player.tickCount % 5 == 0&&player.getHealth()<player.getMaxHealth()) {
                if (player.getMaxHealth()*0.005f<1) {
                    player.heal(1);
                }else if (player.getMaxHealth()*0.005f>1) {
                    player.heal(player.getMaxHealth() * 0.005f);
                }
            }
        }
        if (a.getInt(stellarcore) == stellarcore_limit && entity instanceof Player player && getMainhandModifierlevel(player, MomotinkerModifiers.superancientmetalsrealb.getId()) > 0&&player.tickCount%10==0){
            int b = (int) player.getAttackRange();
            List<Entity> ls0 = player.level.getEntitiesOfClass(Entity.class, player.getBoundingBox().inflate(b+1.5, b+1.5, b+1.5));
            for (Entity targets : ls0) {
                if (targets != player) {
                    targets.setSecondsOnFire(200);
                    int c = (int) (entity.position().subtract(player.position()).normalize().x + entity.position().subtract(player.position()).normalize().y + entity.position().subtract(player.position()).normalize().z);
                    if ((b + 1.5) * 3 > c) {
                        attackUtil.attackEntity(tool, player, InteractionHand.MAIN_HAND, targets, () -> 1, true, Util.getSlotType(InteractionHand.MAIN_HAND), tool.getStats().get(ToolStats.ATTACK_DAMAGE), c, false, true, true, false);
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
        if (a.getInt(hadal) == hadal_limit) {
            biConsumer.accept(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("3002EBA2-F66B-8549-B203-4C0D7152299B"), Attributes.MAX_HEALTH.getDescriptionId(), 0.5f, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
        if (a.getInt(stellarcore)==stellarcore_limit) {
            biConsumer.accept(Attributes.ARMOR, new AttributeModifier(UUID.fromString("A256E90F-0383-E39E-3FBC-2C11F94160E8"), Attributes.MAX_HEALTH.getDescriptionId(), 0.4f, AttributeModifier.Operation.MULTIPLY_TOTAL));
            biConsumer.accept(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(UUID.fromString("6A5B426C-6342-1E6D-53F1-4D8918CFEB6A"), Attributes.MAX_HEALTH.getDescriptionId(), 0.3f, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }
    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity attacker =context.getAttacker();
        int stellarcore_limit = MomotinkerConfig.stellarcore_limit.get();
        int crystallized_limit = MomotinkerConfig.crystallized_limit.get();
        int crystallized_hurt_limit = MomotinkerConfig.crystallized_hurt_limit.get();
        if (attacker instanceof Player player){
            ModDataNBT a = tool.getPersistentData();
            if (a.getInt(crystallized)==crystallized_limit) {
                int b = (int) (tool.getStats().getInt(ToolStats.DURABILITY)*0.02f);
                if (b<crystallized_hurt_limit) {
                    if (tool.getStats().getInt(ToolStats.DURABILITY) - tool.getDamage() < b) {
                        tool.setDamage(tool.getStats().getInt(ToolStats.DURABILITY));
                    } else if (tool.getStats().getInt(ToolStats.DURABILITY) - tool.getDamage() > b) {
                        tool.setDamage(tool.getDamage() + b);
                    }
                    return damage*(1f+0.0025f*b);
                }else if (b>crystallized_hurt_limit){
                    if (tool.getStats().getInt(ToolStats.DURABILITY) - tool.getDamage() < crystallized_hurt_limit) {
                        tool.setDamage(tool.getStats().getInt(ToolStats.DURABILITY));
                    } else if (tool.getStats().getInt(ToolStats.DURABILITY) - tool.getDamage() > crystallized_hurt_limit) {
                        tool.setDamage(tool.getDamage() + crystallized_hurt_limit);
                    }
                    return damage*(1f+0.0025f*crystallized_hurt_limit);
                }
            }
            if (a.getInt(stellarcore)==stellarcore_limit&&context.getLivingTarget()!=null&&(context.getLivingTarget().getMobType()==MobType.ILLAGER
            ||context.getLivingTarget().getMobType()==MobType.WATER||context.getLivingTarget().isOnFire())){
                return damage*1.5f;
            }
        }
        return damage;
    }


    public float getbonus(float speed, int status) {
        return speed * status;
    }
    private void livingattackevent(LivingAttackEvent event) {
        LivingEntity living = event.getEntity();
        int hadal_limit = MomotinkerConfig.hadal_limit.get();
        int a =  getAllModifierlevel(living, MomotinkerModifiers.superancientmetalsrealb.getId());
        int b = RANDOM.nextInt(100);
        if (event.getEntity() instanceof Player player&&a>0) {
            float speed = (float) player.getDeltaMovement().length();
            int bonus = (int) (getbonus(speed, 5)*50);
            ModDataNBT c = ToolStack.from(player.getMainHandItem()).getPersistentData();
            if (c.getInt(hadal)==hadal_limit) {
                if (bonus<50&&b<bonus) {
                    event.setCanceled(true);
                }else if (bonus>=50&&b<50) {
                    event.setCanceled(true);
                }
            }
        }
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> builder, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        int hadal_limit = MomotinkerConfig.hadal_limit.get();
        int stellarcore_limit = MomotinkerConfig.stellarcore_limit.get();
        int crystallized_limit = MomotinkerConfig.crystallized_limit.get();
        int liverization_limit = MomotinkerConfig.liverization_limit.get();
        ModDataNBT a = tool.getPersistentData();
        if (a.getInt(hadal)==hadal_limit) {
            builder.add(Component.translatable("modifier.momotinker.tooltip.hadal1").withStyle(ChatFormatting.DARK_BLUE));
            builder.add(Component.translatable("modifier.momotinker.tooltip.hadal2").withStyle(ChatFormatting.DARK_BLUE));
        }
        if (a.getInt(stellarcore)==stellarcore_limit) {
            builder.add(Component.translatable("modifier.momotinker.tooltip.stellarcore1").withStyle(ChatFormatting.YELLOW));
            builder.add(Component.translatable("modifier.momotinker.tooltip.stellarcore2").withStyle(ChatFormatting.YELLOW));
        }
        if (a.getInt(crystallized)==crystallized_limit) {
            builder.add(Component.translatable("modifier.momotinker.tooltip.crystallized1").withStyle(ChatFormatting.AQUA));
            builder.add(Component.translatable("modifier.momotinker.tooltip.crystallized2").withStyle(ChatFormatting.AQUA));
        }
        if (a.getInt(liverization)>=liverization_limit) {
            builder.add(Component.translatable("modifier.momotinker.tooltip.liverization1").withStyle(ChatFormatting.DARK_RED));
            builder.add(Component.translatable("modifier.momotinker.tooltip.liverization2").withStyle(ChatFormatting.DARK_RED));
        }
    }
}