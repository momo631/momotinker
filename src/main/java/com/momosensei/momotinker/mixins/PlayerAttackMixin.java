package com.momosensei.momotinker.mixins;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.momosensei.momotinker.Items.tool.nodens.nodens_ready;

@Mixin(Player.class)
public abstract class PlayerAttackMixin extends LivingEntity {
    protected PlayerAttackMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "getAttackStrengthScale", at = @At(value = "RETURN"), cancellable = true)
    public void changeScale(float pAdjustTicks, CallbackInfoReturnable<Float> cir) {
        Player player = (Player) (Object) this;
        if (player.getPersistentData().getBoolean(nodens_ready.toString())) {
            cir.setReturnValue(1f);
        }
    }
}