package com.momosensei.momotinker.mixins;

import com.momosensei.momotinker.test.testc.BasicSimulation;
import com.momosensei.momotinker.test.testc.CapeHolder;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
