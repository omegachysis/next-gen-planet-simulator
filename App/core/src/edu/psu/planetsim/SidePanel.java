package edu.psu.planetsim;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Gdx;

import java.awt.*;

public class SidePanel {

    public Stage mTable;
    public Table mItems;
    public Menu currentMenu;
    public Array<Menu> menus = new Array<Menu>();

    public void create () {

        mTable = new Stage();
        Gdx.input.setInputProcessor(mTable);

        mItems = new Table();
        mItems.setSize(300, 650);
        mItems.setPosition(16, 25);
        mTable.addActor(mItems);
        mItems.setDebug(true);
    }


    public void render() {
        mTable.act(Gdx.graphics.getDeltaTime());
        mTable.draw();
    }

    public void dispose() {
        mTable.dispose();
    }
}
