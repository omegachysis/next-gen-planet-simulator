package edu.psu.planetsim.ui;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;

public class SidePanelDialog {
    public SidePanelDialog (Skin skin, Stage stage, SelectBox<String> viewSelect)
    {
        Dialog sideDialog = new Dialog("Side panel", skin);
        sideDialog.setMovable(true);
        sideDialog.setResizable(true);
        sideDialog.setPosition(10, 200);
        sideDialog.setWidth(450);
        sideDialog.setHeight(400);
        sideDialog.button("Done", true);
        sideDialog.button("Close", true);

        Label nameLabel1 = new Label("Modify Physical Properties:", skin);
        nameLabel1.setPosition(5, 350);

        CheckBox pp1 = new CheckBox("Mass (M⊕):", skin);
        pp1.setPosition(7,310);
        TextField Tpp1 = new TextField("",skin);
        Tpp1.setPosition(160,310);
        Tpp1.setWidth(100);
        Tpp1.setHeight(20);
        Tpp1.setVisible(false);
        pp1.addListener(e -> {
            if (pp1.isChecked()){
                Tpp1.setVisible(true);
            } else{
                Tpp1.setVisible(false);
            }
            return true;
        });

        CheckBox pp5 = new CheckBox("Radius(m):", skin);
        pp5.setPosition(7,280);
        TextField Tpp5 = new TextField("",skin);
        Tpp5.setPosition(160,280);
        Tpp5.setWidth(100);
        Tpp5.setHeight(20);
        Tpp5.setVisible(false);
        pp5.addListener(e -> {
            if (pp5.isChecked()){
                Tpp5.setVisible(true);
            } else{
                Tpp5.setVisible(false);
            }
            return true;
        });

        CheckBox pp2 = new CheckBox("Position(m):", skin);
        pp2.setPosition(7,250);
        Label xPos = new Label("x: ", skin);
        xPos.setPosition(150, 250);
        xPos.setVisible(false);
        TextField posXField = new TextField("0",skin);
        posXField.setPosition(170,250);
        posXField.setSize(60, 20);
        posXField.setVisible(false);

        var yPos = new Label("y: ", skin);
        yPos.setPosition(250, 250);
        yPos.setVisible(false);
        var posYField = new TextField("0", skin);
        posYField.setPosition(270, 250);
        posYField.setSize(60, 20);
        posYField.setVisible(false);

        var zPos = new Label("z: ", skin);
        zPos.setPosition(350, 250);
        zPos.setVisible(false);
        var posZField = new TextField("0",  skin);
        posZField.setPosition(370, 250);
        posZField.setSize(60, 20);
        posZField.setVisible(false);

        pp2.addListener(e -> {
            if (pp2.isChecked()){
                xPos.setVisible(true);
                yPos.setVisible(true);
                zPos.setVisible(true);
                posXField.setVisible(true);
                posYField.setVisible(true);
                posZField.setVisible(true);
            } else{
                xPos.setVisible(false);
                yPos.setVisible(false);
                zPos.setVisible(false);
                posXField.setVisible(false);
                posYField.setVisible(false);
                posZField.setVisible(false);
            }
            return true;
        });

        CheckBox pp3 = new CheckBox("Velocity (m/s):",skin);
        pp3.setPosition(7, 220);
        Label xVel = new Label("x: ", skin);
        xVel.setPosition(150, 220);
        xVel.setVisible(false);
        TextField velXField = new TextField("0",skin);
        velXField.setPosition(170,220);
        velXField.setSize(60, 20);
        velXField.setVisible(false);

        var yVel = new Label("y: ", skin);
        yVel.setPosition(250, 220);
        yVel.setVisible(false);
        var velYField = new TextField("0", skin);
        velYField.setPosition(270, 220);
        velYField.setSize(60, 20);
        velYField.setVisible(false);

        var zVel = new Label("z: ", skin);
        zVel.setPosition(350, 220);
        zVel.setVisible(false);
        var velZField = new TextField("0",  skin);
        velZField.setPosition(370, 220);
        velZField.setSize(60, 20);
        velZField.setVisible(false);

        pp3.addListener(e -> {
            if (pp3.isChecked()){
                xVel.setVisible(true);
                yVel.setVisible(true);
                zVel.setVisible(true);
                velXField.setVisible(true);
                velYField.setVisible(true);
                velZField.setVisible(true);
            } else{
                xVel.setVisible(false);
                yVel.setVisible(false);
                zVel.setVisible(false);
                velXField.setVisible(false);
                velYField.setVisible(false);
                velZField.setVisible(false);
            }
            return true;
        });

        CheckBox pp4 = new CheckBox("Spin:",skin);
        pp4.setPosition(7, 190);

        Label xSpin = new Label("x: ", skin);
        xSpin.setPosition(150, 190);
        xSpin.setVisible(false);
        TextField spinXField = new TextField("0",skin);
        spinXField.setPosition(170,190);
        spinXField.setSize(60, 20);
        spinXField.setVisible(false);

        var ySpin = new Label("y: ", skin);
        ySpin.setPosition(250, 190);
        ySpin.setVisible(false);
        var spinYField = new TextField("0", skin);
        spinYField.setPosition(270, 190);
        spinYField.setSize(60, 20);
        spinYField.setVisible(false);

        var zSpin = new Label("z: ", skin);
        zSpin.setPosition(350, 190);
        zSpin.setVisible(false);
        var spinZField = new TextField("0",  skin);
        spinZField.setPosition(370, 190);
        spinZField.setSize(60, 20);
        spinZField.setVisible(false);

        pp4.addListener(e -> {
                if (pp4.isChecked()) {
                    xSpin.setVisible(true);
                    ySpin.setVisible(true);
                    zSpin.setVisible(true);
                    spinXField.setVisible(true);
                    spinYField.setVisible(true);
                    spinZField.setVisible(true);
                } else {
                    xSpin.setVisible(false);
                    ySpin.setVisible(false);
                    zSpin.setVisible(false);
                    spinXField.setVisible(false);
                    spinYField.setVisible(false);
                    spinZField.setVisible(false);
                }
                return true;
        });

        var modifyButton = new TextButton("Modify", skin);
        modifyButton.setPosition(200, 150);
        modifyButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                Dialog wrong = new Dialog("Incorrect entry", skin);
                wrong.text("One or more of your values is invalid. \n Please try again.");
                wrong.setPosition(500, 500);
                wrong.setWidth(400);
                wrong.setHeight(100);

                try {
                    double mass = Double.parseDouble(Tpp1.getText());
                    double radius = Double.parseDouble(Tpp5.getText());

                    double xPos = Double.parseDouble(posXField.getText());
                    double yPos = Double.parseDouble(posYField.getText());
                    double zPos = Double.parseDouble(posZField.getText());
                    Vector3 position = new Vector3((float) xPos, (float) yPos, (float) zPos);

                    double xVel = Double.parseDouble(velXField.getText());
                    double yVel = Double.parseDouble(velYField.getText());
                    double zVel = Double.parseDouble(velZField.getText());
                    Vector3 velocity = new Vector3((float) xVel, (float) yVel, (float) zVel);

                    double xSpin = Double.parseDouble(spinXField.getText());
                    double ySpin = Double.parseDouble(spinYField.getText());
                    double zSpin = Double.parseDouble(spinZField.getText());
                    Vector3 spin = new Vector3((float) xSpin, (float) ySpin, (float) zSpin);
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
                success.setWidth(400);
                success.setHeight(100);
                success.text("Selected physical properties has been modified!");
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        success.hide();
                    }
                }, 2);

                stage.addActor(success);
                sideDialog.hide();

            }
    });

        sideDialog.addActor(modifyButton);

        Table table = new Table(skin);
        table.add(nameLabel1);
        table.add(pp1);
        table.add(pp2);
        table.add(pp3);
        table.add(pp4);
        table.add(pp5);
        table.add(Tpp1);
        table.add(Tpp5);
        table.add(xPos);
        table.add(yPos);
        table.add(zPos);
        table.add(xVel);
        table.add(yVel);
        table.add(zVel);
        table.add(xSpin);
        table.add(ySpin);
        table.add(zSpin);
        table.add(spinXField);
        table.add(spinYField);
        table.add(spinZField);
        table.add(velXField);
        table.add(velYField);
        table.add(velZField);
        table.add(posXField);
        table.add(posYField);
        table.add(posZField);

        sideDialog.addActor(nameLabel1);
        sideDialog.addActor(pp1);
        sideDialog.addActor(pp2);
        sideDialog.addActor(pp3);
        sideDialog.addActor(pp4);
        sideDialog.addActor(pp5);
        sideDialog.addActor(Tpp1);
        sideDialog.addActor(Tpp5);
        sideDialog.addActor(xPos);
        sideDialog.addActor(yPos);
        sideDialog.addActor(zPos);
        sideDialog.addActor(xVel);
        sideDialog.addActor(yVel);
        sideDialog.addActor(zVel);
        sideDialog.addActor(xSpin);
        sideDialog.addActor(ySpin);
        sideDialog.addActor(zSpin);
        sideDialog.addActor(spinXField);
        sideDialog.addActor(spinYField);
        sideDialog.addActor(spinZField);
        sideDialog.addActor(velXField);
        sideDialog.addActor(velYField);
        sideDialog.addActor(velZField);
        sideDialog.addActor(posXField);
        sideDialog.addActor(posYField);
        sideDialog.addActor(posZField);
        sideDialog.addActor(table);
        stage.addActor(sideDialog);
        viewSelect.setSelectedIndex(0);

    }
}
