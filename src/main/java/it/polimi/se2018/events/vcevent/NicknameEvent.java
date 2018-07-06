package it.polimi.se2018.events.vcevent;

/**
 * This event is generated when a player inserts a nickname while trying to connecting to the server
 * @author Framonti
 */
public class NicknameEvent extends VCEvent {

    private String nickname;
    private boolean firstTime;

    /**
     * Constructor
     * @param nickname The chosen nickname; a user will be know with this name during the game
     * @param firstTime True if the client is connecting with the server for the first time, false otherwise
     */
    public NicknameEvent(String nickname, boolean firstTime){

        super(20);
        this.nickname = nickname;
        this.firstTime = firstTime;
    }

    /**
     * Gets the nickname
     * @return The nickname
     */
    public String getNickname() {

        return nickname;
    }

    /**
     * Gets the firstTime attribute
     * @return The firstTime attribute
     */
    public boolean isFirstTime() {
        return firstTime;
    }
}
