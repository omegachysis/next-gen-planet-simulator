package edu.psu.planetsim;

public final class Metrics 
{
    private static final float EARTH_MASS = 5.97e24f;
    private static final float M_PER_SOLAR_RADIUS = 6.957e8f;

    // Gravitational constant in cubic solar radii earth masses per second squared:
    public static final float G = 1.1833522605e-12f;

    /** Return naturalized mass from kilograms. */
    public static float kg(final double kg) 
    {
        // Divide by earth's mass to first convert into earth units.
        return (float)(kg / EARTH_MASS);
    }

    /** Return naturalized length from meters. */
    public static float m(final double m) 
    {
        // Convert all lengths to AU.
        return (float)(m / M_PER_SOLAR_RADIUS);
    }

    /** Return meters from naturalized length. */
    public static double length(final float x) 
    {
        return x * M_PER_SOLAR_RADIUS;
    }

    /** Return kilograms from naturalized mass. */
    public static double mass(final float x) 
    {
        return x * EARTH_MASS;
    }
}