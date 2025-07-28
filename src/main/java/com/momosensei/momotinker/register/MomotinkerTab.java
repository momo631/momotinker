package com.momosensei.momotinker.register;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class MomotinkerTab {
    public static final CreativeModeTab MATERIALS = new CreativeModeTab("momotinker.materials") {
        @Override
        public  ItemStack makeIcon() {
            return new ItemStack(MomotinkerItem.laomo.get());
        }
    };

    public static final CreativeModeTab TOOLS = new CreativeModeTab("momotinker.tools") {
        @Override
        public ItemStack makeIcon() {
            return MomotinkerItem.eclipse_container.get().getRenderTool();
        }
    };
/*
    public static final CreativeModeTab BLOCKS = new CreativeModeTab("momotinker.blocks") {
        @Override
        public  ItemStack makeIcon() {
            return new ItemStack(MomotinkerItem.Laomo_block.get());
        }
    };
    public MomotinkerTab(){}
*/
}
