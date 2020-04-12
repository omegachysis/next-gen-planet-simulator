package edu.psu.planetsim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.Pixmap.Filter;
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
    ShaderProgram visionShader;
    SpriteBatch batch;
    Texture thermal;
    Texture diffusivity;
    Texture radiation;
    FrameBuffer fbo;
    // FrameBuffer visionFbo;
    Mesh mesh;
    int len = 90;

    public static ShaderProgram loadShader(final String vert, final String frag) 
    {
        final var res = new ShaderProgram(
			Gdx.files.getFileHandle(vert + ".vert", Files.FileType.Internal),
			Gdx.files.getFileHandle(frag + ".frag", Files.FileType.Internal));
		if (!res.isCompiled())
			throw new GdxRuntimeException(res.getLog());
		System.out.print(res.getLog());
		return res;
	}

    public void create() 
    {
        shader = loadShader("thermal", "thermal");
        visionShader = loadShader("thermal", "thermal_vision");

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
        
        fbo = new FrameBuffer(Format.RGB888, len * len, len, false);
        fbo.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        // visionFbo = new FrameBuffer(Format.RGB888, len * len, len, false);
        batch = new SpriteBatch();

        // Procedurally generate a thermal texture.
        var pix = new Pixmap(len * len, len, Format.RGB888);
        pix.setFilter(Filter.NearestNeighbour);
        pix.setColor(0f, 0f, 0f, 1f);
        pix.fill();
        pix.setColor(1f, 1f, 1f, 1f);
        for (int z = 0; z < len; z++)
        {
            for (int y = 0; y < len; y++)
            {
                for (int x = 0; x < len; x++)
                {
                    // Test whether the point is in the 3D sphere.
                    final var radius = (len / 2f - 2f) * Math.sin(Math.PI * z / len);
                    final var centerX = len / 2f;
                    final var centerY = len / 2f;
                    final var dist = Math.sqrt(
                        Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
                    if (dist < radius)
                    {
                        pix.setColor(1f, 0f, 0f, 1f);
                        pix.drawPixel(z * len + x, y);
                    }
                }
            }
        }
        thermal = new Texture(pix);
        thermal.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
        thermal.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        // Procedurally generate a diffusivity texture that doesn't allow heat 
        // to leak through the boundaries.
        pix = new Pixmap(len * len, len, Format.RGB888);
        pix.setFilter(Filter.NearestNeighbour);
        pix.setColor(0f, 0f, 0f, 1f);
        pix.fill();
        pix.setColor(1f, 1f, 1f, 1f);
        // Procedurally create a spherical body for heat mapping.
        for (int z = 0; z < len; z++)
        {
            for (int y = 0; y < len; y++)
            {
                for (int x = 0; x < len; x++)
                {
                    // Test whether the point is in the 3D sphere.
                    final var radius = (len / 2f - 2f) * Math.sin(Math.PI * z / len);
                    final var centerX = len / 2f;
                    final var centerY = len / 2f;
                    final var dist = Math.sqrt(
                        Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
                    if (dist < radius)
                    {
                        // Inside the sphere.
                        pix.drawPixel(z * len + x, y);
                    }
                }
            }
        }
        diffusivity = new Texture(pix);
        diffusivity.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        // Procedurally generate a radiation texture for mapping radiation input 
        // and radiant cooling.
        pix = new Pixmap(len * len, len, Format.RGB888);
        pix.setFilter(Filter.NearestNeighbour);
        pix.setColor(0f, 0f, 0f, 1f);
        pix.fill();
        for (int z = 0; z < len; z++)
        {
            for (int y = 0; y < len; y++)
            {
                for (int x = 0; x < len; x++)
                {
                    pix.setColor(0f, 0f, 1f, 1f);
                    if (x == y)
                        pix.drawPixel(z * len + x, y);
                }
            }
        }
        radiation = new Texture(pix);
        radiation.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        fbo.begin();
        batch.begin();
        batch.draw(thermal, 0, 0, 
            Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
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

        // Bind the texture describing the radiant input and output.
        radiation.bind(2);
        shader.setUniformi("u_radiation", 2);

        // Bind uniforms for discrete time evolution and sizes.
        shader.setUniformf("u_dt", Gdx.graphics.getDeltaTime() * 0.1f);
        shader.setUniformf("u_dx", 1f / fbo.getWidth());
        shader.setUniformf("u_dy", 1f / fbo.getHeight());
        shader.setUniformf("u_len", len);

        // Calculate the heat equation approximation by drawing to the shader.
        mesh.render(shader, GL20.GL_TRIANGLES, 0, 6);
        shader.end();
        fbo.end();

        // Render to the thermal vision view.
        // visionFbo.begin();
        // visionShader.begin();
        // fbo.getColorBufferTexture().bind(0);
        // shader.setUniformi("u_texture", 0);
        // mesh.render(shader, GL20.GL_TRIANGLES, 0, 6);
        // visionFbo.end();

        // Render the result as several texture strips.
        batch.begin();
        for (int i = 0; i < Gdx.graphics.getBackBufferHeight() / len; i++)
            batch.draw(fbo.getColorBufferTexture(), -i * len * 
                Gdx.graphics.getBackBufferWidth() / len, i * len * 1.1f);
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
