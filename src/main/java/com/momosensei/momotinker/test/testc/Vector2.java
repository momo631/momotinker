package com.momosensei.momotinker.test.testc;

import net.minecraft.util.Mth;

public class Vector2 {
    public float x, y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 clone() {
        return new Vector2(x, y);
    }

    public void copy(Vector2 vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    public Vector2 add(Vector2 vec) {
        this.x += vec.x;
        this.y += vec.y;
        return this;
    }

    public Vector2 subtract(Vector2 vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        return this;
    }

    public Vector2 div(float amount) {
        this.x /= amount;
        this.y /= amount;
        return this;
    }

    public Vector2 mul(float amount) {
        this.x *= amount;
        this.y *= amount;
        return this;
    }

    public Vector2 normalize() {
        float f = Mth.sqrt(this.x * this.x + this.y * this.y);
        if (f < 1.0E-4F) {
            this.x = 0;
            this.y = 0;
        } else {
            this.x /= f;
            this.y /= f;
        }
        return this;
    }

    public Vector2 rotateDegrees(float deg) {
        float ox = x;
        float oy = y;
        deg = (float) Math.toRadians(deg);
        x = Mth.cos(deg) * ox - Mth.sin(deg)*oy;
        y = Mth.sin(deg) * ox + Mth.cos(deg)*oy;
        return this;
    }

    @Override
    public String toString() {
        return "Vector2 [x=" + x + ", y=" + y + "]";
    }

}
