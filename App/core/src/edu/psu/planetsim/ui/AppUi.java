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
                        break;

                    case "Save As":
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
                        Dialog addDialog = new Dialog("Add Celestial Body", skin);
                        addDialog.setMovable(true);
                        addDialog.setPosition(10, 200);
                        addDialog.setWidth(330);
                        addDialog.setHeight(400);

                        Label addLabel1 = new Label("Name:", skin);
                        addLabel1.setPosition(5,340);
                        final TextField nameField = new TextField("",skin);
                        nameField.setPosition(160,340);

                        Label addLabel2 = new Label("Mass (kg):", skin);
                        addLabel2.setPosition(5,295);
                        TextField massField = new TextField("",skin);
                        massField.setPosition(160,295);

                        Label addLabel3 = new Label("Position (m):", skin);
                        addLabel3.setPosition(5,250);
                        TextField positionField = new TextField("",skin);
                        positionField.setPosition(160,250);

                        Label addLabel4 = new Label("Velocity (m/s):", skin);
                        addLabel4.setPosition(5,205);
                        TextField velocityField = new TextField("",skin);
                        velocityField.setPosition(160,205);

                        Label addLabel5 = new Label("Spin (x, y, z):",skin);
                        addLabel5.setPosition(5, 160);
                        TextField spinField = new TextField("",skin);
                        spinField.setPosition(160,160);

                        TextButton addButton = new TextButton("Add", skin);
                        addButton.setPosition(115, 10);
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
                                    double position = Double.parseDouble(positionField.getText());
                                    double velocity = Double.parseDouble(velocityField.getText());
                                    double spin = Double.parseDouble(spinField.getText());

                                    addCB(name, mass, position);
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
                                success.text("Your celestial body has been added.");
                                Timer.schedule(new Timer.Task() {
                                    @Override
                                    public void run() {
                                        success.hide();
                                    }
                                }, 2);

                                stage.addActor(success);
                                addDialog.hide();
                            }
                        });

                        TextButton cancelButton = new TextButton("Cancel", skin);
                        cancelButton.setPosition(160, 10);
                        cancelButton.addListener(e -> {
                            if (cancelButton.isPressed()) {
                                addDialog.hide();
                            }
                            return true;
                        });

                        Table table = new Table(skin);
                        table.add(addLabel1);
                        table.add(nameField);
                        table.add(addLabel2);
                        table.add(massField);
                        table.add(addLabel3);
                        table.add(positionField);
                        table.add(addLabel4);
                        table.add(velocityField);
                        table.add(addLabel5);
                        table.add(spinField);
                        table.add(addButton);
                        table.add(cancelButton);

                        addDialog.addActor(addLabel1);
                        addDialog.addActor(nameField);
                        addDialog.addActor(addLabel2);
                        addDialog.addActor(massField);
                        addDialog.addActor(addLabel3);
                        addDialog.addActor(positionField);
                        addDialog.addActor(addLabel4);
                        addDialog.addActor(velocityField);
                        addDialog.addActor(addLabel5);
                        addDialog.addActor(spinField);
                        addDialog.addActor(addButton);
                        addDialog.addActor(cancelButton);
                        stage.addActor(addDialog);
                        editSelect.setSelectedIndex(0);
                        break;

                    case "Add Satellite":
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

                                    addSat(name, mass, position);
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
                        break;

                    case "Delete Celestial Body":
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
                                    Map.Entry<UUID, AppState.CelestialBody> entries = _appState.bodies.entrySet().iterator().next();
                                    AppState.CelestialBody newPlanet = entries.getValue();
                                    deleteSelect.setItems("Select", newPlanet.name);
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
                                   // deleteDialog.hide();
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
                                }else{

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
                                }
                            }
                            return true;
                        });
                        deleteDialog.addActor(deleteButton);

                        var closeButton = new TextButton("Close", skin);
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
        viewSelect.setItems("View","Show Side Panel", "Change Celestial Body");
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

                    case "Change Celestial Body":
                        Dialog changeDialog = new Dialog("Change Celestial Body", skin);
                        changeDialog.setMovable(true);
                        changeDialog.setResizable(true);
                        changeDialog.setPosition(500, 450);
                        changeDialog.setWidth(300);
                        changeDialog.setHeight(150);

                        var changeLabel = new Label("Change selection", skin);
                        changeLabel.setPosition(50, 85);
                        changeDialog.addActor(changeLabel);

                        var changeSelect = new SelectBox<>(skin);
                        changeSelect.setItems("Select");
                        changeSelect.setPosition(50, 50);
                        changeSelect.setWidth(200);
                        changeDialog.addActor(changeSelect);

                        var change = new TextButton("Change", skin);
                        change.setPosition(135, 3);
                        change.addListener(e -> {
                            if (change.isPressed()) {
                                var selected = changeSelect.getSelected();
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
                                    //changeDialog.hide();
                                    stage.addActor(cbTable());
                                }
                            }
                            return true;
                        });
                        changeDialog.addActor(change);

                        var close = new TextButton("Close", skin);
                        close.setPosition(80, 3);
                        close.addListener(e -> {
                            if (close.isPressed()){
                                changeDialog.hide();
                            }
                            return true;
                        });
                        changeDialog.addActor(close);

                        changeSelect.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y){
                                if (_appState.bodies.isEmpty()){
                                    changeSelect.showList();
                                }else {
                                    Map.Entry<UUID, AppState.CelestialBody> entries = _appState.bodies.entrySet().iterator().next();
                                    AppState.CelestialBody newPlanet = entries.getValue();

                                    changeSelect.setItems("Select", newPlanet.name);
                                    changeSelect.showList();
                                }
                            }
                        });

                    stage.addActor(changeDialog);
                    viewSelect.setSelectedIndex(0);
                    break;
                }
            }
        });

        final SelectBox<String> inspectSelect= new SelectBox<>(skin);
        inspectSelect.setItems("Inspection Mode", "Climate", "Thermodynamics", "Magnetism");
        inspectSelect.setPosition(260, 680);
        inspectSelect.setWidth(150);
        inspectSelect.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                inspectSelect.showList();
            }
        });

        inspectSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String currentSelection = inspectSelect.getSelected();

                switch(currentSelection){
                    case "Climate":
                        Dialog dialog = new Dialog("Inspection Mode: Climate", skin);
                        dialog.setMovable(true);
                        dialog.setResizable(true);
                        dialog.setPosition(500, 500);
                        dialog.setWidth(250);
                        dialog.text("Climate description here...");
                        dialog.button("Close", true);
                        stage.addActor(dialog);
                        inspectSelect.setSelectedIndex(0);
                        break;

                    case "Thermodynamics":
                        Dialog thermoDialog = new Dialog("Inspection Mode: Thermodynamics", skin);
                        thermoDialog.setMovable(true);
                        thermoDialog.setResizable(true);
                        thermoDialog.setPosition(500, 500);
                        thermoDialog.setWidth(250);
                        thermoDialog.text("Thermodynamics description here...");
                        thermoDialog.button("Close", true);
                        stage.addActor(thermoDialog);
                        inspectSelect.setSelectedIndex(0);
                        break;
                    case "Magnetism":
                        Dialog magDialog = new Dialog("Inspection Mode: Magnetism", skin);
                        magDialog.setMovable(true);
                        magDialog.setResizable(true);
                        magDialog.setPosition(500, 500);
                        magDialog.setWidth(250);
                        magDialog.text("Magnetism description here...");
                        magDialog.button("Close", true);
                        stage.addActor(magDialog);
                        inspectSelect.setSelectedIndex(0);
                        break;
                }
            }
        });

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


//        Table CBtable = new Table(skin);
//        CBtable.setPosition(110,635);
//        CBtable.setSize(300, 300);

//        stage.addActor(CBtable);


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

    }

    private void addCB(String name, double mass, double position) {
        final var newCB = new AppState.CelestialBody();
        newCB.id = UUID.randomUUID();
        newCB.name = name;
        newCB.mass = Metrics.kg(mass);
        newCB.position = new Vector3();
        newCB.velocity = new Vector3();
        newCB.spin = new Vector3();
        newCB.orientation = new Quaternion().setFromCross(Vector3.Y, newCB.spin);
        newCB.positionRelativeToSun = new Vector3(Metrics.m(position), 0, 0); // 1 AU
        newCB.velocityRelativeToSun = new Vector3();
        var radius = Metrics.m(1e6);
        newCB.elevationMap = TerrainBuilder.MakeRandomElevationMap(100, radius);
        _appState.bodies.put(newCB.id, newCB);
        _appState.currentCelestialBodyId = newCB.id;
        _appState.needsRefresh = true;
    }

    private void addSat(String name,double mass, double position){
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

/*         // Incase if we need checkbox :

                        sideDialog.row();
                        Label nameLabel = new Label("Select Physical Properties:", skin);
                        nameLabel.setPosition(5, 350);
                        CheckBox check1 = new CheckBox("Initial distance from star(AU)", skin);
                        check1.setPosition(10, 330);
                        CheckBox check2 = new CheckBox("Initial velocity(km/s)", skin);
                        check2.setPosition(10, 310);
                        CheckBox check3 = new CheckBox("PhysicalProperty-3", skin);
                        check3.setPosition(10, 290);

                        table.add(nameLabel);
                        table.add(check1);
                        table.add(check2);
                        table.add(check3);

                        sideDialog.addActor(check1);
                        sideDialog.addActor(check2);
                        sideDialog.addActor(check3);
                        sideDialog.addActor(nameLabel);


 */