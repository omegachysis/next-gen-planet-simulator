package edu.psu.planetsim;

import java.util.ArrayList;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.*;

public class CelestialRenderer 
{
    private final PerspectiveCamera _cam;
    private final ModelBatch _modelBatch;
    private final btDynamicsWorld _world;
    private final GravitySimulation _gravitySim;
    private final Environment _environment;
    private final btCollisionDispatcher _dispatcher;
    private final btDbvtBroadphase _pairCache;
    private final btSequentialImpulseConstraintSolver _solver;
    private final ArrayList<CelestialBody> _bodies = new ArrayList<>();
    private final AppState _appState;
    private UUID _currentCelestialBody;
    
    /** Which body to actually zoom to in the simulation.
     * Specify -1 to zoom to the center of mass.
     * Specify 0 to zoom to the main celestial body.
     * Specify a number >= 1 to zoom to that indexed natural satellite.
     */
    public int zoomTo = -1;

    public CelestialRenderer(final AppState appState) 
    {
        Bullet.init();

        _appState = appState;
        _modelBatch = new ModelBatch();

        _cam = new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        _cam.near = Metrics.m(1e3);
        _cam.far = Metrics.m(1000e9);
        _cam.update();

        _environment = new Environment();
        _environment.add(new DirectionalLight().set(1, 1, 1, 1, 2, 0));

        final btCollisionConfiguration config = new btDefaultCollisionConfiguration();
        _dispatcher = new btCollisionDispatcher(config);
        _pairCache = new btDbvtBroadphase();
        _solver = new btSequentialImpulseConstraintSolver();
        _world = new btSimpleDynamicsWorld(_dispatcher, _pairCache, _solver, config);
        _world.setGravity(Vector3.Zero);

        _gravitySim = new GravitySimulation();
    }

    public UUID getCurrentCelestialBody()
    {
        return _currentCelestialBody;
    }
    
    public void setCurrentCelestialBody(UUID value)
    {
        zoomTo = 0;
        clear();

        _currentCelestialBody = value;
        if (_appState.bodies.containsKey(value))
        {
            AppState.CelestialBody dto = _appState.bodies.get(value);
            add(new CelestialBody(dto));
            for (UUID satelliteId : dto.satellites)
            {
                add(new CelestialBody(_appState.bodies.get(satelliteId)));
            }
        }
    }

    public void add(final CelestialBody body) 
    {
        _bodies.add(body);
        body.attachTo(_world);
        body.attachTo(_gravitySim);
    }

    public void remove(final CelestialBody body)
    {
        _bodies.remove(body);
        body.removeFrom(_world);
        body.removeFrom(_gravitySim);
    }

    public void clear()
    {
        for (final CelestialBody body : _bodies) 
        {
            body.removeFrom(_world);
            body.removeFrom(_gravitySim);
            body.dispose();
        }

        _bodies.clear();
    }

    public void render() 
    {
        _modelBatch.begin(_cam);
        for (final CelestialBody body : _bodies) 
        {
            body.render(_modelBatch, _environment);
        }
        _modelBatch.end();

        _gravitySim.applyGravityForces();
        _world.stepSimulation(Gdx.graphics.getDeltaTime() * _appState.speed);

        final Vector3 target;
        if (zoomTo == -1) 
        {
            target = _gravitySim.getCenterOfMass();
        }
        else
        {
            if (zoomTo < _bodies.size())
                target = _bodies.get(zoomTo).getPosition();
            else
                target = Vector3.Zero;
        }

        _cam.position.set(target.cpy().add(0f, Metrics.m(-6e8), Metrics.m(-4e8)));
        _cam.lookAt(target);

        final Interpolation interp = Interpolation.pow4Out;
        _cam.fieldOfView = interp.apply(180f, 0.1f, _appState.zoom);
        _cam.update();
    }

    public void dispose() 
    {
        _modelBatch.dispose();
        _world.dispose();
        clear();
    }
}