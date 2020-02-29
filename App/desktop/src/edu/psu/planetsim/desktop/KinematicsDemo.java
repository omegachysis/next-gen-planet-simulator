package edu.psu.planetsim.desktop;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.*;
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.*;
import com.badlogic.gdx.physics.bullet.linearmath.*;

import edu.psu.planetsim.GravitySimulation;
import edu.psu.planetsim.KinematicObject;

public class KinematicsDemo extends ApplicationAdapter {
    private PerspectiveCamera _cam;
    private ModelBatch _modelBatch;
    private Model _model;
    private CameraInputController _camControl;
    private btDynamicsWorld _world;
    private GravitySimulation _gravitySim;

    private ArrayList<KinematicObject> _objects = new ArrayList<>();

    static class MyMotionState extends btMotionState {
        private Matrix4 _transform;
        private float _scale;

        public MyMotionState(Matrix4 transform, float scale) {
            _transform = transform;
            _scale = scale;
        }

        public void getWorldTransform(Matrix4 worldTrans) {
            worldTrans.set(_transform);
        }

        public void setWorldTransform(Matrix4 worldTrans) {
            _transform.set(worldTrans).scl(_scale);
        }
    }

    public void create() {
        Bullet.init();
        
        _modelBatch = new ModelBatch();

        _cam = new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        _cam.position.set(0, 0, 500);
        _cam.lookAt(Vector3.Zero);
        _cam.near = 1;
        _cam.far = 10000;

        _camControl = new CameraInputController(_cam);
        Gdx.input.setInputProcessor(_camControl);

        ModelBuilder builder = new ModelBuilder();
        _model = builder.createSphere(2, 2, 2, 20, 20, 
            new Material(ColorAttribute.createDiffuse(Color.WHITE)), 
            Usage.Position | Usage.Normal);

        btCollisionConfiguration config = new btDefaultCollisionConfiguration();
        _world = new btSimpleDynamicsWorld(new btCollisionDispatcher(config), 
            new btDbvtBroadphase(), new btSequentialImpulseConstraintSolver(),
            config);
        _world.setGravity(Vector3.Zero);

        _gravitySim = new GravitySimulation(1);



        spawnBody(20, 5, Vector3.Zero, Vector3.Zero);
        spawnBody(3, 2, new Vector3(-100, 0, 0), new Vector3(0, -50, 0));
        spawnBody(1, 1, new Vector3(200, 0, 0), new Vector3(0, 50, 0));
    }

    private void spawnBody(float mass, float radius, Vector3 position, Vector3 velocity) {
        ModelInstance inst = new ModelInstance(_model);
        inst.transform.set(position.cpy(), new Quaternion());
        btRigidBody body = new btRigidBody(mass, new MyMotionState(inst.transform, radius),
            new btSphereShape(radius));
        _world.addRigidBody(body);
        KinematicObject obj = new KinematicObject(inst, body, mass);
        _objects.add(obj);
        _gravitySim.addMass(obj);
        body.applyCentralImpulse(velocity.cpy().scl(mass));
    }

    public void render() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        _modelBatch.begin(_cam);
        for (KinematicObject obj : _objects) {
            _modelBatch.render(obj.model);
        }
        _modelBatch.end();

        
        _gravitySim.applyGravityForces(true);
        _world.stepSimulation(Gdx.graphics.getDeltaTime());

        _cam.update();
        _camControl.update();
    }

    public void dispose() {
        _modelBatch.dispose();
        _model.dispose();
        _world.dispose();
        for (KinematicObject obj : _objects) {
            obj.body.dispose();
        }
    }
}