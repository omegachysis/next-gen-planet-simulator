package edu.psu.planetsim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PlanetSim extends ApplicationAdapter {
	Mesh testMesh;
	ShaderProgram testShader;
	MenuBar menuBar;
	Stage stage;
	//SideBar sideBar;
	//SelectBox selectBox;
	//SelectBox.SelectBoxStyle style;
    BitmapFont font;
	Skin skin;
	CelestialRenderer _cRenderer;

	public static ShaderProgram loadShader(String name) {
		ShaderProgram res = new ShaderProgram(
			Gdx.files.getFileHandle(name + ".vert", Files.FileType.Internal),
			Gdx.files.getFileHandle(name + ".frag", Files.FileType.Internal));
		if (!res.isCompiled())
			throw new GdxRuntimeException(res.getLog());
		System.out.print(res.getLog());
		return res;
	}

	@Override
	public void create () {
		// testShader = loadShader("ranibow_trangle");

		// if (testMesh == null) {
		// 	testMesh = new Mesh(
		// 			true, 3, 3,
		// 			new VertexAttribute(Usage.Position, 2, "a_position"),
		// 			new VertexAttribute(Usage.ColorUnpacked, 3, "a_color"));

		// 	testMesh.setVertices(new float[] { -0.2f, -0.7f,   1f, 0f, 0f,
		// 	                               0.9f, -0.3f,   0f, 1f, 0f,
		// 	                               0, 0.1f,   0f, 0f, 1f});
		// 	testMesh.setIndices(new short[] { 0, 1, 2 });			
		// }
		
		font = new BitmapFont();
        skin = new Skin();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        menuBar = new MenuBar(stage);

        //sideBar = new SideBar(stage);
        //selectBox = new SelectBox(style);
		//selectBox = new SelectBox(style);
		
		_cRenderer = new CelestialRenderer();


	}

	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		// testShader.begin();
		// testMesh.render(testShader, GL20.GL_TRIANGLES, 0, 3);
		// testShader.end();

		_cRenderer.render();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
	}
	
	@Override
	public void dispose () {
		stage.dispose();


		_cRenderer.dispose();

    }
}
