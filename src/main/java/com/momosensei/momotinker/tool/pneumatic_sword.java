package com.momosensei.momotinker.tool;


import com.momosensei.momotinker.register.MomotinkerItem;
import com.momosensei.momotinker.util.AttackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.Util;
import slimeknights.tconstruct.tools.modifiers.ability.interaction.BlockingModifier;
import slimeknights.tconstruct.tools.modifiers.upgrades.ranged.ScopeModifier;

import java.util.Iterator;
import java.util.List;

import static com.momosensei.momotinker.Momotinker.getResource;

public class pneumatic_sword extends ModifiableItem {
    public pneumatic_sword(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtevent);
    }
    private void livinghurtevent(LivingHurtEvent event) {
        Entity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player&&a!=null&&(player.getMainHandItem().is(MomotinkerItem.pneumatic_sword.get())||player.getOffhandItem().is(MomotinkerItem.pneumatic_sword.get()))){
            ModDataNBT data1 = ToolStack.from(player.getMainHandItem()).getPersistentData();
            ModDataNBT data2 = ToolStack.from(player.getOffhandItem()).getPersistentData();
            if (player.isAutoSpinAttack()){
                if (data1.getInt(getResource("issnick"))>0&&player.getOffhandItem().is(MomotinkerItem.pneumatic_sword.get())){
                    data1.putInt(getResource("issnick"),data1.getInt(getResource("issnick"))-2);
                    if (!data1.getBoolean(getResource("snicking"))) {
                        data1.putBoolean(getResource("snicking"), true);
                    }
                }
                if (data1.getInt(getResource("issnick"))>0&&!player.getOffhandItem().is(MomotinkerItem.pneumatic_sword.get())){
                    data1.putInt(getResource("issnick"),data1.getInt(getResource("issnick"))-4);
                }
                if (data2.getInt(getResource("issnick"))>0&&!player.getMainHandItem().is(MomotinkerItem.pneumatic_sword.get())){
                    data2.putInt(getResource("issnick"),data2.getInt(getResource("issnick"))-4);
                }
            }
        }
    }
    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        InventoryTickModifierHook.heldInventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        if (entityIn instanceof Player player) {
            ModDataNBT data1 = ToolStack.from(player.getMainHandItem()).getPersistentData();
            ModDataNBT data2 = ToolStack.from(player.getOffhandItem()).getPersistentData();
            if (data1.getInt(getResource("issnick"))>0&&!player.getOffhandItem().is(MomotinkerItem.pneumatic_sword.get())){
                data1.putInt(getResource("issnick"),data1.getInt(getResource("issnick"))-1);
                if (player.isOnGround())data1.putInt(getResource("issnick"),data1.getInt(getResource("issnick"))-4);
                player.hasImpulse = true;
                player.startAutoSpinAttack(4);
                player.setDeltaMovement(player.getLookAngle().scale(2));
                player.fallDistance = 0;
                List<LivingEntity> ls0 = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(1));
                for (LivingEntity targets : ls0) {
                    if (targets != player && targets != null) {
                        AttackUtil.attackEntity(ToolStack.from(player.getMainHandItem()), player, InteractionHand.MAIN_HAND, targets, () -> 1, true, Util.getSlotType(InteractionHand.MAIN_HAND), ToolStack.from(player.getMainHandItem()).getStats().get(ToolStats.ATTACK_DAMAGE), 1f, false, true, true, false);
                    }
                }
            }else
            if (data2.getInt(getResource("issnick"))>0&&!player.getMainHandItem().is(MomotinkerItem.pneumatic_sword.get())){
                data2.putInt(getResource("issnick"),data2.getInt(getResource("issnick"))-1);
                if (player.isOnGround())data2.putInt(getResource("issnick"),data2.getInt(getResource("issnick"))-4);
                player.hasImpulse = true;
                player.startAutoSpinAttack(4);
                player.setDeltaMovement(player.getLookAngle().scale(2));
                player.fallDistance = 0;
                List<LivingEntity> ls0 = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(1));
                for (LivingEntity targets : ls0) {
                    if (targets != player && targets != null) {
                        AttackUtil.attackEntity(ToolStack.from(player.getOffhandItem()), player, InteractionHand.OFF_HAND, targets, () -> 1, true, Util.getSlotType(InteractionHand.OFF_HAND), ToolStack.from(player.getOffhandItem()).getStats().get(ToolStats.ATTACK_DAMAGE), 1f, false, true, true, false);
                    }
                }
            }else
            if (data1.getInt(getResource("issnick"))>0&&player.getOffhandItem().is(MomotinkerItem.pneumatic_sword.get())) {
                data1.putInt(getResource("issnick"), data1.getInt(getResource("issnick")) - 3);
                if (player.isOnGround()&&!data1.getBoolean(getResource("snicking"))) {
                    data1.putInt(getResource("issnick"), data1.getInt(getResource("issnick")) - 4);
                }
                player.hasImpulse = true;
                player.startAutoSpinAttack(8);
                List<LivingEntity> ls0 = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(1));
                for (LivingEntity targets : ls0) {
                    if (targets != player && targets != null) {
                        AttackUtil.attackEntity(ToolStack.from(player.getMainHandItem()), player, InteractionHand.MAIN_HAND, targets, () -> 1, true, Util.getSlotType(InteractionHand.MAIN_HAND), ToolStack.from(player.getMainHandItem()).getStats().get(ToolStats.ATTACK_DAMAGE)+ToolStack.from(player.getOffhandItem()).getStats().get(ToolStats.ATTACK_DAMAGE), 1f, false, true, true, false);
                    }
                }
                double c = 3;
                if (data1.getBoolean(getResource("snicking"))&&ls0.size()!=1) {
                    data1.putInt(getResource("issnick"),data1.getInt(getResource("issnick"))+1);
                    c = 0.1;
                    player.setNoGravity(true);
                }
                player.setDeltaMovement(player.getLookAngle().scale(c));
                player.fallDistance = 0;
            }
            if (data1.getInt(getResource("issnick"))==0&&data1.getBoolean(getResource("snicking"))){
                data1.putBoolean(getResource("snicking"),false);
                player.setNoGravity(false);
            }
            if (data1.getInt(getResource("issnick"))<0)data1.putInt(getResource("issnick"),0);
            if (data2.getInt(getResource("issnick"))<0)data2.putInt(getResource("issnick"),0);
        }
    }
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ToolStack tool = ToolStack.from(stack);
        player.startUsingItem(hand);
        if (tool.isBroken()) {
            return InteractionResultHolder.fail(stack);
        }
        if (!tool.isBroken()) {
            return InteractionResultHolder.pass(stack);
        }
        return InteractionResultHolder.consume(stack);
    }
    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int chargeRemaining) {
        ToolStack tool = ToolStack.from(stack);

    }
    public int getUseDuration(ItemStack stack) {
        return 1;
    }
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return BlockingModifier.blockWhileCharging(ToolStack.from(stack), UseAnim.BLOCK);
    }
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity livingEntity) {
        ScopeModifier.stopScoping(livingEntity);
        ToolStack tool = ToolStack.from(stack);
        if (tool.isBroken()){
            return stack;
        }
        if (livingEntity instanceof Player player) {
            if (!tool.getPersistentData().getBoolean(getResource("cansnick"))||player.isOnGround()) {
                player.hasImpulse = true;
                player.startAutoSpinAttack(2);
                player.setDeltaMovement(player.getLookAngle().scale(3).add(0,1.2,0));
                player.fallDistance = 0;
                int cool = 30;
                if (player.getOffhandItem().is(MomotinkerItem.pneumatic_sword.get())) {
                    cool /= 3;
                }
                player.getCooldowns().addCooldown(stack.getItem(), cool);
                if (player.level instanceof ServerLevel level) {
                    for (int n = 0; n <= 36; n++) {
                        level.sendParticles(ParticleTypes.CLOUD, player.getX(), player.getY(), player.getZ(), 3, 0.5, 0, 0.5, 0.1);
                    }
                }
            } else if (!player.isOnGround()&&tool.getPersistentData().getBoolean(getResource("cansnick"))) {
                int c = 60;
                if (player.getOffhandItem().is(MomotinkerItem.pneumatic_sword.get()))c=180;
                tool.getPersistentData().putInt(getResource("issnick"), c);
                tool.getPersistentData().putBoolean(getResource("cansnick"), false);
            }
        }
        if (livingEntity instanceof ServerPlayer player) {
            ToolDamageUtil.damageAnimated(tool,1,player);
        }
        return stack;
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
        if (tool.hasTag(TinkerTags.Items.DURABILITY)) {
            builder.add(ToolStats.DURABILITY);
        }
        if (tool.hasTag(TinkerTags.Items.MELEE)) {
            builder.add(ToolStats.ATTACK_DAMAGE);
            builder.add(ToolStats.ATTACK_SPEED);
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