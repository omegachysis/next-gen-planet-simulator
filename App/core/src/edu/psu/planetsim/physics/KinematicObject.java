package edu.psu.planetsim.physics;

import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class KinematicObject implements IMass 
{
    public Matrix4 transform;
    public btRigidBody body;
    public float mass;

    public float getMass() 
    {
        return mass;
    }

    public Vector3 getPosition() 
    {
        final var res = new Vector3();
        transform.getTranslation(res);
        return res;
    }

    public void applyForce(final Vector3 force) 
    {
        body.applyCentralForce(force);
    }
}
