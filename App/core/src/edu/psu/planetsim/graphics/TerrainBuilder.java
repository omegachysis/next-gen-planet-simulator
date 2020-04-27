package edu.psu.planetsim.graphics;

import java.time.LocalDate;
import java.util.Random;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Filter;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import edu.psu.planetsim.AppState;
import edu.psu.planetsim.FastNoise;
import edu.psu.planetsim.Metrics;
import edu.psu.planetsim.FastNoise.FractalType;

public class TerrainBuilder {
    public static Model BuildTerrainModel(int dim, AppState.CelestialBody dto) {
        var elevationMap = BuildElevationMap(dim, dto);

        var material = new Material(
            new TextureAttribute(TextureAttribute.Diffuse, BuildDiffuseTexture(1024, dto)),
            new TextureAttribute(TextureAttribute.Specular, BuildSpecularTexture(512, dto))
        );

        var res = new ModelBuilder().createSphere(2f, 2f, 2f, dim - 1, dim - 1, material,
                Usage.Position | Usage.Normal | Usage.TextureCoordinates);

        var mesh = res.meshes.first();
        float[] verticesHolder = new float[mesh.getNumVertices() * 8];
        mesh.getVertices(verticesHolder);

        for (int i = 0; i < elevationMap.length; i++) {
            var elev = elevationMap[i];
            verticesHolder[i * 8 + 0] *= elev;
            verticesHolder[i * 8 + 1] *= elev;
            verticesHolder[i * 8 + 2] *= elev;

            // Set custom texture coordinates:
            verticesHolder[i * 8 + 6] = (i / dim) / (float)dim;
            verticesHolder[i * 8 + 7] = (i % dim) / (float)dim;
        }

        // for terrain smoothing and seam-fixing.
        for (int i = 0; i < verticesHolder.length / 8; i++) {
            // This condition connects the last longitude with the first longitude
            // to share the same vertex per latitude.
            if ((i + 1) % (dim) == 0 && i != 0) {
                verticesHolder[i * 8 + 0] = verticesHolder[(i - dim + 1) * 8 + 0];
                verticesHolder[i * 8 + 1] = verticesHolder[(i - dim + 1) * 8 + 1];
                verticesHolder[i * 8 + 2] = verticesHolder[(i - dim + 1) * 8 + 2];
            }

            // This condition forces north pole vertices to share the same coordinate.
            else if (i <= dim && i != 0) {
                verticesHolder[i * 8 + 0] = verticesHolder[0 * 8 + 0];
                verticesHolder[i * 8 + 1] = verticesHolder[0 * 8 + 1];
                verticesHolder[i * 8 + 2] = verticesHolder[0 * 8 + 2];
            }

            // This condition forces south pole vertices to share the same coordinate.
            else if (i > (dim * (dim + 1))) {
                verticesHolder[i * 8 + 0] = verticesHolder[(dim * (dim + 1)) * 8 + 0];
                verticesHolder[i * 8 + 1] = verticesHolder[(dim * (dim + 1)) * 8 + 1];
                verticesHolder[i * 8 + 2] = verticesHolder[(dim * (dim + 1)) * 8 + 2];
            }
        }

        // Following loops generate colors depending on a variety of inputs; currently
        // it
        // generates a topography/elevation map depending on elevationMap
        // The new VertexAttributes changes the structure of the vertex
        // Now if p stands for position coordinates, n stands for normal coordinates,
        // t stands for texture coordinates, and r/g/b/a stand for color/alpha values,
        // then the vertex in the float array is as follows; p p p r g b a n n n t t
        // The vertex is now 12 values long
        mesh.setVertices(verticesHolder);

        // Re-center the mesh around its geometric center.
        var bounds = mesh.calculateBoundingBox();
        var median = new Vector3();
        bounds.getCenter(median);
        for (int i = 0; i < elevationMap.length; i++) {
            verticesHolder[i * 8 + 0] -= median.x;
            verticesHolder[i * 8 + 1] -= median.y;
            verticesHolder[i * 8 + 2] -= median.z;
        }

        return res;
    }

    public static Texture BuildDiffuseTexture(int dim, AppState.CelestialBody dto)
    {
        var pix = new Pixmap(dim, dim, Format.RGB888);
        var elevMap = BuildElevationMap(dim, dto);
        var rand = new Random();
        for (int lat = 0; lat < dim; lat++)
        {
            for (int lon = 0; lon < dim; lon++)
            {
                var elev = elevMap[lat * dim + lon];
                if (elev < dto.seaLevel) {
                    var alpha = 1f - (dto.seaLevel - elev) / dto.radius / 0.2f;
                    alpha = MathUtils.clamp(alpha, 0.4f, 1);
                    var r1 = rand.nextFloat() * .1f - .05f + 0.1f;
                    var g1 = rand.nextFloat() * .1f - .05f + 0.1f;
                    var b1 = rand.nextFloat() * .1f - .05f + 0.7f;
                    pix.setColor(r1 * alpha, g1 * alpha, b1 * alpha, 1f);
                }
                else {
                    var r1 = rand.nextFloat() * .1f - .05f + 0.1f;
                    var g1 = rand.nextFloat() * .1f - .05f + 0.65f;
                    var b1 = rand.nextFloat() * .1f - .05f + 0.3f;
                    var r2 = rand.nextFloat() * .1f - .05f + 0.8f;
                    var g2 = rand.nextFloat() * .1f - .05f + 0.8f;
                    var b2 = rand.nextFloat() * .1f - .05f + 0.4f;
                    var alpha = (elev - dto.seaLevel) / dto.radius / 0.1f; 
                    pix.setColor(
                        r2 * alpha + r1 * (1 - alpha), 
                        g2 * alpha + g1 * (1 - alpha), 
                        b2 * alpha + b1 * (1 - alpha), 
                        1f);
                }
                    
                pix.drawPixel(lat, lon);
            }
        }
        
        var res = new Texture(pix);
        res.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        return res;
    }

    public static Texture BuildSpecularTexture(int dim, AppState.CelestialBody dto)
    {
        var pix = new Pixmap(dim, dim, Format.RGB888);
        var elevMap = BuildElevationMap(dim, dto);
        for (int lat = 0; lat < dim; lat++)
        {
            for (int lon = 0; lon < dim; lon++)
            {
                var elev = elevMap[lat * dim + lon];
                if (elev < dto.seaLevel)
                    pix.setColor(1f, 1f, 1f, 1f);
                else
                    pix.setColor(.2f, .2f, .2f, 1f);
                pix.drawPixel(lat, lon);
            }
        }
        
        var res = new Texture(pix);
        res.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        return res;
    }

    public static float[] BuildElevationMap(int dim, AppState.CelestialBody dto)
    {
        var res = new float[dim * dim];

        // Create noise generation modules.
        var lumpsGen = new FastNoise(dto.seed);
        lumpsGen.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
        // Fractal brownian motion for nice lumps and things:
        lumpsGen.SetFractalType(FractalType.FBM);
        lumpsGen.SetFractalOctaves(8);
        lumpsGen.SetFrequency(0.4f);

        var mountainGen = new FastNoise(dto.seed);
        mountainGen.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
        // Ridged multi-fractals for mountains and valleys:
        mountainGen.SetFractalType(FractalType.RigidMulti);
        mountainGen.SetFractalOctaves(8);
        mountainGen.SetFrequency(0.5f);

        // Generate terrain noise
        for (int lat = 0; lat < dim; lat++)
        {
            for (int lon = 0; lon < dim; lon++)
            {
                var v = Metrics.toCartesianCoords((float)lat / dim, (float)lon / dim);

                var lumps = lumpsGen.GetNoise(v.x, v.y, v.z);
                var mountains = mountainGen.GetNoise(v.x, v.y, v.z);
                var noise = lumps * 0.1f + mountains * 0.1f;
                res[lat * dim + lon] = (float)dto.radius + noise * (float)dto.radius;
            }
        }

        return res;
    }
}