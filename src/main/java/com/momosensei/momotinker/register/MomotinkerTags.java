package com.momosensei.momotinker.register;


import com.momosensei.momotinker.Momotinker;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class MomotinkerTags {
    public static final TagKey<Item> TRIGGER_BLADE = ItemTags.create(new ResourceLocation(Momotinker.MOD_ID,"modifiable/trigger_blade"));

}
