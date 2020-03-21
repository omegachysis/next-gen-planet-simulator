#version 110
varying vec2 f_position;
uniform sampler2D u_texture;
uniform float u_dx;
uniform float u_dt;

void main()
{
    float c = texture2D(u_texture, vec2(f_position.x, f_position.y)).r;
    float w = texture2D(u_texture, vec2(f_position.x - u_dx, f_position.y)).r;
    float e = texture2D(u_texture, vec2(f_position.x + u_dx, f_position.y)).r;
    float n = texture2D(u_texture, vec2(f_position.x, f_position.y - u_dx)).r;
    float s = texture2D(u_texture, vec2(f_position.x, f_position.y + u_dx)).r;

    gl_FragColor = vec4(c + (u_dx * u_dt), 0, 0, 1);
}