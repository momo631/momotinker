package com.momosensei.momotinker.Items.tool.gungnir;

import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.data.gungnir.GungnirMarkedData;
import com.momosensei.momotinker.data.gungnir.GungnirRaycast;
import com.momosensei.momotinker.data.gungnir.GungnirState;
import com.momosensei.momotinker.data.gungnir.GungnirTrajectoryClient;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.HudCharge.ToolsTimeCharge;
import com.momosensei.momotinker.util.AttackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
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
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.Util;
import slimeknights.tconstruct.tools.modifiers.ability.interaction.BlockingModifier;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static com.momosensei.momotinker.util.ChunkLoadManager.startForceLoadingForEntity;
import static slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook.KEY_DRAWTIME;

public class gungnir extends ModifiableItem {
    public gungnir(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
        MinecraftForge.EVENT_BUS.addListener(this::OnLivingDrop);
        MinecraftForge.EVENT_BUS.addListener(this::OnLivingHurt);
    }

    public static final String its_gungnir_key = Momotinker.getResource("its_gungnir").toString();
    public static final String its_gungnir_damage_key = Momotinker.getResource("its_gungnir_damage").toString();
    public static final String its_gungnir_trajectory_key = Momotinker.getResource("its_gungnir_trajectory").toString();

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ToolStack tool = ToolStack.from(stack);
        if (tool.isBroken()) return InteractionResultHolder.fail(stack);

        tool.getPersistentData().putInt(KEY_DRAWTIME, 10);
        player.startUsingItem(hand);

        if (player instanceof ServerPlayer sp) {
            MinecraftServer server = sp.getServer();
            if (server != null) {
                GungnirMarkedData data = GungnirMarkedData.get(server);
                UUID pid = sp.getUUID();
                if (data.isFired(pid) || data.hasMark(pid)) {
                    GungnirState.settle(sp, hand, true);
                    return InteractionResultHolder.success(sp.getItemInHand(hand));
                }
            }
        }
        return InteractionResultHolder.consume(stack);
    }


    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int duration) {
        AttackUtil.stopScoping(livingEntity);
        ToolStack tool = ToolStack.from(stack);

        if (tool.isBroken()) {
            tool.getPersistentData().remove(KEY_DRAWTIME);
            return;
        }
        if (!(livingEntity instanceof Player player)) return;

        player.awardStat(net.minecraft.stats.Stats.ITEM_USED.get(this));
        ToolDamageUtil.damageAnimated(tool, 1, player);
        if (player instanceof ServerPlayer sp) {
            Channel.sendToPlayer(new ToolsTimeCharge(0), sp);
        }
        int i = this.getUseDuration(stack) - duration;
        int drawTime = 10;
        if (i>drawTime){
            LivingEntity target = GungnirRaycast.findNearestToRay(player, 150);
            if (target != null && player instanceof ServerPlayer sp) {
                tryFire(sp, tool, target);
            }
        }
        tool.getPersistentData().remove(KEY_DRAWTIME);
    }
    private static void tryFire(ServerPlayer sp, ToolStack tool, LivingEntity target) {
        MinecraftServer server = sp.getServer();
        if (server == null) return;

        GungnirMarkedData data = GungnirMarkedData.get(server);
        UUID playerId = sp.getUUID();
        if (data.hasMark(playerId)) return;

        InteractionHand usedHand = sp.getUsedItemHand();
        int slot = usedHand == InteractionHand.OFF_HAND
                ? sp.getInventory().getContainerSize() - 1
                : sp.getInventory().selected;
        ItemStack usedStack = sp.getItemInHand(usedHand);

        target.invulnerableTime = 0;
        AttackUtil.attackEntity(tool, sp, InteractionHand.MAIN_HAND, target, () -> 1,
                true, Util.getSlotType(InteractionHand.MAIN_HAND),
                tool.getStats().get(ToolStats.ATTACK_DAMAGE), 1f,
                false, true, true, false);
        GungnirTrajectoryClient.addGungnirTrajectory(sp,target,usedHand);
        if (!target.isAlive()) return;

        if (!GungnirState.disable(sp, slot)) return;

        data.setSlot(playerId, slot);
        GungnirState.markEntity(target, usedStack, playerId);
        startForceLoadingForEntity(target, target.level().dimension());
    }

    private void OnLivingDrop(LivingDropsEvent event) {
        if (event.getEntity().isAlive()) return;
        GungnirState.onEntityRemoved(event.getEntity());
    }
    private void OnLivingHurt(LivingHurtEvent event) {
        LivingEntity a = event.getEntity();
        Entity b = event.getSource().getEntity();
        if (b instanceof Player player && a != null) {
            float key = a.getPersistentData().getFloat(its_gungnir_damage_key);
            float damage = event.getAmount();
            if (key > 0&&a.level() instanceof ServerLevel sl) {
                GungnirMarkedData data = GungnirMarkedData.get(sl.getServer());
                UUID ownerId = data.getPlayerForEntity(a.getUUID());
                if (ownerId != null) {
                    Player owner = sl.getServer().getPlayerList().getPlayer(ownerId);
                    if (owner != null&&owner==player) {
                        GungnirState.setGungnirDamageNbt(a, key + damage*0.5f);
                    }
                }
            }
        }
    }
    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int chargeRemaining) {
        if (living instanceof ServerPlayer player) {
            float perc = Mth.clamp((float) (this.getUseDuration(stack) - chargeRemaining) / 10, 0, 1);
            Channel.sendToPlayer(new ToolsTimeCharge(perc), player);
        }
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return BlockingModifier.blockWhileCharging(ToolStack.from(stack), UseAnim.SPEAR);
    }

    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        return !player.isCreative();
    }

    public @NotNull List<Component> getStatInformation(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
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
        while (var7.hasNext()) {
            ModifierEntry entry = (ModifierEntry) var7.next();
            entry.getHook(ModifierHooks.TOOLTIP).addTooltip(tool, entry, player, tooltips, key, tooltipFlag);
        }
        return tooltips;
    }
}
