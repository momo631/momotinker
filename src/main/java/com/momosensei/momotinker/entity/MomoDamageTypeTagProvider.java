package com.momosensei.momotinker.entity;

import com.momosensei.momotinker.Momotinker;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.momosensei.momotinker.entity.MomoDamageTypes.OVER;
import static net.minecraft.tags.DamageTypeTags.*;

public class MomoDamageTypeTagProvider extends DamageTypeTagsProvider {
    public MomoDamageTypeTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, lookup, Momotinker.MOD_ID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
        protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(BYPASSES_ARMOR).add(OVER);
        tag(BYPASSES_SHIELD).add(OVER);
        tag(BYPASSES_INVULNERABILITY).add(OVER);
        tag(BYPASSES_COOLDOWN).add(OVER);
        tag(BYPASSES_EFFECTS).add(OVER);
        tag(BYPASSES_ENCHANTMENTS).add(OVER);
        tag(BYPASSES_RESISTANCE).add(OVER);
        tag(AVOIDS_GUARDIAN_THORNS).add(OVER);

    }
}