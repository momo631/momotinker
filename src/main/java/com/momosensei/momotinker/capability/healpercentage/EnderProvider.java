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

public class EnderProvider implements ICapabilityProvider, INBTSerializable {

    private Ender Ender;

    private static final Capability<Ender>ENDER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    private final LazyOptional<Ender>lazyOptional = LazyOptional.of(() -> this.Ender);

    public EnderProvider(){
        this.Ender = new Ender();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        return getCapability(capability);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == ENDER_CAPABILITY){
            return lazyOptional.cast();
        }
        else
        {
            return LazyOptional.empty();
        }
    }

    @Override
    public Tag serializeNBT() {
        var tag = new CompoundTag();
        Ender.saveNBTData(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(Tag tag) {
        Ender.loadNBTData((CompoundTag) tag);
    }
}
