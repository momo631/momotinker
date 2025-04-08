package com.momosensei.momotinker.event;

import com.momosensei.momotinker.mobs.CoolTimeA;
import com.momosensei.momotinker.mobs.CoolTimeB;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.CoolTimeChargeA;
import com.momosensei.momotinker.network.packet.CoolTimeChargeB;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import com.momosensei.momotinker.util.attackUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;

import static slimeknights.tconstruct.TConstruct.RANDOM;

@Mod.EventBusSubscriber(modid = "momotinker", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvent {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onMobDrop(LivingDropsEvent event) {
        LivingEntity killer = event.getEntity().getKillCredit();
        if (killer != null) {
            int a = ModifierUtil.getModifierLevel(killer.getMainHandItem(), MomotinkerModifiers.intendingplunder.getId());
            int b = ModifierUtil.getModifierLevel(killer.getMainHandItem(), MomotinkerModifiers.origin.getId());
            int b_random = RANDOM.nextInt(4 + b);
            for (var stack : event.getDrops()) {
                if (a > 0) {
                    stack.getItem().setCount((int) (stack.getItem().getCount() * (1 + a * 0.5)));
                }
                if (b > 0 && b_random >= 4) {
                    stack.getItem().setCount(0);
                }
            }
        }
    }

    @SubscribeEvent
    public static void Livingtickevent(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Player player){
            if (player.tickCount%60==0) {
                String[] array = new String[]{"msg.blank1", "msg.blank2", "msg.blank3", "msg.blank4", "msg.blank5", "msg.blank6", "msg.blank7"};
                if (CoolTimeB.getCoolTime() >= 579 && CoolTimeB.getCoolTime() <= 599) {
                    int i = (599 - CoolTimeB.getCoolTime()) / 3;
                    if (player.level.isClientSide()) {
                        player.sendSystemMessage(Component.translatable(array[i]));
                    }
                }
            }
            if (CoolTimeB.getCoolTime() == 578&&player.isAlive()){
                attackUtil.executeall(player.level, player.getX(), player.getY(), player.getZ(), player);
            }
            if (player instanceof ServerPlayer player1) {
                if (CoolTimeA.getCoolTime() < 0) {
                    Channel.sendToPlayer(new CoolTimeChargeA(0), player1);
                }
                if (CoolTimeB.getCoolTime() < 575) {
                    Channel.sendToPlayer(new CoolTimeChargeB(0), player1);
                }
                if (player.tickCount % 20 == 0) {
                    if (CoolTimeA.getCoolTime() > 0) {
                        Channel.sendToPlayer(new CoolTimeChargeA(CoolTimeA.getCoolTime() - 1), player1);
                    }
                    if (CoolTimeB.getCoolTime() > 0) {
                        Channel.sendToPlayer(new CoolTimeChargeB(CoolTimeB.getCoolTime() - 1), player1);
                    }
                }
            }
        }
    }
}
