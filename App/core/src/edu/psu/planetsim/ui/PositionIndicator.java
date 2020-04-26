package edu.psu.planetsim.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import edu.psu.planetsim.physics.CelestialSim;

public class PositionIndicator 
{
    private final Image _image;

    PositionIndicator(Stage stage, CelestialSim sim, Vector3 position)
    {
        _image = new Image(
            new Texture(Gdx.files.internal("indicator.png"))
        );
        stage.addActor(_image);
    }

    public void dispose()
    {
        _image.remove();
    }
}
