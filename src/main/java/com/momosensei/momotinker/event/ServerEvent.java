package com.momosensei.momotinker.event;

import com.momosensei.momotinker.entity.BurningEntity;
import com.momosensei.momotinker.entity.CleanseEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;

public class ServerEvent {
    public ServerEvent(){
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerTick);
    }

    public void onPlayerTick(TickEvent.PlayerTickEvent event){
        if(event.player.level() instanceof ServerLevel level &&level.getGameTime()%20==0) {
            Player player = event.player;
            if (Math.abs(player.getX()) + Math.abs(player.getY()) > 750) {
                Vec2 pos = new Vec2((float) (player.getX()), (float) (player.getZ()));
                CleanseSpawnEvent event1 = new CleanseSpawnEvent(new Vec3(pos.x, player.getY() + 100, pos.y));
                MinecraftForge.EVENT_BUS.post(event1);
                if (!event1.isCanceled()) {
                    double x =player.getLookAngle().x;
                    double z =player.getLookAngle().z;
                    CleanseEntity entity = new CleanseEntity(level, pos.x, player.getY() + 100, pos.y, new Vec3(x*0.2,0,z*0.2));
                    entity.noPhysics = true;
                    entity.setExplosionPower((byte)120);
                    level.addFreshEntity(entity);
                }
            }
        }

        if(event.player.level() instanceof ServerLevel level &&level.getGameTime()%20==0) {
            Player player = event.player;
            if (Math.abs(player.getX()) + Math.abs(player.getY()) > 750) {
                Vec2 pos = new Vec2((float) (player.getX()), (float) (player.getZ()));
                CleanseSpawnEvent event1 = new CleanseSpawnEvent(new Vec3(pos.x, player.getY() + 100, pos.y));
                MinecraftForge.EVENT_BUS.post(event1);
                if (!event1.isCanceled()) {
                    BurningEntity entity = new BurningEntity(level, pos.x, player.getY() + 100, pos.y, player.getLookAngle().scale(1));
                    entity.noPhysics = true;
                    entity.setExplosionPower((byte)120);
                    level.addFreshEntity(entity);
                }
            }
        }
    }
}
