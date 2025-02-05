package com.momosensei.momotinker.entity;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class PenetrateDamage extends TagsProvider<DamageType> {
    protected PenetrateDamage(PackOutput output, ResourceKey<? extends Registry<DamageType>> resourceKey, CompletableFuture<HolderLookup.Provider> providerCompletableFuture, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, resourceKey, providerCompletableFuture, modId, existingFileHelper);
    }

    @Override
    public void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(DamageTypeTags.BYPASSES_ARMOR).add();
        this.tag(DamageTypeTags.BYPASSES_SHIELD).add();
        this.tag(DamageTypeTags.BYPASSES_INVULNERABILITY).add();
        this.tag(DamageTypeTags.BYPASSES_COOLDOWN).add();
        this.tag(DamageTypeTags.BYPASSES_EFFECTS).add();
        this.tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add();
        this.tag(DamageTypeTags.BYPASSES_RESISTANCE).add();
        this.tag(DamageTypeTags.AVOIDS_GUARDIAN_THORNS).add();

    }
}