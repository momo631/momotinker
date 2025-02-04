package com.momosensei.momotinker.entity;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class LegacyDamageSource extends DamageSource {
    public ArrayList<ResourceKey<DamageType>> damageTypes = new ArrayList<>();

    public LegacyDamageSource(Holder<DamageType> holder, @Nullable Entity directEntity, @Nullable Entity causingEntity, @Nullable Vec3 sourcePos) {
        super(holder, directEntity, causingEntity, sourcePos);
    }

    public LegacyDamageSource(DamageSource source) {
        this(source.typeHolder(), source.getDirectEntity(), source.getEntity(), source.sourcePositionRaw());
    }

    public static LegacyDamageSource mobAttack(@NotNull LivingEntity living) {
        return new LegacyDamageSource(living.damageSources().mobAttack(living)).setBypassArmor().setBypassInvul().setBypassInvulnerableTime().setBypassMagic().setBypassEnchantment().setBypassShield();
    }

    public LegacyDamageSource setBypassInvulnerableTime() {
        this.damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE, DamageTypeTags.BYPASSES_COOLDOWN.location()));
        return this;
    }

    public LegacyDamageSource setBypassArmor() {
        damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE, DamageTypeTags.BYPASSES_ARMOR.location()));
        return this;
    }

    public LegacyDamageSource setBypassInvul() {
        damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE, DamageTypeTags.BYPASSES_INVULNERABILITY.location()));
        return this;
    }

    public LegacyDamageSource setBypassMagic() {
        damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE, DamageTypeTags.BYPASSES_RESISTANCE.location()));
        damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE, DamageTypeTags.BYPASSES_EFFECTS.location()));
        return this;
    }

    public LegacyDamageSource setBypassEnchantment() {
        damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE, DamageTypeTags.BYPASSES_ENCHANTMENTS.location()));
        damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE, DamageTypeTags.AVOIDS_GUARDIAN_THORNS.location()));
        return this;
    }

    public LegacyDamageSource setBypassShield() {
        damageTypes.add(ResourceKey.create(Registries.DAMAGE_TYPE, DamageTypeTags.BYPASSES_SHIELD.location()));
        return this;
    }

    @Override
    public boolean is(TagKey<DamageType> key) {
        if (!damageTypes.isEmpty()) {
            return damageTypes.contains(ResourceKey.create(Registries.DAMAGE_TYPE, key.location())) || super.is(key);
        }
        return super.is(key);
    }

    @Override
    public boolean is(ResourceKey<DamageType> key) {
        if (!damageTypes.isEmpty()) {
            return damageTypes.contains(key) || super.is(key);
        }
        return super.is(key);
    }
}