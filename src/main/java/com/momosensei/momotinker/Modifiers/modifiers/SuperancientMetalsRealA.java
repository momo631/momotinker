package com.momosensei.momotinker.Modifiers.modifiers;


import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.momosensei.momotinker.Modifiers.momomodifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerConfig;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.level.BlockEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.*;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

import static com.momosensei.momotinker.Momotinker.getResource;
import static com.momosensei.momotinker.tool.divine_punishment_spear.degenerate;
import static com.momosensei.momotinker.tool.divine_punishment_spear.sanctification;
import static com.momosensei.momotinker.tool.entropy_burning_cube.*;
import static com.momosensei.momotinker.tool.pocket_watch.backtracking;
import static com.momosensei.momotinker.tool.pocket_watch.transmit;

public class SuperancientMetalsRealA extends momomodifier {
    public SuperancientMetalsRealA() {
        MinecraftForge.EVENT_BUS.addListener(this::AddMobEffect);
        MinecraftForge.EVENT_BUS.addListener(this::onBreakEvent);
    }
    public static final ResourceLocation degeneratepoints = Momotinker.getResource("degeneratepoints");
    public static final ResourceLocation stellarcorepoints = Momotinker.getResource("stellarcorepoints");
    public static final ResourceLocation crystallizedpoints = Momotinker.getResource("crystallizedpoints");
    public static final ResourceLocation liverizationpointa = Momotinker.getResource("liverizationpointa");
    public static final ResourceLocation liverizationpointb = Momotinker.getResource("liverizationpointb");
    public static final ResourceLocation tpprotection = Momotinker.getResource("tpprotection");

    private static final String be_conquered = getResource("be_conquered").toString();

    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Override
    public @javax.annotation.Nullable Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(degeneratepoints);
        iToolStackView.getPersistentData().remove(stellarcorepoints);
        iToolStackView.getPersistentData().remove(crystallizedpoints);
        iToolStackView.getPersistentData().remove(liverizationpointa);
        iToolStackView.getPersistentData().remove(liverizationpointb);
        iToolStackView.getPersistentData().remove(tpprotection);
        return null;
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        if (modifier.getLevel() > 0) {
            ToolStats.DURABILITY.multiply(builder, 1.8);
            ToolStats.ATTACK_SPEED.multiply(builder, 1.8);
            ToolStats.ATTACK_DAMAGE.multiply(builder, 1.8);
            ToolStats.ACCURACY.multiply(builder, 1.8);
            ToolStats.DRAW_SPEED.multiply(builder, 1.8);
            ToolStats.VELOCITY.multiply(builder, 1.8);
            ToolStats.MINING_SPEED.multiply(builder, 1.8);
            ToolStats.ARMOR.multiply(builder, 1.8);
            ToolStats.ARMOR_TOUGHNESS.multiply(builder, 1.8);
            ToolStats.PROJECTILE_DAMAGE.multiply(builder, 1.8);
            ToolStats.KNOCKBACK_RESISTANCE.multiply(builder, 1.8);
            ToolStats.BLOCK_AMOUNT.multiply(builder, 1.8);
            ToolStats.BLOCK_ANGLE.multiply(builder, 1.8);
        }
    }
    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity livingEntity) {
        if (modifier.getLevel()>0){
            return (int) (amount * 0.1f);
        }
        return amount;
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        int degenerate_limit = MomotinkerConfig.degenerate_limit.get();
        int stellarcore_limit = MomotinkerConfig.stellarcore_limit.get();
        int crystallized_limit = MomotinkerConfig.crystallized_limit.get();
        int liverization_limit = MomotinkerConfig.liverization_limit.get();
        ModDataNBT a = tool.getPersistentData();
        if (a.getInt(degenerate)==degenerate_limit) {
            if (entity instanceof Player player&&a.getInt(degeneratepoints)>0){
                if (isToolStack(player.getMainHandItem())&&player.tickCount%2==0){
                    if (ToolStack.from(player.getMainHandItem()).getPersistentData().getInt(degenerate)!=degenerate_limit){
                        a.putInt(degeneratepoints,a.getInt(degeneratepoints)-1);
                    }
                }else if (player.tickCount%10==0) {
                    a.putInt(degeneratepoints, a.getInt(degeneratepoints) - 1);
                }
            }
            if (a.getInt(degeneratepoints)>20) {
                a.putInt(degeneratepoints,20);
            }
            if (a.getInt(degeneratepoints)<0) {
                a.putInt(degeneratepoints,0);
            }
        }
        if (a.getInt(stellarcore)==stellarcore_limit) {
            if (entity instanceof Player player&&a.getInt(stellarcorepoints)!=player.getArmorValue()){
                a.putInt(stellarcorepoints,player.getArmorValue());
            }
        }
        if (a.getInt(crystallized)==crystallized_limit) {
            int amount =tool.getStats().getInt(ToolStats.DURABILITY);
            int b =((int) Math.log10(amount))-3;
            if (b>=0&&b!=a.getInt(crystallizedpoints)){
                a.putInt(crystallizedpoints, b);
            }
        }
        if (a.getInt(liverization)>=liverization_limit) {
            if (a.getInt(liverizationpointb)<20) {
                a.putInt(liverizationpointb, 20);
            }
            if (a.getInt(liverizationpointb)>10&&a.getInt(liverizationpointa)==a.getInt(liverizationpointb)){
                ToolDataNBT b =ToolStack.from(stack).getPersistentData();
                b.addSlots(SlotType.ABILITY, 1);
                b.addSlots(SlotType.DEFENSE, 1);
                b.addSlots(SlotType.UPGRADE, 1);
                a.putInt(liverizationpointa,0);
                a.putInt(liverizationpointb,a.getInt(liverizationpointb)*2);
            }
        }
        if (a.getInt(tpprotection)<0) {
            a.putInt(tpprotection,0);
        }
        if (a.getInt(tpprotection)>0) {
            a.putInt(tpprotection,a.getInt(tpprotection)-1);
            if (entity instanceof Player player&&player.level() instanceof ServerLevel level) {
                for (int i = 0; i < 32; ++i) {
                    level.sendParticles(ParticleTypes.PORTAL, player.getX(), player.getY(), player.getZ(), 1, 0, 0, 0, 0.5);
                }
            }
        }
    }
    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity attacker = context.getAttacker();
        LivingEntity entity = context.getLivingTarget();
        int sanctification_limit = MomotinkerConfig.sanctification_limit.get();
        int degenerate_limit = MomotinkerConfig.degenerate_limit.get();
        int hadal_limit = MomotinkerConfig.hadal_limit.get();
        int crystallized_limit = MomotinkerConfig.crystallized_limit.get();
        if (attacker instanceof Player player) {
            ModDataNBT a = tool.getPersistentData();
            if (a.getInt(sanctification) == sanctification_limit && entity!=null && !isBeConquered(entity)) {
                setBeConquered(entity);
            }
            if (a.getInt(crystallized)==crystallized_limit&&a.getInt(crystallizedpoints)>0) {
                float b= (float) Math.pow(1.15,a.getInt(crystallizedpoints));
                return damage*b;
            }
            if (a.getInt(hadal) == hadal_limit&&context.getLivingTarget()!=null) {
                context.getLivingTarget().invulnerableTime = 0;
                context.getLivingTarget().hurt(player.level().damageSources().dragonBreath(), context.getLivingTarget().getMaxHealth() * 0.05f);
                context.getLivingTarget().invulnerableTime = 0;
            }
            if (a.getInt(degenerate) == degenerate_limit&&entity!=null) {
                if (a.getInt(degeneratepoints)<20) {
                    a.putInt(degeneratepoints, a.getInt(degeneratepoints) + 2);
                }
            }
        }
        return damage;
    }
    @Override
    public void OnEntityDeath(LivingDeathEvent event) {
        int liverization_limit = MomotinkerConfig.liverization_limit.get();
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player&& a != null&&isToolStack(player.getMainHandItem())){
            ToolStack tool = ToolStack.from(player.getMainHandItem());
            ModDataNBT c = tool.getPersistentData();
            if (tool.getModifierLevel(MomotinkerModifiers.superancientmetalsreala.getId())>0&&c.getInt(liverization)>=liverization_limit){
                if (c.getInt(liverizationpointa)<c.getInt(liverizationpointb)){
                    c.putInt(liverizationpointa,c.getInt(liverizationpointa)+1);
                }
            }
        }
    }

    @Override
    public void OnLivingHurt(LivingHurtEvent event) {
        int backtracking_limit = MomotinkerConfig.backtracking_limit.get();
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player && a != null&&isToolStack(player.getMainHandItem())) {
            ModDataNBT c = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
            if (c.getInt(tpprotection)>0) {
                c.putInt(tpprotection,0);
            }
        }
        if (a instanceof LivingEntity living && isBeConquered(living)) {
            event.setAmount(event.getAmount() * 1.8F);
        }
        if (a instanceof Player player) {
            for (int j = 0; j < player.getInventory().items.size(); j++) {
                ItemStack stack = player.getInventory().getItem(j);
                if (isToolStack(stack)) {
                    ToolStack tool = ToolStack.from(stack);
                    ModDataNBT data = tool.getPersistentData();
                    if (data.getInt(backtracking) == backtracking_limit && tool.getModifierLevel(MomotinkerModifiers.superancientmetalsreala.getId()) > 0 && data.getInt(tpprotection) > 0) {
                        event.setAmount(0);
                    }
                }
            }
        }
    }
    private static boolean isBeConquered(LivingEntity living){
        var data=living.getPersistentData();
        return data.getBoolean(be_conquered);
    }
    private static void setBeConquered(LivingEntity living){
        var data =living.getPersistentData();
        data.putBoolean(be_conquered, true);
    }
    @Override
    public LegacyDamageSource modifyDamageSource(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical, LegacyDamageSource source) {
        int degenerate_limit = MomotinkerConfig.degenerate_limit.get();
        ModDataNBT c = tool.getPersistentData();
        if (attacker instanceof Player player && target instanceof LivingEntity entity ) {
            if (tool.getModifierLevel(MomotinkerModifiers.superancientmetalsreala.getId()) > 0&&c.getInt(degenerate) == degenerate_limit) {
                entity.invulnerableTime=0;
                return source.setBypassArmor();
            }
        }
        return source;
    }

    @Override
    public LegacyDamageSource modifyArrowDamageSource(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, AbstractArrow arrow, @Nullable LivingEntity attacker, @NotNull Entity target, LegacyDamageSource source) {
        int degenerate_limit = MomotinkerConfig.degenerate_limit.get();
        if (attacker instanceof Player player&& arrow!=null && target instanceof LivingEntity entity && isToolStack(player.getMainHandItem())) {
            ModDataNBT c = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
            if (modifiers.getLevel(MomotinkerModifiers.superancientmetalsreala.getId()) > 0&&c.getInt(degenerate) == degenerate_limit) {
                entity.invulnerableTime=0;
                return source.setBypassArmor();
            }
        }
        return source;
    }
    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifierEntry, EquipmentSlot equipmentSlot, BiConsumer<Attribute, AttributeModifier> biConsumer) {
        ModDataNBT a = tool.getPersistentData();
        int degenerate_limit = MomotinkerConfig.degenerate_limit.get();
        int stellarcore_limit = MomotinkerConfig.stellarcore_limit.get();
        if (a.getInt(stellarcore)==stellarcore_limit) {
            biConsumer.accept(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("30324209-D3A4-4BDB-988F-81F683850386"), Attributes.MAX_HEALTH.getDescriptionId(), a.getInt(stellarcorepoints)*0.6f, AttributeModifier.Operation.ADDITION));
            biConsumer.accept(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(UUID.fromString("F25D09BF-FD6D-0ACE-AD38-05916FF9096A"), Attributes.ARMOR_TOUGHNESS.getDescriptionId(), a.getInt(stellarcorepoints)*0.3f, AttributeModifier.Operation.ADDITION));
        }
        if (a.getInt(degenerate) == degenerate_limit&&a.getInt(degeneratepoints)>0) {
            biConsumer.accept(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("294E32F4-48AE-F55E-8111-2AA9C97E1689"), Attributes.ATTACK_SPEED.getDescriptionId(), a.getInt(degeneratepoints)*0.04f, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }
    public void AddMobEffect(MobEffectEvent.Added event) {
        int transmit_limit = MomotinkerConfig.transmit_limit.get();
        if (event.getEntity() instanceof Player player) {
            for (int j = 0; j < player.getInventory().items.size(); j++) {
                ItemStack stack = player.getInventory().getItem(j);
                if (isToolStack(stack)) {
                    ToolStack tool = ToolStack.from(stack);
                    ModDataNBT data = tool.getPersistentData();
                    if (data.getInt(transmit) == transmit_limit && tool.getModifierLevel(MomotinkerModifiers.superancientmetalsreala.getId()) > 0) {
                        var instance = event.getEffectInstance();
                        var effect = instance.getEffect();
                        instance.duration = instance.mapDuration(b -> {
                            if (instance.isInfiniteDuration() || effect.isInstantenous() || !effect.isBeneficial())
                                return b;
                            return b * 2;
                        });
                    }
                }
            }
        }
    }
    private void onBreakEvent(BlockEvent.BreakEvent event) {
        int backtracking_limit = MomotinkerConfig.backtracking_limit.get();
        Player player = event.getPlayer();
        if (player!=null) {
            for (int j = 0; j < player.getInventory().items.size(); j++) {
                ItemStack stack = player.getInventory().getItem(j);
                if (isToolStack(stack)) {
                    ToolStack tool = ToolStack.from(stack);
                    ModDataNBT data = tool.getPersistentData();
                    if (data.getInt(backtracking) == backtracking_limit && tool.getModifierLevel(MomotinkerModifiers.superancientmetalsreala.getId()) > 0) {
                        data.putInt(tpprotection, 0);
                    }
                }
            }
        }
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> builder, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        int sanctification_limit = MomotinkerConfig.sanctification_limit.get();
        int degenerate_limit = MomotinkerConfig.degenerate_limit.get();
        int hadal_limit = MomotinkerConfig.hadal_limit.get();
        int stellarcore_limit = MomotinkerConfig.stellarcore_limit.get();
        int crystallized_limit = MomotinkerConfig.crystallized_limit.get();
        int liverization_limit = MomotinkerConfig.liverization_limit.get();
        int transmit_limit = MomotinkerConfig.transmit_limit.get();
        int backtracking_limit = MomotinkerConfig.backtracking_limit.get();
        ModDataNBT a = tool.getPersistentData();
        if (a.getInt(degenerate)==degenerate_limit) {
            builder.add(Component.translatable("modifier.momotinker.tooltip.degenerate2").withStyle(ChatFormatting.DARK_RED));
        }
        if (a.getInt(sanctification)==sanctification_limit) {
            builder.add(Component.translatable("modifier.momotinker.tooltip.sanctification2").withStyle(ChatFormatting.YELLOW));
        }
        if (a.getInt(hadal)==hadal_limit) {
            builder.add(Component.translatable("modifier.momotinker.tooltip.hadal3").withStyle(ChatFormatting.DARK_BLUE));
        }
        if (a.getInt(stellarcore)==stellarcore_limit) {
            builder.add(Component.translatable("modifier.momotinker.tooltip.stellarcore3").withStyle(ChatFormatting.GOLD));
        }
        if (a.getInt(crystallized)==crystallized_limit) {
            float b= (float) Math.pow(1.15,a.getInt(crystallizedpoints));
            builder.add(Component.translatable("modifier.momotinker.tooltip.crystallized3").withStyle(ChatFormatting.AQUA));
            builder.add(Component.translatable("modifier.momotinker.tooltip.crystallized31").append(b*100+"%").withStyle(ChatFormatting.AQUA));
        }
        if (a.getInt(liverization)>=liverization_limit) {
           builder.add(Component.translatable("modifier.momotinker.tooltip.liverization3").withStyle(ChatFormatting.RED));
           builder.add(Component.translatable("modifier.momotinker.tooltip.liverization31").append(a.getInt(liverizationpointa)+",").append(Component.translatable("modifier.momotinker.tooltip.liverization32")).append(a.getInt(liverizationpointb)+"").withStyle(ChatFormatting.RED));
        }
        if (a.getInt(transmit)==transmit_limit) {
            builder.add(Component.translatable("modifier.momotinker.tooltip.transmit3").withStyle(ChatFormatting.GREEN));
        }
        if (a.getInt(backtracking)==backtracking_limit) {
            builder.add(Component.translatable("modifier.momotinker.tooltip.backtracking3").withStyle(ChatFormatting.DARK_PURPLE));
        }
    }
}