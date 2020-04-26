package edu.psu.planetsim.ui;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;

import edu.psu.planetsim.AppState;
import edu.psu.planetsim.Metrics;
import edu.psu.planetsim.physics.CelestialSim;

public abstract class BaseAddCbDialog 
{
    protected AppState appState;
    protected Stage stage;
    protected Skin skin;
    protected CelestialSim sim;

    private TextField _posXField;
    private TextField _posYField;
    private TextField _posZField;

    BaseAddCbDialog(AppState appState, Stage stage, Skin skin,
        CelestialSim sim, String title)
    {
        this.appState = appState;
        this.stage = stage;
        this.skin = skin;
        this.sim = sim;

        Dialog addDialog = new Dialog(title, skin);
        addDialog.setMovable(true);
        addDialog.setPosition(10, 200);
        addDialog.setWidth(520);
        addDialog.setHeight(400);
        addDialog.setModal(false);

        Label addLabel1 = new Label("Name:", skin);
        addLabel1.setPosition(5,340);
        final TextField nameField = new TextField("",skin);
        nameField.setPosition(160,340);

        Label addLabel2 = new Label("Mass (kg):", skin);
        addLabel2.setPosition(5,295);
        TextField massField = new TextField("",skin);
        massField.setPosition(160,295);

        Label radiusLabel = new Label("Radius (km): ", skin);
        radiusLabel.setPosition(5, 250);
        var radiusField = new TextField("", skin);
        radiusField.setPosition(160, 250);

        Label addLabel3 = new Label("Position (km):", skin);
        addLabel3.setPosition(5,205);
        Label xPos = new Label("x: ", skin);
        xPos.setPosition(160, 205);
        _posXField = new TextField("0",skin);
        _posXField.setPosition(185,205);
        _posXField.setSize(75, 30);
        var yPos = new Label("y: ", skin);
        yPos.setPosition(280, 205);
        _posYField = new TextField("0", skin);
        _posYField.setPosition(305, 205);
        _posYField.setSize(75, 30);
        var zPos = new Label("z: ", skin);
        zPos.setPosition(400, 205);
        _posZField = new TextField("0", skin);
        _posZField.setPosition(425, 205);
        _posZField.setSize(75, 30);

        final var positionChanged = new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                onPositionEnteredChanged();
            }
        };
        _posXField.addListener(positionChanged);
        _posYField.addListener(positionChanged);
        _posZField.addListener(positionChanged);
        positionChanged.changed(null, null);

        Label addLabel4 = new Label("Velocity (km/s):", skin);
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
                    double radius = Double.parseDouble(radiusField.getText());

                    double xvel = Double.parseDouble(velocityXField.getText());
                    double yvel = Double.parseDouble(velocityYField.getText());
                    double zvel = Double.parseDouble(velocityZField.getText());
                    Vector3 velocity = new Vector3((float) xvel, (float) yvel, (float)zvel);

                    var position = getEnteredPosition();
                    if (position == null)
                        displayError(stage, wrong);

                    double xspin = Double.parseDouble(spinXField.getText());
                    double yspin = Double.parseDouble(spinYField.getText());
                    double zspin = Double.parseDouble(spinZField.getText());
                    Vector3 spin = new Vector3((float) xspin, (float)yspin, (float)zspin);

                    onConfirm(name, mass, radius, position, velocity, spin);
                } catch (NumberFormatException n) {
                    displayError(stage, wrong);
                    return;
                }

                displaySuccessDialog();
                addDialog.hide();
                onClose();
            }

            private void displayError(Stage stage, Dialog wrong) {
                stage.addActor(wrong);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        wrong.hide();
                    }
                }, 1);
            }
        });

        TextButton cancelButton = new TextButton("Cancel", skin);
        cancelButton.setPosition(260, 10);
        cancelButton.addListener(e -> {
            if (cancelButton.isPressed()) {
                addDialog.hide();
                onClose();
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
        table.add(_posXField);
        table.add(yPos);
        table.add(_posYField);
        table.add(zPos);
        table.add(_posZField);
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
        addDialog.addActor(_posXField);
        addDialog.addActor(yPos);
        addDialog.addActor(_posYField);
        addDialog.addActor(zPos);
        addDialog.addActor(_posZField);
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
        
        //editSelect.setSelectedIndex(0);
    }

    protected Vector3 getEnteredPosition()
    {
        try
        {
            double xpos = Double.parseDouble(_posXField.getText());
            double ypos = Double.parseDouble(_posYField.getText());
            double zpos = Double.parseDouble(_posZField.getText());
            return Metrics.km(new Vector3((float)xpos, (float)ypos, (float)zpos));
        }
        catch (NumberFormatException n) 
        {
            return null;
        }
    }

    protected abstract void onClose();

    protected abstract void onPositionEnteredChanged();

    protected abstract void displaySuccessDialog();

    protected abstract void onConfirm(String name, double mass, double radius,
        Vector3 position, Vector3 velocity, Vector3 spin);
}
