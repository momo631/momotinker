package com.momosensei.momotinker.test.testa;


import com.google.common.collect.Maps;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.test.testb.UltimateSlashRenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;

@OnlyIn(Dist.CLIENT)
public class RenderType {

    public static SpaceBrokenRenderType SpaceBroken1 = new SpaceBrokenRenderType(OjangUtils.newRL(Momotinker.MOD_ID, "space_broken" ), 0);
    public static SpaceBrokenRenderType SpaceBroken2 = new SpaceBrokenRenderType(OjangUtils.newRL(Momotinker.MOD_ID, "space_broken" ), 1);

    public static SpaceBrokenRenderType SpaceBrokenEnd = new SpaceBrokenRenderType(OjangUtils.newRL(Momotinker.MOD_ID, "space_broken_end" ), RenderUtils.GetTexture("particle/glass"), 0, 4);
    private static int bloomIdx = 0;
    public static final HashMap<ResourceLocation, UltimateSlashRenderType> BloomRenderTypes = Maps.newHashMap();
    public static UltimateSlashRenderType getBloomRenderTypeByTexture(ResourceLocation texture){
        if(BloomRenderTypes.containsKey(texture)){
            return BloomRenderTypes.get(texture);
        }
        else {
            UltimateSlashRenderType bloomType = new UltimateSlashRenderType(OjangUtils.newRL(Momotinker.MOD_ID, "bp_" + bloomIdx++), texture);
            BloomRenderTypes.put(texture, bloomType);
            return bloomType;
        }
    }
}
