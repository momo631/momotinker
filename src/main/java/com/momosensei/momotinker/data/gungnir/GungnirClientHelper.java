package com.momosensei.momotinker.data.gungnir;

import com.momosensei.momotinker.Items.tool.gungnir.gungnir;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class GungnirClientHelper {
    public static ItemStack resolveGungnirStack(LivingEntity entity) {
        CompoundTag nbt = entity.getPersistentData().getCompound(gungnir.its_gungnir_key);
        if (!nbt.isEmpty()) {
            ItemStack stack = ItemStack.of(nbt);
            if (!stack.isEmpty()) return stack;
        }
        ItemStack cached = GungnirItemCache.get(entity.getId());
        if (cached != null && !cached.isEmpty()) return cached;
        return ItemStack.EMPTY;
    }
}
