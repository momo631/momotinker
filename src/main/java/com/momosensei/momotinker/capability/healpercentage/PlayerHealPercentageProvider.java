package com.momosensei.momotinker.capability.healpercentage;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerHealPercentageProvider implements ICapabilityProvider, INBTSerializable {

    private PlayerHealPercentage playerHealPercentage;

    private static final Capability<PlayerHealPercentage>PLAYER_HEAL_PERCENTAGE_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    private final LazyOptional<PlayerHealPercentage>lazyOptional = LazyOptional.of(() -> this.playerHealPercentage);

    public PlayerHealPercentageProvider(){
        this.playerHealPercentage = new PlayerHealPercentage();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        return getCapability(capability);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == PLAYER_HEAL_PERCENTAGE_CAPABILITY){
            return lazyOptional.cast();
        }
        else
        {
            return lazyOptional.empty();
        }
    }

    @Override
    public Tag serializeNBT() {
        var tag = new CompoundTag();
        playerHealPercentage.saveNBTData(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(Tag tag) {
        playerHealPercentage.loadNBTData((CompoundTag) tag);
    }
}
