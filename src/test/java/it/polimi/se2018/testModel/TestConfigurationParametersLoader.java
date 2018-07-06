package it.polimi.se2018.testModel;

import it.polimi.se2018.utilities.ConfigurationParametersLoader;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests class ConfigurationParametersLoader
 * @author Framonti
 */
public class TestConfigurationParametersLoader {

    private int turnTimer;
    private int setupTimer;

    @Before
    public void setUp(){

        ConfigurationParametersLoader configurationParametersLoader = new ConfigurationParametersLoader("src/main/java/it/polimi/se2018/xml/ConfigurationParameters.xml");
        turnTimer = configurationParametersLoader.getTurnTimer();
        setupTimer = configurationParametersLoader.getTurnTimer();
    }

    /**
     * Tests getters of the class
     */
    @Test
    public void testGetter(){

        assertEquals(100000, turnTimer);
        assertEquals(100000, setupTimer);
    }
}
