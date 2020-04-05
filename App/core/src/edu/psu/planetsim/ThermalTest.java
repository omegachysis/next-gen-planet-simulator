package edu.psu.planetsim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ThermalTest extends ApplicationAdapter 
{
    ShaderProgram shader;
    SpriteBatch batch;
    Texture thermal;
    Texture diffusivity;
    FrameBuffer fbo;
    Mesh mesh;

    public static ShaderProgram loadShader(final String name) 
    {
        final var res = new ShaderProgram(
			Gdx.files.getFileHandle(name + ".vert", Files.FileType.Internal),
			Gdx.files.getFileHandle(name + ".frag", Files.FileType.Internal));
		if (!res.isCompiled())
			throw new GdxRuntimeException(res.getLog());
		System.out.print(res.getLog());
		return res;
	}

    public void create() 
    {
        shader = loadShader("thermal");

        // Create a quad for performing the draw instruction that calculates 
        // the heat equation on the graphics card.
        mesh = new Mesh(
            true, 4, 6, new VertexAttribute(Usage.Position, 2, "a_position"));
        mesh.setVertices(new float[] 
        { 
            -1, 1,
            1, 1,
            -1, -1,
            1, -1,
        });
        mesh.setIndices(new short[] { 0, 2, 1, 1, 2, 3 });
        
        fbo = new FrameBuffer(Format.RGB888, 16 * 16, 16, false);
        batch = new SpriteBatch();
        thermal = new Texture("heat.png");
        thermal.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);

        diffusivity = new Texture("diffusivity.png");

        fbo.begin();
        batch.begin();
        batch.draw(thermal, 0, 0, 1280, 800);
        batch.end();
        fbo.end();
	}

    public void render() 
    {
		Gdx.gl.glClearColor(0.4f, 0.1f, 0.3f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        fbo.begin();
        shader.begin();

        // Bind heat storage texture.
        fbo.getColorBufferTexture().bind(0);
        shader.setUniformi("u_texture", 0);

        // Bind the texture describing the thermal diffusivity.
        diffusivity.bind(1);
        shader.setUniformi("u_diffusivity", 1);

        // Bind uniforms for discrete time evolution and sizes.
        shader.setUniformf("u_dt", Gdx.graphics.getDeltaTime() / 10f);
        shader.setUniformf("u_dx", 1f / fbo.getWidth());
        shader.setUniformf("u_len", 16f);

        // Calcualte the heat equation approximation by drawing to the shader.
        mesh.render(shader, GL20.GL_TRIANGLES, 0, 6);
        shader.end();
        fbo.end();

        batch.begin();
        batch.draw(fbo.getColorBufferTexture(), 0, 400, 1280, 1280 / 16);
        batch.end();
	}

    public void dispose() 
    {
        batch.dispose();
        shader.dispose();
        thermal.dispose();
        mesh.dispose();
        fbo.dispose();
    }
}
