package com.momosensei.momotinker.gui.overlay;


import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.momosensei.momotinker.mobs.Signifidatatime;
import com.momosensei.momotinker.register.MomotinkerEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public class MomotinkerOverlay {


    public MomotinkerOverlay() {
    }

    public final Minecraft minecraft = Minecraft.getInstance();

    public static final ResourceLocation ENDER_SHADER = new ResourceLocation("momotinker", "/shaders/post/ender/ender.json");
    public static final ResourceLocation ENDER_TEXTURE = new ResourceLocation("momotinker", "/textures/gui/overlay/end.png");

    public static final ResourceLocation SIGNIFI_SHADER = new ResourceLocation("momotinker", "/shaders/post/signifi/signifi.json");

    public static void render(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        Player player = Minecraft.getInstance().player;
        float b = Signifidatatime.getSignifidata();
        if (player == null){
            Minecraft.getInstance().tell(() -> Minecraft.getInstance().gameRenderer.shutdownEffect());
        }else
        if (player.getEffect(MomotinkerEffects.End.get())!=null && player.hasEffect(MomotinkerEffects.End.get())) {
            Minecraft.getInstance().tell(() -> Minecraft.getInstance().gameRenderer.loadEffect(ENDER_SHADER));
            gui.setupOverlayRenderState(true, false, ENDER_TEXTURE);
            RenderSystem.enableTexture();
            renderOverlay(poseStack);
        }else
        if (b==0){
            Minecraft.getInstance().tell(() -> Minecraft.getInstance().gameRenderer.shutdownEffect());
        }else
        if (b>0){
            Signifidatatime.setSignifidata(b-1);
            Minecraft.getInstance().tell(() -> Minecraft.getInstance().gameRenderer.loadEffect(SIGNIFI_SHADER));
            gui.setupOverlayRenderState(true, false, ENDER_TEXTURE);
            RenderSystem.enableTexture();
            renderOverlay(poseStack);
        }
        if (b<0){
            Signifidatatime.setSignifidata(0);
        }
    }

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