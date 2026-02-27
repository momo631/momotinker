package com.momosensei.momotinker.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.gui.components.IncarnonButton;
import com.momosensei.momotinker.menu.IncarnonMenu;
import com.momosensei.momotinker.network.Channel;
import com.momosensei.momotinker.network.packet.IncarnonAdjustPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.momosensei.momotinker.Modifiers.modifiers.IncarnonModifiers.IncarnonModifier.*;

@OnlyIn(Dist.CLIENT)
public class IncarnonScreen extends AbstractContainerScreen<IncarnonMenu> {
    public final ItemStack toolItem;
    public final int slot;
    public static final ResourceLocation TEXTURE = new ResourceLocation(Momotinker.MOD_ID, "/textures/gui/menu/incarnon_bg.png");
    public final String catalogue;
    public final Map<String, Component> hoverTexts;
    public final Component title;

    private int currentPhase = 0;

    private int valueA = 0;
    private int valueB = 0;
    private int valueC = 0;
    private int valueD = 0;

    private final List<IncarnonButton> buttonsA = new ArrayList<>();
    private final List<IncarnonButton> buttonsB = new ArrayList<>();
    private final List<IncarnonButton> buttonsC = new ArrayList<>();
    private final List<IncarnonButton> buttonsD = new ArrayList<>();

    public IncarnonScreen(IncarnonMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.toolItem = menu.getToolItem();
        this.catalogue = menu.getCatalogue();
        this.hoverTexts = menu.getHoverTexts();
        this.slot = menu.slotIndex;
        this.title = menu.getTitle();
        this.imageWidth = 560;
        this.imageHeight = 262;

        loadValuesFromTool();
        currentPhase = loadIntFromTool(incarnon_phase);
    }
    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }
    private void loadValuesFromTool() {
        if (this.toolItem.isEmpty()) {
            return;
        }

        ModDataNBT tag = ToolStack.from(this.toolItem).getPersistentData();
        valueA = tag.getInt(incarnon_a);
        valueB = tag.getInt(incarnon_b);
        valueC = tag.getInt(incarnon_c);
        valueD = tag.getInt(incarnon_d);
    }

    private int loadIntFromTool(ResourceLocation resourceLocation) {
        if (this.toolItem.isEmpty()) {
            return 0;
        }
        ModDataNBT tag = ToolStack.from(this.toolItem).getPersistentData();
        return tag.getInt(resourceLocation);
    }

    private record ButtonConfig(
            int x, int y, int width, int height,
            char varType, int value,
            String hoverText,
            String normalTexture,
            String activeTexture,
            String catalogue,
            Map<String, Component> hoverTexts
    ) {
        public ResourceLocation getNormalTexture() {
            return new ResourceLocation(Momotinker.MOD_ID,
                    "/textures/gui/buttons/" + catalogue + "/" + normalTexture);
        }

        public ResourceLocation getActiveTexture() {
            return new ResourceLocation(Momotinker.MOD_ID,
                    "/textures/gui/buttons/" + catalogue + "/" + activeTexture);
        }

        public Component getHoverText() {
            if (hoverTexts != null && hoverText != null) {
                return hoverTexts.getOrDefault(hoverText, Component.empty());
            }
            return Component.empty();
        }
        public List<Component> getHoverTextList() {
            List<Component> result = new ArrayList<>();
            if (hoverTexts != null && hoverText != null) {
                Component component = hoverTexts.get(hoverText);
                if (component != null) {
                    String text = component.getString();
                    Style style = component.getStyle();
                    if (text.contains("\n")) {
                        String[] lines = text.split("\n");
                        for (String line : lines) {
                            if (!line.trim().isEmpty()) {
                                result.add(Component.literal(line.trim()).withStyle(style));
                            }
                        }
                    } else {
                        result.add(component);
                    }
                }
            }
            return result;
        }
    }
    public Component getOtherHoverText(String hoverText, Map<String, Component> hoverTexts){
        if (hoverTexts != null && hoverText != null) {
            return hoverTexts.getOrDefault(hoverText, Component.empty());
        }
        return Component.empty();
    }
    @Override
    protected void init() {
        super.init();
        clearButtonLists();

        int guiLeft = this.leftPos;
        int guiTop = this.topPos;

        List<ButtonConfig> phase1Buttons = List.of(
                new ButtonConfig(guiLeft + 16, guiTop + 50, 32, 32, 'A', 1,
                        "phase_1_ability_1", "phase_1/ability_1_off.png", "phase_1/ability_1_on.png",this.catalogue,this.hoverTexts),
                new ButtonConfig(guiLeft + 52, guiTop + 50, 32, 32, 'A', 2,
                        "phase_1_ability_2", "phase_1/ability_2_off.png", "phase_1/ability_2_on.png",this.catalogue,this.hoverTexts)
        );

        List<ButtonConfig> phase2Buttons = List.of(
                new ButtonConfig(guiLeft + 106, guiTop + 190, 32, 32, 'B', 1,
                        "phase_2_ability_1", "phase_2/ability_1_off.png", "phase_2/ability_1_on.png",this.catalogue,this.hoverTexts),
                new ButtonConfig(guiLeft + 142, guiTop + 190, 32, 32, 'B', 2,
                        "phase_2_ability_2", "phase_2/ability_2_off.png", "phase_2/ability_2_on.png",this.catalogue,this.hoverTexts)
        );

        List<ButtonConfig> phase3Buttons = List.of(
                new ButtonConfig(guiLeft + 206, guiTop + 20, 32, 32, 'C', 1,
                        "phase_3_ability_1", "phase_3/ability_1_off.png", "phase_3/ability_1_on.png",this.catalogue,this.hoverTexts),
                new ButtonConfig(guiLeft + 242, guiTop + 20, 32, 32, 'C', 2,
                        "phase_3_ability_2", "phase_3/ability_2_off.png", "phase_3/ability_2_on.png",this.catalogue,this.hoverTexts)
        );

        List<ButtonConfig> phase4Buttons = List.of(
                new ButtonConfig(guiLeft + 310, guiTop + 158, 32, 32, 'D', 1,
                        "phase_4_ability_1", "phase_4/ability_1_off.png", "phase_4/ability_1_on.png",this.catalogue,this.hoverTexts),
                new ButtonConfig(guiLeft + 346, guiTop + 158, 32, 32, 'D', 2,
                        "phase_4_ability_2", "phase_4/ability_2_off.png", "phase_4/ability_2_on.png",this.catalogue,this.hoverTexts),
                new ButtonConfig(guiLeft + 382, guiTop + 158, 32, 32, 'D', 3,
                        "phase_4_ability_3", "phase_4/ability_3_off.png", "phase_4/ability_3_on.png",this.catalogue,this.hoverTexts)
        );

        if (currentPhase >= 1) createButtonsFromConfigs(phase1Buttons);
        if (currentPhase >= 2) createButtonsFromConfigs(phase2Buttons);
        if (currentPhase >= 3) createButtonsFromConfigs(phase3Buttons);
        if (currentPhase >= 4) createButtonsFromConfigs(phase4Buttons);

        initializeButtonStates();
        updateButtonAvailability();
    }

    private void createButtonsFromConfigs(List<ButtonConfig> configs) {
        for (ButtonConfig config : configs) {
            createButtonFromConfig(config);
        }
    }

    private void createButtonFromConfig(ButtonConfig config) {
        createButtonWithTexture(
                config.x(), config.y(), config.width(), config.height(),
                Component.literal(""),
                config.varType(), config.value(),
                config.getHoverTextList(),
                config.getNormalTexture(),
                config.getActiveTexture(),
                config.width(), config.height()
        );
    }

    private void createButtonWithTexture(int x, int y, int width, int height,
                                         Component text, char varType, int value,
                                         List<Component> hoverText,
                                         ResourceLocation normalTexture,
                                         ResourceLocation activeTexture,
                                         int textureWidth, int textureHeight) {
        IncarnonButton button = new IncarnonButton(
                x, y, width, height, normalTexture, activeTexture, 
                textureWidth, textureHeight, 0, 0, 0, 0,    
                text, this.toolItem, this.slot, varType, value, this, hoverText
        );
        addButtonToGroup(button, varType);
    }
    private void addButtonToGroup(IncarnonButton button, char varType) {
        switch (varType) {
            case 'A' -> buttonsA.add(button);
            case 'B' -> buttonsB.add(button);
            case 'C' -> buttonsC.add(button);
            case 'D' -> buttonsD.add(button);
        }
        this.addRenderableWidget(button);
    }
    @Override
    public void onClose() {
        Channel.sendToServer(new IncarnonAdjustPacket(this.slot, valueA, valueB, valueC, valueD));
        super.onClose();
    }

    private void clearButtonLists() {
        buttonsA.clear();
        buttonsB.clear();
        buttonsC.clear();
        buttonsD.clear();
    }

    private void updateButtonAvailability() {
        setButtonGroupEnabled(buttonsA, currentPhase >= 1);
        setButtonGroupEnabled(buttonsB, currentPhase >= 2);
        setButtonGroupEnabled(buttonsC, currentPhase >= 3);
        setButtonGroupEnabled(buttonsD, currentPhase >= 4);
    }

    private void setButtonGroupEnabled(List<IncarnonButton> buttons, boolean enabled) {
        for (IncarnonButton button : buttons) {
            button.active = enabled;
            button.visible = enabled;
            if (!enabled) {
                button.setMessage(button.getMessage().copy().withStyle(ChatFormatting.DARK_GRAY));
            }
        }
    }

    public void handleButtonClick(IncarnonButton clickedButton) {
        if (!clickedButton.active) {
            return;
        }
        char varType = clickedButton.getVariableType();
        boolean wasActive = clickedButton.isActive();
        List<IncarnonButton> buttonGroup = getButtonGroup(varType);
        if (wasActive) {
            for (IncarnonButton button : buttonGroup) {
                button.setActive(false);
            }
            updateVariableValue(varType, 0);
        } else {
            for (IncarnonButton button : buttonGroup) {
                button.setActive(button == clickedButton);
            }
            updateVariableValue(varType, clickedButton.getButtonValue());
        }

        applyValueToTool(varType);
        Channel.sendToServer(new IncarnonAdjustPacket(this.slot, valueA, valueB, valueC, valueD));
    }

    private void initializeButtonStates() {
        updateButtonGroupState('A', valueA, buttonsA);
        updateButtonGroupState('B', valueB, buttonsB);
        updateButtonGroupState('C', valueC, buttonsC);
        updateButtonGroupState('D', valueD, buttonsD);
    }

    private void updateButtonGroupState(char varType, int value, List<IncarnonButton> buttons) {
        if (buttons.isEmpty()) return;

        boolean foundActive = false;

        if (Math.abs(value) < 0.01f) {
            for (IncarnonButton button : buttons) {
                button.setActive(false);
            }
            return;
        }
        for (IncarnonButton button : buttons) {
            if (Math.abs(button.getButtonValue() - value) < 0.01f) {
                button.setActive(true);
                foundActive = true;
            } else {
                button.setActive(false);
            }
        }
        if (!foundActive && Math.abs(value) > 0.01f) {
            for (IncarnonButton button : buttons) {
                button.setActive(false);
            }
            updateVariableValue(varType, 0);
        }
    }

    private void updateVariableValue(char varType, int value) {
        switch (varType) {
            case 'A' -> valueA = value;
            case 'B' -> valueB = value;
            case 'C' -> valueC = value;
            case 'D' -> valueD = value;
        }
    }

    private void applyValueToTool(char varType) {
        if (this.toolItem.isEmpty()) {
            return;
        }

        ToolStack toolStack = ToolStack.from(this.toolItem);
        ModDataNBT tag = toolStack.getPersistentData();

        ResourceLocation key = switch (varType) {
            case 'B' -> incarnon_b;
            case 'C' -> incarnon_c;
            case 'D' -> incarnon_d;
            default -> incarnon_a;
        };

        int value = getVariableValue(varType);
        if (Math.abs(value) > 0.01f) {
            tag.putInt(key, value);
        } else {
            tag.remove(key);
        }

        toolStack.rebuildStats();
    }

    private int getVariableValue(char varType) {
        return switch (varType) {
            case 'A' -> valueA;
            case 'B' -> valueB;
            case 'C' -> valueC;
            case 'D' -> valueD;
            default -> 0;
        };
    }

    private List<IncarnonButton> getButtonGroup(char varType) {
        return switch (varType) {
            case 'A' -> buttonsA;
            case 'B' -> buttonsB;
            case 'C' -> buttonsC;
            case 'D' -> buttonsD;
            default -> new ArrayList<>();
        };
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
        renderCurrentSettings(guiGraphics);
    }

    private void renderCurrentSettings(GuiGraphics guiGraphics) {
        int screenWidth = this.width;
        int screenHeight = this.height;
        int textureWidth = 560;
        int textureHeight = 262;
        int x = (screenWidth - textureWidth) / 2;
        int y = (screenHeight - textureHeight) / 2;

        int titleX = x + 20;
        int titleY = y + 13;
        drawScaledText(guiGraphics, this.title.copy().withStyle(ChatFormatting.ITALIC,ChatFormatting.BOLD,ChatFormatting.AQUA), titleX, titleY, 2f, 0xFFFFFF);

        renderProgressBar(guiGraphics, x + 6, y + 24);

        int textX = x + 442;
        int textY = y + 24;
        if (!this.toolItem.isEmpty()) {
            List<Component> displays = getInfoList(ToolStack.from(this.toolItem));
            int fontHeight = this.font.lineHeight;
            for (Component component : displays) {
                int color = component.getStyle().getColor() != null ? component.getStyle().getColor().getValue() : 0xFFFFFF;
                guiGraphics.drawString(this.font, component, textX, textY, color);
                textY += fontHeight + 2;
            }
        }
    }
    private void drawScaledText(GuiGraphics guiGraphics, Component text, int x, int y, float scale, int color) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        poseStack.scale(scale, scale, 1.0f);
        guiGraphics.drawString(this.font, text, 0, 0, color, false);
        poseStack.popPose();
    }
    private void renderProgressBar(GuiGraphics guiGraphics, int x, int y) {
        int progressWidth = 128;
        int progressHeight = 32;
        int displayPhase = Mth.clamp(currentPhase, 0, 4);
        ResourceLocation phaseTexture = getPhaseTexture(displayPhase);
        guiGraphics.blit(phaseTexture, x, y, 0, 0, progressWidth, progressHeight, progressWidth, progressHeight);
    }

    private ResourceLocation getPhaseTexture(int phase) {
        return switch (phase) {
            case 1 -> new ResourceLocation(Momotinker.MOD_ID, "/textures/gui/overlay/incarnon/progress_1.png");
            case 2 -> new ResourceLocation(Momotinker.MOD_ID, "/textures/gui/overlay/incarnon/progress_2.png");
            case 3 -> new ResourceLocation(Momotinker.MOD_ID, "/textures/gui/overlay/incarnon/progress_3.png");
            case 4 -> new ResourceLocation(Momotinker.MOD_ID, "/textures/gui/overlay/incarnon/progress_4.png");
            default -> new ResourceLocation(Momotinker.MOD_ID, "/textures/gui/overlay/incarnon/progress_0.png");
        };
    }
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int screenWidth = this.width;
        int screenHeight = this.height;
        int textureWidth = 560;
        int textureHeight = 262;
        int x = (screenWidth - textureWidth) / 2;
        int y = (screenHeight - textureHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);

        int box1X = x + 8;
        int box1Y = y + 8;
        int box2X = x + 430;
        int box2Y = y + 8;
        renderStarryBox(guiGraphics, box1X, box1Y, 413, 246);
        renderStarryBox(guiGraphics, box2X, box2Y, 122, 246);
        if (!this.toolItem.isEmpty()) {
            renderRotatedItem(guiGraphics, this.toolItem, x+200, y+130, -135f, 10f);
        }
    }

    private void renderStarryBox(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();

        RenderType endGatewayRenderType = RenderType.endGateway();
        endGatewayRenderType.setupRenderState();

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        bufferBuilder.vertex(poseStack.last().pose(), x, y + height, 0).color(255, 255, 255, 255).endVertex();
        bufferBuilder.vertex(poseStack.last().pose(), x + width, y + height, 0).color(255, 255, 255, 255).endVertex();
        bufferBuilder.vertex(poseStack.last().pose(), x + width, y, 0).color(255, 255, 255, 255).endVertex();
        bufferBuilder.vertex(poseStack.last().pose(), x, y, 0).color(255, 255, 255, 255).endVertex();

        tessellator.end();
        endGatewayRenderType.clearRenderState();

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        poseStack.popPose();
    }
    private void renderRotatedItem(GuiGraphics guiGraphics, ItemStack stack, int x, int y, float rotationDegrees, float scale) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(x, y, 100);
        poseStack.scale(scale, scale, 1.0f);
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotationDegrees));
        poseStack.translate(-8, -8, 0);
        guiGraphics.renderItem(stack, 0, 0);
        poseStack.popPose();
    }
    public List<Component> getInfoList(IToolStackView tool) {
        List<Component> list = new ArrayList<>();
        list.add(Component.translatable("modifier.momotinker.incarnon_on").withStyle(ChatFormatting.AQUA));
        list.add(getOtherHoverText("incarnon_on",this.hoverTexts).copy().withStyle(ChatFormatting.AQUA));

        if (this.currentPhase<4){
            list.add(Component.translatable("modifier.momotinker.incarnon_task").withStyle(ChatFormatting.DARK_AQUA));
            list.add(getPhaseText(this.currentPhase).copy().withStyle(ChatFormatting.DARK_AQUA));
            int task_phase = loadIntFromTool(incarnon_task_phase);
            int task_goal = loadIntFromTool(incarnon_task_goal);
            list.add(Component.translatable("modifier.momotinker.incarnon_task0").append(task_phase+"/"+task_goal).withStyle(ChatFormatting.DARK_AQUA));
        }
        if (tool.hasTag(TinkerTags.Items.DURABILITY)) {
            list.add(Component.translatable(ToolStats.DURABILITY.getTranslationKey()).append(String.format(": %.0f", tool.getStats().get(ToolStats.DURABILITY))).withStyle(ChatFormatting.GREEN));
        }
        if (tool.hasTag(TinkerTags.Items.MELEE)) {
            list.add(Component.translatable(ToolStats.ATTACK_DAMAGE.getTranslationKey()).append(String.format(": %.2f", tool.getStats().get(ToolStats.ATTACK_DAMAGE))).withStyle(ChatFormatting.DARK_RED));
            list.add(Component.translatable(ToolStats.ATTACK_SPEED.getTranslationKey()).append(String.format(": %.2f", tool.getStats().get(ToolStats.ATTACK_SPEED))).withStyle(ChatFormatting.DARK_RED));
        }
        if (tool.hasTag(TinkerTags.Items.HARVEST)) {
            list.add(Component.translatable(ToolStats.MINING_SPEED.getTranslationKey()).append(String.format(": %.2f", tool.getStats().get(ToolStats.MINING_SPEED))).withStyle(ChatFormatting.LIGHT_PURPLE));
            list.add(Component.translatable(ToolStats.HARVEST_TIER.getTranslationKey()).append(String.format(":" + tool.getStats().get(ToolStats.HARVEST_TIER))).withStyle(ChatFormatting.LIGHT_PURPLE));
        }
        if (tool.hasTag(TinkerTags.Items.RANGED)) {
            list.add(Component.translatable(ToolStats.PROJECTILE_DAMAGE.getTranslationKey()).append(String.format(": %.2f", tool.getStats().get(ToolStats.PROJECTILE_DAMAGE))).withStyle(ChatFormatting.GOLD));
            list.add(Component.translatable(ToolStats.DRAW_SPEED.getTranslationKey()).append(String.format(": %.2f", tool.getStats().get(ToolStats.DRAW_SPEED))).withStyle(ChatFormatting.GOLD));
            list.add(Component.translatable(ToolStats.ACCURACY.getTranslationKey()).append(String.format(": %.2f", tool.getStats().get(ToolStats.ACCURACY))).withStyle(ChatFormatting.GOLD));
        }
        if (tool.hasTag(TinkerTags.Items.ARMOR)) {
            list.add(Component.translatable(ToolStats.ARMOR.getTranslationKey()).append(String.format(": %.2f", tool.getStats().get(ToolStats.ARMOR))).withStyle(ChatFormatting.BLUE));
            list.add(Component.translatable(ToolStats.ARMOR_TOUGHNESS.getTranslationKey()).append(String.format(": %.2f", tool.getStats().get(ToolStats.ARMOR_TOUGHNESS))).withStyle(ChatFormatting.BLUE));
            list.add(Component.translatable(ToolStats.KNOCKBACK_RESISTANCE.getTranslationKey()).append(String.format(": %.2f", tool.getStats().get(ToolStats.KNOCKBACK_RESISTANCE))).withStyle(ChatFormatting.BLUE));
        }
        List<Component> finalList = new ArrayList<>();
        for (Component comp : list) {
            String text = comp.getString();
            Style style = comp.getStyle();
            String[] lines = text.split("\n");
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    finalList.add(Component.literal(line.trim()).withStyle(style));
                }
            }
        }
        return finalList;
    }
    private Component getPhaseText(int phase) {
        return switch (phase) {
            case 1 ->getOtherHoverText("phase_task_2",this.hoverTexts);
            case 2 ->getOtherHoverText("phase_task_3",this.hoverTexts);
            case 3 ->getOtherHoverText("phase_task_4",this.hoverTexts);
            default -> getOtherHoverText("phase_task_1",this.hoverTexts);
        };
    }
}
