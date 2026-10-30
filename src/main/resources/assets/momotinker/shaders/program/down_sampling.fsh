#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 OutSize;
uniform vec2 InSize;

in vec2 texCoord;
out vec4 fragColor;

void main(){
    vec2 texel = 1.0 / InSize;

    vec4 c = texture(DiffuseSampler, texCoord + vec2(-texel.x, -texel.y))
    + texture(DiffuseSampler, texCoord + vec2( texel.x, -texel.y))
    + texture(DiffuseSampler, texCoord + vec2(-texel.x,  texel.y))
    + texture(DiffuseSampler, texCoord + vec2( texel.x,  texel.y));

    fragColor = c * 0.25;
}