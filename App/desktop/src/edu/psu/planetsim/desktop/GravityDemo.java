package edu.psu.planetsim.desktop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.VertexAttributes.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.*;
import com.badlogic.gdx.graphics.g3d.utils.*;

public class GravityDemo extends ApplicationAdapter {
    private PerspectiveCamera _cam;
    private ModelBatch _modelBatch;
    private Model _model;
    private ModelInstance _instance;

    public void create() {
        _modelBatch = new ModelBatch();

        _cam = new PerspectiveCamera(70, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        _cam.position.set(10f, 10f, 10f);
        _cam.lookAt(0,0,0);
        _cam.near = 1f;
        _cam.far = 300f;
        _cam.update();
        _cam.update();

        ModelBuilder builder = new ModelBuilder();
        _model = builder.createSphere(1.0f, 1.0f, 1.0f, 10, 10, 
            new Material(ColorAttribute.createDiffuse(Color.GREEN)), 
            Usage.Position | Usage.Normal);
        _instance = new ModelInstance(_model);
    }

    public void render() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        _modelBatch.begin(_cam);
        _modelBatch.render(_instance);
        _modelBatch.end();
    }

    public void dispose() {
        _modelBatch.dispose();
        _model.dispose();
    }
}