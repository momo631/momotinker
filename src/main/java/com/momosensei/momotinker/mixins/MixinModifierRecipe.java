package com.momosensei.momotinker.mixins;

import com.momosensei.momotinker.mobs.ClientCache;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.RequestDataPacket;
import com.momosensei.momotinker.register.MomotinkerItem;
import com.momosensei.momotinker.util.RandomRecipeManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.mantle.recipe.ingredient.SizedIngredient;
import slimeknights.tconstruct.library.json.IntRange;
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
import java.util.*;

@Mixin(ModifierRecipe.class)
public abstract class MixinModifierRecipe extends AbstractModifierRecipe {
    @Unique
    private static final Map<ResourceLocation, ModifierId> TARGET_MODIFIER_IDS = new HashMap<>();
    @Unique
    private static final Set<ResourceLocation> PROCESSED_RECIPES = new HashSet<>();
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
    private ModifierId getTargetModifierId(Level world) {
        if (world.isClientSide) {
            return null;
        }
        ModifierRecipe self = (ModifierRecipe)(Object)this;
        ResourceLocation recipeId = self.getId();
        if (PROCESSED_RECIPES.contains(recipeId)) {
            return TARGET_MODIFIER_IDS.get(recipeId);
        }
        ResourceLocation targetModifierId = RandomRecipeManager.getModifierId(recipeId);
        if (targetModifierId == null) {
            PROCESSED_RECIPES.add(recipeId);
            return null;
        }
        ModifierId modifierId = new ModifierId(targetModifierId);
        TARGET_MODIFIER_IDS.put(recipeId, modifierId);
        PROCESSED_RECIPES.add(recipeId);
        return modifierId;
    }

    @Unique
    private boolean shouldApplyToRecipe(Level world) {
        if (world.isClientSide) {
            return false;
        }
        ModifierId targetModifierId = getTargetModifierId(world);
        if (targetModifierId == null) {
            return false;
        }
        ModifierId currentResultId = this.result.getId();
        return currentResultId.equals(targetModifierId);
    }

    @Inject(method = "matches", at = @At("HEAD"), cancellable = true, remap = false)
    private void onMatches(ITinkerStationContainer inv, Level world, CallbackInfoReturnable<Boolean> cir) {
        if (world.isClientSide) {
            return;
        }
        ModifierRecipe self = (ModifierRecipe)(Object)this;
        if (!shouldApplyToRecipe(world)) {
            return;
        }
        tconstruct_isServerSide = true;
        RandomRecipeManager.ensureRecipeInitialized(self.getId());
        if (shouldRandomize(self)) {
            randomizeRecipe(self);
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
//    @Inject(method = "matches", at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/library/recipe/modifiers/adding/ModifierRecipe;checkMatch(Lslimeknights/tconstruct/library/recipe/ITinkerableContainer;Ljava/util/List;)Z"), remap = false, cancellable = true)
//    private void redirectCheckMatch(ITinkerStationContainer inv, Level world, CallbackInfoReturnable<Boolean> cir) {
//        ModifierRecipe self = (ModifierRecipe) (Object) this;
//        List<SizedIngredient> currentInputs = RandomRecipeManager.getCurrentInputs(self.getId());
//        if (currentInputs != null) {
//            boolean result = checkMatch(inv, currentInputs);
//            cir.setReturnValue(result);
//        }
//    }
    @Inject(method = "getValidatedResult", at = @At("RETURN"), remap = false)
    private void onGetValidatedResultReturn(ITinkerStationContainer inv, RegistryAccess access, CallbackInfoReturnable<RecipeResult<LazyToolStack>> cir) {
        if (tconstruct_isServerSide && cir.getReturnValue().isSuccess()) {
            tconstruct_isFirstRandomization = false;
        }
    }

    @Inject(method = "updateInputs*", at = @At("HEAD"), remap = false)
    private void onUpdateInputs(LazyToolStack result, IMutableTinkerStationContainer inv, boolean isServer, CallbackInfo ci) {
        if (!isServer) {
            return;
        }
        ModifierRecipe self = (ModifierRecipe)(Object)this;
        if (tconstruct_isServerSide) {
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
                    RandomRecipeManager.resetAllPlayersStage(self.getId());
                    RandomRecipeManager.generateNewDisplay(self.getId());
                    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                    if (server!=null) {
                        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                            RandomRecipeManager.refreshAllClientDisplays(player);
                        }
                    }
                }
            }
        }
    }

    @Unique
    private static final List<ItemStack> SLOT_ITEMS = Arrays.asList(
            MomotinkerItem.hidden_bit_a.get().getDefaultInstance(),
            MomotinkerItem.hidden_bit_b.get().getDefaultInstance(),
            MomotinkerItem.hidden_bit_c.get().getDefaultInstance(),
            MomotinkerItem.hidden_bit_d.get().getDefaultInstance(),
            MomotinkerItem.hidden_bit_e.get().getDefaultInstance()
    );

    @Unique
    private ItemStack getItemForSlot(int slot, int count) {
        ItemStack stack = SLOT_ITEMS.get(slot).copy();
        stack.setCount(count);
        return stack;
    }

    @Inject(method = "getDisplayItems", at = @At("HEAD"), remap = false, cancellable = true)
    public void onGetDisplayItems(int slot, CallbackInfoReturnable<List<ItemStack>> cir) {
        ModifierRecipe self = (ModifierRecipe)(Object)this;
        ResourceLocation recipeId = self.getId();
        RandomRecipeManager.ensureRecipeInitialized(recipeId);
        Map<Integer, ItemStack> fixedDisplays = RandomRecipeManager.getFixedDisplay(recipeId);
        if (fixedDisplays != null ) {
            if (fixedDisplays.containsKey(slot)) {
                List<ItemStack> displayStacks = new ArrayList<>();
                if (!ClientCache.hasStage(recipeId)) {
                    if (ClientCache.trySetPending(recipeId)) {
                        Channel.sendToServer(new RequestDataPacket(recipeId));
                    }
                } else {
                    int stage = ClientCache.getStage(recipeId);
                    if (slot < stage) {
                        displayStacks.add(fixedDisplays.get(slot).copy());
                    } else {
                        displayStacks.add(getItemForSlot(slot, fixedDisplays.get(slot).getCount()));
                    }
                }
                cir.setReturnValue(displayStacks);
                cir.cancel();
            } else {
                cir.setReturnValue(new ArrayList<>());
                cir.cancel();
            }
        }
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
}