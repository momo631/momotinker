package com.momosensei.momotinker.Items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class meteor_nucleus_block extends BlockItem {
    public meteor_nucleus_block(Block block, Properties properties) {
        super(block, properties);
    }
    @Override
    public boolean isFireResistant() {
        return true;
    }


}