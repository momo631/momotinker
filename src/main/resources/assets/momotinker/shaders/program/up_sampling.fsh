#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DownTexture;
uniform vec2 OutSize;

in vec2 texCoord;
out vec4 fragColor;

void main(){
    vec2 texel = 1.0 / OutSize;

    vec4 c = texture(DiffuseSampler, texCoord + vec2(-texel.x, -texel.y))        * 1.0
    + texture(DiffuseSampler, texCoord + vec2( 0.0,     -texel.y))        * 2.0
    + texture(DiffuseSampler, texCoord + vec2( texel.x, -texel.y))        * 1.0
    + texture(DiffuseSampler, texCoord + vec2(-texel.x,  0.0))            * 2.0
    + texture(DiffuseSampler, texCoord)                                   * 4.0
    + texture(DiffuseSampler, texCoord + vec2( texel.x,  0.0))            * 2.0
    + texture(DiffuseSampler, texCoord + vec2(-texel.x,  texel.y))        * 1.0
    + texture(DiffuseSampler, texCoord + vec2( 0.0,      texel.y))        * 2.0
    + texture(DiffuseSampler, texCoord + vec2( texel.x,  texel.y))        * 1.0;

    fragColor = c * 0.8 / 16.0 + texture(DownTexture, texCoord) * 0.8;
}