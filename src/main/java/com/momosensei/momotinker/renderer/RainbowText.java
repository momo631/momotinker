package com.momosensei.momotinker.renderer;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class RainbowText {
    private static final ChatFormatting[] colour;
    private static final ChatFormatting[] colour2;
    public RainbowText() {
    }
    public static String formatting(String input, ChatFormatting[] colours, double delay) {
        StringBuilder sb = new StringBuilder(input.length() * 3);
        if (delay <= 0.0) {
            delay = 0.001;
        }
        int offset = (int)Math.floor((double)(System.currentTimeMillis() & 16383L) / delay) % colours.length;
        for(int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);
            sb.append(colours[(colours.length + i - offset) % colours.length].toString());
            sb.append(c);
        }
        return sb.toString();
    }

    public static String makeColour(String input) {
        return formatting(input, colour, 80.0);
    }

    public static MutableComponent makeColourTest(String input) {
        // 1. 先获取翻译后的文本（支持语言环境）
        Component translatedText = Component.translatable(input);
        String plainText = translatedText.getString(); // 获取实际显示的文本

        // 2. 应用你的颜色格式化（假设 formatting 返回带颜色代码的字符串）
        String coloredText = formatting(plainText, colour, 80.0);

        // 3. 转换为 Component 并保留颜色（解析颜色代码）
        return Component.Serializer.fromJson(
                "{\"text\":\"" + coloredText + "\",\"color\":\"reset\"}"
        ).copy();
    }

    public static String makeColour2(String input) {
        return formatting(input, colour2, 200.0);
    }

    static {
        colour = new ChatFormatting[]{ChatFormatting.RED, ChatFormatting.GOLD, ChatFormatting.YELLOW, ChatFormatting.GREEN, ChatFormatting.AQUA, ChatFormatting.BLUE, ChatFormatting.LIGHT_PURPLE};

        colour2 = new ChatFormatting[]{ChatFormatting.DARK_GREEN, ChatFormatting.DARK_BLUE, ChatFormatting.DARK_PURPLE, ChatFormatting.RED, ChatFormatting.DARK_GRAY, ChatFormatting.GOLD, ChatFormatting.YELLOW, ChatFormatting.GRAY};

    }
}
