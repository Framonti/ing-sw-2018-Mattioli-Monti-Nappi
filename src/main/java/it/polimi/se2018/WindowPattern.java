package it.polimi.se2018;

public class WindowPattern {

    private String name;
    private int difficultyNumber;
    private Dice[][] windowPattern;

    //Constructor
    public WindowPattern(String name, int difficultyNumber, Dice[][] windowPattern) {
        this.name = name;
        this.difficultyNumber = difficultyNumber;
        this.windowPattern = windowPattern;
    }

    //controlls if the dice respects the colour and value restrictions of the pattern
    public boolean checkCell(Position position, Dice dice) {
        return (windowPattern[position.getX()][position.getY()].getColour() == null || windowPattern[position.getX()][position.getY()].getColour().equals(dice.getColour())) && (windowPattern[position.getX()][position.getY()].getValue() == 0 || windowPattern[position.getX()][position.getY()].getValue() == dice.getValue());
    }

    public String getName() {
        return name;
    }

    public int getDifficultyNumber() {
        return difficultyNumber;
    }

    //prints the pattern with its restrictions, "o" is displayed when there is none
    public void show() {
        int row;
        int column;
        for (row = 0; row < 4; row++) {
            for (column = 0; column < 5; column++) {
                if (windowPattern[row][column] == null) {
                    System.out.print("O\t");
                } else if (windowPattern[row][column].getColour() != null) {
                    System.out.printf("%s\t", windowPattern[row][column].getColour().getAbbreviation());
                } else if (windowPattern[row][column].getValue() != 0) {
                    System.out.printf("%d\t", windowPattern[row][column].getValue());
                }
            }
            System.out.print("\n");
        }
    }
}
