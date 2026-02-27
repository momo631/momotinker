package com.momosensei.momotinker.gui.components;

import com.momosensei.momotinker.gui.screen.IncarnonScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public class IncarnonButton extends ImageButton {
    public final ItemStack toolItem;
    public final int slot;
    private final char variableType;
    private final int buttonValue;
    private boolean isActive = false;
    private final IncarnonScreen parentScreen;
    private final List<Component> hoverText;

    private final ResourceLocation normalTexture;
    private final ResourceLocation activeTexture;
    private final int textureWidth;
    private final int textureHeight;
    private final int[] uvs;

    public IncarnonButton(int x, int y, int width, int height,
                          ResourceLocation normalTexture, ResourceLocation activeTexture,
                          int textureWidth, int textureHeight,
                          int normalU, int normalV,
                          int hoverU, int hoverV,
                          Component message, ItemStack toolItem,
                          int slot, char variableType, int buttonValue,
                          IncarnonScreen parentScreen, List<Component> hoverText) {
        super(x, y, width, height, normalU, normalV, normalTexture, button -> {});

        this.toolItem = toolItem;
        this.slot = slot;
        this.variableType = variableType;
        this.buttonValue = buttonValue;
        this.parentScreen = parentScreen;
        this.hoverText = hoverText;

        this.normalTexture = normalTexture;
        this.activeTexture = activeTexture;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.uvs = new int[]{normalU, normalV, hoverU, hoverV};

        updateAppearance();
    }

    private IncarnonButton(int x, int y, int width, int height,
                           ResourceLocation normalTexture, ResourceLocation activeTexture,
                           int textureWidth, int textureHeight,
                           int normalU, int normalV,
                           int hoverUOffset, int hoverVOffset,
                           int activeUOffset, int activeVOffset,
                           Component message, ItemStack toolItem,
                           int slot, char variableType, int buttonValue,
                           IncarnonScreen parentScreen, List<Component> hoverText) {
        this(x, y, width, height, normalTexture, activeTexture, textureWidth, textureHeight,
                normalU, normalV,
                normalU + hoverUOffset, normalV + hoverVOffset,
                message, toolItem, slot, variableType, buttonValue, parentScreen, hoverText);
    }

    public static IncarnonButton create(int x, int y, int width, int height,
                                        ResourceLocation texture, int textureWidth, int textureHeight,
                                        int normalU, int normalV,
                                        int hoverUOffset, int hoverVOffset,
                                        int activeUOffset, int activeVOffset,
                                        Component message, ItemStack toolItem,
                                        int slot, char variableType, int buttonValue,
                                        IncarnonScreen parentScreen, List<Component> hoverText) {
        return new IncarnonButton(x, y, width, height, texture, texture,
                textureWidth, textureHeight,
                normalU, normalV, hoverUOffset, hoverVOffset, activeUOffset, activeVOffset,
                message, toolItem, slot, variableType, buttonValue, parentScreen, hoverText);
    }

    @Override
    public void onPress() {
        if (parentScreen != null) {
            parentScreen.handleButtonClick(this);
        }
    }

    public boolean isActive() {
        return this.isActive;
    }

    public char getVariableType() {
        return variableType;
    }

    public int getButtonValue() {
        return buttonValue;
    }

    public List<Component> getHoverText() {
        return hoverText;
    }

    public void setActive(boolean active) {
        boolean changed = this.isActive != active;
        this.isActive = active;
        if (changed) {
            updateAppearance();
        }
    }

    private void updateAppearance() {
        if (this.isActive) {
            this.setMessage(this.getMessage().copy()
                    .withStyle(ChatFormatting.GREEN)
                    .withStyle(ChatFormatting.BOLD));
        } else {
            this.setMessage(this.getMessage().copy()
                    .withStyle(ChatFormatting.WHITE));
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        ResourceLocation currentTexture;
        int u, v;

        if (this.isActive) {
            currentTexture = this.activeTexture;
            u = this.uvs[0];
            v = this.uvs[1];
        } else {
            currentTexture = this.normalTexture;
            if (this.isHoveredOrFocused()) {
                u = this.uvs[2];
                v = this.uvs[3];
            } else {
                u = this.uvs[0];
                v = this.uvs[1];
            }
        }

        guiGraphics.blit(currentTexture, this.getX(), this.getY(),
                u, v, this.width, this.height,
                this.textureWidth, this.textureHeight);

        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        if (this.isHovered && hoverText != null) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, hoverText, Optional.empty(),mouseX, mouseY);
        }
    }
}