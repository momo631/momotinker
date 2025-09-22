package com.momosensei.momotinker.mixins;

import com.momosensei.momotinker.register.MomotinkerEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = LivingEntity.class, priority = Integer.MAX_VALUE)
public abstract class LivingEntityMixin {

    @Inject(method = "aiStep", at = @At("HEAD"), cancellable = true)
    private void HorologiumNoAI(CallbackInfo ci) {
        LivingEntity self = (LivingEntity)(Object)this;

        if (self.hasEffect(MomotinkerEffects.Stiffening.get())&&self.getEffect(MomotinkerEffects.Stiffening.get())!=null&&self.getEffect(MomotinkerEffects.Stiffening.get()).getAmplifier()>=20) {
            ci.cancel();
        }
    }

    @Inject(method = "setHealth", at = @At("HEAD"), cancellable = true)
    private void onSetHealth(float health, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity)(Object)this;

        if (!(entity instanceof Player player) || !player.isAddedToWorld()) {
            return;
        }

        if (player.isDeadOrDying()) {
            return;
        }

        if (player.hasEffect(MomotinkerEffects.Stiffening.get())&&player.getEffect(MomotinkerEffects.Stiffening.get())!=null&&player.getEffect(MomotinkerEffects.Stiffening.get()).getAmplifier()>=20) {
            float damageAmount = player.getHealth() - health;
            if (damageAmount > 7.0f) {
                ci.cancel();
            }
        }
    }

}
