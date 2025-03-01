package com.momosensei.momotinker.Modifiers.modifiers;


import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.momosensei.momotinker.Modifiers.momomodifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.*;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

import static com.momosensei.momotinker.tool.divine_punishment_spear.degenerate;
import static com.momosensei.momotinker.tool.divine_punishment_spear.sanctification;

public class SuperancientMetalsReal extends momomodifier {
    public SuperancientMetalsReal() {
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        if (modifier.getLevel() > 0) {
            ToolStats.DURABILITY.multiply(builder, 1.5);
            ToolStats.ATTACK_SPEED.multiply(builder, 1.5);
            ToolStats.ATTACK_DAMAGE.multiply(builder, 1.5);
            ToolStats.ACCURACY.multiply(builder, 1.5);
            ToolStats.DRAW_SPEED.multiply(builder, 1.5);
            ToolStats.VELOCITY.multiply(builder, 1.5);
            ToolStats.MINING_SPEED.multiply(builder, 1.5);
            ToolStats.ARMOR.multiply(builder, 1.5);
            ToolStats.ARMOR_TOUGHNESS.multiply(builder, 1.5);
            ToolStats.PROJECTILE_DAMAGE.multiply(builder, 1.5);
            ToolStats.KNOCKBACK_RESISTANCE.multiply(builder, 1.5);
            ToolStats.BLOCK_AMOUNT.multiply(builder, 1.5);
            ToolStats.BLOCK_ANGLE.multiply(builder, 1.5);
        }
    }
    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity livingEntity) {
        if (modifier.getLevel()>0){
            return (int) (amount * 0.1f);
        }
        return amount;
    }

    private void livinghurtevent(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player&&a!=null){
            ModDataNBT c = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
            if (c.getFloat(sanctification)==500&&a instanceof Mob mob&&!mob.getTags().contains("BeConquered")) {
                mob.addTag("BeConquered");
            }
        }
        if (a!=null&&a.getTags().contains("beconquered")) {
            event.setAmount(event.getAmount() * 1.5F);
        }
    }
    @Override
    public LegacyDamageSource modifyDamageSource(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical, LegacyDamageSource source) {
        ModDataNBT c = tool.getPersistentData();
        if (attacker instanceof ServerPlayer player && target != null) {
            if (c.getFloat(degenerate)==100) {
                target.invulnerableTime=0;
                return source.setBypassArmor();
            }
        }
        return source;
    }

    @Override
    public LegacyDamageSource modifyArrowDamageSource(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, AbstractArrow arrow, @org.jetbrains.annotations.Nullable LivingEntity attacker, @org.jetbrains.annotations.Nullable LivingEntity target, LegacyDamageSource source) {
        if (attacker instanceof ServerPlayer player&&arrow!=null && target != null) {
            if (persistentData.getFloat(degenerate)==100) {
                target.invulnerableTime=0;
                return source.setBypassArmor();
            }
        }
        return source;
    }

    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @org.jetbrains.annotations.Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        ModDataNBT c = tool.getPersistentData();
        if (c.getFloat(degenerate)==100) {
            tooltip.add(net.minecraft.network.chat.Component.translatable("古代神兵认证:天谴之矛-染血").withStyle(ChatFormatting.DARK_RED));
            tooltip.add(net.minecraft.network.chat.Component.translatable("此工具攻击将无视护甲且不造成无敌帧").withStyle(ChatFormatting.DARK_RED));
        }
        if (c.getFloat(sanctification)==500) {
            tooltip.add(net.minecraft.network.chat.Component.translatable("古代神兵认证:天谴之矛-星辰").withStyle(ChatFormatting.YELLOW));
            tooltip.add(net.minecraft.network.chat.Component.translatable("此武器攻击怪物会永久添加“被征服者”的标记，此怪物受到的任何伤害会修正为150%").withStyle(ChatFormatting.YELLOW));
        }
    }
}