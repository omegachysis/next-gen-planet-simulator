#version 110
attribute vec2 a_position;
attribute vec3 a_color;
varying vec3 f_color;

void main()
{
    f_color = a_color;
    gl_Position = vec4(a_position, 0, 1);
}