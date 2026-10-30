package com.momosensei.momotinker.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import static net.minecraft.util.Mth.nextFloat;

public class SlashStrikeParticle extends NoRenderParticle {
    private final float colorR, colorG, colorB, trailAlpha;
    private final int perTick;
    private final float range;
    private final Vector3f forward;
    public SlashStrikeParticle(ClientLevel level,
                               double x, double y, double z,
                               double vx, double vy, double vz,
                               SlashStrikeOptions opt) {
        super(level, x, y, z, vx, vy, vz);
        this.lifetime = opt.lifetime;
        this.perTick = opt.particlesPerTick;
        this.colorR = opt.r;
        this.colorG = opt.g;
        this.colorB = opt.b;
        this.trailAlpha = opt.a;
        this.range = opt.range;

        Vector3f fwd = new Vector3f(opt.lookX, opt.lookY, opt.lookZ);
        fwd.normalize();
        this.forward = fwd;

        Vector3f worldUp = new Vector3f(0f, 1f, 0f);
        Vector3f r = new Vector3f(fwd).cross(worldUp);
        r.normalize();
    }

    @Override
    public void tick() {
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        for (int i = 0; i < perTick; i++) {
            float r = nextFloat(random, 0, 4);
            float theta = nextFloat(random, 0, 360);
            float beta = nextFloat(random, 30, 150);

            float r2 = 4 + this.range*1.5f;
            float theta2 = nextFloat(random, theta - 50, theta + 50);
            float beta2 = nextFloat(random, beta - 50, beta + 50);

            theta = (float) (theta / 180 * Math.PI);
            beta = (float) (beta / 180 * Math.PI);
            theta2 = (float) (theta2 / 180 * Math.PI);
            beta2 = (float) (beta2 / 180 * Math.PI);

            float scale = 0.6f;
            double sr = r * Math.sin(beta);
            double sx = sr * Math.sin(theta) * scale;
            double sy = r * Math.cos(beta) * scale;
            double sz = sr * Math.cos(theta) * scale;

            double er = r2 * Math.sin(beta2);
            double ex = er * Math.sin(theta2) * scale;
            double ey = r2 * Math.cos(beta2) * scale;
            double ez = er * Math.cos(theta2) * scale;

            double offX = this.forward.x * this.range;
            double offY = this.forward.y * this.range;
            double offZ = this.forward.z * this.range;

            level.addParticle(
                    new SlashTrailOptions(colorR, colorG, colorB, trailAlpha,
                            (float) (-ex - sx + offX),
                            (float) ( ey - sy + offY),
                            (float) (-ez - sz + offZ)),
                    (sx + x),
                    (sy + y),
                    (sz + z),
                    0, 0, 0);
        }
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleProvider<SlashStrikeOptions> {
        public Provider() {}

        @Override
        public Particle createParticle(SlashStrikeOptions opt,
                                       ClientLevel level,
                                       double x, double y, double z,
                                       double vx, double vy, double vz) {
            return new SlashStrikeParticle(level, x, y, z, vx, vy, vz, opt);
        }
    }
}