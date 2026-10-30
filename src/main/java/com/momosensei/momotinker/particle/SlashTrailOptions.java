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
 * 斩击拖尾参数:
 *  - 颜色 RGBA
 *  - 拖尾方向 (dirX, dirY, dirZ)
 *  - 空间旋转四元数(与物品渲染一致)
 */
public class SlashTrailOptions implements ParticleOptions {

    public final float r, g, b, a;
    public final float dirX, dirY, dirZ;

    public SlashTrailOptions(float r, float g, float b, float a,
                             float dirX, float dirY, float dirZ) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.dirX = dirX;
        this.dirY = dirY;
        this.dirZ = dirZ;
    }

    @Override
    public @NotNull ParticleType<?> getType() {
        return MomotinkerParticles.SLASH_TRAIL_TYPE.get();
    }

    @Override
    public void writeToNetwork(@NotNull FriendlyByteBuf buf) {
        buf.writeFloat(r);
        buf.writeFloat(g);
        buf.writeFloat(b);
        buf.writeFloat(a);
        buf.writeFloat(dirX);
        buf.writeFloat(dirY);
        buf.writeFloat(dirZ);
    }

    @Override
    public @NotNull String writeToString() {
        return String.format("%.3f %.3f %.3f %.3f %.3f %.3f %.3f",
                r, g, b, a, dirX, dirY, dirZ);
    }

    public static final Codec<SlashTrailOptions> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("r").forGetter(o -> o.r),
                    Codec.FLOAT.fieldOf("g").forGetter(o -> o.g),
                    Codec.FLOAT.fieldOf("b").forGetter(o -> o.b),
                    Codec.FLOAT.fieldOf("a").forGetter(o -> o.a),
                    Codec.FLOAT.fieldOf("dirX").forGetter(o -> o.dirX),
                    Codec.FLOAT.fieldOf("dirY").forGetter(o -> o.dirY),
                    Codec.FLOAT.fieldOf("dirZ").forGetter(o -> o.dirZ)
            ).apply(instance, SlashTrailOptions::new)
    );

    public static final ParticleOptions.Deserializer<SlashTrailOptions> DESERIALIZER =
            new ParticleOptions.Deserializer<>() {

                @Override
                public @NotNull SlashTrailOptions fromCommand(@NotNull ParticleType<SlashTrailOptions> type,
                                                              @NotNull StringReader reader)
                        throws CommandSyntaxException {
                    // 命令: <r> <g> <b> <a> <dirX> <dirY> <dirZ>
                    reader.expect(' ');
                    float r = reader.readFloat();
                    reader.expect(' ');
                    float g = reader.readFloat();
                    reader.expect(' ');
                    float b = reader.readFloat();
                    reader.expect(' ');
                    float a = reader.readFloat();
                    reader.expect(' ');
                    float dx = reader.readFloat();
                    reader.expect(' ');
                    float dy = reader.readFloat();
                    reader.expect(' ');
                    float dz = reader.readFloat();
                    reader.expect(' ');
                    return new SlashTrailOptions(r, g, b, a, dx, dy, dz);
                }

                @Override
                public @NotNull SlashTrailOptions fromNetwork(@NotNull ParticleType<SlashTrailOptions> type,
                                                              @NotNull FriendlyByteBuf buf) {
                    return new SlashTrailOptions(
                            buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(),
                            buf.readFloat(), buf.readFloat(), buf.readFloat());
                }
            };
}