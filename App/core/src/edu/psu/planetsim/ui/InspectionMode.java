package edu.psu.planetsim.ui;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import edu.psu.planetsim.AppState;

public class InspectionMode {
    public InspectionMode(Skin skin, Stage stage, SelectBox<String> inspectSelect,
        AppState appState) {

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
                        dialog.text("Climate is commonly considered to be the weather averaged over a long period of \n" +
                                    " time. The different parts of the weather include: temperature, pressure, \n" +
                                    "precipitation and humidity.");
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
                        if (appState.inspectionMode == AppState.InspectionMode.None)
                            appState.inspectionMode = AppState.InspectionMode.Thermodynamics;
                        else
                        appState.inspectionMode = AppState.InspectionMode.None;
                        break;

                    case "Magnetism":
                        Dialog magDialog = new Dialog("Inspection Mode: Magnetism", skin);
                        magDialog.setMovable(true);
                        magDialog.setResizable(true);
                        magDialog.setPosition(500, 500);
                        magDialog.setWidth(250);
                        magDialog.text("Magnetism, along with gravity and electricity, is a universal force of nature.\n " +
                                       "Earth’s magnetic field has a north and south pole, and these poles happen to \n" +
                                        "roughly line up with geographic north and south.");
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

                        case "Barycenter":
                        Dialog BarycenterDialog = new Dialog("Barycenter", skin);
                        BarycenterDialog.setMovable(true);
                        BarycenterDialog.setResizable(false);
                        BarycenterDialog.setPosition(400, 500);
                        BarycenterDialog.setWidth(550);
                        BarycenterDialog.setHeight(170);
                        BarycenterDialog.text("The Barycenter is the center of mass of two or more bodies that orbit \n" +
                                              "one another and is the point about which the bodies orbit. If one of \n" +
                                              "the two orbiting bodies is much more massive than the other and the \n" +
                                              "bodies are relatively close to one another, the barycenter will typically\n " +
                                              "be located within the more massive object.");
                        BarycenterDialog.button("Close", true);
                        stage.addActor(BarycenterDialog);
                        inspectSelect.setSelectedIndex(0);
                        break;
                }
            }
        });
    }
}
