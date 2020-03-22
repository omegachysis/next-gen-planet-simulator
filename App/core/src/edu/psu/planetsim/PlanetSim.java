package edu.psu.planetsim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;

public class PlanetSim extends ApplicationAdapter 
{
	AppState appState;
	MenuBar menuBar;
	Stage stage;
	//SideBar sideBar;
	//SelectBox selectBox;
	//SelectBox.SelectBoxStyle style;
	CelestialRenderer _cRenderer;

	public void create() 
	{
		appState = new AppState();

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		menuBar = new MenuBar(stage, appState);

		// sideBar = new SideBar(stage);
		// selectBox = new SelectBox(style);
		// selectBox = new SelectBox(style);

		_cRenderer = new CelestialRenderer(appState);
	}

	public void resize(final int width, final int height)
	{
		stage.getViewport().update(width, height, true);
	}

	public void render() 
	{
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		_cRenderer.render();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
	}
	
	public void dispose() 
	{
		stage.dispose();
		_cRenderer.dispose();
    }
}
