package edu.psu.planetsim;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.2f, 0.2f, 0.2f, 1f));
        environment.add(new DirectionalLight().set(0.7f, 0.3f, 0.9f, -1f, -0.8f, -0.2f));
 
        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(5f, 5f, 5f, 
            new Material(ColorAttribute.createDiffuse(Color.WHITE)),
            Usage.Position | Usage.Normal);
        instance = new ModelInstance(model);
        
        model1 = modelBuilder.createSphere(6f, 6f, 6f, 10, 10,
                new Material(ColorAttribute.createDiffuse(Color.BLUE)),
                Usage.Position | Usage.Normal);
        instance1 = new ModelInstance(model1, 6f, 6f, 0f);
        
        model2 = modelBuilder.createCone(4f, 3f, 4f, 10,
                new Material(ColorAttribute.createDiffuse(Color.RED)),
                Usage.Position | Usage.Normal);
        instance2 = new ModelInstance(model2, 0f, 5f, 0f);
    }
    
    @Override
    public void render () {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        camController.update();

        modelBatch.begin(cam);
        modelBatch.render(instance, environment);
        modelBatch.render(instance1, environment);
        modelBatch.render(instance2, environment);
        modelBatch.end();
    }

    @Override
    public void dispose () {
    	modelBatch.dispose();
    	model.dispose();
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