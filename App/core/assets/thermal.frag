#version 110
varying vec2 f_position;
uniform sampler2D u_texture;
uniform float u_dx;
uniform float u_dt;

float getF(vec3 pixel)
{
    return floor(pixel.r * 255.0) * 255.0 * 255.0 + 
        floor(pixel.g * 255.0) * 255.0 + 
        floor(pixel.b * 255.0);
}

vec3 getColor(float f)
{
    float r = mod(f, 255.0);
    float g = mod(floor(f / 255.0), 255.0 * 255.0);
    float b = floor(f / 255.0 / 255.0);
    return vec3(r, g, b);
}

void main()
{
    // Fetch nearby pixel values for calculating derivatives.
    float c = texture2D(u_texture, vec2(f_position.x, f_position.y)).r;
    float w = texture2D(u_texture, vec2(f_position.x - u_dx, f_position.y)).r;
    float e = texture2D(u_texture, vec2(f_position.x + u_dx, f_position.y)).r;
    float n = texture2D(u_texture, vec2(f_position.x, f_position.y - u_dx)).r;
    float s = texture2D(u_texture, vec2(f_position.x, f_position.y + u_dx)).r;

    // Calculate laplacian.
    float laplacian = (w + e + n + s) * 0.25 - c;

    // Calculate heat equation.
    float df_dt = 10.0 * laplacian * u_dt;

    gl_FragColor = vec4(c + df_dt, 0, 0, 1);
}