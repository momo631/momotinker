package com.momosensei.momotinker.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.momosensei.momotinker.test.testa.ModRenderTypes;
import com.momosensei.momotinker.test.testa.post.PostEffectPipelines;
import com.momosensei.momotinker.test.testa.post.render.BloomSlashRenderType;
import com.momosensei.momotinker.test.testa.util.RenderUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import static net.minecraft.util.Mth.nextFloat;


public class SlashTrailParticle extends SingleQuadParticle {

    private static final ResourceLocation TEXTURE = RenderUtil.GetTexture("particle/sparks");
    private static final float WIDTH_MUL = 0.03f;
    private static final int LIFETIME = 8;

    private static BloomSlashRenderType cachedRenderType;
    private static BloomSlashRenderType getOrCreateRenderType() {
        if (cachedRenderType == null) {
            cachedRenderType = ModRenderTypes.getBloomRenderTypeByTexture(TEXTURE);
        }
        return cachedRenderType;
    }

    private final float timeOffset;
    private final double originX, originY, originZ;

    public SlashTrailParticle(ClientLevel level,
                              double x, double y, double z,
                              double dirX, double dirY, double dirZ,
                              float r, float g, float b, float a) {
        super(level, x, y, z, dirX, dirY, dirZ);
        this.lifetime = LIFETIME;
        this.timeOffset = nextFloat(random, 0f, 1f);
        this.originX = x;
        this.originY = y;
        this.originZ = z;
        this.xd = dirX;
        this.yd = dirY;
        this.zd = dirZ;
        this.setColor(r, g, b);
        this.alpha = a;
    }

    @Override
    public void tick() {
        if (this.age++ > this.lifetime) {
            this.remove();
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float pt) {
        if (!PostEffectPipelines.isActive()) return;
        getOrCreateRenderType().callPipeline();

        float at = this.age + pt;
        if (at < timeOffset || at > lifetime + timeOffset) return;

        float t = Math.min(1f, at / this.lifetime * 15f);

        Vec3 camPos = camera.getPosition();
        float f  = (float)(this.originX - camPos.x());
        float f1 = (float)(this.originY - camPos.y());
        float f2 = (float)(this.originZ - camPos.z());

        Vector3f right = new Vector3f(f,f1,f2);
        Vector3f dir = new Vector3f((float) this.xd, (float) this.yd, (float) this.zd);

        dir.mul(t);
        right.cross(dir);
        right.normalize();

        float fade = (float) Math.sqrt(Math.min(1f, (lifetime - at) / lifetime * 7.5f));
        right.mul(WIDTH_MUL * fade);

        Vector3f left = new Vector3f(right).mul(-1f);

        Vector3f[] points = {
                new Vector3f(right),
                new Vector3f(left).add(dir),
                new Vector3f(right).add(dir),
                new Vector3f(right),
        };

        for (Vector3f p : points) {
            p.add(f, f1, f2);
        }

        int light = RenderUtil.EMISSIVE_LIGHT;

        buffer.vertex(points[0].x(), points[0].y(), points[0].z())
                .color(rCol, gCol, bCol, alpha).uv(1f, 1f).uv2(light).endVertex();
        buffer.vertex(points[1].x(), points[1].y(), points[1].z())
                .color(rCol, gCol, bCol, alpha).uv(1f, 0f).uv2(light).endVertex();
        buffer.vertex(points[2].x(), points[2].y(), points[2].z())
                .color(rCol, gCol, bCol, alpha).uv(0f, 0f).uv2(light).endVertex();
        buffer.vertex(points[3].x(), points[3].y(), points[3].z())
                .color(rCol, gCol, bCol, alpha).uv(0f, 1f).uv2(light).endVertex();
    }

    @Override
    protected float getU0() { return 0; }
    @Override
    protected float getU1() { return 0; }
    @Override
    protected float getV0() { return 0; }
    @Override
    protected float getV1() { return 0; }

    @Override
    public ParticleRenderType getRenderType() {
        return getOrCreateRenderType();
    }

    @Override
    public boolean shouldCull() {
        return false;
    }

    public static class Provider implements ParticleProvider<SlashTrailOptions> {
        public Provider() {}

        @Override
        public Particle createParticle(SlashTrailOptions opt,
                                       ClientLevel level,
                                       double x, double y, double z,
                                       double vx, double vy, double vz) {
            return new SlashTrailParticle(level, x, y, z,
                    opt.dirX, opt.dirY, opt.dirZ,
                    opt.r, opt.g, opt.b, opt.a);
        }
    }
}