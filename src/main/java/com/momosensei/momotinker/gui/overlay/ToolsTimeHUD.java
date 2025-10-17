package com.momosensei.momotinker.gui.overlay;


import com.mojang.blaze3d.systems.RenderSystem;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.mobs.ToolsTimeDrawtime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

import static com.momosensei.momotinker.register.MomotinkerTools.*;

public class ToolsTimeHUD {
    public static ResourceLocation Texture0 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/gui_trigger_blade_0.png");
    public static ResourceLocation Texture1 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/gui_trigger_blade_1.png");
    public static ResourceLocation Texture2 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/gui_trigger_blade_2.png");
    public static ResourceLocation Texture3 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/gui_trigger_blade_3.png");
    public static ResourceLocation Texture4 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/gui_trigger_blade_4.png");
    public static ResourceLocation Texture5 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/gui_trigger_blade_5.png");
    public static ResourceLocation Texture6 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/gui_trigger_blade_6.png");
    public static ResourceLocation Texture7 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/gui_trigger_blade_7.png");
    public static ResourceLocation Texture8 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/gui_trigger_blade_8.png");
    public static List<ResourceLocation> Texture = List.of(Texture0,Texture1,Texture2,Texture3,Texture4,Texture5,Texture6,Texture7,Texture8);
    public static IGuiOverlay TOOS_TIME_HUD = ((gui, poseStack, partialTick, width, height) -> {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null){
            return;
        }
        if (!(player.getItemBySlot(EquipmentSlot.MAINHAND).is(trigger_blade.get())
                ||player.getItemBySlot(EquipmentSlot.MAINHAND).is(divine_punishment_spear.get())
                ||player.getItemBySlot(EquipmentSlot.MAINHAND).is(entropy_burning_sword.get())
                ||player.getItemBySlot(EquipmentSlot.MAINHAND).is(entropy_burning_riding_spear.get())
                ||player.getItemBySlot(EquipmentSlot.MAINHAND).is(entropy_burning_cannon.get())
                ||player.getItemBySlot(EquipmentSlot.MAINHAND).is(coronal_key.get())
                ||player.getItemBySlot(EquipmentSlot.MAINHAND).is(eclipse_container.get())
                ||player.getItemBySlot(EquipmentSlot.MAINHAND).is(pneumatic_sword.get())
                ||player.getItemBySlot(EquipmentSlot.OFFHAND).is(pneumatic_sword.get())
                ||player.getItemBySlot(EquipmentSlot.MAINHAND).is(box.get()))){
            return;
        }
        if (ToolStack.from(player.getMainHandItem()).isBroken()){
            return;
        }
        float perc = ToolsTimeDrawtime.getPercentage();
        int amount = Mth.clamp((int) (perc*8),0,8);
        int x =width/2;
        int y = height/2;

            RenderSystem.setShader(GameRenderer::getPositionShader);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.setShaderTexture(0, Texture.get(amount));
            poseStack.blit(Texture.get(amount), x - 9, y - 8, 0, 0, 17, 17, 17, 17);

    });
}
