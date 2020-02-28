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

import edu.psu.planetsim.KinematicObject;

public class KinematicsDemo extends ApplicationAdapter {
    private PerspectiveCamera _cam;
    private ModelBatch _modelBatch;
    private Model _model;
    private CameraInputController _camControl;
    private btDynamicsWorld _world;

    private ArrayList<KinematicObject> _objects = new ArrayList<>();

    static class MyMotionState extends btMotionState {
        private Matrix4 _transform;

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
        _cam.position.set(10f, 10f, 10f);
        _cam.lookAt(0,0,0);
        _cam.near = 0.1f;
        _cam.far = 30f;
        _cam.update();
        _cam.update();

        _camControl = new CameraInputController(_cam);
        Gdx.input.setInputProcessor(_camControl);

        ModelBuilder builder = new ModelBuilder();
        _model = builder.createSphere(1, 1, 1, 10, 10, 
            new Material(ColorAttribute.createDiffuse(Color.WHITE)), 
            Usage.Position | Usage.Normal);

        btCollisionConfiguration config = new btDefaultCollisionConfiguration();
        _world = new btSimpleDynamicsWorld(new btCollisionDispatcher(config), 
            new btDbvtBroadphase(), new btSequentialImpulseConstraintSolver(),
            config);
        _world.setGravity(Vector3.Zero);

        Random rand = new Random();

        for (int i = 0; i < 1000; i++) {
            ModelInstance inst = new ModelInstance(_model);

            float x = rand.nextFloat() * 100f - 50f;
            float y = rand.nextFloat() * 100f - 50f;
            float z = rand.nextFloat() * 100f - 50f;

            inst.transform.set(new Vector3(x, y, z), new Quaternion());
            btRigidBody body = new btRigidBody(1.0f, new MyMotionState(inst.transform),
            new btSphereShape(0.5f));
            _world.addRigidBody(body);
            _objects.add(new KinematicObject(inst, body));
        }
    }

    public void render() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        _modelBatch.begin(_cam);
        for (KinematicObject obj : _objects) {
            _modelBatch.render(obj.model);
        }
        _modelBatch.end();

        _camControl.update();

        for (KinematicObject obj : _objects) {
            Vector3 pos = new Vector3();
            obj.model.transform.getTranslation(pos);
            obj.body.applyCentralForce(
                pos.scl(-1)
            );
        }
        _world.stepSimulation(Gdx.graphics.getDeltaTime());
    }

    public void dispose() {
        _modelBatch.dispose();
        _model.dispose();
    }
}