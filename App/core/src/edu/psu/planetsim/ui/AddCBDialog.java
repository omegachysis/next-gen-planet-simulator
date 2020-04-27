package edu.psu.planetsim.ui;

import java.util.Random;
import java.util.UUID;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Timer;

import edu.psu.planetsim.AppState;
import edu.psu.planetsim.Metrics;
import edu.psu.planetsim.graphics.TerrainBuilder;
import edu.psu.planetsim.physics.CelestialSim;

public class AddCBDialog extends BaseAddCbDialog
{
    AddCBDialog(AppState appState, Stage stage, Skin skin,
    CelestialSim sim)
    {
        super(appState, stage, skin, sim, "Add Celestial Body", "1e3", "1e10");
    }

    protected void onConfirm(String name, double mass, double radius,
    Vector3 position, Vector3 velocity, Vector3 spin) {
        final var newCB = new AppState.CelestialBody();
        newCB.id = UUID.randomUUID();
        newCB.name = name;
        newCB.mass = Metrics.kg(mass);
        newCB.position = new Vector3();
        newCB.velocity = new Vector3();
        newCB.spin = spin;
        newCB.orientation = new Quaternion().setFromCross(Vector3.Z, newCB.spin);
        newCB.positionRelativeToSun = position;
        newCB.velocityRelativeToSun = Metrics.km(velocity);
        newCB.radius = Metrics.km(radius);
        newCB.seed = new Random().nextInt(Integer.MAX_VALUE);
        newCB.seaLevel = Metrics.km(radius);
        newCB.oceanColor = Color.BLUE;
        appState.bodies.put(newCB.id, newCB);
        appState.currentCelestialBodyId = newCB.id;
        appState.invokeChangeListeners(true);
    }

    protected void displaySuccessDialog()
    {
        Dialog success = new Dialog("Correct entry", skin);
        success.setPosition(500, 500);
        success.setWidth(300);
        success.setHeight(100);
        success.text("Your celestial body has been added.");
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                success.hide();
            }
        }, 2);

        stage.addActor(success);
    }

    protected void onClose() {}

    protected void onPositionEnteredChanged() {}
}
