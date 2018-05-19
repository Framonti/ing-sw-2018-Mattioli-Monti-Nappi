package it.polimi.se2018;

import java.util.List;

public class ConfigurationParametersLoader extends LoaderXML {

    public ConfigurationParametersLoader(String filePath){

        super(filePath);
    }

    public int getSetupTimer(){

        List<String > setupTimerSting = this.getStringList("setupTimer");
        return Integer.parseInt(setupTimerSting.get(0));
    }

    public int getTurnTimer(){

        List<String > setupTimerSting = this.getStringList("turnTimer");
        return Integer.parseInt(setupTimerSting.get(0));
    }

}
