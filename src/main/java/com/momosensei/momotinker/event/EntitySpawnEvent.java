package com.momosensei.momotinker.event;

import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class EntitySpawnEvent extends Event {
    public final Vec3 Pos;
    public EntitySpawnEvent(Vec3 Pos){
        this.Pos = Pos;
    }
    public Vec3 getPos(){
        return this.Pos;
    }
}
