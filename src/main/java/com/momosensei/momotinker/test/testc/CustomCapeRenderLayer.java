package com.momosensei.momotinker.test.testc;

/*
public class CustomCapeRenderLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    // 可调配常量
    private static final int PART_COUNT = 16;           // 披风分段数量
    private static final float CAPE_WIDTH = 10F;      // 披风宽度
    private static final float CAPE_DEPTH = 0.2F;       // 披风厚度
    private static final float UV_SCALE = 1F;         // UV缩放
    private static final float WIND_SWING_MULTIPLIER = 2F; // 风摆动乘数
    private static final boolean SMOOTH_RENDERING = true; // 平滑渲染模式

    private float capeModelScaleX = WIDTH_MULTIPLIER;
    private float capeModelScaleY = LENGTH_MULTIPLIER;

    private ModelPart[] customCape = new ModelPart[PART_COUNT];

    public CustomCapeRenderLayer(
            RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParent) {
        super(renderLayerParent);
        buildMesh();
    }

    private void buildMesh() {
        customCape = new ModelPart[PART_COUNT];
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        float scaledWidth = CAPE_WIDTH * capeModelScaleX;
        float originalSegmentHeight = 16f / PART_COUNT;
        float scaledSegmentHeight = originalSegmentHeight * capeModelScaleY;

        for (int i = 0; i < PART_COUNT; i++) {
            float cumulativeHeight = 0;
            for (int j = 0; j < i; j++) {
                cumulativeHeight += originalSegmentHeight * capeModelScaleY;
            }

            partDefinition.addOrReplaceChild("customCape_" + i,
                    CubeListBuilder.create().texOffs(0, (int)(i * originalSegmentHeight))
                            .addBox(-scaledWidth/2, cumulativeHeight, -CAPE_DEPTH,
                                    scaledWidth, scaledSegmentHeight, CAPE_DEPTH,
                                    CubeDeformation.NONE, UV_SCALE, 0.5F),
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
        float originalSegmentSize = 0.96F / PART_COUNT;
        float segmentSize = originalSegmentSize * capeModelScaleY;  // 应用长度缩放
        float halfWidth = 0.3F * capeModelScaleX;  // 应用宽度缩放
        float depth = 0.06F;

        for (int part = 0; part < PART_COUNT; part++) {
            modifyPoseStack(poseStack, player, delta, part);

            if (oldPositionMatrix == null) {
                oldPositionMatrix = poseStack.last().pose();
            }

            // 计算累积的高度，保持起始点不变
            float cumulativeBottomY = 0;
            float cumulativeTopY = 0;
            for (int i = 0; i < part + 1; i++) {
                cumulativeBottomY += originalSegmentSize * capeModelScaleY;
            }
            for (int i = 0; i < part; i++) {
                cumulativeTopY += originalSegmentSize * capeModelScaleY;
            }

            float bottomY = cumulativeBottomY;
            float topY = cumulativeTopY;

            // 添加顶部顶点（仅第一部分）
            if (part == 0) {
                addTopVertex(bufferBuilder, poseStack.last().pose(), oldPositionMatrix,
                        halfWidth, 0, 0,  // Y位置保持为0
                        -halfWidth, 0, -depth, part, light);
            }

            // 添加底部顶点（仅最后一部分）
            if (part == PART_COUNT - 1) {
                addBottomVertex(bufferBuilder, poseStack.last().pose(), poseStack.last().pose(),
                        halfWidth, bottomY, 0,
                        -halfWidth, bottomY, -depth, part, light);
            }

            // 添加左侧面
            addLeftVertex(bufferBuilder, poseStack.last().pose(), oldPositionMatrix,
                    -halfWidth, bottomY, 0,
                    -halfWidth, topY, -depth, part, light);

            // 添加右侧面
            addRightVertex(bufferBuilder, poseStack.last().pose(), oldPositionMatrix,
                    halfWidth, bottomY, 0,
                    halfWidth, topY, -depth, part, light);

            // 添加背面
            addBackVertex(bufferBuilder, poseStack.last().pose(), oldPositionMatrix,
                    halfWidth, bottomY, -depth,
                    -halfWidth, topY, -depth, part, light);

            // 添加正面
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

        // 计算相对于基点的偏移
        float baseX = points.get(0).getLerpX(delta);
        float baseY = points.get(0).getLerpY(delta);
        float baseZ = points.get(0).getLerpZ(delta);

        // 应用长度缩放
        float yOffset = part * capeModelScaleY; // 应用Y轴缩放
        float x = points.get(part).getLerpX(delta) - baseX;
        float y = baseY - yOffset - points.get(part).getLerpY(delta); // 考虑缩放后的Y偏移
        float z = baseZ - points.get(part).getLerpZ(delta);

        // 限制X轴偏移（考虑宽度缩放）
        float widthLimit = 0.0f * capeModelScaleX; // 根据宽度缩放调整限制
        if (x > widthLimit) {
            x = widthLimit;
        }

        float heightOffset = 0;
        if (player.isCrouching()) {
            heightOffset += 25.0F;
            poseStack.translate(0, 0.15F, 0);
        }

        float naturalWindSwing = getNaturalWindSwing(part, player.isUnderWater());

        // 应用基础旋转
        poseStack.mulPose(Axis.XP.rotationDegrees(6F + heightOffset + naturalWindSwing));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        // 应用模拟偏移
        poseStack.translate(-z / PART_COUNT, y / PART_COUNT, x / PART_COUNT);

        // 应用旋转偏移
        float rotationOffset = 0.48f / 16;
        poseStack.translate(0, rotationOffset, -rotationOffset);
        poseStack.translate(0, part * 1f / PART_COUNT, 0);

        // 应用分段旋转
        float partRotation = getPartRotation(delta, part, simulation);
        poseStack.mulPose(Axis.XP.rotationDegrees(-partRotation));

        // 应用缩放后的偏移
        poseStack.translate(0, -part * capeModelScaleY / PART_COUNT, 0); // 使用缩放后的Y偏移
        poseStack.translate(0, -rotationOffset, rotationOffset);
    }

    private float getPartRotation(float delta, int part, BasicSimulation simulation) {
        if (part == PART_COUNT - 1) {
            return getPartRotation(delta, part - 1, simulation);
        }
        float angle = (float) getAngle(simulation.getPoints().get(part).getLerpedPos(delta), simulation.getPoints().get(part+1).getLerpedPos(delta));
        return angle;
    }
    private double getAngle(Vector3 a, Vector3 b) {
        Vector3 angle = b.subtract(a);
        return Math.toDegrees(Math.atan2(angle.x, angle.y))+180;
    }
    private float getNaturalWindSwing(int part, boolean underwater) {
        long time = System.currentTimeMillis() / (underwater ? 9 : 3);
        float relativePart = (float) (part + 1) / PART_COUNT;
        return (float) (Math.sin(Math.toRadians(relativePart * 360 - (time % 360))) * WIND_SWING_MULTIPLIER);
    }

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

 */