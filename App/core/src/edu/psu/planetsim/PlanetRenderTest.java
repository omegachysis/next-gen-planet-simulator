package edu.psu.planetsim;

import java.util.Random;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import edu.psu.planetsim.FastNoise.FractalType;

public class PlanetRenderTest implements ApplicationListener {

    public PerspectiveCamera cam;
    public CameraInputController camController;
    public Model model;
    public Model model1;
    public Model model2;
    public ModelInstance instance;
    public ModelInstance instance1;
    public ModelInstance instance2;
    public ModelBatch modelBatch;
    public Environment environment;
    public Mesh mesh1;
    public Mesh mesh2;
    public Texture texture1;

    @Override
    public void create() {

        modelBatch = new ModelBatch();

        cam = new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 200f;
        cam.update();
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.1f, 0.1f, 0.1f, 1f));
        environment.add(new DirectionalLight().set(1f, 1f, 1f, 1f, 0.2f, 0.2f));

        var modelBuilder = new ModelBuilder();

        int latitudes = 180;
        int longitudes = 180;
        
        // planet terrain
        model1 = modelBuilder.createSphere(5f, 5f, 5f, longitudes - 1, latitudes - 1,
                new Material(ColorAttribute.createDiffuse(Color.WHITE)),
                Usage.Position | Usage.Normal | Usage.TextureCoordinates | Usage.ColorUnpacked);

        var rand = new Random();

        mesh1 = model1.meshes.first();

        // var imageFileHandle = Gdx.files.getFileHandle("textest2.jpg", Files.FileType.Internal);
        // texture1 = new Texture(imageFileHandle);
        // texture1.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        // var textureAttribute1 = new TextureAttribute(TextureAttribute.Diffuse, texture1);

        float[] verticesHolder = new float[mesh1.getNumVertices() * 12];
        mesh1.getVertices(verticesHolder);

        float[] elevationMapTester = new float[latitudes * longitudes];
        // We'll get some input map of elevations by latitude/longitude:
        float[] elevationMap = new float[latitudes * longitudes];
        // These are stand-ins for values the program will pass into the generation loop
        int planetSeed = rand.nextInt();

        // Create noise generation modules.
        var lumpsGen = new FastNoise(planetSeed);
        lumpsGen.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
        // Fractal brownian motion for nice lumps and things:
        lumpsGen.SetFractalType(FractalType.FBM);
        lumpsGen.SetFractalOctaves(6);
        lumpsGen.SetFrequency(0.2f);

        var mountainGen = new FastNoise(planetSeed);
        mountainGen.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
        // Ridged multi-fractals for mountains and valleys:
        mountainGen.SetFractalType(FractalType.RigidMulti);
        mountainGen.SetFractalOctaves(8);
        mountainGen.SetFrequency(0.5f);

        // Generate terrain noise
        for (int i = 0; i < elevationMap.length; i++)
        {
        	float px = verticesHolder[i * 12 + 0];
            float py = verticesHolder[i * 12 + 1];
            float pz = verticesHolder[i * 12 + 2];
            
            // float r = verticesHolder[i * 12 + 3];
            // float g = verticesHolder[i * 12 + 4];
            // float b = verticesHolder[i * 12 + 5];

            var lumps = (lumpsGen.GetNoise(px, py, pz) + 1f) * 0.5f;
            var mountains = (mountainGen.GetNoise(px, py, pz) + 1f) * 0.5f;
            var noise = lumps + mountains * 0.2f;
            
            var elev = noise + 2f;
            elevationMap[i] = 5f * elev;
            verticesHolder[i * 12 + 0] *= elev;
            verticesHolder[i * 12 + 1] *= elev;
            verticesHolder[i * 12 + 2] *= elev;
        }
        
        // Following loops generate colors depending on a variety of inputs; currently it
        // generates a topography/elevation map depending on elevationMap
        // The new VertexAttributes changes the structure of the vertex
        // Now if p stands for position coordinates, n stands for normal coordinates,
        // t stands for texture coordinates, and r/g/b/a stand for color/alpha values,
        // then the vertex in the float array is as follows; p p p r g b a n n n t t
        // The vertex is now 12 values long
        
        // These values are needed for the topography map to provide reasonable color
        // variety, as topography maps are dependent on the difference between maximum elevation
        // and maximum depth, though sometimes sea level is used. 
        float highestElev = 0f;
        float lowestElev = 999f;
        for (int i = 0; i < elevationMap.length; i++)
        {   
            if (elevationMap[i] > highestElev)
            {
            	highestElev = elevationMap[i];
            }
            if (elevationMap[i] < lowestElev)
            {
            	lowestElev = elevationMap[i];
            }
        }
        
        // This is used for calculating the color value.
        // Colors are in the range of float [0, 1]. Alpha is not used in this iteration, but
        // the index of the alpha value should be verticesHolder[i * 12 + 6].
        float medianElev = (highestElev + lowestElev) / 2;
        for (int i = 0; i < elevationMap.length; i++)
        {   
        	float colorModifier; // This determines the actual color saturation.
        	// colorModifier is assigned a value in the range of float [0, 0.5].
        	// RGB values are within the range of float [0.25, 0.75]. Values that are too extreme
        	// tend to be overly bright and saturated. Values over 1.0 interact unpredictably
        	// with the lighting.
            if (elevationMap[i] > medianElev)
            {
            	colorModifier = ((elevationMap[i] - medianElev) / (highestElev - medianElev)) / 2;
            	verticesHolder[i * 12 + 3] = 0.25f + colorModifier;
                verticesHolder[i * 12 + 4] = 0.75f - colorModifier;
                verticesHolder[i * 12 + 5] = 0.25f;
            }
            else
            {
            	colorModifier = ((medianElev - elevationMap[i]) / (medianElev - lowestElev)) / 2;
            	verticesHolder[i * 12 + 3] = 0.25f;
                verticesHolder[i * 12 + 4] = 0.75f - colorModifier;
                verticesHolder[i * 12 + 5] = 0.25f + colorModifier;
            }
        }
        
        // planet sea
        model = modelBuilder.createSphere(medianElev, medianElev, medianElev, longitudes - 1, latitudes - 1,
                new Material(ColorAttribute.createDiffuse(Color.BLUE)),
                Usage.Position | Usage.Normal | Usage.TextureCoordinates | Usage.ColorUnpacked);
        
        // planet atmosphere
        model2 = modelBuilder.createSphere(highestElev, highestElev, highestElev, longitudes - 1, latitudes - 1,
                new Material(ColorAttribute.createDiffuse(0.5f, 0.8f, 0.9f, 0.1f)),
                Usage.Position | Usage.Normal | Usage.TextureCoordinates | Usage.ColorUnpacked);
        
        mesh2 = model2.meshes.first();

        float[] atmoHolder = new float[mesh2.getNumVertices() * 12];
        mesh2.getVertices(atmoHolder);
        
        for (int i = 0; i < mesh2.getNumVertices(); i++)
        {
        	float colorSeed = (float)rand.nextDouble();
        	atmoHolder[i * 12 + 3] += (0.5f * colorSeed);
            atmoHolder[i * 12 + 4] += (0.2f * colorSeed);
            atmoHolder[i * 12 + 5] += (0.1f * colorSeed);
            atmoHolder[i * 12 + 6] += (0.9f * colorSeed);
        }
        
        mesh2.setVertices(atmoHolder);

        // We won't need to use this seam-matching stuff for now since 
        // the fact that we access the 3D noise in spherical coordinates 
        // takes care of that for us, but we will probably need this again soon.

	    // for (int i = 0; i < verticesHolder.length / 8; i++)
	    // {
	    // 	float nx = verticesHolder[i * 8 + 3];
	    // 	float ny = verticesHolder[i * 8 + 4];
	    // 	float nz = verticesHolder[i * 8 + 5];

	    // 	var workVector = new Vector3(nx, ny, nz);
	    // 	workVector = workVector.scl(elevationMap[i]);
	    // 	verticesHolder[i * 8 + 0] = workVector.x;
	    //     verticesHolder[i * 8 + 1] = workVector.y;
	    //     verticesHolder[i * 8 + 2] = workVector.z;
	    // }
        // for terrain smoothing and seam-fixing; this should ALWAYS run
        // int f = 0;
        // for (int i = 0; i < verticesHolder.length / 8; i++)
        // {
        // 	// This condition connects the last longitude with the first longitude to share the same vertex per latitude.
        // 	if ((i+1) % (longitudes) == 0 && i != 0)
        // 	{
        // 		verticesHolder[i * 8 + 0] = verticesHolder[(i - longitudes + 1) * 8 + 0];
        //         verticesHolder[i * 8 + 1] = verticesHolder[(i - longitudes + 1) * 8 + 1];
        //         verticesHolder[i * 8 + 2] = verticesHolder[(i - longitudes + 1) * 8 + 2];
        // 	}
        	
        // 	// This condition forces north pole vertices to share the same coordinate.
        // 	else if (i <= longitudes && i != 0)
        // 	{
        // 		verticesHolder[i * 8 + 0] = verticesHolder[0 * 8 + 0];
        //         verticesHolder[i * 8 + 1] = verticesHolder[0 * 8 + 1];
        //         verticesHolder[i * 8 + 2] = verticesHolder[0 * 8 + 2];
        // 	}
        	
        // 	// This condition forces south pole vertices to share the same coordinate.
        // 	else if (i > (longitudes * (latitudes + 1)))
        // 	{
        // 		verticesHolder[i * 8 + 0] = verticesHolder[(longitudes * (latitudes + 1)) * 8 + 0];
        //         verticesHolder[i * 8 + 1] = verticesHolder[(longitudes * (latitudes + 1)) * 8 + 1];
        //         verticesHolder[i * 8 + 2] = verticesHolder[(longitudes * (latitudes + 1)) * 8 + 2];
        // 	}
        	
        //     float px = verticesHolder[i * 8 + 0];
        //     float py = verticesHolder[i * 8 + 1];
        //     float pz = verticesHolder[i * 8 + 2];

        //     // This if-else passes the result of planetCenter.dst(x, y, z) to the elevationMapTester[].
        //     // I (Danny Ruan) will clean this up at some point because I don't like using empty thens
        //     // in if-elses. The first three conditions prevent reassignment of elevations to already-
        //     // existing coordinates.
            
        //     if ((i+1) % (longitudes+1) == 0 && i != 0)
        // 	{
        // 	}
        // 	else if (i <= longitudes && i != 0)
        // 	{
        // 	}
        // 	else if (i > (longitudes * (latitudes + 1)))
        // 	{
        // 	}
        // 	else
        // 	{
        // 		elevationMapTester[f] = planetCenter.dst(px, py, pz);
        // 		f++;
        // 	}
        	
        // }
        
        // Test code to check that the elevationMap is storing values correctly
        /*
        for (int i = 0; i < f; i++)
        {
        	System.out.println(elevationMapTester[i]);
        }
        */
        mesh1.setVertices(verticesHolder);
        instance1 = new ModelInstance(model1, 0f, 0f, 0f);
        instance = new ModelInstance(model, 0f, 0f, 0f);
        instance2 = new ModelInstance(model2, 0f, 0f, 0f);
        // var tempMaterial = instance1.materials.get(0);
        // tempMaterial.set(textureAttribute1);
        
        instance2.materials.get(0).set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
    }
    
    @Override
    public void render () {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        


        
        camController.update();

        modelBatch.begin(cam);
        modelBatch.render(instance1, environment);
        modelBatch.render(instance, environment);
        modelBatch.render(instance2, environment);
        modelBatch.end();
        
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void dispose () {
    	modelBatch.dispose();
    	model1.dispose();
    }

    @Override
    public void resume () {
    }

    @Override
    public void resize (int width, int height) {
    }

    @Override
    public void pause () {
    }
}