package it.polimi.se2018;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GameSingleton {
    private int playersNumber;
    private int diceNumberToExtract;
    private int round;
    private Player  currentPlayer;
    private ArrayList <Player> players;
    private ArrayList <PublicObjectiveCard>  publicObjectiveCards;
    private ArrayList <ToolCard> toolCards;
    private HashSet <Dice> diceBag;
    private ArrayList <Dice> draftPool;
    private RoundTrack roundTrack;
    private ScoreTrack scoreTrack;
    private int lap; //indicates current semi round
    private static GameSingleton instance = null;

    //private constructor
    private GameSingleton( ArrayList<Player> players , ArrayList<PublicObjectiveCard> publicObjectiveCards, ArrayList<ToolCard> toolCards, RoundTrack roundTrack, ScoreTrack scoreTrack)
    {
        this.players = players;
        playersNumber = players.size();
        diceNumberToExtract = (2*playersNumber)+1;
        round = 1;
        this.currentPlayer = players.get(0);
        this.publicObjectiveCards = publicObjectiveCards;
        this.toolCards = toolCards;
        this.diceBag = createDiceBag();
        this.roundTrack = roundTrack; //round track created during setup
        this.scoreTrack = scoreTrack; //score track created during setup
        lap = 0;


    }

    //returns the unique instance of class GameSingleton
    public static GameSingleton instance(ArrayList<Player> players , ArrayList<PublicObjectiveCard> publicObjectiveCards, ArrayList<ToolCard> toolCards, RoundTrack roundTrack, ScoreTrack scoreTrack){
        if(instance == null)
            instance= new GameSingleton(players, publicObjectiveCards, toolCards, roundTrack, scoreTrack); //creates instance only if it doesn't already exist
        return instance;
    }

    //returns dice bag, which contains 90 dices. It's a private method beacuse it has to be seen only by the constructor
    private HashSet <Dice> createDiceBag()
    {
        HashSet <Dice> theDiceBag = new HashSet <>();
        for (int i=0; i<18; i++){
                Dice diceB = new Dice(Colour.BLUE);
                Dice diceY = new Dice(Colour.YELLOW);
                Dice diceP = new Dice(Colour.PURPLE);
                Dice diceG = new Dice(Colour.GREEN);
                Dice diceR = new Dice(Colour.RED);
                theDiceBag.add(diceB);
                theDiceBag.add(diceY);
                theDiceBag.add(diceP);
                theDiceBag.add(diceG);
                theDiceBag.add(diceR);
            }
        return theDiceBag;
    }


    //if it's possible increases the round by 1
    public void increaseRound(){
        if (round >= 1 && round <= 9)
            round++;
    }

    //extracts dices from dice bag and put them in the draft pool
    public void extractAndRoll(){
        int item = new Random().nextInt(diceBag.size());
        int j;
        for(j=0; j< diceNumberToExtract; j++) {
            int i = 0;
            for (Dice dice : diceBag) {
                if (i == item) {
                    dice.roll();
                    draftPool.add(dice);
                    diceBag.remove(dice);
                    i++;
                }
            }
        }
    }



    //moves  remaining dices from draft pool to round track
    public void fromDraftPoolToRoundTrack() {
        roundTrack.addDice(round, draftPool);
    }


    //return the winner of the game
    public Player selectWinner(){
        int i = 0;
        Player winner = players.get(i);
        for(i= 1 ; i< playersNumber; i++){
             if(players.get(i).getScore() > winner.getScore())
                 winner = players.get(i);
             else if (players.get(i).getScore() == winner.getScore()){
                 if(players.get(i).computePrivateObjectiveCardScore() > winner.computePrivateObjectiveCardScore())
                     winner = players.get(i);
                 else if(players.get(i).computePrivateObjectiveCardScore() == winner.computePrivateObjectiveCardScore()){
                     if(players.get(i).getFavorTokens() > winner.getFavorTokens())
                         winner = players.get(i);
                     else if(players.get(i).getFavorTokens() == winner.getFavorTokens() && i < players.indexOf(winner))
                         winner = players.get(i);
                 }
             }
        }
        return winner;
    }

    //returns current player
    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    //sets current player
    public void setCurrentPlayer(Player currentPlayer){
        this.currentPlayer = currentPlayer;
    }

    //gets current lap
    public int getLap(){
        return this.lap;
    }

    //sets lap
    public void setLap(int lap){
        this.lap = lap;
    }







}