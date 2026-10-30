package com.momosensei.momotinker.test.testa.util;

import org.joml.Quaternionf;

public class MathUtil {
    public static Quaternionf fromEuler(float yaw, float pitch, float roll){
       return new Quaternionf().rotateZ(roll).rotateX(pitch).rotateY(yaw);
    }

    public static final Quaternionf Quat_One = new Quaternionf();

    public static final float DEGREES_TO_RADIANS = (float) (Math.PI / 180);
    public static float toDegrees(float angrad){
        return angrad * DEGREES_TO_RADIANS;
    }

}
