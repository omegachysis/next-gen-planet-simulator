package edu.psu.planetsim.ui;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Timer;

public class SidePanelDialog {
    public SidePanelDialog (Skin skin, Stage stage, SelectBox<String> viewSelect)
    {
        Dialog sideDialog = new Dialog("Side panel", skin);
        sideDialog.setMovable(true);
        sideDialog.setResizable(true);
        sideDialog.setPosition(10, 200);
        sideDialog.setWidth(280);
        sideDialog.setHeight(400);
        sideDialog.button("Done", true);
        sideDialog.button("Close", true);

        var modifyButton = new TextButton("Modify", skin);
        modifyButton.setPosition(100, 180);
        modifyButton.addListener(e -> {
            if (modifyButton.isPressed()) {
                Dialog modified = new Dialog("", skin);
                modified.setMovable(true);
                modified.text("Physical Properties Modified");
                modified.setPosition(600, 600);
                modified.setWidth(250);
                modified.setHeight(60);

                stage.addActor(modified);

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        modified.hide();
                    }
                }, 1);
            }
            return true;
        });

        sideDialog.addActor(modifyButton);

        Label nameLabel1 = new Label("Modify Physical Properties:", skin);
        nameLabel1.setPosition(5, 350);


        CheckBox pp1 = new CheckBox("Mass (M⊕):", skin);
        pp1.setPosition(7,310);
        TextField Tpp1 = new TextField("",skin);
        Tpp1.setPosition(160,310);
        Tpp1.setWidth(100);
        Tpp1.setHeight(20);
        pp1.addListener(e -> {
            if (pp1.isChecked()){
                Tpp1.setVisible(true);
            } else{
                Tpp1.setVisible(false);
            }
            return true;
        });


        CheckBox pp2 = new CheckBox("Position(x,y,z):", skin);
        pp2.setPosition(7,280);
        TextField Tpp2 = new TextField("",skin);
        Tpp2.setPosition(160,280);
        Tpp2.setWidth(100);
        Tpp2.setHeight(20);
        pp2.addListener(e -> {
            if (pp2.isChecked()){
                Tpp2.setVisible(true);
            } else{
                Tpp2.setVisible(false);
            }
            return true;
        });

        CheckBox pp3 = new CheckBox("Velocity (km/s):",skin);
        pp3.setPosition(7, 250);
        TextField Tpp3 = new TextField("",skin);
        Tpp3.setPosition(160,250);
        Tpp3.setWidth(100);
        Tpp3.setHeight(20);
        pp1.addListener(e -> {
            if (pp3.isChecked()){
                Tpp3.setVisible(true);
            } else{
                Tpp3.setVisible(false);
            }
            return true;
        });

        CheckBox pp4 = new CheckBox("Spin (x,y,z):",skin);
        pp4.setPosition(7, 220);
        TextField Tpp4 = new TextField("",skin);
        Tpp4.setPosition(160,220);
        Tpp4.setWidth(100);
        Tpp4.setHeight(20);
        pp1.addListener(e -> {
            if (pp4.isChecked()){
                Tpp4.setVisible(true);
            } else{
                Tpp4.setVisible(false);
            }
            return true;
        });


        Table table = new Table(skin);
        table.add(nameLabel1);
        table.add(pp1);
        table.add(pp2);
        table.add(pp3);
        table.add(pp4);
        table.add(Tpp1);
        table.add(Tpp2);
        table.add(Tpp3);
        table.add(Tpp4);

        sideDialog.addActor(nameLabel1);
        sideDialog.addActor(pp1);
        sideDialog.addActor(pp2);
        sideDialog.addActor(pp3);
        sideDialog.addActor(pp4);
        sideDialog.addActor(Tpp1);
        sideDialog.addActor(Tpp2);
        sideDialog.addActor(Tpp3);
        sideDialog.addActor(Tpp4);
        sideDialog.addActor(table);
        stage.addActor(sideDialog);
        viewSelect.setSelectedIndex(0);

    }

}
