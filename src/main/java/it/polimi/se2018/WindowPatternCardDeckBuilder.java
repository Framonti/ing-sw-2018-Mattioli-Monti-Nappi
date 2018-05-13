package it.polimi.se2018;

import it.polimi.se2018.model.*;

import java.util.ArrayList;
import java.util.List;

/** This class is used for creating a Deck that contains all the WindowPatternCards present in the game, loading them from an xml file
 * @author Framonti
 */

public class WindowPatternCardDeckBuilder extends LoaderXML {

    //Constructor
    public WindowPatternCardDeckBuilder (String filePath){

        super(filePath);
    }

    /** Build a Dice matrix by scanning a list of colours and values
     * @param id Represents the card id in the xml file
     * @param coloursList A list of Colour, loaded from an xml file
     * @param valuesList A list of int, loaded from an xml file
     * @return A bidimensional array of Dice that represents a WindowPattern
     */

    private Dice[][] getMatrix(int id, List<String> coloursList, List<String> valuesList) {

        int rows = 4;
        int columns = 5;
        Dice [][] toReturn = new Dice[rows][columns];
        int index = id*columns*rows;

        //The Dice only has a value, a colour or is null
        for(int i = 0; i<rows; i++) {
            for(int j = 0; j < columns; j++, index++) {

                if (!coloursList.get(index).equals(""))
                    toReturn[i][j] = new Dice(Colour.getColourFromString(coloursList.get(index)));
                else if(!valuesList.get(index).equals(""))
                    toReturn[i][j] = new Dice(Integer.parseInt(valuesList.get(index)));
                else
                    toReturn[i][j] = null;
            }
        }
        return toReturn;
    }

    /** Create a List with all the WindowPattern in the game, loaded from an xml file
     * @return A List of WindowPattern, created with the "getMatrix" method
     */

    private List<WindowPattern> getWindowPatterns() {

        List<WindowPattern> windowPatterns = new ArrayList<>();

        //read from a xml file
        List<String> cardNames = this.getStringList("name");
        List<String> cardDifficulties = this.getStringList("difficulty");
        List<String> allColours = this.getStringList("colour");
        List<String> allValues = this.getStringList("value");

        for(int i = 0; i< cardDifficulties.size(); i++) {
            Dice [][] matrix = getMatrix(i, allColours, allValues);
            WindowPattern windowPattern = new WindowPattern(cardNames.get(i), Integer.parseInt(cardDifficulties.get(i)), matrix);
            windowPatterns.add(windowPattern);
        }
        return windowPatterns;
    }

    /** This method assigns two WindowPattern to a WindowPatterCard before returning the Deck
     * @return A Deck with all the WindowPatternObjectiveCard in the game, loaded from an xml file
     */
    public Deck<WindowPatternCard> getWindowPatternCardDeck() {

        List<WindowPatternCard> cards = new ArrayList<>();
        List<WindowPattern> windowPatterns = this.getWindowPatterns();

        for(int i = 0; i<(windowPatterns.size()); i = i+2) {
            WindowPatternCard windowPatternCard = new WindowPatternCard(windowPatterns.get(i), windowPatterns.get(i+1));
            cards.add(windowPatternCard);
        }

        return new Deck<>(cards);
    }
}
