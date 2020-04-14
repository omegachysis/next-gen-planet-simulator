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

public class NewDialog {
    NewDialog(Stage stage, Skin skin, SelectBox<String> fileSelect){
        final Dialog newDialog = new Dialog("New", skin);
        newDialog.setResizable(true);
        newDialog.setMovable(true);
        newDialog.setPosition(500, 500);
        newDialog.setWidth(250);
        newDialog.text("Start a new file? \n You'll lose all unsaved changes.");

        Table table2 = new Table(skin);
        table2.add(newDialog);

        var newButton = new TextButton("Yes", skin);
        newButton.setPosition(90, 3);
        newButton.addListener(e -> {
            if (newButton.isPressed()) {
                newDialog.hide();
            }
            return true;
        });
        table2.add(newButton);
        newDialog.addActor(newButton);

        var noButton = new TextButton("No", skin);
        noButton.setPosition(140, 3);
        noButton.addListener(e -> {
            if (noButton.isPressed()) {
                newDialog.hide();
            }
            return true;
        });
        table2.add(noButton);
        newDialog.addActor(noButton);

        stage.addActor(newDialog);
        fileSelect.setSelectedIndex(0);
    }
}
