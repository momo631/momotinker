#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D Mask;
uniform float InterScale;

in vec2 texCoord;
out vec4 fragColor;

void main(){
    vec4 mask = texture(Mask, texCoord);

    if (mask.a > 0.001) {
        float scaled = max(InterScale * mask.a, 0.001);
        vec2 patchedCoord = texCoord / scaled;
        patchedCoord += (scaled - 1.0) * mask.gb;
        patchedCoord = clamp(patchedCoord, vec2(0.0), vec2(1.0));

        fragColor = vec4(texture(DiffuseSampler, patchedCoord).rgb * (1.05 + mask.r * 0.1), 1.0);
    } else {
        fragColor = vec4(texture(DiffuseSampler, texCoord).rgb, 1.0);
    }
}