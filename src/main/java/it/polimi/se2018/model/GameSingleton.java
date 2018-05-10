package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


public class GameSingleton {
    private int playersNumber;
    private int diceNumberToExtract;
    private int round;
    private Player currentPlayer;
    private ArrayList<Player> players;
    private ArrayList<PublicObjectiveCard> publicObjectiveCards;
    private ArrayList<ToolCard> toolCards;
    private ArrayList<Dice> diceBag;
    private ArrayList<Dice> draftPool;
    private RoundTrack roundTrack;
    private ScoreTrack scoreTrack;
    private int lap; //indicates current semi round
    private static GameSingleton instance = null;

    //private constructor
    private GameSingleton(ArrayList<Player> players, ArrayList<PublicObjectiveCard> publicObjectiveCards, ArrayList<ToolCard> toolCards, RoundTrack roundTrack, ScoreTrack scoreTrack) {
        this.players = players;
        playersNumber = players.size();
        diceNumberToExtract = (2 * playersNumber) + 1;
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
    public static GameSingleton instance(ArrayList<Player> players, ArrayList<PublicObjectiveCard> publicObjectiveCards, ArrayList<ToolCard> toolCards, RoundTrack roundTrack, ScoreTrack scoreTrack) {
        if (instance == null)
            instance = new GameSingleton(players, publicObjectiveCards, toolCards, roundTrack, scoreTrack); //creates instance only if it doesn't already exist
        return instance;
    }


    public ArrayList<ToolCard> getToolCards() {
        return toolCards;
    }


    public ArrayList<PublicObjectiveCard> getPublicObjectiveCards() {
        return publicObjectiveCards;
    }

    //returns current player
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    //gets current lap
    public int getLap() {
        return this.lap;
    }

    //returns draft pool
    public ArrayList<Dice> getDraftPool() {
        return draftPool;
    }

    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

    public ScoreTrack getScoreTrack() {
        return scoreTrack;
    }

    public int getPlayersNumber() {
        return playersNumber;
    }

    public int getDiceNumberToExtract() {
        return diceNumberToExtract;
    }

    public int getRound() {
        return round;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Dice> getDiceBag() {
        return diceBag;
    }

    //sets current player
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    //sets lap
    public void setLap(int lap) {
        this.lap = lap;
    }


    //returns dice bag, which contains 90 dices. It's a private method beacuse it has to be seen only by the constructor
    private ArrayList<Dice> createDiceBag() {
        ArrayList<Dice> DiceBagToReturn = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            Dice diceB = new Dice(Colour.BLUE);
            Dice diceY = new Dice(Colour.YELLOW);
            Dice diceP = new Dice(Colour.PURPLE);
            Dice diceG = new Dice(Colour.GREEN);
            Dice diceR = new Dice(Colour.RED);
            DiceBagToReturn.add(diceB);
            DiceBagToReturn.add(diceY);
            DiceBagToReturn.add(diceP);
            DiceBagToReturn.add(diceG);
            DiceBagToReturn.add(diceR);
        }
        return DiceBagToReturn;
    }


    //if it's possible increases the round by 1
    public void increaseRound() {
        if (round >= 1 && round <= 9)
            round++;
    }

    //extracts dices from dice bag and put them in the draft pool
    public void extractAndRoll() {
        int i;
        int randomNumber;
        for (i = 0; i < diceNumberToExtract; i++) {
            randomNumber = ThreadLocalRandom.current().nextInt(diceBag.size());
            diceBag.get(randomNumber).roll();
            draftPool.add(diceBag.get(randomNumber));
            diceBag.remove(randomNumber);
        }
    }


    //moves  remaining dices from draft pool to round track
    public void fromDraftPoolToRoundTrack() {
        roundTrack.addDices(draftPool);
    }


    //return the winner of the game
    public Player selectWinner() {
        int i;
        i = 0;
        Player winner;
        winner = players.get(i);
        for (i = 1; i < playersNumber; i++) {
            if ((players.get(i).getScore() > winner.getScore()) ||
                    (players.get(i).getScore() == winner.getScore() && players.get(i).computePrivateObjectiveCardScore() > winner.computePrivateObjectiveCardScore()) ||
                    (players.get(i).getFavorTokensNumber() == winner.getFavorTokensNumber() && i < players.indexOf(winner)))

                winner = players.get(i);
        }
        return winner;
    }
}


