package it.polimi.se2018;

import java.util.ArrayList;

public class Player {

    //Attributes
    private String name;
    private int score;
    private int favorTokensNumber;
    private ScoreMarker scoreMarker;
    private WindowPattern windowPattern;
    private DicePattern dicePattern;
    private PrivateObjectiveCard privateObjectiveCard;
    private boolean diceMoved;
    private boolean toolCardUsed;
    private ArrayList<WindowPattern> windowPatterns; //represents the 4 window patterns the player has during the setup



    //Constructor
    public Player(String name, PrivateObjectiveCard privateObjectiveCard) {
        this.name = name;
        this.score = 0;
        this.privateObjectiveCard = privateObjectiveCard;
    }



    //getters and setters
    public int getScore() { return score; }

    public int getFavorTokensNumber() { return favorTokensNumber; }

    public String getName() { return name; }

    public PrivateObjectiveCard getPrivateObjectiveCard() { return privateObjectiveCard; }

    public boolean isDiceMoved() { return diceMoved; }

    public void setScore(int score) { this.score = score; }

    public void setFavorTokens() { favorTokensNumber = windowPattern.getDifficultyNumber(); }

    public void setWindowPattern(WindowPattern windowPattern) { this.windowPattern = windowPattern; }

    public void setScoreMarker(ScoreMarker scoreMarker) { this.scoreMarker = scoreMarker; }

    public void setDicePattern(DicePattern dicePattern) { this.dicePattern = dicePattern; }

    public void reverseDiceMoved() { diceMoved = !diceMoved; }



    //it is called when the player uses a ToolCard, this decreases the number of the favor tokens by cost
    public void reduceFavorTokens(int cost) { favorTokensNumber -= cost; }

    //calculates the score obtained with the PrivateObjectiveCard
    public int computePrivateObjectiveCardScore() {
        int sum = 0;
        int row;
        int column;
        Position position;
        for(row = 0; row < 4; row++) {
            for(column = 0; column < 5; column++) {
                position = new Position(row, column);
                if(!dicePattern.isEmpty(position) && dicePattern.getDice(position).getColour().equals(privateObjectiveCard.getColour())) {
                    sum += dicePattern.getDice(position).getValue();
                }
            }
        }
        return sum;
    }

    //calcolates the score obtained by the player at the end of the match
    public int computeMyScore(ArrayList<PublicObjectiveCard> publicObjectiveCards) {
        int sum = 0;

        //points obtained with the PublicObjectiveCards
        for(PublicObjectiveCard poc: publicObjectiveCards) {
            switch (poc.getName()) {

                //points obtained if the card is "Colori diversi - Riga"
                case "Colori diversi - Riga":
                    sum += rowPoints(true);
                    break;

                //points obtained if the card is "Colori diversi - Colonna"
                case "Colori diversi - Colonna":
                    sum += colPoints(true);
                    break;

                //points obtained if the card is "Sfumature diverse - Riga"
                case "Sfumature diverse - Riga":
                    sum += rowPoints(false);
                    break;

                //points obtained if the card is "Sfumature diverse - Colonna"
                case "Sfumature diverse - Colonna":
                    sum += colPoints(false);
                    break;

                //points obtained if the card is "Sfumature Chiare"
                case "Sfumature Chiare":
                    sum += shades(1, 2) * poc.getVictoryPoint();
                    break;

                //points obtained if the card is "Sfumature Medie"
                case "Sfumature Medie":
                    sum += shades(3, 4) * poc.getVictoryPoint();
                    break;

                //points obtained if the card is "Sfumature Scure"
                case "Sfumature Scure":
                    sum += shades(5, 6) * poc.getVictoryPoint();
                    break;

                //points obtained if the card is "Sfumature Diverse"
                case "Sfumature Diverse":
                    sum += Math.min(Math.min(shades(1, 2), shades(3, 4)), shades(5, 6)) * poc.getVictoryPoint();
                    break;

                //points obtained if the card is "Varietà di Colore"
                case "Varietà di Colore":
                    sum += colourVariety() * poc.getVictoryPoint();
                    break;

                //points obtained if the card is "Diagonali Colorate"
                case "Diagonali Colorate":
                    sum += colourDiagonals();
                    break;
            }
        }

        //points obtained with the PrivateObjectiveCard
        sum += computePrivateObjectiveCardScore();

        //points obtained with the FavorTokens
        sum += favorTokensNumber;

        //points lost because of empty spaces
        sum -= emptySpaces();

        return sum;
    }



    //support method used by computeMyScore
    //returns the number of points obtained in the columns either with the colours or with the values
    private int colPoints(boolean isColour) {
        int partialSum = 0;
        int row;
        int row2;
        int column;
        int colScore;
        if(isColour) {
            colScore = 5;
        } else {
            colScore = 4;
        }
        Position p1;
        Position p2;
        for(column = 0; column < 5; column++) {
            for(row = 0; row < 3 && colScore != 0; row++) {
                for(row2 = row + 1; row2 < 4 && colScore != 0; row2++) {
                    p1 = new Position(row, column);
                    p2 = new Position(row2, column);
                    if(dicePattern.isEmpty(p1) || dicePattern.isEmpty(p2)) {
                        colScore = 0;
                    } else if(dicePattern.getDice(p1).getColour().equals(dicePattern.getDice(p2).getColour())) {
                        colScore = 0;
                    } else if(dicePattern.getDice(p1).getValue() == dicePattern.getDice(p2).getValue()) {
                        colScore = 0;
                    }
                }
            }
            partialSum += colScore;
            if(isColour) {
                colScore = 5;
            } else {
                colScore = 4;
            }
        }
        return partialSum;
    }

    //support method used by computeMyScore
    //returns the number of points obtained in the rows either with the colours or with the values
    private int rowPoints(boolean isColour) {
        int partialSum = 0;
        int row;
        int column;
        int column2;
        int rowScore;
        if(isColour) {
            rowScore = 6;
        } else {
            rowScore = 5;
        }
        Position p1;
        Position p2;
        for(row = 0; row < 4; row++) {
            for(column = 0; column < 4 && rowScore != 0; column++) {
                for(column2 = column + 1; column2 < 5 && rowScore != 0; column2++) {
                    p1 = new Position(row, column);
                    p2 = new Position(row, column2);
                    if(dicePattern.isEmpty(p1) || dicePattern.isEmpty(p2)) {
                        rowScore = 0;
                    } else if(isColour && dicePattern.getDice(p1).getColour().equals(dicePattern.getDice(p2).getColour())) {
                        rowScore = 0;
                    } else if(!isColour && dicePattern.getDice(p1).getValue() == dicePattern.getDice(p2).getValue()) {
                        rowScore = 0;
                    }
                }
            }
            partialSum += rowScore;
            if(isColour) {
                rowScore = 6;
            } else {
                rowScore = 5;
            }
        }
        return partialSum;
    }

    //support method used by computeMyScore
    //returns the number of the minor set of 2 values
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

    //support method used by computeMyScore
    //returns the number of the minor set of one colour
    private int colourVariety() {
        int Ys = 0;
        int Gs = 0;
        int Rs = 0;
        int Ps = 0;
        int Bs = 0;
        int row;
        int column;
        Position p1;
        for(row = 0; row < 4; row++) {
            for(column = 0; column < 5; column++) {
                p1 = new Position(row, column);
                if (!dicePattern.isEmpty(p1)) {
                    switch (dicePattern.getDice(p1).getColour()) {
                        case YELLOW:
                            Ys++;
                            break;
                        case GREEN:
                            Gs++;
                            break;
                        case RED:
                            Rs++;
                            break;
                        case PURPLE:
                            Ps++;
                            break;
                        case BLUE:
                            Bs++;
                            break;
                    }
                }
            }
        }
        return Math.min(Math.min(Math.min(Math.min(Ys, Gs), Rs), Ps), Bs);
    }

    //support method used by computeMyScore
    //returns the number of diagonal adjacent dices of the same colour
    private int colourDiagonals() {
        int partialSum = 0;
        int row;
        int row2;
        int column;
        int column2;
        Position p1;
        Position p2;
        for(row = 0; row < 4; row++) {
            for (column = 0; column < 5; column++) {
                p1 = new Position(row, column);
                if(!dicePattern.isEmpty(p1)) {
                    outer: for (row2 = row - 1; row2 < row + 2; row2 = row2 + 2) {
                        for (column2 = column - 1; column2 < column + 2; column2 = column2 + 2) {
                            if (row2 >= 0 && row2 < 4 && column2 >= 0 && column2 < 5) {
                                p2 = new Position(row2, column2);
                                if (!dicePattern.isEmpty(p2) && dicePattern.getDice(p1).getColour().equals(dicePattern.getDice(p2).getColour())) {
                                    partialSum++;
                                    break outer;
                                }
                            }
                        }
                    }
                }
            }
        }
        return partialSum;
    }

    //support method used by computeMyScore
    //returns the number of empty spaces on the dicePattern
    private int emptySpaces() {
        int partialSum = 0;
        int row;
        int column;
        Position p1;
        for(row = 0; row < 4; row++) {
            for(column = 0; column < 5; column++) {
                p1 = new Position(row, column);
                if(dicePattern.isEmpty(p1))
                    partialSum++;
            }
        }
        return partialSum;
    }

    //
    public void addWindowPattern(WindowPattern windowPatternToAdd){
        windowPatterns.add(windowPatternToAdd);
    }

}
