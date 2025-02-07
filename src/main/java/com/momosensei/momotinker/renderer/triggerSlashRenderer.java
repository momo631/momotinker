package com.momosensei.momotinker.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.momosensei.momotinker.entity.TriggerSlashEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;

import static com.momosensei.momotinker.entity.TriggerSlashEntity.getMold;

public class triggerSlashRenderer extends EntityRenderer<TriggerSlashEntity> {

    private final ItemRenderer itemRenderer;
    public triggerSlashRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
        this.itemRenderer=p_174008_.getItemRenderer();
    }
    public void render(TriggerSlashEntity entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        if (entity.tickCount >= 0 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(entity) < 12.25D)) {
            matrixStackIn.pushPose();
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(entityYaw, entity.yRotO, entity.getYRot())- 90.0F ));
            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(entityYaw, entity.xRotO, entity.getXRot())));
            matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(270+entity.angle));
            matrixStackIn.translate(-0.03125*Math.max(1, getMold(entity.getDeltaMovement())), -0.09375*Math.max(1, getMold(entity.getDeltaMovement())),0);
            matrixStackIn.scale((float)Math.max(1, getMold(entity.getDeltaMovement())),(float) Math.max(1, getMold(entity.getDeltaMovement())),(float) Math.max(1, getMold(entity.getDeltaMovement())));
            this.itemRenderer.renderStatic(entity.getSlash(), ItemTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, entity.getId());
            matrixStackIn.popPose();
            super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        }
    }

    protected int getSkyLightLevel(TriggerSlashEntity p_114509_, BlockPos p_114510_) {
        return 15;
    }
    protected int getBlockLightLevel(TriggerSlashEntity p_114496_, BlockPos p_114497_) {
        return 15;
    }


    @Override
    public ResourceLocation getTextureLocation(TriggerSlashEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
