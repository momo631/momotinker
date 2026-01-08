package com.momosensei.momotinker.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.momosensei.momotinker.test.testc.PlayerModelAccess;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value =  PlayerModel.class)
public class PlayerModelMixin<T extends LivingEntity> extends HumanoidModel<T> implements PlayerModelAccess {

    @Shadow
    @Final
    private ModelPart cloak;
    
    public PlayerModelMixin(ModelPart modelPart) {
        super(modelPart);
    }

    @Inject(method = "renderCloak", at = @At("HEAD"), cancellable = true)
    public void renderCloak(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int j, CallbackInfo info) {
        info.cancel();
    }

    @Override
    public ModelPart getCloak() {
        return cloak;
    }


}
