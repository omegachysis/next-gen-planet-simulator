package edu.psu.planetsim;

import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

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
    public Texture texture1;

    @Override
    public void create () {
    	
    	modelBatch = new ModelBatch();

        cam = new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0,0,0);
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
        
        model1 = modelBuilder.createSphere(5f, 5f, 5f, longitudes - 1, latitudes - 1,
                new Material(ColorAttribute.createDiffuse(Color.WHITE)),
                Usage.Position | Usage.Normal | Usage.TextureCoordinates);

        var rand = new Random();

        mesh1 = model1.meshes.first();
        
        var imageFileHandle = Gdx.files.getFileHandle("textest2.jpg", Files.FileType.Internal);
        texture1 = new Texture(imageFileHandle);
        var textureAttribute1 = new TextureAttribute(TextureAttribute.Diffuse, texture1);

        // Our model1 usage flags are Usage.Position and Usage.Normal,
        // thus every vertex has a Position vector and a Normal vector.
        // Each of these has three float components, thus the whole vertex array 
        // has 6 floats for every vertex:
        float[] verticesHolder = new float[mesh1.getNumVertices() * 8];
        mesh1.getVertices(verticesHolder);

        float[] elevationMapTester = new float[latitudes * longitudes];
        // We'll get some input map of elevations by latitude/longitude:
        float[] elevationMap = new float[latitudes * longitudes];
        // These are stand-ins for values the program will pass into the generation loop
        float terrainEccentricity = 0.2f;
        float terrainRoughness = 0.5f;
        float planetRadius = 5f;
        for (int i = 0; i < elevationMap.length; i++)
        {
        	double val = terrainEccentricity * rand.nextDouble();
            elevationMap[i] = planetRadius;
            elevationMap[i] += val;
            if (i > longitudes)
            {
            	float avgElevation = (elevationMap[i-1] + elevationMap[i-longitudes] + elevationMap[i-longitudes+1] + elevationMap[i-longitudes-1]) / 4;
            	float localRoughness = avgElevation - planetRadius;
            	if (avgElevation < 7)
            	{
            		System.out.println(avgElevation);
            	}
            	if (rand.nextDouble() <= (terrainRoughness + localRoughness))
            	{
            		elevationMap[i] = avgElevation;
            		elevationMap[i] += val;
            	}
            	else
            	{
            		//elevationMap[i] = (elevationMap[i] + avgElevation)/2;
            	}
            }
        }
        boolean useMap = true;
        
        var planetCenter = new Vector3();

        // for generating using elevationMap
        if (useMap)
        {
	        for (int i = 0; i < verticesHolder.length / 8; i++)
	        {
	        	float px = verticesHolder[i * 8 + 0];
	            float py = verticesHolder[i * 8 + 1];
	            float pz = verticesHolder[i * 8 + 2];
	            float nx = verticesHolder[i * 8 + 3];
	            float ny = verticesHolder[i * 8 + 4];
	            float nz = verticesHolder[i * 8 + 5];
	            float tx = verticesHolder[i * 8 + 6];
	            float ty = verticesHolder[i * 8 + 7];
	            
	            var workVector = new Vector3(nx, ny, nz);
	            workVector = workVector.scl(elevationMap[i]);
	            verticesHolder[i * 8 + 0] = workVector.x;
	            verticesHolder[i * 8 + 1] = workVector.y;
	            verticesHolder[i * 8 + 2] = workVector.z;
	            px = workVector.x;
	            py = workVector.y;
	            pz = workVector.z;
	        }
        }
              
        // for generating procedurally
        else
        {
	        for (int i = 0; i < verticesHolder.length / 8; i++)
	        {
	        	float px = verticesHolder[i * 8 + 0];
	            float py = verticesHolder[i * 8 + 1];
	            float pz = verticesHolder[i * 8 + 2];
	            float nx = verticesHolder[i * 8 + 3];
	            float ny = verticesHolder[i * 8 + 4];
	            float nz = verticesHolder[i * 8 + 5];
	            float tx = verticesHolder[i * 8 + 6];
	            float ty = verticesHolder[i * 8 + 7];
	            
	        	double val = 0.04f * rand.nextDouble();
        		
                verticesHolder[i * 8 + 0] += val;
                verticesHolder[i * 8 + 1] += val;
                verticesHolder[i * 8 + 2] += val;
	        }
        }
        
        // for terrain smoothing and seam-fixing; this should ALWAYS run
        int f = 0;
        for (int i = 0; i < verticesHolder.length / 8; i++)
        {
            // Note: this does not work on the pole vertices (e.g. for latitude = 4 and longitude = 4, verticesHolder[0]
            // actually corresponds to verticesHolder[3], not verticesHolder[4]; likewise verticesHolder[21] corresponds
            // to verticesHolder[24], not verticesHolder[20].
        	
        	// This condition connects the last longitude with the first longitude to share the same vertex per latitude.
        	if ((i+1) % (longitudes) == 0 && i != 0)
        	{
        		verticesHolder[i * 8 + 0] = verticesHolder[(i - longitudes + 1) * 8 + 0];
                verticesHolder[i * 8 + 1] = verticesHolder[(i - longitudes + 1) * 8 + 1];
                verticesHolder[i * 8 + 2] = verticesHolder[(i - longitudes + 1) * 8 + 2];
        	}
        	
        	// This condition forces north pole vertices to share the same coordinate.
        	else if (i <= longitudes && i != 0)
        	{
        		verticesHolder[i * 8 + 0] = verticesHolder[0 * 8 + 0];
                verticesHolder[i * 8 + 1] = verticesHolder[0 * 8 + 1];
                verticesHolder[i * 8 + 2] = verticesHolder[0 * 8 + 2];
        	}
        	
        	// This condition forces south pole vertices to share the same coordinate.
        	else if (i > (longitudes * (latitudes + 1)))
        	{
        		verticesHolder[i * 8 + 0] = verticesHolder[(longitudes * (latitudes + 1)) * 8 + 0];
                verticesHolder[i * 8 + 1] = verticesHolder[(longitudes * (latitudes + 1)) * 8 + 1];
                verticesHolder[i * 8 + 2] = verticesHolder[(longitudes * (latitudes + 1)) * 8 + 2];
        	}

        	// Vertex format goes like pppnnnpppnnn...
            // where p are position components, n are normal components.
        	
            float px = verticesHolder[i * 8 + 0];
            float py = verticesHolder[i * 8 + 1];
            float pz = verticesHolder[i * 8 + 2];
            float nx = verticesHolder[i * 8 + 3];
            float ny = verticesHolder[i * 8 + 4];
            float nz = verticesHolder[i * 8 + 5];
            float tx = verticesHolder[i * 8 + 6];
            float ty = verticesHolder[i * 8 + 7];

            // This if-else passes the result of planetCenter.dst(x, y, z) to the elevationMapTester[].
            // I (Danny Ruan) will clean this up at some point because I don't like using empty thens
            // in if-elses. The first three conditions prevent reassignment of elevations to already-
            // existing coordinates.
            
            if ((i+1) % (longitudes+1) == 0 && i != 0)
        	{
        	}
        	else if (i <= longitudes && i != 0)
        	{
        	}
        	else if (i > (longitudes * (latitudes + 1)))
        	{
        	}
        	else
        	{
        		elevationMapTester[f] = planetCenter.dst(px, py, pz);
        		f++;
        	}
        	
        }
        
        // Test code to check that the elevationMap is storing values correctly
        /*
        for (int i = 0; i < f; i++)
        {
        	System.out.println(elevationMapTester[i]);
        }
        */
        mesh1.setVertices(verticesHolder);
        instance1 = new ModelInstance(model1, 0f, 0f, 0f);
        var tempMaterial = instance1.materials.get(0);
        tempMaterial.set(textureAttribute1);
    }
    
    @Override
    public void render () {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        camController.update();

        modelBatch.begin(cam);
        modelBatch.render(instance1, environment);
        modelBatch.end();
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