package com.momosensei.momotinker.test.testa;

import org.joml.Quaternionf;

public class MathUtils {
    public static Quaternionf fromEuler(float yaw, float pitch, float roll){
       return new Quaternionf().rotateZ(roll).rotateX(pitch).rotateY(yaw);
    }

    public static final Quaternionf Quat_One = new Quaternionf();

    public static final float RADIANS_TO_DEGREES = (float) (Math.PI / 180);
    public static float toDegrees(float angrad){
        return angrad * RADIANS_TO_DEGREES;
    }

}
