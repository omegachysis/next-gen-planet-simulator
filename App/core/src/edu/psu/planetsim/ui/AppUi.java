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
import edu.psu.planetsim.AppState.ViewingMode;
import edu.psu.planetsim.graphics.TerrainBuilder;

import org.w3c.dom.Text;

import java.util.Map;
import java.util.UUID;

public class AppUi {
    private final AppState _appState;
    private final Skin skin;
    private final Stage _stage;

    private TextButton button1;
    private TextButton button2;
    private TextField speedbutton;
    private TextButton button4;
    private TextButton button5;
    private TextButton button7;
    private TextButton statusBarText;
    private TextButton.TextButtonStyle textButtonStyle;
    private Slider zoomSlider;
    private TextButton center_button;
    private Table _sideBar;
    private ScrollPane _sideBarPane;
    BitmapFont font;

    public AppUi(final Stage stage, AppState appState) 
    {
        _stage = stage;
        _appState = appState;
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        final SelectBox<String> fileSelect= new SelectBox<>(skin);
        fileSelect.setItems("File", "New", "Save As", "Save");
        fileSelect.setPosition(20, 680);
        fileSelect.setWidth(75);
        fileSelect.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                fileSelect.showList();
            }
        });

        fileSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String currentSelection = fileSelect.getSelected();
                switch (currentSelection){
                    case "New":
                        new NewDialog(stage, skin, fileSelect);
                        break;

                    case "Save As":
                        new SaveAsDialog(stage, skin, fileSelect);
                        break;

                    case "Save":
                        final Dialog save = new Dialog("", skin);
                        save.setResizable(true);
                        save.setMovable(true);
                        save.setPosition(500, 500);
                        save.setWidth(250);
                        save.text("File Saved!" );
                        stage.addActor(save);

                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                save.hide();
                            }
                        }, 1);

                        fileSelect.setSelectedIndex(0);
                        break;
                }
            }
        });

        final SelectBox<String> editSelect= new SelectBox<>(skin);
        editSelect.setItems("Edit", "Add Celestial Body", "Add Satellite", "Delete Celestial Body", "Duplicate");
        editSelect.setPosition(100, 680);
        editSelect.setWidth(75);
        editSelect.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                editSelect.showList();
            }
        });
        editSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String currentSelection = editSelect.getSelected();
                switch (currentSelection) {
                    case "Add Celestial Body":
                        new AddCBDialog(_appState, stage, skin, editSelect);
                        break;

                    case "Add Satellite":
                        new AddSatDialog(_appState, stage, skin, editSelect);
                        break;

                    case "Delete Celestial Body":
                        new DeleteDialog(_appState, stage, skin, editSelect);
                        break;

                    case "Duplicate":
//                        var dupDialog = new Dialog("Duplicate", skin);
//                        dupDialog.setMovable(true);
//                        dupDialog.setPosition(500, 500);
//                        dupDialog.setWidth(400);
//                        var dupSelect = new SelectBox<>(skin);
//                        dupSelect.setItems();

                        editSelect.setSelectedIndex(0);
                }
            }
        });

        final SelectBox<String> viewSelect = new SelectBox<>(skin);
        viewSelect.setItems("View","Show Side Panel", "Switch Celestial Body");
        viewSelect.setPosition(180, 680);
        viewSelect.setWidth(75);
        viewSelect.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                viewSelect.showList();
            }
        });
        viewSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String currentSelection = viewSelect.getSelected();
                switch (currentSelection) {
                    case "Show Side Panel":
                        new SidePanelDialog(skin, stage, viewSelect);
                        break;

                    case "Switch Celestial Body":
                        new SwitchDialog(_appState, stage, skin, viewSelect);
                    break;
                }
            }
        });

        final SelectBox<String> inspectSelect= new SelectBox<>(skin);
        inspectSelect.setItems("Inspection Mode", "Climate", "Thermodynamics", "Magnetism", "Van Allen Belt","Greenhouse Effect","Aerosols","Volcanoes");
        inspectSelect.setPosition(260, 680);
        inspectSelect.setWidth(150);
        inspectSelect.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                inspectSelect.showList();
            }
        });
        new InspectionMode(skin, stage, inspectSelect);

        //Created new class

        button1 = new TextButton("Play/Pause", skin, "default");
        button1.setSize(100, 35);
        button1.setPosition(1170, 680);
        button1.addListener(e ->{
            if (button1.isPressed()) {
                appState.paused = !appState.paused;
            }
            return true;
        });

        font = new BitmapFont();
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        button2 = new TextButton("Speed", textButtonStyle);
        button2.setSize(150, 80);
        button2.setPosition(940, 655);

        statusBarText = new TextButton("", textButtonStyle);
        statusBarText.setSize(150, 60);
        statusBarText.setPosition(50, 0);

        speedbutton = new TextField("",skin);
        speedbutton.setSize(110,35);
        speedbutton.setPosition(1050,680);
        speedbutton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                try
                {
                    _appState.speed = (float)Double.parseDouble(speedbutton.getText());
                }
                catch (NumberFormatException e)
                {
                    _appState.speed = 0.6f;
                }
            }
        });

        button4 = new TextButton("Add Planet", skin, "default");
        button4.setSize(110, 30);
        button4.setPosition(30, 635);

        button5 = new TextButton("Add Satellite", skin, "default");
        button5.setSize(110, 30);
        button5.setPosition(30, 600);

        var button6 = new TextButton(null, skin, "default");
        button6.setVisible(false);
        button6.setSize(80, 30);
        button6.setPosition(40, 535);

        TextButton zoomText;
        zoomText = new TextButton("Zoom", textButtonStyle);
        zoomText.setSize(150, 80);
        zoomText.setPosition(1000, 615);

        zoomSlider = new Slider(0f, 1f, 0.001f, false, skin);
        zoomSlider.setPosition(1105, 645);
        zoomSlider.setValue(appState.zoom);
        zoomSlider.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                _appState.zoom = zoomSlider.getValue();
            }
        });

        stage.addActor(button1);
        stage.addActor(button2);
        stage.addActor(speedbutton);
        stage.addActor(statusBarText);
        stage.addActor(zoomText);
        stage.addActor(zoomSlider);
        stage.addActor(fileSelect);
        stage.addActor(editSelect);
        stage.addActor(viewSelect);
        stage.addActor(inspectSelect);

        _sideBar = new Table();
        _rebuildSideBar();
        _sideBarPane = new ScrollPane(_sideBar, skin, "default");
        _sideBarPane.setPosition(10, 50);
        _sideBarPane.setSize(200, 600);
        stage.addActor(_sideBarPane);

        appState.whenChanged.add((cbsChanged) ->
        {
            if (cbsChanged)
                _rebuildSideBar();
        });
    }

    public void setStatusBarText(String text) 
    {
        statusBarText.setText(text);
    }

    private void _rebuildSideBar() 
    {
        _sideBar.clear();

        if (_appState.currentCelestialBodyId != null) {
            // Add a button for the barycenter.
            _sideBar.top().row().expandX().fillX();
            var barycenter = new TextButton("Barycenter", skin, "default");
            barycenter.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    // Dialog centerDialog = new Dialog("What is Barycenter?", skin);
                    // centerDialog.setMovable(true);
                    // centerDialog.setResizable(true);
                    // centerDialog.setPosition(450, 500);
                    // centerDialog.setWidth(500);
                    // centerDialog.setHeight(120);
                    // centerDialog.text("The center of mass of two or more bodies that orbit one another \n and it is the point about which the bodies orbit.");
                    // centerDialog.button("Close", true);
                    // _stage.addActor(centerDialog);
                    _appState.viewingMode = ViewingMode.CenterOfMass;
                    _appState.invokeChangeListeners(false);
                }
            });
            _sideBar.add(barycenter);

            // Add a button for the actual CB.
            var body = _appState.getCurrentCelestialBody();
            _sideBar.top().row().expandX().fillX();
            var cbButton = new TextButton(body.name, skin, "default");
            cbButton.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    _appState.viewingMode = ViewingMode.MainCelestialBody;
                    _appState.invokeChangeListeners(false);
                }
            });
            _sideBar.add(cbButton);

            // Add buttons for the natural satellites.
            for (int i = 0; i < body.satellites.size(); i++) {
                var satellite = _appState.bodies.get(
                    body.satellites.get(i)
                );

                _sideBar.top().row().expandX().fillX();
                var satelliteButton = new TextButton(satellite.name, skin, "default");

                final var _i = i;
                satelliteButton.addListener(new ChangeListener() {
                    public void changed(ChangeEvent event, Actor actor) {
                        _appState.viewingMode = ViewingMode.NaturalSatellite;
                        _appState.satelliteFocusedIndex = _i;
                        _appState.invokeChangeListeners(false);
                    }
                });

                _sideBar.add(satelliteButton);
            }
        }

        // for (var body : _appState) {
        //     AppState.CelestialBody nextCB = 
        //         _appState.bodies.get(_appState.bodies.keySet().toArray()[i]);
        //     AppState.CelestialBody nextSat = 
        //         _appState.bodies.get(_appState.bodies.keySet().toArray()[i]);
        //     var newButton = new TextButton(nextCB.name, skin);
        //     newButton.setSize(110, 30);
        //     _sideBar.add(newButton);
        // }
    }

    public int getSpeedFactor() 
    {
        try 
        {
            return Integer.parseInt(speedbutton.getText());
        } 
        catch (NumberFormatException e) 
        {
            return 1;
        }
    }

    public float getZoom() 
    {
        return zoomSlider.getValue();
    }
}

