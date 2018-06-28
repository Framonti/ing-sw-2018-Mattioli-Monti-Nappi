package it.polimi.se2018.view.gui;

import it.polimi.se2018.events.mvevent.*;
import it.polimi.se2018.events.vcevent.DiceChosenSinglePlayer;
import it.polimi.se2018.events.vcevent.GlazingHammerEvent;
import it.polimi.se2018.events.vcevent.SkipTurnEvent;
import it.polimi.se2018.events.vcevent.VCEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
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

    private List <ImageView> toolCardsUsed = new ArrayList<>();
    private ImageView diceChosenToPay;


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
        if(diceChosenToPay != null) {
            diceChosenToPay.setDisable(false);
            diceChosenToPay.setEffect(null);
            diceChosenToPay = null;
        }
        if(idToolCardSelected != 0)
            toolCardsUsed.remove(toolCardSelected);
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
        /*idToolCardSelected = 0;
        dicePaid = false;
        toolCardsUsed.remove(toolCardSelected);
        toolCardSelected.setEffect(null);
        toolCardSelected.setDisable(false);
        toolCardSelected.setOpacity(1);
        diceChosenToPay.setEffect(null);
        diceChosenToPay.setOpacity(1);
        diceChosenToPay.setDisable(false);
        diceChosenToPay = null;*/
        showErrorAbstract(errorEvent);
        handleDraftPoolAndToolCards();

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
        if(idToolCardSelected != 0) {
            toolCardsUsed.remove(toolCardSelected);
            toolCardSelected.setOpacity(1);
            toolCardSelected.setDisable(false);
            idToolCardSelected = 0;
            if(diceChosenToPay != null) {
                diceChosenToPay.setOpacity(1);
                diceChosenToPay.setEffect(null);
                diceChosenToPay.setDisable(false);
                diceChosenToPay = null;
                dicePaid = false;
            }

        }


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
        System.out.println("sono nel nuovo handle tool cards");
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
            dicePaid = false;
        } else {
            enableDraftPool();
            disableGridPane(dicePatternGridPane1, windowPattern1);
        }
    }

    private void payDice(){

        diceChosenToPay.setDisable(true);
        diceChosenToPay.setOpacity(0.5);
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
        toolCardsUsed.add(toolCard1);
        System.out.println("hai selezionato la tool card" + idToolCardSelected);
        disableToolCards();
        disableGridPane(roundTrackGridPane, roundTrack);
        disableGridPane(dicePatternGridPane1, windowPattern1);
        enableDraftPool();
    }

    private void useToolCard2() {
        toolCard2.setEffect(setBorderGlow());
        toolCardSelected = toolCard2;
        idToolCardSelected = idToolCard2;
        toolCardsUsed.add(toolCard2);
        System.out.println("hai selezionato la tool card" + idToolCardSelected);
       // handleToolCards(idToolCardSelected);
        disableToolCards();
        disableGridPane(roundTrackGridPane, roundTrack);
        disableGridPane(dicePatternGridPane1, windowPattern1);
        enableDraftPool();
    }

    private void useToolCard3() {
        toolCard3.setEffect(setBorderGlow());
        toolCardSelected = toolCard3;
        idToolCardSelected = idToolCard3;
        toolCardsUsed.add(toolCard3);
        System.out.println("hai selezionato la tool card" + idToolCardSelected);
       // handleToolCards(idToolCardSelected);
        disableToolCards();
        disableGridPane(roundTrackGridPane, roundTrack);
        disableGridPane(dicePatternGridPane1, windowPattern1);
        enableDraftPool();
    }

    private void useToolCard4(){

        toolCard4.setEffect(setBorderGlow());
        toolCardSelected = toolCard4;
        idToolCardSelected = idToolCard4;
        toolCardsUsed.add(toolCard4);
        System.out.println("hai selezionato la tool card" + idToolCardSelected);
       // handleToolCards(idToolCardSelected);
        disableToolCards();
        disableGridPane(roundTrackGridPane, roundTrack);
        disableGridPane(dicePatternGridPane1, windowPattern1);
        enableDraftPool();
    }

    private void useToolCard5(){

        toolCard5.setEffect(setBorderGlow());
        toolCardSelected = toolCard5;
        idToolCardSelected = idToolCard5;
        toolCardsUsed.add(toolCard5);
        System.out.println("hai selezionato la tool card" + idToolCardSelected);
      //  handleToolCards(idToolCardSelected);
        disableToolCards();
        disableGridPane(roundTrackGridPane, roundTrack);
        disableGridPane(dicePatternGridPane1, windowPattern1);
        enableDraftPool();
    }

    void getDiceIndexFromDraftPool(ImageView imageView) {

        System.out.println("sono nella draftpool nuova");
        imageView.setEffect(setBorderGlow());
        //diceChosenFromDraftPool = imageView;
        diceIndexDraftPool = GridPane.getColumnIndex(imageView);
        System.out.println(diceIndexDraftPool+1);

        if(!dicePaid && idToolCardSelected != 0){
            diceChosenToPay = imageView;
            payDice();
        }

        else {
            diceChosenFromDraftPool = imageView;
            diceIndexMap.get(idToolCardSelected).run();
        }

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
    void enableToolCards(){
        for(ImageView toolCardImage : toolCardImageList) {
            if(!toolCardsUsed.contains(toolCardImage)) {
                toolCardImage.setDisable(false);
                toolCardImage.setOpacity(1);
            }
        }
    }

    @Override
    void enableDraftPool(){
        super.enableDraftPool();
        if(diceChosenToPay == null)
            System.out.println("sono nel ramo di enable draft pool quando non hai scelto un dado con cui pagare");
        if(diceChosenToPay != null) {
            System.out.println("sono nel ramo di enable draft pool quando hai scelto un dado con cui pagare");
            for(Node node : draftPool.getChildren()){
                if(node instanceof ImageView) {
                    ImageView img = (ImageView) node;
                    if (img == diceChosenToPay) {
                        node.setDisable(true);
                        node.setOpacity(0.5);
                    }
                }
            }
        }
    }


    /**
     * Creates a Map associating the id of a toolCard with a method
     */
    @Override
    void createDiceIndexMap(){

        diceIndexMap.put(0, this :: putDiceOnDicePattern);

        diceIndexMap.put(1, () -> {
            grozinPliers();
            dicePaid = false;
        });
        diceIndexMap.put(2, () -> {});
        diceIndexMap.put(3, () -> {});
        diceIndexMap.put(4, () -> {});
        diceIndexMap.put(5, () ->{
            disableDraftPool();
            enableGridPane(roundTrackGridPane,roundTrack);});
        diceIndexMap.put(6, this::fluxBrushChooseDice);
        diceIndexMap.put(7, () -> {});
        diceIndexMap.put(8, () -> {
            enableGridPane(dicePatternGridPane1,windowPattern1);
            disableDraftPool();});
        diceIndexMap.put(9, () -> {
            enableGridPane(dicePatternGridPane1,windowPattern1);
            disableDraftPool();});
        diceIndexMap.put(10, () -> {
            grindingStone();
            dicePaid = false;
        });
        diceIndexMap.put(11, this::fluxRemoverChooseDice);
        diceIndexMap.put(12, () -> {});

    }


    /**
     * Creates a map associating a toolCard ID with a method
     */
    @Override
    void createDicePositionFromDicePatternMap(){

        dicePositionFromDicePatternMap.put(0, () -> placeDiceMove(dicePatternRowPosition, dicePatternColumnPosition));
        dicePositionFromDicePatternMap.put(1, ()->{});
        dicePositionFromDicePatternMap.put(2, () -> {
            eglomiseBrush(dicePatternRowPosition, dicePatternColumnPosition);
            dicePaid = false;
        });
        dicePositionFromDicePatternMap.put(3, () -> {
            copperFoilBurnisher(dicePatternRowPosition, dicePatternColumnPosition);
            dicePaid = false;
        });
        dicePositionFromDicePatternMap.put(4, () -> {
            lathekin(dicePatternRowPosition, dicePatternColumnPosition);
            dicePaid = false;
        });
        dicePositionFromDicePatternMap.put(5, () -> {});
        dicePositionFromDicePatternMap.put(6, () -> {
            fluxBrushPlaceDice(dicePatternRowPosition, dicePatternColumnPosition);
            dicePaid = false;
        });
        dicePositionFromDicePatternMap.put(7, () -> {});
        dicePositionFromDicePatternMap.put(8, () -> {
            runnerPliers(dicePatternRowPosition, dicePatternColumnPosition);
            dicePaid = false;
        });
        dicePositionFromDicePatternMap.put(9, () -> {
            corkBackedStraighedge(dicePatternRowPosition, dicePatternColumnPosition);
            dicePaid = false;
        });
        dicePositionFromDicePatternMap.put(10, () ->{});
        dicePositionFromDicePatternMap.put(11, () -> {
            fluxRemoverPlaceDice(dicePatternRowPosition, dicePatternColumnPosition);
            dicePaid = false;
        });
        dicePositionFromDicePatternMap.put(12, () -> {
            tapWheel(dicePatternRowPosition, dicePatternColumnPosition);
            dicePaid = false;
        });
    }

    @Override
    void getDicePositionFromRoundTrack(ImageView imageView){
        super.getDicePositionFromRoundTrack(imageView);
        if(idToolCardSelected == 5)
            dicePaid = false;
    }


    @Override
    void updatePublicObjectiveCards(List<String> publicObjectiveCards) {

        addImageToImageView(publicObjectiveCards.get(0), publicObjectiveCard1, 144, 95);
        addImageToImageView(publicObjectiveCards.get(1), publicObjectiveCard2, 144, 95);
    }

    @Override
    public void update(Observable o, Object event) {
        if(event instanceof Integer)
            turnDuration = (int)event;
        else{
            mvEvent = (MVEvent) event;
            mvEvents.get(mvEvent.getId()).run();}
    }
}
