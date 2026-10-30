package com.momosensei.momotinker.test.testa.post.pass;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.momosensei.momotinker.test.testa.post.PostPassBase;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;

import static com.momosensei.momotinker.test.testa.post.PostEffectPipelines.shaderOrthoMatrix;

public class SpaceBrokenPass extends PostPassBase {

    public SpaceBrokenPass(String resourceLocation, ResourceManager resmgr) throws IOException {
        super(resourceLocation, resmgr);
    }


    public void process(RenderTarget inTarget, RenderTarget mask, RenderTarget outTarget) {
        prevProcess(inTarget, outTarget);
        inTarget.unbindWrite();

        RenderSystem.viewport(0, 0, outTarget.width, outTarget.height);
        this.effect.setSampler("DiffuseSampler", inTarget::getColorTextureId);
        this.effect.setSampler("Mask", mask::getColorTextureId);

        this.effect.safeGetUniform("ProjMat").set(shaderOrthoMatrix);
        this.effect.safeGetUniform("OutSize").set((float) outTarget.width, (float) outTarget.height);

        this.effect.apply();

        pushVertex(inTarget, outTarget);

        this.effect.clear();
        outTarget.unbindWrite();
        inTarget.unbindRead();
    }
}
