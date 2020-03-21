package edu.psu.planetsim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;

import javax.swing.text.View;

import static com.badlogic.gdx.utils.Align.center;

public class MenuBar {

    private Stage stage;
    private Skin skin;

    private TextButton button1;
    private TextButton button2;
    private TextButton button3;
    private TextButton button4;
    private TextButton button5;
    private TextButton.TextButtonStyle textButtonStyle;
    private Dialog endDialog;
    BitmapFont font;
    Color fontColor;


    public MenuBar(final Stage stage) {

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
                        Dialog newDialog = new Dialog("New", skin);
                        newDialog.setResizable(true);
                        newDialog.setMovable(true);
                        newDialog.setPosition(500, 500);
                        newDialog.setWidth(250);
                        newDialog.text("Create a new celestial body? \n You'll lose all unsaved changes." );
                        newDialog.button("Yes", true);
                        newDialog.button("No", true);
                        stage.addActor(newDialog);
                        fileSelect.setSelectedIndex(0);
                        break;
                    case "Save As":
                        Dialog saveDialog = new Dialog("Save as", skin);
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
                        saveDialog.button("Save", true);
                        saveDialog.button("Cancel", true);

                        saveDialog.addActor(nameLabel1);
                        saveDialog.addActor(fileName1);
                        saveDialog.addActor(nameLabel2);
                        saveDialog.addActor(fileName2);
                        stage.addActor(saveDialog);
                        fileSelect.setSelectedIndex(0);
                        break;

                    case "Save":
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
                        addDialog.setResizable(true);
                        addDialog.setPosition(10, 250);
                        addDialog.setWidth(330);
                        addDialog.setHeight(300);

                        Label addLabel1 = new Label("Name:", skin);
                        addLabel1.setPosition(5,240);
                        TextField textField1 = new TextField("",skin);
                        textField1.setPosition(160,240);

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

                        addDialog.button("Add", true);
                        addDialog.button("Cancel", true);

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
                        deleteDialog.button("Yes", true);
                        deleteDialog.button("No", false);
                        stage.addActor(deleteDialog);
                        editSelect.setSelectedIndex(0);
                        break;

                    case "Duplicate":
                        editSelect.setSelectedIndex(0);
                }


            }
        });

        final SelectBox<String> viewSelect= new SelectBox<>(skin);
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
                switch (currentSelection){
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
                        nameLabel.setPosition(5,300);
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

        font = new BitmapFont();
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        button2 = new TextButton("Speed", textButtonStyle);
        button2.setSize(150, 80);
        button2.setPosition(1000, 655);

        button3 = new TextButton("100", skin, "default");
        button3.setSize(60, 35);
        button3.setPosition(1105, 680);

        button4 = new TextButton("Earth", skin, "default");
        button4.setSize(110, 30);
        button4.setPosition(30, 635);

        button5 = new TextButton("Luna", skin, "default");
        button5.setSize(80, 30);
        button5.setPosition(40, 600);

        stage.addActor(button1);
        stage.addActor(button2);
        stage.addActor(button3);
        stage.addActor(button4);
        stage.addActor(button5);
        stage.addActor(fileSelect);
        stage.addActor(editSelect);
        stage.addActor(viewSelect);
        stage.addActor(inspectSelect);
    }
}