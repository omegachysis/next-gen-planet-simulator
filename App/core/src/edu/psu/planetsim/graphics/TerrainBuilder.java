package edu.psu.planetsim.graphics;

import java.time.LocalDate;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import edu.psu.planetsim.FastNoise;
import edu.psu.planetsim.FastNoise.FractalType;

public class TerrainBuilder 
{
    public static Model BuildTerrainModel(float[] elevationMap)
    {
        // Elevation maps will always be square arrays, so we can find the 
        // dimension from that.
        var dim = (int)Math.sqrt(elevationMap.length);

        var material = new Material(
            ColorAttribute.createDiffuse(new Color(0.05f, 0.4f, 0.1f, 1f)),
            ColorAttribute.createSpecular(new Color(0.4f, 0.4f, 0.4f, 1f)),
            FloatAttribute.createShininess(1f)
        );

        var res = new ModelBuilder().createSphere(1f, 1f, 1f, 
            dim - 1, dim - 1, material,
            Usage.Position | Usage.Normal);

        var mesh = res.meshes.first();
        float[] verticesHolder = new float[mesh.getNumVertices() * 6];
        mesh.getVertices(verticesHolder);

        for (int i = 0; i < elevationMap.length; i++)
        {
            var elev = elevationMap[i];
            verticesHolder[i * 6 + 0] *= elev;
            verticesHolder[i * 6 + 1] *= elev;
            verticesHolder[i * 6 + 2] *= elev;
        }
        
        // for terrain smoothing and seam-fixing.
        for (int i = 0; i < verticesHolder.length / 6; i++)
        {
            // This condition connects the last longitude with the first longitude 
            // to share the same vertex per latitude.
        	if ((i+1) % (dim) == 0 && i != 0)
        	{
        		verticesHolder[i * 6 + 0] = verticesHolder[(i - dim + 1) * 6 + 0];
                verticesHolder[i * 6 + 1] = verticesHolder[(i - dim + 1) * 6 + 1];
                verticesHolder[i * 6 + 2] = verticesHolder[(i - dim + 1) * 6 + 2];
        	}
        	
        	// This condition forces north pole vertices to share the same coordinate.
        	else if (i <= dim && i != 0)
        	{
        		verticesHolder[i * 6 + 0] = verticesHolder[0 * 6 + 0];
                verticesHolder[i * 6 + 1] = verticesHolder[0 * 6 + 1];
                verticesHolder[i * 6 + 2] = verticesHolder[0 * 6 + 2];
        	}
        	
        	// This condition forces south pole vertices to share the same coordinate.
        	else if (i > (dim * (dim + 1)))
        	{
        		verticesHolder[i * 6 + 0] = verticesHolder[(dim * (dim + 1)) * 6 + 0];
                verticesHolder[i * 6 + 1] = verticesHolder[(dim * (dim + 1)) * 6 + 1];
                verticesHolder[i * 6 + 2] = verticesHolder[(dim * (dim + 1)) * 6 + 2];
        	}
        }
        
        // Following loops generate colors depending on a variety of inputs; currently it
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
        for (int i = 0; i < elevationMap.length; i++)
        {
            verticesHolder[i * 6 + 0] -= median.x;
            verticesHolder[i * 6 + 1] -= median.y;
            verticesHolder[i * 6 + 2] -= median.z;
        }
        
        return res;
    }

    public static Model BuildOceanModel(float[] elevationMap, 
        float seaLevel, Color oceanColor, Model terrainModel)
    {
        var dim = (int)Math.sqrt(elevationMap.length);

        var material = new Material(
            ColorAttribute.createDiffuse(new Color(0.15f, 0.25f, 0.8f, 1f)),
            ColorAttribute.createSpecular(new Color(0.8f, 0.8f, 0.8f, 1f)),
            FloatAttribute.createShininess(24f)
        );

        return new ModelBuilder().createSphere(
            seaLevel, seaLevel, seaLevel, dim - 1, dim - 1,
            material, Usage.Position | Usage.Normal);
    }

    public static float[] MakeRandomElevationMap(int dim, double radius)
    {
        var planetSeed = (int)(System.currentTimeMillis() % (Integer.MAX_VALUE + 1));
        var res = new float[dim * dim];

        // Create noise generation modules.
        var lumpsGen = new FastNoise(planetSeed);
        lumpsGen.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
        // Fractal brownian motion for nice lumps and things:
        lumpsGen.SetFractalType(FractalType.FBM);
        lumpsGen.SetFractalOctaves(6);
        lumpsGen.SetFrequency(0.4f);

        var mountainGen = new FastNoise(planetSeed);
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
                // Convert to spherical coordinates.
                var theta = lat * Math.PI / dim;
                var phi = lon * Math.PI * 2 / dim;
                var x = (float)(Math.sin(theta) * Math.cos(phi));
                var y = (float)(Math.sin(theta) * Math.sin(phi));
                var z = (float)(Math.cos(theta));

                var lumps = lumpsGen.GetNoise(x, y, z);
                var mountains = mountainGen.GetNoise(x, y, z);
                var noise = lumps * 0.1f + mountains * 0.1f;
                res[lat * dim + lon] = (float)radius + noise * (float)radius;
            }
        }

        return res;
    }
}