package edu.psu.planetsim;

import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class KinematicObject implements IMass {
    public ModelInstance model;
    public btRigidBody body;
    private float _mass;

    public KinematicObject(ModelInstance model, btRigidBody body, float mass) {
        this.model = model;
        this.body = body;
        _mass = mass;
    }

    public float getMass() {
        return _mass;
    }

    public Vector3 getPosition() {
        Vector3 res = new Vector3();
        model.transform.getTranslation(res);
        return res;
    }

    public void applyForce(Vector3 force) {
        body.applyCentralForce(force);
    }
}
