package com.momosensei.momotinker.test.testa;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;

import java.util.UUID;

public class ChunkBlueprintItem {

    public static ItemStack createBlueprintItem(UUID fileId, ChunkPos chunkPos) {
        ItemStack blueprint = new ItemStack(Items.PAPER);
        CompoundTag tag = blueprint.getOrCreateTag();

        tag.putUUID("blueprintId", fileId);
        tag.putInt("chunkX", chunkPos.x);
        tag.putInt("chunkZ", chunkPos.z);

        blueprint.setHoverName(Component.literal("区块蓝图 [" + chunkPos.x + ", " + chunkPos.z + "]"));

        return blueprint;
    }

    public static boolean isBlueprintItem(ItemStack stack) {
        return stack.hasTag() && stack.getTag().hasUUID("blueprintId");
    }

    public static UUID getBlueprintFileId(ItemStack stack) {
        if (!isBlueprintItem(stack)) return null;
        return stack.getTag().getUUID("blueprintId");
    }
}