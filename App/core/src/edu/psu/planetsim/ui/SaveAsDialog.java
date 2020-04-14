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
public class SaveAsDialog {
    SaveAsDialog(Stage stage, Skin skin, SelectBox<String> fileSelect){
        final Dialog saveDialog = new Dialog("Save as", skin);
        saveDialog.setMovable(true);
        saveDialog.setResizable(true);
        saveDialog.setPosition(500, 500);
        saveDialog.setWidth(300);

        Label nameLabel1 = new Label("File name:", skin);
        nameLabel1.setPosition(5,95);
        TextField fileName1 = new TextField("",skin);
        fileName1.setPosition(100,95);

        Label nameLabel2 = new Label("Location:", skin);
        nameLabel2.setPosition(5,55);
        SelectBox<String> fileName2= new SelectBox<>(skin);
        fileName2.setItems("Desktop", "Documents", "Home", "Other Folder");
        fileName2.setPosition(100,55);
        fileName2.setWidth(90);

        Table table = new Table(skin);
        table.add(nameLabel1);
        table.add(fileName1);
        table.add(nameLabel2);
        table.add(fileName2);

        var saveButton = new TextButton("Save", skin);
        saveButton.setPosition(90, 3);
        saveButton.addListener(e -> {
            if (saveButton.isPressed()) {
                Dialog saved = new Dialog("", skin);
                saved.setMovable(true);
                saved.text("File Saved!");
                saved.setPosition(500, 500);
                saved.setWidth(250);

                stage.addActor(saved);
                saveDialog.hide();

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        saved.hide();
                    }
                }, 1);

            }
            return true;
        });

        table.add(saveButton);
        saveDialog.addActor(saveButton);

        var cancelButton = new TextButton("Cancel", skin);
        cancelButton.setPosition(140, 3);
        cancelButton.addListener(e -> {
            if (cancelButton.isPressed()) {
                saveDialog.hide();
            }
            return true;
        });
        table.add(cancelButton);
        saveDialog.addActor(cancelButton);

        saveDialog.addActor(nameLabel1);
        saveDialog.addActor(fileName1);
        saveDialog.addActor(nameLabel2);
        saveDialog.addActor(fileName2);
        stage.addActor(saveDialog);
        fileSelect.setSelectedIndex(0);
    }
}
