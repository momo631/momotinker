package com.momosensei.momotinker.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class Layer {
    public static final ResourceLocation CHARGE_TEXTURE = new ResourceLocation(Momotinker.MOD_ID, "/textures/entity/charged.png");
    public static final long CHARGED = 64;

    public static class Vanilla extends RenderLayer<Player, HumanoidModel<Player>> {
        public static ModelLayerLocation ENERGY_LAYER = new ModelLayerLocation(new ResourceLocation(Momotinker.MOD_ID, "energy_layer"), "main");
        private final HumanoidModel<Player> model;
        private final ResourceLocation TEXTURE;
        private final Long shouldRenderFlag;

        public Vanilla(RenderLayerParent pRenderer, ResourceLocation texture, Long shouldRenderFlag) {
            super(pRenderer);
            this.model = new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ENERGY_LAYER));
            this.TEXTURE = texture;
            this.shouldRenderFlag = shouldRenderFlag;
        }

        public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, Player pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
            if (Layer.shouldRender(pLivingEntity, shouldRenderFlag)) {
                float f = (float) pLivingEntity.tickCount + pPartialTicks;
                HumanoidModel<Player> entitymodel = this.model();
                entitymodel.prepareMobModel(pLivingEntity, pLimbSwing, pLimbSwingAmount, pPartialTicks);
                this.getParentModel().copyPropertiesTo(entitymodel);
                VertexConsumer vertexconsumer = pBuffer.getBuffer(Layer.getRenderType(TEXTURE, f));
                entitymodel.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
                entitymodel.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 0.8F, 0.8F, 0.8F, 1.0F);
            }
        }
        protected HumanoidModel<Player> model() {
            return model;
        }
    }

    private static RenderType getRenderType(ResourceLocation texture, float f) {
        return RenderType.energySwirl(texture, f * 0.02F % 1.0F, f * 0.01F % 1.0F);
    }

    private static boolean shouldRender(Player player, Long shouldRenderFlag) {
        return player.hasEffect(MomotinkerEffects.B.get());
    }
}