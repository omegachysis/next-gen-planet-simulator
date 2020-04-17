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
        addDialog.setWidth(520);
        addDialog.setHeight(400);

        Label addLabel1 = new Label("Name:", skin);
        addLabel1.setPosition(5,340);
        final TextField nameField = new TextField("",skin);
        nameField.setPosition(160,340);

        Label addLabel2 = new Label("Mass (kg):", skin);
        addLabel2.setPosition(5,295);
        TextField massField = new TextField("",skin);
        massField.setPosition(160,295);

        Label radiusLabel = new Label("Radius (m): ", skin);
        radiusLabel.setPosition(5, 250);
        var radiusField = new TextField("", skin);
        radiusField.setPosition(160, 250);

        Label addLabel3 = new Label("Position (m):", skin);
        addLabel3.setPosition(5,205);
        Label xPos = new Label("x: ", skin);
        xPos.setPosition(160, 205);
        TextField positionXField = new TextField("0",skin);
        positionXField.setPosition(185,205);
        positionXField.setSize(75, 30);
        var yPos = new Label("y: ", skin);
        yPos.setPosition(280, 205);
        var positionYField = new TextField("0", skin);
        positionYField.setPosition(305, 205);
        positionYField.setSize(75, 30);
        var zPos = new Label("z: ", skin);
        zPos.setPosition(400, 205);
        var positionZField = new TextField("0", skin);
        positionZField.setPosition(425, 205);
        positionZField.setSize(75, 30);

        Label addLabel4 = new Label("Velocity (m/s):", skin);
        addLabel4.setPosition(5,160);
        Label xVel = new Label("x: ", skin);
        xVel.setPosition(160, 160);
        TextField velocityXField = new TextField("0",skin);
        velocityXField.setPosition(185,160);
        velocityXField.setSize(75, 30);
        var yVel = new Label("y: ", skin);
        yVel.setPosition(280, 160);
        var velocityYField = new TextField("0", skin);
        velocityYField.setPosition(305, 160);
        velocityYField.setSize(75, 30);
        var zVel = new Label("z: ", skin);
        zVel.setPosition(400, 160);
        var velocityZField = new TextField("0", skin);
        velocityZField.setPosition(425, 160);
        velocityZField.setSize(75, 30);

        Label addLabel5 = new Label("Spin:", skin);
        addLabel5.setPosition(5, 115);
        Label xSpin = new Label("x: ", skin);
        xSpin.setPosition(160, 115);
        TextField spinXField = new TextField("0",skin);
        spinXField.setPosition(185,115);
        spinXField.setSize(75, 30);
        var ySpin = new Label("y: ", skin);
        ySpin.setPosition(280, 115);
        var spinYField = new TextField("0", skin);
        spinYField.setPosition(305, 115);
        spinYField.setSize(75, 30);
        var zSpin = new Label("z: ", skin);
        zSpin.setPosition(400, 115);
        var spinZField = new TextField("0", skin);
        spinZField.setPosition(425, 160);
        spinZField.setSize(75, 30);

        TextButton addButton = new TextButton("Add", skin);
        addButton.setPosition(215, 10);
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
                    double position = Double.parseDouble(positionXField.getText());
                    double velocity = Double.parseDouble(velocityXField.getText());
                    double spin = Double.parseDouble(spinXField.getText());

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
        cancelButton.setPosition(260, 10);
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
        table.add(radiusLabel);
        table.add(radiusField);
        table.add(xPos);
        table.add(positionXField);
        table.add(yPos);
        table.add(positionYField);
        table.add(zPos);
        table.add(positionZField);
        table.add(addLabel4);
        table.add(xVel);
        table.add(velocityXField);
        table.add(yVel);
        table.add(velocityYField);
        table.add(zVel);
        table.add(velocityZField);
        table.add(addLabel5);
        table.add(xSpin);
        table.add(spinXField);
        table.add(ySpin);
        table.add(spinYField);
        table.add(zSpin);
        table.add(spinZField);
        table.add(addButton);
        table.add(cancelButton);

        addDialog.addActor(addLabel1);
        addDialog.addActor(nameField);
        addDialog.addActor(addLabel2);
        addDialog.addActor(massField);
        addDialog.addActor(addLabel3);
        addDialog.addActor(radiusLabel);
        addDialog.addActor(radiusField);
        addDialog.addActor(xPos);
        addDialog.addActor(positionXField);
        addDialog.addActor(yPos);
        addDialog.addActor(positionYField);
        addDialog.addActor(zPos);
        addDialog.addActor(positionZField);
        addDialog.addActor(addLabel4);
        addDialog.addActor(xVel);
        addDialog.addActor(velocityXField);
        addDialog.addActor(yVel);
        addDialog.addActor(velocityYField);
        addDialog.addActor(zVel);
        addDialog.addActor(velocityZField);
        addDialog.addActor(addLabel5);
        addDialog.addActor(xSpin);
        addDialog.addActor(spinXField);
        addDialog.addActor(ySpin);
        addDialog.addActor(spinYField);
        addDialog.addActor(zSpin);
        addDialog.addActor(spinZField);
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
