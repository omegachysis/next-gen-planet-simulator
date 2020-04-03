package edu.psu.planetsim.physics;

import com.badlogic.gdx.math.Vector3;

import edu.psu.planetsim.Metrics;

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
                final var a = _masses.get(i);
                final var b = _masses.get(j);

                // Calculate the displacement vector from mass A to mass B.
                final var r = b.getPosition().sub(a.getPosition());

                // Calculate the vector force gravity by taking the scaled normalized
                // vector from mass A to mass B by Newton's Law of Universal Gravitation.
                final var m = a.getMass() * b.getMass();
                final var force = r.cpy().nor().scl(Metrics.G * m / r.len2());

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
        final var val = new Vector3(0, 0, 0);
        var totalMass = 0f;
        for (final var mass : _masses) 
        {
            val.mulAdd(mass.getPosition(), mass.getMass());
            totalMass += mass.getMass();
        }
        return val.scl(1f / totalMass);
    }
}
