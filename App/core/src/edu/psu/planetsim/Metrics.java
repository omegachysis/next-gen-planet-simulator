package edu.psu.planetsim;

public final class Metrics {
    private static final float EARTH_MASS = 5.97e24f;
    private static final float M_PER_SOLAR_RADIUS = 6.957e8f;

    // Gravitational constant in cubic solar radii earth masses per day squared:
    public static final float G = 0.0088337f * 4;

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

    public static float m_s(double m_s) {
        return (float)(m_s * 86400 / M_PER_SOLAR_RADIUS);
    }

    public static double speed(float dx) {
        return dx * M_PER_SOLAR_RADIUS / 86400;
    }
}