package edu.psu.planetsim;

import java.util.UUID;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;

public class PlanetSim extends ApplicationAdapter 
{
	AppState appState;
	MenuBar menuBar;
	Stage stage;
	CelestialRenderer _cRenderer;

	public void create() 
	{
		appState = new AppState();

		stage = new Stage();

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);

		Gdx.input.setInputProcessor(multiplexer);
//		Gdx.input.setInputProcessor(stage);

		menuBar = new MenuBar(stage, appState);

		// sideBar = new SideBar(stage);
		// selectBox = new SelectBox(style);
		// selectBox = new SelectBox(style);

		AppState.CelestialBody planet1 = new AppState.CelestialBody();
        planet1.id = UUID.randomUUID();
        planet1.name = "Planet 1";
        planet1.mass = 1.0e24;
        planet1.position = new Vector3();
        planet1.velocity = new Vector3();
        planet1.spin = new Vector3(0, 0, -7.292115e-5f).rotate(Vector3.Y, 23.5f);
		planet1.orientation = new Quaternion().setFromCross(Vector3.Y, planet1.spin);
		appState.bodies.put(planet1.id, planet1);

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

		_cRenderer = new CelestialRenderer(appState);
		_cRenderer.setCurrentCelestialBody(planet1.id);
	}

	public void resize(final int width, final int height)
	{
		stage.getViewport().update(width, height, true);
	}

	public void render() 
	{
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		_cRenderer.render();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
	}
	
	public void dispose() 
	{
		stage.dispose();
		_cRenderer.dispose();
    }
}
