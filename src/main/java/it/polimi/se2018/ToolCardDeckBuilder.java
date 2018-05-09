package it.polimi.se2018;

import it.polimi.se2018.model.*;
import java.util.ArrayList;
import java.util.List;

/** This class loads all the ToolCards from an xml file and helps create a deck with these cards
 */
public class ToolCardDeckBuilder extends LoaderXML {

    //Constructor
    public ToolCardDeckBuilder(String filePath){

        super(filePath);
    }

    /** Create a deck with all the ToolCard, loading them from an xml file
     *
     * @return A deck with all the game ToolCards
     */
    public Deck<ToolCard> getToolCardDeck() {

        List<ToolCard> cards = new ArrayList<>();

        List<String> cardNames = this.getStringList("name");
        List<String> cardDescriptions = this.getStringList("description");
        List<String> cardColours = this.getStringList("colour");

        //Create the cards and add them to a list
        for (int i = 0; i < cardNames.size(); i++) {

            ToolCard toolCard = new ToolCard(cardNames.get(i), cardDescriptions.get(i), Colour.getColourFromString(cardColours.get(i)));
            cards.add(toolCard);
        }
        return new Deck<>(cards);
    }
}
