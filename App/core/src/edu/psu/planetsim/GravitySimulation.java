package edu.psu.planetsim;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class GravitySimulation {
    private ArrayList<IMass> _masses;

    public GravitySimulation() {
        _masses = new ArrayList<>();
    }

    public void addMass(IMass mass) {
        _masses.add(mass);
    }

    public void removeMass(IMass mass) {
        _masses.remove(mass);
    }

    /**
     * Applies the force of gravity to all masses in the simulation
     * in the current configuration.
     */
    public void applyGravityForces() {
        for (int i = 0; i < _masses.size(); i++) {
            for (int j = i + 1; j < _masses.size(); j++) {
                IMass a = _masses.get(i);
                IMass b = _masses.get(j);

                // Calculate the displacement vector from mass A to mass B.
                Vector3 r = b.getPosition().sub(a.getPosition());

                // Calculate the vector force gravity by taking the scaled normalized
                // vector from mass A to mass B by Newton's Law of Universal Gravitation.
                float m = a.getMass() * b.getMass();
                Vector3 force =  r.nor().scl(Math.min(m, m / r.len2()));

                // Take advantage of Newton's Third Law of Motion to calculate
                // the mutual attraction of any two bodies once.
                a.applyForce(force);
                b.applyForce(force.scl(-1));
            }
        }
    }

    public Vector3 getCenterOfMass() {
        Vector3 val = new Vector3(0, 0, 0);
        for (IMass mass : _masses) {
            val.mulAdd(mass.getPosition(), mass.getMass());
        }

        return val.scl(1f / _masses.size());
    }
}
