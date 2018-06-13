package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ThreadLocalRandom;

import it.polimi.se2018.events.mvevent.*;
import it.polimi.se2018.events.vcevent.*;


/**
 * This class represents the game and contains all the objects on the game table.
 * This class is a singleton because there can be only one game at a time.
 * @author Daniele Mattioli
 */
public class GameSingleton extends Observable{
    private int playersNumber;
    private int diceNumberToExtract;
    private int round;
    private Player currentPlayer;
    private List<Player> players;
    private List<PublicObjectiveCard> publicObjectiveCards;
    private List<ToolCard> toolCards;
    private List<Dice> diceBag;
    private List<Dice> draftPool;
    private RoundTrack roundTrack;
    private int lap; //indicates current semi round
    private static GameSingleton instance = null;

    /**
     * Private constructor of the class. It's private because of the singleton pattern.
     * @param players list of all players in the game
     * @param publicObjectiveCards list of the public objective cards chosen during game setup
     * @param toolCards list of the toll cards chosen during game setup
     * @param roundTrack round track of the game
     */
    private GameSingleton( List<Player> players, List<PublicObjectiveCard> publicObjectiveCards, List<ToolCard> toolCards, RoundTrack roundTrack) {
        this.players = players;
        playersNumber = players.size();
        diceNumberToExtract = (2 * playersNumber) + 1;
        round = 1;
        this.currentPlayer = players.get(0);
        this.publicObjectiveCards = publicObjectiveCards;
        this.toolCards = toolCards;
        this.diceBag = createDiceBag();
        this.roundTrack = roundTrack; //round track created during setup
        lap = 0;
        draftPool = new ArrayList<>();

    }

    /**
     * Method called by users to create the instance of the class.
     * @param players list of all players in the game
     * @param publicObjectiveCards list of the public objective cards chosen during game setup
     * @param toolCards list of the toll cards chosen during game setup
     * @param roundTrack round track of the game
     * @return the instance of the class
     */
    public static GameSingleton instance(List<Player> players, List<PublicObjectiveCard> publicObjectiveCards, List<ToolCard> toolCards, RoundTrack roundTrack) {
        if (instance == null)
            instance = new GameSingleton(players, publicObjectiveCards, toolCards, roundTrack); //creates instance only if it doesn't already exist
        return instance;
    }

    /**
     * Gets all the tool cards
     * @return tool cards
     */
    public List<ToolCard> getToolCards() {
        return toolCards;
    }

    /**
     * Gets all the public objective cards
     * @return public objective cards
     */
    public List<PublicObjectiveCard> getPublicObjectiveCards() {
        return publicObjectiveCards;
    }

    /**
     * Gets current player
     * @return current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Gets current lap
     * @return current lap
     */
    public int getLap() {
        return this.lap;
    }

    /**
     * Gets draft pool
     * @return draft pool
     */
    public List<Dice> getDraftPool() {
        return draftPool;
    }

    /**
     * Gets round track
     * @return round track
     */
    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

    /**
     * Gets player number
     * @return player number
     */
    public int getPlayersNumber() {
        return playersNumber;
    }

    /**
     * Gets dice number to extract each round
     * @return number to extract each round
     */
    public int getDiceNumberToExtract() {
        return diceNumberToExtract;
    }

    /**
     * Gets current round
     * @return current round
     */
    public int getRound() {
        return round;
    }

    /**
     * Gets list of all players in the game
     * @return list of all players in the game
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Gets dice bag
     * @return  dice bag
     */
    public List<Dice> getDiceBag() {
        return diceBag;
    }

    /**
     * Sets current player
     * @param currentPlayer New current player
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Sets current lap
     * @param lap New current lap
     */
    public void setLap(int lap) {
        this.lap = lap;
    }

    /**
     * Sets current round
     * @param round New current round
     */
    public void setRound(int round) {
        this.round = round;
    }


    /**
     * Creates a new dice bag made of 90 dices (18 for each colour). It's a private method because it has to be seen only by the constructor.
     * @return  A new dice bag.
     */
    private List<Dice> createDiceBag() {
        List<Dice> diceBagToReturn = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            Dice diceB = new Dice(Colour.BLUE);
            Dice diceY = new Dice(Colour.YELLOW);
            Dice diceP = new Dice(Colour.PURPLE);
            Dice diceG = new Dice(Colour.GREEN);
            Dice diceR = new Dice(Colour.RED);
            diceBagToReturn.add(diceB);
            diceBagToReturn.add(diceY);
            diceBagToReturn.add(diceP);
            diceBagToReturn.add(diceG);
            diceBagToReturn.add(diceR);
        }
        return diceBagToReturn;
    }

    /**
     *Increases current round by one.

     */
    public void increaseRound() {
        round++;
    }


    public void extractAndRollOneDice(){
        int randomNumber;
        randomNumber = ThreadLocalRandom.current().nextInt(diceBag.size());
        diceBag.get(randomNumber).roll();
        draftPool.add(diceBag.get(randomNumber));
        diceBag.remove(randomNumber);
    }
    /**
     * Extracts dices from dice bag and put them in the draft pool
     */
    public void extractAndRoll() {
        int i;
        for (i = 0; i < diceNumberToExtract; i++) {
            extractAndRollOneDice();
        }
         //DraftPoolEvent draftPoolEvent = new DraftPoolEvent( draftPoolToString());
         //notifyObservers(draftPoolEvent);
    }


    /**
     * Moves the remaining dices from draft pool to round track
     */
    public void fromDraftPoolToRoundTrack() {
        roundTrack.addDices(draftPool);
        draftPool.clear();

    }


    //TODO: aggiungere evento WinnerEvent
    /**
     * Computes the winner
     * @return Winner of the game
     */
    public Player selectWinner() {
        int i;
        i = 0;
        Player winner;
        winner = players.get(i);
        for (i = 1; i < playersNumber; i++) {
            if ((players.get(i).getScore() > winner.getScore()) ||
                (players.get(i).getScore() == winner.getScore() &&
                (players.get(i).computePrivateObjectiveCardScore() > winner.computePrivateObjectiveCardScore() ||
                (players.get(i).computePrivateObjectiveCardScore() == winner.computePrivateObjectiveCardScore() && players.get(i).getFavorTokensNumber() > winner.getFavorTokensNumber()) ||
                (players.get(i).getFavorTokensNumber() == winner.getFavorTokensNumber() && i < players.indexOf(winner)))))
                winner = players.get(i);
        }
        return winner;
    }

    /**
     *Gets a representation of the draft pool
     * @return A representation of the draft pool
     */
    public List<String> draftPoolToString(){
        List<String> tmp = new ArrayList<>();
        if (draftPool.isEmpty()) {
            tmp.add("[]");
            return tmp;
        }
        int i = 1;
        for( Dice dice : draftPool){
            tmp.add("\n\t" + i + ":    "+ dice.toString());
            i++;
        }
        tmp.add("\n");
        return tmp;
    }

    public List<String> draftPoolToStringPath(){
        List<String> tmp = new ArrayList<>();
        if (draftPool.isEmpty()) {
            tmp.add("");
            return tmp;
        }
        for( Dice dice : draftPool)
            tmp.add(dice.toStringPath());
        return tmp;
    }

    /**
     * Gets a representation of the dice patterns of all players
     * @return A representation of the dice patterns of all players
     */
    public List<String> dicePatternsToString(){
        List<String> list = new ArrayList<>();
        for(Player player : getPlayers()) {
            list.add(player.getDicePattern().toString());
        }
        return list;
    }


    /**
     * Gets a representation of all the tool cards
     * @return  A representation of all the tool cards
     */
    public List<String> toolCardsToString(){
        List <String> list = new ArrayList<>();
        for( ToolCard toolCard : toolCards){
            list.add(toolCard.toString());
        }
        return list;
    }

    public List<String> toolCardsToStringAbbreviated(){
        List <String> list = new ArrayList<>();
        for( ToolCard toolCard : toolCards){
            list.add(toolCard.toStringAbbreviated());
        }
        return list;
    }

    public List<String> toolCardsToStringPath(){
        List <String> list = new ArrayList<>();
        for( ToolCard toolCard : toolCards){
            list.add(toolCard.toStringPath());
            list.add(String.valueOf(toolCard.getId()));
        }
        return list;
    }

    public List<String> playerToString() {
        List<String> player = new ArrayList<>();
        player.add(getCurrentPlayer().getName());
        return player;
    }

    /**
     * Gets a representation of all players
     * @return A representation of all players
     */
    public List<String> playersToString(){
        List<String> list = new ArrayList<>();
        for(Player player : getPlayers())
            list.add(player.getName());
        return list;
    }

    public List<String> publicObjectiveCardsToStringPath(){
        List<String> list = new ArrayList<>();
        for (PublicObjectiveCard card : publicObjectiveCards)
            list.add(card.toStringPath());
        return list;
    }

    public List<List<String>> dicePatternsToStringPath(){
        List<List<String>> list = new ArrayList<>();
        for(Player player : players)
            list.add(player.getDicePattern().dicePatternToStringPath());
        return list;
    }

    public List<String> windowPatternsToStringPath(){
        List<String> list = new ArrayList<>();
        for(Player player : players)
            list.add(player.getDicePattern().getWindowPattern().toStringPath());
        return list;
    }

    public List<String> getFavorTokensOnToolCards(){
        List<String> list = new ArrayList<>();
        for(ToolCard toolCard : toolCards)
            list.add(Integer.toString(toolCard.getFavorPoint()));
        return list;
    }

    public List<String> getFavorTokensNumberPlayers(){
        List<String> list = new ArrayList<>();
        for(Player player : players)
            list.add(Integer.toString(player.getFavorTokensNumber()));
        return list;
    }


    /**
     * Gets a representation of  public objective cards in the game
     * @return A representation of  public objective cards in the game
     */
    public List <String> publicObjectiveCardsToString(){
        List<String> list = new ArrayList<>();
        for (PublicObjectiveCard card : publicObjectiveCards)
            list.add(card.toString());
        return list;
    }

    /**
     * Method called in the controller before a notifyObservers
     */
    public void myNotify(MVEvent event){
        setChanged();
        notifyObservers(event);
    }

    public static GameSingleton getInstance(){
        return instance;
    }

    public void rollEveryDice() {
        int i;
        for (i = 0; i < draftPool.size(); i++)
            draftPool.get(i).roll();
        DraftPoolEvent draftPoolEvent = new DraftPoolEvent(draftPoolToString(), draftPoolToStringPath());
        myNotify(draftPoolEvent);
    }

    public void createScoreTrack(List<Integer> scores) {
        ScoreTrackEvent showScoreTrackEvent = new ScoreTrackEvent(playersToString(), scores);
        myNotify(showScoreTrackEvent);
        WinnerEvent winnerEvent = new WinnerEvent(selectWinner().getName());
        myNotify(winnerEvent);
    }

    public void lastPlayer() {
        myNotify(new ErrorEvent("\nTutti i giocatori hanno abbandonato la partita.\nHAI VINTO!"));
    }
}


