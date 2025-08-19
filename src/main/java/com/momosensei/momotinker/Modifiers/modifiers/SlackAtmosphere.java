package com.momosensei.momotinker.Modifiers.modifiers;

import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;
import java.util.*;


public class SlackAtmosphere extends momomodifier {
    public SlackAtmosphere() {
        MinecraftForge.EVENT_BUS.addListener(this::AddMobEffect);
        
        MinecraftForge.EVENT_BUS.addListener(this::onlivingtickevent);
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
    private void onlivingtickevent(LivingEvent.LivingTickEvent event) {
        var entity=event.getEntity();
        String s = "slackatmosphere";
        if (entity.level.isClientSide)return;
        if (entity.tickCount%10!=0)return;
        var target=getSlackTarget(entity);
        if (target==null)return;
//        if (target.distanceTo(entity)>25||!target.isAlive()){
//            entity.getPersistentData().remove(s);
//        }
        int a = 0;
        if (entity instanceof Player player) {
            for (int i = 0; i < Inventory.INVENTORY_SIZE; i++){
                ItemStack stack = player.getInventory().getItem(i);
                ToolStack tool=ToolStack.from(stack);
                if (tool.getModifierLevel(MomotinkerModifiers.slackatmosphere.getId())>0){
                    a+=1;
                }
            }
            if (getAllModifierlevel(player,MomotinkerModifiers.slackatmosphere.getId())>0){
                a+=getAllModifierlevel(player,MomotinkerModifiers.slackatmosphere.getId());
            }
        }
        if (target.distanceTo(entity)>25||a==0||!entity.isAlive()){
            entity.getPersistentData().remove(s);
        }
    }
    private static LivingEntity getSlackTarget(LivingEntity living){
        var nbt=living.getPersistentData();
        String s = "slackatmosphere";
        if (living.level instanceof ServerLevel serverLevel&&nbt.contains(s)){
            var uuid=nbt.getUUID(s);
            var entity=serverLevel.getEntity(uuid);
            if (entity instanceof LivingEntity living1){
                return living1;
            }
        }
        return null;
    }
    private static void setSlackTarget(LivingEntity attacker,LivingEntity target){
        //var data1=attacker.getPersistentData();
        var data2=target.getPersistentData();
        String s = "slackatmosphere";
        //data1.putUUID(s,target.getUUID());
        data2.putUUID(s,attacker.getUUID());
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifierEntry, Level level, LivingEntity entity, int index, boolean b, boolean b1, ItemStack itemStack) {
        if (entity instanceof Player player && !player.level.isClientSide) {
            List<LivingEntity> ls0 = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(25));
            for (LivingEntity living0 : ls0) {
                if (living0 != null&&getSlackTarget(living0)==null) {
                   setSlackTarget(player,living0);
                }
            }
        }
    }
    public void AddMobEffect(MobEffectEvent.Added event) {
        LivingEntity living = event.getEntity();
        if (living != null&&!living.level.isClientSide&&getSlackTarget(living)!=null) {
            var instance = event.getEffectInstance();
            var effect = instance.getEffect();
            if (instance.getAmplifier() != -1) {
                if (!effect.isBeneficial()) {
                    instance.amplifier *= 2;
                } else if (effect.isBeneficial()) {
                    int a = 0;
                    if (living instanceof Player player) {
                        for (int i = 0; i < Inventory.INVENTORY_SIZE; i++){
                            ItemStack stack = player.getInventory().getItem(i);
                            ToolStack tool=ToolStack.from(stack);
                            if (tool.getModifierLevel(MomotinkerModifiers.slackatmosphere.getId())>0){
                                a+=1;
                            }
                        }
                        if (getArmorModifierlevel(player,MomotinkerModifiers.slackatmosphere.getId())>0){
                            a+=1;
                        }
                    }
                    if (a!=0){
                        return;
                    }
                    instance.amplifier /= 2;
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
     @Override
    public void OnLivingAttack(LivingAttackEvent event) {
        if (event.getEntity() instanceof Player player&&getAllModifierlevel(player,MomotinkerModifiers.slackatmosphere.getId())>0&&event.getSource().getEntity() instanceof LivingEntity living){
            addRandomHarmfulEffects(living,1,200);
        }
        if (event.getSource().getEntity() instanceof Player player&&getAllModifierlevel(player,MomotinkerModifiers.slackatmosphere.getId())>0&&event.getEntity() !=null){
            addRandomHarmfulEffects(event.getEntity(),1,200);
        }
    }
}