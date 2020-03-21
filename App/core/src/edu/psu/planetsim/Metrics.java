package edu.psu.planetsim;

public final class Metrics {
    private static final float EARTH_MASS = 5.97e24f;
    private static final float M_PER_SOLAR_RADIUS = 6.957e8f;

    // Gravitational constant in cubic solar radii earth masses per second squared:
    public static final float G = 1.1833522605e-12f;

    /** Return naturalized mass from kilograms and the exponent in scientific notation. */
    public static float kg(double kg) {
        // Divide by earth's mass to first convert into earth units.
        return (float)(kg / EARTH_MASS);
    }

    /** Return naturalized length from meters and the exponent in scientific notation. */
    public static float m(double m) {
        // Convert all lengths to AU.
        return (float)(m / M_PER_SOLAR_RADIUS);
    }

    public static double length(float x) {
        return x * M_PER_SOLAR_RADIUS;
    }
}