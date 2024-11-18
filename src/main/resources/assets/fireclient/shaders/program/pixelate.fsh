#version 150

uniform sampler2D DiffuseSampler;

uniform vec2 ScreenSize;

uniform float PixelationStrength;

in vec2 texCoord;

out vec4 fragColor;

void main(){
    float aspect = ScreenSize.x / ScreenSize.y;
    float x = texCoord.x;
    float y = texCoord.y;
    float xw = PixelationStrength;
    float yw = xw / aspect;
    vec4 pixelColour = texture(DiffuseSampler, vec2(floor(x * xw) / xw, floor(y * yw) / yw));
    fragColor = vec4(pixelColour.rgb, 1.0);
}
