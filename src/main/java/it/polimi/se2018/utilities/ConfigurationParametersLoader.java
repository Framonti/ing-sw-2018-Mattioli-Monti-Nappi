package it.polimi.se2018.utilities;

import java.util.List;

/**
 * This class loads the configuration parameters from an xml file
 * @author Framonti
 */
public class ConfigurationParametersLoader extends LoaderXML {

    public ConfigurationParametersLoader(String filePath){

        super(filePath);
    }

    /**
     * Gets the duration of the setup timer
     * @return The duration of the setup timer in milliseconds
     */
    public int getSetupTimer(){

        List<String > setupTimerSting = this.getStringList("setupTimer");
        return Integer.parseInt(setupTimerSting.get(0));
    }

    /**
     * Gets the duration of the turn timer
     * @return The duration of the turn timer in milliseconds
     */
    public int getTurnTimer(){

        List<String > setupTimerSting = this.getStringList("turnTimer");
        return Integer.parseInt(setupTimerSting.get(0));
    }

}
