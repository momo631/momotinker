#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DownTexture;
uniform sampler2D Background;
uniform vec2 OutSize;
uniform float BloomIntensive;
uniform float BloomBase;
uniform float BloomThresholdUp;
uniform float BloomThresholdDown;

in vec2 texCoord;
out vec4 fragColor;

void main(){
    vec2 texel = 1.0 / OutSize;

    vec4 out_colour = texture(DiffuseSampler, texCoord + vec2( texel.x,  texel.y)) * 1.0
    + texture(DiffuseSampler, texCoord + vec2( texel.x,  0.0))      * 2.0
    + texture(DiffuseSampler, texCoord + vec2( texel.x, -texel.y)) * 1.0
    + texture(DiffuseSampler, texCoord + vec2( 0.0,     -texel.y)) * 2.0
    + texture(DiffuseSampler, texCoord)                            * 4.0
    + texture(DiffuseSampler, texCoord + vec2( 0.0,      texel.y)) * 2.0
    + texture(DiffuseSampler, texCoord + vec2(-texel.x, -texel.y)) * 1.0
    + texture(DiffuseSampler, texCoord + vec2(-texel.x,  0.0))      * 2.0
    + texture(DiffuseSampler, texCoord + vec2(-texel.x,  texel.y)) * 1.0;

    vec4 highLight = texture(DownTexture, texCoord);
    vec4 bloom = BloomIntensive * vec4(out_colour.rgb * 0.8 / 16.0 + highLight.rgb * 0.8, 1.0);

    vec4 background = texture(Background, texCoord);
    background.rgb = background.rgb * (1.0 - highLight.a) + highLight.a * highLight.rgb;

    float maxC = max(background.b, max(background.r, background.g));
    float minC = min(background.b, min(background.r, background.g));
    float luma = (maxC + minC) * 0.5;

    float strength = (1.0 - luma) * (BloomThresholdUp - BloomThresholdDown)
    + BloomThresholdDown + BloomBase;

    fragColor = vec4(background.rgb + bloom.rgb * strength, 1.0);
}