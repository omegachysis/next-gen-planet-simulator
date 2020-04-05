package edu.psu.planetsim.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    private TextButton inputButton;
    private TextButton.TextButtonStyle textButtonStyle;
    private Slider zoomSlider;
    BitmapFont font;

    public AppUi(final Stage stage, AppState appState) {

        _appState = appState;
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        //inputManager = new InputMethodManager();
        //Gdx.input.setInputProcessor(inputManager);

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
                        newDialog.text("Create a new celestial body? \n You'll lose all unsaved changes." );

                        Table table2 = new Table(skin);
                        table2.add(newDialog);

                        var newButton = new TextButton("Yes", skin);
                        newButton.setPosition(90, 3);
                        newButton.addListener(e -> {
                            if (newButton.isPressed()) {
                                Dialog New = new Dialog("", skin);
                                New.setMovable(true);
                                New.text("Assume this as new CB for now");
                                New.setPosition(500, 300);
                                New.setWidth(250);
                                New.button("Ok", true);
                                newDialog.hide();
                                stage.addActor(New);
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
                                 //saved.button("Ok", true);

                                 /*MoveToAction savedAction = new MoveToAction();
                                 savedAction.setPosition(300f, 100f);
                                 savedAction.setDuration(2f);
                                 saved.addAction(savedAction);
                                  */

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

                        // saveDialog.button("Cancel", true);

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
        editSelect.setItems("Edit", "Add Celestial Body", "Delete Celestial Body", "Duplicate");
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
                        Dialog addDialog = new Dialog("Add Celestial Body", skin);
                        addDialog.setMovable(true);
                        addDialog.setPosition(10, 250);
                        addDialog.setWidth(330);
                        addDialog.setHeight(300);

                        Label addLabel1 = new Label("Name:", skin);
                        addLabel1.setPosition(5,240);
                        final TextField textField1 = new TextField("",skin);
                        textField1.setPosition(160,240);
                        textField1.addListener(new ChangeListener() {
                            @Override
                            public void changed(ChangeEvent event, Actor actor) {
                                 textField1.getText();
                                //String test = textField1.getText();
                                //System.out.println(test);
                                button4 = new TextButton(textField1.getText(), skin);
                                button4.setSize(110, 30);
                                button4.setPosition(30, 635);

                            }
                        });

                        Label addLabel2 = new Label("Radius (km):", skin);
                        addLabel2.setPosition(5,195);
                        TextField textField2 = new TextField("",skin);
                        textField2.setPosition(160,195);

                        Label addLabel3 = new Label("Initial Distance (AU):", skin);
                        addLabel3.setPosition(5,150);
                        TextField textField3 = new TextField("",skin);
                        textField3.setPosition(160,150);

                        Label addLabel4 = new Label("Velocity (km/s):", skin);
                        addLabel4.setPosition(5,105);
                        TextField textField4 = new TextField("",skin);
                        textField4.setPosition(160,105);

                        Label addLabel5 = new Label("Mass (M⊕):",skin);
                        addLabel5.setPosition(5, 60);
                        TextField textField5 = new TextField("",skin);
                        textField5.setPosition(160,60);


                        TextButton addButton = new TextButton("Add", skin);
                        addButton.setPosition(115, 10);
                        addButton.addListener(e -> {
                            if (addButton.isPressed()) {
                                Dialog wrong = new Dialog("Incorrect entry", skin);
                                wrong.text("One or more of your values is invalid. \n Please try again.");
                                wrong.setPosition(500, 500);
                                wrong.setWidth(400);
                                wrong.setHeight(100);

                                try {
                                    String name = textField1.getText();
                                } catch (NullPointerException n){
                                    stage.addActor(wrong);
                                }

                                try {
                                    double radius = Double.parseDouble(textField2.getText());
                                    double distance = Double.parseDouble(textField3.getText());
                                    double velocity = Double.parseDouble(textField4.getText());
                                    double mass = Double.parseDouble(textField5.getText());
                                } catch (NumberFormatException n) {
                                        stage.addActor(wrong);
                                    Timer.schedule(new Timer.Task() {
                                        @Override
                                        public void run() {
                                            wrong.hide();
                                        }
                                    }, 1);
                                    return true;
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
                                stage.addActor(button4);
                                stage.addActor(success);
                                addDialog.hide();
                            }
                            return true;
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
                        table.add(textField1);
                        table.add(addLabel2);
                        table.add(textField2);
                        table.add(addLabel3);
                        table.add(textField3);
                        table.add(addLabel4);
                        table.add(textField4);
                        table.add(addLabel5);
                        table.add(textField5);
                        table.add(addButton);
                        table.add(cancelButton);

//                        addDialog.button("Add", true);
//                        addDialog.button("Cancel", true);

                        addDialog.addActor(addLabel1);
                        addDialog.addActor(textField1);
                        addDialog.addActor(addLabel2);
                        addDialog.addActor(textField2);
                        addDialog.addActor(addLabel3);
                        addDialog.addActor(textField3);
                        addDialog.addActor(addLabel4);
                        addDialog.addActor(textField4);
                        addDialog.addActor(addLabel5);
                        addDialog.addActor(textField5);
                        addDialog.addActor(addButton);
                        addDialog.addActor(cancelButton);
                        stage.addActor(addDialog);
                        editSelect.setSelectedIndex(0);
                        break;

                    case "Delete Celestial Body":
                        Dialog deleteDialog = new Dialog("Delete Celestial Body", skin);
                        deleteDialog.setResizable(true);
                        deleteDialog.setMovable(true);
                        deleteDialog.setPosition(500, 500);
                        deleteDialog.setWidth(310);
                        deleteDialog.text("Are you sure to delete a celestial body? \n Remember: You'll lose this celestial body.");

                        var deleteButton = new TextButton("Yes", skin);
                        deleteButton.setPosition(110, 3);
                        deleteButton.addListener(e -> {
                            if (deleteButton.isPressed()) {
                                button4.remove();
                                deleteDialog.hide();
                            }
                            return true;
                        });

                        var NoButton = new TextButton("No", skin);
                        NoButton.setPosition(160, 3);
                        NoButton.addListener(e -> {
                            if (NoButton.isPressed()) {
                                deleteDialog.hide();
                            }
                            return true;
                        });

                        Table table3 = new Table(skin);
                        table3.add(deleteButton);
                        table3.add(NoButton);

                        deleteDialog.addActor(deleteButton);
                        deleteDialog.addActor(NoButton);
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
                        Dialog sideDialog = new Dialog("Side panel", skin);
                        sideDialog.setMovable(true);
                        sideDialog.setResizable(true);
                        sideDialog.setPosition(10, 250);
                        sideDialog.setWidth(250);
                        sideDialog.setHeight(350);
                        sideDialog.button("Done", true);
                        sideDialog.button("Close", true);

                        sideDialog.row();
                        Label nameLabel = new Label("Select Physical Properties:", skin);
                        nameLabel.setPosition(5, 300);
                        CheckBox check1 = new CheckBox("Initial distance from star(AU)", skin);
                        check1.setPosition(10, 280);
                        CheckBox check2 = new CheckBox("Initial velocity(km/s)", skin);
                        check2.setPosition(10, 260);
                        CheckBox check3 = new CheckBox("PhysicalProperty-3", skin);
                        check3.setPosition(10, 240);

                        Table table = new Table(skin);
                        table.add(nameLabel);
                        table.add(check1);
                        table.add(check2);
                        table.add(check3);

                        sideDialog.addActor(check1);
                        sideDialog.addActor(check2);
                        sideDialog.addActor(check3);
                        sideDialog.addActor(table);
                        sideDialog.addActor(nameLabel);
                        stage.addActor(sideDialog);
                        viewSelect.setSelectedIndex(0);

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
                                    changeDialog.hide();
                                    Dialog error = new Dialog ("Invalid entry", skin);
                                    error.setPosition(500, 500);
                                    error.setHeight(100);
                                    error.text("Invalid Selection \n Please try again.");
                                    stage.addActor(error);
                                    Timer.schedule(new Timer.Task() {
                                        @Override
                                        public void run() {
                                            error.hide();
                                        }
                                    }, 1);
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
                                Map.Entry<UUID, AppState.CelestialBody> entries = _appState.bodies.entrySet().iterator().next();
                                AppState.CelestialBody newPlanet = entries.getValue();
                                changeSelect.setItems("Select", newPlanet.name);
                                changeSelect.showList();
                            }
                        });

//                        var changeButton = new TextButton("Change", skin);
//                                changeButton.setPosition(60, 3);
//                                changeButton.addListener(e -> {
//                                    if (changeButton.isPressed()) {
//                                Dialog newCB = new Dialog("New Celestial Body", skin);
//                                newCB.setMovable(true);
//                                newCB.text("Assume this as New CB for now...");
//                                newCB.setPosition(500, 300);
//                                newCB.setWidth(250);
//                                newCB.button("Ok", true);
//                                stage.addActor(newCB);
//                                changeDialog.hide();
//                            }
//                            return true;
//                        });

//                        Table table1 = new Table(skin);
//                        table1.add(changeButton);
//                        changeDialog.addActor(changeButton);
//
//                        var cancelButton = new TextButton("Cancel", skin);
//                        cancelButton.setPosition(140, 3);
//                        cancelButton.addListener(e -> {
//                            if (cancelButton.isPressed()) {
//                                changeDialog.hide();
//                            }
//                            return true;
//                        });
//
//                        table1.add(cancelButton);
//                        changeDialog.addActor(cancelButton);

                 //   {
                 //       if (Gdx.input.isTouched()) {
                 //           Dialog newCB = new Dialog("CB", skin);
                 //           newCB.setMovable(true);
                 //           newCB.setPosition(500, 500);
                 //           stage.addActor(newCB);
                 //       }
                 //   }

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
        // TODO: play pause in app state
        //  e.g. appState.paused = !appState.paused
        button1.addListener(e ->{
            appState.paused = !appState.paused;
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
        button5.setSize(80, 30);
        button5.setPosition(40, 600);
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
        stage.addActor(button4);
        stage.addActor(button5);
        stage.addActor(zoomText);
        stage.addActor(zoomSlider);
        stage.addActor(fileSelect);
        stage.addActor(editSelect);
        stage.addActor(viewSelect);
        stage.addActor(inspectSelect);
    }

//    public String getName(){
//        Map.Entry<UUID, AppState.CelestialBody> entries = _appState.bodies.entrySet().iterator().next();
//        UUID key = entries.getKey();
//        AppState.CelestialBody newPlanet = entries.getValue();
//        return newPlanet.name;
//        var celBodies = _appState.bodies.values().toArray();
//        return celBodies
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