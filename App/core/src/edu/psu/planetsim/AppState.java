package edu.psu.planetsim;

import java.util.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

/** Encapsulates all data about the planet simulator's
 * application state, including user settings and 
 * all of the user's data about celestial bodies and more.
 */
public class AppState
{
    public interface ChangeListener
    {
        public void run(boolean cbsChanged);
    }

    public HashMap<UUID, CelestialBody> bodies = new LinkedHashMap<>();

    public float speed = 1;
    public boolean paused = false;

    /** The current PARENT celestial body, do not put satellites in here! */
    public UUID currentCelestialBodyId = null;
    public ViewingMode viewingMode = ViewingMode.MainCelestialBody;

    /** Set the index of the satellite to zoom to, if in satellite mode. */
    public int satelliteFocusedIndex = 0;

    public float zoom = 0.6f;

    /** Bind runnables to invoke when the app state changes. */
    public transient ArrayList<ChangeListener> whenChanged = new ArrayList<>();

    public static enum ViewingMode
    {
        CenterOfMass,
        MainCelestialBody,
        NaturalSatellite
    }

    public static class CelestialBody
    {
        public UUID id;
        public boolean isSatellite;
        public String name;
        public double mass;
        public Vector3 position;
        public Vector3 velocity;
        public Vector3 spin;
        public Quaternion orientation;
        public int seed;
        public float radius;
        public ArrayList<UUID> satellites = new ArrayList<>();
        public Vector3 positionRelativeToSun;
        public Vector3 velocityRelativeToSun;
        public float seaLevel;
        public Color oceanColor;
    }

    /** Returns the current celestial body at the top level. */
    public CelestialBody getCurrentCelestialBody()
    {
        if (currentCelestialBodyId == null)
            return null;
        return bodies.get(currentCelestialBodyId);
    }

    /** Returns the celestial body or natural satellite that 
     * is being focused by the camera.
     */
    public CelestialBody getCelestialBodyFocused()
    {
        var body = getCurrentCelestialBody();
        if (viewingMode == ViewingMode.CenterOfMass)
            return null;
        else if (viewingMode == ViewingMode.MainCelestialBody)
            return body;
        else if (viewingMode == ViewingMode.NaturalSatellite)
            return bodies.get(body.satellites.get(satelliteFocusedIndex));
        else
            return null;
    }

    public void invokeChangeListeners(boolean cbsChanged)
    {
        for (var listener : whenChanged)
            listener.run(cbsChanged);
    }
}