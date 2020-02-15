package edu.psu.planetsim.desktop;

import com.badlogic.gdx.math.Vector3;
import edu.psu.planetsim.GravitySimulation;
import edu.psu.planetsim.IMass;

class TestMass implements IMass {
    private float _mass;
    private Vector3 _pos;
    private Vector3 _netForces;

    public TestMass(float mass, float x, float y, float z) {
        _mass = mass;
        _pos = new Vector3(x, y, z);
        _netForces = Vector3.Zero.cpy();
    }

    public float getMass() { return _mass; }
    public Vector3 getPosition() { return _pos.cpy(); }

    public void applyForce(Vector3 force) {
        _netForces.add(force);
    }

    public Vector3 getNetForces() { return _netForces.cpy(); }
    public void reset() {
        _netForces.setZero();
    }
}

public class GravitySimulationTests {
    public static void run() {

        // Test that there is no gravity exerted on a single mass.
        TestMass m1 = new TestMass(1, 0, 0, 0);
        GravitySimulation sim = new GravitySimulation();
        sim.addMass(m1);

        Assert.True(m1.getNetForces().epsilonEquals(Vector3.Zero),
                "Mass 1 starts with zero net forces");
        sim.applyGravityForces();
        Assert.True(m1.getNetForces().epsilonEquals(Vector3.Zero),
                "Mass 1 has zero net forces when it is alone");

        TestMass m2 = new TestMass(1, 1, 0, 0);
        sim.addMass(m2);

        sim.applyGravityForces();
        System.out.println(m1.getNetForces());
        Assert.True(m1.getNetForces().epsilonEquals(Vector3.X.scl(GravitySimulation.G)),
                "Mass 1 is pulled in the positive X direction");
        Assert.True(m2.getNetForces().epsilonEquals(Vector3.X.scl(-GravitySimulation.G)),
                "Mass 2 is pulled in the negative X direction");
    }
}
