package com.momosensei.momotinker.mobs;

import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.CoolTimeChargeB;
import com.momosensei.momotinker.register.MomotinkerItem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.List;

public class CoolTimeB {
    public static int cooltime;
    public static boolean owner;
    public static void setCoolTime(int f){
        cooltime  =f;
    }
    public static int getCoolTime(){
        return cooltime;
    }

    public static void setOwner(boolean f){
        owner  =f;
    }
    public static boolean getOwner(){
        return owner;
    }

    public static boolean tryActivateSkill(ServerPlayer player) {
        if (CoolTimeB.getCoolTime()!=0) {
           return false;
        }
        if (player.getItemBySlot(EquipmentSlot.OFFHAND).is(MomotinkerItem.nihilism.get())){
            sendDifferentCoolTimeToPlayers(player,600);
            player.getItemBySlot(EquipmentSlot.OFFHAND).setCount(player.getItemBySlot(EquipmentSlot.OFFHAND).getCount() - 1);
        }
        return true;
    }
    public static void sendDifferentCoolTimeToPlayers(ServerPlayer specialPlayer, int coolTime) {
        MinecraftServer server = specialPlayer.getServer();
        if (server != null) {
            List<ServerPlayer> allPlayers = server.getPlayerList().getPlayers();
            for (ServerPlayer eachPlayer : allPlayers) {
                if (eachPlayer.equals(specialPlayer)) {
                    Channel.sendToPlayer(new CoolTimeChargeB(coolTime, true), eachPlayer);
                } else {
                    Channel.sendToPlayer(new CoolTimeChargeB(coolTime, false), eachPlayer);
                }
            }
        }
    }
}
