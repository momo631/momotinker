package com.momosensei.momotinker.test.testc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Matrix4f;

import java.util.List;

import static com.momosensei.momotinker.test.testc.CapeConfig.LENGTH_MULTIPLIER;
import static com.momosensei.momotinker.test.testc.CapeConfig.WIDTH_MULTIPLIER;

public class CustomCapeRenderLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private static final int PART_COUNT = 48;           // 披风分段数量
    private static final float DEFAULT_WIDTH = 1F;       // 默认披风宽度
    private static final float DEFAULT_LENGTH = PART_COUNT;     // 默认披风长度
    private static final float CAPE_DEPTH = 0.2F;       // 披风厚度
    private static final float UV_SCALE = 1F;           // UV缩放
    private static final float WIND_SWING_MULTIPLIER = 2.0F; // 风摆动乘数
    private static final boolean SMOOTH_RENDERING = true; // 平滑渲染模式

    private ModelPart[] customCape = new ModelPart[PART_COUNT];

    private float capeWidth = DEFAULT_WIDTH;     // 披风宽度（可动态调整）
    private float capeLength = DEFAULT_LENGTH;   // 披风长度（可动态调整）

    private float widthScale = WIDTH_MULTIPLIER;
    private float lengthScale = LENGTH_MULTIPLIER;

    public CustomCapeRenderLayer(
            RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParent) {
        super(renderLayerParent);
        buildMesh();
    }

    private void buildMesh() {
        customCape = new ModelPart[PART_COUNT];
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        float scaledWidth = capeWidth * widthScale;
        float scaledLength = capeLength * lengthScale;

        float originalSegmentHeight = scaledLength / PART_COUNT;

        for (int i = 0; i < PART_COUNT; i++) {
            float cumulativeHeight = i * originalSegmentHeight;

            partDefinition.addOrReplaceChild("customCape_" + i,
                    CubeListBuilder.create()
                            .texOffs(0, (int)(i * originalSegmentHeight))
                            .addBox(
                                    -scaledWidth / 2, cumulativeHeight, -CAPE_DEPTH,
                                    scaledWidth, originalSegmentHeight, CAPE_DEPTH,
                                    CubeDeformation.NONE, UV_SCALE, 0.5F
                            ),
                    PartPose.offset(0.0F, 0.0F, 0.0F));
        }

        ModelPart modelPart = partDefinition.bake(64, 64);
        for (int i = 0; i < PART_COUNT; i++) {
            this.customCape[i] = modelPart.getChild("customCape_" + i);
        }
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int light,
                       AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {

        if (player.isInvisible()) return;

        CapeRenderer renderer = getCapeRenderer(player, multiBufferSource);
        if (renderer == null) return;

        ItemStack chestItem = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chestItem.is(Items.ELYTRA)) return;

        if (getParentModel() instanceof PlayerModelAccess pma && !pma.getCloak().visible) {
            return;
        }

        CapeHolder holder = (CapeHolder) player;
        holder.updateSimulation(player, PART_COUNT);

        if (SMOOTH_RENDERING && renderer.vanillaUvValues()) {
            renderSmoothCape(poseStack, multiBufferSource, renderer, player, partialTick, light);
        } else {
            for (int part = 0; part < PART_COUNT; part++) {
                ModelPart model = customCape[part];
                modifyPoseStack(poseStack, player, partialTick, part);
                renderer.render(player, part, model, poseStack, multiBufferSource, light, OverlayTexture.NO_OVERLAY);
                poseStack.popPose();
            }
        }
    }

    private void renderSmoothCape(PoseStack poseStack, MultiBufferSource multiBufferSource,
                                  CapeRenderer capeRenderer, AbstractClientPlayer player,
                                  float delta, int light) {
        VertexConsumer bufferBuilder = capeRenderer.getVertexConsumer(multiBufferSource, player);

        Matrix4f oldPositionMatrix = null;

        // 使用动态尺寸计算渲染参数
        float scaledWidth = capeWidth * widthScale;
        float scaledLength = capeLength * lengthScale;

        // 计算渲染坐标（原代码使用0.3F作为半宽，需要调整为基于实际宽度）
        float halfWidth = (scaledWidth / DEFAULT_WIDTH) * 0.3F; // 保持与原比例一致
        float depth = 0.06F;

        // 计算每个分块在渲染坐标系中的高度
        float segmentRenderHeight = (0.96F / PART_COUNT) * (scaledLength / DEFAULT_LENGTH);

        for (int part = 0; part < PART_COUNT; part++) {
            modifyPoseStack(poseStack, player, delta, part);

            if (oldPositionMatrix == null) {
                oldPositionMatrix = poseStack.last().pose();
            }

            // 计算累积高度（考虑缩放）
            float bottomY = (part + 1) * segmentRenderHeight;
            float topY = part * segmentRenderHeight;

            if (part == 0) {
                addTopVertex(bufferBuilder, poseStack.last().pose(), oldPositionMatrix,
                        halfWidth, 0, 0,
                        -halfWidth, 0, -depth, part, light);
            }

            if (part == PART_COUNT - 1) {
                addBottomVertex(bufferBuilder, poseStack.last().pose(), poseStack.last().pose(),
                        halfWidth, bottomY, 0,
                        -halfWidth, bottomY, -depth, part, light);
            }

            addLeftVertex(bufferBuilder, poseStack.last().pose(), oldPositionMatrix,
                    -halfWidth, bottomY, 0,
                    -halfWidth, topY, -depth, part, light);

            addRightVertex(bufferBuilder, poseStack.last().pose(), oldPositionMatrix,
                    halfWidth, bottomY, 0,
                    halfWidth, topY, -depth, part, light);

            addBackVertex(bufferBuilder, poseStack.last().pose(), oldPositionMatrix,
                    halfWidth, bottomY, -depth,
                    -halfWidth, topY, -depth, part, light);

            addFrontVertex(bufferBuilder, oldPositionMatrix, poseStack.last().pose(),
                    halfWidth, bottomY, 0,
                    -halfWidth, topY, 0, part, light);

            oldPositionMatrix = poseStack.last().pose();
            poseStack.popPose();
        }
    }

    private void modifyPoseStack(PoseStack poseStack, AbstractClientPlayer player, float delta, int part) {
        modifyPoseStackSimulation(poseStack, player, delta, part);
    }

    private void modifyPoseStackSimulation(PoseStack poseStack, AbstractClientPlayer player,
                                           float delta, int part) {
        BasicSimulation simulation = ((CapeHolder)player).momotinker$getSimulation();
        List<CapePoint> points = simulation.getPoints();

        poseStack.pushPose();
        poseStack.translate(0.0D, 0.0D, 0.125D);

        float baseX = points.get(0).getLerpX(delta);
        float baseY = points.get(0).getLerpY(delta);
        float baseZ = points.get(0).getLerpZ(delta);

        float x = points.get(part).getLerpX(delta) - baseX;
        float y = baseY - part - points.get(part).getLerpY(delta);
        float z = baseZ - points.get(part).getLerpZ(delta);

        if (x > 0) {
            x = 0;
        }

        float heightOffset = 0;
        if (player.isCrouching()) {
            heightOffset += 25.0F;
            poseStack.translate(0, 0.15F, 0);
        }

        float naturalWindSwing = getNaturalWindSwing(part, player.isUnderWater());

        poseStack.mulPose(Axis.XP.rotationDegrees(6.0F + heightOffset + naturalWindSwing));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        poseStack.translate(-z / PART_COUNT, y / PART_COUNT, x / PART_COUNT);

        // 调整旋转偏移量，考虑披风长度缩放
        float rotationOffset = 0.48f / 16;
        poseStack.translate(0, rotationOffset, -rotationOffset);

        // 调整垂直偏移量，考虑披风长度
        float verticalOffset = (1f / PART_COUNT) * (capeLength / DEFAULT_LENGTH);
        poseStack.translate(0, part * verticalOffset, 0);

        float partRotation = getPartRotation(delta, part, simulation);
        poseStack.mulPose(Axis.XP.rotationDegrees(-partRotation));

        poseStack.translate(0, -part * verticalOffset, 0);
        poseStack.translate(0, -rotationOffset, rotationOffset);
    }

    private float getPartRotation(float delta, int part, BasicSimulation simulation) {
        if (part == PART_COUNT - 1) {
            return getPartRotation(delta, part - 1, simulation);
        }

        List<CapePoint> points = simulation.getPoints();
        float x1 = points.get(part).getLerpX(delta);
        float y1 = points.get(part).getLerpY(delta);
        float x2 = points.get(part + 1).getLerpX(delta);
        float y2 = points.get(part + 1).getLerpY(delta);

        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.toDegrees(Math.atan2(dx, dy)) + 180;
    }

    private float getNaturalWindSwing(int part, boolean underwater) {
        long time = System.currentTimeMillis() / (underwater ? 9 : 3);
        float relativePart = (float) (part + 1) / PART_COUNT;
        return (float) (Math.sin(Math.toRadians(relativePart * 360 - (time % 360))) * WIND_SWING_MULTIPLIER);
    }

    // 以下顶点渲染方法保持不变（但实际渲染时会使用动态计算的参数）
    private static void addBackVertex(VertexConsumer bufferBuilder, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, float z1, float x2, float y2, float z2, int part, int light) {
        float i;
        Matrix4f k;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;

            k = matrix;
            matrix = oldMatrix;
            oldMatrix = k;
        }

        float minU = .015625F;
        float maxU = .171875F;

        float minV = .03125F;
        float maxV = .53125F;

        float deltaV = maxV - minV;
        float vPerPart = deltaV / PART_COUNT;
        maxV = minV + (vPerPart * (part + 1));
        minV = minV + (vPerPart * part);

        bufferBuilder.vertex(oldMatrix, x1, y2, z1).color(1f, 1f, 1f, 1f).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
        bufferBuilder.vertex(oldMatrix, x2, y2, z1).color(1f, 1f, 1f, 1f).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
        bufferBuilder.vertex(matrix, x2, y1, z2).color(1f, 1f, 1f, 1f).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
        bufferBuilder.vertex(matrix, x1, y1, z2).color(1f, 1f, 1f, 1f).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
    }

    private static void addFrontVertex(VertexConsumer bufferBuilder, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, float z1, float x2, float y2, float z2, int part, int light) {
        float i;
        Matrix4f k;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;

            k = matrix;
            matrix = oldMatrix;
            oldMatrix = k;
        }

        float minU = .1875F;
        float maxU = .34375F;

        float minV = .03125F;
        float maxV = .53125F;

        float deltaV = maxV - minV;
        float vPerPart = deltaV / PART_COUNT;
        maxV = minV + (vPerPart * (part + 1));
        minV = minV + (vPerPart * part);

        bufferBuilder.vertex(oldMatrix, x1, y1, z1).color(1f, 1f, 1f, 1f).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
        bufferBuilder.vertex(oldMatrix, x2, y1, z1).color(1f, 1f, 1f, 1f).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
        bufferBuilder.vertex(matrix, x2, y2, z2).color(1f, 1f, 1f, 1f).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
        bufferBuilder.vertex(matrix, x1, y2, z2).color(1f, 1f, 1f, 1f).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
    }

    private static void addLeftVertex(VertexConsumer bufferBuilder, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, float z1, float x2, float y2, float z2, int part, int light) {
        float i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }

        float minU = 0;
        float maxU = .015625F;

        float minV = .03125F;
        float maxV = .53125F;

        float deltaV = maxV - minV;
        float vPerPart = deltaV / PART_COUNT;
        maxV = minV + (vPerPart * (part + 1));
        minV = minV + (vPerPart * part);

        bufferBuilder.vertex(matrix, x2, y1, z1).color(1f, 1f, 1f, 1f).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
        bufferBuilder.vertex(matrix, x2, y1, z2).color(1f, 1f, 1f, 1f).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
        bufferBuilder.vertex(oldMatrix, x2, y2, z2).color(1f, 1f, 1f, 1f).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
        bufferBuilder.vertex(oldMatrix, x2, y2, z1).color(1f, 1f, 1f, 1f).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
    }

    private static void addRightVertex(VertexConsumer bufferBuilder, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, float z1, float x2, float y2, float z2, int part, int light) {
        float i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }

        float minU = .171875F;
        float maxU = .1875F;

        float minV = .03125F;
        float maxV = .53125F;

        float deltaV = maxV - minV;
        float vPerPart = deltaV / PART_COUNT;
        maxV = minV + (vPerPart * (part + 1));
        minV = minV + (vPerPart * part);

        bufferBuilder.vertex(matrix, x2, y1, z2).color(1f, 1f, 1f, 1f).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
        bufferBuilder.vertex(matrix, x2, y1, z1).color(1f, 1f, 1f, 1f).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
        bufferBuilder.vertex(oldMatrix, x2, y2, z1).color(1f, 1f, 1f, 1f).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
        bufferBuilder.vertex(oldMatrix, x2, y2, z2).color(1f, 1f, 1f, 1f).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
    }

    private static void addBottomVertex(VertexConsumer bufferBuilder, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, float z1, float x2, float y2, float z2, int part, int light) {
        float i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }

        float minU = .171875F;
        float maxU = .328125F;

        float minV = 0;
        float maxV = .03125F;

        float deltaV = maxV - minV;
        float vPerPart = deltaV / PART_COUNT;
        maxV = minV + (vPerPart * (part + 1));
        minV = minV + (vPerPart * part);

        bufferBuilder.vertex(oldMatrix, x1, y2, z2).color(1f, 1f, 1f, 1f).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
        bufferBuilder.vertex(oldMatrix, x2, y2, z2).color(1f, 1f, 1f, 1f).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
        bufferBuilder.vertex(matrix, x2, y1, z1).color(1f, 1f, 1f, 1f).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
        bufferBuilder.vertex(matrix, x1, y1, z1).color(1f, 1f, 1f, 1f).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
    }

    private static void addTopVertex(VertexConsumer bufferBuilder, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, float z1, float x2, float y2, float z2, int part, int light) {
        float i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }

        float minU = .015625F;
        float maxU = .171875F;

        float minV = 0;
        float maxV = .03125F;

        float deltaV = maxV - minV;
        float vPerPart = deltaV / PART_COUNT;
        maxV = minV + (vPerPart * (part + 1));
        minV = minV + (vPerPart * part);

        bufferBuilder.vertex(oldMatrix, x1, y2, z1).color(1f, 1f, 1f, 1f).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0, 1, 0).endVertex();
        bufferBuilder.vertex(oldMatrix, x2, y2, z1).color(1f, 1f, 1f, 1f).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0, 1, 0).endVertex();
        bufferBuilder.vertex(matrix, x2, y1, z2).color(1f, 1f, 1f, 1f).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0, 1, 0).endVertex();
        bufferBuilder.vertex(matrix, x1, y1, z2).color(1f, 1f, 1f, 1f).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0, 1, 0).endVertex();
    }

    private CapeRenderer getCapeRenderer(AbstractClientPlayer player, MultiBufferSource multiBufferSource) {
        if (!player.isCapeLoaded() || player.isInvisible()
                || !player.isModelPartShown(PlayerModelPart.CAPE)
                || player.getCloakTextureLocation() == null) {
            return null;
        } else {
            VanillaCapeRenderer renderer = new VanillaCapeRenderer();
            renderer.vertexConsumer = multiBufferSource
                    .getBuffer(RenderType.entityCutout(player.getCloakTextureLocation()));
            return renderer;
        }
    }
}