package edu.psu.planetsim.inspection;

import java.text.DecimalFormat;

import edu.psu.planetsim.AppState;
import edu.psu.planetsim.Metrics;
import edu.psu.planetsim.AppState.ViewingMode;
import edu.psu.planetsim.ui.AppUi;

/** Controls the text of the status bar. */
public class StatusBarController 
{
    private final AppState _appState;
    private final AppUi _ui;

    public StatusBarController(AppState appState, AppUi ui)
    {
        _appState = appState;
        _ui = ui;
    }

    public void update()
    {
        var text = "";

        // Create text to indicate the current celestial body.
        if (_appState.currentCelestialBodyId != null)
        {
            // Display name of currently viewed object.
            var currBody = _appState.getCurrentCelestialBody();
            text += currBody.name + " system, viewing ";
            if (_appState.viewingMode == ViewingMode.CenterOfMass)
                text += "Barycenter";
            else
                text += _appState.getCelestialBodyFocused().name;
            text += ".  ";

            // Display kinematics information.
            if (_appState.viewingMode != ViewingMode.NaturalSatellite) {
                text += "Solar Dist: ";
                var dist = Metrics.length(currBody.positionRelativeToSun.len());
                text += new DecimalFormat("0.00E00").format(dist / 1000).toLowerCase();
                text += " km";
            }
            else {
                var satellite = _appState.getCelestialBodyFocused();
                text += "Orbit Dist: ";
                var dist = Metrics.length(satellite.position.len());
                text += new DecimalFormat("0.00E00").format(dist / 1000).toLowerCase();
                text += " km";
            }
        }
        else
        {
            text += "No celestial body.";
        }

        _ui.setStatusBarText(text);
    }
}