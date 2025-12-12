package com.momosensei.momotinker.test.testa;

import com.momosensei.momotinker.test.testb.DownSampling;
import com.momosensei.momotinker.test.testb.UnityComposite;
import com.momosensei.momotinker.test.testb.UpSampling;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterShadersEvent;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class PostPasses {

    public static PostPassBase blit;
    public static SpaceBroken space_broken;
    public static DownSampling downSampler;
    public static UpSampling upSampler;
    public static UnityComposite unity_composite;

    public static void register(RegisterShadersEvent event){
        try {
            System.out.println("Load Shader");
            ResourceManager rm = Minecraft.getInstance().getResourceManager();
            blit = new PostPassBase("momotinker:blit",rm);
           
            space_broken = new SpaceBroken("momotinker:space_broken",rm);
           
            downSampler = new DownSampling("momotinker:down_sampling",rm);
            upSampler = new UpSampling("momotinker:up_sampling",rm);
            unity_composite = new UnityComposite("momotinker:unity_composite",rm);
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
