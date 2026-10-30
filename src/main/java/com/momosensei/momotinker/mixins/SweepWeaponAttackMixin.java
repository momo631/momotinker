package com.momosensei.momotinker.mixins;

import com.momosensei.momotinker.Modifiers.hook.SweepAttackModifierHook;
import com.momosensei.momotinker.register.MomotinkerHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.json.LevelingValue;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.definition.module.weapon.SweepWeaponAttack;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

@Mixin(SweepWeaponAttack.class)
public abstract class SweepWeaponAttackMixin {
    @Unique private static final ThreadLocal<IToolStackView> CURRENT_TOOL = new ThreadLocal<>();
    @Unique private static final ThreadLocal<ToolAttackContext> CURRENT_CONTEXT = new ThreadLocal<>();

    @Inject(method = "afterMeleeHit", at = @At("HEAD"), remap = false)
    private void captureContext(IToolStackView tool, ToolAttackContext context, float damage, CallbackInfo ci) {
        CURRENT_TOOL.set(tool);
        CURRENT_CONTEXT.set(context);
    }

    @Inject(method = "afterMeleeHit", at = @At("RETURN"), remap = false)
    private void clearContext(IToolStackView tool, ToolAttackContext context, float damage, CallbackInfo ci) {
        CURRENT_TOOL.remove();
        CURRENT_CONTEXT.remove();
    }

    @Redirect(
            method = "afterMeleeHit",
            at = @At(
                    value = "INVOKE",
                    target = "Lslimeknights/tconstruct/library/tools/definition/module/weapon/SweepWeaponAttack;getSweepingDamage(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;F)F"
            ),
            remap = false
    )
    private float redirectSweepingDamage(IToolStackView tool, float baseDamage) {
        float original = SweepWeaponAttack.getSweepingDamage(tool, baseDamage);
        ToolAttackContext context = CURRENT_CONTEXT.get();
        if (context == null) {
            return original;
        }
        float modified = original;
        for (ModifierEntry entry : tool.getModifierList()) {
            SweepAttackModifierHook hook = entry.getHook(MomotinkerHook.SWEEP_ATTACK);
            modified = hook.modifySweepDamage(tool, entry, context, baseDamage, modified);
        }
        return modified;
    }

    @Redirect(
            method = "afterMeleeHit",
            at = @At(
                    value = "INVOKE",
                    target = "Lslimeknights/tconstruct/library/json/LevelingValue;compute(F)F"
            ),
            remap = false
    )
    private float redirectRangeCompute(LevelingValue levelingValue, float level) {
        float original = levelingValue.compute(level);
        ToolAttackContext context = CURRENT_CONTEXT.get();
        IToolStackView tool = CURRENT_TOOL.get();
        if (tool == null || context == null) {
            return original;
        }
        double modifiedRange = original;
        for (ModifierEntry entry : tool.getModifierList()) {
            SweepAttackModifierHook hook = entry.getHook(MomotinkerHook.SWEEP_ATTACK);
            modifiedRange = hook.modifySweepRange(tool, entry, context, modifiedRange);
        }
        return (float) modifiedRange;
    }

    @ModifyArg(
            method = "afterMeleeHit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"
            ),
            index = 0
    )
    private double modifyKnockbackStrength(double knockback) {
        IToolStackView tool = CURRENT_TOOL.get();
        ToolAttackContext context = CURRENT_CONTEXT.get();
        if (tool == null || context == null) {
            return knockback;
        }
        float modifiedKnockback = (float)knockback;
        for (ModifierEntry entry : tool.getModifierList()) {
            SweepAttackModifierHook hook = entry.getHook(MomotinkerHook.SWEEP_ATTACK);
            modifiedKnockback = hook.modifySweepKnockback(tool, entry, context, modifiedKnockback);
        }
        return modifiedKnockback;
    }
}
