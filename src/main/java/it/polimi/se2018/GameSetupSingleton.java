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
        DeckLoader privateObjectiveCardDeckLoader = new DeckLoader("it/polimi/se2018/PrivateObjectiveCard.xml");
        DeckLoader publicObjectiveCardDeckLoader = new DeckLoader("it/polimi/se2018/PublicObjectiveCard.xml");
        DeckLoader toolCardDeckLoader = new DeckLoader("it/polimi/se2018/ToolCard.xml");
        DeckLoader windowPatternCardDeckLoader = new DeckLoader("it/polimi/se2018/WindowPatternCard.xml");
        this.privateObjectiveCardDeck = privateObjectiveCardDeckLoader.getPrivateObjectiveCardDeck(players.size());
        this.publicObjectiveCardDeck = publicObjectiveCardDeckLoader.getPublicObjectiveCardDeck();
        this.toolCardDeck = toolCardDeckLoader.getToolCardDeck(3);
        this.windowPatternCardDeck = windowPatternCardDeckLoader.getWindowPatternCardDeck(players.size());
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



    //assigns 4 window patterns for each player
    public void assignWindowPattern(){
        List <WindowPatternCard> deckToDistribute = windowPatternCardDeck.mixAndDistribute();
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
