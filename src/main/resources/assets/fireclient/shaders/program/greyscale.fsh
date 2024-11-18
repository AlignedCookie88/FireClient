#version 150

uniform sampler2D DiffuseSampler;

uniform vec2 ScreenSize;

uniform float BlendAmount;

in vec2 texCoord;

out vec4 fragColor;


void main(){
    vec4 originalColour = texture(DiffuseSampler, texCoord);
    float greyscaleValue = (originalColour.r + originalColour.g + originalColour.b) / 3;
    vec3 greyscaleColour = vec3(greyscaleValue, greyscaleValue, greyscaleValue);
    vec3 blendedColour = mix(originalColour.rgb, greyscaleColour, BlendAmount);
    fragColor = vec4(blendedColour, 1.0);
}
