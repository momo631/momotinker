package com.momosensei.momotinker.Modifiers.modifiers;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

import static com.momosensei.momotinker.util.AttackUtil.attackdamage;
import static com.momosensei.momotinker.util.PenetratingDamage.reflectionPenetratingDamage;

public class Origin extends momomodifier {
    public Origin() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    private void livinghurtevent(LivingAttackEvent event) {
        LivingEntity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player && a != null){
            int c = getMainhandModifierlevel(player,MomotinkerModifiers.origin.getId());
            if (c>0){
                if (a.getHealth()>a.getMaxHealth()){
                    a.setHealth(a.getMaxHealth());
                }
                a.getAttribute(Attributes.MAX_HEALTH).setBaseValue(a.getMaxHealth()*(1f-0.2f*c));
                reflectionPenetratingDamage(a,player, attackdamage(ToolStack.from(player.getMainHandItem()), player, InteractionHand.MAIN_HAND, a, ()->1, true, EquipmentSlot.MAINHAND, 1f));
                if (a.getHealth()<0){
                    a.onRemovedFromWorld();
                    a.setPos(Double.NaN, Double.NaN, Double.NaN);
                }
            }
        }
    }
    @Override
    public LegacyDamageSource modifyDamageSource(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical, LegacyDamageSource source) {
        if (attacker instanceof Player player && target != null) {
            if (ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.MAINHAND), MomotinkerModifiers.origin.getId()) > 0) {
                target.invulnerableTime=0;
                return source.setBypassArmor().setBypassInvul().setBypassInvulnerableTime().setBypassMagic().setBypassEnchantment().setBypassShield();
            }
        }
        return source;
    }

    @Override
    public LegacyDamageSource modifyArrowDamageSource(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, AbstractArrow arrow, @Nullable LivingEntity attacker, @NotNull Entity target, LegacyDamageSource source) {
        if (attacker instanceof Player player && arrow != null) {
            if (ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.MAINHAND), MomotinkerModifiers.origin.getId()) > 0) {
                target.invulnerableTime=0;
                return source.setBypassArmor().setBypassInvul().setBypassInvulnerableTime().setBypassMagic().setBypassEnchantment().setBypassShield();
            }
        }
        return source;
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof Player player && getMainhandModifierlevel(player, MomotinkerModifiers.origin.getId()) > 0) {
            double a = player.getEntityReach();
            List<LivingEntity> list = player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(a));
            for (LivingEntity e : list) {
                if (e != null && e != player) {
                    if (e.getMaxHealth() < player.getMaxHealth()) {
                        reflectionPenetratingDamage(e, player, e.getMaxHealth());
                        e.onRemovedFromWorld();
                        e.setPos(Double.NaN, Double.NaN, Double.NaN);
                    }
                }
            }
        }
    }
}