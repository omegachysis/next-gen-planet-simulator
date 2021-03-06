package edu.psu.planetsim.physics;

import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.bullet.linearmath.*;

import edu.psu.planetsim.AppState;
import edu.psu.planetsim.Metrics;
import edu.psu.planetsim.AppState.InspectionMode;
import edu.psu.planetsim.graphics.TerrainBuilder;

public class CelestialBody extends KinematicObject 
{
    public final AppState.CelestialBody dto;
    public final ThermalObject _temperature;
    private final ModelInstance _terrainModel;
    private final ModelInstance _thermometers;
    private final float _radius;
    private final Vector3 _linkedPosition;

    public CelestialBody(final AppState.CelestialBody dto)
    {
        this.dto = dto;
        final var radius = Metrics.m(1.0e6);

        // Initialize the thermal object.
        _temperature = new ThermalObject(50, dto);

        // Build the model.
        _terrainModel = new ModelInstance(
            TerrainBuilder.BuildTerrainModel(100, dto));
        _thermometers = new ModelInstance(
            TerrainBuilder.BuildThermometerField(50, dto,
            _temperature.temperature.getColorBufferTexture()));

        // Attach the inner model.
        transform = _terrainModel.transform;

        // Initialize physics.
        _radius = radius;
        mass = (float)dto.mass;
        _linkedPosition = dto.position;
        resetUnderlyingPhysics(dto.position, dto.velocity, dto.spin, dto.orientation);
    }

    public void resetUnderlyingPhysics(final Vector3 position, 
        final Vector3 velocity, final Vector3 spin, final Quaternion orientation) 
    {
        transform.set(position, orientation);

        final var shape = new btSphereShape(_radius);
        final var inertia = new Vector3();
        shape.calculateLocalInertia(mass, inertia);
        body = new btRigidBody(mass, new MotionState(transform), shape, inertia);
        body.applyCentralImpulse(velocity.cpy().scl(mass));

        final var moment = 2 * mass * _radius * _radius / 5;
        body.applyTorqueImpulse(spin.cpy().scl(moment));
    }

    public void attachTo(final btDynamicsWorld world) 
    {
        world.addRigidBody(body);
    }

    public void attachTo(final GravitySimulation sim) 
    {
        sim.addMass(this);
    }

    public void removeFrom(final btDynamicsWorld world) 
    {
        world.removeRigidBody(body);
    }

    public void removeFrom(final GravitySimulation sim) 
    {
        sim.removeMass(this);
    }

    public void render(final ModelBatch batch, final Environment env,
    InspectionMode mode) 
    {
        _thermometers.transform = _terrainModel.transform;
        if (mode == InspectionMode.None)
            batch.render(_terrainModel, env);
        else if (mode == InspectionMode.Thermodynamics)
            batch.render(_thermometers, env);
    }

    public void updateAppStateData()
    {
        transform.getTranslation(_linkedPosition);
    }

    public void dispose() 
    {
        body.dispose();
    }

    static class MotionState extends btMotionState 
    {
        private final Matrix4 _transform;

        public MotionState(final Matrix4 transform) 
        {
            _transform = transform;
        }

        public void getWorldTransform(final Matrix4 worldTrans) 
        {
            worldTrans.set(_transform);
        }

        public void setWorldTransform(final Matrix4 worldTrans) 
        {
            _transform.set(worldTrans);
        }
    }
}
