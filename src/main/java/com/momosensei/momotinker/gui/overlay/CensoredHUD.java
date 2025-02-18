package com.momosensei.momotinker.gui.overlay;


import com.mojang.blaze3d.systems.RenderSystem;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.network.packet.servertoplay.Censoreddatatime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.List;

import static slimeknights.tconstruct.TConstruct.RANDOM;

public class CensoredHUD {
    public CensoredHUD(){}
    public static ResourceLocation Texture0 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/gui_censored_0.png");
    public static ResourceLocation Texture1 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/gui_censored_1.png");
    public static ResourceLocation Texture2 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/gui_censored_2.png");
    public static ResourceLocation Texture3 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/gui_censored_3.png");
    public static ResourceLocation Texture4 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/gui_censored_4.png");
    public static ResourceLocation Texture5 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/gui_censored_5.png");
    public static ResourceLocation Texture6 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/gui_censored_6.png");
    public static ResourceLocation Texture7 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/gui_censored_7.png");
    public static ResourceLocation Texture8 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/gui_censored_8.png");
    public static List<ResourceLocation> Texture = List.of(Texture0,Texture1,Texture2,Texture3,Texture4,Texture5,Texture6,Texture7,Texture8);
    public static IGuiOverlay CENSORED = ((gui, poseStack, partialTick, width, height) -> {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        float b = Censoreddatatime.getCensoreddata();
        if (b==0){
            return;
        }
        if (player == null) {
            return;
        }
        if (b>0&&player.tickCount%20==0){
            Censoreddatatime.setCensoreddata(b-1);
        }
        int i = RANDOM.nextInt(9);
        int amount = Mth.clamp(i, 0, 8);
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, Texture.get(amount));
        GuiComponent.blit(poseStack, 0, 0, 0, 0, width, height, width, height);
    });
}
