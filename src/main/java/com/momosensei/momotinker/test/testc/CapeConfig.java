package com.momosensei.momotinker.test.testc;

public class CapeConfig {
    // 可调配常量
    public static final float GRAVITY = 25;           // 重力强度
    public static final float MAX_BEND = 20;          // 最大弯曲角度
    public static final float CLIPPING_THRESHOLD = 0.99f; // 自相交检测阈值
    public static final int MAX_ITERATIONS = 30;       // 模拟迭代次数
    public static final int MAX_SELF_CLIP_RUNS = 32;   // 自相交检测最大循环次数
    public static final float FIXED_DELTA_TIME = 50f / 1000f; // 固定时间步长
    public static final float MAX_Z_MULTIPLIER = 10;   // Z轴最大偏移乘数
    public static final float HEIGHT_MULTIPLIER = 6; // 高度乘数
    public static final float STRAFE_MULTIPLIER = 2; // 横向移动乘数

    public static float WIDTH_MULTIPLIER = 0.5f;  // 宽度倍率
    public static float LENGTH_MULTIPLIER = 2f; // 长度倍率
}
