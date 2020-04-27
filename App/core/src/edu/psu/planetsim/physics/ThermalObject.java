package edu.psu.planetsim.physics;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;

import edu.psu.planetsim.AppState;
import edu.psu.planetsim.Metrics;
import edu.psu.planetsim.graphics.TerrainBuilder;

/** Describes a thermal profile and a diffusivity profile 
 * for a celestial body.
 */
public class ThermalObject
{
    public final int resolution;
    public final Texture diffusivity;
    public final Texture radiation;

    private final ShaderProgram _shader;
    private final SpriteBatch _spriteBatch;
    private final Mesh _quadMesh;
    public final FrameBuffer temperature;

    public ThermalObject(int resolution, AppState.CelestialBody dto)
    {
        this.resolution = resolution;

        // Load shader that performs temperature simulation.
        _shader = _loadShader("thermal", "thermal");

        // Create a quad for performing the draw instruction that calculates 
        // the heat equation on the graphics card.
        _quadMesh = new Mesh(
            true, 4, 6, new VertexAttribute(Usage.Position, 2, "a_position"));
            _quadMesh.setVertices(new float[] 
        { 
            -1, 1,
            1, 1,
            -1, -1,
            1, -1,
        });
        _quadMesh.setIndices(new short[] { 0, 2, 1, 1, 2, 3 });

        // Set up the framebuffer for performing the thermal calculations.
        temperature = new FrameBuffer(Format.RGB888,
            resolution * resolution, resolution, false);
        temperature.getColorBufferTexture().setFilter(
            TextureFilter.Nearest, TextureFilter.Nearest);
        
        // Create a spritebatch that will render the quad to perform the calculations.
        _spriteBatch = new SpriteBatch();

        // Generate a temperature profile from the input data.
        var heat = new Pixmap(
            resolution * resolution, resolution, Format.RGB888);

        // Generate a diffusivity texture from the elevation data,
        // making areas above the elevation have zero diffusivity, 
        // while areas in the interior have non-zero diffusivity.
        var diffusivity = new Pixmap(
            resolution * resolution, resolution, Format.RGB888);

        // Also generate a radiation texture that is blue in places 
        // where the terrain faces the cold abyss of space.
        var radiation = new Pixmap(
            resolution * resolution, resolution, Format.RGB888);

        // Loop through every pixel of the diffusivity texture and 
        // determine whether the point it represents is an interior point,
        // a boundary point, or an exterior point.
        var elevationMap = TerrainBuilder.BuildElevationMap(resolution, dto);

        for (int z = 0; z < resolution; z++)
        {
            for (int y = 0; y < resolution; y++)
            {
                for (int x = 0; x < resolution; x++)
                {
                    // Convert the cartesian texture coordinates x,y,z
                    // into unit sphere cartesian coordinates.
                    var dx = x * 2.0 / resolution - 1.0;
                    var dy = y * 2.0 / resolution - 1.0;
                    var dz = z * 2.0 / resolution - 1.0;

                    var r = new Vector3((float)dx, (float)dy, (float)dz);

                    // Convert the cartesian x,y,z coordinates 
                    // to spherical coordinates on the elevation map
                    // assuming we are on the surface of the unit sphere.
                    var latlon = Metrics.toSphericalCoords(r);
                    var latIndex = (int)(latlon.x * (resolution - 1));
                    var lonIndex = (int)(latlon.y * (resolution - 1));

                    // Find the elevation at this point above the unit sphere.
                    var elev = elevationMap[latIndex * resolution + lonIndex];
                    elev /= dto.radius; // Convert elevations to unit sphere space.
                    
                    // Find out whether this is interior, exterior, or boundary.
                    var boundaryThickness = 0.1f;

                    if (r.len() <= elev) 
                    {
                        if (elev - r.len() <= boundaryThickness)
                        {
                            // Boundary
                            diffusivity.setColor(0.5f, 0.5f, 0.5f, 1f);
                            radiation.setColor(0f, 0f, 1f, 1f);
                            heat.setColor(0f, 1f, 0f, 1f);
                        }
                        else
                        {
                            // Interior
                            diffusivity.setColor(1f, 1f, 1f, 1f);
                            radiation.setColor(.1f, 0f, 0f, 1f);
                            heat.setColor(1f, 0f, 0f, 1f);
                        }
                    }
                    else
                    {
                        // Exterior
                        diffusivity.setColor(0f, 0f, 0f, 1f);
                        radiation.setColor(0f, 0f, 0f, 1f);
                        heat.setColor(0f, 0f, 0f, 1f);
                    }

                    diffusivity.drawPixel(x + z * resolution, y);
                    radiation.drawPixel(x + z * resolution, y);
                    heat.drawPixel(x + z * resolution, y);
                }
            }
        }

        // Copy the temperature data.
        var temperatureTexture = new Texture(heat);
        temperatureTexture.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
        temperatureTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        // Copy the data into the diffusivity texture resource.
        this.diffusivity = new Texture(diffusivity);
        this.diffusivity.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
        this.diffusivity.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        // Generate a radiation profile from the input data.
        this.radiation = new Texture(radiation);
        this.radiation.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
        this.radiation.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        // Draw the initial temperature configuration to the framebuffer object.
        temperature.begin();
        _spriteBatch.begin();
        _spriteBatch.draw(temperatureTexture, 0, 0, 
            Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
        _spriteBatch.end();
        temperature.end();

        temperatureTexture.dispose();
    }

    public void update() 
    {
        temperature.begin();
        _shader.begin();

        // Bind heat storage texture.
        temperature.getColorBufferTexture().bind(0);
        _shader.setUniformi("u_texture", 0);

        // Bind the texture describing the thermal diffusivity.
        diffusivity.bind(1);
        _shader.setUniformi("u_diffusivity", 1);

        // Bind the texture describing the radiant input and output.
        radiation.bind(2);
        _shader.setUniformi("u_radiation", 2);

        // Bind uniforms for discrete time evolution and sizes.
        _shader.setUniformf("u_dt", Gdx.graphics.getDeltaTime() * 1f);
        _shader.setUniformf("u_dx", 1f / temperature.getWidth());
        _shader.setUniformf("u_dy", 1f / temperature.getHeight());
        _shader.setUniformf("u_len", resolution);

        // Calculate the heat equation approximation by drawing to the shader.
        _quadMesh.render(_shader, GL20.GL_TRIANGLES, 0, 6);
        _shader.end();
        temperature.end();
	}

    public void dispose() 
    {
        _shader.dispose();
        _spriteBatch.dispose();
        diffusivity.dispose();
        radiation.dispose();
        temperature.dispose();
        _quadMesh.dispose();
    }

    private static ShaderProgram _loadShader(final String vert, final String frag) 
    {
        final var res = new ShaderProgram(
			Gdx.files.getFileHandle(vert + ".vert", Files.FileType.Internal),
			Gdx.files.getFileHandle(frag + ".frag", Files.FileType.Internal));
		if (!res.isCompiled())
			throw new GdxRuntimeException(res.getLog());
		System.out.print(res.getLog());
		return res;
	}
}