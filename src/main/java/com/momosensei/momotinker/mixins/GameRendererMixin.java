package com.momosensei.momotinker.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.momosensei.momotinker.register.MomotinkerEffects;
import com.momosensei.momotinker.register.MomotinkerModifiers;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(at = @At(value = "HEAD"),method = "bobHurt",cancellable = true)
    public void cancleShaking(PoseStack p_109118_, float p_109119_, CallbackInfo ci) {
        Entity entity = ((GameRenderer) (Object) this).getMinecraft().getCameraEntity();
        if (entity instanceof Player player) {
            for (ItemStack stack : player.getInventory().items) {
                if (ModifierUtil.getModifierLevel(stack, MomotinkerModifiers.origin.getId()) > 0) {
                    ci.cancel();
                }
            }
            if (player.hasEffect(MomotinkerEffects.End.get())){
                ci.cancel();
            }
        }
    }
//    @Inject(method = "render",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V",
//                    ordinal = 0
//            ))
//    private void PostRender(float pt, long startTime, boolean tick, CallbackInfo cbi){
//        PostEffectPipelines.RenderPost();
//    }

}