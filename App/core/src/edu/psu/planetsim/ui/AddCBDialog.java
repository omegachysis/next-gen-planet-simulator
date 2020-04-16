package edu.psu.planetsim.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Select;
import com.badlogic.gdx.utils.Timer;

import edu.psu.planetsim.AppState;
import edu.psu.planetsim.Metrics;
import edu.psu.planetsim.graphics.TerrainBuilder;

import org.w3c.dom.Text;

import java.util.UUID;

public class AddCBDialog {
    AddCBDialog(AppState _appState, Stage stage, Skin skin, SelectBox<String> editSelect){
        Dialog addDialog = new Dialog("Add Celestial Body", skin);
        addDialog.setMovable(true);
        addDialog.setPosition(10, 200);
        addDialog.setWidth(330);
        addDialog.setHeight(400);

        Label addLabel1 = new Label("Name:", skin);
        addLabel1.setPosition(5,340);
        final TextField nameField = new TextField("",skin);
        nameField.setPosition(160,340);

        Label addLabel2 = new Label("Mass (kg):", skin);
        addLabel2.setPosition(5,295);
        TextField massField = new TextField("",skin);
        massField.setPosition(160,295);

        Label addLabel3 = new Label("Position (m):", skin);
        addLabel3.setPosition(5,250);
        TextField positionField = new TextField("",skin);
        positionField.setPosition(160,250);

        Label addLabel4 = new Label("Velocity (m/s):", skin);
        addLabel4.setPosition(5,205);
        TextField velocityField = new TextField("",skin);
        velocityField.setPosition(160,205);

        Label addLabel5 = new Label("Spin (x, y, z):",skin);
        addLabel5.setPosition(5, 160);
        TextField spinField = new TextField("",skin);
        spinField.setPosition(160,160);

        TextButton addButton = new TextButton("Add", skin);
        addButton.setPosition(115, 10);
        addButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Dialog wrong = new Dialog("Incorrect entry", skin);
                wrong.text("One or more of your values is invalid. \n Please try again.");
                wrong.setPosition(500, 500);
                wrong.setWidth(400);
                wrong.setHeight(100);

                try {
                    String name = nameField.getText();
                    double mass = Double.parseDouble(massField.getText());
                    double position = Double.parseDouble(positionField.getText());
                    double velocity = Double.parseDouble(velocityField.getText());
                    double spin = Double.parseDouble(spinField.getText());

                    addCB(_appState, name, mass, position);
                } catch (NumberFormatException n) {
                    stage.addActor(wrong);
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            wrong.hide();
                        }
                    }, 1);

                    return;
                }

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
                addDialog.hide();
            }
        });

        TextButton cancelButton = new TextButton("Cancel", skin);
        cancelButton.setPosition(160, 10);
        cancelButton.addListener(e -> {
            if (cancelButton.isPressed()) {
                addDialog.hide();
            }
            return true;
        });

        Table table = new Table(skin);
        table.add(addLabel1);
        table.add(nameField);
        table.add(addLabel2);
        table.add(massField);
        table.add(addLabel3);
        table.add(positionField);
        table.add(addLabel4);
        table.add(velocityField);
        table.add(addLabel5);
        table.add(spinField);
        table.add(addButton);
        table.add(cancelButton);

        addDialog.addActor(addLabel1);
        addDialog.addActor(nameField);
        addDialog.addActor(addLabel2);
        addDialog.addActor(massField);
        addDialog.addActor(addLabel3);
        addDialog.addActor(positionField);
        addDialog.addActor(addLabel4);
        addDialog.addActor(velocityField);
        addDialog.addActor(addLabel5);
        addDialog.addActor(spinField);
        addDialog.addActor(addButton);
        addDialog.addActor(cancelButton);
        stage.addActor(addDialog);
        editSelect.setSelectedIndex(0);
    }

    private void addCB(AppState _appState, String name, double mass, double position) {
        final var newCB = new AppState.CelestialBody();
        newCB.id = UUID.randomUUID();
        newCB.name = name;
        newCB.mass = Metrics.kg(mass);
        newCB.position = new Vector3();
        newCB.velocity = new Vector3();
        newCB.spin = new Vector3();
        newCB.orientation = new Quaternion().setFromCross(Vector3.Y, newCB.spin);
        newCB.positionRelativeToSun = new Vector3(Metrics.m(position), 0, 0); // 1 AU
        newCB.velocityRelativeToSun = new Vector3();
        var radius = Metrics.m(1e6);
        newCB.elevationMap = TerrainBuilder.MakeRandomElevationMap(100, radius);
        _appState.bodies.put(newCB.id, newCB);
        _appState.currentCelestialBodyId = newCB.id;
        _appState.needsRefresh = true;
    }
}
