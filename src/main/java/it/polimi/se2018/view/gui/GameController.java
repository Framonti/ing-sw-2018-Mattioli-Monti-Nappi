package it.polimi.se2018.view.gui;

//TODO: mappa VC, pulsante per annullare mossa fatta
import it.polimi.se2018.events.mvevent.*;
import it.polimi.se2018.events.vcevent.*;
import it.polimi.se2018.model.Position;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.*;

public class GameController extends GameControllerAbstract implements Observer {

    @FXML private ImageView toolCard1;
    @FXML private ImageView toolCard2;
    @FXML private ImageView toolCard3;

    @FXML private ImageView privateObjectiveCard;

    @FXML private ImageView windowPattern4;
    @FXML private ImageView windowPattern3;
    @FXML private ImageView windowPattern2;

    @FXML private GridPane dicePatternGridPane4;
    @FXML private GridPane dicePatternGridPane3;
    @FXML private GridPane dicePatternGridPane2;

    @FXML private Label toolCard1FavorTokensLabel;
    @FXML private Label toolCard2FavorTokensLabel;
    @FXML private Label toolCard3FavorTokensLabel;

    @FXML private Label player1NameLabel;
    @FXML private Label player2NameLabel;
    @FXML private Label player3NameLabel;
    @FXML private Label player4NameLabel;

    @FXML private ImageView favorTokensToolCard1ImageView;
    @FXML private ImageView favorTokensToolCard2ImageView;
    @FXML private ImageView favorTokensToolCard3ImageView;

    @FXML private ImageView favorTokensPlayer1ImageView;
    @FXML private ImageView favorTokensPlayer2ImageView;
    @FXML private ImageView favorTokensPlayer3ImageView;
    @FXML private ImageView favorTokensPlayer4ImageView;

    @FXML private Label favorTokensPlayer1Label;
    @FXML private Label favorTokensPlayer2Label;
    @FXML private Label favorTokensPlayer3Label;
    @FXML private Label favorTokensPlayer4Label;

    private Position dicePatternPosition;

    private VCEvent vcEvent;

    //private String eventParameters = "";

    private Map<Integer, Runnable> vcEvents = new HashMap<>();
    private ImageView tmpImageView;

    private int idToolCard1;
    private int idToolCard2;
    private int idToolCard3;

    private OurGridPane ourGridPane2;
    private OurGridPane ourGridPane3;
    private OurGridPane ourGridPane4;

    private final String favorTokensPath = "src/main/Images/Others/FavorToken.png";


    private ImageView diceChosenFromDicePattern;


    @FXML
    void initialize() {

        toolCardImageList.add(toolCard1);
        toolCardImageList.add(toolCard2);
        toolCardImageList.add(toolCard3);

        abstractInitialize();

        initializeGridPaneImagesView(dicePatternGridPane2, 4, 5, 29, 39);
        initializeGridPaneImagesView(dicePatternGridPane3, 4, 5, 29, 39);
        initializeGridPaneImagesView(dicePatternGridPane4, 4, 5, 29, 39);

        createMVMap();
        //createVCMap();

        favorTokensPlayer1ImageView.setImage(new Image(ViewGUI.getUrlFromPath(favorTokensPath)));
        favorTokensToolCard1ImageView.setImage(new Image(ViewGUI.getUrlFromPath(favorTokensPath)));
        favorTokensToolCard2ImageView.setImage(new Image(ViewGUI.getUrlFromPath(favorTokensPath)));
        favorTokensToolCard3ImageView.setImage(new Image(ViewGUI.getUrlFromPath(favorTokensPath)));

        Image background = new Image(ViewGUI.getUrlFromPath("src/main/Images/Others/sagradaBackground.png"));
        BackgroundImage myBGI = new BackgroundImage(background, null, null,null, null);
        pane.setBackground(new Background(myBGI));

        privateObjectiveCard.setOnMouseEntered(event -> zoomIn(privateObjectiveCard));
        privateObjectiveCard.setOnMouseExited(event -> zoomOut(privateObjectiveCard));

        toolCard1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> useToolCard1());
        toolCard2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> useToolCard2());
        toolCard3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> useToolCard3());

        initializeDicePatternEventHandler(dicePatternGridPane1);
        initializeDraftPoolEventHandler(draftPool);

    }

    private void initializeDraftPoolEventHandler(GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof ImageView) {
                ImageView tmp = (ImageView) node;
                tmp.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> getDiceIndexFromDraftPool(tmp));
            }
        }
    }

    private void initializeDicePatternEventHandler(GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof ImageView) {
                ImageView tmp = (ImageView) node;
                tmp.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> getDicePositionFromDicePattern(tmp));
            }
        }
    }

    //l'evento che passa le toolcard, oltre al path passa anche l'id delle toolcard, così posso usare la mappa e lanciare il metodo corretto
    private void useToolCard1() {
        toolCard1.setEffect(setBorderGlow());
        toolCardSelected = toolCard1;
        idToolCardSelected = idToolCard1;
        System.out.println("hai selezionato la tool card" + idToolCardSelected);
        handleToolCards(idToolCardSelected);
    }

    private void useToolCard2() {
        toolCard2.setEffect(setBorderGlow());
        toolCardSelected = toolCard2;
        idToolCardSelected = idToolCard2;
        System.out.println("hai selezionato la tool card" + idToolCardSelected);
        handleToolCards(idToolCardSelected);
    }

    private void useToolCard3() {
        toolCard3.setEffect(setBorderGlow());
        toolCardSelected = toolCard3;
        idToolCardSelected = idToolCard3;
        System.out.println("hai selezionato la tool card" + idToolCardSelected);
        handleToolCards(idToolCardSelected);

    }


    private void handleToolCards(int idToolCard){
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

    @Override
    public void update(Observable o, Object event) {
        mvEvent = (MVEvent) event;
        mvEvents.get(mvEvent.getId()).run();
    }

    private void createMVMap() {
        createMVMapAbstract();
       // mvEvents.put(-1, ()-> {});
       //    mvEvents.put(1, ()-> updateDicePatterns(mvEvent));
       // mvEvents.put(2, ()-> updateDraftPool(mvEvent));
           mvEvents.put(3, ()-> updateToolCards(mvEvent));
       // mvEvents.put(4, ()-> updateRoundTrack(mvEvent));
       // mvEvents.put(5, ()-> {});
       // mvEvents.put(6, ()->handleActionMenu(mvEvent));
       // mvEvents.put(7, ()-> showAll(mvEvent));
        //mvEvents.put(8, ()-> updateFavorTokens(mvEvent));
        //  mvEvents.put(9, ()-> showError(mvEvent));
       // mvEvents.put(10, ()-> {});
       // mvEvents.put(11, () -> {});
         // mvEvents.put(12, this::handleEndTurnEvent);
        //mvEvents.put(13, ()-> {});
       // mvEvents.put(14, ()-> {});
       // mvEvents.put(15, ()->{});
       //  mvEvents.put(16, this::playerSuspended);
    }


    void playerSuspended(){
        Platform.runLater(() -> {
            Stage window = new Stage();
            //Blocks interaction with the caller Scene
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Tempo scaduto");
            window.setMinWidth(400);
            window.setMinHeight(225);
            window.setResizable(false);

            VBox vBox = new VBox(5);
            HBox hBox = new HBox(5);

            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(30);
            vBox.setAlignment(Pos.CENTER);
            vBox.setSpacing(20);
            //Sets the message and the button
            Label label = new Label("Sei stato sospeso. Premi il pulsante per riconnetterti");
            Button button = new Button("Riconnetti");
            //Adds everything to the layouts
            vBox.getChildren().addAll(label, hBox);
            hBox.getChildren().add(button);

            turnLabel.setStyle("-fx-background-color: #ff0000;");
            turnLabel.setText(waitText);

            button.setOnAction(event -> {
                UnsuspendEvent unsuspendEvent = new UnsuspendEvent(null);
                setChanged();
                notifyObservers(unsuspendEvent);
                window.close();
            });

            Scene scene = new Scene(vBox);
            window.setScene(scene);
            window.showAndWait();
        });
    }

    /*private void createVCMap() {
        //vcEvents.put(-1, () -> vcEvent = new WindowPatternChoiceEvent(eventParameters));
        vcEvents.put(1, () -> vcEvent = new GrozingPliersEvent(eventParameters));
        vcEvents.put(2, () -> vcEvent = new EglomiseBrushEvent(eventParameters));
        vcEvents.put(3, () -> vcEvent = new CopperFoilBurnisherEvent(eventParameters));
        vcEvents.put(4, () -> vcEvent = new LathekinEvent(eventParameters));
        vcEvents.put(5, () -> vcEvent = new LensCutterEvent(eventParameters));
        vcEvents.put(6, () -> vcEvent = new FluxBrushChooseDiceEvent(eventParameters));
        vcEvents.put(7, () -> vcEvent = new GlazingHammerEvent());                       //può non stare nella mappa
        vcEvents.put(8, () -> vcEvent = new RunnerPliersEvent(eventParameters));
        vcEvents.put(9, () -> vcEvent = new CorkBakedStraightedgeEvent(eventParameters));
        vcEvents.put(10, () -> vcEvent = new GrindingStoneEvent(eventParameters));
        vcEvents.put(11, () -> vcEvent = new FluxRemoverChooseDiceEvent(eventParameters));
        vcEvents.put(12, () -> vcEvent = new TapWheelEvent(eventParameters));
        vcEvents.put(13, () -> vcEvent = new FluxBrushPlaceDiceEvent(eventParameters));  //può non stare nella mappa
        vcEvents.put(14, ()-> vcEvent = new FluxRemoverPlaceDiceEvent(eventParameters)); //può non stare nella mappa
    }*/


    /*void showAll(MVEvent event){
        ShowAllEvent showAllEvent = (ShowAllEvent) event;
        if (!imageViewsSetup) {
            updateDraftPool(showAllEvent.getDraftPool());
            updateToolCards(showAllEvent.getToolCards());
            createAssociationWithOurGridPane(showAllEvent.getSetWindowPatternsGUIEvent(),showAllEvent.getDicePatterns());
            updateRoundTrack(showAllEvent.getRoundTrack());
            updatePublicObjectiveCards(showAllEvent.getPublicObjectiveCardsGUI());
            updatePrivateObjectiveCards(showAllEvent.getPrivateObjectiveCardsStringGUI());
            imageViewsSetup = true;
            disableDraftPool();
            disableToolCards();
        }
        else{
            turnLabel.setStyle("-fx-background-color: #00ff19;");
            Platform.runLater(()-> turnLabel.setText(yourTurnText));
            updateDraftPool(showAllEvent.getDraftPool());
            updateRoundTrack(showAllEvent.getRoundTrack());
            updateToolCards(showAllEvent.getToolCards());
            enableDraftPool();
            enableToolCards();
            skipTurnButton.setDisable(false);
            deleteMoveButton.setDisable(false);

        }
    }*/


    private int getCurrentPlayerIndex(List<String> players, String currentPlayer){
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).equals(currentPlayer))
                return i;
        }
        return -1;
    }

    private List<Integer> getRemainingPositions(List<String> players, int index){
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < players.size(); i++){
            if(i != index)
                list.add(i);
        }
        return list;
    }


    void createAssociationWithOurGridPane(MVEvent event1, MVEvent event2){

        SetWindowPatternsGUIEvent setWindowPatternsGUIEvent = (SetWindowPatternsGUIEvent) event1;

        List<String> windowPatterns = setWindowPatternsGUIEvent.getWindowPatternsGUI();
        List<String> favorTokensPlayers = setWindowPatternsGUIEvent.getFavorTokensNumber();
        DicePatternEvent dicePatternEvent = (DicePatternEvent) event2;
        List <String> players = dicePatternEvent.getPlayerNames();
        String currentPlayer = dicePatternEvent.getCurrentPlayer();
        List<List<String>> dicePatternGUI = dicePatternEvent.getDicePatternsGUI();

        int currentPlayerIndex = getCurrentPlayerIndex(players,currentPlayer);
        ourGridPane1 = new OurGridPane(dicePatternGridPane1,currentPlayer,windowPatterns.get(currentPlayerIndex),player1NameLabel,Integer.parseInt(favorTokensPlayers.get(currentPlayerIndex)),favorTokensPlayer1Label);
        addImageToImageView(windowPatterns.get(currentPlayerIndex), windowPattern1,460, 520);
        updateDicePattern(dicePatternGUI.get(currentPlayerIndex),dicePatternGridPane1,59,69);
        ourGridPaneList.add(ourGridPane1);
        List<Integer> list = getRemainingPositions(players,currentPlayerIndex);

        ourGridPane2 = new OurGridPane(dicePatternGridPane2,players.get(list.get(0)), windowPatterns.get(list.get(0)),player2NameLabel,Integer.parseInt(favorTokensPlayers.get(list.get(0))),favorTokensPlayer2Label);
        addImageToImageView(windowPatterns.get(list.get(0)), windowPattern2,180, 200);
        updateDicePattern(dicePatternGUI.get(0),dicePatternGridPane2,29,39);
        ourGridPaneList.add(ourGridPane2);
        favorTokensPlayer2ImageView.setImage(new Image(ViewGUI.getUrlFromPath(favorTokensPath)));

        if(list.size() > 1 ){
            ourGridPane3 = new OurGridPane(dicePatternGridPane3,players.get(list.get(1)), windowPatterns.get(list.get(1)),player3NameLabel,Integer.parseInt(favorTokensPlayers.get(list.get(1))),favorTokensPlayer3Label);
            addImageToImageView(windowPatterns.get(list.get(1)), windowPattern3,180, 200);
            updateDicePattern(dicePatternGUI.get(1),dicePatternGridPane3,29,39);
            ourGridPaneList.add(ourGridPane3);
            favorTokensPlayer3ImageView.setImage(new Image(ViewGUI.getUrlFromPath(favorTokensPath)));
        }
        if(list.size() > 2){
            ourGridPane4 = new OurGridPane(dicePatternGridPane4,players.get(list.get(2)), windowPatterns.get(list.get(2)),player4NameLabel,Integer.parseInt(favorTokensPlayers.get(list.get(2))),favorTokensPlayer4Label);
            addImageToImageView(windowPatterns.get(list.get(2)), windowPattern4,180, 200);
            updateDicePattern(dicePatternGUI.get(2),dicePatternGridPane4,29,39);
            ourGridPaneList.add(ourGridPane4);
            favorTokensPlayer4ImageView.setImage(new Image(ViewGUI.getUrlFromPath(favorTokensPath)));
        }
    }

    protected void skipTurn(){
        setChanged();
        notifyObservers(new SkipTurnEvent());
        super.skipTurnAbstract();
        idToolCardSelected = 0;
    }

    void showError(MVEvent event){
       ErrorEvent errorEvent = (ErrorEvent) event;
       showErrorAbstract(errorEvent);
        if(errorEvent.getMessageToDisplay().equals("Non hai abbastanza segnalini favore\n"))
            idToolCardSelected = 0;
    }

    void updatePrivateObjectiveCards(List<String> paths){
        addImageToImageView(paths.get(0),privateObjectiveCard,144,95);
    }

    void updateFavorTokens(MVEvent event){
        FavorTokensEvent favorTokensEvent = (FavorTokensEvent) event;
        int favorTokensNumber = Integer.parseInt(favorTokensEvent.getFavorTokensNumberString());
        for(OurGridPane ourGridPane : ourGridPaneList){
            if(ourGridPane.getPlayerName().equals(favorTokensEvent.getPlayer()))
                ourGridPane.setFavorTokensLabel(favorTokensNumber);
        }
    }

    void updateDicePatterns(MVEvent event) {
        DicePatternEvent dicePatternEvent = (DicePatternEvent) event;
        int currentPlayerIndex = getCurrentPlayerIndex(dicePatternEvent.getPlayerNames(), dicePatternEvent.getCurrentPlayer());
        GridPane tmp = getCurrentPlayerOurGridPane(dicePatternEvent.getCurrentPlayer()).getGridPane();
        if(tmp == dicePatternGridPane1)
            updateDicePattern(dicePatternEvent.getDicePatternsGUI().get(currentPlayerIndex), tmp, 59,69);
        else
            updateDicePattern(dicePatternEvent.getDicePatternsGUI().get(currentPlayerIndex), tmp, 29,39);
    }

    private OurGridPane getCurrentPlayerOurGridPane(String currentPlayer){
        for(OurGridPane ourGridPane : ourGridPaneList){
            if (ourGridPane.getPlayerName().equals(currentPlayer))
                return ourGridPane;

        }
        return ourGridPaneList.get(0); //non dovrei mai arrivare quì
    }

    private void updateDicePattern(List<String> list, GridPane gridPane, int height , int width){
        int column = 0;
        int row = 0;
        for (String path : list) {
            Object foundNode = getNodeByRowColumnIndex(row,column, gridPane,5);
            if (foundNode instanceof ImageView) {
                ImageView img = (ImageView) foundNode;
                addImageToImageView(path,img,height,width);
            }
            if(column != 4)
                column++;
            else{
                row++;
                column = 0;
            }

        }
    }


    void updateToolCards(MVEvent event){

        ToolCardEvent toolCardEvent = (ToolCardEvent) event;
        addImageToImageView(toolCardEvent.getToolCardsGUI().get(0),toolCard1,144,95);
        idToolCard1 = Integer.parseInt(toolCardEvent.getToolCardsGUI().get(1));

        addImageToImageView(toolCardEvent.getToolCardsGUI().get(2),toolCard2,144,95);
        idToolCard2 = Integer.parseInt(toolCardEvent.getToolCardsGUI().get(3));

        addImageToImageView(toolCardEvent.getToolCardsGUI().get(4),toolCard3,144,95);
        idToolCard3 = Integer.parseInt(toolCardEvent.getToolCardsGUI().get(5));

        Platform.runLater(() ->updateLabel(toolCard1FavorTokensLabel,toolCardEvent.getFavorTokensOnToolCards().get(0)));
        Platform.runLater(() ->updateLabel(toolCard2FavorTokensLabel,toolCardEvent.getFavorTokensOnToolCards().get(1)));
        Platform.runLater(() ->updateLabel(toolCard3FavorTokensLabel,toolCardEvent.getFavorTokensOnToolCards().get(2)));


    }

    private void updateLabel(Label label,String text){
        label.setText(text);
    }



    //tool card che interagiscono con la draft pool : 1, 5, 6, 8, 9, 10, 11
    //tool card che intragiscono con il dice pattern : 2, 3, 4, 6, 8, 9, 11, 12
    //tool card che interagiscono con il round track : 5, 12

    private void getDiceIndexFromDraftPool(ImageView imageView) {
        System.out.println("sono nella draftpool");
        imageView.setEffect(setBorderGlow());
        diceChosenFromDraftPool = imageView;
        diceIndexDraftPool = GridPane.getColumnIndex(imageView);
        System.out.println(diceIndexDraftPool+1);
        if (idToolCardSelected == 0 && !diceMoved) {
            disableToolCards();
            disableDraftPool();
            enableGridPane(dicePatternGridPane1,windowPattern1);
        } else if (idToolCardSelected == 1) {
            grozingPliersWindow();
            VCEvent event = new GrozingPliersEvent(Integer.toString(diceIndexDraftPool + 1) + " " + Integer.toString(choiceGrozingPliers));
            setChanged();
            notifyObservers(event);
            diceChosenFromDraftPool.setEffect(null);
            handleDraftPool();
            disableToolCards();
            idToolCardSelected = 0;
            toolCardSelected.setEffect(null);
        } else if (idToolCardSelected == 5) {
            disableDraftPool();
            enableGridPane(roundTrackGridPane,roundTrack);
        } else if (idToolCardSelected == 6) {
            VCEvent event = new FluxBrushChooseDiceEvent(Integer.toString(diceIndexDraftPool + 1));
            System.out.println("tool card6:" + Integer.toString(diceIndexDraftPool + 1));
            setChanged();
            notifyObservers(event);
            disableDraftPool();
            enableGridPane(dicePatternGridPane1,windowPattern1);
        }
        else if (idToolCardSelected == 8 || idToolCardSelected == 9){
            enableGridPane(dicePatternGridPane1,windowPattern1);
            disableDraftPool();
        } else if (idToolCardSelected == 10) {
            VCEvent event = new GrindingStoneEvent(Integer.toString(diceIndexDraftPool + 1));
            System.out.println("tool card 10:" + Integer.toString(diceIndexDraftPool + 1));
            setChanged();
            notifyObservers(event);
            diceChosenFromDraftPool.setEffect(null);
            handleDraftPool();
            idToolCardSelected = 0;
            disableToolCards();
            toolCardSelected.setEffect(null);
        } else if (idToolCardSelected == 11) {
            VCEvent event = new FluxRemoverChooseDiceEvent(Integer.toString(diceIndexDraftPool + 1));
            System.out.println("tool card 11:" + Integer.toString(diceIndexDraftPool + 1));
            setChanged();
            notifyObservers(event);
            diceChosenFromDraftPool.setEffect(setBorderGlow());
            fluxRemoverWindow();
            disableDraftPool();
            enableGridPane(dicePatternGridPane1,windowPattern1);
        }

    }


    private void placeDiceMove(int row, int column){

        dicePatternPosition = new Position(row, column);
        System.out.println("mossa su dice pattern:" + Integer.toString(diceIndexDraftPool+1) + " " + Integer.toString(dicePatternPosition.getX()) + " " + Integer.toString(dicePatternPosition.getY()));
        VCEvent event = new PlaceDiceEvent(Integer.toString(diceIndexDraftPool + 1) + " " + Integer.toString(dicePatternPosition.getX() + 1) + " " + Integer.toString(dicePatternPosition.getY() + 1));
        setChanged();
        notifyObservers(event);
        diceChosenFromDraftPool.setEffect(null);
        disableGridPane(dicePatternGridPane1, windowPattern1);
        handleToolCardsEffect();
    }

    private void eglomiseBrushSecondStep(int row, int column){

        finalPosition = new Position(row, column);
        step = 1;
        System.out.println("tool card 2 :" + Integer.toString(initialPosition.getX() + 1) + " " + Integer.toString(initialPosition.getY() + 1) + " " + Integer.toString(finalPosition.getX() + 1) + " " + Integer.toString(finalPosition.getY() + 1));
        VCEvent event = new EglomiseBrushEvent(Integer.toString(initialPosition.getX() + 1) + " " + Integer.toString(initialPosition.getY() + 1) + " " + Integer.toString(finalPosition.getX() + 1) + " " + Integer.toString(finalPosition.getY() + 1));
        setChanged();
        notifyObservers(event);
        handleDraftPool();
        disableGridPane(dicePatternGridPane1,windowPattern1);
        idToolCardSelected = 0;
        disableToolCards();
        toolCardSelected.setEffect(null);
        diceChosenFromDicePattern.setEffect(null);
    }

    private void copperFoilBurnisherSecondStep(int row, int column){

        finalPosition = new Position(row, column);
        step = 1;
        System.out.println("tool card 3 :" + Integer.toString(initialPosition.getX() + 1) + " " + Integer.toString(initialPosition.getY() + 1) + " " + Integer.toString(finalPosition.getX() + 1) + " " + Integer.toString(finalPosition.getY() + 1));
        VCEvent event = new CopperFoilBurnisherEvent(Integer.toString(initialPosition.getX() + 1) + " " + Integer.toString(initialPosition.getY() + 1) + " " + Integer.toString(finalPosition.getX() + 1) + " " + Integer.toString(finalPosition.getY() + 1));
        setChanged();
        notifyObservers(event);
        handleDraftPool();
        disableGridPane(dicePatternGridPane1,windowPattern1);
        idToolCardSelected = 0;
        disableToolCards();
        toolCardSelected.setEffect(null);
        diceChosenFromDicePattern.setEffect(null);
    }

    private void lathekinSecondStep(int row, int column){
        finalPosition = new Position(row, column);
        tmpImageView = (ImageView) getNodeByRowColumnIndex(row,column,dicePatternGridPane1,5);
        tmpImageView.setStyle("-fx-background-color: BLACK");
        step++;
    }

    private void lathekinForthStep(int row, int column){

        finalPosition2 = new Position(row, column);
        step = 1;
        VCEvent event = new LathekinEvent(Integer.toString(initialPosition.getX() + 1) + " " + Integer.toString(initialPosition.getY() + 1) + " " + Integer.toString(finalPosition.getX() + 1) + " " + Integer.toString(finalPosition.getY() + 1) + " " + Integer.toString(initialPosition2.getX() + 1) + " " + Integer.toString(initialPosition2.getY() + 1) + " " + Integer.toString(finalPosition2.getX() + 1) + " " + Integer.toString(finalPosition2.getY() + 1));
        System.out.println("tool card 4: " + Integer.toString(initialPosition.getX() + 1) + " " + Integer.toString(initialPosition.getY() + 1) + " " + Integer.toString(finalPosition.getX() + 1) + " " + Integer.toString(finalPosition.getY() + 1) + " " + Integer.toString(initialPosition2.getX() + 1) + " " + Integer.toString(initialPosition2.getY() + 1) + " " + Integer.toString(finalPosition2.getX() + 1) + " " + Integer.toString(finalPosition2.getY() + 1));
        setChanged();
        notifyObservers(event);
        handleDraftPool();
        disableGridPane(dicePatternGridPane1,windowPattern1);
        idToolCardSelected = 0;
        disableToolCards();
        toolCardSelected.setEffect(null);
        diceChosenFromDicePattern.setEffect(null);
        tmpImageView.setStyle(null);
    }

    private void fluxBrushPlaceDice(int row, int column){
        diceChosenFromDraftPool.setEffect(null);
        dicePatternPosition = new Position(row, column);
        VCEvent event = new FluxBrushPlaceDiceEvent(Integer.toString(dicePatternPosition.getX() + 1) + " " + Integer.toString(dicePatternPosition.getY() + 1));
        System.out.println("tool card 6: " + Integer.toString(dicePatternPosition.getX() + 1) + " " + Integer.toString(dicePatternPosition.getY() + 1));
        setChanged();
        notifyObservers(event);
        handleDraftPool();
        idToolCardSelected = 0;
        disableGridPane(dicePatternGridPane1,windowPattern1);
        disableToolCards();
        toolCardSelected.setEffect(null);
    }

    private void runnerPliers(int row, int column){
        dicePatternPosition = new Position(row, column);
        VCEvent event = new RunnerPliersEvent(Integer.toString(diceIndexDraftPool + 1) + " " + Integer.toString(dicePatternPosition.getX() + 1) + " " + Integer.toString(dicePatternPosition.getY() + 1));
        System.out.println("tool card 8: " + Integer.toString(diceIndexDraftPool + 1) + " " + Integer.toString(dicePatternPosition.getX() + 1) + " " + Integer.toString(dicePatternPosition.getY() + 1));
        setChanged();
        notifyObservers(event);
        handleDraftPool();
        idToolCardSelected = 0;
        disableGridPane(dicePatternGridPane1,windowPattern1);
        disableToolCards();
        toolCardSelected.setEffect(null);
    }

    private void corkBackedStraighedge(int row, int column){

        dicePatternPosition = new Position(row, column);
        VCEvent event = new CorkBakedStraightedgeEvent(Integer.toString(diceIndexDraftPool + 1) + " " + Integer.toString(dicePatternPosition.getX() + 1) + " " + Integer.toString(dicePatternPosition.getY() + 1));
        System.out.println("tool card 9 :" + Integer.toString(diceIndexDraftPool + 1) + " " + Integer.toString(dicePatternPosition.getX() + 1) + " " + Integer.toString(dicePatternPosition.getY() + 1));
        setChanged();
        notifyObservers(event);
        handleDraftPool();
        idToolCardSelected = 0;
        disableGridPane(dicePatternGridPane1,windowPattern1);
        disableToolCards();
        toolCardSelected.setEffect(null);
    }

    private void fluxRemoverPlaceDice(int row, int column){
        dicePatternPosition = new Position(row, column);
        VCEvent event = new FluxRemoverPlaceDiceEvent(Integer.toString(choiceFluxRemover) + " " + Integer.toString(dicePatternPosition.getX() + 1) + " " + Integer.toString(dicePatternPosition.getY() + 1));
        System.out.println("tool card 11: " + Integer.toString(choiceFluxRemover) + " " + Integer.toString(dicePatternPosition.getX() + 1) + " " + Integer.toString(dicePatternPosition.getY() + 1));
        setChanged();
        notifyObservers(event);
        handleDraftPool();
        disableGridPane(dicePatternGridPane1,windowPattern1);
        idToolCardSelected = 0;
        disableToolCards();
        toolCardSelected.setEffect(null);
        diceChosenFromDraftPool.setEffect(null);
    }

    private void tapWheelSecondStep(int row, int column){
        finalPosition = new Position(row, column);
        if (choiceTapWheel == 1) {
            step = 1;
            VCEvent event = new TapWheelEvent(Integer.toString(roundIndex + 1) + " " + Integer.toString(diceIndexRoundTrack + 1) + " " + Integer.toString(initialPosition.getX() + 1) + " " + Integer.toString(initialPosition.getY() + 1) + " " + Integer.toString(finalPosition.getX() + 1) + " " + Integer.toString(finalPosition.getY() + 1));
            System.out.println("tool card 12: " + Integer.toString(roundIndex + 1) + " " + Integer.toString(diceIndexRoundTrack + 1) + " " + Integer.toString(initialPosition.getX() + 1) + " " + Integer.toString(initialPosition.getY() + 1) + " " + Integer.toString(finalPosition.getX() + 1) + " " + Integer.toString(finalPosition.getY() + 1));
            setChanged();
            notifyObservers(event);
            handleDraftPool();
            disableGridPane(dicePatternGridPane1,windowPattern1);
            idToolCardSelected = 0;
            disableToolCards();
            toolCardSelected.setEffect(null);
            diceChosenFromDicePattern.setEffect(null);
        } else
            step ++;
    }

    private void tapWheelForthStep(int row, int column){

        finalPosition2 = new Position(row, column);
        step = 1;
        System.out.println("tool card 12: " + Integer.toString(roundIndex + 1) + " " + Integer.toString(diceIndexRoundTrack + 1) + " " + Integer.toString(initialPosition.getX() + 1) + " " + Integer.toString(initialPosition.getY() + 1) + " " + Integer.toString(finalPosition.getX() + 1) + " " + Integer.toString(finalPosition.getY() + 1) + " " + Integer.toString(initialPosition2.getX() + 1) + " " + Integer.toString(initialPosition2.getY() + 1) + " " + Integer.toString(finalPosition2.getX() + 1) + " " + Integer.toString(finalPosition2.getY() + 1));
        VCEvent event = new TapWheelEvent(Integer.toString(roundIndex + 1) + " " + Integer.toString(diceIndexRoundTrack + 1) + " " + Integer.toString(initialPosition.getX() + 1) + " " + Integer.toString(initialPosition.getY() + 1) + " " + Integer.toString(finalPosition.getX() + 1) + " " + Integer.toString(finalPosition.getY() + 1) + " " + Integer.toString(initialPosition2.getX() + 1) + " " + Integer.toString(initialPosition2.getY() + 1) + " " + Integer.toString(finalPosition2.getX() + 1) + " " + Integer.toString(finalPosition2.getY() + 1));
        setChanged();
        notifyObservers(event);
        handleDraftPool();
        disableGridPane(dicePatternGridPane1,windowPattern1);
        idToolCardSelected = 0;
        roundTrack.setOpacity(1);
        disableToolCards();
        toolCardSelected.setEffect(null);
        diceChosenFromDicePattern.setEffect(null);
    }
    //tool card che intragiscono con il dice pattern : 2, 3, 4, 6, 8, 9, 11, 12
    private void getDicePositionFromDicePattern(ImageView imageView) {
        int row = GridPane.getRowIndex(imageView);
        int column = GridPane.getColumnIndex(imageView);
        System.out.println("riga: "+row);
        System.out.println("colonna:"+ column);
        if (idToolCardSelected == 0  && !diceMoved) {
            placeDiceMove(row, column);
        } else if ((idToolCardSelected == 2 && step == 1) || (idToolCardSelected == 3 && step == 1) || (idToolCardSelected == 4 && step == 1) || (idToolCardSelected == 12 && step == 1)) {
            initialPosition = new Position(row, column);
            Object node = getNodeByRowColumnIndex(row,column,dicePatternGridPane1,5);
            diceChosenFromDicePattern = (ImageView) node;
            diceChosenFromDicePattern.setEffect(setBorderGlow());
            step++;
        } else if (idToolCardSelected == 2 && step == 2) {
            eglomiseBrushSecondStep(row, column);
        } else if (idToolCardSelected == 3 && step == 2) {
            copperFoilBurnisherSecondStep(row, column);
        } else if (idToolCardSelected == 4 && step == 2) {
            lathekinSecondStep(row, column);
        } else if ((idToolCardSelected == 4 && step == 3) || (idToolCardSelected == 12 && step == 3)) {
            initialPosition2 = new Position(row, column);
            step++;
        } else if (idToolCardSelected == 4 && step == 4) {
            lathekinForthStep(row, column);
        } else if (idToolCardSelected == 6) {
            fluxBrushPlaceDice(row, column);
        } else if (idToolCardSelected == 8) {
            runnerPliers(row, column);
        } else if (idToolCardSelected == 9) {
           corkBackedStraighedge(row, column);
        } else if (idToolCardSelected == 11) {
            fluxRemoverPlaceDice(row, column);
        } else if (idToolCardSelected == 12 && step == 2) {
            tapWheelSecondStep(row, column);
        } else if (idToolCardSelected == 12 && step == 4) {
            tapWheelForthStep(row, column);
        }
    }

}

