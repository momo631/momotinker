package com.momosensei.momotinker.register;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import slimeknights.mantle.client.model.NBTKeyModel;
import slimeknights.tconstruct.library.tools.SlotType;

public class MomotinkerSlots {
    public static SlotType mystery = SlotType.getOrCreate("mystery");

    public MomotinkerSlots(){
    }
    @OnlyIn(Dist.CLIENT)
    public static void init() {
        NBTKeyModel.registerExtraTexture(new ResourceLocation("tconstruct:creative_slot")
                ,mystery.getName(),new ResourceLocation("momotinker:item/slot/mystery"));
    }
}
