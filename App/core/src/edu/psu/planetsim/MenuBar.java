package edu.psu.planetsim;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
//import com.badlogic.gdx.scenes.scene2d.ui.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Gdx;

import java.awt.*;

public class MenuBar {

    public Stage mainTable;
    public Table menuItems;
    public Menu currentMenu;
    public Array<Menu> menus = new Array<Menu>();
    public MenuBarListener menuListener;

    public TextButton button1;
    public TextButton button2;
    public TextButton button3;
    public TextButton button4;

    public TextButton.TextButtonStyle textButtonStyle;
    BitmapFont font;

    public void create () {

        mainTable = new Stage();
        Gdx.input.setInputProcessor(mainTable);

        menuItems = new Table();
        menuItems.setSize(1200, 25);
        menuItems.setPosition(16, 675);
        mainTable.addActor(menuItems);
        menuItems.setDebug(true);

        font = new BitmapFont();
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        button1 = new TextButton("File", textButtonStyle);
        button2 = new TextButton("Edit", textButtonStyle);
        button3 = new TextButton("View", textButtonStyle);
        button4 = new TextButton("Tools", textButtonStyle);
        mainTable.addActor(button1);
        mainTable.addActor(button2);
        mainTable.addActor(button3);
        mainTable.addActor(button4);


        button1.setSize(200, 20);
        button1.setPosition(-50, 675);

        button2.setSize(200, 20);
        button2.setPosition(25, 675);

        button3.setSize(200, 20);
        button3.setPosition(100, 675);

        button4.setSize(200, 20);
        button4.setPosition(175, 675);

    }

    private void setMenuListener(MenuBarListener menuListener) {
        this.menuListener = menuListener;
    }

    public Stage getTable () {
        return mainTable;
    }

    public static class MenuBarStyle {
        public Drawable background;

        public MenuBarStyle () {
        }

        public MenuBarStyle (MenuBarStyle style) {
            this.background = style.background;
        }

        public MenuBarStyle (Drawable background) {
            this.background = background;
        }
    }

    public interface MenuBarListener {
        void menuOpened (Menu menu);

        void menuClosed (Menu menu);
    }

    public void render() {
        mainTable.act(Gdx.graphics.getDeltaTime());
        mainTable.draw();

    }

    public void dispose() {

        mainTable.dispose();
    }
}
