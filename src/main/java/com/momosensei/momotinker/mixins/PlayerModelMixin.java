package com.momosensei.momotinker.mixins;

/*
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

 */
