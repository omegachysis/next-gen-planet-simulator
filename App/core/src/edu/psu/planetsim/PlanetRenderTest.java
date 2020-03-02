package edu.psu.planetsim;

import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

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
        environment.add(new DirectionalLight().set(1f, 1f, 1f, -1f, -0.8f, -0.2f));
 
        ModelBuilder modelBuilder = new ModelBuilder();
        /*
        model = modelBuilder.createBox(5f, 5f, 5f, 
            new Material(ColorAttribute.createDiffuse(Color.WHITE)),
            Usage.Position | Usage.Normal);
        instance = new ModelInstance(model);
        */
        
        int latitude = 64;
        
        int longitude = 64;
        
        model1 = modelBuilder.createSphere(1f, 1f, 1f, longitude, latitude,
                new Material(ColorAttribute.createDiffuse(Color.WHITE)),
                Usage.Position | Usage.Normal);

        Random rand = new Random();

        mesh1 = model1.meshes.first();

        // Our model1 usage flags are Usage.Position and Usage.Normal,
        // thus every vertex has a Position vector and a Normal vector.
        // Each of these has three float components, thus the whole vertex array 
        // has 6 floats for every vertex:
        float[] verticesHolder = new float[mesh1.getNumVertices() * 6];
        mesh1.getVertices(verticesHolder);

        // We'll get some input map of elevations by latitude/longitude:
        float[] elevationMap = new float[latitude * longitude + 2];
        
        System.out.println(verticesHolder.length);
        
        for (int i = 0; i < verticesHolder.length / 6; i++)
        {
        	// Vertex format goes like pppnnnpppnnn...
            // where p are position components, n are normal components.
            float px = verticesHolder[i * 6 + 0];
            float py = verticesHolder[i * 6 + 1];
            float pz = verticesHolder[i * 6 + 2];
            float nx = verticesHolder[i * 6 + 3];
            float ny = verticesHolder[i * 6 + 4];
            float nz = verticesHolder[i * 6 + 5];
                
            // Note: this does not work on the pole vertices (e.g. for latitude = 4 and longitude = 4, verticesHolder[0]
            // actually corresponds to verticesHolder[3], not verticesHolder[4]; likewise verticesHolder[21] corresponds
            // to verticesHolder[24], not verticesHolder[20].
        	if ((i+1) % (longitude+1) == 0 && i != 0)
        	{
        		verticesHolder[i * 6 + 0] = verticesHolder[(i - longitude) * 6 + 0];
                verticesHolder[i * 6 + 1] = verticesHolder[(i - longitude) * 6 + 1];
                verticesHolder[i * 6 + 2] = verticesHolder[(i - longitude) * 6 + 2];
        	}
        	else
        	{
                // Don't worry about the normal vectors yet... it gets pretty complicated 
                // and it only affects lighting.
                verticesHolder[i * 6 + 0] *= 5f + rand.nextDouble();
                verticesHolder[i * 6 + 1] *= 5f + rand.nextDouble();
                verticesHolder[i * 6 + 2] *= 5f + rand.nextDouble();	
        	}
        }
        mesh1.setVertices(verticesHolder);

        // model1.meshes.set(0, mesh1);
        
        instance1 = new ModelInstance(model1, 0f, 0f, 0f);
        
        /*
        model2 = modelBuilder.createCone(4f, 3f, 4f, 10,
                new Material(ColorAttribute.createDiffuse(Color.RED)),
                Usage.Position | Usage.Normal);
        instance2 = new ModelInstance(model2, 0f, 5f, 0f);
        */
    }
    
    @Override
    public void render () {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        camController.update();

        modelBatch.begin(cam);
        //modelBatch.render(instance, environment);
        modelBatch.render(instance1, environment);
        //modelBatch.render(instance2, environment);
        modelBatch.end();
    }

    @Override
    public void dispose () {
    	modelBatch.dispose();
    	//model.dispose();
    	model1.dispose();
    	//model2.dispose();
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