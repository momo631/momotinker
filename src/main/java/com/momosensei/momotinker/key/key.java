package com.momosensei.momotinker.key;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;


public class key {
    public key(){
    }
    public static class KeyBinding {
        public static final String KEY_CATEGORY_EXAMPLE_MOD = "特殊技能";
        public static final String KEY_KEY = "墨工坊";

        public static final KeyMapping KEY = new KeyMapping(KEY_CATEGORY_EXAMPLE_MOD, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_X,KEY_KEY);

    }
}
