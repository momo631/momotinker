package com.momosensei.momotinker.test.testa;

import com.momosensei.momotinker.Momotinker;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MyModels {
    public static NoTextureJsonModel SpaceBrokenModel;

    public static void LoadOtherModel(){
        SpaceBrokenModel = NoTextureJsonModel.loadFromJson(OjangUtils.newRL(Momotinker.MOD_ID, "models/effect/spacebroken.json"));
    }
}
