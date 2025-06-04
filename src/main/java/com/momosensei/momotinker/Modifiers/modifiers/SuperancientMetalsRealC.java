package com.momosensei.momotinker.Modifiers.modifiers;


import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerConfig;
import com.momosensei.momotinker.register.MomotinkerEffects;
import com.momosensei.momotinker.register.MomotinkerItem;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.function.BiConsumer;

import static com.momosensei.momotinker.tool.entropy_burning_cube.*;
import static com.momosensei.momotinker.tool.pocket_watch.transmit;

public class SuperancientMetalsRealC extends momomodifier {
    public SuperancientMetalsRealC() {
        MinecraftForge.EVENT_BUS.addListener(this::onEntityDeath);
        MinecraftForge.EVENT_BUS.addListener(this::livingattackevent);
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }
    public static final ResourceLocation transmitpoints = Momotinker.getResource("transmitpoints");
    public static final ResourceLocation overheat = Momotinker.getResource("overheat");
    public static final ResourceLocation overheatingcooling = Momotinker.getResource("overheatingcooling");
    public static final ResourceLocation durabilityrecovery = Momotinker.getResource("durabilityrecovery");
    public static final ResourceLocation recorddamage = Momotinker.getResource("recorddamage");

    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Override
    public @javax.annotation.Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(transmitpoints);
        iToolStackView.getPersistentData().remove(overheat);
        iToolStackView.getPersistentData().remove(overheatingcooling);
        iToolStackView.getPersistentData().remove(durabilityrecovery);
        iToolStackView.getPersistentData().remove(recorddamage);
        return null;
    }
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        if (modifier.getLevel() > 0) {
            ToolStats.DURABILITY.multiply(builder, 2.5);
            ToolStats.ATTACK_SPEED.multiply(builder, 2);
            ToolStats.ATTACK_DAMAGE.multiply(builder, 2);
            ToolStats.ACCURACY.multiply(builder, 2);
            ToolStats.DRAW_SPEED.multiply(builder, 2);
            ToolStats.VELOCITY.multiply(builder, 2);
            ToolStats.MINING_SPEED.multiply(builder, 2);
            ToolStats.ARMOR.multiply(builder, 2);
            ToolStats.ARMOR_TOUGHNESS.multiply(builder, 2);
            ToolStats.PROJECTILE_DAMAGE.multiply(builder, 2);
            ToolStats.KNOCKBACK_RESISTANCE.multiply(builder, 2);
            ToolStats.BLOCK_AMOUNT.multiply(builder, 2);
            ToolStats.BLOCK_ANGLE.multiply(builder, 2);
        }
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        int transmit_limit = MomotinkerConfig.transmit_limit.get();
        int stellarcore_limit = MomotinkerConfig.stellarcore_limit.get();
        ModDataNBT a = tool.getPersistentData();
        if (entity instanceof Player player) {
            if (a.getInt(transmit) == transmit_limit) {
                if (player.tickCount % 20 == 0&&a.getInt(transmitpoints)<400) {
                    a.putInt(transmitpoints, a.getInt(transmitpoints) + 1);
                }
                int b= (int) Math.floor(a.getInt(transmitpoints)*0.025f);
                int c=player.getEffect(MomotinkerEffects.IncreaseAllAttributes.get()).getAmplifier();
                if (player.tickCount % 20 == 0&&c<=b){
                    player.addEffect(new MobEffectInstance(MomotinkerEffects.IncreaseAllAttributes.get(),100,b));
                }
            }
            if (a.getInt(stellarcore) == stellarcore_limit){
                if (player.tickCount % 5 == 0&&a.getInt(overheat)<90) {
                    a.putInt(overheat, a.getInt(overheat) + 1);
                }
            }
            if (ToolStack.from(player.getMainHandItem()).getPersistentData().getInt(stellarcore) == stellarcore_limit&&player.tickCount % 5 == 0&&a.getInt(overheat)==90&& player.level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.LAVA, player.getX(), player.getY(), player.getZ(), 1/2, 0, 0, 0, 1);
                serverLevel.sendParticles(ParticleTypes.FLAME, player.getX(), player.getY(), player.getZ(), 1, 0, 0, 0, 0.5);
            }
            if (a.getInt(transmitpoints)<0){
                a.putInt(transmitpoints, 0);
            }
            if (a.getInt(overheat)<0){
                a.putInt(overheat, 0);
            }
            if (player.tickCount % 20 == 0&&a.getInt(overheatingcooling)<6) {
                a.putInt(overheatingcooling, a.getInt(overheatingcooling) - 1);
            }
            if (player.tickCount % 2 == 0&&a.getInt(overheatingcooling)==0) {
                a.putInt(overheat, a.getInt(overheat) - 1);
            }
        }
    }
    private void onEntityDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            for (int j = 0; j < player.getInventory().items.size(); j++) {
                ItemStack stack = player.getInventory().getItem(j);
                ToolStack tool = ToolStack.from(stack);
                ModDataNBT data = tool.getPersistentData();
                if (stack.getItem() == MomotinkerItem.pocket_watch.get() && data.getInt(transmitpoints) != 0 &&tool.getModifierLevel(MomotinkerModifiers.superancientmetalsrealc.getId())>0) {
                    data.putInt(transmitpoints,0);
                }
            }
        }
    }

    private void livingattackevent(LivingAttackEvent event) {
        //if (event.getEntity().level.isClientSide) return;
        int hadal_limit = MomotinkerConfig.hadal_limit.get();
        if (event.getEntity() instanceof Player player){
            for (int j = 0; j < player.getInventory().items.size(); j++) {
                ItemStack stack = player.getInventory().getItem(j);
                ToolStack tool = ToolStack.from(stack);
                ModDataNBT data = tool.getPersistentData();
                if (data.getInt(hadal) ==hadal_limit &&tool.getModifierLevel(MomotinkerModifiers.superancientmetalsrealc.getId())>0) {
                    if (event.getSource()==DamageSource.IN_WALL||event.getSource()==DamageSource.DROWN){
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
    private void livinghurtevent(LivingHurtEvent event) {
        int hadal_limit = MomotinkerConfig.hadal_limit.get();
        int crystallized_limit = MomotinkerConfig.crystallized_limit.get();
        if (event.getEntity() instanceof Player player) {
            ToolStack tool = ToolStack.from(player.getMainHandItem());
            ModDataNBT data = tool.getPersistentData();
            if (tool.getModifierLevel(MomotinkerModifiers.superancientmetalsrealc.getId()) > 0) {
                if (data.getInt(hadal) == hadal_limit &&player.level.isNight()) {
                    event.setAmount(event.getAmount() * 0.4f);
                }
            }
        }
        if (event.getSource().getEntity() instanceof Player player&&event.getEntity()!=null){
            ToolStack tool = ToolStack.from(player.getMainHandItem());
            ModDataNBT data = tool.getPersistentData();
            if (data.getInt(crystallized)==crystallized_limit) {
                data.putInt(durabilityrecovery, (int) (event.getAmount()*0.02f));
                if (data.getInt(durabilityrecovery)>tool.getDamage()&&tool.getDamage()>0) {
                    tool.setDamage(0);
                    data.putInt(durabilityrecovery,data.getInt(durabilityrecovery)-tool.getDamage());
                }else if (data.getInt(durabilityrecovery)<tool.getDamage()&&tool.getDamage()>0){
                    tool.setDamage(tool.getDamage()-data.getInt(durabilityrecovery));
                    data.putInt(durabilityrecovery,0);
                }
                if (data.getInt(durabilityrecovery)>ToolStack.from(player.getOffhandItem()).getDamage()&&ToolStack.from(player.getOffhandItem()).getDamage()>0) {
                    ToolStack.from(player.getOffhandItem()).setDamage(0);
                    data.putInt(durabilityrecovery,data.getInt(durabilityrecovery)-ToolStack.from(player.getOffhandItem()).getDamage());
                }else if (data.getInt(durabilityrecovery)<ToolStack.from(player.getOffhandItem()).getDamage()&&ToolStack.from(player.getOffhandItem()).getDamage()>0){
                    ToolStack.from(player.getOffhandItem()).setDamage(ToolStack.from(player.getOffhandItem()).getDamage()-data.getInt(durabilityrecovery));
                    data.putInt(durabilityrecovery,0);
                }
                for (ItemStack stack : player.getInventory().armor) {
                    if (data.getInt(durabilityrecovery)>stack.getDamageValue()&&stack.getDamageValue()>0) {
                        stack.setDamageValue(0);
                        data.putInt(durabilityrecovery,data.getInt(durabilityrecovery)-stack.getDamageValue());
                    }else if (data.getInt(durabilityrecovery)<stack.getDamageValue()&&stack.getDamageValue()>0){
                        stack.setDamageValue(stack.getDamageValue()-data.getInt(durabilityrecovery));
                        data.putInt(durabilityrecovery,0);
                    }
                }
            }
        }
    }
    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity attacker =context.getAttacker();
        int hadal_limit = MomotinkerConfig.hadal_limit.get();
        int stellarcore_limit = MomotinkerConfig.stellarcore_limit.get();
        int crystallized_limit = MomotinkerConfig.crystallized_limit.get();
        int crystallized_hurt_limit = MomotinkerConfig.crystallized_hurt_limit.get();
        int sanctification_limit = MomotinkerConfig.sanctification_limit.get();
        if (attacker instanceof Player player){
            ModDataNBT a = tool.getPersistentData();
            if (a.getInt(hadal) ==hadal_limit&&player.level.isDay()){
                return damage*1.6f;
            }
            if (a.getInt(stellarcore) == stellarcore_limit){
                a.putInt(overheatingcooling, 6);
                if (a.getInt(overheat)>0) {
                    return damage*(1f+0.03f*a.getInt(overheat));
                }
            }
        }
        return damage;
    }
    @Override
    public void addAttributes(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentSlot equipmentSlot, BiConsumer<Attribute, AttributeModifier> biConsumer) {
        ModDataNBT a = iToolStackView.getPersistentData();
        int hadal_limit = MomotinkerConfig.hadal_limit.get();
        int stellarcore_limit = MomotinkerConfig.stellarcore_limit.get();
        int degenerate_limit = MomotinkerConfig.degenerate_limit.get();
        if (a.getInt(stellarcore)==stellarcore_limit) {
            biConsumer.accept(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("839E8403-B32D-B7AF-79BB-10522D5A994A"), Attributes.ATTACK_SPEED.getDescriptionId(), -0.01F*a.getInt(overheat), AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }
}