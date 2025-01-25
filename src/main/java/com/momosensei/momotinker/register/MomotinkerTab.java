package com.momosensei.momotinker.register;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class MomotinkerTab {

    //makeIcon里面那个Itemstack是创造模式物品栏图标对的物品
    //label里面的会影响zh_cn里面的本地化键名
    //如果你要开新类型像我这样往下派生就行



    public static final CreativeModeTab MATERIALS = new CreativeModeTab("momotinker.materials") {
        @Override
        public  ItemStack makeIcon() {
            return new ItemStack(MomotinkerItem.laomo.get());
        }
    };



    public static final CreativeModeTab BLOCKS = new CreativeModeTab("momotinker.blocks") {
        @Override
        public  ItemStack makeIcon() {
            return new ItemStack(MomotinkerItem.Laomo_block.get());
        }
    };
    public MomotinkerTab(){}
}
