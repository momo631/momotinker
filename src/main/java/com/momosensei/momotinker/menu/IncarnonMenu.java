package com.momosensei.momotinker.menu;

import com.momosensei.momotinker.register.MomotinkerMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class IncarnonMenu extends AbstractContainerMenu {
    private ItemStack toolItem;
    public final Inventory playerInventory;
    public final int slotIndex;
    private final Map<Integer, Slot> customSlots = new HashMap<>();
    private String catalogue;
    private Map<String, Component> hoverTexts;
    private Component title;

    public IncarnonMenu(@Nullable MenuType<?> pMenuType, Inventory playerInventory, int pContainerId, ItemStack toolItem, int slotIndex, String catalogue, Map<String, Component> hoverTexts,Component title) {
        super(pMenuType, pContainerId);
        this.toolItem = toolItem;
        this.slotIndex = slotIndex;
        this.playerInventory = playerInventory;
        this.catalogue = catalogue;
        this.hoverTexts = hoverTexts;
        this.title = title;
        this.broadcastChanges();
    }

    public ItemStack getToolItem() {return toolItem;}

    public String getCatalogue() {
        return catalogue;
    }

    public Map<String, Component> getHoverTexts() {
        return hoverTexts;
    }

    public Component getTitle() {
        return title;
    }

    public void updateToolSlot(ItemStack toolItem){
        this.toolItem = toolItem;
    }

    public void updateCatalogue(String catalogue){
        this.catalogue = catalogue;
    }

    public void updateHoverTexts(Map<String, Component> hoverTexts){
        this.hoverTexts = hoverTexts;
    }

    public void updateTitle(Component title){
        this.title = title;
    }

    public IncarnonMenu(int id, Inventory inventory, FriendlyByteBuf buffer) {
        super(MomotinkerMenus.Incarnon_menu.get(), id);
        this.slotIndex = buffer.readVarInt();
        this.catalogue = buffer.readUtf();
        this.toolItem = inventory.getItem(this.slotIndex);
        this.playerInventory = inventory;
        this.hoverTexts = buffer.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readComponent);
        this.title = buffer.readComponent();
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    public Map<Integer, Slot> get() {
        return customSlots;
    }
    @Override
    public boolean stillValid(@NotNull Player player) {
        ItemStack currentItem = player.getInventory().getItem(this.slotIndex);
        if (currentItem != this.toolItem && ItemStack.matches(currentItem, this.toolItem)) {
            this.toolItem = currentItem;
        }
        return currentItem == this.toolItem;
    }

}
