package it.polimi.se2018.utilities;

import it.polimi.se2018.model.*;
import java.util.ArrayList;
import java.util.List;

/** This class is used for creating a Deck that contains all the WindowPatternCards present in the game, loading them from an xml file
 * @author Framonti
 */

public class PrivateObjectiveCardDeckBuilder extends LoaderXML {

    public PrivateObjectiveCardDeckBuilder(String filePath){

        super(filePath);
    }

    /** Create a Deck of PublicObjectiveCard, loading them from an xml file
     * @return A Deck with all the PublicObjectiveCard, loaded from an xml file
     */
    public Deck<PrivateObjectiveCard> getPrivateObjectiveCardDeck() {

        List<PrivateObjectiveCard> cards = new ArrayList<>();

        List<String> cardNames = this.getStringList("name");
        List<String> cardDescriptions = this.getStringList("description");
        List<String> cardColours = this.getStringList("colour");

        for (int i = 0; i < cardNames.size(); i++) {

            PrivateObjectiveCard pOC = new PrivateObjectiveCard(cardNames.get(i), cardDescriptions.get(i), Colour.getColourFromString(cardColours.get(i)));
            cards.add(pOC);
        }

        return new Deck<>(cards);
    }
}
