package edu.psu.planetsim;

import java.util.Map;
import java.util.UUID;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

import edu.psu.planetsim.physics.CelestialSim;
import edu.psu.planetsim.ui.AppUi;

public class MainApp extends ApplicationAdapter 
{
	private AppState _appState;
	private Stage _stage;
	private CelestialSim _sim;

	public void create() 
	{
		// Set up app state.
		_appState = new AppState();

		// Set up UI and input controls.
		_stage = new Stage();
		final var multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(_stage);
		Gdx.input.setInputProcessor(multiplexer);
		new AppUi(_stage, _appState);

		// final var planet1 = new AppState.CelestialBody();
        // planet1.id = UUID.randomUUID();
        // planet1.name = "Planet 1";
        // planet1.mass = Metrics.kg(1.0e24);
        // planet1.position = new Vector3();
        // planet1.velocity = new Vector3();
        // planet1.spin = new Vector3(0, 0, -7.292115e-5f).rotate(Vector3.Y, 23.5f);
		// planet1.orientation = new Quaternion().setFromCross(Vector3.Y, planet1.spin);
		// _appState.bodies.put(planet1.id, planet1);
		// _appState.currentCelestialBody = planet1.id;

		// final var moon1 = new AppState.CelestialBody();
        // moon1.id = UUID.randomUUID();
        // moon1.name = "Moon 1";
        // moon1.mass = Metrics.kg(1.0e22);
        // moon1.position = new Vector3(Metrics.m(10.0e6), 0, 0);
        // moon1.velocity = new Vector3();
        // moon1.spin = new Vector3();
		// moon1.orientation = new Quaternion();
		// _appState.bodies.put(moon1.id, moon1);

		// planet1.satellites.add(moon1.id);

        // final CelestialBody earth = new CelestialBody(
        //     Metrics.kg(5.97e24), // mass
        //     Metrics.m(6378.1e3), // radius
        //     Vector3.Zero, Vector3.Zero,
        //     new Vector3(0, 0, -7.292115e-5f).rotate(Vector3.Y, 23.5f), // spin
        //     "earth.jpg");
        // add(earth);
        // final CelestialBody luna = new CelestialBody(
        //     Metrics.kg(7.348e22), // mass
        //     Metrics.m(1737.1e3), // radius
        //     new Vector3(Metrics.m(357e6), 0, 0), // position
        //     new Vector3(0, Metrics.m(1100), 0), // velocity
        //     new Vector3(0, 0, 2.6617e-6f).rotate(Vector3.Y, 1.5f), // spin
        //     "luna.jpg");
        // add(luna);

		_sim = new CelestialSim(_appState, multiplexer);
	}

	public void resize(final int width, final int height)
	{
		_stage.getViewport().update(width, height, true);
	}

	public void render() 
	{
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		_sim.render();

        _stage.act(Gdx.graphics.getDeltaTime());
        _stage.draw();
	}
	
	public void dispose() 
	{
		_stage.dispose();
		_sim.dispose();
    }
}
