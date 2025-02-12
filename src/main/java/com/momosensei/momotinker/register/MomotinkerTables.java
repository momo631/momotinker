package com.momosensei.momotinker.register;

import com.momosensei.momotinker.Momotinker;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

public class MomotinkerTables extends MomotinkerMod {
    public static final RegistryObject<CreativeModeTab> tabTables = CREATIVE_TABS.register(
            "materials", () -> CreativeModeTab.builder().title(Momotinker.makeTranslation("itemGroup", "materials"))
                    .icon(() -> new ItemStack(MomotinkerItem.laomo.get()))
                    .displayItems(MomotinkerTables::addTabItems)
                    .build());

    public static Item.Properties material() {
        return new Item.Properties();
    }

    public static Item.Properties material(int stackSize) {
        return material().stacksTo(stackSize);
    }

    private static void addTabItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
        output.accept(MomotinkerItem.laomo.get());
        output.accept(MomotinkerItem.arriving_at_the_other_shore.get());
        output.accept(MomotinkerItem.spirit_visage.get());
        output.accept(MomotinkerItem.heartsteel.get());
        output.accept(MomotinkerItem.gluttony_core.get());
        output.accept(MomotinkerItem.greedy_contract.get());
        output.accept(MomotinkerItem.lust_mirror.get());
        output.accept(MomotinkerItem.arrogance_proof.get());
        output.accept(MomotinkerItem.lazy_grail.get());
        output.accept(MomotinkerItem.rage_stone_statue.get());
        output.accept(MomotinkerItem.jealous_notes.get());
        output.accept(MomotinkerItem.interdimensional_crystal.get());
        output.accept(MomotinkerItem.dimensional_prism.get());
        output.accept(MomotinkerItem.trigger_slash_a.get());
    }
}