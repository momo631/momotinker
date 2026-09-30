package com.momosensei.momotinker.register;


import com.momosensei.momotinker.Momotinker;
import net.minecraft.core.registries.Registries;
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

        public static final TagKey<Item> FROM_BRILLIANCE = local("frombrilliance");
        public static final TagKey<Item> NEED_KEY_TO_RUIN = local("need_key_to_ruin");
        public static final TagKey<Item> DIVINE_PUNISHMENT_SPEAR = local( "divine_punishment_spear");
        public static final TagKey<Item> ENTROPY_BURNING = local("entropy_burning");
        public static final TagKey<Item> POCKET_WATCH = local("pocket_watch");

        public static final TagKey<Item> NYARLATHOTEP = local("nyarlathotep");

    }
    private static TagKey<Item> local(String name) {
        return TagKey.create(Registries.ITEM, Momotinker.getResource(name));
    }
}
