package com.momosensei.momotinker.test.testa.model;

import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.test.testa.util.ResourceUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class JsonMeshLoader {
    public static JsonMeshModel SpaceBrokenModel;

    public static void LoadOtherModel(){
        SpaceBrokenModel = JsonMeshModel.loadFromJson(ResourceUtil.newRL(Momotinker.MOD_ID, "models/effect/spacebroken.json"));
    }
}
