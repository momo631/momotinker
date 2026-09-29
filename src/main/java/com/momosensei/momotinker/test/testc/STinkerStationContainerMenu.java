package com.momosensei.momotinker.test.testc;
/*
import com.momosensei.momotinker.register.MomotinkerMenus;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.tools.layout.LayoutSlot;
import slimeknights.tconstruct.library.tools.layout.StationSlotLayout;
import slimeknights.tconstruct.library.tools.layout.StationSlotLayoutLoader;
import slimeknights.tconstruct.tables.menu.TabbedContainerMenu;
import slimeknights.tconstruct.tables.menu.slot.ArmorSlot;
import slimeknights.tconstruct.tables.menu.slot.LazyResultSlot;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class STinkerStationContainerMenu extends TabbedContainerMenu<STinkerStationBlockEntity> {

  private final List<Slot> inputSlots;
  private final LazyResultSlot resultSlot;

  @SuppressWarnings("deprecation")
  public STinkerStationContainerMenu(int id, Inventory inv, @Nullable STinkerStationBlockEntity tile) {
    super(MomotinkerMenus.special_tinker_station.get(), id, inv, tile);

    // unfortunately, nothing works with no tile
    if (tile != null) {
      tile.setItemName("");
      // send the player the current recipe, as we only sync to open containers
      tile.syncRecipe(inv.player);

      inputSlots = new ArrayList<>();
      this.addSlot(new STinkerStationSlot(tile, STinkerStationBlockEntity.TINKER_SLOT, 0, 0));

      for (int index = 0; index < tile.getContainerSize() - 1; index++) {
        inputSlots.add(this.addSlot(new STinkerStationSlot(tile, index + STinkerStationBlockEntity.INPUT_SLOT, 0, 0)));
      }

      // add result slot, will fetch result cache
      this.addSlot(this.resultSlot = new LazyResultSlot(tile.getCraftingResult(), 114, 38));
      // set initial slot filters and activations
      setToolSelection(StationSlotLayoutLoader.getInstance().get(BuiltInRegistries.BLOCK.getKey(tile.getBlockState().getBlock())));
    }
    else {
      // requirement for final variable
      this.resultSlot = null;
      this.inputSlots = Collections.emptyList();
    }

    // add armor and offhand slots, for convenience
    for (ArmorItem.Type slotType : ArmorItem.Type.values()) {
      this.addSlot(new ArmorSlot(inv, slotType.getSlot(), 152, 20 + slotType.ordinal() * 18));
    }
    this.addSlot(new Slot(inv, 40, 132, 74).setBackground(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD));

    this.addInventorySlots();
  }

  public STinkerStationContainerMenu(int id, Inventory inv, FriendlyByteBuf buf) {
    this(id, inv, getTileEntityFromBuf(buf, STinkerStationBlockEntity.class));
  }

  @Override
  protected int getInventoryYOffset() {
    return 102;
  }

  @Override
  public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
    return slot != this.resultSlot && super.canTakeItemForPickAll(stack, slot);
  }

  public void setToolSelection(StationSlotLayout layout) {
    assert this.tile != null;
    int maxSize = tile.getContainerSize();
    for (int i = 0; i < maxSize; i++) {
      Slot slot = this.slots.get(i);
      if (slot instanceof STinkerStationSlot slotToolPart) {
        // activate or deactivate the slots, sets the filters
        LayoutSlot layoutSlot = layout.getSlot(i);
        if (layoutSlot.isHidden()) {
          slotToolPart.deactivate();
        }
        else {
          slotToolPart.activate(layoutSlot);
        }
      }
    }
  }

  public List<Slot> getInputSlots() {
    return inputSlots;
  }

}

 */
