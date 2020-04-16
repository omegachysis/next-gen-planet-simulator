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
        SatDialog.setWidth(330);
        SatDialog.setHeight(400);
        stage.addActor(SatDialog);
        editSelect.setSelectedIndex(0);

        Label label1 = new Label("Name:", skin);
        label1.setPosition(5,340);
        final TextField field1 = new TextField("",skin);
        field1.setPosition(160,340);

        Label label2 = new Label("Mass (kg):", skin);
        label2.setPosition(5,295);
        TextField field2 = new TextField("",skin);
        field2.setPosition(160,295);

        Label label3 = new Label("Position (m):", skin);
        label3.setPosition(5,250);
        TextField field3 = new TextField("",skin);
        field3.setPosition(160,250);

        Label label4 = new Label("Velocity (m/s):", skin);
        label4.setPosition(5,205);
        TextField field4 = new TextField("",skin);
        field4.setPosition(160,205);

        Label label5 = new Label("Spin (x, y, z):",skin);
        label5.setPosition(5, 160);
        TextField field5 = new TextField("",skin);
        field5.setPosition(160,160);

        TextButton add = new TextButton("Add", skin);
        add.setPosition(115, 10);
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
                    double mass = Double.parseDouble(field2.getText());
                    double position = Double.parseDouble(field3.getText());
                    double velocity = Double.parseDouble(field4.getText());
                    double spin = Double.parseDouble(field5.getText());

                    addSat(_appState, name, mass, position);
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
        cancel.setPosition(160, 10);
        cancel.addListener(e -> {
            if (cancel.isPressed()) {
                SatDialog.hide();
            }
            return true;
        });

        Table table5 = new Table(skin);
        table5.add(label1);
        table5.add(field1);
        table5.add(label2);
        table5.add(field2);
        table5.add(label3);
        table5.add(field3);
        table5.add(label4);
        table5.add(field4);
        table5.add(label5);
        table5.add(field5);
        table5.add(add);
        table5.add(cancel);

        SatDialog.addActor(label1);
        SatDialog.addActor(field1);
        SatDialog.addActor(label2);
        SatDialog.addActor(field2);
        SatDialog.addActor(label3);
        SatDialog.addActor(field3);
        SatDialog.addActor(label4);
        SatDialog.addActor(field4);
        SatDialog.addActor(label5);
        SatDialog.addActor(field5);
        SatDialog.addActor(add);
        SatDialog.addActor(cancel);
        stage.addActor(SatDialog);
        editSelect.setSelectedIndex(0);
    }

    private void addSat(AppState _appState, String name,double mass, double position){
        final var newSat = new AppState.CelestialBody();
        newSat.id = UUID.randomUUID();
        newSat.name = name;
        newSat.isSatellite = true;
        newSat.mass = Metrics.kg(mass);
        newSat.position = new Vector3(Metrics.m(position), 0, 0);
        newSat.velocity = new Vector3();
        newSat.spin = new Vector3();
        newSat.orientation = new Quaternion().setFromCross(Vector3.Y, newSat.spin);
        newSat.positionRelativeToSun = new Vector3(); // 1 AU
        newSat.velocityRelativeToSun = new Vector3();
        var radius = Metrics.m(1e6);
        newSat.elevationMap = TerrainBuilder.MakeRandomElevationMap(100, radius);
        _appState.bodies.put(newSat.id, newSat);
        _appState.getCurrentCelestialBody().satellites.add(newSat.id);
        _appState.needsRefresh = true;
    }
}
