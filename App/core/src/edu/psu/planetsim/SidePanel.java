package edu.psu.planetsim;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Gdx;

import java.awt.*;

public class SidePanel {

    public Table mItems;

    public SidePanel(Stage stage) {

        mItems = new Table();
        mItems.setSize(300, 650);
        mItems.setPosition(16, 25);
        stage.addActor(mItems);
        mItems.setDebug(true);
    }

}