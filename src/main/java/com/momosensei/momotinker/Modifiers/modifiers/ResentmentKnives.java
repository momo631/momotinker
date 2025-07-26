package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;


public class ResentmentKnives extends momomodifier {
    public ResentmentKnives() {
        MinecraftForge.EVENT_BUS.addListener(this::livingattackevent);
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
        MinecraftForge.EVENT_BUS.addListener(this::onEntityDeath);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        String s = "mutual_jealousy";
        String e = "mutual_jealousy_ture";
        if (entity instanceof Player player && !player.level.isClientSide) {
            CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
            List<LivingEntity> ls0 = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(65));
            List<LivingEntity> ls1 = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(60));
            for (LivingEntity living0 : ls0) {
                if (living0!=null&&living0!=player&&((!ls1.contains(living0))||living0.isDeadOrDying())&&tag.getString(s).equals(living0.getPersistentData().getString(s))) {
                    living0.getPersistentData().remove(s);
                    player.getPersistentData().getString(s);
                    tag.remove(s);
                    player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
                    break;
                }
            }
            if (tag.getBoolean(e)&&tag.getString(s).isEmpty()){
                player.getPersistentData().getBoolean(e);
                tag.remove(e);
                player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
                player.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(player.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue());
            }
        }
    }
    private void livingattackevent(LivingAttackEvent event) {
        LivingEntity living = event.getEntity();
        Entity living1 = event.getSource().getEntity();
        if (living1 instanceof Player player && !event.isCanceled() && living != null && getAllModifierlevel(player, MomotinkerModifiers.resentmentknives.getId()) > 0) {
            CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
            String a = "mutual_jealousy";
            if (!tag.getString(a).isEmpty()) {
                player.getPersistentData().getString(a);
                tag.putString(a, String.valueOf(player.getName()));
                player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
            }
            if (living.getPersistentData().getString(a).isEmpty()) {
                living.getPersistentData().putString(a, String.valueOf(player.getName()));
            }
            if (tag.getString(a).equals(living.getPersistentData().getString(a))) {
                double c = player.getAttributeValue(Attributes.ATTACK_DAMAGE);
                double d = living.getAttributeValue(Attributes.ATTACK_DAMAGE);
                String e = "mutual_jealousy_ture";
                player.getPersistentData().getBoolean(e);
                tag.putBoolean(e, true);
                player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
                if (d > c && tag.getBoolean(e)) {
                    player.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(d);
                }
                event.getSource().bypassArmor().bypassMagic().bypassEnchantments().bypassInvul();
            }
        }
        if (living instanceof Player player && !event.isCanceled() && living1 instanceof LivingEntity living2 && getAllModifierlevel(player, MomotinkerModifiers.resentmentknives.getId()) > 0) {
            CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
            String a = "mutual_jealousy";
            if (tag.getString(a).equals(living2.getPersistentData().getString(a))) {
                double c = player.getAttributeValue(Attributes.ATTACK_DAMAGE);
                double d = living2.getAttributeValue(Attributes.ATTACK_DAMAGE);
                if (d < c) {
                    living2.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(c);
                }
            }
        }
    }

    private void livinghurtevent(LivingHurtEvent event) {
        LivingEntity living = event.getEntity();
        String a = "mutual_jealousy";
        if (event.getSource().getEntity() instanceof LivingEntity living1 && living != null) {
            if (!living.getPersistentData().getString(a).equals(living1.getPersistentData().getString(a))) {
                event.setAmount(event.getAmount() * 0.05f);
            }
//            else if (living instanceof Player player){
//                CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
//                if (!tag.getString(a).equals(living1.getPersistentData().getString(a))){
//                    event.setAmount(event.getAmount()*0.05f);
//                }
//            }else if (living1 instanceof Player player){
//                CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
//                if (!tag.getString(a).equals(living.getPersistentData().getString(a))){
//                    event.setAmount(event.getAmount()*0.05f);
//                }
//            }
        }
    }

    private void onEntityDeath(LivingDeathEvent event) {
        LivingEntity a = event.getEntity();
        LivingEntity b = (LivingEntity) event.getSource().getEntity();
        String s = "mutual_jealousy";
        if (a != null) {
            if (!a.getPersistentData().getString(s).isEmpty()) {
                a.getPersistentData().remove(s);
                List<LivingEntity> ls0 = a.level.getEntitiesOfClass(LivingEntity.class, a.getBoundingBox().inflate(60));
                for (LivingEntity living : ls0) {
                    if (living!=null&&living!=a&&a.getPersistentData().getString(s).equals(living.getPersistentData().getString(s))){
                        living.getPersistentData().remove(s);
                    }
                }
            }

        }
    }

}