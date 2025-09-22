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
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
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
    public static void Livingtickevent(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Player player){
            String[] array = new String[]{"msg.blank1", "msg.blank2", "msg.blank3", "msg.blank4", "msg.blank5", "msg.blank6", "msg.blank7"};
            if (CoolTimeB.getCoolTime() >= 579 && CoolTimeB.getCoolTime() <= 599) {
                if (player.level instanceof ServerLevel serverLevel&&player.tickCount%10==0){
                    for (int i = 0; i <= 360; i++) {
                        double rad = i * 0.017453292519943295;
                        double r = 0.5D;
                        double x = r * Math.cos(rad);
                        double z = r * Math.sin(rad);
                        serverLevel.sendParticles(ParticleTypes.ASH, player.getX()+x, player.getY()+player.getBbHeight(), player.getZ()+z, 1/2, 0, 0.5, 0, 0.5);
                    }
                }
                int i = (599 - CoolTimeB.getCoolTime()) / 3;
                if (player.level.isClientSide()&&player.tickCount%60==0) {
                    player.sendSystemMessage(Component.translatable(array[i]).withStyle(ChatFormatting.GRAY));
                }
            }
            if (CoolTimeB.getCoolTime() == 578&&player.isAlive()){
                AttackUtil.executeall(player.level, player.getX(), player.getY(), player.getZ(), player);
            }
            if (player instanceof ServerPlayer player1) {
                if (CoolTimeA.getCoolTime() < 0) {
                    Channel.sendToPlayer(new CoolTimeChargeA(0), player1);
                }
                if (CoolTimeB.getCoolTime() < 0) {
                    Channel.sendToPlayer(new CoolTimeChargeB(0), player1);
                }
                if (CoolTimeC.getCoolTime() < 0) {
                    Channel.sendToPlayer(new CoolTimeChargeC(0), player1);
                }
                if (player.tickCount % 20 == 0) {
                    if (CoolTimeA.getCoolTime() > 0) {
                        Channel.sendToPlayer(new CoolTimeChargeA(CoolTimeA.getCoolTime() - 1), player1);
                    }
                    if (CoolTimeB.getCoolTime() > 0) {
                        Channel.sendToPlayer(new CoolTimeChargeB(CoolTimeB.getCoolTime() - 1), player1);
                    }
                    if (CoolTimeC.getCoolTime() > 0) {
                        Channel.sendToPlayer(new CoolTimeChargeC(CoolTimeC.getCoolTime() - 1), player1);
                    }
                }
            }
        }
    }
}
