package com.momosensei.momotinker.util;

import net.minecraft.ChatFormatting;

public class C {
    public static int bc ;
    private static final ChatFormatting[] color ={
            ChatFormatting.DARK_BLUE,
            ChatFormatting.AQUA,
            ChatFormatting.BLUE,
            ChatFormatting.DARK_AQUA,
            ChatFormatting.DARK_GREEN,
            ChatFormatting.DARK_PURPLE,
            ChatFormatting.GOLD,
            ChatFormatting.DARK_RED,
            ChatFormatting.YELLOW,
            ChatFormatting.RED,
            ChatFormatting.GREEN,
            ChatFormatting.LIGHT_PURPLE
    };
    public static final ChatFormatting[] trinitycolor={
            ChatFormatting.WHITE,
            ChatFormatting.BLUE,
            ChatFormatting.LIGHT_PURPLE,
            ChatFormatting.AQUA
    };

    public static String formatting(String input, ChatFormatting[] colours, double delay) {
        StringBuilder builder = new StringBuilder(input.length() * 1000);
        if (delay <= 0.2D)
            delay = 0.9999D;
        int offset = (int)Math.floor((System.currentTimeMillis() & 0x3FFFL) / delay) % colours.length;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            builder.append(colours[(colours.length + i - offset) % colours.length].toString());
            builder.append(c);
            bc = i;
        }
        return builder.toString();
    }

    public static String GetColor(String input) {
        return formatting(input, color, 80.0D);
        //                  ^      ^     ^
        //               输入文本  颜色  延迟
    }
    public static String GetColorT(String input) {
        return formatting(input, trinitycolor, 80.0D);
    }

}

