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

public class AddSatDialog {
    AddSatDialog(AppState _appState, Stage stage, Skin skin, SelectBox<String> editSelect){
        Dialog SatDialog = new Dialog("Add Satellite ", skin);
        SatDialog.setMovable(true);
        SatDialog.setPosition(10, 200);
        SatDialog.setWidth(520);
        SatDialog.setHeight(400);
        stage.addActor(SatDialog);
        editSelect.setSelectedIndex(0);

        Label label1 = new Label("Name:", skin);
        label1.setPosition(5,340);
        final TextField field1 = new TextField("",skin);
        field1.setPosition(160,340);

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
        spinZField.setPosition(425, 115);
        spinZField.setSize(75, 30);

        TextButton add = new TextButton("Add", skin);
        add.setPosition(215, 10);
        add.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Dialog wrong = new Dialog("Incorrect entry", skin);
                wrong.text("One or more of your values is invalid. \n Please try again.");
                wrong.setPosition(500, 500);
                wrong.setWidth(400);
                wrong.setHeight(100);

                try {
                    String name = field1.getText();
                    double mass = Double.parseDouble(massField.getText());
                    double radius = Double.parseDouble(radiusField.getText());

                    double xpos = Metrics.m(Double.parseDouble(positionXField.getText()));
                    double ypos = Metrics.m(Double.parseDouble(positionYField.getText()));
                    double zpos = Metrics.m(Double.parseDouble(positionZField.getText()));
                    Vector3 position = new Vector3((float)xpos, (float)ypos, (float)zpos);

                    double xvel = Double.parseDouble(velocityXField.getText());
                    double yvel = Double.parseDouble(velocityYField.getText());
                    double zvel = Double.parseDouble(velocityZField.getText());
                    Vector3 velocity = new Vector3((float) xvel, (float) yvel, (float)zvel);
                    //double velocity = Double.parseDouble(velocityXField.getText());

                    double xspin = Double.parseDouble(spinXField.getText());
                    double yspin = Double.parseDouble(spinYField.getText());
                    double zspin = Double.parseDouble(spinZField.getText());
                    Vector3 spin = new Vector3((float) xspin, (float)yspin, (float)zspin);

//                    double position = Double.parseDouble(positionXField.getText());
//                    double velocity = Double.parseDouble(velocityXField.getText());
//                    double spin = Double.parseDouble(spinXField.getText());

                    addSat(_appState, name, mass, radius, position, velocity, spin);
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
                success.text("Your satellite has been added.");
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        success.hide();
                    }
                }, 2);

                stage.addActor(success);
                SatDialog.hide();
            }
        });

        TextButton cancel = new TextButton("Cancel", skin);
        cancel.setPosition(260, 10);
        cancel.addListener(e -> {
            if (cancel.isPressed()) {
                SatDialog.hide();
            }
            return true;
        });

        Table table = new Table(skin);
        table.add(label1);
        table.add(field1);
        table.add(addLabel2);
        table.add(massField);
        table.add(radiusLabel);
        table.add(radiusField);
        table.add(addLabel3);
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
        table.add(add);
        table.add(cancel);

        SatDialog.addActor(label1);
        SatDialog.addActor(field1);
        SatDialog.addActor(addLabel2);
        SatDialog.addActor(massField);
        SatDialog.addActor(radiusLabel);
        SatDialog.addActor(radiusField);
        SatDialog.addActor(addLabel3);
        SatDialog.addActor(xPos);
        SatDialog.addActor(positionXField);
        SatDialog.addActor(yPos);
        SatDialog.addActor(positionYField);
        SatDialog.addActor(zPos);
        SatDialog.addActor(positionZField);
        SatDialog.addActor(addLabel4);
        SatDialog.addActor(xVel);
        SatDialog.addActor(velocityXField);
        SatDialog.addActor(yVel);
        SatDialog.addActor(velocityYField);
        SatDialog.addActor(zVel);
        SatDialog.addActor(velocityZField);
        SatDialog.addActor(addLabel5);
        SatDialog.addActor(xSpin);
        SatDialog.addActor(spinXField);
        SatDialog.addActor(ySpin);
        SatDialog.addActor(spinYField);
        SatDialog.addActor(zSpin);
        SatDialog.addActor(spinZField);
        SatDialog.addActor(add);
        SatDialog.addActor(cancel);
        stage.addActor(SatDialog);
        editSelect.setSelectedIndex(0);
    }

    private void addSat(AppState _appState, String name, double mass, double radius,
                        Vector3 position, Vector3 velocity, Vector3 spin){
        final var newSat = new AppState.CelestialBody();
        newSat.id = UUID.randomUUID();
        newSat.name = name;
        newSat.isSatellite = true;
        newSat.mass = Metrics.kg(mass);
//        newSat.position = new Vector3(Metrics.m(position), 0, 0);
//        newSat.velocity = new Vector3();
//        newSat.spin = new Vector3();
        newSat.position = position;
        newSat.velocity = velocity;
        newSat.spin = spin;
        newSat.orientation = new Quaternion().setFromCross(Vector3.Y, newSat.spin);
        newSat.positionRelativeToSun = new Vector3(); // 1 AU
        newSat.velocityRelativeToSun = new Vector3();
//        var radius = Metrics.m(1e6);
        newSat.elevationMap = TerrainBuilder.MakeRandomElevationMap(100, Metrics.m(radius));
        _appState.bodies.put(newSat.id, newSat);
        _appState.getCurrentCelestialBody().satellites.add(newSat.id);
        _appState.needsRefresh = true;
    }
}
