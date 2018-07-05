package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ThreadLocalRandom;
import it.polimi.se2018.events.mvevent.*;

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
        if (playersNumber != 1)
            diceNumberToExtract = (2 * playersNumber) + 1;
        else
            diceNumberToExtract = 4;
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
     * Used only after a game is ended. It sets the instance to null.
     */
    public static void instanceToNull() {
        GameSingleton.instance = null;
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
     * Increases current round by one.
     */
    public void increaseRound() {
        round++;
    }

    /**
     * Gets, removes and rolls a dice from the diceBag
     */
    public void extractAndRollOneDiceWithoutReturning(){
            int randomNumber;
            randomNumber = ThreadLocalRandom.current().nextInt(diceBag.size());
            diceBag.get(randomNumber).roll();
            draftPool.add(diceBag.get(randomNumber));
            diceBag.remove(randomNumber);
    }

    /**
     * Gets, removes and rolls a dice from the diceBag
     * @return A rolled dice from the diceBag
     */
    public Dice extractAndRollOneDice(){
        int randomNumber;

        randomNumber = ThreadLocalRandom.current().nextInt(diceBag.size());
        Dice toReturn = diceBag.get(randomNumber);
        toReturn.roll();
        diceBag.remove(randomNumber);
        return toReturn;

    }
    /**
     * Extracts dices from dice bag and put them in the draft pool
     */
    public void extractAndRoll() {
        int i;
        for (i = 0; i < diceNumberToExtract; i++) {
            extractAndRollOneDiceWithoutReturning();
        }
    }


    /**
     * Moves the remaining dices from draft pool to round track
     */
    public void fromDraftPoolToRoundTrack() {
        roundTrack.addDices(draftPool);
        draftPool.clear();

    }

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

    /**
     * Gets a representation of the draftPool for the GUI
     * @return A List of String representing path that the GUI will load
     */
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
            list.add(toolCard.toString( playersNumber == 1 ));
        }
        return list;
    }

    /**
     * Gets a representation of all the toolCard for the CLI
     * @return A List of String representing the toolCards in game
     */
    public List<String> toolCardsToStringAbbreviated(){
        List <String> list = new ArrayList<>();
        for( ToolCard toolCard : toolCards){
            list.add(toolCard.toStringAbbreviated( playersNumber == 1));
        }
        return list;
    }

    /**
     * Gets a representation of the toolCard for the GUI
     * @return A List of String representing the path of the ToolCard that the GUI will load
     */
    public List<String> toolCardsToStringPath(){
        List <String> list = new ArrayList<>();
        for( ToolCard toolCard : toolCards){
            list.add(toolCard.toStringPath());
            list.add(String.valueOf(toolCard.getId()));
        }
        return list;
    }

    /**
     * Gets a representation of a player
     * @return A List of String containing the name of a player
     */
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

    /**
     * Gets a representation of the PublicObjectiveCard for the GUI
     * @return A List of String representing the path of PublicObjectiveCard that the GUI will load
     */
    public List<String> publicObjectiveCardsToStringPath(){
        List<String> list = new ArrayList<>();
        for (PublicObjectiveCard card : publicObjectiveCards)
            list.add(card.toStringPath());
        return list;
    }

    /**
     * Gets a representation of all the DicePatterns for the GUI
     * @return A List that contains a List of String, each representing a DicePattern for the GUI
     */
    public List<List<String>> dicePatternsToStringPath(){
        List<List<String>> list = new ArrayList<>();
        for(Player player : players)
            list.add(player.getDicePattern().dicePatternToStringPath());
        return list;
    }

    /**
     * Gets a representation of all the WindowPatterns for the GUI
     * @return A List of String, each representing a path of a picture that will be load by the GUI
     */
    public List<String> windowPatternsToStringPath(){
        List<String> list = new ArrayList<>();
        for(Player player : players)
            list.add(player.getDicePattern().getWindowPattern().toStringPath());
        return list;
    }

    /**
     * Gets a representation of the favor token on each toolCard
     * @return A List representing the favor token on each toolCard
     */
    public List<String> getFavorTokensOnToolCards(){
        List<String> list = new ArrayList<>();
        for(ToolCard toolCard : toolCards)
            list.add(Integer.toString(toolCard.getFavorPoint()));
        return list;
    }

    /**
     * Gets a representation of all the favor tokens of each player
     * @return A List representing the favor tokens of each player
     */
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
     * @param event An MVEvent generated
     */
    public void myNotify(MVEvent event){
        setChanged();
        notifyObservers(event);
    }

    /**
     * Gets the unique instance of GameSingleton
     * @return The unique instance of GameSingleton
     */
    public static GameSingleton getInstance(){
        return instance;
    }

    /**
     * Rolls every dice on the DraftPool
     */
    public void rollEveryDice() {
        int i;
        for (i = 0; i < draftPool.size(); i++)
            draftPool.get(i).roll();
        DraftPoolEvent draftPoolEvent = new DraftPoolEvent(draftPoolToString(), draftPoolToStringPath());
        myNotify(draftPoolEvent);
    }

    /**
     * Creates the ScoreTrackEvent and send it to every player
     * @param scores It's the list containing the scores of the players.
     */
    public void createScoreTrack(List<Integer> scores) {
        ScoreTrackEvent showScoreTrackEvent = new ScoreTrackEvent(playersToString(), scores);
        myNotify(showScoreTrackEvent);
    }

    /**
     * This method sends to the last player a message saying that he has won because every other player left.
     */
    public void lastPlayer() {
        Player winner = null;
        for (Player player: players) {
            if (!player.isConnectionLost()) {
                winner = player;
                if (winner.getScore() < 1)
                    winner.setScore(1);
            } else {
                player.setScore(0);
            }
        }
        players.remove(winner);
        players.add(0, winner);

        List<Integer> scores = new ArrayList<>();
        for (Player player: players) {
            scores.add(player.getScore());
        }
        createScoreTrack(scores);
    }
}


