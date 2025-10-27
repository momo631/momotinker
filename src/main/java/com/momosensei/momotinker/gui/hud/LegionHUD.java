package com.momosensei.momotinker.gui.hud;


import com.mojang.blaze3d.systems.RenderSystem;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.gui.hudhelder.LegionDrawTime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

import static com.momosensei.momotinker.register.MomotinkerTools.legion;

public class LegionHUD {
    public static ResourceLocation phase0 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/legion_time/0.png");
    public static ResourceLocation phase1 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/legion_time/5.png");
    public static ResourceLocation phase2 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/legion_time/4.png");
    public static ResourceLocation phase3 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/legion_time/3.png");
    public static ResourceLocation phase4 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/legion_time/2.png");
    public static ResourceLocation phase5 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/legion_time/1.png");
    public static List<ResourceLocation> phase = List.of(phase0,phase1,phase2,phase3,phase4,phase5);

    public static ResourceLocation progress0 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/legion_time/10.png");
    public static ResourceLocation progress1 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/legion_time/18.png");
    public static ResourceLocation progress2 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/legion_time/17.png");
    public static ResourceLocation progress3 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/legion_time/16.png");
    public static ResourceLocation progress4 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/legion_time/15.png");
    public static ResourceLocation progress5 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/legion_time/14.png");
    public static ResourceLocation progress6 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/legion_time/13.png");
    public static ResourceLocation progress7 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/legion_time/12.png");
    public static ResourceLocation progress8 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/legion_time/11.png");
    public static List<ResourceLocation> progress = List.of(progress0,progress1,progress2,progress3,progress4,progress5,progress6,progress7,progress8);

    public static ResourceLocation cooldown0 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/legion_time/20.png");
    public static ResourceLocation cooldown1 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/legion_time/28.png");
    public static ResourceLocation cooldown2 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/legion_time/27.png");
    public static ResourceLocation cooldown3 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/legion_time/26.png");
    public static ResourceLocation cooldown4 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/legion_time/25.png");
    public static ResourceLocation cooldown5 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/legion_time/24.png");
    public static ResourceLocation cooldown6 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/legion_time/23.png");
    public static ResourceLocation cooldown7 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/legion_time/22.png");
    public static ResourceLocation cooldown8 = new ResourceLocation(Momotinker.MOD_ID,"/textures/gui/overlay/legion_time/21.png");
    public static List<ResourceLocation> cooldown = List.of(cooldown0,cooldown1,cooldown2,cooldown3,cooldown4,cooldown5,cooldown6,cooldown7,cooldown8);

    public static IGuiOverlay Legion_Phase_HUD = ((gui, poseStack, partialTick, width, height) -> {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) {
            return;
        }
        if (!(player.getItemBySlot(EquipmentSlot.MAINHAND).is(legion.get()))) {
            return;
        }
        if (ToolStack.from(player.getMainHandItem()).isBroken()) {
            return;
        }
        float phase_perc = LegionDrawTime.getChargingPhasePercentage();

        int phase_amount = Mth.clamp((int) (phase_perc * 5), 0, 5);

        int x = width / 2;
        int y = height / 2;

        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, phase.get(phase_amount));
        poseStack.blit(phase.get(phase_amount),x-5, y-17, 0, 0, 32, 32, 32, 32);

    });

    public static IGuiOverlay Legion_Progress_HUD = ((gui, poseStack, partialTick, width, height) -> {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) {
            return;
        }
        if (!(player.getItemBySlot(EquipmentSlot.MAINHAND).is(legion.get()))) {
            return;
        }
        if (ToolStack.from(player.getMainHandItem()).isBroken()) {
            return;
        }

        float progress_perc = LegionDrawTime.getChargingProgressPercentage();

        int progress_amount = Mth.clamp((int) (progress_perc * 8), 0, 8);

        int x = width / 2;
        int y = height / 2;

        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, progress.get(progress_amount));
        poseStack.blit( progress.get(progress_amount), x-5, y - 17, 0, 0, 32, 32, 32, 32);

    });

    public static IGuiOverlay Legion_Cooldown_HUD = ((gui, poseStack, partialTick, width, height) -> {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) {
            return;
        }
        if (!(player.getItemBySlot(EquipmentSlot.MAINHAND).is(legion.get()))) {
            return;
        }
        if (ToolStack.from(player.getMainHandItem()).isBroken()) {
            return;
        }

        float cooldown_perc = LegionDrawTime.getCooldownPercentage();

        int cooldown_amount = Mth.clamp((int) (cooldown_perc * 8), 0, 8);

        int x = width / 2;
        int y = height / 2;

        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, cooldown.get(cooldown_amount));
        poseStack.blit( cooldown.get(cooldown_amount), x-29, y - 17, 0, 0, 32, 32, 32, 32);

    });
}
