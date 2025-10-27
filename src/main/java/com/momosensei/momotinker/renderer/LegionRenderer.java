package com.momosensei.momotinker.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.momosensei.momotinker.entity.LegionEntity.LegionEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;

public class LegionRenderer extends EntityRenderer<LegionEntity> {
    public ItemRenderer itemRenderer;
    public LegionRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
        this.itemRenderer = p_174008_.getItemRenderer();
    }

//    @Override
//    public void render(BoxEntity entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
//        if (entity.tickCount >= 0 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(entity) < 12.25D)) {
//            matrixStackIn.pushPose();
//
//            float spawnYaw = entity.getSpawnYaw();
//            float spawnPitch = entity.getSpawnPitch();
//
//            matrixStackIn.mulPose(Axis.YP.rotationDegrees(-spawnYaw));
//            matrixStackIn.mulPose(Axis.XP.rotationDegrees(spawnPitch));
//            matrixStackIn.translate(0, entity.getBbHeight() * 0.5F, 0);
//            float rotationSpeed = 12f;
//            float rotationAngle = (entity.tickCount + partialTicks) * rotationSpeed;
//            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(rotationAngle));
//
//            matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F));
//            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(135.0F));
//            matrixStackIn.translate(0, -entity.getBbHeight() * 0.55F, 0);
//
//            matrixStackIn.scale(1.5F,1.5F,1.5F);
//            this.itemRenderer.renderStatic(entity.getItem(), ItemTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, entity.getId());
//            matrixStackIn.popPose();
//            super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
//        }
//    }

    @Override
    public void render(LegionEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        double xRot = Mth.rotLerp(pPartialTick, pEntity.xRotO, pEntity.getXRot());
        double yRot = Mth.rotLerp(pPartialTick, pEntity.yRotO, pEntity.getYRot());
        Vec3 rotation = new Vec3(xRot, yRot, 180);

        pPoseStack.pushPose();

        pPoseStack.translate(0, pEntity.getBbHeight() * 0.5F, 0);

        pPoseStack.mulPose(Axis.YP.rotationDegrees((float) rotation.y - 90));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees((float) rotation.x));
        pPoseStack.mulPose(Axis.XP.rotationDegrees((float) rotation.z));

        float rotationSpeed = 64f;
        float rotationAngle = (pEntity.tickCount + pPartialTick) * rotationSpeed;
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(rotationAngle));

        pPoseStack.translate(-0.03125, -0.09375, 0);
        pPoseStack.scale(1.5F, 1.5F, 1.5F);

        this.itemRenderer.renderStatic(pEntity.getItem(), ItemDisplayContext.GROUND, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pEntity.level(),pEntity.getId());
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);

    }


    protected int getSkyLightLevel(LegionEntity p_114509_, BlockPos p_114510_) {
        return 15;
    }
    protected int getBlockLightLevel(LegionEntity p_114496_, BlockPos p_114497_) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(LegionEntity meteorEntity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
