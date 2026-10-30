package com.momosensei.momotinker.test.testa.post.pass;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.momosensei.momotinker.test.testa.post.PostPassBase;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;

public class UpSamplingPass extends PostPassBase {
    public UpSamplingPass(EffectInstance effect) {
        super(effect);
    }

    public UpSamplingPass(String resourceLocation, ResourceManager resmgr) throws IOException {
        super(resourceLocation, resmgr);
    }

    public void process(RenderTarget inTarget, RenderTarget outTarget, RenderTarget downTexture) {
        inTarget.unbindWrite();

        RenderSystem.viewport(0, 0, outTarget.width, outTarget.height);
        this.effect.setSampler("DiffuseSampler", inTarget::getColorTextureId);

        this.effect.safeGetUniform("ProjMat").set(orthographic(outTarget));
        this.effect.safeGetUniform("OutSize").set((float) outTarget.width, (float) outTarget.height);
        //this.effect.safeGetUniform("InSize").set((float) inTarget.width, (float) inTarget.height);
        effect.setSampler("DownTexture", downTexture::getColorTextureId);

        this.effect.apply();

        pushVertex(inTarget, outTarget);

        this.effect.clear();
        outTarget.unbindWrite();
        inTarget.unbindRead();
    }
}
