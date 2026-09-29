package com.momosensei.momotinker.test.testc;
/*
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.tools.layout.LayoutSlot;
import slimeknights.tconstruct.tables.block.entity.inventory.LazyResultContainer;

public class STinkerStationSlot extends Slot {
  private final LazyResultContainer craftResult;
  private LayoutSlot layout = null;
  public STinkerStationSlot(STinkerStationBlockEntity tile, int index, int xPosition, int yPosition) {
    super(tile, index, xPosition, yPosition);
    this.craftResult = tile.getCraftingResult();
  }

  public boolean isDormant() {
    return layout == null;
  }

  public void activate(LayoutSlot layout) {
    this.layout = layout;
  }

  public void deactivate() {
    this.layout = null;
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    // dormant slots don't take any items, they can only be taken out of
    return stack.isEmpty() || (layout != null && layout.isValid(stack));
  }

  @Override
  public void setChanged() {
    craftResult.clearContent();
    super.setChanged();
  }
}

 */
