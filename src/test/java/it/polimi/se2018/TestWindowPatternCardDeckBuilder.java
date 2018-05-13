package it.polimi.se2018;

import it.polimi.se2018.model.Deck;
import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.model.WindowPatternCard;
import org.junit.Before;
import org.junit.Test;


import java.util.List;

import static org.junit.Assert.*;

public class TestWindowPatternCardDeckBuilder {

    private final String filePath = "src/main/java/it/polimi/se2018/xml/WindowPatternCard.xml";
    private WindowPatternCardDeckBuilder windowPatternCardDeckBuilder;

    private boolean checkWindowPatternName(WindowPattern windowPattern){

        return  windowPattern.getName().equals("Kaleidoscopic Dream") ||
                windowPattern.getName().equals("Firmitas") ||
                windowPattern.getName().equals("Fractal Drops") ||
                windowPattern.getName().equals("Lux Mundi") ||
                windowPattern.getName().equals("Lux Astram") ||
                windowPattern.getName().equals("Gravitas") ||
                windowPattern.getName().equals("Water of Life") ||
                windowPattern.getName().equals("Sun Catcher") ||
                windowPattern.getName().equals("Shadow Thief") ||
                windowPattern.getName().equals("Aurorae Magnificus") ||
                windowPattern.getName().equals("Aurora Sagradis") ||
                windowPattern.getName().equals("Symphony of Light") ||
                windowPattern.getName().equals("Virtus") ||
                windowPattern.getName().equals("Firelight") ||
                windowPattern.getName().equals("Sun Glory") ||
                windowPattern.getName().equals("Battllo") ||
                windowPattern.getName().equals("Bellesguard") ||
                windowPattern.getName().equals("Fulgor del Cielo") ||
                windowPattern.getName().equals("Luz Celestial") ||
                windowPattern.getName().equals("Chromatic Splendor") ||
                windowPattern.getName().equals("Comitas") ||
                windowPattern.getName().equals("Via Lux") ||
                windowPattern.getName().equals("Industria");

    }

    @Before
    public void setUp(){

       windowPatternCardDeckBuilder = new WindowPatternCardDeckBuilder(filePath);
    }

    @Test
    public void testGetWindowPatternCardDeck(){

        Deck<WindowPatternCard> windowPatternCardDeck = windowPatternCardDeckBuilder.getWindowPatternCardDeck();
        List<WindowPatternCard> windowPatternCards = windowPatternCardDeck.mixAndDistribute(1);
        WindowPatternCard windowPatternCard = windowPatternCards.get(0);
        WindowPattern windowPattern = windowPatternCard.getWindowPattern1();
        assertTrue(checkWindowPatternName(windowPattern));

    }
}
