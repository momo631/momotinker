package com.momosensei.momotinker.tool;


import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerItem;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.Iterator;
import java.util.List;

public class divine_punishment_spear extends ModifiableItem {
    public divine_punishment_spear(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST,this::onEntityDeath);
    }
    public static final ResourceLocation sanctification = Momotinker.getResource("sanctification");
    public static final ResourceLocation degenerate = Momotinker.getResource("degenerate");

    private void onEntityDeath(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player&&event.getEntity()!=null) {
            if (player.getItemBySlot(EquipmentSlot.MAINHAND).is(MomotinkerItem.divine_punishment_spear.get())&& ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.MAINHAND), MomotinkerModifiers.frombrilliance.getId())>0){
                ModDataNBT a = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
                if (event.getEntity().getMobType() == MobType.UNDEAD&&a.getFloat(sanctification)<500&&a.getFloat(degenerate)<100){
                    a.putFloat(sanctification, a.getFloat(sanctification) + 1);
                }
                if (event.getEntity() instanceof Villager&&a.getFloat(sanctification)<500&&a.getFloat(degenerate)<100){
                    a.putFloat(degenerate,a.getFloat(degenerate) + 1);
                }
            }
        }
    }

    private void livinghurtevent(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player&&a!=null){
            ModDataNBT c = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
            if (!checkOffHand(player)) {
                event.setAmount(0.5F * event.getAmount());
            }
            if (ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.MAINHAND), MomotinkerModifiers.frombrilliance.getId())>0) {
                if (c.getFloat(sanctification) == 500) {
                    a.hurt(DamageSource.MAGIC.bypassMagic(), event.getAmount() * 0.25F);
                }
                if (c.getFloat(degenerate) == 100) {
                    if (player.getItemBySlot(EquipmentSlot.MAINHAND).getDamageValue() == 0) {
                        player.heal(event.getAmount() * 0.2F);
                    }
                    if (player.getItemBySlot(EquipmentSlot.MAINHAND).getDamageValue() > 0 && player.getItemBySlot(EquipmentSlot.MAINHAND).getDamageValue() != 0) {
                        player.getItemBySlot(EquipmentSlot.MAINHAND).setDamageValue((int) (player.getItemBySlot(EquipmentSlot.MAINHAND).getDamageValue() - (event.getAmount() * 0.01F)));
                    }
                }
            }
        }
    }
    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        return !player.isCreative();
    }
    public boolean hurtEnemy(ItemStack stack, LivingEntity entity, LivingEntity player) {
        stack.hurtAndBreak(1, player, (player1) -> {
            player1.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    public boolean mineBlock(ItemStack stack, Level level, BlockState blockState, BlockPos blockPos, LivingEntity entity) {
        if ((double) blockState.getDestroySpeed(level, blockPos) != 0.0D) {
            stack.hurtAndBreak(100, entity, (entity1) -> {
                entity1.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        return true;
    }
    public static boolean checkOffHand(Player player){
        return player!=null&& !player.hasItemInSlot(EquipmentSlot.OFFHAND);
    }
    public List<Component> getStatInformation(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        tooltips = this.getDivinePunishmentSpearStats(tool, player, tooltips, key, tooltipFlag);
        return tooltips;
    }
    public List<Component> getDivinePunishmentSpearStats(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
        ModDataNBT a = tool.getPersistentData();
        if (tool.hasTag(TinkerTags.Items.DURABILITY)) {
            builder.add(ToolStats.DURABILITY);
        }
        if (tool.hasTag(TinkerTags.Items.MELEE)) {
            builder.add(ToolStats.ATTACK_DAMAGE);
            builder.add(ToolStats.ATTACK_SPEED);
        }
        builder.addAllFreeSlots();
        if (a.getFloat(sanctification)<500) {
            builder.add(Component.translatable("古代神兵任务:击杀500只亡灵生物。当前击杀数为" + a.getFloat(sanctification)).withStyle(ChatFormatting.GOLD));
        }
        if (a.getFloat(sanctification)==500) {
            builder.add(Component.translatable("古代神兵任务已完成！此工具将格外造成25%魔法伤害且此伤害无视魔法防御").withStyle(ChatFormatting.YELLOW));
        }
        if (a.getFloat(degenerate)==100) {
            builder.add(Component.translatable("古代神兵隐藏任务已完成！此工具造成伤害会恢复耐久，若耐久为满则恢复使用者生命").withStyle(ChatFormatting.DARK_RED));
        }
        if (!checkOffHand(player)){
            builder.add(Component.translatable("momotinker.tool.tooltip.offhand_hastool").withStyle(ChatFormatting.RED));
        }

        Iterator var7 = tool.getModifierList().iterator();
        while(var7.hasNext()) {
            ModifierEntry entry = (ModifierEntry)var7.next();
            ((TooltipModifierHook)entry.getHook(ModifierHooks.TOOLTIP)).addTooltip(tool, entry, player, tooltips, key, tooltipFlag);
        }
        return tooltips;
    }
}