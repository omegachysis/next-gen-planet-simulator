package edu.psu.planetsim.graphics;

import java.util.Random;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

import edu.psu.planetsim.AppState;
import edu.psu.planetsim.FastNoise;
import edu.psu.planetsim.Metrics;
import edu.psu.planetsim.FastNoise.FractalType;

public class TerrainBuilder 
{
    public static Model BuildTerrainModel(int dim, AppState.CelestialBody dto) 
    {
        var elevationMap = BuildElevationMap(dim, dto);

        var material = new Material(
            new TextureAttribute(TextureAttribute.Diffuse, BuildDiffuseTexture(1024, dto)),
            new TextureAttribute(TextureAttribute.Specular, BuildSpecularTexture(512, dto))
        );

        var res = new ModelBuilder().createSphere(2f, 2f, 2f, dim - 1, dim - 1, material,
                Usage.Position | Usage.Normal | Usage.TextureCoordinates);

        var mesh = res.meshes.first();
        float[] verts = new float[mesh.getNumVertices() * 8];
        mesh.getVertices(verts);

        for (int i = 0; i < elevationMap.length; i++) {
            var elev = elevationMap[i];
            verts[i * 8 + 0] *= elev;
            verts[i * 8 + 1] *= elev;
            verts[i * 8 + 2] *= elev;

            // Set custom texture coordinates:
            verts[i * 8 + 6] = (i / dim) / (float)dim;
            verts[i * 8 + 7] = (i % dim) / (float)dim;
        }

        FixSeamsAndFinishMesh(dim, elevationMap, mesh, verts);
        return res;
    }

    public static Model BuildThermometerField(int dim, AppState.CelestialBody dto,
    Texture temperatureTexture) 
    {
        var elevationMap = BuildElevationMap(dim, dto);

        var material = new Material(
            ColorAttribute.createDiffuse(Color.BLACK),
            new TextureAttribute(TextureAttribute.Emissive, temperatureTexture)
        );

        var res = new ModelBuilder();
        res.begin();

        var thermometerSize = dto.radius / dim * 2f;

        final var h = temperatureTexture.getHeight();
        for (int z = 0; z < dim; z++)
        {
            for (int y = 0; y < dim; y++)
            {
                for (int x = 0; x < dim; x++)
                {
                    // Convert the cartesian texture coordinates x,y,z
                    // into unit sphere cartesian coordinates.
                    var dx = x * 2.0 / dim - 1.0;
                    var dy = y * 2.0 / dim - 1.0;
                    var dz = z * 2.0 / dim - 1.0;
                    var r = new Vector3((float)dx, (float)dy, (float)dz);

                    // Convert the cartesian x,y,z coordinates 
                    // to spherical coordinates on the elevation map
                    // assuming we are on the surface of the unit sphere.
                    var latlon = Metrics.toSphericalCoords(r);
                    var latIndex = (int)(latlon.x * (dim - 1));
                    var lonIndex = (int)(latlon.y * (dim - 1));

                    // Find the elevation at this point above the unit sphere.
                    var elev = elevationMap[latIndex * dim + lonIndex] / dto.radius;

                    if (r.len() <= elev)
                    {
                        // Interior point.
                        var builder = new MeshBuilder();
                        builder.begin(Usage.Position | Usage.TextureCoordinates, GL20.GL_TRIANGLES);

                        BoxShapeBuilder.build(builder, 
                            (float)x / dim * 2f * dto.radius - 
                            dto.radius - thermometerSize * 0.5f,
                            (float)y / dim * 2f * dto.radius - 
                            dto.radius - thermometerSize * 0.5f,
                            (float)z / dim * 2f * dto.radius - 
                            dto.radius - thermometerSize * 0.5f,
                            thermometerSize, thermometerSize, thermometerSize);
                        var mesh = builder.end();
    
                        var verts = new float[mesh.getNumVertices() * mesh.getVertexSize() / 4];
                        mesh.getVertices(verts);
                        for (int i = 0; i < mesh.getNumVertices(); i++) {
                            var px = (float)x / dim;
                            var py = (float)y / dim;
                            var pz = (float)z / dim;
                            var tx = px * (h - 1);
                            var ty = py * (h - 1);
                            var tz = h * (int)(pz * (h - 1));

                            // Set new texture coordinates that map to the heat texture.
                            verts[i * 5 + 3] = (tx + tz) / (h * h);
                            verts[i * 5 + 4] = ty / h;
                        }
                        mesh.setVertices(verts);
    
                        res.part("" + x + y + z, mesh,
                            GL20.GL_TRIANGLES, material);
                    }
                }
            }
        }

        return res.end();
    }


    private static void FixSeamsAndFinishMesh(int dim, float[] elevationMap, 
        Mesh mesh, float[]  verts) 
    {
        // for terrain smoothing and seam-fixing.
        for (int i = 0; i < verts.length / 8; i++) {
            // This condition connects the last longitude with the first longitude
            // to share the same vertex per latitude.
            if ((i + 1) % (dim) == 0 && i != 0) {
                verts[i * 8 + 0] = verts[(i - dim + 1) * 8 + 0];
                verts[i * 8 + 1] = verts[(i - dim + 1) * 8 + 1];
                verts[i * 8 + 2] = verts[(i - dim + 1) * 8 + 2];
            }

            // This condition forces north pole vertices to share the same coordinate.
            else if (i <= dim && i != 0) {
                verts[i * 8 + 0] = verts[0 * 8 + 0];
                verts[i * 8 + 1] = verts[0 * 8 + 1];
                verts[i * 8 + 2] = verts[0 * 8 + 2];
            }

            // This condition forces south pole vertices to share the same coordinate.
            else if (i > (dim * (dim + 1))) {
                verts[i * 8 + 0] = verts[(dim * (dim + 1)) * 8 + 0];
                verts[i * 8 + 1] = verts[(dim * (dim + 1)) * 8 + 1];
                verts[i * 8 + 2] = verts[(dim * (dim + 1)) * 8 + 2];
            }
        }

        // Re-center the mesh around its geometric center.
        // var bounds = mesh.calculateBoundingBox();
        // var median = new Vector3();
        // bounds.getCenter(median);
        // for (int i = 0; i < elevationMap.length; i++) {
        //     verts[i * 8 + 0] -= median.x;
        //     verts[i * 8 + 1] -= median.y;
        //     verts[i * 8 + 2] -= median.z;
        // }

        // Following loops generate colors depending on a variety of inputs; currently
        // it
        // generates a topography/elevation map depending on elevationMap
        // The new VertexAttributes changes the structure of the vertex
        // Now if p stands for position coordinates, n stands for normal coordinates,
        // t stands for texture coordinates, and r/g/b/a stand for color/alpha values,
        // then the vertex in the float array is as follows; p p p r g b a n n n t t
        // The vertex is now 12 values long
        mesh.setVertices(verts);
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
                    alpha = MathUtils.clamp(alpha, 0, 1);
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

                var lumps = lumpsGen.GetNoise(v.x, v.y, v.z) - 1f;
                var mountains = mountainGen.GetNoise(v.x, v.y, v.z) - 1f;
                var noise = lumps * 0.1f + mountains * 0.1f;
                res[lat * dim + lon] = (float)dto.radius + noise * (float)dto.radius;
            }
        }

        return res;
    }
}