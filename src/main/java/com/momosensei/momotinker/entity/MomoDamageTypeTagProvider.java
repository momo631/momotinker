package com.momosensei.momotinker.entity;

import com.momosensei.momotinker.Momotinker;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class MomoDamageTypeTagProvider extends DamageTypeTagsProvider {
    public MomoDamageTypeTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, lookup, Momotinker.MOD_ID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
        protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(DamageTypeTags.BYPASSES_ARMOR).add(MomoDamageTypes.OVER);
        tag(DamageTypeTags.BYPASSES_SHIELD).add(MomoDamageTypes.OVER);
        tag(DamageTypeTags.BYPASSES_INVULNERABILITY).add(MomoDamageTypes.OVER);
        tag(DamageTypeTags.BYPASSES_COOLDOWN).add(MomoDamageTypes.OVER);
        tag(DamageTypeTags.BYPASSES_EFFECTS).add(MomoDamageTypes.OVER);
        tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(MomoDamageTypes.OVER);
        tag(DamageTypeTags.BYPASSES_RESISTANCE).add(MomoDamageTypes.OVER);
        tag(DamageTypeTags.AVOIDS_GUARDIAN_THORNS).add(MomoDamageTypes.OVER);

    }
}