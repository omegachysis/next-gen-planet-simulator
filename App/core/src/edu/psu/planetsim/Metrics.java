package edu.psu.planetsim;

public final class Metrics {
    
    // sqrt of gravitational constant is approx. 8.16946 * 10^-6
    private static final float SQRT_G = 8.16946f;
    private static final int SQRT_G_E10 = -6;

    private static final float EARTH_MASS = 5.97f;
    private static final int EARTH_MASS_E10 = 24;

    private static final float M_PER_AU = 1.496f;
    private static final int M_PER_AU_E10 = 11;

    /** Return naturalized mass from kilograms and the exponent in scientific notation. */
    public static float kg(float kg, int e10) {
        // Divide by earth's mass to first convert into earth units.
        kg /= EARTH_MASS;
        e10 -= EARTH_MASS_E10;

        // Scale all masses by the recip. of the sqrt. of the gravitational constant 
        // to get naturalized units.
        kg *= SQRT_G;
        e10 += SQRT_G_E10;

        return kg * e10;
    }

    /** Return naturalized mass from kilograms. */
    public static float kg(float kg) {
        return kg(kg, 1);
    }

    /** Return naturalized length from meters and the exponent in scientific notation. */
    public static float m(float m, int e10) {
        // Convert all lengths to AU.
        m /= M_PER_AU;
        e10 -= M_PER_AU_E10;
        return m * e10;
    }

    /** Return naturalized length from meters. */
    public static float m(float m) {
        return m(m, 1);
    }
}