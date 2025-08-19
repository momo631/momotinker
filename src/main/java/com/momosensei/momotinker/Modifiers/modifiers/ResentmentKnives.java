package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;
import java.util.List;


public class ResentmentKnives extends momomodifier {
    public ResentmentKnives() {
        MinecraftForge.EVENT_BUS.addListener(this::onlivingtickevent);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Nullable
    @Override
    public Component requirementsError(ModifierEntry entry) {
        return Component.translatable("recipe.momotinker.modifier.resentmentknives");
    }
    @Override
    public @NotNull List<ModifierEntry> displayModifiers(ModifierEntry entry) {
        return List.of(new ModifierEntry(MomotinkerModifiers.overenvysin.getId(),1));
    }
    @Override
    public Component validate(IToolStackView tool, ModifierEntry modifier) {
        if (tool.getModifierLevel(MomotinkerModifiers.overenvysin.getId())>0
                &&tool.getModifierLevel(MomotinkerModifiers.eternalanger.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.slackatmosphere.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.thepinnacleofarrogance.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.filledwithhunger.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.forbiddenfruit.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.gainsalone.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.compassionateeverything.getId())==0){
            return null;
        }
        return requirementsError(modifier);
    }

    private static LivingEntity getEnvyTarget(LivingEntity living){
        var nbt=living.getPersistentData();
        String s = "mutual_jealousy";
        if (living.level instanceof ServerLevel serverLevel&&nbt.contains(s)){
            var uuid=nbt.getUUID(s);
            var entity=serverLevel.getEntity(uuid);
            if (entity instanceof LivingEntity living1){
                return living1;
            }
        }
        return null;
    }
    private static void setEnvyTarget(LivingEntity attacker,LivingEntity target){
        var data1=attacker.getPersistentData();
        var data2=target.getPersistentData();
        String s = "mutual_jealousy";
        data1.putUUID(s,target.getUUID());
        data2.putUUID(s,attacker.getUUID());
    }
    private void onlivingtickevent(LivingEvent.LivingTickEvent event) {
        var entity=event.getEntity();
        String s = "mutual_jealousy";
        String e = "mutual_jealousy_damage";
        if (entity.level.isClientSide)return;
        if (entity.tickCount%10!=0)return;
        var target=getEnvyTarget(entity);
        if (target==null)return;
        if (target.distanceTo(entity)>30||!target.isAlive()){
            entity.getPersistentData().remove(s);
            target.getPersistentData().remove(s);
            entity.getPersistentData().remove(e);
            target.getPersistentData().remove(e);
            entity.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(1);
            target.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(1);
        }
    }
     @Override
    public void OnLivingAttack(LivingAttackEvent event) {
        LivingEntity living = event.getEntity();
        Entity living1 = event.getSource().getEntity();
        String e = "mutual_jealousy_damage";
        if (!(living1 instanceof LivingEntity attacker))return;
        if (living.level.isClientSide)return;
        if (getAllModifierlevel(attacker,MomotinkerModifiers.resentmentknives.getId())>0||getAllModifierlevel(living,MomotinkerModifiers.resentmentknives.getId())>0){
            if (getEnvyTarget(living)==null&&getEnvyTarget(attacker)==null){
                setEnvyTarget(attacker,living);
                double c = attacker.getAttributeValue(Attributes.ATTACK_DAMAGE);
                double d = living.getAttributeValue(Attributes.ATTACK_DAMAGE);
                if (d == 0) d = 1;
                if (c > d) {
                    living.getPersistentData().putFloat(e, (float) (c - d));
                    living.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(d + living.getPersistentData().getFloat(e));
                } else if (c < d) {
                    attacker.getPersistentData().putFloat(e, (float) (d - c));
                    attacker.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(1 + attacker.getPersistentData().getFloat(e));
                }
            }
        }
        if (getAllModifierlevel(attacker,MomotinkerModifiers.resentmentknives.getId())>0){
            if (getEnvyTarget(attacker)==living){
                event.getSource().bypassArmor().bypassMagic().bypassEnchantments().bypassInvul();
            }
        }
    }
    @Override
    public void OnLivingHurt(LivingHurtEvent event) {
        LivingEntity living = event.getEntity();
        Entity living1 = event.getSource().getEntity();
        if (!(living1 instanceof LivingEntity attacker))return;
        if (living.level.isClientSide)return;
        if ((getEnvyTarget(living)!=null||getEnvyTarget(attacker)!=null)&&getEnvyTarget(living)!=attacker){
            event.setAmount(event.getAmount()*0.05f);
        }
    }
}