package edu.psu.planetsim;

import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class KinematicObject implements IMass {
    protected ModelInstance _model;
    protected btRigidBody _body;
    protected float _mass;

    public KinematicObject(ModelInstance model, btRigidBody body, float mass) {
        _model = model;
        _body = body;
        _mass = mass;
    }

    public btRigidBody getBody() {
        return _body;
    }

    public void setBody(btRigidBody _body) {
        this._body = _body;
    }

    public ModelInstance getModel() {
        return _model;
    }

    public void setModel(ModelInstance _model) {
        this._model = _model;
    }

    public float getMass() {
        return _mass;
    }

    public Vector3 getPosition() {
        Vector3 res = new Vector3();
        _model.transform.getTranslation(res);
        return res;
    }

    public void applyForce(Vector3 force) {
        _body.applyCentralForce(force);
    }
}
