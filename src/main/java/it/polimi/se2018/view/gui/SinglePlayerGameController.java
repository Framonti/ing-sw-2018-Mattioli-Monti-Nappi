package it.polimi.se2018.view.gui;

import it.polimi.se2018.events.mvevent.*;
import it.polimi.se2018.events.vcevent.DiceChosenSinglePlayer;
import it.polimi.se2018.events.vcevent.GlazingHammerEvent;
import it.polimi.se2018.events.vcevent.SkipTurnEvent;
import it.polimi.se2018.events.vcevent.VCEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class SinglePlayerGameController extends GameControllerAbstract implements Observer{

    @FXML private ImageView privateObjectiveCard1;
    @FXML private ImageView privateObjectiveCard2;

    @FXML private ImageView toolCard1;
    @FXML private ImageView toolCard2;
    @FXML private ImageView toolCard3;
    @FXML private ImageView toolCard4;
    @FXML private ImageView toolCard5;

    private int idToolCard1;
    private int idToolCard2;
    private int idToolCard3;
    private int idToolCard4;
    private int idToolCard5;

    private boolean dicePaid = false;

    private int toolCardStillAvaiable;


    @FXML
    public void initialize(){

        toolCardImageList.add(toolCard1);
        toolCardImageList.add(toolCard2);
        toolCardImageList.add(toolCard3);
        toolCardImageList.add(toolCard4);
        toolCardImageList.add(toolCard5);

        abstractInitialize();

        privateObjectiveCard1.setOnMouseEntered(event -> zoomInPOC(privateObjectiveCard1));
        privateObjectiveCard1.setOnMouseExited(event -> zoomOutPOC(privateObjectiveCard1));
        privateObjectiveCard2.setOnMouseEntered(event -> zoomInPOC(privateObjectiveCard2));
        privateObjectiveCard2.setOnMouseExited(event -> zoomOutPOC(privateObjectiveCard2));

    }

    @Override
    void skipTurn() {

        dicePaid = false;
        idToolCardSelected = 0;
        disableToolCards();
        disableDraftPool();
        skipTurnButton.setDisable(true);
        turnLabel.setStyle("-fx-background-color: #ff0000;");
        turnLabel.setText( "Attendi");
        disableGridPane(dicePatternGridPane1,windowPattern1);
        skipTurnButton.setDisable(true);
        for(ImageView toolCardImage : toolCardImageList)
            toolCardImage.setEffect(null);
        setChanged();
        notifyObservers(new SkipTurnEvent());
    }

    @Override
    void playerSuspended() {
        //senso?
    }

    @Override
    void createAssociationWithOurGridPane(MVEvent mvEvent1, MVEvent mvEvent2) {

        SetWindowPatternsGUIEvent setWindowPatternsGUIEvent = (SetWindowPatternsGUIEvent) mvEvent1;

        List<String> windowPatterns = setWindowPatternsGUIEvent.getWindowPatternsGUI();
        DicePatternEvent dicePatternEvent = (DicePatternEvent) mvEvent2;
        String player = dicePatternEvent.getPlayerNames().get(0);

        ourGridPane1 = new OurGridPane(dicePatternGridPane1,player,windowPatterns.get(0),player1NameLabel);
        addImageToImageView(windowPatterns.get(0), windowPattern1,460, 520);
        ourGridPaneList.add(ourGridPane1);
    }

    @Override
    void updatePrivateObjectiveCards(List<String> paths) {

        addImageToImageView(paths.get(0),privateObjectiveCard1,144,95);
        addImageToImageView(paths.get(1),privateObjectiveCard2,144,95);
    }

    @Override
    void showError(MVEvent event) {
        ErrorEvent errorEvent = (ErrorEvent) event;
        if(errorEvent.getMessageToDisplay().equals("Non hai abbastanza segnalini favore\n")) {
            dicePaid = false;
            idToolCardSelected = 0;
            Platform.runLater(()-> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setContentText("Il dado che hai selezionato per pagare la carta utensile non era corretto");
                alert.showAndWait();
            });
        }
        else showErrorAbstract(errorEvent);

    }

    @Override
    void updateDicePatterns(MVEvent event) {
        DicePatternEvent dicePatternEvent = (DicePatternEvent) event;
        updateDicePattern(dicePatternEvent.getDicePatternsGUI().get(0), ourGridPane1.getGridPane(), 59,69);
    }

    @Override
    void updateFavorTokens(MVEvent mvEvent) {
        //The singlePlayerGameController doesn't update Favor Tokens
    }

    @Override
    void handleDraftPoolAndToolCards() {
        super.handleDraftPoolAndToolCards();
        dicePaid = false;
    }

    private void setToolCardEventHandler(int size){

        if(size>0) {
            toolCard1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> useToolCard1());
            if(size >1) {
                toolCard2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> useToolCard2());
                if(size > 2){
                    toolCard3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> useToolCard3());
                    if(size > 3){
                        toolCard4.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> useToolCard4());
                        if(size>4) {
                            toolCard5.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> useToolCard5());
                        }
                    }

                }
            }
        }
    }

    void handleToolCards(int idToolCard){
        disableToolCards();
        if (idToolCard >= 2 && idToolCard <= 4) {
            disableDraftPool();
            enableGridPane(dicePatternGridPane1, windowPattern1);
        } else if (idToolCard == 12) {
            disableDraftPool();
            disableGridPane(dicePatternGridPane1, windowPattern1);
            enableGridPane(roundTrackGridPane, roundTrack);
        } else if (idToolCard == 7) {
            VCEvent event = new GlazingHammerEvent();
            setChanged();
            notifyObservers(event);
            idToolCardSelected = 0;
        } else {
            enableDraftPool();
            disableGridPane(dicePatternGridPane1, windowPattern1);
        }
    }

    private void payDice(){

        diceChosenFromDraftPool.setDisable(true);
        diceChosenFromDraftPool.setOpacity(0.5);
        setChanged();
        notifyObservers(new DiceChosenSinglePlayer(Integer.toString(diceIndexDraftPool+1)));
        dicePaid = true;
        disableDraftPool();
        handleToolCards(idToolCardSelected);
    }

    private void useToolCard1() {
        toolCard1.setEffect(setBorderGlow());
        toolCardSelected = toolCard1;
        idToolCardSelected = idToolCard1;
        System.out.println("hai selezionato la tool card" + idToolCardSelected);
        handleToolCards(idToolCardSelected);
        disableToolCards();
        disableGridPane(roundTrackGridPane, roundTrack);
        disableGridPane(dicePatternGridPane1, windowPattern1);
        enableDraftPool();
    }

    private void useToolCard2() {
        toolCard2.setEffect(setBorderGlow());
        toolCardSelected = toolCard2;
        idToolCardSelected = idToolCard2;
        System.out.println("hai selezionato la tool card" + idToolCardSelected);
       // handleToolCards(idToolCardSelected);
        enableDraftPool();
    }

    private void useToolCard3() {
        toolCard3.setEffect(setBorderGlow());
        toolCardSelected = toolCard3;
        idToolCardSelected = idToolCard3;
        System.out.println("hai selezionato la tool card" + idToolCardSelected);
       // handleToolCards(idToolCardSelected);
        enableDraftPool();
    }

    private void useToolCard4(){

        toolCard4.setEffect(setBorderGlow());
        toolCardSelected = toolCard4;
        idToolCardSelected = idToolCard4;
        System.out.println("hai selezionato la tool card" + idToolCardSelected);
       // handleToolCards(idToolCardSelected);
        enableDraftPool();
    }

    private void useToolCard5(){

        toolCard5.setEffect(setBorderGlow());
        toolCardSelected = toolCard5;
        idToolCardSelected = idToolCard5;
        System.out.println("hai selezionato la tool card" + idToolCardSelected);
      //  handleToolCards(idToolCardSelected);
        enableDraftPool();
    }

    void getDiceIndexFromDraftPool(ImageView imageView) {

        System.out.println("sono nella draftpool");
        imageView.setEffect(setBorderGlow());
        diceChosenFromDraftPool = imageView;
        diceIndexDraftPool = GridPane.getColumnIndex(imageView);
        System.out.println(diceIndexDraftPool+1);

        if(!dicePaid && idToolCardSelected != 0)
            payDice();
        else diceIndexMap.get(idToolCardSelected).run();

    }

    @Override
    void updateToolCards(MVEvent event) {

        if(!imageViewsSetup) {

            ToolCardEvent toolCardEvent = (ToolCardEvent) event;
            List<String> toolCardsGUI = toolCardEvent.getToolCardsGUI();

            setToolCardEventHandler(toolCardsGUI.size());

            if (!toolCardsGUI.isEmpty()) {
                addImageToImageView(toolCardsGUI.get(0), toolCard1, 144, 95);
                idToolCard1 = Integer.parseInt(toolCardsGUI.get(1));
                if (toolCardsGUI.size() > 2) {
                    addImageToImageView(toolCardsGUI.get(2), toolCard2, 144, 95);
                    idToolCard2 = Integer.parseInt(toolCardsGUI.get(3));
                    if (toolCardsGUI.size() > 4) {
                        addImageToImageView(toolCardsGUI.get(4), toolCard3, 144, 95);
                        idToolCard3 = Integer.parseInt(toolCardsGUI.get(5));
                        if (toolCardsGUI.size() > 6) {
                            addImageToImageView(toolCardsGUI.get(6), toolCard4, 144, 95);
                            idToolCard4 = Integer.parseInt(toolCardsGUI.get(7));
                            if (toolCardsGUI.size() > 8) {
                                addImageToImageView(toolCardsGUI.get(8), toolCard5, 144, 95);
                                idToolCard5 = Integer.parseInt(toolCardsGUI.get(9));
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    void updatePublicObjectiveCards(List<String> publicObjectiveCards) {

        addImageToImageView(publicObjectiveCards.get(0), publicObjectiveCard1, 144, 95);
        addImageToImageView(publicObjectiveCards.get(1), publicObjectiveCard2, 144, 95);
    }

    @Override
    public void update(Observable o, Object event) {

        mvEvent = (MVEvent) event;
        mvEvents.get(mvEvent.getId()).run();
    }
}
