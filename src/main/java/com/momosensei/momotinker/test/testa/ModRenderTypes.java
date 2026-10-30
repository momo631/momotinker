package com.momosensei.momotinker.test.testa;


import com.google.common.collect.Maps;
import com.momosensei.momotinker.Momotinker;
import com.momosensei.momotinker.test.testa.post.render.BloomSlashRenderType;
import com.momosensei.momotinker.test.testa.post.render.SpaceBrokenRenderType;
import com.momosensei.momotinker.test.testa.util.RenderUtil;
import com.momosensei.momotinker.test.testa.util.ResourceUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;

@OnlyIn(Dist.CLIENT)
public class ModRenderTypes {

    public static SpaceBrokenRenderType SPACE_BROKEN_LAYER_0 = new SpaceBrokenRenderType(ResourceUtil.newRL(Momotinker.MOD_ID, "space_broken" ), 0);
    public static SpaceBrokenRenderType SPACE_BROKEN_LAYER_1 = new SpaceBrokenRenderType(ResourceUtil.newRL(Momotinker.MOD_ID, "space_broken" ), 1);

    public static SpaceBrokenRenderType SpaceBrokenEnd = new SpaceBrokenRenderType(ResourceUtil.newRL(Momotinker.MOD_ID, "space_broken_end" ), RenderUtil.GetTexture("particle/glass"), 0, 4);
    private static int bloomIdx = 0;
    public static final HashMap<ResourceLocation, BloomSlashRenderType> BloomRenderTypes = Maps.newHashMap();
    public static BloomSlashRenderType getBloomRenderTypeByTexture(ResourceLocation texture){
        if(BloomRenderTypes.containsKey(texture)){
            return BloomRenderTypes.get(texture);
        }
        else {
            BloomSlashRenderType bloomType = new BloomSlashRenderType(ResourceUtil.newRL(Momotinker.MOD_ID, "bp_" + bloomIdx++), texture);
            BloomRenderTypes.put(texture, bloomType);
            return bloomType;
        }
    }
}
