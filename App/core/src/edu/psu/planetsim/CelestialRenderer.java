package edu.psu.planetsim;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.*;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.*;
import com.badlogic.gdx.physics.bullet.linearmath.*;

public class CelestialRenderer {
    private PerspectiveCamera _cam;
    private ModelBatch _modelBatch;
    private ModelBuilder _builder;
    private CameraInputController _camControl;
    private btDynamicsWorld _world;
    private GravitySimulation _gravitySim;
    private Environment _environment;
    private btCollisionDispatcher _dispatcher;
    private btDbvtBroadphase _pairCache;
    private btSequentialImpulseConstraintSolver _solver;

    private ArrayList<KinematicObject> _objects = new ArrayList<>();

    static class MyMotionState extends btMotionState {
        private final Matrix4 _transform;

        public MyMotionState(Matrix4 transform) {
            _transform = transform;
        }

        public void getWorldTransform(Matrix4 worldTrans) {
            worldTrans.set(_transform);
        }

        public void setWorldTransform(Matrix4 worldTrans) {
            _transform.set(worldTrans);
        }
    }

    public void create() {
        Bullet.init();
        
        _modelBatch = new ModelBatch();

        _cam = new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        _cam.near = 1;
        _cam.far = 10000;

        _environment = new Environment();
        _environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.3f, 0.3f, 0.3f, 1f));
        _environment.add(new DirectionalLight().set(1f, 1f, 1f, -1f, -0.8f, -0.2f));

        _camControl = new CameraInputController(_cam);
        Gdx.input.setInputProcessor(_camControl);

        _builder = new ModelBuilder();

        btCollisionConfiguration config = new btDefaultCollisionConfiguration();
        _dispatcher = new btCollisionDispatcher(config); 
        _pairCache = new btDbvtBroadphase();
        _solver = new btSequentialImpulseConstraintSolver(); 
        _world = new btSimpleDynamicsWorld(_dispatcher, _pairCache, _solver, config);
        _world.setGravity(Vector3.Zero);

        _gravitySim = new GravitySimulation();

        spawnBody(20, 10, Vector3.Zero, Vector3.Zero, new Vector3(0, 0, 10));
        spawnBody(3, 2, new Vector3(-100, 0, 0), new Vector3(0, -50, 0), new Vector3(0, 0, 2));
        spawnBody(2, 2, new Vector3(200, 0, 0), new Vector3(0, 50, 0), new Vector3(0, 0, 1));
        spawnBody(5, 4, new Vector3(350, 0, 0), new Vector3(0, 20, 0), new Vector3(0, 0, 1));
        spawnBody(3, 2, new Vector3(400, 0, 0), new Vector3(0, 10, 0), new Vector3(0, 0, 1));
        spawnBody(1, 1, new Vector3(300, 300, 0), new Vector3(-60, -20, 0), new Vector3(0, 0, 3));
    }

    private void spawnBody(float mass, float radius, Vector3 position, Vector3 velocity,
        Vector3 spin) {

        Model model = _builder.createBox(
            radius * 2, radius * 2, radius * 2,
            new Material(ColorAttribute.createDiffuse(Color.WHITE)), 
            Usage.Position | Usage.Normal);
        // Model model = _builder.createSphere(radius * 2, radius * 2, radius * 2, 5, 5,
        //     new Material(ColorAttribute.createDiffuse(Color.WHITE)), 
        //     Usage.Position | Usage.Normal);

        ModelInstance inst = new ModelInstance(model);
        inst.transform.set(position.cpy(), new Quaternion());
        btCollisionShape shape = new btBoxShape(new Vector3(radius, radius, radius));
        Vector3 inertia = new Vector3();
        shape.calculateLocalInertia(mass, inertia);
        btRigidBody body = new btRigidBody(mass, new MyMotionState(inst.transform),
            shape, inertia);
        _world.addRigidBody(body);
        KinematicObject obj = new KinematicObject(inst, body, mass);
        _objects.add(obj);
        _gravitySim.addMass(obj);
        body.applyCentralImpulse(velocity.cpy().scl(mass));
        body.applyTorqueImpulse(spin.scl(mass));
    }

    public void render() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        _modelBatch.begin(_cam);
        for (KinematicObject obj : _objects) {
            _modelBatch.render(obj.getModel(), _environment);
        }
        _modelBatch.end();

        _gravitySim.applyGravityForces(true);
        _world.stepSimulation(Gdx.graphics.getDeltaTime());

        _cam.position.set(_objects.get(0).getPosition().x, 
            _objects.get(0).getPosition().y + 300, 
            _objects.get(0).getPosition().z - 200);
        _cam.lookAt(_objects.get(0).getPosition());
        _cam.update();
        _camControl.update();
    }

    public void dispose() {
        _modelBatch.dispose();
        _world.dispose();
        for (KinematicObject obj : _objects) {
            obj.getBody().dispose();
        }
    }
}