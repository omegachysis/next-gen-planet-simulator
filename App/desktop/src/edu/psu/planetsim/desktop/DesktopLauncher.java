package edu.psu.planetsim.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import edu.psu.planetsim.PlanetSim;

public class DesktopLauncher {
	public static void main (String[] arg) throws Exception {

		// Run unit tests.
		GravitySimulationTests.run();

		Assert.PrintResults();

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new PlanetSim(), config);
	}
}
