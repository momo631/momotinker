package com.momosensei.momotinker.test.testa.post;

import com.momosensei.momotinker.test.testa.post.pass.CompositePass;
import com.momosensei.momotinker.test.testa.post.pass.DownSamplingPass;
import com.momosensei.momotinker.test.testa.post.pass.SpaceBrokenPass;
import com.momosensei.momotinker.test.testa.post.pass.UpSamplingPass;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterShadersEvent;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class PostPassRegistry {

    public static PostPassBase blit;
    public static SpaceBrokenPass space_broken;
    public static DownSamplingPass downSampler;
    public static UpSamplingPass upSampler;
    public static CompositePass composite_pass;

    public static void register(RegisterShadersEvent event){
        try {
            System.out.println("Load Shader");
            ResourceManager rm = Minecraft.getInstance().getResourceManager();
            blit = new PostPassBase("momotinker:blit",rm);
           
            space_broken = new SpaceBrokenPass("momotinker:space_broken",rm);
           
            downSampler = new DownSamplingPass("momotinker:down_sampling",rm);
            upSampler = new UpSamplingPass("momotinker:up_sampling",rm);
            composite_pass = new CompositePass("momotinker:composite_pass",rm);
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
