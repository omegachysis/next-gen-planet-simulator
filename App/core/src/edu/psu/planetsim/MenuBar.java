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

public class MenuBar extends ApplicationAdapter {

    private Stage mainTable;
    private Skin skin;

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        mainTable = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(mainTable);

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

        mainTable.addActor(fileSelect);
        mainTable.addActor(editSelect);
        mainTable.addActor(viewSelect);
        mainTable.addActor(inspecSelect);
    }

    @Override
    public void render () {
        mainTable.act(Gdx.graphics.getDeltaTime());
        mainTable.draw();
    }

    @Override
    public void dispose () {
        mainTable.dispose();
    }
}