package edu.psu.planetsim.ui;

import java.util.UUID;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Timer;

import edu.psu.planetsim.AppState;
import edu.psu.planetsim.Metrics;
import edu.psu.planetsim.graphics.TerrainBuilder;
import edu.psu.planetsim.physics.CelestialSim;

public class AddSatDialog extends BaseAddCbDialog
{
    private PositionIndicator _positionIndicator;

    AddSatDialog(AppState appState, Stage stage, Skin skin,
    CelestialSim sim)
    {
        super(appState, stage, skin, sim, "Add Natural Satellite");
    }

    protected void onConfirm(String name, double mass, double radius,
    Vector3 position, Vector3 velocity, Vector3 spin) {
        final var newSat = new AppState.CelestialBody();
        newSat.id = UUID.randomUUID();
        newSat.name = name;
        newSat.isSatellite = true;
        newSat.mass = Metrics.kg(mass);
        newSat.position = Metrics.km(position);
        newSat.velocity = Metrics.km(velocity);
        newSat.spin = spin;
        newSat.orientation = new Quaternion().setFromCross(Vector3.Z, newSat.spin);
        newSat.positionRelativeToSun = new Vector3();
        newSat.velocityRelativeToSun = new Vector3();
        newSat.elevationMap = TerrainBuilder.MakeRandomElevationMap(100, Metrics.km(radius));
        appState.bodies.put(newSat.id, newSat);
        appState.getCurrentCelestialBody().satellites.add(newSat.id);
        appState.invokeChangeListeners(true);
    }

    protected void displaySuccessDialog()
    {
        Dialog success = new Dialog("Correct entry", skin);
        success.setPosition(500, 500);
        success.setWidth(300);
        success.setHeight(100);
        success.text("Your natural satellite has been added.");
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                success.hide();
            }
        }, 2);

        stage.addActor(success);
    }

    protected void onPositionEnteredChanged() {
        if (_positionIndicator != null)
            _positionIndicator.dispose();

        var pos = getEnteredPosition();
        if (pos != null)
            _positionIndicator = new PositionIndicator(
                stage, sim, pos
            );
    }
}
