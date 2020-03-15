package edu.psu.planetsim;

import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
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

public class CelestialBody extends KinematicObject {
    private final Model _modelBase;

    public CelestialBody(final float mass, final float radius) {
        super(null, null, mass);

        // Build the model.
        _modelBase = new ModelBuilder().createSphere(
            radius * 2, radius * 2, radius * 2,
            100, 100,
            new Material(ColorAttribute.createDiffuse(Color.WHITE)),
            Usage.Position | Usage.Normal
        );

        _model = new ModelInstance(_modelBase);

        // Build collision and kinematics data.
        final btCollisionShape shape = new btBoxShape(new Vector3(radius, radius, radius));
        final Vector3 inertia = new Vector3();
        shape.calculateLocalInertia(mass, inertia);
        _body = new btRigidBody(mass, new MotionState(_model.transform), shape, inertia);
    }

    void attachTo(btDynamicsWorld world) {
        world.addRigidBody(_body);
    }

    void removeFrom(btDynamicsWorld world) {
        world.removeRigidBody(_body);
    }

    void render(ModelBatch batch, Environment env) {
        batch.render(_model);
    }

    void dispose() {
        _modelBase.dispose();
        _body.dispose();
    }

    static class MotionState extends btMotionState {
        private final Matrix4 _transform;

        public MotionState(final Matrix4 transform) {
            _transform = transform;
        }

        public void getWorldTransform(final Matrix4 worldTrans) {
            worldTrans.set(_transform);
        }

        public void setWorldTransform(final Matrix4 worldTrans) {
            _transform.set(worldTrans);
        }
    }
}
