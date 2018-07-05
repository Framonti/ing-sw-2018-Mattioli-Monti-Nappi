package it.polimi.se2018.model;

import it.polimi.se2018.events.mvevent.FavorTokensEvent;
import it.polimi.se2018.network.client.ClientInterfaceRMI;
import it.polimi.se2018.network.client.ClientInterfaceSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the player of the game
 * @author fabio
 */
public class Player {

    private String name;
    private int score;
    private int favorTokensNumber;
    private WindowPattern windowPattern;
    private DicePattern dicePattern;
    private List<PrivateObjectiveCard> privateObjectiveCards;
    private boolean diceMoved;
    private boolean toolCardUsed;
    private List<WindowPattern> windowPatterns; //represents the 4 window patterns the player has during the setup
    private int lap;
    private Map<String, Integer> publicObjectiveCards = new HashMap<>();
    private ClientInterfaceRMI clientInterfaceRMI;
    private ClientInterfaceSocket clientInterfaceSocket;
    private boolean connectionLost = false;
    private boolean isSuspended = false;


    /**
     * @param name The Nickname chosen by an user during its connection to the server
     */
    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.lap = 0;
        this.windowPatterns = new ArrayList<>();
        this.privateObjectiveCards = new ArrayList<>();
    }

    /**
     * Gets the player score
     * @return The Player score
     */
    public int getScore() { return score; }

    /**
     * Gets the lap of the player
     * @return The lap of the player
     */
    public int getLap() { return lap; }

    /**
     * Gets the number of favorToken a player owns
     * @return The favorTokenNumber of the player
     */
    public int getFavorTokensNumber() { return favorTokensNumber; }

    /**
     * Gets the name of the Player
     * @return The name of the Player
     */
    public String getName() { return name; }

    /**
     * Gets a string representation of the privateObjectiveCards of a player
     * @return A List of string, each representing a privateObjectiveCard for the CLI
     */
    public List<String> getPrivateObjectiveCardsToString() {
        List<String> toReturn = new ArrayList<>();
        for (PrivateObjectiveCard privateObjectiveCard: privateObjectiveCards)
            toReturn.add(privateObjectiveCard.toString());
        return toReturn;
    }

    /**
     * Gets a string representation of the privateObjectiveCards of a player for the GUI
     * @return A List of string, each representing a path of an privateObjectiveCard that the GUI will load
     */
    public List<String> getPrivateObjectiveCardsToStringPath() {
        List<String> toReturn = new ArrayList<>();
        for (PrivateObjectiveCard privateObjectiveCard: privateObjectiveCards)
            toReturn.add(privateObjectiveCard.toStringPath());
        return toReturn;
    }

    /**
     * Gets the private objectiveCards
     * @return A list with all the privateObjectiveCard of a player
     */
    public List<PrivateObjectiveCard> getPrivateObjectiveCards() { return privateObjectiveCards; }

    /**
     * Gets the windowPattern chosen by the player
     * @return The windowPattern chosen by the player
     */
    public WindowPattern getWindowPattern() { return windowPattern; }

    /**
     * @return The player's WindowPattern
     */
    public List<WindowPattern> getWindowPatterns() { return windowPatterns; }

    /**
     * @return The Player's DicePattern
     */
    public DicePattern getDicePattern() { return dicePattern; }

    /**
     * @return The ClientInterfaceRMI associated with the user
     */
    public ClientInterfaceRMI getClientInterfaceRMI() { return clientInterfaceRMI; }

    /**
     * @return The ClientInterfaceSocket associated with the user
     */
    public ClientInterfaceSocket getClientInterfaceSocket() { return clientInterfaceSocket; }

    /**
     * Checks the diceMoved attribute
     * @return True if a player has already moved a dice to his DicePattern in his turn, false otherwise
     */
    public boolean isDiceMoved() { return diceMoved; }

    /**
     * Checks the toolCardUsed attribute
     * @return True if a player has already used a ToolCard in his turn, false otherwise
     */
    public boolean isToolCardUsed() { return toolCardUsed; }

    /**
     * Gets the connectionLost attribute
     * @return True if the connection between the client and the server is lost, false otherwise
     */
    public boolean isConnectionLost() { return connectionLost; }

    /**
     * Checks if a player was previously suspended
     * @return True if the Player is suspended, false otherwise
     */
    public boolean isSuspended() { return isSuspended; }

    /**
     * Sets the score of the player
     * @param score The final score of a player
     */
    public void setScore(int score) { this.score = score; }

    /**
     * Sets the favorTokens, based on the difficulty of the WindowPattern chosen
     */
    private void setFavorTokens() { favorTokensNumber = windowPattern.getDifficultyNumber(); }

    /**
     * This setter sets also the dicePattern and the favor tokens number
     * @param windowPattern It's the window pattern to be set to the player
     * @param isSinglePlayer True if the game is a SinglePlayer game, false otherwise
     */
    public void setWindowPattern(WindowPattern windowPattern, boolean isSinglePlayer) {
        this.windowPattern = windowPattern;
        setDicePattern(windowPattern);
        if (!isSinglePlayer)
            setFavorTokens();
    }

    /**
     * Sets a lap
     * @param lap 1 if the first lap, 2 if the second
     */
    public void setLap(int lap) { this.lap = lap; }

    /**
     * Sets a DicePattern to the player
     * @param windowPattern The windowPattern chosen by the Player
     */
    private void setDicePattern(WindowPattern windowPattern) { this.dicePattern = new DicePattern(windowPattern); }

    /**
     * Adds to a player a PrivateObjectiveCard
     * @param privateObjectiveCard The PrivateObjectiveCard to add
     */
    public void addPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) {
        privateObjectiveCards.add(privateObjectiveCard);
    }

    /**
     * Sets the ClientInterfaceRMI
     * @param clientInterface The ClientInterfaceRMI used by the user
     */
    public void setClientInterface(ClientInterfaceRMI clientInterface) { this.clientInterfaceRMI = clientInterface; }

    /**
     * Sets the ClientInterfaceSocket
     * @param clientInterface The ClientInterfaceSocket used by the user
     */
    public void setClientInterface(ClientInterfaceSocket clientInterface) { this.clientInterfaceSocket = clientInterface; }

    /**
     * Changes the value of the diceMoved
     * @param diceMoved True if the player has moved a Dice
     */
    public void setDiceMoved(boolean diceMoved) { this.diceMoved = diceMoved; }

    /**
     * Changes the value of the diceMoved
     * @param toolCardUsed True if the player has used a ToolCard
     */
    public void setToolCardUsed(boolean toolCardUsed) { this.toolCardUsed = toolCardUsed; }

    /**
     * Adds WindowPatterns to the player
     * @param windowPatternToAdd The WindowPattern to add
     */
    public void addWindowPattern(WindowPattern windowPatternToAdd) { windowPatterns.add(windowPatternToAdd); }

    /**
     * Sets the connection as lost or as regained
     * @param connectionLost True if the connection between the Client and the Server is lost, false if the connection is established again
     */
    public void setConnectionLost(boolean connectionLost) { this.connectionLost = connectionLost; }

    /**
     * Sets a player as suspended or as active
     * @param suspended True if the player runs out of time during his turn, false if a suspended user reconnects to the game
     */
    public void setSuspended(boolean suspended) { isSuspended = suspended; }

    /**
     * This method reduces the number of favor tokens by cost every time that a tool card is used
     * @param cost Represents the number of tokens needed to use the tool card
     */
    public void reduceFavorTokens(int cost) {
        if(favorTokensNumber < cost)
            throw new UnsupportedOperationException("The number of favor tokens is not sufficient");
        favorTokensNumber -= cost;
        FavorTokensEvent favorTokensEvent = new FavorTokensEvent(String.valueOf(favorTokensNumber), name);
        GameSingleton.getInstance().myNotify(favorTokensEvent);
    }

    /**
     * @return The score obtained with the private objective card
     */
    public int computePrivateObjectiveCardScore() {
        int sum = 0;
        int row;
        int column;
        Position position;
        for(row = 0; row < 4; row++) {
            for(column = 0; column < 5; column++) {
                position = new Position(row, column);

                //adds the value of the dice if it has the same colour of the privateObjectiveCard
                if(!dicePattern.isEmpty(position) && dicePattern.getDice(position).getColour().equals(privateObjectiveCards.get(0).getColour())) {
                    sum += dicePattern.getDice(position).getValue();
                }
            }
        }
        return sum;
    }

    /**
     * Computes and sets the score obtained by the player at the end of the match
     * @param publicObjectiveCards The list of the public objective cards that are in game
     */
    public void computeMyScore(List<PublicObjectiveCard> publicObjectiveCards) {
        int sum = 0;
        createMap();

        //points obtained with the PublicObjectiveCards
        for(PublicObjectiveCard poc: publicObjectiveCards) {
            sum +=  this.publicObjectiveCards.get(poc.getName());
        }

        //points obtained with the PrivateObjectiveCard
        sum += computePrivateObjectiveCardScore();

        //points obtained with the FavorTokens
        sum += favorTokensNumber;

        //points lost because of empty spaces
        sum -= dicePattern.emptySpaces();

        score = sum;
    }


    /**
     * This is a support method used by computeMyScore
     * @param isColour It's true if the public objective card is "Colori diversi - Colonna", false if "Sfumature diverse - Colonna"
     * @return The number of points obtained in the columns either with the colours or with the values
     */
    private int colPoints(boolean isColour) {
        int partialSum = 0;
        int column;
        int colScore;
        if(isColour) {
            colScore = 5;
        } else {
            colScore = 4;
        }

        //this cycle controls if there are two dices of the same colour or with the same value in the same column
        //the value of colScore is set to 0 if it has
        for(column = 0; column < 5; column++) {
            //add the colScore to sum and restore colScore for the next column
            if(hasColumnScore(column, isColour))
                partialSum += colScore;
        }
        return partialSum;
    }

    /**
     * This is a support method used by computeMyScore
     * @param isColour It's true if the public objective card is "Colori diversi - Riga", false if "Sfumature diverse - Riga"
     * @return The number of points obtained in the rows either with the colours or with the values
     */
    private int rowPoints(boolean isColour) {
        int partialSum = 0;
        int row;
        int rowScore;
        if(isColour) {
            rowScore = 6;
        } else {
            rowScore = 5;
        }

        //this cicle controlls if there are two dices of the same colour or with the same value in the same row
        //the value of rowScore is set to 0 if it has
        for(row = 0; row < 4; row++) {
            if(hasRowScore(row, isColour))
                partialSum += rowScore;
        }
        return partialSum;
    }

    /**
     * This is a support method used by computeMyScore
     * @param less It's the smaller value of the couple of dices
     * @param more It's the bigger value of the couple of dices
     * @return The number of the smaller set of the two values
     */
    private int shades(int less, int more) {
        int row;
        int column;
        int minor = 0;
        int major = 0;
        Position p1;
        for(row = 0; row < 4; row++) {
            for(column = 0; column < 5; column++) {
                p1 = new Position(row, column);
                if (!dicePattern.isEmpty(p1)) {
                    if (dicePattern.getDice(p1).getValue() == less)
                        minor++;
                    if (dicePattern.getDice(p1).getValue() == more)
                        major++;
                }
            }
        }
        return Math.min(minor, major);
    }

    /**
     * This is a support method used by computeMyScore
     * @return The number of the smaller set of one colour
     */
    private int colourVariety() {
        int yellowNum = 0;
        int greenNum = 0;
        int redNum = 0;
        int purpleNum = 0;
        int blueNum = 0;
        int row;
        int column;
        Position p1;

        //increase the related variable of the dice colour
        for(row = 0; row < 4; row++) {
            for(column = 0; column < 5; column++) {
                p1 = new Position(row, column);
                if (!dicePattern.isEmpty(p1)) {
                    switch (dicePattern.getDice(p1).getColour()) {
                        case YELLOW:
                            yellowNum++;
                            break;
                        case GREEN:
                            greenNum++;
                            break;
                        case RED:
                            redNum++;
                            break;
                        case PURPLE:
                            purpleNum++;
                            break;
                        case BLUE:
                            blueNum++;
                            break;
                    }
                }
            }
        }
        return Math.min(Math.min(Math.min(Math.min(yellowNum, greenNum), redNum), purpleNum), blueNum);
    }

    /**
     * This is a support method used by computeMyScore
     * @return The number of diagonal adjacent dices of the same colour
     */
    private int colourDiagonals() {
        int partialSum = 0;
        int row;
        int column;
        Position p1;
        for(row = 0; row < 4; row++) {
            for (column = 0; column < 5; column++) {
                p1 = new Position(row, column);
                if(hasSameColourOnDiagonal(p1))
                    partialSum++;
            }
        }
        return partialSum;
    }

    /**
     * This is a support method used by colPoints
     * @param column Represents the column analyzed
     * @param isColour It's true if the public objective card is "Colori diversi - Colonna", false if "Sfumature diverse - Colonna"
     * @return True if the column has a score, false otherwise
     */
    private boolean hasColumnScore(int column, boolean isColour) {
        int row;
        int row2;
        Position p1;
        Position p2;
        for(row = 0; row < 3; row++) {
            for(row2 = row + 1; row2 < 4; row2++) {
                p1 = new Position(row, column);
                p2 = new Position(row2, column);
                if(dicePattern.isEmpty(p1) || dicePattern.isEmpty(p2) ||
                        isColour && dicePattern.getDice(p1).getColour().equals(dicePattern.getDice(p2).getColour()) ||
                        !isColour && dicePattern.getDice(p1).getValue() == dicePattern.getDice(p2).getValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This is a support method used by rowPoints
     * @param row Represents the row analyzed
     * @param isColour isColour It's true if the public objective card is "Colori diversi - Riga", false if "Sfumature diverse - Riga"
     * @return True if the column has a score, false otherwise
     */
    private boolean hasRowScore(int row, boolean isColour) {
        int column;
        int column2;
        Position p1;
        Position p2;
        for(column = 0; column < 4; column++) {
            for(column2 = column + 1; column2 < 5; column2++) {
                p1 = new Position(row, column);
                p2 = new Position(row, column2);
                if(dicePattern.isEmpty(p1) || dicePattern.isEmpty(p2) ||
                        isColour && dicePattern.getDice(p1).getColour().equals(dicePattern.getDice(p2).getColour()) ||
                        !isColour && dicePattern.getDice(p1).getValue() == dicePattern.getDice(p2).getValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This is a support method used by colourDiagonals
     * @param position Represents the position analyzed
     * @return True if there is at least one dice with the same colour diagonally adjacent
     */
    private boolean hasSameColourOnDiagonal(Position position) {
        if(!dicePattern.isEmpty(position)) {
            List<Position> positions = position.getAdjacentPositions();
            positions.removeAll(position.getOrthogonalAdjacentPositions());
            for(Position p2: positions) {
                if (!dicePattern.isEmpty(p2) && dicePattern.getDice(position).getColour().equals(dicePattern.getDice(p2).getColour())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Maps the public objective cards to the respective methods
     */
    private void createMap() {
        this.publicObjectiveCards.put("Colori diversi - Riga", rowPoints(true));
        this.publicObjectiveCards.put("Colori diversi - Colonna", colPoints(true));
        this.publicObjectiveCards.put("Sfumature diverse - Riga", rowPoints(false));
        this.publicObjectiveCards.put("Sfumature diverse - Colonna", colPoints(false));
        this.publicObjectiveCards.put("Sfumature Chiare", shades(1, 2) * 2);
        this.publicObjectiveCards.put("Sfumature Medie", shades(3, 4) * 2);
        this.publicObjectiveCards.put("Sfumature Scure", shades(5, 6) * 2);
        this.publicObjectiveCards.put("Sfumature Diverse", Math.min(Math.min(shades(1, 2), shades(3, 4)), shades(5, 6)) * 5);
        this.publicObjectiveCards.put("Varietà di Colore", colourVariety() * 4);
        this.publicObjectiveCards.put("Diagonali Colorate", colourDiagonals());

    }
}
