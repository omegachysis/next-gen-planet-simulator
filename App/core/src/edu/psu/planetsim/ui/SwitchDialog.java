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
import edu.psu.planetsim.AppState.CelestialBody;
import edu.psu.planetsim.Metrics;
import edu.psu.planetsim.graphics.TerrainBuilder;

import org.w3c.dom.Text;

import java.util.*;

public class SwitchDialog {
    SwitchDialog (AppState _appState, Stage stage, Skin skin, SelectBox<String> viewSelect){
        Dialog switchDialog = new Dialog("Switch Celestial Body", skin);
        switchDialog.setMovable(true);
        switchDialog.setResizable(true);
        switchDialog.setPosition(500, 450);
        switchDialog.setWidth(300);
        switchDialog.setHeight(150);

        var switchLabel = new Label("Change selection", skin);
        switchLabel.setPosition(50, 85);
        switchDialog.addActor(switchLabel);

        var switchSelect = new SelectBox<>(skin);
        switchSelect.setItems("Select");
        switchSelect.setPosition(50, 50);
        switchSelect.setWidth(200);
        switchDialog.addActor(switchSelect);

        var change = new TextButton("Change", skin);
        change.setPosition(135, 3);
        change.addListener(e -> {
            if (change.isPressed()) {
                var selected = switchSelect.getSelected();
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
                    }, 2);
                }else{
                    switchDialog.hide();
                }
            }
            return true;
        });
        switchDialog.addActor(change);

        var close = new TextButton("Close", skin);
        close.setPosition(80, 3);
        close.addListener(e -> {
            if (close.isPressed()){
                switchDialog.hide();
            }
            return true;
        });
        switchDialog.addActor(close);

        switchSelect.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (_appState.bodies.isEmpty()){
                    switchSelect.showList();
                }else {
//                    Map.Entry<UUID, AppState.CelestialBody> entries = _appState.bodies.entrySet().iterator().next();
//                    AppState.CelestialBody newPlanet = entries.getValue();
//
//                    switchSelect.setItems("Select", newPlanet.name);

                    var allCBs = new ArrayList<CelestialBody>(_appState.bodies.values());
                    var cbNames = new ArrayList<String>();
                    for(int i = 0; i < allCBs.size(); i++){
                        var current = allCBs.get(i);
                        if (!current.isSatellite){
                            String currentName = current.name;
                            cbNames.add(currentName);
                        }
                    }

//                    String[] allNames = new String[cbNames.size()];
//                    int i = 0;
//                    for (Object value : cbNames){
//                        allNames[i] = (String) value;
//                        i++;
//                    }

                    // Make sure the select box displays
                    // this placeholder text.
                    cbNames.add(0, "Select");

                    switchSelect.setItems(cbNames.toArray());
                    switchSelect.showList();
                }
            }
        });

        stage.addActor(switchDialog);
        viewSelect.setSelectedIndex(0);
    }
}
