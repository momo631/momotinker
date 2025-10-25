package com.momosensei.momotinker.mobs;

import java.util.HashMap;
import java.util.UUID;

public class StageMeteor {
    public static float stagefloat = 0f;

    private static final HashMap<UUID, Float> playerStageFloats = new HashMap<>();

    public static void setStageFloat(float f) {
        stagefloat = f;
    }
    public static float getStageFloat() {
        return stagefloat;
    }

    public static void setStageFloat(UUID playerId, float f) {
        playerStageFloats.put(playerId, f);
    }

    public static float getStageFloat(UUID playerId) {
        return playerStageFloats.getOrDefault(playerId, stagefloat);
    }
}
