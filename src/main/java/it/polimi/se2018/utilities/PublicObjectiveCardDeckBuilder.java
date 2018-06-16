package it.polimi.se2018.utilities;

import it.polimi.se2018.model.*;
import java.util.ArrayList;
import java.util.List;

/** This class is used for creating a Deck that contains all the PublicObjectiveCards present in the game, loading them from an xml file
 * @author Framonti
 */

public class PublicObjectiveCardDeckBuilder extends LoaderXML {

    public PublicObjectiveCardDeckBuilder(String filePath) {

        super(filePath);
    }

    /** Create a Deck of PublicObjectiveCard, loading them from an xml file
     * @return A Deck with all the PublicObjectiveCard, loaded from an xml file
     */
    public Deck<PublicObjectiveCard> getPublicObjectiveCardDeck() {

        List<PublicObjectiveCard> cards = new ArrayList<>();
        List<String> cardNames = this.getStringList("name");
        List<String> cardDescriptions = this.getStringList("description");
        List<String> cardVictoryPoints = this.getStringList("victoryPoint");

        for(int i = 0; i < cardNames.size(); i++) {

            PublicObjectiveCard pOC = new PublicObjectiveCard(cardNames.get(i), cardDescriptions.get(i), Integer.parseInt(cardVictoryPoints.get(i)));
            cards.add(pOC);
        }

        return new Deck<>(cards);
    }
}
