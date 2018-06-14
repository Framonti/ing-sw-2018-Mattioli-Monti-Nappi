package it.polimi.se2018.model;

import it.polimi.se2018.events.mvevent.FavorTokensEvent;
import it.polimi.se2018.network.client.ClientInterfaceRMI;
import it.polimi.se2018.network.client.ClientInterfaceSocket;
import it.polimi.se2018.network.server.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the player of the game
 * @author fabio
 */
public class Player {

    //Attributes
    private String name;
    private int score;
    private int favorTokensNumber;
    private WindowPattern windowPattern;
    private DicePattern dicePattern;
    private PrivateObjectiveCard privateObjectiveCard;
    private boolean diceMoved;
    private boolean toolCardUsed;
    private List<WindowPattern> windowPatterns; //represents the 4 window patterns the player has during the setup
    private int lap;
    private Map<String, Integer> publicObjectiveCards = new HashMap<>();
    private ClientInterfaceRMI clientInterfaceRMI;
    private ClientInterfaceSocket clientInterfaceSocket;
    private boolean connectionLost = false;
    private boolean isSuspended = false;


    //Constructor
    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.lap = 0;
        this.windowPatterns = new ArrayList<>();
    }



    //getters and setters
    public int getScore() { return score; }

    public int getLap() { return lap; }

    public int getFavorTokensNumber() { return favorTokensNumber; }

    public String getName() { return name; }

    public PrivateObjectiveCard getPrivateObjectiveCard() { return privateObjectiveCard; }

    public String getPrivateObjectiveCardToString(){return privateObjectiveCard.toStringPath();}

    public WindowPattern getWindowPattern() { return windowPattern; }

    public List<WindowPattern> getWindowPatterns() { return windowPatterns; }

    public DicePattern getDicePattern() { return dicePattern; }

    public ClientInterfaceRMI getClientInterfaceRMI() { return clientInterfaceRMI; }

    public ClientInterfaceSocket getClientInterfaceSocket() { return clientInterfaceSocket; }

    public boolean isDiceMoved() { return diceMoved; }

    public boolean isToolCardUsed() { return toolCardUsed; }

    public boolean isConnectionLost() { return connectionLost; }

    public boolean isSuspended() { return isSuspended; }

    public void setScore(int score) { this.score = score; }

    private void setFavorTokens() { favorTokensNumber = windowPattern.getDifficultyNumber(); }

    /**
     * This setter sets also the dicePattern and the favor tokens number
     * @param windowPattern It's the window pattern to be set to the player
     */
    public void setWindowPattern(WindowPattern windowPattern) {
        this.windowPattern = windowPattern;
        setDicePattern(windowPattern);
        setFavorTokens();
        synchronized (Server.windowPatternLock) {
            Server.windowPatternLock.notifyAll();
        }
    }

    public void setLap(int lap) { this.lap = lap; }

    private void setDicePattern(WindowPattern windowPattern) { this.dicePattern = new DicePattern(windowPattern); }

    public void setPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) { this.privateObjectiveCard = privateObjectiveCard; }

    public void setClientInterface(ClientInterfaceRMI clientInterface) { this.clientInterfaceRMI = clientInterface; }

    public void setClientInterface(ClientInterfaceSocket clientInterface) { this.clientInterfaceSocket = clientInterface; }

    public void setDiceMoved(boolean diceMoved) { this.diceMoved = diceMoved; }

    public void setToolCardUsed(boolean toolCardUsed) { this.toolCardUsed = toolCardUsed; }

    public void addWindowPattern(WindowPattern windowPatternToAdd) { windowPatterns.add(windowPatternToAdd); }

    public void setConnectionLost(boolean connectionLost) { this.connectionLost = connectionLost; }

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
                if(!dicePattern.isEmpty(position) && dicePattern.getDice(position).getColour().equals(privateObjectiveCard.getColour())) {
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
            try {
                sum +=  this.publicObjectiveCards.get(poc.getName());
            } catch (NullPointerException ignore) {}
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

        //this cicle controlls if there are two dices of the same colour or with the same value in the same column
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
