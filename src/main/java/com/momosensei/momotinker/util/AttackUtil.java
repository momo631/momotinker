package com.momosensei.momotinker.util;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.data.FloatMultiplier;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.capability.TinkerDataKeys;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.definition.module.weapon.MeleeHitToolHook;
import slimeknights.tconstruct.library.tools.helper.ModifierLootingHandler;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.data.ModifierIds;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.DoubleSupplier;

import static slimeknights.tconstruct.library.tools.helper.ToolAttackUtil.getLivingEntity;

public class AttackUtil {
    private static void disableKnockback(AttributeInstance instance) {
        instance.addTransientModifier(ANTI_KNOCKBACK_MODIFIER);
    }

    private static void enableKnockback(AttributeInstance instance) {
        instance.removeModifier(ANTI_KNOCKBACK_MODIFIER);
    }
    private static final float DEGREE_TO_RADIANS = (float)Math.PI / 180F;
    private static final AttributeModifier ANTI_KNOCKBACK_MODIFIER = new AttributeModifier(TConstruct.MOD_ID + ".anti_knockback", 1f, AttributeModifier.Operation.ADDITION);
    private static Optional<AttributeInstance> getKnockbackAttribute(@Nullable LivingEntity living) {
        return Optional.ofNullable(living)
                .map(e -> e.getAttribute(Attributes.KNOCKBACK_RESISTANCE))
                .filter(attribute -> !attribute.hasModifier(ANTI_KNOCKBACK_MODIFIER));
    }

    public static boolean attackEntity(IToolStackView tool, LivingEntity attackerLiving, InteractionHand hand, Entity targetEntity, DoubleSupplier cooldownFunction, boolean isExtraAttack, EquipmentSlot sourceSlot, float setDamage,
                                       float damageMultiplier, boolean setCritical, boolean notDamageTool, boolean removeInvTime, boolean removeKnockback) {
        if (tool.isBroken() || !tool.hasTag(TinkerTags.Items.MELEE)) {
            return false;
        }
        if (attackerLiving.level().isClientSide || !targetEntity.isAttackable() || targetEntity.skipAttackInteraction(attackerLiving)) {
            return true;
        }

        float cooldown = (float)cooldownFunction.getAsDouble();
        ToolAttackContext context = ToolAttackContext.attacker(attackerLiving).target(targetEntity).cooldown(cooldown).slot(sourceSlot,hand).hand(hand).applyAttributes().build();
        if (isExtraAttack){
            context = ToolAttackContext.attacker(attackerLiving).target(targetEntity).cooldown(cooldown).slot(sourceSlot,hand).hand(hand).applyAttributes().extraAttack().build();
        }
        float baseDamage = context.getBaseDamage();
        float damage = baseDamage + setDamage;

        List<ModifierEntry> modifiers = tool.getModifierList();
        for (ModifierEntry entry : modifiers) {
            damage = entry.getHook(ModifierHooks.MELEE_DAMAGE).getMeleeDamage(tool, entry, context, baseDamage, damage);
        }

        if (damage <= 0.0F) {
            return !isExtraAttack;
        }

        float criticalModifier;
        boolean isCritical;
        boolean fullyCharged = cooldown > 0.9f;
        LivingEntity targetLiving = getLivingEntity(targetEntity);

        if (setCritical) {
            criticalModifier = 1.5f;
            isCritical = true;
        } else {
            boolean checkCritical = !isExtraAttack && fullyCharged && attackerLiving.fallDistance > 0.0F && !attackerLiving.onGround() && !attackerLiving.onClimbable() && !attackerLiving.isInWater()
                    && !attackerLiving.hasEffect(MobEffects.BLINDNESS) && !attackerLiving.isPassenger() && targetLiving != null && !attackerLiving.isSprinting();

            Player attackerPlayer = attackerLiving instanceof Player ? (Player)attackerLiving : null;
            criticalModifier = checkCritical ? 1.5f : 1.0f;
            isCritical = checkCritical;

            if (attackerPlayer != null) {
                CriticalHitEvent hitResult = ForgeHooks.getCriticalHit(attackerPlayer,
                        targetEntity, checkCritical, criticalModifier);
                isCritical = hitResult != null;
                if (isCritical) {
                    criticalModifier = hitResult.getDamageModifier();
                }
            }
        }

        if (isCritical) {
            damage += baseDamage * (criticalModifier - 1.0F);
        }

        if (cooldown < 1.0F) {
            damage *= 0.2F + cooldown * cooldown * 0.8F;
        }

        if (damageMultiplier >= 0) {
            damage *= damageMultiplier;
        }

        boolean isMagic = damage > baseDamage;

        float oldHealth = 0.0F;
        if (targetLiving != null) {
            oldHealth = targetLiving.getHealth();
        }

        float baseKnockback;
        if (removeKnockback) {
            baseKnockback = 0f;
        } else {
            baseKnockback = (float) attackerLiving.getAttributeValue(Attributes.ATTACK_KNOCKBACK) / 2f;
            if (targetLiving != null) {
                baseKnockback += 0.4f;
            }

            if (attackerLiving.isSprinting() && fullyCharged) {
                baseKnockback += 0.5f;
            }
        }

        float knockback = baseKnockback;
        for (ModifierEntry entry : modifiers) {
            knockback = entry.getHook(ModifierHooks.MELEE_HIT).beforeMeleeHit(tool, entry, context, damage, baseKnockback, knockback);
        }

        ModifierLootingHandler.setLootingSlot(attackerLiving, sourceSlot);

        Optional<AttributeInstance> knockbackModifier = Optional.empty();
        boolean canceledKnockback = false;
        if (knockback < 0.4F || removeKnockback) {
            canceledKnockback = true;
            knockbackModifier = getKnockbackAttribute(targetLiving);
            knockbackModifier.ifPresent(AttackUtil::disableKnockback);
        } else if (targetLiving != null) {
            knockback -= 0.4F;
        }

        boolean didHit;
        if (isExtraAttack) {
            didHit = dealDefaultDamage(attackerLiving, targetEntity, damage);
        } else {
            didHit = MeleeHitToolHook.dealDamage(tool, context, damage);
        }

        ModifierLootingHandler.setLootingSlot(attackerLiving, EquipmentSlot.MAINHAND);

        if (canceledKnockback) {
            knockbackModifier.ifPresent(AttackUtil::enableKnockback);
        }

        Level level = attackerLiving.level();
        if (!didHit) {
            if (!isExtraAttack) {
                level.playSound(null, attackerLiving.getX(), attackerLiving.getY(), attackerLiving.getZ(),
                        SoundEvents.PLAYER_ATTACK_NODAMAGE, attackerLiving.getSoundSource(), 1.0F, 1.0F);
            }

            for (ModifierEntry entry : modifiers) {
                entry.getHook(ModifierHooks.MELEE_HIT).failedMeleeHit(tool, entry, context, damage);
            }

            return !isExtraAttack;
        }

        float damageDealt = damage;
        if (targetLiving != null) {
            damageDealt = oldHealth - targetLiving.getHealth();
        }

        if (!removeKnockback && knockback > 0) {
            if (targetLiving != null) {
                targetLiving.knockback(knockback, Mth.sin(attackerLiving.getYRot() * DEGREE_TO_RADIANS),
                        -Mth.cos(attackerLiving.getYRot() * DEGREE_TO_RADIANS));
            } else {
                targetEntity.push(-Mth.sin(attackerLiving.getYRot() * DEGREE_TO_RADIANS) * knockback, 0.1d,
                        Mth.cos(attackerLiving.getYRot() * DEGREE_TO_RADIANS) * knockback);
            }
            attackerLiving.setDeltaMovement(attackerLiving.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            attackerLiving.setSprinting(false);
        }

        if (targetEntity.hurtMarked && targetEntity instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(targetEntity));
            targetEntity.hurtMarked = false;
        }

        Player attackerPlayer = attackerLiving instanceof Player ? (Player)attackerLiving : null;
        if (attackerPlayer != null) {
            SoundEvent sound;
            if (isCritical) {
                sound = SoundEvents.PLAYER_ATTACK_CRIT;
                attackerPlayer.crit(targetEntity);
            } else {
                if (attackerLiving.isSprinting() && fullyCharged) {
                    sound = SoundEvents.PLAYER_ATTACK_KNOCKBACK;
                } else if (fullyCharged) {
                    sound = SoundEvents.PLAYER_ATTACK_STRONG;
                } else {
                    sound = SoundEvents.PLAYER_ATTACK_WEAK;
                }
            }

            if (isMagic) {
                attackerPlayer.magicCrit(targetEntity);
            }

            level.playSound(null, attackerLiving.getX(), attackerLiving.getY(), attackerLiving.getZ(),
                    sound, attackerLiving.getSoundSource(), 1.0F, 1.0F);
        }

        if (damageDealt > 2.0F && level instanceof ServerLevel server) {
            int particleCount = (int)(damageDealt * 0.5f);
            server.sendParticles(ParticleTypes.DAMAGE_INDICATOR,
                    targetEntity.getX(), targetEntity.getY(0.5), targetEntity.getZ(),
                    particleCount, 0.1, 0, 0.1, 0.2);
        }

        attackerLiving.setLastHurtMob(targetEntity);

        if (targetLiving != null) {
            EnchantmentHelper.doPostHurtEffects(targetLiving, attackerLiving);
        }

        for (ModifierEntry entry : modifiers) {
            entry.getHook(ModifierHooks.MELEE_HIT).afterMeleeHit(tool, entry, context, damageDealt);
        }

        if (removeInvTime) {
            targetEntity.invulnerableTime = 0;
        } else {
            float speed = tool.getStats().get(ToolStats.ATTACK_SPEED);
            int time = Math.round(20f / speed);
            if (time < targetEntity.invulnerableTime) {
                targetEntity.invulnerableTime = (targetEntity.invulnerableTime + time) / 2;
            }
        }

        if (attackerPlayer != null) {
            if (targetLiving != null) {
                if (!level.isClientSide && !isExtraAttack) {
                    ItemStack held = attackerLiving.getItemBySlot(sourceSlot);
                    if (!held.isEmpty()) {
                        held.hurtEnemy(targetLiving, attackerPlayer);
                    }
                }
                attackerPlayer.awardStat(Stats.DAMAGE_DEALT, Math.round(damageDealt * 10.0F));
            }

            Projectile projectile = context.getProjectile();
            if (projectile == null) {
                attackerPlayer.causeFoodExhaustion(0.1F);
            }

            if (!isExtraAttack && projectile == null) {
                attackerPlayer.awardStat(Stats.ITEM_USED.get(tool.getItem()));
            }
        }

        if (!tool.hasTag(TinkerTags.Items.UNARMED) && !notDamageTool) {
            int durabilityLost = targetLiving != null ? 1 : 0;
            if (!tool.hasTag(TinkerTags.Items.MELEE_PRIMARY)) {
                durabilityLost *= 2;
            }

            Projectile projectile = context.getProjectile();
            if (projectile != null) {
                ToolDamageUtil.damage(tool, durabilityLost, attackerLiving, attackerLiving.getItemBySlot(sourceSlot));
            } else {
                ToolDamageUtil.damageAnimated(tool, durabilityLost, attackerLiving, sourceSlot);
            }
        }
        return true;
    }

    public static boolean dealDefaultDamage(LivingEntity attacker, Entity target, float damage) {
        if (attacker instanceof Player player) {
            return target.hurt(attacker.damageSources().playerAttack(player), damage);
        } else {
            return target.hurt(attacker.damageSources().mobAttack(attacker), damage);
        }
    }
    public static float attackdamage(IToolStackView tool, LivingEntity attackerLiving, InteractionHand hand, Entity targetEntity, DoubleSupplier cooldownFunction, boolean isExtraAttack, EquipmentSlot sourceSlot,float DamageMultiplier) {
        float damage = 0;
        if (tool.isBroken() || !tool.hasTag(TinkerTags.Items.MELEE)) {
            return damage;
        }
        if (attackerLiving.level().isClientSide || !targetEntity.isAttackable() || targetEntity.skipAttackInteraction(attackerLiving)) {
            return damage;
        }
        LivingEntity targetLiving = getLivingEntity(targetEntity);
        Player attackerPlayer = null;
        if (attackerLiving instanceof Player player) {
            attackerPlayer = player;
        }

        damage = tool.getStats().get(ToolStats.ATTACK_DAMAGE);

        float cooldown = (float)cooldownFunction.getAsDouble();
        boolean fullyCharged = cooldown > 0.9f;

        boolean isCritical = (!isExtraAttack && fullyCharged && attackerLiving.fallDistance > 0.0F && !attackerLiving.onGround() && !attackerLiving.onClimbable()
                && !attackerLiving.isInWater() && !attackerLiving.hasEffect(MobEffects.BLINDNESS)
                && !attackerLiving.isPassenger() && targetLiving != null && !attackerLiving.isSprinting());

        ToolAttackContext context =ToolAttackContext.attacker(attackerLiving).target(targetEntity).cooldown(cooldown).applyAttributes().build();

        float baseDamage = damage;
        List<ModifierEntry> modifiers = tool.getModifierList();
        for (ModifierEntry entry : modifiers) {
            damage = entry.getHook(ModifierHooks.MELEE_DAMAGE).getMeleeDamage(tool, entry, context, baseDamage, damage);
        }
        float criticalModifier = isCritical ? 1.5f: 1.0f;
        if (attackerPlayer != null) {
            CriticalHitEvent hitResult = ForgeHooks.getCriticalHit(attackerPlayer, targetEntity, isCritical, isCritical ? 1.5F : 1.0F);
            isCritical = hitResult != null;
            if (isCritical) {
                criticalModifier = hitResult.getDamageModifier();
            }
        }
        if (isCritical) {
            damage *= criticalModifier;
        }
        if (DamageMultiplier>=0){
            damage *= DamageMultiplier;
        }
        if (cooldown < 1) {
            damage *= (0.2f + cooldown * cooldown * 0.8f);
        }
        return damage;
    }
    public static float hurtdamage(IToolStackView tool, LivingEntity attackerLiving, InteractionHand hand, Entity targetEntity, DoubleSupplier cooldownFunction, boolean isExtraAttack, EquipmentSlot sourceSlot,float DamageMultiplier) {
        float damage = 0;
        if (tool.isBroken() || !tool.hasTag(TinkerTags.Items.MELEE)) {
            return damage;
        }
        if (attackerLiving.level().isClientSide || !targetEntity.isAttackable() || targetEntity.skipAttackInteraction(attackerLiving)) {
            return damage;
        }
        damage = tool.getStats().get(ToolStats.ATTACK_DAMAGE);
        ToolAttackContext context = ToolAttackContext.attacker(attackerLiving).target(targetEntity).cooldown(1F).applyAttributes().build();
        float baseDamage = damage;
        List<ModifierEntry> modifiers = tool.getModifierList();
        for (ModifierEntry entry : modifiers) {
            damage = entry.getHook(ModifierHooks.MELEE_DAMAGE).getMeleeDamage(tool, entry, context, baseDamage, damage);
        }
        if (DamageMultiplier>=0){
            damage *= DamageMultiplier;
        }
        return damage;
    }
    public static void executeall(LevelAccessor world, double x, double y, double z, LivingEntity damager) {
        if (damager instanceof Player player) {
            if (!damager.getCommandSenderWorld().isClientSide) {
                Vec3 vec3 = new Vec3(x, y, z);
                List<LivingEntity> list = world.getEntitiesOfClass(LivingEntity.class, (new AABB(vec3, vec3)).inflate(200F), (e) -> true).stream().sorted(Comparator.comparingDouble((_entcnd) -> _entcnd.distanceToSqr(vec3))).toList();
                for (LivingEntity entity : list) {
                    PenetratingDamage.reflectionPenetratingDamage(entity,player, entity.getMaxHealth());
                    entity.onRemovedFromWorld();
                    entity.remove(Entity.RemovalReason.KILLED);
                    entity.setPos(Double.NaN, Double.NaN, Double.NaN);
                }
            }
        }
    }

    public static float getCooldownFunctionFloat(Player player, InteractionHand hand){
        return (float) getCooldownFunction(player,hand).getAsDouble();
    }
    public static DoubleSupplier getCooldownFunction(Player player, InteractionHand hand) {
        return () -> player.getAttackStrengthScale(0.5f);
    }
    public static float getCriticalFloat(Player player,float damageModifier){
        float d = getCooldownFunctionFloat(player, InteractionHand.MAIN_HAND);
        boolean fullyCharged = (0.2f + d * d * 0.8f) > 0.9f;
        boolean isCritical = fullyCharged && player.fallDistance > 0.0F && !player.onGround() && !player.onClimbable() && !player.isInWater() && !player.hasEffect(MobEffects.BLINDNESS) && !player.isPassenger() && !player.isSprinting();
        if (isCritical){
            return damageModifier;
        }else return 1f;
    }
    public static void stopScoping(LivingEntity entity) {
        if (entity.level().isClientSide) {
            entity.getCapability(TinkerDataCapability.CAPABILITY).ifPresent((data) -> ((FloatMultiplier)data.computeIfAbsent(TinkerDataKeys.FOV_MODIFIER)).remove(ModifierIds.scope));
        }
    }
}
