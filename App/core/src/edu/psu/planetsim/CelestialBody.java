package edu.psu.planetsim;

import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.bullet.linearmath.*;

public class CelestialBody extends KinematicObject 
{
    private final Model _modelBase;
    private final float _radius;

    public CelestialBody(final AppState.CelestialBody dto)
    {
        final var radius = Metrics.m(1.0e6);

        // Build the model.
        _modelBase = new ModelBuilder().createSphere(
            radius * 2, radius * 2, radius * 2, 100, 100,
            new Material(ColorAttribute.createDiffuse(new Color(.9f, .35f, .35f, 1f))),
            Usage.Position | Usage.Normal | Usage.TextureCoordinates);
        model = new ModelInstance(_modelBase);

        // Initialize physics.
        _radius = radius;
        mass = (float)dto.mass;
        resetUnderlyingPhysics(dto.position, dto.velocity, dto.spin, dto.orientation);
    }

    public void resetUnderlyingPhysics(final Vector3 position, 
    final Vector3 velocity, final Vector3 spin, final Quaternion orientation) 
    {
        model.transform.set(position, orientation);

        final var shape = new btSphereShape(_radius);
        final var inertia = new Vector3();
        shape.calculateLocalInertia(mass, inertia);
        body = new btRigidBody(mass, new MotionState(model.transform), shape, inertia);
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
        batch.render(model, env);
    }

    public void dispose() 
    {
        _modelBase.dispose();
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
