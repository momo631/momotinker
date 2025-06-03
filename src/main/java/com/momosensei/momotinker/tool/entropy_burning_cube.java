package com.momosensei.momotinker.tool;


import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerConfig;
import com.momosensei.momotinker.register.MomotinkerItem;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
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

public class entropy_burning_cube extends ModifiableItem {
    public entropy_burning_cube(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::onEntityDeath);
    }
    public static final ResourceLocation hadal = Momotinker.getResource("hadal");
    public static final ResourceLocation crystallized = Momotinker.getResource("crystallized");
    public static final ResourceLocation stellarcore = Momotinker.getResource("stellarcore");
    public static final ResourceLocation liverization = Momotinker.getResource("liverization");

    private void onEntityDeath(LivingDeathEvent event) {
        int hadal_limit = MomotinkerConfig.hadal_limit.get();
        int stellarcore_limit = MomotinkerConfig.stellarcore_limit.get();
        int crystallized_limit = MomotinkerConfig.crystallized_limit.get();
        int liverization_limit = MomotinkerConfig.liverization_limit.get();
        if (event.getSource().getEntity() instanceof Player player && event.getEntity() != null&&ModifierUtil.getModifierLevel(player.getItemBySlot(EquipmentSlot.MAINHAND), MomotinkerModifiers.frombrilliance.getId()) > 0) {
            ModDataNBT a = ToolStack.from(player.getItemBySlot(EquipmentSlot.MAINHAND)).getPersistentData();
            if (player.getItemBySlot(EquipmentSlot.MAINHAND).is(MomotinkerItem.entropy_burning_riding_spear.get())) {
                if (event.getEntity().getMobType() == MobType.WATER && a.getInt(hadal) < hadal_limit && a.getInt(stellarcore) < stellarcore_limit&& a.getInt(crystallized) < crystallized_limit&& a.getInt(liverization) < liverization_limit) {
                    a.putInt(hadal, a.getInt(hadal) + 1);
                }
            }
            if (player.getItemBySlot(EquipmentSlot.MAINHAND).is(MomotinkerItem.entropy_burning_sword.get())) {
                if (event.getEntity().isOnFire() && a.getInt(hadal) < hadal_limit && a.getInt(stellarcore) < stellarcore_limit&& a.getInt(crystallized) < crystallized_limit&& a.getInt(liverization) < liverization_limit) {
                    a.putInt(stellarcore, a.getInt(stellarcore) + 1);
                }
            }
            if (player.getItemBySlot(EquipmentSlot.MAINHAND).is(MomotinkerItem.entropy_burning_cannon.get())) {
                if (event.getEntity() instanceof WitherBoss && a.getInt(hadal) < hadal_limit && a.getInt(stellarcore) < stellarcore_limit&& a.getInt(crystallized) < crystallized_limit&& a.getInt(liverization) < liverization_limit) {
                    a.putInt(crystallized, a.getInt(crystallized) + 1);
                }
            }
            if (player.getItemBySlot(EquipmentSlot.MAINHAND).is(MomotinkerItem.entropy_burning_cube.get())) {
                if (event.getEntity() instanceof Animal&& a.getInt(hadal) < hadal_limit && a.getInt(stellarcore) < stellarcore_limit&& a.getInt(crystallized) < crystallized_limit&& a.getInt(liverization) < liverization_limit) {
                    a.putInt(liverization, a.getInt(liverization) + 1);
                }
            }
        }
    }
    private void livinghurtevent(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player&&a!=null&&player.getMainHandItem().is(MomotinkerItem.entropy_burning_cube.get())){
            event.setAmount(event.getAmount()*0.006f);
        }
    }
    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        return !player.isCreative();
    }

    public List<Component> getStatInformation(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        tooltips = this.getStats(tool, player, tooltips, key, tooltipFlag);
        return tooltips;
    }
    public List<Component> getStats(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        TooltipBuilder builder = new TooltipBuilder(tool, tooltips);
        int hadal_limit = MomotinkerConfig.hadal_limit.get();
        int stellarcore_limit = MomotinkerConfig.stellarcore_limit.get();
        int crystallized_limit = MomotinkerConfig.crystallized_limit.get();
        int liverization_limit = MomotinkerConfig.liverization_limit.get();
        ModDataNBT a = tool.getPersistentData();
        if (tool.hasTag(TinkerTags.Items.DURABILITY)) {
            builder.add(ToolStats.DURABILITY);
        }
        if (tool.hasTag(TinkerTags.Items.MELEE)) {
            builder.add(ToolStats.ATTACK_DAMAGE);
            builder.add(ToolStats.ATTACK_SPEED);
        }
        if (a.getInt(hadal)<hadal_limit&&a.getInt(stellarcore)<stellarcore_limit&&a.getInt(crystallized)<crystallized_limit&&a.getInt(liverization)<liverization_limit) {
            builder.add(Component.translatable("item.momotinker.tooltip.hadal").append(hadal_limit + "").append(Component.translatable("item.momotinker.tooltip.hadal1")).append(a.getInt(hadal) + "").withStyle(ChatFormatting.GOLD));
            builder.add(Component.translatable("item.momotinker.tooltip.stellarcore").append(stellarcore_limit + "").append(Component.translatable("item.momotinker.tooltip.stellarcore1")).append(a.getInt(stellarcore) + "").withStyle(ChatFormatting.GOLD));
            builder.add(Component.translatable("item.momotinker.tooltip.crystallized").append(crystallized_limit + "").append(Component.translatable("item.momotinker.tooltip.crystallized1")).append(a.getInt(crystallized) + "").withStyle(ChatFormatting.GOLD));
        }
        if (a.getInt(hadal)==hadal_limit) {
            builder.add(Component.translatable("item.momotinker.tooltip.hadal2").withStyle(ChatFormatting.DARK_BLUE));
        }
        if (a.getInt(stellarcore)==stellarcore_limit) {
            builder.add(Component.translatable("item.momotinker.tooltip.stellarcore2").withStyle(ChatFormatting.GOLD));
        }
        if (a.getInt(crystallized)==crystallized_limit) {
            builder.add(Component.translatable("item.momotinker.tooltip.crystallized2").withStyle(ChatFormatting.AQUA));
        }
        if (a.getInt(liverization)>=liverization_limit) {
            if (a.getInt(liverization)==liverization_limit) {
                builder.add(Component.translatable("item.momotinker.tooltip.liverization").withStyle(ChatFormatting.RED));
                builder.add(Component.translatable("item.momotinker.tooltip.liverization1").withStyle(ChatFormatting.RED));
            }
            if (a.getInt(liverization)>liverization_limit) {
                builder.add(Component.translatable("item.momotinker.tooltip.liverization2").withStyle(ChatFormatting.GRAY));
            }
        }

        builder.addAllFreeSlots();
        Iterator var7 = tool.getModifierList().iterator();
        while(var7.hasNext()) {
            ModifierEntry entry = (ModifierEntry)var7.next();
            entry.getHook(ModifierHooks.TOOLTIP).addTooltip(tool, entry, player, tooltips, key, tooltipFlag);
        }
        return tooltips;
    }
}