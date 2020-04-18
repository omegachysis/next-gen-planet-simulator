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

import java.util.Map;
import java.util.UUID;

public class AppUi {
    private final AppState _appState;
    private final Skin skin;

    private TextButton button1;
    private TextButton button2;
    private TextField speedbutton;
    private TextButton button4;
    private TextButton button5;
    private TextButton button7;
    private TextButton inputButton;
    private TextButton.TextButtonStyle textButtonStyle;
    private Slider zoomSlider;
    private TextButton center_button;
    BitmapFont font;

    public AppUi(final Stage stage, AppState appState) {

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
                        //save.button("Ok",true);
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
                System.out.println(_appState.bodies.size());
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

        inputButton = new TextButton("Input text will be displayed here...", textButtonStyle);
        inputButton.setSize(150, 60);
        inputButton.setPosition(50, 0);
        inputButton.setText(appState.inputtext);
        inputButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                _appState.inputtext = inputButton.toString();
            }
         });


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


//        Map.Entry<UUID, AppState.CelestialBody> entries = _appState.bodies.entrySet().iterator().next();
//        UUID key = entries.getKey();
//        AppState.CelestialBody newPlanet = entries.getValue();


        button4 = new TextButton("Add Planet", skin, "default");
        button4.setSize(110, 30);
        button4.setPosition(30, 635);
        button4.addListener(e -> {
            if (button4.isPressed()){
                var currentBody = _appState.bodies.get(_appState.bodies.keySet().toArray()[0]);
                button4.setText(currentBody.name);
            }
            return true;
        });

        button5 = new TextButton("Add Satellite", skin, "default");
        button5.setSize(110, 30);
        button5.setPosition(30, 600);
        button5.addListener(e -> {
            if (_appState.bodies.size() == 2){
                button5.isVisible();
            }
            if (button5.isPressed()){
                var currentBody = _appState.bodies.get(_appState.bodies.keySet().toArray()[1]);
                button5.setText(currentBody.name);
            }
            return true;
        });

        var button6 = new TextButton(null, skin, "default");
        button6.setVisible(false);
        button6.setSize(80, 30);
        button6.setPosition(40, 535);
        button6.addListener(e ->
        {
            if (_appState.bodies.size() == 3){
                button6.isVisible();
            }
           return true;
        });

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
        stage.addActor(inputButton);
        stage.addActor(zoomText);
        stage.addActor(zoomSlider);
        stage.addActor(fileSelect);
        stage.addActor(editSelect);
        stage.addActor(viewSelect);
        stage.addActor(inspectSelect);

        center_button = new TextButton("Barycenter", skin, "default");
        center_button.setSize(90, 35);
        center_button.setPosition(25, 640);
        center_button.addListener(new TextTooltip("What is Barycenter? \n" + "The center of mass of two or more bodies that orbit one another and it is the point about which the bodies orbit.", skin));
        center_button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                //Dialog centerDialog = new Dialog("What is Barycenter?", skin);
                //centerDialog.setMovable(true);
                //centerDialog.setResizable(true);
                //centerDialog.setPosition(450, 500);
                //centerDialog.setWidth(500);
                //centerDialog.setHeight(120);
                //centerDialog.text("The center of mass of two or more bodies that orbit one another \n " +
                       // "and it is the point about which the bodies orbit.");
                //centerDialog.button("Close", true);
                //stage.addActor(centerDialog);
            }
        });
        stage.addActor(center_button);
    }

    private Table cbTable(){
        Table buttonTable = new Table();
        buttonTable.setPosition(110, 635);
        buttonTable.setSize(300, 300);
        for (int i = 0; i < _appState.bodies.size(); i++){
            AppState.CelestialBody nextCB = _appState.bodies.get(_appState.bodies.keySet().toArray()[i]);
            AppState.CelestialBody nextSat = _appState.bodies.get(_appState.bodies.keySet().toArray()[i]);
            var newButton = new TextButton(nextCB.name, skin);
            newButton.setSize(110, 30);
            buttonTable.add(newButton);
        }
        return buttonTable;
    }



//    private void addPlanet(){
//         final var planet1 = new AppState.CelestialBody();
//         planet1.id = UUID.randomUUID();
//         planet1.name = "Planet 1";
//         planet1.mass = Metrics.kg(1.0e24);
//         planet1.position = new Vector3(); // Only set the position for satellites,
//         // we will interpret position for planets as referring to the
//         // 'positionRelativeToSun' field down lower. For planets that get added,
//         // this should always initialize to new Vector3().
//         planet1.velocity = new Vector3(); // same for velocity, we'll set it relative
//         // to the sun for planets.
//         planet1.spin = new Vector3(0, 0, -7.292115e-5f).rotate(Vector3.Y, 23.5f);
//         planet1.orientation = new Quaternion().setFromCross(Vector3.Y, planet1.spin);
//         planet1.positionRelativeToSun = new Vector3(Metrics.m(1.496e11), 0, 0); // 1 AU
//         planet1.velocityRelativeToSun = new Vector3();
//         _appState.bodies.put(planet1.id, planet1);
//         _appState.currentCelestialBodyId = planet1.id;
//    }

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

    public String getText()
    {
        return inputButton.toString();

    }
}

