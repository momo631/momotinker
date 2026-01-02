package com.momosensei.momotinker.mixins;

import com.momosensei.momotinker.Modifiers.hook.SweepAttackModifierHook;
import com.momosensei.momotinker.register.MomotinkerHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.definition.module.weapon.SweepWeaponAttack;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.modifiers.upgrades.melee.SweepingEdgeModifier;

@Mixin(SweepWeaponAttack.class)
public abstract class SweepWeaponAttackMixin {
    @Unique
    private static final ThreadLocal<IToolStackView> CURRENT_TOOL = new ThreadLocal<>();
    @Unique
    private static final ThreadLocal<ToolAttackContext> CURRENT_CONTEXT = new ThreadLocal<>();
    @Unique
    private static final ThreadLocal<Float> CURRENT_BASE_DAMAGE = new ThreadLocal<>();

    @Inject(method = "afterMeleeHit", at = @At("HEAD"), remap = false)
    private void captureContext(IToolStackView tool, ToolAttackContext context, float damage, CallbackInfo ci) {
        CURRENT_TOOL.set(tool);
        CURRENT_CONTEXT.set(context);
        CURRENT_BASE_DAMAGE.set(damage);
    }

    @Inject(method = "afterMeleeHit", at = @At("RETURN"), remap = false)
    private void clearContext(CallbackInfo ci) {
        CURRENT_TOOL.remove();
        CURRENT_CONTEXT.remove();
        CURRENT_BASE_DAMAGE.remove();
    }

    @Redirect(
            method = "afterMeleeHit",
            at = @At(
                    value = "INVOKE",
                    target = "Lslimeknights/tconstruct/tools/modifiers/upgrades/melee/SweepingEdgeModifier;getSweepingDamage(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;F)F"
            ),
            remap = false
    )
    private float redirectSweepingDamage(SweepingEdgeModifier instance, IToolStackView tool, float baseDamage) {
        float original = instance.getSweepingDamage(tool, baseDamage);
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

    @ModifyVariable(
            method = "afterMeleeHit",
            at = @At(
                    value = "STORE",
                    ordinal = 0
            ),
            ordinal = 0,
            remap = false
    )
    private double modifySweepRange(double range) {
        IToolStackView tool = CURRENT_TOOL.get();
        ToolAttackContext context = CURRENT_CONTEXT.get();
        if (tool == null || context == null) {
            return range;
        }
        float expandedLevel = tool.getModifierLevel(TinkerModifiers.expanded.getId());
        double modifiedBaseRange = range - expandedLevel;
        for (ModifierEntry entry : tool.getModifierList()) {
            SweepAttackModifierHook hook = entry.getHook(MomotinkerHook.SWEEP_ATTACK);
            modifiedBaseRange = hook.modifySweepRange(tool, entry, context, modifiedBaseRange);
        }
        return modifiedBaseRange + expandedLevel;
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
