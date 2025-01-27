package com.momosensei.momotinker.gui.overlay;


import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.momosensei.momotinker.register.MomotinkerEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class PlayerHealPercentageOverlay {


    public PlayerHealPercentageOverlay() {
    }

    public final Minecraft minecraft = Minecraft.getInstance();
    public static final ResourceLocation PLAYER_HEAL_PERCENTAGE_SHADER = new ResourceLocation("momotinker", "/shaders/post/playhealpercentage/playhealpercentage.json");
    public static final ResourceLocation PLAYER_HEAL_PERCENTAGE_TEXTURE = new ResourceLocation("momotinker", "/textures/gui/overlay/end.png");

    public static IGuiOverlay PLAYER_HEAL_PERCENTAGE = ((gui, poseStack, partialTick, width, height) -> {
        Player player = Minecraft.getInstance().player;
        if (player != null && player.getEffect(MomotinkerEffects.End.get())!=null && player.hasEffect(MomotinkerEffects.End.get())) {
            Minecraft.getInstance().tell(() -> Minecraft.getInstance().gameRenderer.loadEffect(PLAYER_HEAL_PERCENTAGE_SHADER));
        }
        else
        {
            if (player != null && player.getEffect(MomotinkerEffects.End.get())!=null && !(player.hasEffect(MomotinkerEffects.End.get()))) {
                Minecraft.getInstance().tell(() -> Minecraft.getInstance().gameRenderer.shutdownEffect());
            }
        }

        gui.setupOverlayRenderState(true, false, PLAYER_HEAL_PERCENTAGE_TEXTURE);
        RenderSystem.enableTexture();
        renderOverlay(poseStack);

    });

    public static void renderOverlay(PoseStack pose) {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
        Window window = Minecraft.getInstance().getWindow();
        pose.pushPose();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex((double)0.0F, (double)window.getGuiScaledHeight(), (double)-90.0F).uv(0.0F, 1.0F).endVertex();
        buffer.vertex((double)window.getGuiScaledWidth(), (double)window.getGuiScaledHeight(), (double)-90.0F).uv(1.0F, 1.0F).endVertex();
        buffer.vertex((double)window.getGuiScaledWidth(), (double)0.0F, (double)-90.0F).uv(1.0F, 0.0F).endVertex();
        buffer.vertex((double)0.0F, (double)0.0F, (double)-90.0F).uv(0.0F, 0.0F).endVertex();
        tessellator.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        pose.popPose();
        RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }
}