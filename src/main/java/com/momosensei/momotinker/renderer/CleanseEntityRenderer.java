package com.momosensei.momotinker.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.momosensei.momotinker.entity.CleanseEntity;
import com.momosensei.momotinker.register.MomotinkerItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CleanseEntityRenderer extends EntityRenderer<CleanseEntity> {
    public ItemRenderer itemRenderer;
    public CleanseEntityRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
        this.itemRenderer = p_174008_.getItemRenderer();
    }

    @Override
    public void render(CleanseEntity entity, float entityYaw, float p_114487_, PoseStack matrixStackIn, MultiBufferSource p_114489_, int p_114490_) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(entity.tickCount%180));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(0));
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(0));
        matrixStackIn.translate(-0.03125, -0.09375,0);
        matrixStackIn.scale(30,30,30);
        this.itemRenderer.renderStatic(new ItemStack(MomotinkerItem.cleanse_item.get()), ItemDisplayContext.GROUND, p_114490_, OverlayTexture.NO_OVERLAY, matrixStackIn, p_114489_,entity.level(),entity.getId());
        matrixStackIn.popPose();
    }
    protected int getSkyLightLevel(CleanseEntity p_114509_, BlockPos p_114510_) {
        return 15;
    }
    protected int getBlockLightLevel(CleanseEntity p_114496_, BlockPos p_114497_) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(CleanseEntity cleanseEntity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
