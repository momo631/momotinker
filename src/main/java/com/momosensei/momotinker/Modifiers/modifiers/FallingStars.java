package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;


public class FallingStars extends momomodifier {
    public FallingStars() {
    }
    public static final ResourceLocation falling = Momotinker.getResource("falling");

    @Override
    public @Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(falling);
        return null;
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof ServerPlayer player) {
            player.level.getEntities(player, player.getBoundingBox().inflate(10, 10, 10));
            if (player.getEffect(MomotinkerEffects.FallingStar.get()) != null && player.isOnGround()) {
                List<Mob> list = player.level.getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(10));
                float a = (float) 20 /player.getEffect(MomotinkerEffects.FallingStar.get()).getDuration();
                for (Mob mob : list) {
                    if (mob != null) {
                        if (a<4) {
                            mob.invulnerableTime = 0;
                            mob.hurt(DamageSource.explosion(player), (modifierEntry.getLevel() * 100) * a);
                            player.removeEffect(MomotinkerEffects.FallingStar.get());
                        }
                        if (a>4){
                            mob.invulnerableTime = 0;
                            mob.hurt(DamageSource.explosion(player), (modifierEntry.getLevel() * 100) * 4);
                            player.removeEffect(MomotinkerEffects.FallingStar.get());
                        }
                    }
                }
                player.playSound(SoundEvents.GENERIC_EXPLODE, 1, 5);
                if (player.level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, player.getX(), player.getY(), player.getZ(), 20, 5, 5, 5, 0);
                }
            }
            ModDataNBT tooldata = iToolStackView.getPersistentData();
            if (entity.tickCount % 20 == 0&&tooldata.getFloat(falling)>0.1){
                tooldata.putFloat(falling,tooldata.getFloat(falling)-1);
            }
        }
    }

    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity attacker =context.getAttacker();
        if (attacker instanceof ServerPlayer player){
            if (player.getEffect(MomotinkerEffects.FallingStar.get()) != null) {
                float a = (float) 20 /player.getEffect(MomotinkerEffects.FallingStar.get()).getDuration();
                List<Mob> list = player.level.getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(10));
                for (Mob mob : list) {
                    if (mob != null) {
                        if (a<4) {
                            mob.invulnerableTime = 0;
                            mob.hurt(DamageSource.explosion(player), (modifier.getLevel() * (100+damage)) * a);
                            player.removeEffect(MomotinkerEffects.FallingStar.get());
                        }
                        if (a>4){
                            mob.invulnerableTime = 0;
                            mob.hurt(DamageSource.explosion(player), (modifier.getLevel() * (100+damage)) * 4);
                            player.removeEffect(MomotinkerEffects.FallingStar.get());
                        }
                    }
                }
                player.playSound(SoundEvents.GENERIC_EXPLODE, 1, 1);
                if (player.level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, player.getX(), player.getY(), player.getZ(), 20, 5, 5, 5, 0);
                }
            }
        }
        return damage;
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player != null) {
            ModDataNBT tooldata = tool.getPersistentData();
            tooltip.add(net.minecraft.network.chat.Component.translatable("[苍穹落星]的冷却还剩" + (tooldata.getFloat(falling))+"秒").withStyle(ChatFormatting.LIGHT_PURPLE));
        }
    }
}