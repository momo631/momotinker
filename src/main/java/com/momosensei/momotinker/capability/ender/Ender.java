package com.momosensei.momotinker.capability.ender;

import net.minecraft.nbt.CompoundTag;

public class Ender {

    private int ender;

    public Ender(){
        this.ender = 0;
    }

    public int getender() {
        return ender;
    }

    public void setender(int ender) {
        this.ender = ender;
    }

    public void increace(int i){
        ender += i;
    }

    public void increace(){
        this.increace(1);
    }

    public boolean decreace(int d){
        if (ender >= d){
            ender -= d;
            return true;
        }
        else return false;
    }

    public void decreace(){
        this.decreace(1);
    }

    public void saveNBTData(CompoundTag compoundTag){
        compoundTag.putInt("ender",ender);
    }

    public void loadNBTData(CompoundTag compoundTag){
        ender = compoundTag.getInt("ender");
    }

    public static float getPercentage() {
        return 0;
    }
}
