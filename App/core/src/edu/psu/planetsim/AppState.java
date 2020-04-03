package edu.psu.planetsim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

/** Encapsulates all data about the planet simulator's
 * application state, including user settings and 
 * all of the user's data about celestial bodies and more.
 */
public class AppState
{
    public HashMap<UUID, CelestialBody> bodies = new HashMap<>();

    public float speed = 1;
    public boolean paused = false;
    public float zoom = 0.6f;
    public String inputtext = "Input text will be displayed here...";

    public static class CelestialBody
    {
        public UUID id;
        public String name;
        public double mass;
        public Vector3 position;
        public Vector3 velocity;
        public Vector3 spin;
        public Quaternion orientation;
        public float[] elevationMap;
        public ArrayList<UUID> satellites = new ArrayList<>();
    }
}