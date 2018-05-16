package it.polimi.se2018.events;

public class RunnerPliersEvent {

    //Just creates a new PlaceDiceEvent, the only difference is that the controller must setLap(1)
    public RunnerPliersEvent(String userInput) {
        new PlaceDiceEvent(userInput);
    }
}
