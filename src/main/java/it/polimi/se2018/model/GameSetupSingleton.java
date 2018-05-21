package it.polimi.se2018.model;

import it.polimi.se2018.PrivateObjectiveCardDeckBuilder;
import it.polimi.se2018.PublicObjectiveCardDeckBuilder;
import it.polimi.se2018.ToolCardDeckBuilder;
import it.polimi.se2018.WindowPatternCardDeckBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the game setup: it contains
 */

public class GameSetupSingleton {
    private List<Player> players;
    private Deck<PublicObjectiveCard> publicObjectiveCardDeck;
    private Deck<PrivateObjectiveCard> privateObjectiveCardDeck;
    private Deck<ToolCard> toolCardDeck;
    private Deck<WindowPatternCard> windowPatternCardDeck;
    private static GameSetupSingleton instance = null;


    //constructor. Comments has to be added
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

    //return the unique instance of class GameSetupSingleton
    public static GameSetupSingleton instance(){
        if (instance == null)
            instance = new GameSetupSingleton();
        return instance;
    }


    private List<PrivateObjectiveCard> getPrivateObjectiveCardList(int numberToExtract) {

        return privateObjectiveCardDeck.mixAndDistribute(numberToExtract);
    }

    private List<PublicObjectiveCard> getPublicObjectiveCardList(int numberToExtract) {

        return publicObjectiveCardDeck.mixAndDistribute(numberToExtract);
    }

    private List<WindowPatternCard> getWindowPatternCardList(int playersNumber) {

        return windowPatternCardDeck.mixAndDistribute(playersNumber*2);
    }

    //serve?
    public  List<Player> getPlayers() {
        return players;
    }


    private List<ToolCard> getToolCardList(int numberToExtract){
        return toolCardDeck.mixAndDistribute(numberToExtract);
    }

    //orders players randomly
    public void choosePlayersOrder(){
        Collections.shuffle(players);
    }

    private void assignPrivateObjective(){

        List<PrivateObjectiveCard> toDistribute = getPrivateObjectiveCardList(players.size());
        for(int i = 0; i < players.size(); i++){
            players.get(i).setPrivateObjectiveCard(toDistribute.get(i));
        }
    }


    public GameSingleton createNewGame(){

        assignWindowPatterns();
        assignPrivateObjective();
        choosePlayersOrder();
        return GameSingleton.instance(players, getPublicObjectiveCardList(3), getToolCardList(3), new RoundTrack(), new ScoreTrack(players));
    }



    /**
     * Assigns 4 window patterns to each player
     * */

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
     * Add players to the game
     * @param players Player to be added to the game
     */
    public void addPlayers(List<Player> players){
        this.players = players;
    }


}
