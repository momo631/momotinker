package com.momosensei.momotinker.event;

import com.momosensei.momotinker.mobs.CoolTimeA;
import com.momosensei.momotinker.mobs.CoolTimeB;
import com.momosensei.momotinker.mobs.CoolTimeC;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.CoolTimeChargeA;
import com.momosensei.momotinker.network.packet.CoolTimeChargeB;
import com.momosensei.momotinker.network.packet.CoolTimeChargeC;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import com.momosensei.momotinker.util.AttackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Random;

import static com.momosensei.momotinker.Modifiers.modifiers.GainsAlone.gainsalonepoints;
import static com.momosensei.momotinker.Modifiers.momomodifier.getRemainingDurability;

@Mod.EventBusSubscriber(modid = "momotinker", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvent {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDrop(LivingDropsEvent event) {
        LivingEntity killer = event.getEntity().getKillCredit();
        if (killer != null) {
            int a = ModifierUtil.getModifierLevel(killer.getMainHandItem(), MomotinkerModifiers.intendingplunder.getId());
            int b = ModifierUtil.getModifierLevel(killer.getMainHandItem(), MomotinkerModifiers.origin.getId());
            int c = ModifierUtil.getModifierLevel(killer.getMainHandItem(), MomotinkerModifiers.gainsalone.getId());
            Random random = new Random();
            for (var stack : event.getDrops()) {
                if (a > 0) {
                    stack.getItem().setCount(stack.getItem().getCount() * (1 + a));
                }
                if (b > 0 && random.nextInt(16) <4+b) {
                    stack.getItem().setCount(0);
                }
                if (c > 0) {
                    ToolStack tool = ToolStack.from(killer.getMainHandItem());
                    int c1 = tool.getPersistentData().getInt(gainsalonepoints);
                    double c2 = Math.pow(2,c1+1);
                    if (!tool.isBroken()&&getRemainingDurability(tool)>1) {
                        stack.getItem().setCount((int) (stack.getItem().getCount() * Math.floor(c2)));
                    }
                }
            }
        }

    }

    /*
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockDrop(BlockEvent.BreakEvent event) {
        Block block= event.getState().getBlock();
        ItemStack item = new ItemStack(block);
        Player player=event.getPlayer();
        ToolStack tool = ToolStack.from(player.getMainHandItem());
        int c = ModifierUtil.getModifierLevel(player.getMainHandItem(), MomotinkerModifiers.gainsalone.getId());
        if (player.level instanceof ServerLevel && c > 0 && event.getState() != null && !event.getState().isAir()&&!tool.isBroken()){
            if (!block.canHarvestBlock(event.getState(),player.level,event.getPos(),player))return;
            int c1 = tool.getPersistentData().getInt(gainsalonepoints);
            int a = (int) Math.floor(Math.pow(2,c1+1));
            for (int i = 0; i < a; ++i) {
                if (item.isEmpty()) return;
                ModifierUtil.dropItem(player, item);
            }
        }
    }
*/
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            Channel.sendToPlayer(new CoolTimeChargeB(CoolTimeB.getCoolTime(),false), serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Player player) {
            int coolTimeB = CoolTimeB.getCoolTime();
            Level level = player.level;
            int tickCount = player.tickCount;
            handleNegativeCoolTimes(player);
            if (CoolTimeB.getOwner()) {
                if (level instanceof ServerLevel serverLevel) {
                    if (coolTimeB >= 579 && coolTimeB <= 599) {
                        if (tickCount % 10 == 0) {
                            spawnParticleRing(serverLevel, player);
                        }
                    }
                } else if (tickCount % 60 == 0) {
                    sendCoolTimeMessage(player, coolTimeB);
                }
            }

            if (tickCount % 20 == 0 && player instanceof ServerPlayer serverPlayer) {
                handleCoolTimeDecrement(serverPlayer);
                if (coolTimeB == 578 && player.isAlive()&&CoolTimeB.getOwner()) {
                    AttackUtil.executeall(player.level, player.getX(), player.getY(), player.getZ(), player);
                }
            }
        }
    }

    private static void spawnParticleRing(ServerLevel serverLevel, Player player) {
        final double radius = 0.5D;
        final double height = player.getBbHeight();
        final int particles = 360;

        for (int i = 0; i < particles; i++) {
            double angle = i * (2 * Math.PI / particles);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);

            serverLevel.sendParticles(ParticleTypes.ASH,
                    player.getX() + x,
                    player.getY() + height,
                    player.getZ() + z,
                    1/2, 0, 0.5, 0, 0.75);
        }
    }

    private static void sendCoolTimeMessage(Player player, int coolTimeB) {
        String[] messages = new String[]{
                "msg.blank1", "msg.blank2", "msg.blank3", "msg.blank4",
                "msg.blank5", "msg.blank6", "msg.blank7"
        };

        int index = (599 - coolTimeB) / 3;
        if (index >= 0 && index < messages.length) {
            player.sendSystemMessage(
                    Component.translatable(messages[index]).withStyle(ChatFormatting.GRAY)
            );
        }
    }

    private static void handleNegativeCoolTimes(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            checkAndSyncNegativeCoolTime(serverPlayer, CoolTimeA.getCoolTime(), CoolTimeChargeA.class);
            checkAndSyncNegativeCoolTime(serverPlayer, CoolTimeB.getCoolTime(), CoolTimeChargeB.class);
            checkAndSyncNegativeCoolTime(serverPlayer, CoolTimeC.getCoolTime(), CoolTimeChargeC.class);
        }
    }

    private static void handleCoolTimeDecrement(ServerPlayer player) {
        checkAndSyncPositiveCoolTime(player, CoolTimeA.getCoolTime(), CoolTimeChargeA.class);
        checkAndSyncPositiveCoolTime(player, CoolTimeB.getCoolTime(), CoolTimeChargeB.class);
        checkAndSyncPositiveCoolTime(player, CoolTimeC.getCoolTime(), CoolTimeChargeC.class);
    }

    private static <T> void checkAndSyncNegativeCoolTime(ServerPlayer player, int coolTime, Class<T> packetClass) {
        if (coolTime < 0) {
            sendCoolTimePacket(player, 0, packetClass);
        }
    }

    private static <T> void checkAndSyncPositiveCoolTime(ServerPlayer player, int coolTime, Class<T> packetClass) {
        if (coolTime > 0) {
            sendCoolTimePacket(player, coolTime - 1, packetClass);
        }
    }

    private static <T> void sendCoolTimePacket(ServerPlayer player, int coolTime, Class<T> packetClass) {
        if (packetClass == CoolTimeChargeA.class) {
            Channel.sendToPlayer(new CoolTimeChargeA(coolTime), player);
        } else if (packetClass == CoolTimeChargeB.class) {
            Channel.sendToPlayer(new CoolTimeChargeB(coolTime,CoolTimeB.getOwner()), player);
        } else if (packetClass == CoolTimeChargeC.class) {
            Channel.sendToPlayer(new CoolTimeChargeC(coolTime), player);
        }
    }
}
