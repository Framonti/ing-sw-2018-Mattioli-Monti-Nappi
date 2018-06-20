package it.polimi.se2018.model;

import it.polimi.se2018.utilities.PrivateObjectiveCardDeckBuilder;
import it.polimi.se2018.utilities.PublicObjectiveCardDeckBuilder;
import it.polimi.se2018.utilities.ToolCardDeckBuilder;
import it.polimi.se2018.utilities.WindowPatternCardDeckBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class manages the first stages of a game, like extracting the PublicObjectiveCard or assigning the WindowPattern to each player
 * @author Daniele Mattioli
 */

public class GameSetupSingleton {
    private List<Player> players;
    private Deck<PublicObjectiveCard> publicObjectiveCardDeck;
    private Deck<PrivateObjectiveCard> privateObjectiveCardDeck;
    private Deck<ToolCard> toolCardDeck;
    private Deck<WindowPatternCard> windowPatternCardDeck;
    private static GameSetupSingleton instance = null;


    /**
     * The constructor loads all the decks from xml files
     */
    private GameSetupSingleton(){
        PrivateObjectiveCardDeckBuilder privateObjectiveCardDeckLoader = new PrivateObjectiveCardDeckBuilder("src/main/java/it/polimi/se2018/xml/PrivateObjectiveCard.xml");
        PublicObjectiveCardDeckBuilder publicObjectiveCardDeckLoader = new PublicObjectiveCardDeckBuilder("src/main/java/it/polimi/se2018/xml/PublicObjectiveCard.xml");
        ToolCardDeckBuilder toolCardDeckLoader = new ToolCardDeckBuilder("src/main/java/it/polimi/se2018/xml/ToolCard.xml");
        WindowPatternCardDeckBuilder windowPatternCardDeckLoader = new WindowPatternCardDeckBuilder("src/main/java/it/polimi/se2018/xml/WindowPatternCard.xml");
        this.privateObjectiveCardDeck = privateObjectiveCardDeckLoader.getPrivateObjectiveCardDeck();
        this.publicObjectiveCardDeck = publicObjectiveCardDeckLoader.getPublicObjectiveCardDeck();
        this.toolCardDeck = toolCardDeckLoader.getToolCardDeck();
        this.windowPatternCardDeck = windowPatternCardDeckLoader.getWindowPatternCardDeck();
        this.players = new ArrayList<>();
    }

    /**
     * Instances a new GameSetupSingleton or return the already existing instance
     * @return The unique instance of class GameSetupSingleton
     */
    public static GameSetupSingleton instance(){
        if (instance == null)
            instance = new GameSetupSingleton();
        return instance;
    }

    /**
     * Used only after a game is ended. It sets the instance to null.
     */
    public static void instanceToNull() {
        GameSetupSingleton.instance = null;
    }

    /**
     * Gets a list of PrivateObjectiveCard from the deck
     * @param numberToExtract The number of cards to extract
     * @return A List of PrivateObjectiveCard
     */
    private List<PrivateObjectiveCard> getPrivateObjectiveCardList(int numberToExtract) {

        return privateObjectiveCardDeck.mixAndDistribute(numberToExtract);
    }

    /**
     * Gets a list of PublicObjectiveCard from the deck
     * @param numberToExtract The number of cards to extract
     * @return A List of PublicObjectiveCard
     */
    private List<PublicObjectiveCard> getPublicObjectiveCardList(int numberToExtract) {

        return publicObjectiveCardDeck.mixAndDistribute(numberToExtract);
    }

    /**
     * Gets a list of WindowPatternCard from the deck
     * @param playersNumber The number of player in game
     * @return A List of WindowPatternCard
     */
    private List<WindowPatternCard> getWindowPatternCardList(int playersNumber) {

        return windowPatternCardDeck.mixAndDistribute(playersNumber*2);
    }

    /**
     * Gets a list of ToolCard from the deck
     * @param numberToExtract The number of cards to extract
     * @return A List of ToolCard
     */
    private List<ToolCard> getToolCardList(int numberToExtract){
        return toolCardDeck.mixAndDistribute(numberToExtract);
    }

    /**
     * Gets the players
     * @return A List of Players
     */
    public  List<Player> getPlayers() {
        return players;
    }

    /**
     * Randomly shuffle the players
     */
    private void choosePlayersOrder(){
        Collections.shuffle(players);
    }

    /**
     * Assigns one or two PrivateObjectiveCards to each player
     */
    private void assignPrivateObjective(boolean isSinglePlayer){

        List<PrivateObjectiveCard> toDistribute = getPrivateObjectiveCardList( (isSinglePlayer ? 2 : players.size()) );
        for(int i = 0; i < toDistribute.size(); i++) {
            if (i < players.size())
                players.get(i).addPrivateObjectiveCard(toDistribute.get(i));
            else
                players.get(i - 1).addPrivateObjectiveCard(toDistribute.get(i));
        }
    }

    /**
     * Create a new GameSingleton instance
     * @param singlePlayerDifficulty Is 0 for multiplayer, from 1 to 5 for singlePlayer
     * @return A GameSingleton instance
     */
    public GameSingleton createNewGame(int singlePlayerDifficulty) {    //il parametro è 0 in multiplayer, tra 1 e 5 in singleplayer

        assignWindowPatterns();
        assignPrivateObjective( (singlePlayerDifficulty != 0) );   //bisogna pescarne 2 se il parametro è diverso da 0
        choosePlayersOrder();
        return GameSingleton.instance(players, getPublicObjectiveCardList((singlePlayerDifficulty == 0 ? 3 : 2)),
                getToolCardList((singlePlayerDifficulty == 0 ? 3 : 6 - singlePlayerDifficulty)), new RoundTrack());
    }

    /**
     * Assigns 4 window patterns to each player
     */

    public void assignWindowPatterns(){
        List <WindowPatternCard> deckToDistribute = getWindowPatternCardList(players.size());
        int i = 0;
        int j;
        for(Player player : players){
            for(j = 0; j < 2 ; j++) {
                player.addWindowPattern(deckToDistribute.get(i).getWindowPattern1());
                player.addWindowPattern(deckToDistribute.get(i).getWindowPattern2());
                i++;
            }

        }
    }

    /**
     * Add a players to the game
     * @param players Player to be added to the game
     */
    public void addPlayers(List<Player> players){
        this.players = players;
    }


}
