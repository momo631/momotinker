package com.momosensei.momotinker.data;

import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.CoolTimeChargeB;
import com.momosensei.momotinker.register.MomotinkerItem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.List;

public final class SkillState {
    private SkillState() {}

    public static final class A {
        private static int cool_time;

        public static void setCoolTime(int f) {
            cool_time = f;
        }

        public static int getCoolTime() {
            return cool_time;
        }

        private A() {}
    }

    public static final class B {
        private static int cool_time;
        private static boolean owner;

        public static void setCoolTime(int f) {
            cool_time = f;
        }

        public static int getCoolTime() {
            return cool_time;
        }

        public static void setOwner(boolean f) {
            owner = f;
        }

        public static boolean getOwner() {
            return owner;
        }

        private B() {}
    }

    public static final class C {
        private static int cool_time;

        public static void setCoolTime(int f) {
            cool_time = f;
        }

        public static int getCoolTime() {
            return cool_time;
        }

        private C() {}
    }

    public static final class Signifidata {
        private static float signifidata;

        public static void setSignifidata(float f) {
            signifidata = f;
        }

        public static float getSignifidata() {
            return signifidata;
        }

        private Signifidata() {}
    }

    public static boolean tryActivateSkill(ServerPlayer player) {
        if (B.getCoolTime()!=0) {
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
