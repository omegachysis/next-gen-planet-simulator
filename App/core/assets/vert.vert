in vec2 a_position;
in vec3 a_color;
out varying vec3 f_color;

void main()
{
    f_color = a_color;
    gl_Position = vec4(a_position, 0, 1);
}