package edu.psu.planetsim;

import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.bullet.linearmath.*;

public class CelestialBody extends KinematicObject 
{
    private final Model _modelBase;
    private final float _radius;

    public CelestialBody(final float mass, final float radius,
    final Vector3 initPosition, final Vector3 initVelocity, final Vector3 initSpin,
    final String textureFile) 
    {
        // Load a texture.
        final FileHandle imageFileHandle = Gdx.files.getFileHandle(
            textureFile, Files.FileType.Internal);
        final Texture texture = new Texture(imageFileHandle, true);
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        // Build the model.
        final TextureAttribute texAttr = new TextureAttribute(TextureAttribute.Diffuse, texture);
        _modelBase = new ModelBuilder().createSphere(radius * 2, radius * 2, radius * 2, 50, 50,
                new Material(texAttr),
                Usage.Position | Usage.Normal | Usage.TextureCoordinates);

        model = new ModelInstance(_modelBase);

        // Initialize physics.
        _radius = radius;
        resetUnderlyingPhysics(initPosition, initVelocity, initSpin);
    }

    public void resetUnderlyingPhysics(final Vector3 position, 
    final Vector3 velocity, final Vector3 spin) 
    {
        model.transform.set(position, 
            new Quaternion().setFromCross(Vector3.Y, spin));

        final btCollisionShape shape = new btSphereShape(_radius);
        final Vector3 inertia = new Vector3();
        shape.calculateLocalInertia(mass, inertia);
        body = new btRigidBody(mass, new MotionState(model.transform), shape, inertia);
        body.applyCentralImpulse(velocity.cpy().scl(mass));

        final float moment = 2 * mass * _radius * _radius / 5;
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
