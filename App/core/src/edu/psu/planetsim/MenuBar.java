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
                        break;
                    case "Save As":
                        Dialog saveDialog = new Dialog("Save as", skin);
                        saveDialog.setMovable(true);
                        saveDialog.setResizable(true);
                        saveDialog.setPosition(500, 500);
                        saveDialog.setWidth(250);
                        saveDialog.text("Name your file:");
//                        TextField savetext = new TextField("", skin);
//                        savetext.setPosition(100, 200);
//                        saveDialog.add(savetext);
                        saveDialog.button("Save", true);
                        saveDialog.button("Cancel", true);
                        stage.addActor(saveDialog);
                        break;
                    case "Save":
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
                editSelect.getSelected();
            }
        });

        final SelectBox<String> viewSelect= new SelectBox<>(skin);
        viewSelect.setItems("View", "Show Side Panel", "Change Celestial Body");
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
                        CheckBox sidepanel = new CheckBox("Show Side Panel?", skin);
                        Dialog sideDialog = new Dialog("Side panel", skin);
                        sideDialog.setMovable(true);
                        sideDialog.setResizable(true);
                        sideDialog.setPosition(500, 500);
                        sideDialog.setWidth(250);
                        //               sideDialog.add(sidepanel);
                        sideDialog.button("Close", true);
                        stage.addActor(sideDialog);
                        break;
                    case "Change Celestial Body":
                        break;
                }
            }
        });

        final SelectBox<String> inspecSelect= new SelectBox<>(skin);
        inspecSelect.setItems("Inspection Mode", "Climate", "Thermodynamics", "Magnetism");
        inspecSelect.setPosition(260, 680);
        inspecSelect.setWidth(150);
        inspecSelect.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                inspecSelect.showList();
//                Dialog dialog = new Dialog("Inspection Mode ", skin);
//                dialog.setPosition(500, 500);
//                dialog.setWidth(250);
//                dialog.text("Discription will come here...");
//                dialog.button("Close", true);
//                stage.addActor(dialog);

            }
        });

        inspecSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String currentSelection = inspecSelect.getSelected();

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
        stage.addActor(inspecSelect);
    }
}