package com.momosensei.momotinker.test.testc;
/*
import net.minecraft.client.gui.components.Button;
import slimeknights.tconstruct.library.client.Icons;
import slimeknights.tconstruct.library.tools.layout.StationSlotLayout;
import slimeknights.tconstruct.tables.client.inventory.widget.SideButtonsWidgetPaged;

import java.util.List;

public class STinkerStationButtonsWidget extends SideButtonsWidgetPaged<SSlotButtonItem> {

  public static final int WOOD_STYLE = 2;
  public static final int METAL_STYLE = 1;

  public STinkerStationButtonsWidget(STinkerStationScreen parent, int leftPos, int topPos, List<StationSlotLayout> layouts, int style) {
    super(parent, leftPos, topPos, STinkerStationScreen.COLUMN_COUNT, rowsForCount(STinkerStationScreen.COLUMN_COUNT, layouts.size()),
      SSlotButtonItem.WIDTH, SSlotButtonItem.HEIGHT);

    // Logic to run when a button is pressed
    Button.OnPress onButtonPressed = self -> {
      for (SSlotButtonItem button : STinkerStationButtonsWidget.this.buttons) {
        button.pressed = false;
      }
      if (self instanceof SSlotButtonItem slotInformationButton) {
        slotInformationButton.pressed = true;
        parent.onToolSelection(slotInformationButton.getLayout());
      }
    };

    // create buttons for layouts
    for (int index = 0; index < layouts.size(); index++) {
      StationSlotLayout layout = layouts.get(index);

      SSlotButtonItem SSlotButtonItem = new SSlotButtonItem(index, -1, -1, layout, onButtonPressed);
      this.addInfoButton(SSlotButtonItem, style);
      if (layout == parent.getCurrentLayout()) {
        SSlotButtonItem.pressed = true;
      }
    }

    this.setButtonPositions();
  }

  private void addInfoButton(SSlotButtonItem SSlotButtonItem, int style) {
    SSlotButtonItem.setGraphics(Icons.BUTTON.shift(0, -18 * style),
      Icons.BUTTON_HOVERED.shift(0, -18 * style),
      Icons.BUTTON_PRESSED.shift(0, -18 * style));
    this.buttons.add(SSlotButtonItem);
  }

  public List<SSlotButtonItem> getButtons() {
    return this.buttons;
  }

  public static int width(int columns) {
    return size(columns, SSlotButtonItem.WIDTH);
  }
}

 */
