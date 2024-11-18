#version 150

uniform sampler2D DiffuseSampler;

uniform vec2 ScreenSize;

uniform float HueShiftDegrees;

in vec2 texCoord;

out vec4 fragColor;

// Conversion functions from https://stackoverflow.com/questions/15095909/from-rgb-to-hsv-in-opengl-glsl, licensed under WTFPL (Basically just public domain)
vec3 rgb2hsv(vec3 c){
    vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));

    float d = q.x - min(q.w, q.y);
    float e = 1.0e-10;
    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
}
vec3 hsv2rgb(vec3 c){
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

void main(){
    vec4 originalColour = texture(DiffuseSampler, texCoord);
    vec3 hsvColour = rgb2hsv(originalColour.rgb);
    float newHue = hsvColour.x + (HueShiftDegrees / 360);
    vec3 newColour = vec3(mod(newHue, 1), hsvColour.y, hsvColour.z);
    vec3 newRGBColour = hsv2rgb(newColour);
    fragColor = vec4(newRGBColour, 1.0);
}
