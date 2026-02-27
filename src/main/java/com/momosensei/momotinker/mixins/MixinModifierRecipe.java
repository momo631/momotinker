package com.momosensei.momotinker.mixins;

import com.momosensei.momotinker.register.MomotinkerModifiers;
import com.momosensei.momotinker.test.testd.RandomRecipeManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.mantle.recipe.ingredient.SizedIngredient;
import slimeknights.tconstruct.library.json.IntRange;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.recipe.ITinkerableContainer;
import slimeknights.tconstruct.library.recipe.RecipeResult;
import slimeknights.tconstruct.library.recipe.modifiers.adding.AbstractModifierRecipe;
import slimeknights.tconstruct.library.recipe.modifiers.adding.ModifierRecipe;
import slimeknights.tconstruct.library.recipe.tinkerstation.IMutableTinkerStationContainer;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationContainer;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.nbt.LazyToolStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

@Mixin(ModifierRecipe.class)
public abstract class MixinModifierRecipe extends AbstractModifierRecipe {
    @Unique
    private static Modifier TCONSTRUCT_TARGET_MODIFIER;
    @Unique
    private boolean tconstruct_isRandomized = false;
    @Unique
    private boolean tconstruct_isServerSide = false;
    @Unique
    private boolean tconstruct_isFirstRandomization = true;

    protected MixinModifierRecipe(ResourceLocation id, Ingredient toolRequirement, int maxToolSize,
                                  ModifierId result, IntRange level, @Nullable SlotType.SlotCount slots, boolean allowCrystal,
                                  boolean checkTraitLevel) {
        super(id, toolRequirement, maxToolSize, result, level, slots, allowCrystal, checkTraitLevel);
    }

    @Unique
    private static Modifier getTargetModifier() {
        if (TCONSTRUCT_TARGET_MODIFIER == null) {
            try {
                TCONSTRUCT_TARGET_MODIFIER = MomotinkerModifiers.yamato.get();
            } catch (IllegalStateException e) {
                return null;
            }
        }
        return TCONSTRUCT_TARGET_MODIFIER;
    }

    @Inject(method = "matches", at = @At("HEAD"), cancellable = true, remap = false)
    private void onMatches(ITinkerStationContainer inv, Level world, CallbackInfoReturnable<Boolean> cir) {
        ModifierRecipe self = (ModifierRecipe)(Object)this;
        if (!isTargetModifier()) {
            return;
        }
        if (!world.isClientSide) {
            tconstruct_isServerSide = true;
            RandomRecipeManager.ensureRecipeInitialized(self.getId());
            if (shouldRandomize(self)) {
                randomizeRecipe(self);
            }
        }
        if (!this.toolRequirement.test(inv.getTinkerableStack())) {
            cir.setReturnValue(false);
            return;
        }
        List<SizedIngredient> currentInputs = RandomRecipeManager.getCurrentInputs(self.getId());
        if (currentInputs != null) {
            boolean result = matchesCrystal(inv) || checkMatch(inv, currentInputs);
            cir.setReturnValue(result);
        }
    }

    @Inject(method = "getValidatedResult", at = @At("RETURN"), remap = false)
    private void onGetValidatedResultReturn(ITinkerStationContainer inv, RegistryAccess access,
                                            CallbackInfoReturnable<RecipeResult<LazyToolStack>> cir) {
        if (isTargetModifier() && tconstruct_isServerSide && cir.getReturnValue().isSuccess()) {
            tconstruct_isRandomized = true;
            tconstruct_isFirstRandomization = false;
        }
    }

    @Inject(method = "updateInputs*", at = @At("HEAD"), remap = false)
    private void onUpdateInputs(LazyToolStack result, IMutableTinkerStationContainer inv, boolean isServer, CallbackInfo ci) {
        ModifierRecipe self = (ModifierRecipe)(Object)this;
        if (!isTargetModifier()) {
            return;
        }
        if (matchesCrystal(inv)) {
            return;
        }
        List<SizedIngredient> currentInputs = RandomRecipeManager.getCurrentInputs(self.getId());
        if (currentInputs != null) {
            BitSet used = makeBitset(inv);
            boolean allConsumed = true;

            for (SizedIngredient ingredient : currentInputs) {
                int index = findMatch(ingredient, inv, used);
                if (index != -1) {
                    inv.shrinkInput(index, ingredient.getAmountNeeded());
                } else {
                    allConsumed = false;
                }
            }

            if (allConsumed && isServer) {
                RandomRecipeManager.generateNewDisplay(self.getId());
            }
        }
    }

    @Inject(method = "matches", at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/library/recipe/modifiers/adding/ModifierRecipe;checkMatch(Lslimeknights/tconstruct/library/recipe/ITinkerableContainer;Ljava/util/List;)Z"), remap = false, cancellable = true)
    private void redirectCheckMatch(ITinkerStationContainer inv, Level world, CallbackInfoReturnable<Boolean> cir) {
        ModifierRecipe self = (ModifierRecipe)(Object)this;
        if (isTargetModifier()) {
            List<SizedIngredient> currentInputs = RandomRecipeManager.getCurrentInputs(self.getId());
            if (currentInputs != null) {
                boolean result = checkMatch(inv, currentInputs);
                cir.setReturnValue(result);
            }
        }
    }

    @Inject(method = "getDisplayItems", at = @At("HEAD"), remap = false, cancellable = true)
    public void onGetDisplayItems(int slot, CallbackInfoReturnable<List<ItemStack>> cir) {
        ModifierRecipe self = (ModifierRecipe)(Object)this;
        if (isTargetModifier()) {
            ResourceLocation recipeId = self.getId();
            RandomRecipeManager.ensureRecipeInitialized(recipeId);
            Map<Integer, ItemStack> fixedDisplays = RandomRecipeManager.getFixedDisplay(recipeId);
            if (fixedDisplays != null && fixedDisplays.containsKey(slot)) {
                List<ItemStack> displayStacks = new ArrayList<>();
                displayStacks.add(fixedDisplays.get(slot).copy());
                cir.setReturnValue(displayStacks);
            }
        }
    }

    @Unique
    private boolean isTargetModifier() {
        Modifier target = getTargetModifier();
        return target != null && this.result.get() == target;
    }

    @Unique
    private boolean shouldRandomize(ModifierRecipe recipe) {
        if (tconstruct_isFirstRandomization) {
            return true;
        }
        return RandomRecipeManager.getCurrentInputs(recipe.getId()) == null;
    }

    @Unique
    private void randomizeRecipe(ModifierRecipe recipe) {
        RandomRecipeManager.updateCurrentInputs(recipe.getId());
        tconstruct_isRandomized = true;
        tconstruct_isFirstRandomization = false;
    }

    @Unique
    private BitSet makeBitset(ITinkerableContainer inv) {
        int inputs = inv.getInputCount();
        BitSet used = new BitSet(inputs);
        for (int i = 0; i < inputs; i++) {
            if (inv.getInput(i).isEmpty()) {
                used.set(i);
            }
        }
        return used;
    }

    @Unique
    private int findMatch(SizedIngredient ingredient, ITinkerableContainer inv, BitSet used) {
        for (int i = 0; i < inv.getInputCount(); i++) {
            if (!used.get(i)) {
                ItemStack stack = inv.getInput(i);
                if (ingredient.test(stack)) {
                    used.set(i);
                    return i;
                }
            }
        }
        return -1;
    }

    @Unique
    private boolean checkMatch(ITinkerableContainer inv, List<SizedIngredient> inputs) {
        if (inputs.isEmpty()) {
            return false;
        }
        BitSet used = makeBitset(inv);
        for (SizedIngredient ingredient : inputs) {
            int index = findMatch(ingredient, inv, used);
            if (index == -1) {
                return false;
            }
        }
        for (int i = 0; i < inv.getInputCount(); i++) {
            if (!used.get(i) && !inv.getInput(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Unique
    private boolean matchesCrystal(ITinkerableContainer inv) {
        try {
            java.lang.reflect.Method method = AbstractModifierRecipe.class.getDeclaredMethod("matchesCrystal", ITinkerableContainer.class);
            method.setAccessible(true);
            return (boolean) method.invoke(this, inv);
        } catch (Exception e) {
            return false;
        }
    }
}