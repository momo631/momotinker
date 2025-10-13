package com.momosensei.momotinker.register;


import com.momosensei.momotinker.Momotinker;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class MomotinkerTags {
    public static void init() {
        Items.init();
    }
    public static class Items {
        private static void init() {
        }
        public static final TagKey<Item> TRIGGER_BLADE = ItemTags.create(new ResourceLocation(Momotinker.MOD_ID, "tag/trigger_blade"));
        public static final TagKey<Item> DIVINE_PUNISHMENT_SPEAR = ItemTags.create(new ResourceLocation(Momotinker.MOD_ID, "tag/divine_punishment_spear"));
        public static final TagKey<Item> ENTROPY_BURNING = ItemTags.create(new ResourceLocation(Momotinker.MOD_ID, "tag/entropy_burning"));
        public static final TagKey<Item> SMILE_EGO = ItemTags.create(new ResourceLocation(Momotinker.MOD_ID, "tag/smile_ego"));

    }
}
