package it.polimi.se2018.events.mvevent;

/**
 * This event holds information about favor tokens number of a player
 * @author Daniele Mattioli
 */
public class FavorTokensEvent extends MVEvent {

    private String favorTokensNumber;
    private String player;

    /**
     * Constructor of the class
     * @param favorTokensNumber String representation of player's favor tokens number
     */
    public FavorTokensEvent(String favorTokensNumber, String player){
        super(8);
        this.favorTokensNumber = favorTokensNumber;
        this. player = player;
    }

    /**
     * Gets favorTokensNumber attribute
     * @return favorTokensNumber sttribute
     */
    public String getFavorTokensNumberString(){
        return this.favorTokensNumber;
    }

    /**
     * Gets player attribute
     * @return Player attribute
     */
    public String getPlayer() {
        return player;
    }

    public String getPlayerAndFavorTokens(){
        return getPlayer()+" "+getFavorTokensNumberString();
    }
}
