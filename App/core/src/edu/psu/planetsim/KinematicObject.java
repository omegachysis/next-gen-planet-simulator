package edu.psu.planetsim;

import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class KinematicObject implements IMass 
{
    public ModelInstance model;
    public btRigidBody body;
    public float mass;

    public float getMass() 
    {
        return mass;
    }

    public Vector3 getPosition() 
    {
        final Vector3 res = new Vector3();
        model.transform.getTranslation(res);
        return res;
    }

    public void applyForce(final Vector3 force) 
    {
        body.applyCentralForce(force);
    }
}
