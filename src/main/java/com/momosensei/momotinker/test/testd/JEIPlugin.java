package com.momosensei.momotinker.test.testd;
/*
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.register.MomotinkerBlock;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import slimeknights.mantle.recipe.helper.RecipeHelper;
import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.modifiers.ModifierRecipeLookup;
import slimeknights.tconstruct.library.recipe.modifiers.adding.IDisplayModifierRecipe;
import slimeknights.tconstruct.plugin.jei.TConstructJEIConstants;
import slimeknights.tconstruct.plugin.jei.modifiers.ModifierBookmarkIngredientRenderer;
import slimeknights.tconstruct.plugin.jei.modifiers.ModifierIngredientHelper;
import slimeknights.tconstruct.plugin.jei.modifiers.ModifierRecipeCategory;

import java.util.Collections;
import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

  @Override
  public ResourceLocation getPluginUid() {
    return Momotinker.getResource("jei_plugin");
  }

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {
    final IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
    registry.addRecipeCategories(new ModifierRecipeCategory(guiHelper));
  }

  @Override
  public void registerIngredients(IModIngredientRegistration registration) {
    List<ModifierEntry> modifiers = Collections.emptyList();
    if (Config.CLIENT.showModifiersInJEI.get()) {
      modifiers = ModifierRecipeLookup.getRecipeModifierList();
    }
    registration.register(
            TConstructJEIConstants.MODIFIER_TYPE,
            modifiers,
            new ModifierIngredientHelper(),
            ModifierBookmarkIngredientRenderer.INSTANCE
    );
  }

  @Override
  public void registerRecipes(IRecipeRegistration register) {
    Level level = Minecraft.getInstance().level;
    assert level != null;
    RegistryAccess access = level.registryAccess();
    RecipeManager manager = level.getRecipeManager();

    List<IDisplayModifierRecipe> modifierRecipes = RecipeHelper.getJEIRecipes(
            access, manager, TinkerRecipeTypes.TINKER_STATION.get(), IDisplayModifierRecipe.class
    );
    register.addRecipes(TConstructJEIConstants.MODIFIERS, modifierRecipes);
  }

  @Override
  public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
    registry.addRecipeCatalyst(
            new ItemStack(MomotinkerBlock.tinkerStation.get()),
            TConstructJEIConstants.MODIFIERS
    );
  }

  @Override
  public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
    IRecipeTransferHandlerHelper helper = registration.getTransferHelper();

    registration.addRecipeTransferHandler(
            new STinkerStationTransferInfo<>(TConstructJEIConstants.MODIFIERS, helper),
            TConstructJEIConstants.MODIFIERS
    );
  }
}

 */