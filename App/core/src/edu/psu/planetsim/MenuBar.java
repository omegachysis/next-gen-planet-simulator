package edu.psu.planetsim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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

        final TextButton button1 = new TextButton("File", skin, "default");
        button1.setWidth(100);
        button1.setHeight(20);
        button1.setPosition(20,680);
        mainTable.addActor(button1);

        button1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                button1.setText("File Clicked");
            }
        });

        final TextButton button2 = new TextButton("Edit", skin, "default");
        button2.setWidth(100);
        button2.setHeight(20);
        button2.setPosition(120,680);
        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                button2.setText("Edit Clicked");
            }
        });

        final TextButton button3 = new TextButton("View", skin, "default");
        button3.setWidth(100);
        button3.setHeight(20);
        button3.setPosition(220,680);
        button3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                button3.setText("File Clicked");
            }
        });

        mainTable.addActor(button1);
        mainTable.addActor(button2);
        mainTable.addActor(button3);

        Gdx.input.setInputProcessor(mainTable);
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