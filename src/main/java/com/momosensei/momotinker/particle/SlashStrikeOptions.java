package com.momosensei.momotinker.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.momosensei.momotinker.register.MomotinkerParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;


/**
 * 斩击触发点参数:
 *  - 颜色 RGBA
 *  - angle:实体随机斩击角
 *  - yaw/pitch:实体朝向(创建时快照,用于四元数)
 *  - lookX/Y/Z:玩家视线单位向量(决定"前方")
 *  - lifetime:存活 tick 数(时间可控)
 *  - particlesPerTick:每 tick 生成 Trail 数(密度可控)
 *  - range:前方最远距离(范围可控,对应 look.scale(N) 的 N)
 */
public class SlashStrikeOptions implements ParticleOptions {

    public final float r, g, b, a;
    public final float angle;
    public final float yaw, pitch;
    public final float lookX, lookY, lookZ;
    public final int lifetime;
    public final int particlesPerTick;
    public final float range;

    public SlashStrikeOptions(float r, float g, float b, float a,
                              float angle, float yaw, float pitch,
                              float lookX, float lookY, float lookZ,
                              int lifetime, int particlesPerTick,
                              float range) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.angle = angle;
        this.yaw = yaw;
        this.pitch = pitch;
        this.lookX = lookX;
        this.lookY = lookY;
        this.lookZ = lookZ;
        this.lifetime = lifetime;
        this.particlesPerTick = particlesPerTick;
        this.range = range;
    }

    public static SlashStrikeOptions red(float angle, float yaw, float pitch,
                                         float lookX, float lookY, float lookZ,
                                         float range) {
        return new SlashStrikeOptions(0.95f, 0f, 0.02f, 0.8f,
                angle, yaw, pitch, lookX, lookY, lookZ, 8, 12, range);
    }

    @Override
    public @NotNull ParticleType<?> getType() {
        return MomotinkerParticles.SLASH_STRIKE_TYPE.get();
    }

    @Override
    public void writeToNetwork(@NotNull FriendlyByteBuf buf) {
        buf.writeFloat(r);
        buf.writeFloat(g);
        buf.writeFloat(b);
        buf.writeFloat(a);
        buf.writeFloat(angle);
        buf.writeFloat(yaw);
        buf.writeFloat(pitch);
        buf.writeFloat(lookX);
        buf.writeFloat(lookY);
        buf.writeFloat(lookZ);
        buf.writeInt(lifetime);
        buf.writeInt(particlesPerTick);
        buf.writeFloat(range);
    }

    @Override
    public @NotNull String writeToString() {
        return String.format("%.3f %.3f %.3f %.3f %.3f %.3f %.3f %.3f %.3f %.3f %d %d %.3f",
                r, g, b, a, angle, yaw, pitch, lookX, lookY, lookZ,
                lifetime, particlesPerTick, range);
    }

    public static final Codec<SlashStrikeOptions> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("r").forGetter(o -> o.r),
                    Codec.FLOAT.fieldOf("g").forGetter(o -> o.g),
                    Codec.FLOAT.fieldOf("b").forGetter(o -> o.b),
                    Codec.FLOAT.fieldOf("a").forGetter(o -> o.a),
                    Codec.FLOAT.fieldOf("angle").forGetter(o -> o.angle),
                    Codec.FLOAT.fieldOf("yaw").forGetter(o -> o.yaw),
                    Codec.FLOAT.fieldOf("pitch").forGetter(o -> o.pitch),
                    Codec.FLOAT.fieldOf("lookX").forGetter(o -> o.lookX),
                    Codec.FLOAT.fieldOf("lookY").forGetter(o -> o.lookY),
                    Codec.FLOAT.fieldOf("lookZ").forGetter(o -> o.lookZ),
                    Codec.INT.fieldOf("lifetime").forGetter(o -> o.lifetime),
                    Codec.INT.fieldOf("particlesPerTick").forGetter(o -> o.particlesPerTick),
                    Codec.FLOAT.fieldOf("range").forGetter(o -> o.range)
            ).apply(instance, SlashStrikeOptions::new)
    );

    public static final ParticleOptions.Deserializer<SlashStrikeOptions> DESERIALIZER =
            new ParticleOptions.Deserializer<>() {

                @Override
                public @NotNull SlashStrikeOptions fromCommand(@NotNull ParticleType<SlashStrikeOptions> type,
                                                               @NotNull StringReader reader)
                        throws CommandSyntaxException {
                    // 命令: <r> <g> <b> <a> <angle> <yaw> <pitch> <lookX> <lookY> <lookZ> <lifetime> <perTick> <range>
                    reader.expect(' ');
                    float r = reader.readFloat();
                    reader.expect(' ');
                    float g = reader.readFloat();
                    reader.expect(' ');
                    float b = reader.readFloat();
                    reader.expect(' ');
                    float a = reader.readFloat();
                    reader.expect(' ');
                    float angle = reader.readFloat();
                    reader.expect(' ');
                    float yaw = reader.readFloat();
                    reader.expect(' ');
                    float pitch = reader.readFloat();
                    reader.expect(' ');
                    float lx = reader.readFloat();
                    reader.expect(' ');
                    float ly = reader.readFloat();
                    reader.expect(' ');
                    float lz = reader.readFloat();
                    reader.expect(' ');
                    int lifetime = reader.readInt();
                    reader.expect(' ');
                    int perTick = reader.readInt();
                    reader.expect(' ');
                    float range = reader.readFloat();          // 新增
                    return new SlashStrikeOptions(r, g, b, a, angle, yaw, pitch,
                            lx, ly, lz, lifetime, perTick, range);
                }

                @Override
                public @NotNull SlashStrikeOptions fromNetwork(@NotNull ParticleType<SlashStrikeOptions> type,
                                                               @NotNull FriendlyByteBuf buf) {
                    return new SlashStrikeOptions(
                            buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(),
                            buf.readFloat(), buf.readFloat(), buf.readFloat(),
                            buf.readFloat(), buf.readFloat(), buf.readFloat(),
                            buf.readInt(), buf.readInt(),
                            buf.readFloat());
                }
            };
}