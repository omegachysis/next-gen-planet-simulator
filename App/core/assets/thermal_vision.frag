#version 110
varying vec2 f_position;
uniform sampler2D u_texture;

// Do a base-255 conversion between RGB values and a single packed value.
float getF(vec3 pixel)
{
    float r = pixel.r * 255.0;
    float g = pixel.g * 255.0;
    float b = pixel.b * 255.0;
    return floor(r) * 256.0 * 256.0 +
        floor(g) * 256.0 + 
        floor(b);
}

void main()
{
    float f = getF(texture2D(u_texture, vec2(f_position.x, f_position.y)).rgb);
    gl_FragColor = vec4(f / 16777216.0, 0, 0, 1);
}