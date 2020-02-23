package edu.psu.planetsim.desktop;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.VertexAttributes.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.*;
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

import edu.psu.planetsim.GravitySimulation;
import edu.psu.planetsim.IMass;

class TestMass3D implements IMass {
    private final float _mass;
    private Vector3 _position;
    private Vector3 _velocity;
    private ModelInstance _instance;
    private Vector3 _netForces;
    private final Quaternion _zeroQuat = new Quaternion();

    public TestMass3D(ModelInstance renderable, float mass, Vector3 pos, Vector3 vel) {
        _instance = renderable;
        _mass = mass;
        _position = pos.cpy();
        _velocity = vel.cpy();
        _netForces = Vector3.Zero.cpy();
    }

    public float getMass() {
        return _mass;
    }

    public Vector3 getPosition() {
        return _position.cpy();
    }

    public void applyForce(Vector3 force) {
        _netForces.add(force);
    }

    public void update(float dt) {
        _position.add(_velocity.cpy().scl(dt));

        // Apply Newton's 2nd law (dv/dt = F/m)
        _velocity.add(_netForces.cpy().scl(1f / _mass));

        _instance.transform.set(_position, _zeroQuat);

        // Reset net forces.
        _netForces.scl(0);
    }
}

public class GravityDemo extends ApplicationAdapter {
    private PerspectiveCamera _cam;
    private ModelBatch _modelBatch;
    private Model _model;
    private ArrayList<ModelInstance> _instances = new ArrayList<>();
    private ArrayList<TestMass3D> _masses = new ArrayList<>();
    private GravitySimulation _sim;
    private CameraInputController _camControl;

    public void create() {
        _modelBatch = new ModelBatch();

        _cam = new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        _cam.position.set(40f, 40f, 40f);
        _cam.lookAt(0,0,0);
        _cam.near = 1f;
        _cam.far = 300f;
        _cam.update();
        _cam.update();

        _camControl = new CameraInputController(_cam);
        Gdx.input.setInputProcessor(_camControl);

        ModelBuilder builder = new ModelBuilder();
        _model = builder.createSphere(1, 1, 1, 10, 10, 
            new Material(ColorAttribute.createDiffuse(Color.WHITE)), 
            Usage.Position | Usage.Normal);

        _sim = new GravitySimulation(0.00005f);

        Random rand = new Random();
        for (int i = 0; i < 40; i++) {
            ModelInstance inst = new ModelInstance(_model);
            _instances.add(inst);
            
            float m, x, y, z, dx, dy, dz;
            if (i == 0) {
                m = 1000f;
                x = 0f;
                y = 0f;
                z = 0f;
                dx = 0f;
                dy = 0f;
                dz = 0f;
                inst.materials.get(0).set(ColorAttribute.createDiffuse(Color.ORANGE));
            }
            else {
                m = rand.nextFloat() * 10f;
                x = rand.nextFloat() * 80f - 40f;
                y = rand.nextFloat() * 80f - 40f;
                z = rand.nextFloat() * 80f - 40f;
                dx = rand.nextFloat() * 10f - 5f;
                dy = rand.nextFloat() * 10f - 5f;
                dz = rand.nextFloat() * 10f - 5f;
            }

            TestMass3D mass = new TestMass3D(
                inst, m, new Vector3(x, y, z), new Vector3(dx, dy, dz)
            );
            _masses.add(mass);
            _sim.addMass(mass);
        }
    }

    public void render() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        _modelBatch.begin(_cam);
        for (ModelInstance inst : _instances) {
            _modelBatch.render(inst);
        }
        _modelBatch.end();

        _camControl.update();

        _sim.applyGravityForces();
        for (TestMass3D mass : _masses) {
            mass.update(Gdx.graphics.getDeltaTime());
        }
    }

    public void dispose() {
        _modelBatch.dispose();
        _model.dispose();
    }
}