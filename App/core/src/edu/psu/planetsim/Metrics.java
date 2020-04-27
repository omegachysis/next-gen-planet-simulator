package edu.psu.planetsim;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

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

    /** Return naturalized length from meters. */
    public static Vector3 m(final Vector3 m) 
    {
        return new Vector3(
            Metrics.m(m.x), Metrics.m(m.y), Metrics.m(m.z)
        );
    }

    /** Return naturalized length from meters. */
    public static float km(final double km) 
    {
        return m(km * 1000);
    }

    /** Return naturalized length from meters. */
    public static Vector3 km(final Vector3 km) 
    {
        return m(km.cpy().scl(1000));
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

    public static Vector2 toSphericalCoords(final Vector3 v)
    {
        var vnorm = v.cpy().nor();

        // Convert the cartesian x,y,z coordinates 
        // to spherical coordinates on the elevation map
        // assuming we are on the surface of the unit sphere.
        var lat = Math.acos(-vnorm.z);
        var lon = Math.atan2(vnorm.y, vnorm.x) + Math.PI;
        return new Vector2((float)(lat / Math.PI), (float)(lon / Math.PI / 2));
    }

    public static Vector3 toCartesianCoords(float lat, float lon)
    {
        // Normalize to unit spherical coordinates.
        var theta = lat * Math.PI;
        var phi = lon * Math.PI * 2;

        // Then use that to convert to cartesian 3D coordinates:
        var x = (float)(Math.sin(theta) * Math.cos(phi));
        var y = (float)(Math.sin(theta) * Math.sin(phi));
        var z = (float)(Math.cos(theta));

        return new Vector3(x, y, z);
    }
}