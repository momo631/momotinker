package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;
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
                &&tool.getModifierLevel(MomotinkerModifiers.gainsalone.getId())==0){
            return null;
        }
        return requirementsError(modifier);
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        String s = "mutual_jealousy";
        String e = "mutual_jealousy_damage";
        if (entity instanceof Player player && !player.level.isClientSide) {
            CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
            player.getPersistentData().getString(s);
            List<LivingEntity> ls0 = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(60));
            List<LivingEntity> ls1 = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(30));

            for (LivingEntity living0 : ls0) {
                if (living0!=null&&living0!=player&&(!ls1.contains(living0))) {
                    if (!(living0 instanceof Player)&&tag.getString(s).equals(living0.getPersistentData().getString(s))) {
                        living0.getPersistentData().remove(s);
                        tag.remove(s);
                        player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
                    }else if (living0 instanceof Player player1){
                        CompoundTag tag1 = player1.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
                        player1.getPersistentData().getString(s);
                        if (tag.getString(s).equals(tag1.getString(s))) {
                            tag.remove(s);
                            tag1.remove(s);
                            player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
                            player1.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
                        }
                    }
                }
            }

            int a = 0;
            for (LivingEntity living0 : ls1) {
                if (living0!=null&&living0!=player) {
                    if (!(living0 instanceof Player)&&tag.getString(s).equals(living0.getPersistentData().getString(s))) {
                        living0.getPersistentData().remove(s);
                        a+=1;
                    }else if (living0 instanceof Player player1){
                        CompoundTag tag1 = player1.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
                        player1.getPersistentData().getString(s);
                        if (tag.getString(s).equals(tag1.getString(s))) {
                            a+=1;
                        }
                    }
                }
            }
            if (a==0){
                tag.remove(s);
                player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
            }
            player.getPersistentData().getFloat(e);
            if (tag.getFloat(e)!=0&&tag.getString(s).isEmpty()){
                tag.remove(e);
                player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
                player.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(1);
            }
        }
    }
    private void livingattackevent(LivingAttackEvent event) {
        LivingEntity living = event.getEntity();
        Entity living1 = event.getSource().getEntity();
        String a = "mutual_jealousy";
        String e = "mutual_jealousy_damage";
        if (living1 instanceof Player player && !event.isCanceled() && living != null && getAllModifierlevel(player, MomotinkerModifiers.resentmentknives.getId()) > 0) {
            CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
            player.getPersistentData().getString(a);
            if (tag.getString(a).isEmpty()) {
                if (!(living instanceof Player)&&living.getPersistentData().getString(a).isEmpty()) {
                    living.getPersistentData().putString(a, String.valueOf(player.getName()));
                    tag.putString(a, String.valueOf(player.getName()));
                    player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
                }else if (living instanceof Player player1){
                    CompoundTag tag1 = player1.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
                    player1.getPersistentData().getString(a);
                    if (tag1.getString(a).isEmpty()) {
                        tag1.putString(a, String.valueOf(player.getName()));
                        player1.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
                        tag.putString(a, String.valueOf(player.getName()));
                        player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
                    }
                }
            }

            if (tag.getString(a).equals(living.getPersistentData().getString(a))) {
                double c = player.getAttributeValue(Attributes.ATTACK_DAMAGE);
                double d = living.getAttributeValue(Attributes.ATTACK_DAMAGE);
                if (d==0)d=1;
                if (c>d) {
                    living.getPersistentData().putFloat(e, (float) (c-d));
                    living.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(d+living.getPersistentData().getFloat(e));
                }else
                if (c<d) {
                    player.getPersistentData().getFloat(e);
                    tag.putFloat(e, (float)(d-c));
                    player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
                    player.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(1+tag.getFloat(e));
                }
                event.getSource().bypassArmor().bypassMagic().bypassEnchantments().bypassInvul();
            }
        }
        if (living instanceof Player player && !event.isCanceled() && living1 instanceof LivingEntity living2 && getAllModifierlevel(player, MomotinkerModifiers.resentmentknives.getId()) > 0) {
            CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
            player.getPersistentData().getString(a);
            if (tag.getString(a).isEmpty()) {
                if (!(living2 instanceof Player)&&living2.getPersistentData().getString(a).isEmpty()) {
                    living2.getPersistentData().putString(a, String.valueOf(player.getName()));
                    tag.putString(a, String.valueOf(player.getName()));
                    player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
                }else if (living2 instanceof Player player1){
                    CompoundTag tag1 = player1.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
                    player.getPersistentData().getString(a);
                    if (tag1.getString(a).isEmpty()) {
                        tag1.putString(a, String.valueOf(player.getName()));
                        player1.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
                        tag.putString(a, String.valueOf(player.getName()));
                        player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
                    }
                }
            }

            if (tag.getString(a).equals(living2.getPersistentData().getString(a))) {
                double c = player.getAttributeValue(Attributes.ATTACK_DAMAGE);
                double d = living2.getAttributeValue(Attributes.ATTACK_DAMAGE);
                if (d==0)d=1;
                if (c>d) {
                    living2.getPersistentData().putFloat(e, (float) (c-d));
                    living2.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(d+living2.getPersistentData().getFloat(e));
                }else
                if (c<d) {
                    player.getPersistentData().getFloat(e);
                    tag.putFloat(e, (float)(d-c));
                    player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
                    player.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(1+tag.getFloat(e));
                }
            }
        }
    }

    private void livinghurtevent(LivingHurtEvent event) {
        LivingEntity living = event.getEntity();
        String a = "mutual_jealousy";
        if (event.getSource().getEntity() instanceof LivingEntity living1 && living != null) {
            if (living instanceof Player player && living1 instanceof Player player1) {
                CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
                player.getPersistentData().getString(a);
                CompoundTag tag1 = player1.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
                player1.getPersistentData().getString(a);
                if (!tag.getString(a).equals(tag1.getString(a))) {
                    event.setAmount(event.getAmount() * 0.05f);
                }
            } else if (living instanceof Player player) {
                CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
                player.getPersistentData().getString(a);
                if (!tag.getString(a).equals(living1.getPersistentData().getString(a))) {
                    event.setAmount(event.getAmount() * 0.05f);
                }
            } else if (living1 instanceof Player player) {
                CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
                player.getPersistentData().getString(a);
                if (!tag.getString(a).equals(living.getPersistentData().getString(a))) {
                    event.setAmount(event.getAmount() * 0.05f);
                }
            } else if (!living.getPersistentData().getString(a).equals(living1.getPersistentData().getString(a))) {
                event.setAmount(event.getAmount() * 0.05f);
            }
        }
    }

    private void onEntityDeath(LivingDeathEvent event) {
        LivingEntity a = event.getEntity();
        String s = "mutual_jealousy";
        String e = "mutual_jealousy_damage";
        if (a != null) {
            if (a instanceof Player player) {
                CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
                player.getPersistentData().getString(s);
                player.getPersistentData().getFloat(e);
                if (!tag.getString(s).isEmpty()) {
                    List<LivingEntity> ls0 = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(30));
                    for (LivingEntity living : ls0) {
                        if (living != null && living != player && tag.getString(s).equals(living.getPersistentData().getString(s))) {
                            living.getPersistentData().remove(s);
                            if (living.getPersistentData().getFloat(e) != 0) {
                                living.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(1);
                                living.getPersistentData().remove(e);
                            }
                        } else if (living instanceof Player player1 && living != player) {
                            CompoundTag tag1 = player1.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
                            player1.getPersistentData().getString(s);
                            player1.getPersistentData().getFloat(e);
                            if (tag.getString(s).equals(tag1.getString(s))) {
                                tag1.remove(s);
                                if (tag1.getFloat(e) != 0) {
                                    player1.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(1);
                                    tag1.remove(e);
                                }
                                player1.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
                            }
                        }
                    }
                    tag.remove(s);
                    if (tag.getFloat(e) != 0) {
                        player.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(1);
                        tag.remove(e);
                    }
                    player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
                }
            } else {
                if (!a.getPersistentData().getString(s).isEmpty()) {
                    List<LivingEntity> ls0 = a.level.getEntitiesOfClass(LivingEntity.class, a.getBoundingBox().inflate(30));
                    for (LivingEntity living : ls0) {
                        if (living != null && living != a && a.getPersistentData().getString(s).equals(living.getPersistentData().getString(s))) {
                            living.getPersistentData().remove(s);
                            if (living.getPersistentData().getFloat(e) != 0) {
                                living.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(1);
                                living.getPersistentData().remove(e);
                            }
                        } else if (living instanceof Player player && living != a) {
                            CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
                            player.getPersistentData().getString(s);
                            player.getPersistentData().getFloat(e);
                            if (a.getPersistentData().getString(s).equals(tag.getString(s))) {
                                tag.remove(s);
                                if (tag.getFloat(e) != 0) {
                                    player.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(1);
                                    tag.remove(e);
                                }
                                player.getPersistentData().put(Player.PERSISTED_NBT_TAG, tag);
                            }
                        }
                    }
                    if (a.getPersistentData().getFloat(e) != 0) {
                        a.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(1);
                        a.getPersistentData().remove(e);
                    }
                    a.getPersistentData().remove(s);
                }
            }
        }
    }
}