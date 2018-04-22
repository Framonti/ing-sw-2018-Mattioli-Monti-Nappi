package it.polimi.se2018;

public class Player {

    //Attributes (the ones commented don't have a class already)
    String name;
    int score;
    int favorTokensNumber;
    //ScoreMarker scoreMarker;
    Window window;
    // PrivateObjectiveCard privateObjectiveCard;

    //Constructor
    public Player(String name /*, ScoreMarker scoreMarker, PrivateObjectCard privateObjectiveCard*/) {
        this.name = name;
        this.score = 0;
        //this.scoreMarker = scoreMarker;
        //this.privateObjectiveCard = privateObjectiveCard;
    }

    //getters and setters
    public void setScore(int score) { this.score = score; }

    public int getScore() { return score; }

    public String getName() { return name; }

    //it is called when the player uses a ToolCard, this decreases the number of the favor tokens by cost
    public void reduceFavorTokens(int cost) { favorTokensNumber -= cost; }

    /*probably will be removed
        public Window returnWindow(WindowFrame windowFrame, WindowPattern windowPattern) {}
    */
    
    //complex methods must be implemented
}
