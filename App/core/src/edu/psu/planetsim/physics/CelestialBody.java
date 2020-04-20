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
import edu.psu.planetsim.graphics.TerrainBuilder;

public class CelestialBody extends KinematicObject 
{
    private final ThermalObject _temperature;
    private final ModelInstance _terrainModel;
    private final ModelInstance _oceanModel;
    private final float _radius;

    public CelestialBody(final AppState.CelestialBody dto)
    {
        final var radius = Metrics.m(1.0e6);

        // Build the model.
        _terrainModel = new ModelInstance(
            TerrainBuilder.BuildTerrainModel(dto.elevationMap));

        // Attach the inner model.
        transform = _terrainModel.transform;

        // Build the ocean layer.
        if (dto.seaLevel > 0f)
        {
            _oceanModel = new ModelInstance(
                TerrainBuilder.BuildOceanModel(
                    dto.elevationMap,dto.seaLevel, dto.oceanColor, _terrainModel.model));

            // Parent the ocean to the terrain model:
            _oceanModel.transform = transform;
        }
        else
        _oceanModel = null;

        // Initialize physics.
        _radius = radius;
        mass = (float)dto.mass;
        resetUnderlyingPhysics(dto.position, dto.velocity, dto.spin, dto.orientation);

        // Initialize the thermal object.
        _temperature = new ThermalObject(90, dto.elevationMap);
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

    public void render(final ModelBatch batch, final Environment env) 
    {
        // This renders the terrain:
        batch.render(_terrainModel, env);

        // Render all other parts:
        if (_oceanModel != null)
            batch.render(_oceanModel, env);
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
