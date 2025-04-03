package com.momosensei.momotinker.tool;


import com.momosensei.momotinker.register.MomotinkerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
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
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.modifiers.ability.interaction.BlockingModifier;
import slimeknights.tconstruct.tools.modifiers.upgrades.ranged.ScopeModifier;

import java.util.Iterator;
import java.util.List;

import static com.momosensei.momotinker.Momotinker.getResource;

public class pocket_watch extends ModifiableItem {
    public pocket_watch(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST,this::onEntityDeath);
    }

    public static final ResourceLocation pocketwatch = getResource("pocketwatch");
    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        InventoryTickModifierHook.heldInventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        if (ToolStack.from(stack).getPersistentData().getInt(pocketwatch) > 0 && entityIn.tickCount % 20 == 0){
            ToolStack.from(stack).getPersistentData().putInt(pocketwatch,ToolStack.from(stack).getPersistentData().getInt(pocketwatch)-1);
        }
        if (ToolStack.from(stack).getPersistentData().getInt(pocketwatch) < 0 ){
            ToolStack.from(stack).getPersistentData().putInt(pocketwatch,0);
        }
    }

    private void onEntityDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            for (int j = 0; j < player.getInventory().items.size(); j++) {
                ItemStack stack = player.getInventory().getItem(j);
                ToolStack tool = ToolStack.from(stack);
                if (stack.getItem() == MomotinkerItem.pocket_watch.get() && tool.getPersistentData().getInt(pocketwatch)==0) {
                    event.setCanceled(true);
                    player.setHealth(player.getMaxHealth()*0.2f);
                    tryTeleport(stack,player);
                    int a = (int) (300/ (ConditionalStatModifierHook.getModifiedStat(tool,player,ToolStats.ATTACK_SPEED)+(0.1f*ConditionalStatModifierHook.getModifiedStat(tool,player,ToolStats.ATTACK_DAMAGE))));
                    if (a>180) {
                        tool.getPersistentData().putInt(pocketwatch, 180);
                    }else if (a<12) {
                        tool.getPersistentData().putInt(pocketwatch, 12);
                    }else if (a<180&&a>12){
                        tool.getPersistentData().putInt(pocketwatch, a);
                    }break;
                }
            }
        }
    }
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ToolStack tool = ToolStack.from(stack);
        player.startUsingItem(hand);
        if (tool.isBroken()){
            return InteractionResultHolder.fail(stack);
        }
        if (!tool.isBroken()) {
            return InteractionResultHolder.pass(stack);
        }
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int duration) {
        ScopeModifier.stopScoping(livingEntity);
        ToolStack tool = ToolStack.from(stack);
        if (tool.isBroken()) {
            return;
        }
        if (livingEntity instanceof ServerPlayer player){
            player.awardStat(Stats.ITEM_USED.get(this));
            if (player.isShiftKeyDown()) {
                tool.getPersistentData().putFloat(getResource("pocketwatchx"), (float) player.getX());
                tool.getPersistentData().putFloat(getResource("pocketwatchy"), (float) player.getY());
                tool.getPersistentData().putFloat(getResource("pocketwatchz"), (float) player.getZ());
                tool.getPersistentData().putString(getResource("pocketwatchlevel"),player.level.dimension().location().toString());
                player.getCooldowns().addCooldown(MomotinkerItem.pocket_watch.get(),10);
            }else if (!player.isShiftKeyDown()&&tool.getPersistentData().getInt(pocketwatch)==0) {
                int a = (int) (600/ (0.2f*ConditionalStatModifierHook.getModifiedStat(tool,player,ToolStats.ATTACK_SPEED))+(0.1f*ConditionalStatModifierHook.getModifiedStat(tool,player,ToolStats.ATTACK_DAMAGE)));
                if (a>180) {
                    tool.getPersistentData().putInt(pocketwatch, 180);
                }else if (a<12) {
                    tool.getPersistentData().putInt(pocketwatch, 12);
                }else if (a<180&&a>12){
                    tool.getPersistentData().putInt(pocketwatch, a);
                }
                tryTeleport(stack,player);
            }
        }
    }
    private static boolean tryTeleport(ItemStack stack,LivingEntity living) {
        Level world = living.getCommandSenderWorld();
        ToolStack tool = ToolStack.from(stack);
        if (world.isClientSide) {
            return false;
        }
        if (living instanceof ServerPlayer player) {
            if (!tool.getPersistentData().getString(getResource("pocketwatchlevel")).equals(living.level.dimension().location().toString())) {
                return false;
            }
            double x = tool.getPersistentData().getFloat(getResource("pocketwatchx"));
            double y = tool.getPersistentData().getFloat(getResource("pocketwatchy"));
            double z = tool.getPersistentData().getFloat(getResource("pocketwatchz"));
            int a = (int) (tool.getStats().getInt(ToolStats.DURABILITY)*0.25f);
            if (a>1000) {
                if (tool.getStats().getInt(ToolStats.DURABILITY) - tool.getDamage() < a) {
                    tool.setDamage(tool.getStats().getInt(ToolStats.DURABILITY));
                } else if (tool.getStats().getInt(ToolStats.DURABILITY) - tool.getDamage() > a) {
                    tool.setDamage(tool.getDamage() + a);
                }
            }else if (a<1000){
                if (tool.getStats().getInt(ToolStats.DURABILITY) - tool.getDamage() < 1000) {
                    tool.setDamage(tool.getStats().getInt(ToolStats.DURABILITY));;
                } else if (tool.getStats().getInt(ToolStats.DURABILITY) - tool.getDamage() > 1000) {
                    tool.setDamage(tool.getDamage() + 1000);
                }
            }

            player.teleportTo(x,y,z);
        }
        if (world instanceof ServerLevel serverWorld) {
            for (int i = 0; i < 32; ++i) {
                serverWorld.sendParticles(ParticleTypes.PORTAL, living.getX(), living.getY() + world.random.nextDouble() * 2.0D, living.getZ(), 1, world.random.nextGaussian(), 0.0D, world.random.nextGaussian(), 0);
            }
        }
        world.playSound(null, living.getX(), living.getY(), living.getZ(), Sounds.ENDERPORTING.getSound(),  living.getSoundSource(), 1f, 1f);
        return true;
    }

    public int getUseDuration(ItemStack stack) {
        return 20;
    }
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return BlockingModifier.blockWhileCharging(ToolStack.from(stack), UseAnim.BLOCK);
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
        builder.add(Component.translatable("item.momotinker.tooltip.pocket_watch1").append(tool.getPersistentData().getInt(pocketwatch)+"s"));
        int x = (int)tool.getPersistentData().getFloat(getResource("pocketwatchx"));
        int y = (int)tool.getPersistentData().getFloat(getResource("pocketwatchy"));
        int z = (int)tool.getPersistentData().getFloat(getResource("pocketwatchz"));
        builder.add(Component.translatable("item.momotinker.tooltip.pocket_watch2").append(x + ",").append(y + ",").append(z + ")"));
        if (player != null && !tool.getPersistentData().getString(getResource("pocketwatchlevel")).equals(player.level.dimension().location().toString())) {
            builder.add(Component.translatable("item.momotinker.tooltip.pocket_watch3"));
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