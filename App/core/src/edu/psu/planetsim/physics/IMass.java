package edu.psu.planetsim.physics;

import com.badlogic.gdx.math.*;

public interface IMass 
{
    public float getMass();
    public Vector3 getPosition();
    public void applyForce(Vector3 force);
}
