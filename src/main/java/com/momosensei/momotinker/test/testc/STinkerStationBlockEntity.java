package com.momosensei.momotinker.test.testc;
/*
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.register.MomotinkerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.StringUtils;
import slimeknights.mantle.util.RetexturedHelper;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.SoundUtils;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.library.client.model.ModelProperties;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.recipe.RecipeResult;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationRecipe;
import slimeknights.tconstruct.library.tools.helper.TooltipUtil;
import slimeknights.tconstruct.library.tools.nbt.LazyToolStack;
import slimeknights.tconstruct.shared.inventory.ConfigurableInvWrapperCapability;
import slimeknights.tconstruct.tables.block.entity.inventory.LazyResultContainer;
import slimeknights.tconstruct.tables.block.entity.inventory.LazyResultContainer.ILazyCrafter;
import slimeknights.tconstruct.tables.block.entity.table.RetexturedTableBlockEntity;

import javax.annotation.Nullable;
import java.util.Objects;

import static slimeknights.tconstruct.library.tools.part.IMaterialItem.MATERIAL_TAG;

public class STinkerStationBlockEntity extends RetexturedTableBlockEntity implements ILazyCrafter {

  public static final int TINKER_SLOT = 0;
  public static final int INPUT_SLOT = 1;
  private static final Component NAME = TConstruct.makeTranslation("gui", "tinker_station");

  @Nullable
  private ITinkerStationRecipe lastRecipe;

  public LazyResultContainer getCraftingResult() {
    return craftingResult;
  }

  private final LazyResultContainer craftingResult;
  private final STinkerStationContainerWrapper inventoryWrapper;

  @Nullable
  private LazyToolStack result = null;
  @Nullable
  private Component currentError = null;

  public String getItemName() {
    return itemName;
  }

  private String itemName = "";

  public MaterialVariantId getMaterial() {
    return material;
  }

  private MaterialVariantId material = IMaterial.UNKNOWN_ID;

  public STinkerStationBlockEntity(BlockPos pos, BlockState state) {
    // if the block is the right type, use it for slot count
    this(pos, state, (state.getBlock() instanceof STinkerStationBlock station) ? station.getSlotCount() : 6);
  }

  public STinkerStationBlockEntity(BlockPos pos, BlockState state, int slots) {
    super(MomotinkerBlock.StinkerStationTile.get(), pos, state, NAME, slots);
    this.itemHandler = new ConfigurableInvWrapperCapability(this, false, false);
    this.itemHandlerCap = LazyOptional.of(() -> this.itemHandler);
    this.inventoryWrapper = new STinkerStationContainerWrapper(this);
    this.craftingResult = new LazyResultContainer(this);
  }

  @Override
  public Component getDefaultName() {
    if (this.level == null) {
      return super.getDefaultName();
    }
    return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
  }

  public int getInputCount() {
    return getContainerSize() - 1;
  }

  public LazyToolStack getTool() {
    return inventoryWrapper.getTool();
  }

  @Nullable
  public LazyToolStack getResult() {
    // ensure the result has been resolved else we may be returning null when we shouldn't
    // if we return null that means there is no result, not its not calculated.
    craftingResult.getResult();
    return result;
  }

  @SuppressWarnings("unused")
  @Deprecated(forRemoval = true)
  @Nullable
  public LazyToolStack getResult(@Nullable Player player) {
    return getResult();
  }

  @Override
  public void resize(int size) {
    super.resize(size);
    inventoryWrapper.resize();
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int menuId, Inventory playerInventory, Player playerEntity) {
    return new STinkerStationContainerMenu(menuId, playerInventory, this);
  }

  @Override
  public ItemStack calcResult(@Nullable Player player) {
    if (this.level == null) {
      return ItemStack.EMPTY;
    }

    // assume empty unless we learn otherwise
    result = null;
    this.currentError = null;

    if (!this.level.isClientSide && this.level.getServer() != null) {
      RecipeManager manager = this.level.getServer().getRecipeManager();

      // first, try the cached recipe
      ITinkerStationRecipe recipe = lastRecipe;
      // if it does not match, find a new recipe
      if (recipe == null || !recipe.matches(this.inventoryWrapper, this.level)) {
        recipe = manager.getRecipeFor(TinkerRecipeTypes.TINKER_STATION.get(), this.inventoryWrapper, this.level).orElse(null);
      }

      // if we have a recipe, fetch its result
      boolean needsSync = true;
      if (recipe != null) {
        // sync if the recipe is different
        if (lastRecipe != recipe) {
          this.lastRecipe = recipe;
          this.syncToRelevantPlayers(this::syncRecipe);
          needsSync = false;
        }

        // try for UI errors
        RecipeResult<LazyToolStack> validatedResult = recipe.getValidatedResult(this.inventoryWrapper, level.registryAccess());
        if (validatedResult.isSuccess()) {
          result = validatedResult.getResult();
        } else if (validatedResult.hasError()) {
          this.currentError = validatedResult.getMessage();
        }
      }
      // recipe will sync screen, so only need to call it when not syncing the recipe
      if (needsSync) {
        syncScreenToRelevantPlayers();
      }
    }
    // client side only needs to update result, server syncs message elsewhere
    else if (this.lastRecipe != null && this.lastRecipe.matches(this.inventoryWrapper, level)) {
      RecipeResult<LazyToolStack> validatedResult = this.lastRecipe.getValidatedResult(this.inventoryWrapper, level.registryAccess());
      if (validatedResult.isSuccess()) {
        result = validatedResult.getResult();
      } else if (validatedResult.hasError()) {
        this.currentError = validatedResult.getMessage();
      }
    }

    if (result != null) {
      // set name if we have one
      if (!itemName.isEmpty()) {
        TooltipUtil.setDisplayName(result.getStack(), itemName);
      }

      return result.getStack();
    } else {
      return ItemStack.EMPTY;
    }
  }

  @Override
  public void onCraft(Player player, ItemStack resultItem, int amount) {
    // the recipe should match if we got this far, but being null is a problem
    LazyToolStack result = this.result;  // result is going to get cleared as we update things
    if (amount == 0 || this.level == null || this.lastRecipe == null || result == null) {
      return;
    }

    // fire crafting events
    resultItem.onCraftedBy(this.level, player, amount);
    ForgeEventFactory.firePlayerCraftingEvent(player, resultItem, this.inventoryWrapper);
    this.playCraftSound(player);

    // fetch this before updating inputs so they can do input sensitive shrinking
    ItemStack tinkerable = this.getItem(TINKER_SLOT);
    int shrinkToolSlot = tinkerable.isEmpty() ? 0 : lastRecipe.shrinkToolSlotBy(result, inventoryWrapper);

    // run the recipe, will shrink inputs
    // run both sides for the sake of shift clicking
    this.inventoryWrapper.setPlayer(player);
    this.lastRecipe.updateInputs(result, inventoryWrapper, !level.isClientSide);
    this.inventoryWrapper.setPlayer(null);

    // remove the center slot item, just clear it entirely (if you want shrinking you should use the outer slots or ask nicely for a shrink amount hook)
    if (shrinkToolSlot > 0) {
      if (tinkerable.getCount() <= shrinkToolSlot) {
        this.setItem(TINKER_SLOT, ItemStack.EMPTY);
      } else {
        this.setItem(TINKER_SLOT, ItemHandlerHelper.copyStackWithSize(tinkerable, tinkerable.getCount() - shrinkToolSlot));
      }
    }
    this.itemName = "";
  }

  @Override
  public void setItem(int slot, ItemStack itemstack) {
    super.setItem(slot, itemstack);
    // clear the crafting result when the matrix changes so we recalculate the result
    this.craftingResult.clearContent();
    this.inventoryWrapper.refreshInput(slot);
  }
  
  @Override
  protected void playCraftSound(Player player) {
    if (isSoundReady(player)) {
      if (this.getInputCount() > 4) {
        SoundUtils.playSoundForAll(player, SoundEvents.ANVIL_USE, 0.4f, 0.9f + 0.1f * player.getRandom().nextFloat());
      } else {
        SoundUtils.playSoundForAll(player, Sounds.SAW.getSound(), 0.8f, 0.8f + 0.4f * player.getRandom().nextFloat());
      }
    }
  }


  public void setItemName(String name) {
    this.itemName = name;
    ItemStack result = craftingResult.getResult();
    if (!result.isEmpty()) {
      // if blank, set name to original name
      if (StringUtils.isBlank(name)) {
        // if the input was named, instead of clearing restore the old name
        ItemStack input = getItem(TINKER_SLOT);
        if (!input.isEmpty()) {
          name = TooltipUtil.getDisplayName(input);
        } else {
          // empty string will clear the stack tag
          name = "";
        }
      }
      TooltipUtil.setDisplayName(result, name);
    }
  }


  public void syncRecipe(Player player) {
    // must have a last recipe and a server level
    if (this.lastRecipe != null && this.level != null && !this.level.isClientSide && player instanceof ServerPlayer server) {
      Channel.sendToPlayer(new SUpdateTinkerStationRecipePacket(this.worldPosition, this.lastRecipe), server);
    }
  }

  public void updateRecipe(ITinkerStationRecipe recipe) {
    this.lastRecipe = recipe;
    this.craftingResult.clearContent();
  }



  @Override
  public ModelData getModelData() {
    // include material and texture, practically only one of the two should do anything
    return RetexturedHelper.getModelDataBuilder(texture).with(ModelProperties.MATERIAL, material).build();
  }

  @Override
  public void updateTexture(String name) {
    // reset material
    if (!name.isEmpty()) {
      this.material = IMaterial.UNKNOWN_ID;
    }
    super.updateTexture(name);
  }

  public void setMaterial(MaterialVariantId material) {
    MaterialVariantId oldMaterial = this.material;
    this.material = material;
    // reset other texture
    this.texture = Blocks.AIR;
    if (!oldMaterial.equals(material)) {
      setChangedFast();
      RetexturedHelper.onTextureUpdated(this);
    }
  }

  @Override
  public void saveSynced(CompoundTag tags) {
    super.saveSynced(tags);
    if (material != IMaterial.UNKNOWN_ID) {
      tags.putString(MATERIAL_TAG, material.toString());
    }
  }

  @Override
  public void load(CompoundTag tags) {
    super.load(tags);
    if (tags.contains(MATERIAL_TAG, Tag.TAG_STRING)) {
      material = Objects.requireNonNullElse(MaterialVariantId.tryParse(tags.getString(MATERIAL_TAG)), IMaterial.UNKNOWN_ID);
      RetexturedHelper.onTextureUpdated(this);
    }
  }

    @Nullable
    public Component getCurrentError() {
        return currentError;
    }
}

 */
