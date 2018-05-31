package it.polimi.se2018.events.vcevent;

public class NicknameEvent extends VCEvent {

    private String nickname;

    public NicknameEvent(String nickname){

        super(20);
        this.nickname = nickname;
    }

    public String getNickname() {

        return nickname;
    }
}
