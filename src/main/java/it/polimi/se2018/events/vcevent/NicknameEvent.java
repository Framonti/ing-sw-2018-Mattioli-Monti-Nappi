package it.polimi.se2018.events.vcevent;

public class NicknameEvent extends VCEvent {

    private String nickname;
    private boolean firstTime;

    public NicknameEvent(String nickname, boolean firstTime){

        super(20);
        this.nickname = nickname;
        this.firstTime = firstTime;
    }

    public String getNickname() {

        return nickname;
    }

    public boolean isFirstTime() {
        return firstTime;
    }
}
