package com.momosensei.momotinker.capability.healpercentage;

import net.minecraft.nbt.CompoundTag;

public class Ender {

    private int healpercentage;

    public Ender(){
        this.healpercentage = 0;
    }

    public int getHealpercentage() {
        return healpercentage;
    }

    public void setHealpercentage(int healpercentage) {
        this.healpercentage = healpercentage;
    }

    public void increace(int i){
        healpercentage += i;
    }

    public void increace(){
        this.increace(1);
    }

    public boolean decreace(int d){
        if (healpercentage >= d){
            healpercentage -= d;
            return true;
        }
        else return false;
    }

    public void decreace(){
        this.decreace(1);
    }

    public void saveNBTData(CompoundTag compoundTag){
        compoundTag.putInt("healpercentage",healpercentage);
    }

    public void loadNBTData(CompoundTag compoundTag){
        healpercentage = compoundTag.getInt("healpercentage");
    }

    public static float getPercentage() {
        return 0;
    }
}
