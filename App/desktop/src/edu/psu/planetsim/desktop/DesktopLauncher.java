package edu.psu.planetsim.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import edu.psu.planetsim.PlanetRenderTest;
import edu.psu.planetsim.MainApp;
import edu.psu.planetsim.ThermalTest;

public class DesktopLauncher {
	public static void main (String[] arg) throws Exception {

		// Run unit tests.
		// GravitySimulationTests.run();

		Assert.PrintResults();

		var config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new MainApp(), config);
		//new LwjglApplication(new ThermalTest(), config);
		//new LwjglApplication(new PlanetRenderTest(), config);
	}
}
