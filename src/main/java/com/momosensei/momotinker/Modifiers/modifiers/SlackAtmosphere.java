package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import javax.annotation.Nullable;
import java.util.*;

import static com.momosensei.momotinker.Momotinker.getResource;


public class SlackAtmosphere extends momomodifier {
    public SlackAtmosphere() {
        MinecraftForge.EVENT_BUS.addListener(this::AddMobEffect);
        MinecraftForge.EVENT_BUS.addListener(this::livingattackevent);
    }
    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Nullable
    @Override
    public Component requirementsError(ModifierEntry entry) {
        return Component.translatable("recipe.momotinker.modifier.slackatmosphere");
    }
    @Override
    public @NotNull List<ModifierEntry> displayModifiers(ModifierEntry entry) {
        return List.of(new ModifierEntry(MomotinkerModifiers.overcowardicesin.getId(),1));
    }
    @Override
    public Component validate(IToolStackView tool, ModifierEntry modifier) {
        if (tool.getModifierLevel(MomotinkerModifiers.overcowardicesin.getId())>0
                &&tool.getModifierLevel(MomotinkerModifiers.eternalanger.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.thepinnacleofarrogance.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.filledwithhunger.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.resentmentknives.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.forbiddenfruit.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.gainsalone.getId())==0
                &&tool.getModifierLevel(MomotinkerModifiers.compassionateeverything.getId())==0){
            return null;
        }
        return requirementsError(modifier);
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof Player player && !player.level.isClientSide) {
            List<LivingEntity> ls0 = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(25));
            List<LivingEntity> ls1 = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(21));
            for (LivingEntity living0 : ls0) {
                for (LivingEntity living1 : ls1) {
                    if (living1!=null&&living1!=player){
                        ModDataNBT data = ModDataNBT.readFromNBT(living1.getPersistentData());
                        if (!data.getBoolean(getResource("slackatmosphere"))){
                            data.putBoolean(getResource("slackatmosphere"),true);
                        }
                    }
                }
                if (living0!=null&&living0!=player&&!ls1.contains(living0)){
                    ModDataNBT data = ModDataNBT.readFromNBT(living0.getPersistentData());{
                        if (data.getBoolean(getResource("slackatmosphere"))){
                            data.putBoolean(getResource("slackatmosphere"),false);
                        }
                    }
                }
            }
        }
    }
    public void AddMobEffect(MobEffectEvent.Added event) {
        LivingEntity living = event.getEntity();
        if (living != null&&!living.level.isClientSide) {
            ModDataNBT data = ModDataNBT.readFromNBT(living.getPersistentData());
            if (data.getBoolean(getResource("slackatmosphere"))) {
                List<Player> ls0 = living.level.getEntitiesOfClass(Player.class, living.getBoundingBox().inflate(20));
                for (Player player : ls0) {
                    if (player != null && getAllModifierlevel(player, MomotinkerModifiers.slackatmosphere.getId()) > 0) {
                        var instance = event.getEffectInstance();
                        var effect = instance.getEffect();
                        if (instance.getAmplifier() != -1) {
                            if (!effect.isBeneficial()) {
                                instance.amplifier *= 2;
                            } else if (effect.isBeneficial()){
                                instance.amplifier /= 2;
                            }
                        }
                        break;
                    }
                }
            }
        }
        if (living instanceof Player player&&!player.level.isClientSide){
            List<Player> ls0 = player.level.getEntitiesOfClass(Player.class, player.getBoundingBox().inflate(20));
            for (Player player1 : ls0) {
                if (player1 != null &&getAllModifierlevel(player1,MomotinkerModifiers.slackatmosphere.getId())>0) {
                    var instance = event.getEffectInstance();
                    var effect = instance.getEffect();
                    if (instance.getAmplifier() != -1) {
                        if (!effect.isBeneficial()) {
                            instance.amplifier *= 2;
                        } else if (effect.isBeneficial()&&player1!=player){
                            instance.amplifier /= 2;
                        }
                    }
                    break;
                }
            }
        }
    }
    private static final List<MobEffect> HARMFUL_EFFECTS = new ArrayList<>();
    private static final Random RANDOM = new Random();
    static {
        for (MobEffect effect : ForgeRegistries.MOB_EFFECTS) {
            ResourceLocation effectId = ForgeRegistries.MOB_EFFECTS.getKey(effect);
            if (effectId != null && effect.getCategory() == MobEffectCategory.HARMFUL) {
                HARMFUL_EFFECTS.add(effect);
            }
        }
    }
    public MobEffect getRandomHarmfulEffect() {
        return HARMFUL_EFFECTS.get(RANDOM.nextInt(HARMFUL_EFFECTS.size()));
    }
    public void addRandomHarmfulEffects(LivingEntity living, int count, int duration) {
        Set<MobEffect> chosenEffects = new HashSet<>();
        while (chosenEffects.size() < count) {
            chosenEffects.add(getRandomHarmfulEffect());
        }
        chosenEffects.forEach(effect -> {
            if (living.getEffect(effect)!=null&&living.hasEffect(effect)){
                living.addEffect(new MobEffectInstance(effect, duration, Objects.requireNonNull(living.getEffect(effect)).amplifier+1));
            }else if (!living.hasEffect(effect)) living.addEffect(new MobEffectInstance(effect, duration,0));
        });
    }
    private void livingattackevent(LivingAttackEvent event) {
        if (event.getEntity() instanceof Player player&&getAllModifierlevel(player,MomotinkerModifiers.slackatmosphere.getId())>0&&event.getSource().getEntity() instanceof LivingEntity living){
            addRandomHarmfulEffects(living,1,200);
        }
        if (event.getSource().getEntity() instanceof Player player&&getAllModifierlevel(player,MomotinkerModifiers.slackatmosphere.getId())>0&&event.getEntity() !=null){
            addRandomHarmfulEffects(event.getEntity(),1,200);
        }
    }
}