package it.polimi.se2018;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameSetupSingleton {
    private ArrayList<Player> players;
    private Deck<PublicObjectiveCard> publicObjectiveCardDeck;
    private Deck<PrivateObjectiveCard> privateObjectiveCardDeck;
    private Deck<ToolCard> toolCardDeck;
    private Deck<WindowPatternCard> windowPatternCardDeck;
    private static GameSetupSingleton instance = null;


    //constructor. Comments has to be added
    private GameSetupSingleton(){
        PrivateObjectiveCardDeckBuilder privateObjectiveCardDeckLoader = new PrivateObjectiveCardDeckBuilder("it/polimi/se2018/PrivateObjectiveCard.xml");
        PublicObjectiveCardDeckBuilder publicObjectiveCardDeckLoader = new PublicObjectiveCardDeckBuilder("it/polimi/se2018/PublicObjectiveCard.xml");
        ToolCardDeckBuilder toolCardDeckLoader = new ToolCardDeckBuilder("it/polimi/se2018/ToolCard.xml"); //TODO
        WindowPatternCardDeckBuilder windowPatternCardDeckLoader = new WindowPatternCardDeckBuilder("it/polimi/se2018/WindowPatternCard.xml");
        this.privateObjectiveCardDeck = privateObjectiveCardDeckLoader.getPrivateObjectiveCardDeck();
        this.publicObjectiveCardDeck = publicObjectiveCardDeckLoader.getPublicObjectiveCardDeck();
      //  this.toolCardDeck = toolCardDeckLoader.getToolCardDeck();         //TODO after the implementation of the 12 classes ToolCard
        this.windowPatternCardDeck = windowPatternCardDeckLoader.getWindowPatternCardDeck();
        this.players = new ArrayList<>();
    }

    //return the unique instance of class GameSetupSingleton
    public static GameSetupSingleton instance(){
        if (instance == null)
            instance = new GameSetupSingleton();
        return instance;
    }

    //orders players randomly
    public void choosePlayersOrder(){
        Collections.shuffle(players);
    }


    private List<PrivateObjectiveCard> getPrivateObjectiveCardList(int numberToExtract) {

        return privateObjectiveCardDeck.mixAndDistribute(numberToExtract);
    }

    private List<PublicObjectiveCard> getPublicObjectiveCardList(int numberToExtract) {

        return publicObjectiveCardDeck.mixAndDistribute(numberToExtract);
    }

    private List<WindowPatternCard> getWindowPatternCardList(int playersNumber) {

        return windowPatternCardDeck.mixAndDistribute(playersNumber);
    }

    /**Assigns 4 window patterns for each player */

    public void assignWindowPattern(){
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






}
