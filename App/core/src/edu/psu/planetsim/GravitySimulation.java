package edu.psu.planetsim;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class GravitySimulation 
{
    private final ArrayList<IMass> _masses;

    public GravitySimulation() 
    {
        _masses = new ArrayList<>();
    }

    public void addMass(final IMass mass) 
    {
        _masses.add(mass);
    }

    public void removeMass(final IMass mass) 
    {
        _masses.remove(mass);
    }

    /**
     * Applies the force of gravity to all masses in the simulation in the current
     * configuration.
     */
    public void applyGravityForces() 
    {
        for (int i = 0; i < _masses.size(); i++) 
        {
            for (int j = i + 1; j < _masses.size(); j++) 
            {
                final IMass a = _masses.get(i);
                final IMass b = _masses.get(j);

                // Calculate the displacement vector from mass A to mass B.
                final Vector3 r = b.getPosition().sub(a.getPosition());

                // Calculate the vector force gravity by taking the scaled normalized
                // vector from mass A to mass B by Newton's Law of Universal Gravitation.
                final float m = a.getMass() * b.getMass();
                final Vector3 force = r.cpy().nor().scl(Metrics.G * m / r.len2());

                // Take advantage of Newton's Third Law of Motion to calculate
                // the mutual attraction of any two bodies once.
                a.applyForce(force);
                b.applyForce(force.scl(-1));
            }
        }
    }

    public Vector3 getCenterOfMass() 
    {
        // Vector form of center of mass formula:
        final Vector3 val = new Vector3(0, 0, 0);
        float totalMass = 0f;
        for (final IMass mass : _masses) 
        {
            val.mulAdd(mass.getPosition(), mass.getMass());
            totalMass += mass.getMass();
        }
        return val.scl(1f / totalMass);
    }
}
