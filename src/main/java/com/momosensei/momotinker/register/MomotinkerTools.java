package com.momosensei.momotinker.register;


import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.tool.trigger_blade;
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
            "tools", () -> CreativeModeTab.builder().title(Momotinker.makeTranslation("itemGroup", "tools"))
                    .icon(() -> MomotinkerTools.trigger_blade.get().getRenderTool())
                    .displayItems(MomotinkerTools::addTabItems)
                    .withTabsBefore(MomotinkerTables.tabTables.getId())
                    .withSearchBar()
                    .build());

    public static final ItemObject<ModifiableItem> trigger_blade = ITEMS.register("trigger_blade", () -> new trigger_blade(UNSTACKABLE_PROPS, MomotinkerToolDefinitions.TRIGGER_BLADE));

    private static void addTabItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output tab) {
        Consumer<ItemStack> output = tab::accept;
        acceptTool(output, trigger_blade);

    }
    private static void acceptTool(Consumer<ItemStack> output, Supplier<? extends IModifiable> tool) {
        ToolBuildHandler.addVariants(output, tool.get(), "");
    }
}
