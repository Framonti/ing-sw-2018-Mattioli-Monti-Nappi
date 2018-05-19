package it.polimi.se2018;

import it.polimi.se2018.model.*;
import java.util.ArrayList;
import java.util.List;

/** This class is used for creating a Deck that contains all the ToolCards present in the game, loading them from an xml file
 * @author Framonti
 */

public class ToolCardDeckBuilder extends LoaderXML {

    //Constructor
    public ToolCardDeckBuilder(String filePath){

        super(filePath);
    }

    /** Create a Deck of ToolCards, loading them from an xml file
     * @return A Deck with all the ToolCard, loaded from an xml file
     */
    public Deck<ToolCard> getToolCardDeck() {

        List<ToolCard> cards = new ArrayList<>();

        List<String> cardNames = this.getStringList("name");
        List<String> cardDescriptions = this.getStringList("description");
        List<String> cardColours = this.getStringList("colour");
        List<String> cardID = this.getStringList("id");

        //Create the cards and add them to a list
        for (int i = 0; i < cardNames.size(); i++) {

            ToolCard toolCard = new ToolCard(cardNames.get(i), cardDescriptions.get(i),
                                             Colour.getColourFromString(cardColours.get(i)), Integer.parseInt(cardID.get(i)));
            cards.add(toolCard);
        }
        return new Deck<>(cards);
    }
}
