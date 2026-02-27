package com.momosensei.momotinker.gui.hud;


import com.mojang.blaze3d.systems.RenderSystem;
import com.momosensei.momotinker.Modifiers.modifiers.IncarnonModifiers.IncarnonModifier;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.gui.hudhelder.IncarnonDrawTime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

import static com.momosensei.momotinker.Modifiers.modifiers.IncarnonModifiers.IncarnonModifier.is_incarnon;
import static com.momosensei.momotinker.Modifiers.momomodifier.isToolStack;

public class IncarnonHUD {
    public static ResourceLocation Texture_off0 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/incarnon/incarnon_energy/incarnon_energy_0.png");
    public static ResourceLocation Texture_off1 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/incarnon/incarnon_energy/off/incarnon_energy_1.png");
    public static ResourceLocation Texture_off2 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/incarnon/incarnon_energy/off/incarnon_energy_2.png");
    public static ResourceLocation Texture_off3 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/incarnon/incarnon_energy/off/incarnon_energy_3.png");
    public static ResourceLocation Texture_off4 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/incarnon/incarnon_energy/off/incarnon_energy_4.png");
    public static ResourceLocation Texture_off5 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/incarnon/incarnon_energy/off/incarnon_energy_5.png");
    public static ResourceLocation Texture_off6 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/incarnon/incarnon_energy/off/incarnon_energy_6.png");
    public static ResourceLocation Texture_off7 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/incarnon/incarnon_energy/off/incarnon_energy_7.png");
    public static ResourceLocation Texture_off8 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/incarnon/incarnon_energy/off/incarnon_energy_8.png");
    public static ResourceLocation Texture_off9 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/incarnon/incarnon_energy/off/incarnon_energy_9.png");
    public static ResourceLocation Texture_off10 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/incarnon/incarnon_energy/off/incarnon_energy_10.png");
    public static List<ResourceLocation> Texture_off = List.of(Texture_off0,Texture_off1,Texture_off2,Texture_off3,Texture_off4,Texture_off5,Texture_off6,Texture_off7,Texture_off8,Texture_off9,Texture_off10);
   
    public static ResourceLocation Texture_on0 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/incarnon/incarnon_energy/incarnon_energy_0.png");
    public static ResourceLocation Texture_on1 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/incarnon/incarnon_energy/on/incarnon_energy_1.png");
    public static ResourceLocation Texture_on2 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/incarnon/incarnon_energy/on/incarnon_energy_2.png");
    public static ResourceLocation Texture_on3 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/incarnon/incarnon_energy/on/incarnon_energy_3.png");
    public static ResourceLocation Texture_on4 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/incarnon/incarnon_energy/on/incarnon_energy_4.png");
    public static ResourceLocation Texture_on5 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/incarnon/incarnon_energy/on/incarnon_energy_5.png");
    public static ResourceLocation Texture_on6 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/incarnon/incarnon_energy/on/incarnon_energy_6.png");
    public static ResourceLocation Texture_on7 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/incarnon/incarnon_energy/on/incarnon_energy_7.png");
    public static ResourceLocation Texture_on8 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/incarnon/incarnon_energy/on/incarnon_energy_8.png");
    public static ResourceLocation Texture_on9 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/incarnon/incarnon_energy/on/incarnon_energy_9.png");
    public static ResourceLocation Texture_on10 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/incarnon/incarnon_energy/on/incarnon_energy_10.png");
    public static List<ResourceLocation> Texture_on = List.of(Texture_on0,Texture_on1,Texture_on2,Texture_on3,Texture_on4,Texture_on5,Texture_on6,Texture_on7,Texture_on8,Texture_on9,Texture_on10);
    
    public static IGuiOverlay INCARNON_OFF_HUD = ((gui, poseStack, partialTick, width, height) -> {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) return;
        if (!(isToolStack(player.getMainHandItem()))) return;

        ToolStack tool=ToolStack.from(player.getMainHandItem());
        if (tool.isBroken()) return;
        boolean hasIncarnon = tool.getModifierList().stream().anyMatch(modifier -> modifier.getModifier() instanceof IncarnonModifier);
        if (!hasIncarnon) return;

        ModDataNBT data=tool.getPersistentData();
        if (data.getBoolean(is_incarnon))return;

        float perc = IncarnonDrawTime.getPercentage();
        int amount = Mth.clamp((int) (perc * 10), 0, 10);
        int x = width / 2;
        int y = height / 2;
        
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, Texture_off.get(amount));
        poseStack.blit(Texture_off.get(amount), x - 33, y - 22, 0, 0, 64, 64, 64, 64);
    });

    public static IGuiOverlay INCARNON_ON_HUD = ((gui, poseStack, partialTick, width, height) -> {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) return;
        if (!(isToolStack(player.getMainHandItem()))) return;

        ToolStack tool=ToolStack.from(player.getMainHandItem());
        if (tool.isBroken()) return;
        boolean hasIncarnon = tool.getModifierList().stream().anyMatch(modifier -> modifier.getModifier() instanceof IncarnonModifier);
        if (!hasIncarnon) return;
        ModDataNBT data=tool.getPersistentData();
        if (!data.getBoolean(is_incarnon))return;
        float perc = IncarnonDrawTime.getPercentage();
        int amount = Mth.clamp((int) (perc * 10), 0, 10);
        int x = width / 2;
        int y = height / 2;

        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, Texture_on.get(amount));
        poseStack.blit(Texture_on.get(amount), x - 33, y - 22, 0, 0, 64, 64, 64, 64);
    });
}
