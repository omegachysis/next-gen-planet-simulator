package edu.psu.planetsim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.awt.*;

public class MenuBar {

    private Skin skin;
    private TextButton button1;
    private TextButton button2;
    private TextButton button3;
    public TextButton.TextButtonStyle textButtonStyle;
    BitmapFont font;

    public MenuBar(Stage stage) {

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        final SelectBox<String> fileSelect= new SelectBox<>(skin);
        fileSelect.setItems("File", "New", "Save As", "Save");
        fileSelect.setPosition(20, 680);
        fileSelect.setWidth(75);
        fileSelect.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                fileSelect.showList();
            }
        });
        fileSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                fileSelect.getSelected();
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
                viewSelect.getSelected();
            }
        });

        final SelectBox<String> inspecSelect= new SelectBox<>(skin);
        inspecSelect.setItems("Inspection Mode", "Climate", "Thermodynamics", "Magnetism");
        // First one is the title that'll pop up
        // There's more that'll be here but the click doesn't listen so does it ,,, matter yet
        inspecSelect.setPosition(260, 680);
        inspecSelect.setWidth(150);
        inspecSelect.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                inspecSelect.showList();
            }
        });
        inspecSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                inspecSelect.getSelected();
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

        stage.addActor(button1);
        stage.addActor(button2);
        stage.addActor(button3);
        stage.addActor(fileSelect);
        stage.addActor(editSelect);
        stage.addActor(viewSelect);
        stage.addActor(inspecSelect);
    }
}