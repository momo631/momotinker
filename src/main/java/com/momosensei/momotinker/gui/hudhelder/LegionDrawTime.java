package com.momosensei.momotinker.gui.hudhelder;

public class LegionDrawTime {
    public static float charging_phase;
    public static float charging_progress;
    public static float cooldown;

    public static void setChargingPhasePercentage(float f){
        charging_phase =f;
    }
    public static float getChargingPhasePercentage(){
        return charging_phase;
    }

    public static void setChargingProgressPercentage(float f){
        charging_progress =f;
    }
    public static float getChargingProgressPercentage(){
        return charging_progress;
    }

    public static void setCooldownPercentage(float f){
        cooldown =f;
    }
    public static float getCooldownPercentage(){
        return cooldown;
    }
}
