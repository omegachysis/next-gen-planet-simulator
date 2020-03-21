#version 120
attribute vec2 a_position;
varying vec2 f_position;

void main()
{
    f_position = vec2(a_position.x + 1.0, a_position.y + 1.0) * 0.5;
    gl_Position = vec4(a_position, 0, 1);
}