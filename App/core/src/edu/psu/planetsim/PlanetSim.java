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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class PlanetSim extends ApplicationAdapter {
	Mesh testMesh;
	ShaderProgram testShader;
	MenuBar menuBar;
	SidePanel sidePanel;

	Stage stage;
    TextButton button;
    com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle textButtonStyle;
    BitmapFont font;
    Skin skin;
    TextureAtlas buttonAtlas;

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
		testShader = loadShader("ranibow_trangle");

		if (testMesh == null) {
			testMesh = new Mesh(
					true, 3, 3,
					new VertexAttribute(Usage.Position, 2, "a_position"),
					new VertexAttribute(Usage.ColorUnpacked, 3, "a_color"));

			testMesh.setVertices(new float[] { -0.2f, -0.7f,   1f, 0f, 0f,
			                               0.9f, -0.3f,   0f, 1f, 0f,
			                               0, 0.1f,   0f, 0f, 1f});
			testMesh.setIndices(new short[] { 0, 1, 2 });			
		}

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		font = new BitmapFont();
        skin = new Skin();
        //buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.pack"));
        //skin.addRegions(buttonAtlas);
        // textButtonStyle = new com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle();
        // textButtonStyle.font = font;
        // textButtonStyle.up = skin.getDrawable("up-button");
        // textButtonStyle.down = skin.getDrawable("down-button");
		// textButtonStyle.checked = skin.getDrawable("checked-button");
		skin = new Skin(Gdx.files.internal("uiskin.json"));
        button = new TextButton("Button1", skin);
		stage.addActor(button);
		
		button.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				System.out.println("Button Pressed");
			}
		});

		//Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
		//Gdx.gl.glDisable(GL20.GL_CULL_FACE);

		menuBar = new MenuBar();
		menuBar.create();

		// sidePanel = new SidePanel();
		// sidePanel.create();

	}

	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// testShader.begin();
		// testMesh.render(testShader, GL20.GL_TRIANGLES, 0, 3);
		// testShader.end();
		menuBar.render();
		// sidePanel.render();
		// stage.act(Gdx.graphics.getDeltaTime());
		// stage.draw();
	}
	
	@Override
	public void dispose () {
		// menuBar.dispose();
		// sidePanel.dispose();
		stage.dispose();
	}
}
