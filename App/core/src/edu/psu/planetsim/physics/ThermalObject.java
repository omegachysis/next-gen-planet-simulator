package edu.psu.planetsim.physics;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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

/** Describes a thermal profile and a diffusivity profile 
 * for a celestial body.
 */
public class ThermalObject
{
    private final ShaderProgram _shader;
    private final SpriteBatch _spriteBatch;
    private final Texture _temperature;
    private final Texture _diffusivity;
    private final Texture _radiation;
    private final FrameBuffer _fbo;
    private final Mesh _quadMesh;
    private final int _resolution;

    public ThermalObject(int resolution, float[] elevationMap)
    {
        _resolution = resolution;

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
        _fbo = new FrameBuffer(Format.RGB888,
            _resolution * _resolution, _resolution, false);
        _fbo.getColorBufferTexture().setFilter(
            TextureFilter.Nearest, TextureFilter.Nearest);
        
        // Create a spritebatch that will render the quad to perform the calculations.
        _spriteBatch = new SpriteBatch();

        // Generate a temperature profile from the input data.
        var pix = new Pixmap(_resolution * _resolution, _resolution, Format.RGB888);
        pix.setFilter(Filter.NearestNeighbour);
        pix.setColor(0f, 0f, 0f, 1f);
        pix.fill();
        _temperature = new Texture(pix);
        _temperature.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
        _temperature.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        // Generate a diffusivity texture from the elevation data,
        // making areas above the elevation have zero diffusivity, 
        // while areas in the interior have non-zero diffusivity.
        pix = new Pixmap(_resolution * _resolution, _resolution, Format.RGB888);
        pix.setFilter(Filter.NearestNeighbour);

        // Loop through every pixel of the diffusivity texture and 
        // terminate whether the point it represents is an interior point,
        // a boundary point, or an exterior point.
        var elevMapLen = (int)Math.sqrt(elevationMap.length);
        var maxElev = 0f;
        for (var elev : elevationMap)
            maxElev = Math.max(maxElev, elev);

        for (int z = 0; z < _resolution; z++)
        {
            for (int y = 0; y < _resolution; y++)
            {
                for (int x = 0; x < _resolution; x++)
                {

                    // Convert the cartesian texture coordinates x,y,z
                    // into unit sphere cartesian coordinates.
                    var dx = x * 2.0 / _resolution - 1.0;
                    var dy = y * 2.0 / _resolution - 1.0;
                    var dz = z * 2.0 / _resolution - 1.0;

                    // Find the distance we are from the center.
                    var dist = new Vector3((float)dx, (float)dy, (float)dz).len();

                    // Convert the cartesian x,y,z coordinates 
                    // to spherical coordinates on the elevation map
                    // assuming we are on the surface of the unit sphere.
                    var lat = Math.acos(-dz / dist);
                    var lon = Math.atan2(dy, dx);
                    var latIndex = (int)(lat * elevMapLen / Math.PI);
                    var lonIndex = (int)(lon * elevMapLen / Math.PI / 2);
                    if (lonIndex < 0) lonIndex = 0;

                    // Find the elevation at this point above the unit sphere.
                    var elev = elevationMap[latIndex * elevMapLen + lonIndex];
                    elev /= maxElev; // Convert elevations to unit sphere space.
                    
                    // Find out whether this is interior, exterior, or boundary.
                    var boundaryThickness = 0.01f;

                    if (dist <= elev) 
                        // Interior
                        pix.setColor(1f, 1f, 1f, 1f);
                    else if (dist - elev <= boundaryThickness)
                        // Boundary
                        pix.setColor(0.5f, 0.5f, 0.5f, 1f);
                    else
                        // Exterior
                        pix.setColor(0f, 0f, 0f, 1f);

                    pix.drawPixel(x + z * _resolution, y);
                }
            }
        }

        // TODO: remove this
        PixmapIO.writePNG(new FileHandle("test.png"), pix);

        // Copy the data into the diffusivity texture resource.
        _diffusivity = new Texture(pix);
        _diffusivity.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
        _diffusivity.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        // Generate a radiation profile from the input data.
        // TODO
        pix = new Pixmap(_resolution * _resolution, _resolution, Format.RGB888);
        pix.setFilter(Filter.NearestNeighbour);
        pix.setColor(0f, 0f, 0f, 1f);
        pix.fill();
        _radiation = new Texture(pix);
        _radiation.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
        _radiation.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        // Draw the initial temperature configuration to the framebuffer object.
        _fbo.begin();
        _spriteBatch.begin();
        _spriteBatch.draw(_temperature, 0, 0, 
            Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
            _spriteBatch.end();
        _fbo.end();
    }

    public void update() 
    {
        _fbo.begin();
        _shader.begin();

        // Bind heat storage texture.
        _fbo.getColorBufferTexture().bind(0);
        _shader.setUniformi("u_texture", 0);

        // Bind the texture describing the thermal diffusivity.
        _diffusivity.bind(1);
        _shader.setUniformi("u_diffusivity", 1);

        // Bind the texture describing the radiant input and output.
        _radiation.bind(2);
        _shader.setUniformi("u_radiation", 2);

        // Bind uniforms for discrete time evolution and sizes.
        _shader.setUniformf("u_dt", Gdx.graphics.getDeltaTime() * 1f);
        _shader.setUniformf("u_dx", 1f / _fbo.getWidth());
        _shader.setUniformf("u_dy", 1f / _fbo.getHeight());
        _shader.setUniformf("u_len", _resolution);

        // Calculate the heat equation approximation by drawing to the shader.
        _quadMesh.render(_shader, GL20.GL_TRIANGLES, 0, 6);
        _shader.end();
        _fbo.end();
	}

    public void dispose() 
    {
        _shader.dispose();
        _spriteBatch.dispose();
        _temperature.dispose();
        _diffusivity.dispose();
        _radiation.dispose();
        _fbo.dispose();
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