package edu.psu.planetsim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class PlanetSim extends ApplicationAdapter {
//	SpriteBatch batch;
//	Texture img;
	Mesh testMesh;
	ShaderProgram testShader;

	@Override
	public void create () {
//		batch = new SpriteBatch();
		String vertexShader =
				"attribute vec4 a_position;    \n" +
                "void main()                  \n" + 
                "{                            \n" +
                "   gl_Position =  a_position;  \n"      +
                "}                            \n" ;

		testShader = new ShaderProgram(Gdx.files.getFileHandle("vert.vert", Files.FileType.Internal),
				Gdx.files.getFileHandle("frag.frag", Files.FileType.Internal));

		if (testMesh == null) {
			testMesh = new Mesh(true, 3, 3, new VertexAttribute(Usage.Position, 3, "a_position"));		

			testMesh.setVertices(new float[] { -0.2f, -0.7f, 0,
			                               0.9f, -0.3f, 0,
			                               0, 0.1f, 0 });	
			testMesh.setIndices(new short[] { 0, 1, 2 });			
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.6f, 0, 0.6f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		testMesh.render(testShader, GL20.GL_TRIANGLES, 0, 3);
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();
	}
	
	@Override
	public void dispose () {
//		batch.dispose();
//		img.dispose();
	}
}
