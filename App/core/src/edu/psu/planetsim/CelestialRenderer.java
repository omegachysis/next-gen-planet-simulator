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
        _cam.update();

        _environment = new Environment();
        _environment.add(new DirectionalLight().set(1, 1, 1, 0, 0, 2));

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
            Vector3.Zero, Vector3.Zero, Vector3.Zero, "earth.jpg");
        add(earth);
        final CelestialBody luna = new CelestialBody(0.0123f, 0.2725f,
            new Vector3(63, 0, 0), new Vector3(0, 3, 0), Vector3.Zero, "luna.jpg");
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

        Vector3 com = _gravitySim.getCenterOfMass();
        _cam.position.set(com.cpy().add(0f, 0f, -80f));
        _cam.lookAt(com);
        _cam.update();
    }

    public void dispose() {
        _modelBatch.dispose();
        _world.dispose();
        for (final CelestialBody body : _bodies) {
            body.dispose();
        }
    }
}