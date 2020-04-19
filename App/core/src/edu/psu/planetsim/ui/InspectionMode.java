package edu.psu.planetsim.ui;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class InspectionMode {
    public InspectionMode(Skin skin, Stage stage, SelectBox<String> inspectSelect) {

        inspectSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                String currentSelection = inspectSelect.getSelected();

                switch (currentSelection) {
                    case "Climate":
                        Dialog dialog = new Dialog("Inspection Mode: Climate", skin);
                        dialog.setMovable(true);
                        dialog.setResizable(true);
                        dialog.setPosition(500, 500);
                        dialog.setWidth(250);
                        dialog.text("Climate description here...");
                        dialog.button("Close", true);
                        stage.addActor(dialog);
                        inspectSelect.setSelectedIndex(0);
                        break;

                    case "Thermodynamics":
                        Dialog thermoDialog = new Dialog("Thermodynamics", skin);
                        thermoDialog.setMovable(true);
                        thermoDialog.setResizable(false);
                        thermoDialog.setPosition(400, 500);
                        thermoDialog.setWidth(545);
                        thermoDialog.setHeight(160);
                        thermoDialog.text("Atmospheric thermodynamics describes the effect of buoyant forces that \n" +
                                "cause the rise of less dense (warmer) air, the descent of more dense air,\n" +
                                "and the transformation of water from liquid to vapor (evaporation) and \n" +
                                "its condensation.");
                        thermoDialog.button("Close", true);
                        stage.addActor(thermoDialog);
                        inspectSelect.setSelectedIndex(0);
                        break;

                    case "Magnetism":
                        Dialog magDialog = new Dialog("Inspection Mode: Magnetism", skin);
                        magDialog.setMovable(true);
                        magDialog.setResizable(true);
                        magDialog.setPosition(500, 500);
                        magDialog.setWidth(250);
                        magDialog.text("Magnetism description here...");
                        magDialog.button("Close", true);
                        stage.addActor(magDialog);
                        inspectSelect.setSelectedIndex(0);
                        break;

                    case "Van Allen Belt":
                        Dialog allenBeltDialog = new Dialog("Van Allen Belt", skin);
                        allenBeltDialog.setMovable(true);
                        allenBeltDialog.setResizable(false);
                        allenBeltDialog.setPosition(400, 500);
                        allenBeltDialog.setWidth(520);
                        allenBeltDialog.setHeight(150);
                        allenBeltDialog.text("A Van Allen radiation belt is a zone of energetic charged particles, \n" +
                                "most of which originate from the solar wind, that are captured by \n" +
                                "and held around a planet by that planet's magnetic field. Earth has \n" +
                                "two such belts and sometimes others may be temporarily created.");
                        allenBeltDialog.button("Close", true);
                        stage.addActor(allenBeltDialog);
                        inspectSelect.setSelectedIndex(0);
                        break;

                    case "Greenhouse Effect":
                        Dialog greenhouseDialog = new Dialog("Greenhouse Effect", skin);
                        greenhouseDialog.setMovable(true);
                        greenhouseDialog.setResizable(false);
                        greenhouseDialog.setPosition(400, 500);
                        greenhouseDialog.setWidth(565);
                        greenhouseDialog.setHeight(150);
                        greenhouseDialog.text("Greenhouse effect, a warming of Earth's surface and troposphere \n" +
                                "(the lowest layer of the atmosphere) caused by the presence of water vapor,\n " +
                                "carbon dioxide, methane, and certain other gases in the air. Of those gases,\n " +
                                "known as greenhouse gases, water vapor has the largest effect.");
                        greenhouseDialog.button("Close", true);
                        stage.addActor(greenhouseDialog);
                        inspectSelect.setSelectedIndex(0);
                        break;

                    case "Aerosols":
                        Dialog aerosolsDialog = new Dialog("Aerosols", skin);
                        aerosolsDialog.setMovable(true);
                        aerosolsDialog.setResizable(false);
                        aerosolsDialog.setPosition(400, 500);
                        aerosolsDialog.setWidth(565);
                        aerosolsDialog.setHeight(160);
                        aerosolsDialog.text("There are some aerosols that cause cooling while others cause warming. \n" +
                                "Aerosols come from volcanoes, dust storms, fires, vegetation, sea spray, \n" +
                                "burning of fossil fuels and land use. Warming aerosols include black carbon \n" +
                                "and dark soot. Cooling aerosols include dust, sulfate particles and sea spray.\n");
                        aerosolsDialog.button("Close", true);
                        stage.addActor(aerosolsDialog);
                        inspectSelect.setSelectedIndex(0);
                        break;

                    case "Volcanoes":
                        Dialog volcanoesDialog = new Dialog("Volcanoes", skin);
                        volcanoesDialog.setMovable(true);
                        volcanoesDialog.setResizable(false);
                        volcanoesDialog.setPosition(400, 500);
                        volcanoesDialog.setWidth(550);
                        volcanoesDialog.setHeight(160);
                        volcanoesDialog.text("Volcanoes erupt because of density and pressure. The lower density of \n" +
                                "the magma relative to the surrounding rocks causes it to rise. It will \n" +
                                "rise to the surface or to a depth that is determined by the density of \n" +
                                "the magma and the weight of the rocks above it.");
                        volcanoesDialog.button("Close", true);
                        stage.addActor(volcanoesDialog);
                        inspectSelect.setSelectedIndex(0);
                        break;
                }
            }
        });
    }
}