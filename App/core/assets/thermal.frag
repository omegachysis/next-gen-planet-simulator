#version 110
varying vec2 f_position;
uniform sampler2D u_texture;
uniform sampler2D u_diffusivity;
uniform float u_dx;
uniform float u_dt;
uniform float u_len;

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

// Do a base-255 conversion between RGB values and a single packed value.
vec3 getColor(float f)
{
    float b = mod(f, 256.0);
    float g = mod(floor(f / 256.0), 256.0);
    float r = floor(f / 256.0 / 256.0);
    return vec3(r / 255.0, g / 255.0, b / 255.0);
}

void main()
{
    // Fetch nearby pixel values for the laplacian.
    float c = getF(texture2D(u_texture, vec2(f_position.x, f_position.y)).rgb);
    float w = getF(texture2D(u_texture, vec2(f_position.x - u_dx, f_position.y)).rgb);
    float e = getF(texture2D(u_texture, vec2(f_position.x + u_dx, f_position.y)).rgb);
    float n = getF(texture2D(u_texture, vec2(f_position.x, f_position.y - u_dx)).rgb);
    float s = getF(texture2D(u_texture, vec2(f_position.x, f_position.y + u_dx)).rgb);
    float u = getF(texture2D(u_texture, vec2(f_position.x + 1.0 / u_len, f_position.y)).rgb);
    float d = getF(texture2D(u_texture, vec2(f_position.x - 1.0 / u_len, f_position.y)).rgb);

    // Fetch the thermal diffusivity at this location.
    float perm = texture2D(u_diffusivity, vec2(f_position.x, f_position.y)).r;

    // Estimate the laplacian.
    float laplacian = (w + e + n + s + u + d) / 6.0 - c;

    // Estimate the discrete time evolution heat equation.
    float df_dt = perm * laplacian * u_dt;

    gl_FragColor = vec4(getColor(c + df_dt), 1);
}