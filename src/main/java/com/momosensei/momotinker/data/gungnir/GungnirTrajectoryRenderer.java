package com.momosensei.momotinker.data.gungnir;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public final class GungnirTrajectoryRenderer {
    public static final ResourceLocation BEAM_TEXTURE =
            new ResourceLocation("textures/entity/beacon_beam.png");

    private GungnirTrajectoryRenderer() {}

    /**
     * 在"原点=起点、+Z=终点方向"的局部空间画一根发光柱。
     * @param dir    起点→终点 的向量
     * @param radius 当前半宽（格）
     * @param alpha  0..255
     */
    public static void renderBeam(Vec3 dir, float radius, int alpha,
                                  long gameTime, float partialTick,
                                  PoseStack pose, MultiBufferSource provider) {
        float lengthXY = Mth.sqrt((float) (dir.x * dir.x + dir.z * dir.z));
        float length = Mth.sqrt((float) (dir.x * dir.x + dir.y * dir.y + dir.z * dir.z));
        if (length < 1e-4F) return;

        pose.pushPose();

        pose.mulPose(Axis.YP.rotation((float) (-Math.atan2(dir.z, dir.x)) - 1.5707964F));
        pose.mulPose(Axis.XP.rotation((float) (-Math.atan2(lengthXY, dir.y)) - 1.5707964F));

        VertexConsumer vc = provider.getBuffer(RenderType.beaconBeam(BEAM_TEXTURE, true));

        float time = gameTime + partialTick;
        float v0 = -time * 0.2F - Mth.floor(-time * 0.2F);
        float v1 = v0 + length / 8F;

        PoseStack.Pose last = pose.last();
        Matrix4f m = last.pose();
        Matrix3f n = last.normal();

        int r = 255, g = 228, b = 77;

        for (int i = 0; i < 4; i++) {
            float a = (float) (i * Math.PI / 2);
            float cx = Mth.cos(a) * radius;
            float cy = Mth.sin(a) * radius;
            float tx = -Mth.sin(a) * radius;
            float ty =  Mth.cos(a) * radius;

            float x0 = cx - tx, y0 = cy - ty;
            float x1 = cx + tx, y1 = cy + ty;

            vc.vertex(m, x0, y0, 0F)     .color(r, g, b, alpha / 2).uv(0F, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xF000F0).normal(n, 0, -1, 0).endVertex();
            vc.vertex(m, x1, y1, 0F)     .color(r, g, b, alpha / 2).uv(1F, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xF000F0).normal(n, 0, -1, 0).endVertex();
            vc.vertex(m, x1, y1, length) .color(r, g, b, alpha)     .uv(1F, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xF000F0).normal(n, 0, -1, 0).endVertex();
            vc.vertex(m, x0, y0, length) .color(r, g, b, alpha)     .uv(0F, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xF000F0).normal(n, 0, -1, 0).endVertex();
        }

        pose.popPose();
    }
}