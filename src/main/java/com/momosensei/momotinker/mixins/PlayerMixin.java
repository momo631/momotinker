package com.momosensei.momotinker.mixins;

/*
@Mixin(Player.class)
public class PlayerMixin implements CapeHolder {

    @Unique
    private BasicSimulation momotinker$stickSimulation;

    @Override
    public BasicSimulation momotinker$getSimulation() {
        return momotinker$stickSimulation;
    }

    @Override
    public void momotinker$setSimulation(BasicSimulation sim) {
        this.momotinker$stickSimulation = sim;
    }

    @Inject(method = "moveCloak", at = @At("HEAD"))
    private void moveCloakUpdate(CallbackInfo info) {
        if((Object)this instanceof AbstractClientPlayer) {
            simulate((AbstractClientPlayer)(Object)this);
        }
    }
    
}
*/