package com.momosensei.momotinker.key;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;


public class key {
    public key(){
    }
    public static class KeyBinding {
        public static final String KEY_CATEGORY_EXAMPLE_MOD = "Category Example";
        public static final String KEY_TOOL_ENHANCEMENT_MOD = "Tool Enhancement";
        public static final String KEY_KEY = "momotinker";

        public static final KeyMapping KEY = new KeyMapping(KEY_CATEGORY_EXAMPLE_MOD, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_X,KEY_KEY);
        public static final KeyMapping KEYA = new KeyMapping(KEY_TOOL_ENHANCEMENT_MOD, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z,KEY_KEY);

    }
}
