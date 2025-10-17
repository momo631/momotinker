package com.momosensei.momotinker.register;


import com.momosensei.momotinker.Momotinker;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class MomotinkerTags {
    public static void init() {
        Items.init();
    }
    public static class Items {
        private static void init() {
        }
        public static final TagKey<Item> TRIGGER_BLADE = local("trigger_blade");
        public static final TagKey<Item> DIVINE_PUNISHMENT_SPEAR = local( "divine_punishment_spear");
        public static final TagKey<Item> ENTROPY_BURNING = local("entropy_burning");

    }
    private static TagKey<Item> local(String name) {
        return TagKey.create(Registry.ITEM_REGISTRY, Momotinker.getResource(name));
    }
}
