package com.momosensei.momotinker.tool;


import com.momosensei.momotinker.register.MomotinkerConfig;
import com.momosensei.momotinker.register.MomotinkerItem;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import com.momosensei.momotinker.util.AttackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
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
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.Util;
import slimeknights.tconstruct.tools.modifiers.ability.interaction.BlockingModifier;
import slimeknights.tconstruct.tools.modifiers.upgrades.ranged.ScopeModifier;

import java.util.Iterator;
import java.util.List;

import static com.momosensei.momotinker.Modifiers.modifiers.SuperancientMetalsRealA.tpprotection;
import static com.momosensei.momotinker.Momotinker.getResource;
import static com.momosensei.momotinker.Momotinker.getResourceLocation;
import static net.minecraft.core.Registry.DIMENSION_REGISTRY;

public class pocket_watch extends ModifiableItem {
    public pocket_watch(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST,this::onEntityDeath);
    }

    public static final ResourceLocation pocketwatch = getResource("pocketwatch");
    public static final ResourceLocation backtracking = getResource("backtracking");
    public static final ResourceLocation transmit = getResource("transmit");

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        InventoryTickModifierHook.heldInventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        ModDataNBT data =ToolStack.from(stack).getPersistentData();
        int backtracking_limit = MomotinkerConfig.backtracking_limit.get();
        if (data.getInt(pocketwatch) > 0 && entityIn.tickCount % 20 == 0){
            data.putInt(pocketwatch,data.getInt(pocketwatch)-1);
        }
        if (data.getInt(pocketwatch) < 0 ){
            data.putInt(pocketwatch,0);
        }
        if (data.getInt(backtracking)==backtracking_limit){
            if (entityIn instanceof Player player&&player.tickCount%20==0){
                data.putFloat(getResource("fx"), data.getFloat(getResource("ex")));
                data.putFloat(getResource("fy"), data.getFloat(getResource("ey")));
                data.putFloat(getResource("fz"), data.getFloat(getResource("ez")));
                data.putString(getResourceLocation("flevel"),data.getString(getResourceLocation("elevel")));
                data.putFloat(getResource("ex"), data.getFloat(getResource("dx")));
                data.putFloat(getResource("ey"), data.getFloat(getResource("dy")));
                data.putFloat(getResource("ez"), data.getFloat(getResource("dz")));
                data.putString(getResourceLocation("elevel"),data.getString(getResourceLocation("dlevel")));
                data.putFloat(getResource("dx"), data.getFloat(getResource("cx")));
                data.putFloat(getResource("dy"), data.getFloat(getResource("cy")));
                data.putFloat(getResource("dz"), data.getFloat(getResource("cz")));
                data.putString(getResourceLocation("dlevel"),data.getString(getResourceLocation("clevel")));
                data.putFloat(getResource("cx"), data.getFloat(getResource("bx")));
                data.putFloat(getResource("cy"), data.getFloat(getResource("by")));
                data.putFloat(getResource("cz"), data.getFloat(getResource("bz")));
                data.putString(getResourceLocation("clevel"),data.getString(getResourceLocation("blevel")));
                data.putFloat(getResource("bx"), data.getFloat(getResource("ax")));
                data.putFloat(getResource("by"), data.getFloat(getResource("ay")));
                data.putFloat(getResource("bz"), data.getFloat(getResource("az")));
                data.putString(getResourceLocation("blevel"),data.getString(getResourceLocation("alevel")));
                data.putFloat(getResource("ax"), (float) player.getX());
                data.putFloat(getResource("ay"), (float) player.getY());
                data.putFloat(getResource("az"), (float) player.getZ());
                data.putString(getResourceLocation("alevel"),player.level.dimension().location().toString());

            }
        }
    }

    private void onEntityDeath(LivingDeathEvent event) {
        int backtracking_limit = MomotinkerConfig.backtracking_limit.get();
        int transmit_limit = MomotinkerConfig.transmit_limit.get();
        if (event.getEntity() instanceof Player player) {
            for (int j = 0; j < player.getInventory().items.size(); j++) {
                ItemStack stack = player.getInventory().getItem(j);
                if (stack.getItem() == MomotinkerItem.pocket_watch.get()) {
                    ToolStack tool = ToolStack.from(stack);
                    ModDataNBT data = tool.getPersistentData();
                    if (data.getInt(transmit) != transmit_limit) {
                        double x = data.getFloat(getResource("pocketwatchx"));
                        double y = data.getFloat(getResource("pocketwatchy"));
                        double z = data.getFloat(getResource("pocketwatchz"));
                        double x1 = data.getFloat(getResource("fx"));
                        double y1 = data.getFloat(getResource("fy"));
                        double z1 = data.getFloat(getResource("fz"));
                        if (data.getInt(backtracking) == backtracking_limit && data.getInt(pocketwatch) <= getcooltime(player, tool, 400, 120, 8) * 0.5f) {
                            if (tool.getModifierLevel(MomotinkerModifiers.superancientmetalsrealb.getId()) == 0 && data.getInt(pocketwatch) != 0) {
                                return;
                            }
                            event.setCanceled(true);
                            player.setHealth(player.getMaxHealth() * 0.2f);
                            tryTeleport(stack, player, x1, y1, z1, data.getString(getResourceLocation("flevel")));
                            if (tool.getModifierLevel(MomotinkerModifiers.superancientmetalsreala.getId()) > 0) {
                                data.putInt(tpprotection, 240);
                            }
                            if (tool.getModifierLevel(MomotinkerModifiers.superancientmetalsrealc.getId()) > 0) {
                                player.heal((player.getMaxHealth() - player.getHealth()) * 0.4f);
                                List<LivingEntity> ls0 = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(4, 4, 4));
                                for (LivingEntity targets : ls0) {
                                    if (targets != player && targets != null) {
                                        AttackUtil.attackEntity(tool, player, InteractionHand.MAIN_HAND, targets, () -> 1, true, Util.getSlotType(InteractionHand.MAIN_HAND), tool.getStats().get(ToolStats.ATTACK_DAMAGE) + player.getMaxHealth(), 2f, false, true, true, false);
                                    }
                                }
                                if (player.level instanceof ServerLevel serverLevel) {
                                    for (int i = 0; i <= 100; i++) {
                                        serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, player.getX(), player.getY(), player.getZ(), 1, 4, 4, 4, 1);
                                    }
                                }
                            }
                            if (data.getInt(pocketwatch) > 0) {
                                player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(player.getMaxHealth() * 0.5f);
                            }
                            data.putInt(pocketwatch, getcooltime(player, tool, 400, 120, 8));
                            break;
                        } else if (data.getInt(backtracking) != backtracking_limit && data.getInt(pocketwatch) == 0) {
                            event.setCanceled(true);
                            player.setHealth(player.getMaxHealth() * 0.2f);
                            tryTeleport(stack, player, x, y, z, data.getString(getResourceLocation("pocketwatchlevel")));
                            if (tool.getPersistentData().getInt(backtracking) < backtracking_limit && data.getInt(transmit) != transmit_limit) {
                                tool.getPersistentData().putInt(backtracking, tool.getPersistentData().getInt(backtracking) + 1);
                            }
                            data.putInt(pocketwatch, getcooltime(player, tool, 600, 180, 12));
                            break;
                        }
                    }
                }
            }
        }
    }
    public static int getcooltime(Player player,ToolStack tool,int i,int max,int min){
        int a = (int) (i/ (ConditionalStatModifierHook.getModifiedStat(tool,player,ToolStats.ATTACK_SPEED)+0.1f*ConditionalStatModifierHook.getModifiedStat(tool,player,ToolStats.ATTACK_DAMAGE)));
        if (a>=max) {
            return max;
        }else return Math.max(a, min);
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
        ModDataNBT data =tool.getPersistentData();
        int backtracking_limit = MomotinkerConfig.backtracking_limit.get();
        int transmit_limit = MomotinkerConfig.transmit_limit.get();
        if (tool.isBroken()) {
            return;
        }
        if (data.getInt(backtracking)==backtracking_limit) {
            return;
        }
        if (livingEntity instanceof ServerPlayer player){
            player.awardStat(Stats.ITEM_USED.get(this));
            if (player.isShiftKeyDown()) {
                data.putFloat(getResource("pocketwatchx"), (float) player.getX());
                data.putFloat(getResource("pocketwatchy"), (float) player.getY());
                data.putFloat(getResource("pocketwatchz"), (float) player.getZ());
                data.putString(getResourceLocation("pocketwatchlevel"),player.level.dimension().location().toString());
                player.getCooldowns().addCooldown(MomotinkerItem.pocket_watch.get(),10);
            }else if (!player.isShiftKeyDown()&&data.getInt(pocketwatch)==0) {
                if (data.getInt(transmit)!=transmit_limit&&data.getInt(backtracking)!=backtracking_limit) {
                    data.putInt(pocketwatch, getcooltime(player,tool,600,180,12));
                }else if (data.getInt(transmit)==transmit_limit) {
                    data.putInt(pocketwatch, getcooltime(player,tool,200,60,4));
                }
                double x = data.getFloat(getResource("pocketwatchx"));
                double y = data.getFloat(getResource("pocketwatchy"));
                double z = data.getFloat(getResource("pocketwatchz"));
                tryTeleport(stack,player,x,y,z,data.getString(getResourceLocation("pocketwatchlevel")));
                if (tool.getPersistentData().getInt(transmit)<transmit_limit&&data.getInt(backtracking)!=backtracking_limit){
                    tool.getPersistentData().putInt(transmit,tool.getPersistentData().getInt(transmit)+1);
                }
            }
        }
    }
    private static boolean tryTeleport(ItemStack stack,LivingEntity living,double x,double y,double z,String string) {
        Level world = living.getCommandSenderWorld();
        ToolStack tool = ToolStack.from(stack);
        int transmit_limit = MomotinkerConfig.transmit_limit.get();
        if (world.isClientSide) {
            return false;
        }
        if (living instanceof ServerPlayer player&&!(living instanceof FakePlayer)) {
            ResourceKey<Level> level=ResourceKey.create(DIMENSION_REGISTRY,getResourceLocation(string));
            if (player.getServer()==null)return false;
            var server=player.getServer().getLevel(level);
            if (tool.getPersistentData().getInt(transmit)!=transmit_limit) {
                int a = (int) (tool.getStats().getInt(ToolStats.DURABILITY) * 0.25f);
                if (a > 1000) {
                    if (tool.getStats().getInt(ToolStats.DURABILITY) - tool.getDamage() < a) {
                        tool.setDamage(tool.getStats().getInt(ToolStats.DURABILITY));
                    } else if (tool.getStats().getInt(ToolStats.DURABILITY) - tool.getDamage() > a) {
                        tool.setDamage(tool.getDamage() + a);
                    }
                } else if (a < 1000) {
                    if (tool.getStats().getInt(ToolStats.DURABILITY) - tool.getDamage() < 1000) {
                        tool.setDamage(tool.getStats().getInt(ToolStats.DURABILITY));
                    } else if (tool.getStats().getInt(ToolStats.DURABILITY) - tool.getDamage() > 1000) {
                        tool.setDamage(tool.getDamage() + 1000);
                    }
                }
            }
            if (server != null) {
                player.teleportTo(server,x,y,z,0,0);
            }
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
        int backtracking_limit = MomotinkerConfig.backtracking_limit.get();
        int transmit_limit = MomotinkerConfig.transmit_limit.get();
        if (tool.hasTag(TinkerTags.Items.DURABILITY)) {
            builder.add(ToolStats.DURABILITY);
        }
        if (tool.hasTag(TinkerTags.Items.MELEE)) {
            builder.add(ToolStats.ATTACK_DAMAGE);
            builder.add(ToolStats.ATTACK_SPEED);
        }
        if (tool.getPersistentData().getInt(pocketwatch)!=0){
            builder.add(Component.translatable("item.momotinker.tooltip.pocket_watch1").append(tool.getPersistentData().getInt(pocketwatch)+"s"));
        }
        if (tool.getPersistentData().getInt(backtracking)<backtracking_limit) {
            int x = (int) tool.getPersistentData().getFloat(getResource("pocketwatchx"));
            int y = (int) tool.getPersistentData().getFloat(getResource("pocketwatchy"));
            int z = (int) tool.getPersistentData().getFloat(getResource("pocketwatchz"));
            builder.add(Component.translatable("item.momotinker.tooltip.pocket_watch2").append(x + ",").append(y + ",").append(z + ")"));
        }else
        if (tool.getPersistentData().getInt(backtracking)==backtracking_limit){
            int x1 = (int)tool.getPersistentData().getFloat(getResource("fx"));
            int y1 = (int)tool.getPersistentData().getFloat(getResource("fy"));
            int z1 = (int)tool.getPersistentData().getFloat(getResource("fz"));
            builder.add(Component.translatable("item.momotinker.tooltip.pocket_watch2").append(x1 + ",").append(y1 + ",").append(z1 + ")"));
        }
        if (tool.getPersistentData().getInt(backtracking)!=backtracking_limit) {
            builder.add(Component.translatable("item.momotinker.tooltip.pocket_watch3").append(getResourceLocation(tool.getPersistentData().getString(getResourceLocation("pocketwatchlevel"))) + ""));
        }else if (tool.getPersistentData().getInt(backtracking)==backtracking_limit){
            builder.add(Component.translatable("item.momotinker.tooltip.pocket_watch3").append(getResourceLocation(tool.getPersistentData().getString(getResourceLocation("flevel"))) + ""));
        }
        if (tool.getPersistentData().getInt(backtracking)<backtracking_limit&&tool.getPersistentData().getInt(transmit)<transmit_limit) {
            builder.add(Component.translatable("item.momotinker.tooltip.transmit").append(transmit_limit+"").append(Component.translatable("item.momotinker.tooltip.transmit1")).append(tool.getPersistentData().getInt(transmit)+"").withStyle(ChatFormatting.GOLD));
        }
        if (tool.getPersistentData().getInt(backtracking)==backtracking_limit) {
            builder.add(Component.translatable("item.momotinker.tooltip.backtracking").withStyle(ChatFormatting.DARK_PURPLE));
        }
        if (tool.getPersistentData().getInt(transmit)==transmit_limit) {
            builder.add(Component.translatable("item.momotinker.tooltip.transmit2").withStyle(ChatFormatting.GREEN));
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