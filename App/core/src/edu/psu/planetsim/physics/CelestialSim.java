package edu.psu.planetsim.physics;

import java.util.ArrayList;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.*;

import edu.psu.planetsim.AppState;
import edu.psu.planetsim.Metrics;

public class CelestialSim 
{
    private final InputMultiplexer _multiplexer;
    private final CameraInputController _camControl;
    private final PerspectiveCamera _fakeCam;
    private final PerspectiveCamera _cam;
    private final ModelBatch _modelBatch;
    private final btDynamicsWorld _world;
    private final GravitySimulation _gravitySim;
    private final Environment _environment;
    private final DirectionalLight _solarLight;
    private final btCollisionDispatcher _dispatcher;
    private final btDefaultCollisionConfiguration _config;
    private final btDbvtBroadphase _pairCache;
    private final btSequentialImpulseConstraintSolver _solver;
    private final ArrayList<CelestialBody> _bodies = new ArrayList<>();
    private final AppState _appState;
    
    public CelestialSim(final AppState appState, final InputMultiplexer multiplexer) 
    {
        Bullet.init();

        _appState = appState;
        _modelBatch = new ModelBatch();

        _cam = new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        _cam.position.set(new Vector3(0f, Metrics.m(-6e8), Metrics.m(-4e8)));
        _cam.lookAt(Vector3.Zero);
        _cam.near = Metrics.m(1e5);
        _cam.far = Metrics.m(1e9);

        _fakeCam = new PerspectiveCamera(60, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        _fakeCam.position.set(new Vector3(0f, Metrics.m(-6e8), Metrics.m(-4e8)));
        _fakeCam.lookAt(Vector3.Zero);
        _camControl = new CameraInputController(_fakeCam);
        _camControl.scrollTarget = false;
        _camControl.alwaysScroll = false;
        _camControl.scrollFactor = 0f;
        multiplexer.addProcessor(_camControl);
        _multiplexer = multiplexer;

        _environment = new Environment();
        _solarLight = new DirectionalLight();
        _solarLight.color.set(Color.WHITE);
        _environment.add(_solarLight);

        _config = new btDefaultCollisionConfiguration();
        _dispatcher = new btCollisionDispatcher(_config);
        _pairCache = new btDbvtBroadphase();
        _solver = new btSequentialImpulseConstraintSolver();
        _world = new btSimpleDynamicsWorld(_dispatcher, _pairCache, _solver, _config);
        _world.setGravity(Vector3.Zero);

        _gravitySim = new GravitySimulation();
    }

    private void updateCurrentCelestialBody(UUID value)
    {
        clear();
        if (_appState.bodies.containsKey(value))
        {
            var dto = _appState.bodies.get(value);
            add(new CelestialBody(dto));
            System.out.println("Adding celestial body to sim");
            for (var satelliteId : dto.satellites)
            {
                add(new CelestialBody(_appState.bodies.get(satelliteId)));
                System.out.println("Adding satellite to sim");
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
        for (final var body : _bodies) 
        {
            body.removeFrom(_world);
            body.removeFrom(_gravitySim);
            body.dispose();
        }

        _bodies.clear();
    }

    public void render() 
    {
        if (_appState.needsRefresh)
        {
            updateCurrentCelestialBody(_appState.currentCelestialBodyId);
            _appState.needsRefresh = false;
        }

        _modelBatch.begin(_cam);
        for (final var body : _bodies) 
        {
            body.render(_modelBatch, _environment);
        }
        _modelBatch.end();

        if (!_appState.paused)
        {
            _gravitySim.applyGravityForces();
            _world.stepSimulation(Gdx.graphics.getDeltaTime() * _appState.speed);
        }

        if (_bodies.size() > 0)
        {
            // Direct sunlight based on position in the solar system.
            var main = _appState.getCurrentCelestialBody();
            _solarLight.direction.set(main.positionRelativeToSun.cpy());

            Vector3 target = new Vector3(0, 0, 0);
            if (_appState.viewingMode == AppState.ViewingMode.CenterOfMass)
                target = _gravitySim.getCenterOfMass();
            else if (_appState.viewingMode == AppState.ViewingMode.MainCelestialBody)
                target = _bodies.get(0).getPosition();
            else if (_appState.viewingMode == AppState.ViewingMode.NaturalSatellite)
                target = _bodies.get(_appState.satelliteFocusedIndex + 1).getPosition();
    
            _camControl.update();
            
            final var interp = Interpolation.pow4Out;
            final var camDist = interp.apply(Metrics.m(1e9), Metrics.m(1e3), _appState.zoom);
            _cam.position.set(_fakeCam.position.cpy().nor().scl(camDist).add(target));
            _cam.direction.set(_fakeCam.direction);
            _cam.up.set(_fakeCam.up);
            _cam.update();
        }
    }

    public void dispose() 
    {
        _modelBatch.dispose();
        _world.dispose();
        clear();
        _multiplexer.removeProcessor(_camControl);
    }
}