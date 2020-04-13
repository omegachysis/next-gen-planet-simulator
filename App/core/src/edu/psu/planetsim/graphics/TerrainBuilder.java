package edu.psu.planetsim.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class TerrainBuilder 
{
    public static Model BuildTerrainModel(float[] elevationMap)
    {
        // Elevation maps will always be square arrays, so we can find the 
        // dimension from that.
        var dim = (int)Math.sqrt(elevationMap.length);

        var res = new ModelBuilder().createSphere(1f, 1f, 1f, 
            dim - 1, dim - 1, new Material(ColorAttribute.createDiffuse(Color.WHITE)),
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
        
        return res;
    }

    public static float[] MakeRandomElevationMap(int dim, double radius)
    {
        var fRadius = (float)radius;
        var res = new float[dim * dim];
        for (int i = 0; i < dim * dim; i++)
            res[i] = fRadius;
        return res;
    }
}