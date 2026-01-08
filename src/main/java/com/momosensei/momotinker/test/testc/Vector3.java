package com.momosensei.momotinker.test.testc;

import net.minecraft.util.Mth;

public class Vector3 {
    public float x, y, z;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 clone() {
        return new Vector3(x, y, z);
    }

    public void copy(Vector3 vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public Vector3 add(Vector3 vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
        return this;
    }

    public Vector3 subtract(Vector3 vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
        return this;
    }

    public Vector3 div(float amount) {
        this.x /= amount;
        this.y /= amount;
        this.z /= amount;
        return this;
    }

    public Vector3 mul(float amount) {
        this.x *= amount;
        this.y *= amount;
        this.z *= amount;
        return this;
    }

    public Vector3 normalize() {
        float f = Mth.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        if (f < 1.0E-4F) {
            this.x = 0;
            this.y = 0;
            this.z = 0;
        } else {
            this.x /= f;
            this.y /= f;
            this.z /= f;
        }
        return this;
    }

    public Vector3 rotateDegrees(float deg) {
        float ox = x;
        float oy = y;
        deg = (float) Math.toRadians(deg);
        x = Mth.cos(deg) * ox - Mth.sin(deg)*oy;
        y = Mth.sin(deg) * ox + Mth.cos(deg)*oy;
        return this;
    }
    public float magnitude() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }
    public Vector3 horizontal() {
        return new Vector3(this.x, 0, this.z);
    }
    public float dot(Vector3 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public Vector3 cross(Vector3 other) {
        return new Vector3(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x
        );
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3 lerp(Vector3 target, float t) {
        t = Math.max(0, Math.min(1, t));
        this.x = this.x + (target.x - this.x) * t;
        this.y = this.y + (target.y - this.y) * t;
        this.z = this.z + (target.z - this.z) * t;
        return this;
    }

    public float distance(Vector3 other) {
        float dx = this.x - other.x;
        float dy = this.y - other.y;
        float dz = this.z - other.z;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public float distanceSquared(Vector3 other) {
        float dx = this.x - other.x;
        float dy = this.y - other.y;
        float dz = this.z - other.z;
        return dx * dx + dy * dy + dz * dz;
    }
    public float horizontalLength() {
        return (float) Math.sqrt(x * x + z * z);
    }

    public Vector3 normalizeHorizontal() {
        float len = horizontalLength();
        if (len > 0.0001f) {
            this.x /= len;
            this.z /= len;
        } else {
            this.x = 1;
            this.z = 0;
        }
        return this;
    }
    public Vector3 rotateAround(Vector3 axis, float angleDegrees) {
        return rotateAroundRadians(axis, angleDegrees * (float) Math.PI / 180f);
    }

    public Vector3 rotateAroundRadians(Vector3 axis, float angleRadians) {
        Vector3 normalizedAxis = axis.clone().normalize();
        float cos = (float) Math.cos(angleRadians);
        float sin = (float) Math.sin(angleRadians);
        float oneMinusCos = 1 - cos;

        float ux = normalizedAxis.x;
        float uy = normalizedAxis.y;
        float uz = normalizedAxis.z;

        float x = this.x;
        float y = this.y;
        float z = this.z;

        float xx = ux * ux;
        float yy = uy * uy;
        float zz = uz * uz;
        float xy = ux * uy;
        float xz = ux * uz;
        float yz = uy * uz;

        this.x = (cos + xx * oneMinusCos) * x + (xy * oneMinusCos - uz * sin) * y + (xz * oneMinusCos + uy * sin) * z;
        this.y = (xy * oneMinusCos + uz * sin) * x + (cos + yy * oneMinusCos) * y + (yz * oneMinusCos - ux * sin) * z;
        this.z = (xz * oneMinusCos - uy * sin) * x + (yz * oneMinusCos + ux * sin) * y + (cos + zz * oneMinusCos) * z;

        return this;
    }
    public float horizontalAngle() {
        if (Math.abs(x) < 0.0001f && Math.abs(z) < 0.0001f) {
            return 0;
        }
        return (float) Math.atan2(z, x) * 180f / (float) Math.PI;
    }

    public static Vector3 fromHorizontalAngle(float angleDegrees, float length) {
        float rad = angleDegrees * (float) Math.PI / 180f;
        return new Vector3(
                (float) Math.cos(rad) * length,
                0,
                (float) Math.sin(rad) * length
        );
    }
    @Override
    public String toString() {
        return "Vector2 [x=" + x + ", y=" + y + "]";
    }

    public float sqrMagnitude() {
        return x*x+y*y+z*z;
    }
    public Vector3() {
        this(0, 0, 0);
    }
    public Vector3 set(Vector3 other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
        return this;
    }

    public Vector3 set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3 add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }
}
