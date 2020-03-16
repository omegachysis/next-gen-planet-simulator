package edu.psu.planetsim;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.*;
import com.badlogic.gdx.physics.bullet.linearmath.*;

public class CelestialRenderer {
    private final PerspectiveCamera _cam;
    private final ModelBatch _modelBatch;
    private final btDynamicsWorld _world;
    private final GravitySimulation _gravitySim;
    private final Environment _environment;
    private final btCollisionDispatcher _dispatcher;
    private final btDbvtBroadphase _pairCache;
    private final btSequentialImpulseConstraintSolver _solver;

    private final ArrayList<CelestialBody> _bodies = new ArrayList<>();

    static class MyMotionState extends btMotionState {
        private final Matrix4 _transform;

        public MyMotionState(final Matrix4 transform) {
            _transform = transform;
        }

        public void getWorldTransform(final Matrix4 worldTrans) {
            worldTrans.set(_transform);
        }

        public void setWorldTransform(final Matrix4 worldTrans) {
            _transform.set(worldTrans);
        }
    }

    public CelestialRenderer() {
        Bullet.init();

        _modelBatch = new ModelBatch();

        _cam = new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        _cam.near = 1;
        _cam.far = 1000;
        _cam.position.set(0, -15, 10);
        _cam.lookAt(Vector3.Zero);
        _cam.update();

        _environment = new Environment();
        _environment.add(new DirectionalLight().set(1, 1, 1, -7, 7, -2));

        final btCollisionConfiguration config = new btDefaultCollisionConfiguration();
        _dispatcher = new btCollisionDispatcher(config);
        _pairCache = new btDbvtBroadphase();
        _solver = new btSequentialImpulseConstraintSolver();
        _world = new btSimpleDynamicsWorld(_dispatcher, _pairCache, _solver, config);
        _world.setGravity(Vector3.Zero);

        _gravitySim = new GravitySimulation();

        // Mass units: earth masses
        // Radius units: earth radii
        final CelestialBody earth = new CelestialBody(1.0f, 1.0f,
            Vector3.Zero, Vector3.Zero, new Vector3(.04f, 0, .1f), "earth.jpg");
        add(earth);
        final CelestialBody luna = new CelestialBody(0.0123f, 0.2725f,
            new Vector3(10, 0, 0), new Vector3(0, 3, 0), new Vector3(0, 0, .02f), "luna.jpg");
        add(luna);
    }

    public void add(final CelestialBody body) {
        _bodies.add(body);
        body.attachTo(_world);
        body.attachTo(_gravitySim);
    }

    public void render() {
        _modelBatch.begin(_cam);
        for (final CelestialBody body : _bodies) {
            body.render(_modelBatch, _environment);
        }
        _modelBatch.end();

        _gravitySim.applyGravityForces();
        _world.stepSimulation(Gdx.graphics.getDeltaTime());

        // _cam.position.set(_objects.get(0).getPosition().x,
        // _objects.get(0).getPosition().y + 300,
        // _objects.get(0).getPosition().z - 200);
        // _cam.lookAt(_objects.get(0).getPosition());
        // _cam.update();
        // _camControl.update();
    }

    public void dispose() {
        _modelBatch.dispose();
        _world.dispose();
        for (final CelestialBody body : _bodies) {
            body.dispose();
        }
    }
}