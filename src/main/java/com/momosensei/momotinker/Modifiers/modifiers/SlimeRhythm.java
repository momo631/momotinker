package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.*;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;

import javax.annotation.Nonnull;
import java.util.Random;

import static slimeknights.tconstruct.common.TinkerTags.Modifiers.OVERSLIME_FRIEND;


public class SlimeRhythm extends momomodifier {
    public SlimeRhythm() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    public static final ResourceLocation rhythmobbsa = Momotinker.getResource("rhythmobbsa");
    public static final ResourceLocation rhythmobbsb = Momotinker.getResource("rhythmobbsb");

    public static final ResourceLocation rhythmpoints = Momotinker.getResource("rhythmpoints");

    @Override
    public Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(rhythmobbsa);
        iToolStackView.getPersistentData().remove(rhythmobbsb);
        iToolStackView.getPersistentData().remove(rhythmpoints);
        return null;
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        ModDataNBT a = tool.getPersistentData();
        a.putInt(rhythmobbsa,a.getInt(rhythmobbsa)+1);
        if (a.getInt(rhythmobbsa)<0){
            a.putInt(rhythmobbsa,0);
        }
        if (a.getInt(rhythmobbsb)<0){
            a.putInt(rhythmobbsb,0);
        }
        if (a.getInt(rhythmpoints)<0){
            a.putInt(rhythmpoints,0);
        }
    }
    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        ModDataNBT a = tool.getPersistentData();
        OverslimeModifier overslime = TinkerModifiers.overslime.get();
        ModifierEntry entry = tool.getModifier(TinkerModifiers.overslime.getId());
        if (a.getInt(rhythmobbsa)!=0){
            int b=Math.abs(a.getInt(rhythmobbsa)-a.getInt(rhythmobbsb));
            if (b<=6) {
                a.putInt(rhythmpoints,a.getInt(rhythmpoints)+1);
                if (entry.getLevel() > 0 && overslime.getShield(tool) < overslime.getShieldCapacity(tool, entry)) {
                    overslime.addOverslime(tool, entry, (int) (1+Math.floor(modifier.getLevel()*0.5f+0.25f*a.getInt(rhythmpoints))));
                }
            }else {
                a.putInt(rhythmpoints,0);
            }
            a.putInt(rhythmobbsb, a.getInt(rhythmobbsa));
            a.putInt(rhythmobbsa, 0);
        }
        if (tool.getPersistentData().getInt(rhythmpoints) >= 24) {
            if (context.getAttacker().level instanceof ServerLevel level){
                level.sendParticles(ParticleTypes.ITEM_SLIME, context.getAttacker().getX(), context.getAttacker().getY()+context.getAttacker().getBbHeight()*0.6f, context.getAttacker().getZ(), 8, 0.5, 0.5, 0.5, 2);
            }
            for (ModifierEntry entry1 : tool.getModifierList()) {
                if (entry1.getModifier() != modifier.getModifier()&&entry1.matches(OVERSLIME_FRIEND)) {
                    damage = entry1.getHook(ModifierHooks.MELEE_DAMAGE).getMeleeDamage(tool, modifier, context, baseDamage, damage);
                }
            }
        }
        return damage;
    }
    @Override
    public void modifierDamageDealt(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, LivingEntity entity, DamageSource damageSource, float amount, boolean isDirectDamage) {
        if (tool.getPersistentData().getInt(rhythmpoints) >= 24) {
            for (ModifierEntry entry : tool.getModifierList()) {
                if (entry.getModifier() != modifier.getModifier()&&entry.matches(OVERSLIME_FRIEND)) {
                    entry.getHook(ModifierHooks.DAMAGE_DEALT).onDamageDealt(tool, modifier, context, slotType, entity, damageSource, amount * 0.25f, isDirectDamage);
                }
            }
        }
    }
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (tool.getPersistentData().getInt(rhythmpoints) >= 24) {
            for (ModifierEntry entry : tool.getModifierList()) {
                if (entry.getModifier() != modifier.getModifier()&&entry.matches(OVERSLIME_FRIEND)) {
                    entry.getHook(ModifierHooks.MELEE_HIT).afterMeleeHit(tool, modifier, context, damageDealt*0.25f);
                }
            }
        }
    }
    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        if (tool.getPersistentData().getInt(rhythmpoints) >= 24) {
            for (ModifierEntry entry : tool.getModifierList()) {
                if (entry.getModifier() != modifier.getModifier()&&entry.matches(OVERSLIME_FRIEND)) {
                    knockback = entry.getHook(ModifierHooks.MELEE_HIT).beforeMeleeHit(tool, modifier, context, damage, baseKnockback, knockback);
                }
            }
        }
        return knockback;
    }
    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker, @javax.annotation.Nullable LivingEntity target) {
        if (attacker !=null && projectile instanceof AbstractArrow arrow&&target != null){
            ToolStack tool = ToolStack.from( attacker.getMainHandItem());
            ModDataNBT a = tool.getPersistentData();
            if (a.getInt(rhythmpoints)>=24){
                if (attacker.level instanceof ServerLevel level){
                    level.sendParticles(ParticleTypes.ITEM_SLIME, attacker.getX(), attacker.getY()+attacker.getBbHeight()*0.6f, attacker.getZ(), 8, 0.5, 0.5, 0.5, 2);
                }
                boolean ret = false;
                for (ModifierEntry entry1 : tool.getModifierList()) {
                    if (entry1.getModifier() != modifier.getModifier()&&entry1.matches(OVERSLIME_FRIEND)) {
                        ret |=entry1.getHook(ModifierHooks.PROJECTILE_HIT).onProjectileHitEntity(modifiers, persistentData, modifier, projectile, hit, attacker, target);
                    }
                }
                return ret;
            }
        }
        return false;
    }
    @Override
    public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, Projectile projectile, @javax.annotation.Nullable AbstractArrow arrow, NamespacedNBT persistentData, boolean primary) {
        if ( projectile instanceof AbstractArrow) {
            ModDataNBT a = tool.getPersistentData();
            OverslimeModifier overslime = TinkerModifiers.overslime.get();
            ModifierEntry entry = tool.getModifier(TinkerModifiers.overslime.getId());
            if (a.getInt(rhythmobbsa) != 0) {
                int b = Math.abs(a.getInt(rhythmobbsa) - a.getInt(rhythmobbsb));
                if (b <= 6) {
                    a.putInt(rhythmpoints, a.getInt(rhythmpoints) + 1);
                    if (entry.getLevel() > 0 && overslime.getShield(tool) < overslime.getShieldCapacity(tool, entry)) {
                        overslime.addOverslime(tool, entry, (int) (1 + Math.floor(modifier.getLevel() * 0.5f + 0.25f * a.getInt(rhythmpoints))));
                    }
                } else {
                    a.putInt(rhythmpoints, 0);
                }
                a.putInt(rhythmobbsb, a.getInt(rhythmobbsa));
                a.putInt(rhythmobbsa, 0);
            }
            if (a.getInt(rhythmpoints)>=24){
                for (ModifierEntry entry1 : tool.getModifierList()) {
                    if (entry1.getModifier() != modifier.getModifier()&&entry1.matches(OVERSLIME_FRIEND)) {
                        entry1.getHook(ModifierHooks.PROJECTILE_LAUNCH).onProjectileLaunch(tool, modifier, shooter, projectile, arrow, persistentData, primary);
                    }
                }
            }
        }
    }

    @Override
    public void onAttacked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if (source.getEntity() != null) {
            ModDataNBT a = tool.getPersistentData();
            OverslimeModifier overslime = TinkerModifiers.overslime.get();
            ModifierEntry entry = tool.getModifier(TinkerModifiers.overslime.getId());

            if (a.getInt(rhythmobbsa)!=0){
                int b=Math.abs(a.getInt(rhythmobbsa)-a.getInt(rhythmobbsb));
                if (b<=6) {
                    int c = 1;
                    if (source.getEntity() instanceof Slime){
                        Random random=new Random();
                        c=random.nextInt(10);
                    }
                    if (c==1) {
                        a.putInt(rhythmpoints, a.getInt(rhythmpoints) + 1);
                        if (entry.getLevel() > 0 && overslime.getShield(tool) < overslime.getShieldCapacity(tool, entry)) {
                            overslime.addOverslime(tool, entry, (int) (1 + Math.floor(getArmorModifierlevel(context.getEntity(), MomotinkerModifiers.slimerhythm.getId()) * 0.25f + 0.25f * a.getInt(rhythmpoints))));
                        }
                    }
                }else {
                    a.putInt(rhythmpoints,0);
                }
                a.putInt(rhythmobbsb, a.getInt(rhythmobbsa));
                a.putInt(rhythmobbsa, 0);
            }
            if (a.getInt(rhythmpoints)>=24){
                if (context.getEntity().level instanceof ServerLevel level){
                    level.sendParticles(ParticleTypes.ITEM_SLIME, context.getEntity().getX(), context.getEntity().getY()+context.getEntity().getBbHeight()*0.6f, context.getEntity().getZ(), 4, 0.5, 0.5, 0.5, 2);
                }
            }
        }
    }
    private ModifierEntry getOverSlime(ToolStack tool){
        return tool.getModifier(TinkerModifiers.overslime.getId());
    }
    private int getRhythmPoints(ToolStack tool){
        return tool.getPersistentData().getInt(rhythmpoints);
    }
    private boolean getRhythmPointsTure(ToolStack tool1,ToolStack tool2,ToolStack tool3,ToolStack tool4){
        return getRhythmPoints(tool1) > 0 || getRhythmPoints(tool2) > 0 || getRhythmPoints(tool3) > 0 || getRhythmPoints(tool4) > 0;
    }
    private void livinghurtevent(LivingHurtEvent event) {
        LivingEntity a = event.getEntity();
        OverslimeModifier overslime = TinkerModifiers.overslime.get();
        if (a instanceof Player player) {
            ToolStack tool3 = ToolStack.from(player.getItemBySlot(EquipmentSlot.HEAD));
            ToolStack tool4 = ToolStack.from(player.getItemBySlot(EquipmentSlot.CHEST));
            ToolStack tool5 = ToolStack.from(player.getItemBySlot(EquipmentSlot.LEGS));
            ToolStack tool6 = ToolStack.from(player.getItemBySlot(EquipmentSlot.FEET));
            
            int d = getArmorModifierlevel(player, MomotinkerModifiers.slimerhythm.getId());
            if (d > 0&&getRhythmPointsTure(tool3,tool4,tool5,tool6)) {
                int b1 = (int) Math.floor(event.getAmount());
                int b3;int b4;int b5;int b6;
                if (b1>0&&getOverSlime(tool3).getLevel() > 0&&overslime.getShield(tool3) >0) {
                    b3 = overslime.getShield(tool3);
                    b1-=b3;
                    if (b1>0) {
                        overslime.addOverslime(tool3, getOverSlime(tool3), -b3);
                    }else overslime.addOverslime(tool3, getOverSlime(tool3), -b1);
                }
                if (b1>0&&getOverSlime(tool4).getLevel() > 0&&overslime.getShield(tool4) >0) {
                    b4 = overslime.getShield(tool4);
                    b1-=b4;
                    if (b1>0) {
                        overslime.addOverslime(tool4, getOverSlime(tool4), -b4);
                    }else overslime.addOverslime(tool4, getOverSlime(tool4), -b1);
                }
                if (b1>0&&getOverSlime(tool5).getLevel() > 0&&overslime.getShield(tool5) >0) {
                    b5 = overslime.getShield(tool5);
                    b1-=b5;
                    if (b1>0) {
                        overslime.addOverslime(tool5, getOverSlime(tool5), -b5);
                    }else overslime.addOverslime(tool5, getOverSlime(tool5), -b1);
                }
                if (b1>0&&getOverSlime(tool6).getLevel() > 0&&overslime.getShield(tool6) >0) {
                    b6 = overslime.getShield(tool6);
                    b1-=b6;
                    if (b1>0) {
                        overslime.addOverslime(tool6, getOverSlime(tool6), -b6);
                    }else overslime.addOverslime(tool6, getOverSlime(tool6), -b1);
                }
                event.setAmount(Math.max(b1, 0));
            }
        }
    }
}