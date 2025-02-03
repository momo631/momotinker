package com.momosensei.momotinker.register;

import com.momosensei.momotinker.Momotinker;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MomotinkerTab {

    public static Item.Properties material() {
        return new Item.Properties();
    }

    public static Item.Properties material(int stackSize) {
        return material().stacksTo(stackSize);
    }


    //makeIcon里面那个Itemstack是创造模式物品栏图标对的物品
    //label里面的会影响zh_cn里面的本地化键名
    //如果你要开新类型像我这样往下派生就行
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Momotinker.MOD_ID);
    public static final String EXAMPLE_MOD_TAB_STRING = "materials";
    public static final Supplier<CreativeModeTab> MATERIALS = CREATIVE_MODE_TABS.register("materials", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .title(Component.translatable(EXAMPLE_MOD_TAB_STRING))
            .icon(() -> MomotinkerItem.laomo.get().getDefaultInstance())
            .displayItems((pParameters, pOutput) -> {
                pOutput.accept(MomotinkerItem.laomo.get());
                pOutput.accept(MomotinkerItem.arriving_at_the_other_shore.get());
                pOutput.accept(MomotinkerItem.spirit_visage.get());
                pOutput.accept(MomotinkerItem.heartsteel.get());
                pOutput.accept(MomotinkerItem.gluttony_core.get());
                pOutput.accept(MomotinkerItem.greedy_contract.get());
                pOutput.accept(MomotinkerItem.lust_mirror.get());
                pOutput.accept(MomotinkerItem.arrogance_proof.get());
                pOutput.accept(MomotinkerItem.lazy_grail.get());
                pOutput.accept(MomotinkerItem.rage_stone_statue.get());
                pOutput.accept(MomotinkerItem.jealous_notes.get());
                pOutput.accept(MomotinkerItem.interdimensional_crystal.get());
                pOutput.accept(MomotinkerItem.dimensional_prism.get());

            }).build());

}