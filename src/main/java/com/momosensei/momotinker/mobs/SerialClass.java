package com.momosensei.momotinker.mobs;


import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SerialClass {
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public @interface SerialField {
        boolean toClient() default false;

        boolean toTracking() default false;
    }
}
