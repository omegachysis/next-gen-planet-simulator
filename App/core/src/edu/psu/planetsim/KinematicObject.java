package edu.psu.planetsim;

import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class KinematicObject {
    public ModelInstance model;
    public btRigidBody body;

    public KinematicObject(ModelInstance model, btRigidBody body) {
        this.model = model;
        this.body = body;
    }
}
