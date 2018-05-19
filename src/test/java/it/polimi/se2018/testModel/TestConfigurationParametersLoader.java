package it.polimi.se2018.testModel;

import it.polimi.se2018.ConfigurationParametersLoader;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestConfigurationParametersLoader {

    private int turnTimer;
    private int setupTimer;

    @Before
    public void setUp(){

        ConfigurationParametersLoader configurationParametersLoader = new ConfigurationParametersLoader("src/main/java/it/polimi/se2018/xml/ConfigurationParameters.xml");
        turnTimer = configurationParametersLoader.getTurnTimer();
        setupTimer = configurationParametersLoader.getTurnTimer();
    }

    @Test
    public void testGetter(){

        assertEquals(100000, turnTimer);
        assertEquals(100000, setupTimer);
    }
}
