package com.momosensei.momotinker.event;

import com.momosensei.momotinker.mobs.CoolTime;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.CoolTimeCharge;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
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
            int b_random = RANDOM.nextInt(4+b);
            for (var stack : event.getDrops()) {
                if (a>0) {
                    stack.getItem().setCount((int) (stack.getItem().getCount() * (1 + a * 0.5)));
                }
                if (b>0&&b_random>=4){
                    stack.getItem().setCount(0);
                }
            }
        }
    }

    @SubscribeEvent
    public static void playertickevent(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayer player&&player.tickCount%20==0) {
            if (CoolTime.getCoolTime()>0){
                Channel.sendToPlayer(new CoolTimeCharge(CoolTime.getCoolTime()-1),player);
            }
            if (CoolTime.getCoolTime()<0){
                Channel.sendToPlayer(new CoolTimeCharge(0),player);
            }
        }
    }
}
