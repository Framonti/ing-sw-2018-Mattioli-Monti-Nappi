package it.polimi.se2018;

import java.util.ArrayList;
import java.util.List;

public class PublicObjectiveCardDeckBuilder extends LoaderXML {

    public PublicObjectiveCardDeckBuilder(String filePath) {

        super(filePath);
    }

    //@return a Deck with all the PublicObjectiveCard, loaded from a xml file
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
