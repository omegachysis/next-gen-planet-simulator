package edu.psu.planetsim.desktop;

import com.badlogic.gdx.math.Vector3;
import edu.psu.planetsim.IMass;

class TestMass implements IMass {
    private float _mass;
    private Vector3 _pos;
    private Vector3 _netForces;

    public TestMass(float mass, float x, float y, float z) {
        _mass = mass;
        _pos = new Vector3(x, y, z);
        _netForces = new Vector3();
    }

    public float getMass() { return _mass; }
    public Vector3 getPosition() { return _pos; }

    public void applyForce(Vector3 force) {
        _netForces = _netForces.add(force);
    }

    public Vector3 getNetForces() { return _netForces; }
}

public class GravitySimulationTests {
    public static void run() {
        Assert.True(true, "basic test");
    }
}
