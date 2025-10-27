package com.momosensei.momotinker.register;


import com.momosensei.momotinker.tool.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.library.materials.RandomMaterial;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.helper.ModifierLootingHandler;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.utils.BlockSideHitListener;

import java.util.function.Consumer;
import java.util.function.Supplier;


public final class MomotinkerTools extends MomotinkerTables {
    public MomotinkerTools() {
        SlotType.init();
        BlockSideHitListener.init();
        ModifierLootingHandler.init();
        RandomMaterial.init();
    }

    public static final RegistryObject<CreativeModeTab> tabTools = CREATIVE_TABS.register(
            "tools", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.momotinker.tools"))
                    .icon(() -> MomotinkerTools.trigger_blade.get().getRenderTool())
                    .displayItems(MomotinkerTools::addTabItems)
                    .withTabsBefore(MomotinkerTables.tabTables.getId())
                    .withSearchBar()
                    .build());

    public static final ItemObject<ModifiableItem> trigger_blade = ITEMS.register("trigger_blade", () -> new trigger_blade(UNSTACKABLE_PROPS, MomotinkerToolDefinitions.TRIGGER_BLADE));
    public static final ItemObject<ModifiableItem> divine_punishment_spear = ITEMS.register("divine_punishment_spear", () -> new divine_punishment_spear(UNSTACKABLE_PROPS, MomotinkerToolDefinitions.DIVINE_PUNISHMENT_SPEAR));
    public static final ItemObject<ModifiableItem> entropy_burning_cube = ITEMS.register("entropy_burning_cube", () -> new entropy_burning_cube(UNSTACKABLE_PROPS, MomotinkerToolDefinitions.ENTROPY_BURNING_CUBE));
    public static final ItemObject<ModifiableItem> entropy_burning_sword = ITEMS.register("entropy_burning_sword", () -> new entropy_burning_sword(UNSTACKABLE_PROPS, MomotinkerToolDefinitions.ENTROPY_BURNING_SWORD));
    public static final ItemObject<ModifiableItem> entropy_burning_riding_spear = ITEMS.register("entropy_burning_riding_spear", () -> new entropy_burning_riding_spear(UNSTACKABLE_PROPS, MomotinkerToolDefinitions.ENTROPY_BURNING_RIDING_SPEAR));
    public static final ItemObject<ModifiableItem> entropy_burning_cannon = ITEMS.register("entropy_burning_cannon", () -> new entropy_burning_cannon(UNSTACKABLE_PROPS, MomotinkerToolDefinitions.ENTROPY_BURNING_CANNON));
    public static final ItemObject<ModifiableItem> eclipse_container = ITEMS.register("eclipse_container", () -> new eclipse_container(UNSTACKABLE_PROPS, MomotinkerToolDefinitions.ECLIPSE_CONTAINER));
    public static final ItemObject<ModifiableItem> coronal_key = ITEMS.register("coronal_key", () -> new coronal_key(UNSTACKABLE_PROPS, MomotinkerToolDefinitions.CORONAL_KEY));
    public static final ItemObject<ModifiableItem> moon_lock = ITEMS.register("moon_lock", () -> new moon_lock(UNSTACKABLE_PROPS, MomotinkerToolDefinitions.MOON_LOCK));
    public static final ItemObject<ModifiableItem> pocket_watch = ITEMS.register("pocket_watch", () -> new pocket_watch(UNSTACKABLE_PROPS, MomotinkerToolDefinitions.POCKET_WATCH));
    public static final ItemObject<ModifiableItem> chain_sword = ITEMS.register("chain_sword", () -> new chain_sword(UNSTACKABLE_PROPS, MomotinkerToolDefinitions.CHAIN_SWORD));
    public static final ItemObject<ModifiableItem> pneumatic_sword = ITEMS.register("pneumatic_sword", () -> new pneumatic_sword(UNSTACKABLE_PROPS, MomotinkerToolDefinitions.PNEUMATIC_SWORD));
    public static final ItemObject<ModifiableItem> legion = ITEMS.register("legion", () -> new legion(UNSTACKABLE_PROPS, MomotinkerToolDefinitions.LEGION));

    private static void addTabItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output tab) {
        Consumer<ItemStack> output = tab::accept;
        acceptTool(output, trigger_blade);
        acceptTool(output, divine_punishment_spear);
        acceptTool(output, entropy_burning_cube);
        acceptTool(output, entropy_burning_sword);
        acceptTool(output, entropy_burning_riding_spear);
        acceptTool(output, entropy_burning_cannon);
        acceptTool(output, eclipse_container);
        acceptTool(output, coronal_key);
        acceptTool(output, moon_lock);
        acceptTool(output, pocket_watch);
        acceptTool(output, chain_sword);
        acceptTool(output, pneumatic_sword);
        acceptTool(output, legion);

        tab.accept(MomotinkerItem.twilight_ego.get());
        tab.accept(MomotinkerItem.star_seeking_pointer.get());
    }
    private static void acceptTool(Consumer<ItemStack> output, Supplier<? extends IModifiable> tool) {
        ToolBuildHandler.addVariants(output, tool.get(), "");
    }
}
