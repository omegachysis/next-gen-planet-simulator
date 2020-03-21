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
        _cam.near = Metrics.m(1e3);
        _cam.far = Metrics.m(1000e9);
        _cam.update();

        _environment = new Environment();
        _environment.add(new DirectionalLight().set(1, 1, 1, -1, 2, 0));

        final btCollisionConfiguration config = new btDefaultCollisionConfiguration();
        _dispatcher = new btCollisionDispatcher(config);
        _pairCache = new btDbvtBroadphase();
        _solver = new btSequentialImpulseConstraintSolver();
        _world = new btSimpleDynamicsWorld(_dispatcher, _pairCache, _solver, config);
        _world.setGravity(Vector3.Zero);

        _gravitySim = new GravitySimulation();

        final CelestialBody earth = new CelestialBody(
            Metrics.kg(5.97e24), // mass
            Metrics.m(6378.1e3), // radius
            Vector3.Zero, Vector3.Zero, Vector3.Zero, "earth.jpg");
        add(earth);
        final CelestialBody luna = new CelestialBody(
            Metrics.kg(7.348e22), // mass
            Metrics.m(1737.1e3), // radius
            new Vector3(Metrics.m(357e6), 0, 0), // position
            new Vector3(0, Metrics.m(1100), 0), // velocity
            Vector3.Zero, "luna.jpg");
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
        _world.stepSimulation(Gdx.graphics.getDeltaTime() * 50000);

        Vector3 com = _gravitySim.getCenterOfMass();
        _cam.position.set(com.cpy().add(0f, Metrics.m(-6e8), Metrics.m(-4e8)));
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