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

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class DeleteDialog {
    DeleteDialog(AppState _appState, Stage stage, Skin skin, SelectBox<String> editSelect){
        Dialog deleteDialog = new Dialog("Delete Celestial Body", skin);
        deleteDialog.setResizable(true);
        deleteDialog.setMovable(true);
        deleteDialog.setPosition(500, 500);
        deleteDialog.setWidth(310);

        var deleteSelect = new SelectBox<>(skin);
        deleteSelect.setItems("Select");
        deleteSelect.setPosition(50, 50);
        deleteSelect.setWidth(200);
        deleteDialog.addActor(deleteSelect);

        deleteSelect.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (_appState.bodies.isEmpty()){
                    deleteSelect.showList();
                }else {
                    var allCBs = new ArrayList<AppState.CelestialBody>(_appState.bodies.values());
                    var cbNames = new ArrayList<String>();
                    for (int i = 0; i < allCBs.size(); i++) {
                        var current = allCBs.get(i);
                        if (!current.isSatellite) {
                            String currentName = current.name;
                            cbNames.add(currentName);
                        }
                    }

                    // Make sure the select box displays
                    // this placeholder text.
                    cbNames.add(0, "Select");

                    deleteSelect.setItems(cbNames.toArray());
                    deleteSelect.showList();
                }
            }
        });

        var deleteButton = new TextButton("Delete", skin);
        deleteButton.setPosition(90, 3);
        deleteButton.addListener(e -> {
            if (deleteButton.isPressed()) {
                var selected = deleteSelect.getSelected();
                if (selected == "Select"){
                    Dialog error = new Dialog ("Invalid entry", skin);
                    error.setPosition(530, 390);
                    error.setHeight(100);
                    error.setWidth(200);
                    error.text("Invalid Selection \n Please try again.");
                    stage.addActor(error);
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            error.hide();
                        }
                    }, 1);
                }else {

                    Dialog delDailog = new Dialog("", skin);
                    delDailog.setPosition(500, 500);
                    delDailog.setWidth(300);
                    delDailog.setHeight(100);
                    delDailog.text("Your celestial body has been deleted.");
                    deleteDialog.hide();

                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            delDailog.hide();
                        }
                    }, 2);
                    stage.addActor(delDailog);
                }
                    /*
                    Dialog confirmDialog = new Dialog("Delete Celestial Body", skin);
                    confirmDialog.setResizable(true);
                    confirmDialog.setMovable(false);
                    confirmDialog.setPosition(520, 340);
                    confirmDialog.setWidth(310);
                    confirmDialog.text("Are you sure to delete this celestial body? \n Remember: You'll lose this celestial body.");

                    TextButton yesButton = new TextButton("Yes", skin);
                    yesButton.setPosition(100, 10);
                    yesButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            if (yesButton.isPressed()) {
                                confirmDialog.hide();
                                deleteDialog.hide();
                            }
                        }
                    });

                    TextButton noButton = new TextButton("No", skin);
                    noButton.setPosition(150, 10);
                    noButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            if (noButton.isPressed()) {
                                deleteDialog.hide();
                                confirmDialog.hide();
                            }
                        }
                    });

                    Table table4 = new Table(skin);
                    table4.add(yesButton);
                    table4.add(noButton);

                    confirmDialog.addActor(yesButton);
                    confirmDialog.addActor(noButton);
                    confirmDialog.addActor(table4);
                    stage.addActor(confirmDialog);

                     */
            }
            return true;
        });


        deleteDialog.addActor(deleteButton);

        var closeButton = new TextButton("Cancel", skin);
        closeButton.setPosition(170, 3);
        closeButton.addListener(e -> {
            if (closeButton.isPressed()) {
                deleteDialog.hide();
            }
            return true;
        });

        Table table3 = new Table(skin);
        table3.add(deleteButton);
        table3.add(closeButton);

        deleteDialog.addActor(deleteButton);
        deleteDialog.addActor(closeButton);
        deleteDialog.addActor(table3);
        stage.addActor(deleteDialog);
        editSelect.setSelectedIndex(0);
    }
}
