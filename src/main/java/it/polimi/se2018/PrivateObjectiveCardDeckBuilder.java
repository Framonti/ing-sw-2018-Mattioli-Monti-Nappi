package it.polimi.se2018;

import java.util.ArrayList;
import java.util.List;

public class PrivateObjectiveCardDeckBuilder extends LoaderXML {

    public PrivateObjectiveCardDeckBuilder(String filePath){

        super(filePath);
    }

    //@return a Deck with all the PrivateObjectiveCard, loaded from a xml file
    public  Deck<PrivateObjectiveCard> getPrivateObjectiveCardDeck() {

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
