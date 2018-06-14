package it.polimi.se2018.view.gui;

//TODO: mappa VC, pulsante per annullare mossa fatta
import it.polimi.se2018.events.mvevent.*;
import it.polimi.se2018.events.vcevent.*;
import it.polimi.se2018.model.Position;
import it.polimi.se2018.view.VirtualViewCLI;
import it.polimi.se2018.view.gui.OurGridPane;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.net.MalformedURLException;
import java.util.*;

public class GameController extends Observable implements Observer {


    @FXML private AnchorPane pane;

    @FXML private Button skipTurnButton;

    @FXML private ImageView toolCard1;
    @FXML private ImageView toolCard2;
    @FXML private ImageView toolCard3;

    @FXML private ImageView publicObjectiveCard1;
    @FXML private ImageView publicObjectiveCard2;
    @FXML private ImageView publicObjectiveCard3;
    @FXML private ImageView privateObjectiveCard;

    @FXML private ImageView windowPattern4;
    @FXML private ImageView windowPattern3;
    @FXML private ImageView windowPattern2;
    @FXML private ImageView windowPattern1;

    @FXML private GridPane dicePatternGridPane4;
    @FXML private GridPane dicePatternGridPane3;
    @FXML private GridPane dicePatternGridPane2;
    @FXML private GridPane dicePatternGridPane1;

    @FXML private ImageView dicePatternImage00;
    @FXML private ImageView dicePatternImage01;
    @FXML private ImageView dicePatternImage02;
    @FXML private ImageView dicePatternImage03;
    @FXML private ImageView dicePatternImage04;
    @FXML private ImageView dicePatternImage10;
    @FXML private ImageView dicePatternImage11;
    @FXML private ImageView dicePatternImage12;
    @FXML private ImageView dicePatternImage13;
    @FXML private ImageView dicePatternImage14;
    @FXML private ImageView dicePatternImage20;
    @FXML private ImageView dicePatternImage21;
    @FXML private ImageView dicePatternImage22;
    @FXML private ImageView dicePatternImage23;
    @FXML private ImageView dicePatternImage24;
    @FXML private ImageView dicePatternImage30;
    @FXML private ImageView dicePatternImage31;
    @FXML private ImageView dicePatternImage32;
    @FXML private ImageView dicePatternImage33;
    @FXML private ImageView dicePatternImage34;

    @FXML private GridPane roundTrackGridPane;

    @FXML private GridPane draftPool;

    @FXML private ImageView roundTrack;


    @FXML private Label turnLabel;

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


    private ImageView toolCardSelected;
    private ImageView diceChosenFromDraftPool;
    private Position dicePatternPosition;
    private int diceIndexDraftPool;
    private int diceIndexRoundTrack;
    private VCEvent vcEvent;
    private MVEvent mvEvent;
    private String eventParameters;
    private Map<Integer, Runnable> mvEvents = new HashMap<>();
    //private Map<Integer, Runnable> vcEvents = new HashMap<>();


    private boolean diceMoved = false;
    private boolean imageViewsSetup = false;

    private int idToolCard1;
    private int idToolCard2;
    private int idToolCard3;
    private int idToolCardSelected = 0;
    public boolean isToolCardSelected = false;



    private int choiceGrozingPliers;
    private int choiceFluxRemover;
    private int choiceTapWheel;
    private Position initialPosition;
    private Position finalPosition;
    private Position initialPosition2;
    private Position finalPosition2;
    private int roundIndex;
    private int step = 1;
    private List<ImageView> listDicePattern = new ArrayList<>();

    private OurGridPane ourGridPane1;
    private OurGridPane ourGridPane2;
    private OurGridPane ourGridPane3;
    private OurGridPane ourGridPane4;
    private List<OurGridPane> ourGridPaneList = new ArrayList<>();
    private final String favorTokensPath = "src/main/Images/Others/FavorToken.png";
    private final String waitText = "Attendi";
    private final String yourTurnText = "È il tuo turno";



    @FXML
    void initialize() {
        initializeGridPaneImagesView(roundTrackGridPane, 10, 9, 39, 49);
        initializeGridPaneImagesView(dicePatternGridPane1, 4, 5, 59, 69);
        initializeGridPaneImagesView(dicePatternGridPane2, 4, 5, 29, 39);
        initializeGridPaneImagesView(dicePatternGridPane3, 4, 5, 29, 39);
        initializeGridPaneImagesView(dicePatternGridPane4, 4, 5, 29, 39);
        initializeGridPaneImagesView(draftPool, 1, 9, 59, 69);


        createMVMap();
        // createVCMap();

        addImageToImageView("src/main/Images/Others/RoundTrack.png",roundTrack,150,776);
        favorTokensPlayer1ImageView.setImage(new Image(ViewGUI.getUrlFromPath(favorTokensPath)));
        favorTokensToolCard1ImageView.setImage(new Image(ViewGUI.getUrlFromPath(favorTokensPath)));
        favorTokensToolCard2ImageView.setImage(new Image(ViewGUI.getUrlFromPath(favorTokensPath)));
        favorTokensToolCard3ImageView.setImage(new Image(ViewGUI.getUrlFromPath(favorTokensPath)));
        Image background = new Image(ViewGUI.getUrlFromPath("src/main/Images/Others/gameBackground.png"));
        BackgroundImage myBGI = new BackgroundImage(background, null, null,null, null);
        pane.setBackground(new Background(myBGI));

        listDicePattern.add(dicePatternImage00);
        listDicePattern.add(dicePatternImage01);
        listDicePattern.add(dicePatternImage02);
        listDicePattern.add(dicePatternImage03);
        listDicePattern.add(dicePatternImage04);
        listDicePattern.add(dicePatternImage10);
        listDicePattern.add(dicePatternImage11);
        listDicePattern.add(dicePatternImage12);
        listDicePattern.add(dicePatternImage13);
        listDicePattern.add(dicePatternImage14);
        listDicePattern.add(dicePatternImage20);
        listDicePattern.add(dicePatternImage21);
        listDicePattern.add(dicePatternImage22);
        listDicePattern.add(dicePatternImage23);
        listDicePattern.add(dicePatternImage24);
        listDicePattern.add(dicePatternImage30);
        listDicePattern.add(dicePatternImage31);
        listDicePattern.add(dicePatternImage32);
        listDicePattern.add(dicePatternImage33);
        listDicePattern.add(dicePatternImage34);
        addImagesViewToGridPane(dicePatternGridPane1, listDicePattern);

        publicObjectiveCard1.setOnMouseEntered(event -> zoomInPOC(publicObjectiveCard1));
        publicObjectiveCard2.setOnMouseEntered(event -> zoomInPOC(publicObjectiveCard2));
        publicObjectiveCard3.setOnMouseEntered(event -> zoomInPOC(publicObjectiveCard3));
        toolCard1.setOnMouseEntered(event -> zoomIn(toolCard1));
        toolCard2.setOnMouseEntered(event -> zoomIn(toolCard2));
        toolCard3.setOnMouseEntered(event -> zoomIn(toolCard3));
        privateObjectiveCard.setOnMouseEntered(event -> zoomIn(privateObjectiveCard));
        publicObjectiveCard1.setOnMouseExited(event -> zoomOutPOC(publicObjectiveCard1));
        publicObjectiveCard2.setOnMouseExited(event -> zoomOutPOC(publicObjectiveCard2));
        publicObjectiveCard3.setOnMouseExited(event -> zoomOutPOC(publicObjectiveCard3));
        toolCard1.setOnMouseExited(event -> zoomOut(toolCard1));
        toolCard2.setOnMouseExited(event -> zoomOut(toolCard2));
        toolCard3.setOnMouseExited(event -> zoomOut(toolCard3));
        privateObjectiveCard.setOnMouseExited(event -> zoomOut(privateObjectiveCard));
        toolCard1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> useToolCard1());
        toolCard2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> useToolCard2());
        toolCard3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> useToolCard3());
        skipTurnButton.setOnMouseClicked(event -> skipTurn());

        initializeRoundTrackEventHandler(roundTrackGridPane);
        initializeDicePatternEventHandler(dicePatternGridPane1);
        initializeDraftPoolEventHandler(draftPool);

        dicePatternGridPane1.toFront();
        roundTrackGridPane.toFront();

        roundTrackGridPane.setDisable(true);
        dicePatternGridPane1.setDisable(true);

    }


    private void zoomInPOC(ImageView imageView){

        imageView.setLayoutY(imageView.getLayoutY()-326);
        zoomIn(imageView);
    }

    private void zoomOutPOC(ImageView imageView){

        imageView.setLayoutY(imageView.getLayoutY()+326);
        zoomOut(imageView);
    }

    private void initializeDraftPoolEventHandler(GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof ImageView) {
                ImageView tmp = (ImageView) node;
                tmp.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> getDiceIndexFromDraftPool(tmp));
            }
        }
    }

    private void addImagesViewToGridPane(GridPane gridPane, List<ImageView> imagesView) {
        int row = 0;
        int column = 0;
        for (ImageView imageView : imagesView) {
            gridPane.add(imageView, column, row);
            if (column != 4)
                column++;
            else {
                column = 0;
                row++;
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

    private void initializeRoundTrackEventHandler(GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof ImageView) {
                ImageView tmp = (ImageView) node;
                tmp.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> getDicePositionFromRoundTrack(tmp));
            }
        }
    }

    //l'evento che passa le toolcard, oltre al path passa anche l'id delle toolcard, così posso usare la mappa e lanciare il metodo corretto
    private void useToolCard1() {
        toolCard1.setEffect(setBorderGlow());
        toolCardSelected = toolCard1;
        isToolCardSelected = true;
        idToolCardSelected = idToolCard1;
        System.out.println("hai selezionato la tool card" + idToolCardSelected);
        handleToolCards(idToolCardSelected);

    }

    private void useToolCard2() {
        toolCard2.setEffect(setBorderGlow());
        toolCardSelected = toolCard2;
        isToolCardSelected = true;
        idToolCardSelected = idToolCard2;
        System.out.println("hai selezionato la tool card" + idToolCardSelected);
        handleToolCards(idToolCardSelected);
    }

    private void useToolCard3() {
        toolCard3.setEffect(setBorderGlow());
        toolCardSelected = toolCard3;
        isToolCardSelected = true;
        idToolCardSelected = idToolCard3;
        System.out.println("hai selezionato la tool card" + idToolCardSelected);
        handleToolCards(idToolCardSelected);

    }


    private void disableToolCards(){
        toolCard1.setDisable(true);
        toolCard1.setOpacity(0.5);
        toolCard2.setDisable(true);
        toolCard2.setOpacity(0.5);
        toolCard3.setDisable(true);
        toolCard3.setOpacity(0.5);
    }

    private void enableToolCards(){
        toolCard1.setDisable(false);
        toolCard1.setOpacity(1);
        toolCard2.setDisable(false);
        toolCard2.setOpacity(1);
        toolCard3.setDisable(false);
        toolCard3.setOpacity(1);
    }

    //TODO: controllare se devo disabilitare dice pattern un'image view alla volta
    private void disableGridPane(GridPane gridPane, ImageView imageView){
        setDisableGridPane(gridPane);
        gridPane.setDisable(true);
        imageView.setOpacity(0.5);
    }

    private void enableGridPane(GridPane gridPane, ImageView imageView){
        setEnableGridPane(gridPane);
        gridPane.setDisable(false);
        imageView.setOpacity(1);
    }


    private void setDisableGridPane(GridPane gridPane){
        for(Node node : gridPane.getChildren()){
            if(node instanceof  ImageView)
                node.setDisable(true);
        }
    }

    private void setEnableGridPane(GridPane gridPane){
        for(Node node : gridPane.getChildren()){
            if(node instanceof  ImageView)
                node.setDisable(false);
        }
    }
    private void setDraftPoolOpacity(double opacity){
        for (int column = 0; column < 9 ; column ++) {
            Object foundNode = getNodeByRowColumnIndex(0,column, draftPool,0 );
            if (foundNode instanceof ImageView) {
                ImageView img = (ImageView) foundNode;
                img.setOpacity(opacity);
            }
        }
    }
    private void disableDraftPool(){
        draftPool.setDisable(true);
        setDraftPoolOpacity(0.5);
    }

    private void enableDraftPool(){
        draftPool.setDisable(false);
        setDraftPoolOpacity(1);
    }

    private void handleDraftPool(){
        if(diceMoved)
            disableDraftPool();
        else
            enableDraftPool();
    }

    private void handleToolCards(int idToolCard){
        if(idToolCard >= 2 && idToolCard <= 4){
            disableDraftPool();
            disableToolCards();
            enableGridPane(dicePatternGridPane1,windowPattern1);
        }
        else if (idToolCard == 12){
            disableDraftPool();
            disableToolCards();
            disableGridPane(dicePatternGridPane1,windowPattern1);
            enableGridPane(roundTrackGridPane,roundTrack);
        }
        else if (idToolCard == 7){
            VCEvent event = new GlazingHammerEvent();
            setChanged();
            notifyObservers(event);
            idToolCardSelected = 0;

        }
        else{
            disableToolCards();
            enableDraftPool();
            disableGridPane(dicePatternGridPane1,windowPattern1);
        }

    }


    private DropShadow setBorderGlow() {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.BLUE);
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);
        borderGlow.setHeight(45);
        return borderGlow;
    }

    private void initializeGridPaneImagesView(GridPane gridPane, int row, int column, int height, int width) {
        int i;
        int j;
        for (i = 0; i < row; i++) {
            for (j = 0; j < column; j++) {
                ImageView img = new ImageView();
                img.setFitHeight(height);
                img.setFitWidth(width);
                img.setPreserveRatio(true);
                gridPane.add(img, j, i);
            }
        }
    }

    private void zoomIn(ImageView imageView) {
        imageView.setFitHeight(470);
        imageView.setFitWidth(357);
        imageView.toFront();
        imageView.setPreserveRatio(true);
    }

    private void zoomOut(ImageView imageView) {
        imageView.setFitHeight(144);
        imageView.setFitWidth(95);
        imageView.toBack();
        imageView.setPreserveRatio(true);
    }


    @Override
    public void update(Observable o, Object event) {
        mvEvent = (MVEvent) event;
        mvEvents.get(mvEvent.getId()).run();
    }

    private void createMVMap() {
        mvEvents.put(-1, ()-> {});
        mvEvents.put(1, ()-> updateDicePatterns(mvEvent));
        mvEvents.put(2, ()-> updateDraftPool(mvEvent));
        mvEvents.put(3, ()-> updateToolCards(mvEvent));
        mvEvents.put(4, ()-> updateRoundTrack(mvEvent));
        mvEvents.put(5, ()-> {});
        mvEvents.put(6, ()->handleActionMenu(mvEvent));
        mvEvents.put(7, ()-> showAll(mvEvent));
        mvEvents.put(8, ()-> updateFavorTokens(mvEvent));
        mvEvents.put(9, ()-> showError(mvEvent));
        mvEvents.put(10, ()-> {});
        mvEvents.put(11, this::handleGetInput);
        mvEvents.put(12, this::handleEndTurnEvent);
        mvEvents.put(13, ()-> {});
        mvEvents.put(14, ()-> {});
        mvEvents.put(15, ()->{});
        mvEvents.put(99, ()->updateDicePatterns(mvEvent));
    }



   /* private void createVCMap() {
        vcEvents.put(-1, () -> vcEvent = new WindowPatternChoiceEvent(eventParameters));
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
    }*/



   private void handleEndTurnEvent(){

       showEndTurn(mvEvent);
       turnLabel.setStyle("-fx-background-color: #ff0000;");
       Platform.runLater(()->turnLabel.setText(waitText));
       skipTurnButton.setDisable(true);
   }

    private void handleGetInput(){
        diceMoved = false;

    }

    private void handleActionMenu(MVEvent event){
        ActionMenuEvent actionMenuEvent = (ActionMenuEvent) event;
        diceMoved = actionMenuEvent.isDiceMoved();
        isToolCardSelected = actionMenuEvent.isToolCardUsed();
    }

    private void showAll(MVEvent event){
        ShowAllEvent showAllEvent = (ShowAllEvent) event;
        if (!imageViewsSetup) {
            updateDraftPool(showAllEvent.getDraftPool());
            updateToolCards(showAllEvent.getToolCards());
            createAssociationWithOurGridPane(showAllEvent.getSetWindowPatternsGUIEvent(),showAllEvent.getDicePatterns());
            updateRoundTrack(showAllEvent.getRoundTrack());
            updatePublicObjectiveCards(showAllEvent.getPublicObjectiveCardsGUI());
            updatePrivateObjectiveCard(showAllEvent.getPrivateObjectiveCardStringGUI());
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

        }
    }


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




    private void createAssociationWithOurGridPane(MVEvent event1, MVEvent event2){

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
        /*switch (list.size()){
            case 1:
                ourGridPane2 = new OurGridPane(dicePatternGridPane2,players.get(list.get(0)), windowPatterns.get(list.get(0)),player2NameLabel,Integer.parseInt(favorTokensPlayers.get(list.get(0))),favorTokensPlayer2Label);
                addImageToImageView(windowPatterns.get(list.get(0)), windowPattern2,180, 200);
                updateDicePattern(dicePatternGUI.get(0),dicePatternGridPane2,29,39);
                ourGridPaneList.add(ourGridPane2);
                favorTokensPlayer2ImageView.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/Others/FavorToken.png")));
                break;
            case 2:
                ourGridPane2 = new OurGridPane(dicePatternGridPane2,players.get(list.get(0)), windowPatterns.get(list.get(0)),player2NameLabel,Integer.parseInt(favorTokensPlayers.get(list.get(0))),favorTokensPlayer2Label);
                addImageToImageView(windowPatterns.get(list.get(0)), windowPattern2,180, 200);
                updateDicePattern(dicePatternGUI.get(0),dicePatternGridPane2,29,39);
                ourGridPaneList.add(ourGridPane2);
                addImageToImageView(favorTokensPath,favorTokensPlayer2ImageView,30,30);
                ourGridPane3 = new OurGridPane(dicePatternGridPane3,players.get(list.get(1)), windowPatterns.get(list.get(1)),player3NameLabel,Integer.parseInt(favorTokensPlayers.get(list.get(1))),favorTokensPlayer3Label);
                addImageToImageView(windowPatterns.get(list.get(1)), windowPattern3,180, 200);
                updateDicePattern(dicePatternGUI.get(1),dicePatternGridPane3,29,39);
                ourGridPaneList.add(ourGridPane3);
                favorTokensPlayer3ImageView.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/Others/FavorToken.png")));
                break;
            case 3:
                ourGridPane2 = new OurGridPane(dicePatternGridPane2,players.get(list.get(0)), windowPatterns.get(list.get(0)),player2NameLabel,Integer.parseInt(favorTokensPlayers.get(list.get(0))),favorTokensPlayer2Label);
                addImageToImageView(windowPatterns.get(list.get(0)), windowPattern2,180, 200);
                updateDicePattern(dicePatternGUI.get(0),dicePatternGridPane2,29,39);
                ourGridPaneList.add(ourGridPane2);
                addImageToImageView(favorTokensPath,favorTokensPlayer2ImageView,30,30);
                ourGridPane3 = new OurGridPane(dicePatternGridPane3,players.get(list.get(1)), windowPatterns.get(list.get(1)),player3NameLabel,Integer.parseInt(favorTokensPlayers.get(list.get(1))),favorTokensPlayer3Label);
                addImageToImageView(windowPatterns.get(list.get(1)), windowPattern3,180, 200);
                updateDicePattern(dicePatternGUI.get(1),dicePatternGridPane3,29,39);
                ourGridPaneList.add(ourGridPane3);
                addImageToImageView(favorTokensPath,favorTokensPlayer3ImageView,30,30);
                ourGridPane4 = new OurGridPane(dicePatternGridPane4,players.get(list.get(2)), windowPatterns.get(list.get(2)),player4NameLabel,Integer.parseInt(favorTokensPlayers.get(list.get(2))),favorTokensPlayer4Label);
                addImageToImageView(windowPatterns.get(list.get(2)), windowPattern4,180, 200);
                updateDicePattern(dicePatternGUI.get(2),dicePatternGridPane4,29,39);
                ourGridPaneList.add(ourGridPane4);
                favorTokensPlayer4ImageView.setImage(new Image(ViewGUI.getUrlFromPath("src/main/Images/Others/FavorToken.png")));
                break;
            default:
                break;
        }*/
    }


    private void skipTurn(){
        setChanged();
        notifyObservers(new SkipTurnEvent());
        turnLabel.setStyle("-fx-background-color: #ff0000;");
        turnLabel.setText(waitText);
        disableToolCards();
        disableDraftPool();
        disableGridPane(dicePatternGridPane1,windowPattern1);
        skipTurnButton.setDisable(true);
        idToolCardSelected = 0;
        toolCard1.setEffect(null);
        toolCard2.setEffect(null);
        toolCard3.setEffect(null);
    }
    private void showEndTurn(MVEvent event){
        Platform.runLater(() ->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Avviso");
            alert.setContentText("Turno terminato. Attendi.");

            alert.showAndWait();
        });
    }

    private void showError(MVEvent event){
        ErrorEvent errorEvent = (ErrorEvent) event;
        if(errorEvent.getMessageToDisplay().equals("Non hai abbastanza segnalini favore\n"))
            idToolCardSelected = 0;
        Platform.runLater(()-> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setContentText(errorEvent.getMessageToDisplay());

            alert.showAndWait();
            enableDraftPool();
        });
    }

    private void updatePrivateObjectiveCard(String path){
        addImageToImageView(path,privateObjectiveCard,144,95);
    }

    private void updatePublicObjectiveCards(List<String> publicObjectiveCards){
        addImageToImageView(publicObjectiveCards.get(0),publicObjectiveCard1,144,95);
        addImageToImageView(publicObjectiveCards.get(1),publicObjectiveCard2,144,95);
        addImageToImageView(publicObjectiveCards.get(2),publicObjectiveCard3,144,95);
    }

    private void cleanDreaftPool(){

        for(int column = 0; column <9 ; column++){
            Object foundNode = getNodeByRowColumnIndex(0,column, draftPool,0 );
            if (foundNode instanceof ImageView) {
                ImageView img = (ImageView) foundNode;
                addImageToImageView(" ",img,59,69);
            }
        }
    }

    private void updateDraftPool(MVEvent event) {
        cleanDreaftPool();
        DraftPoolEvent draftPoolEvent = (DraftPoolEvent) event;
        int column = 0;
        for (String path : draftPoolEvent.getDraftPoolGUI()) {
            Object foundNode = getNodeByRowColumnIndex(0,column, draftPool,0 );
            if (foundNode instanceof ImageView) {
                ImageView img = (ImageView) foundNode;
                addImageToImageView(path,img,59,69);
            }
            column++;
        }
    }

    private void updateFavorTokens(MVEvent event){
        FavorTokensEvent favorTokensEvent = (FavorTokensEvent) event;
        int favorTokensNumber = Integer.parseInt(favorTokensEvent.getFavorTokensNumberString());
        for(OurGridPane ourGridPane : ourGridPaneList){
            if(ourGridPane.getPlayerName().equals(favorTokensEvent.getPlayer()))
                ourGridPane.setFavorTokensLabel(favorTokensNumber);
        }
    }

    private void updateDicePatterns(MVEvent event) {
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

    private void updateRoundTrack(MVEvent event){
        RoundTrackEvent roundTrackEvent = (RoundTrackEvent) event;
        int column = 0;
        int row = 0;
        for (String path : roundTrackEvent.getRoundTrackGUI()) {
            Object foundNode = getNodeByRowColumnIndex(row,column, roundTrackGridPane,9);
            if (foundNode instanceof ImageView) {
                ImageView img = (ImageView) foundNode;
                addImageToImageView(path,img,39,49);
            }
            if(column != 8)
                column++;
            else{
                row++;
                column = 0;
            }

        }
    }

    private void updateToolCards(MVEvent event){
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

    private void grozingPliersWindow() {
        Stage window = new Stage();
        //Blocks interaction with the caller Scene
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Scelta");
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
        Label label = new Label("Vuoi aumentare o diminuire il valore del dado scelto?");
        Button increaseButton = new Button("Aumentare");
        Button decreaseButton = new Button("Diminuire");

        //Adds everything to the layouts
        vBox.getChildren().addAll(label, hBox);
        hBox.getChildren().addAll(increaseButton, decreaseButton);

        increaseButton.setOnAction(event -> {
            choiceGrozingPliers = 2;
            window.close();
        });
        decreaseButton.setOnAction(event -> {
            choiceGrozingPliers = 1;
            window.close();
        });

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.showAndWait();
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
            diceChosenFromDraftPool.setEffect(null);
            fluxRemoverWindow();
            disableDraftPool();
            enableGridPane(dicePatternGridPane1,windowPattern1);
        }

    }


    private void fluxRemoverWindow() {
        Stage window = new Stage();
        //Blocks interaction with the caller Scene
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Scelta");
        window.setMinWidth(400);
        window.setMinHeight(225);
        window.setResizable(false);

        Button confirmButton = new Button("Conferma Scelta");

        ChoiceBox<Integer> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(1,2,3,4,5,6);
        choiceBox.setValue(1); //set a default value
        confirmButton.setOnAction(event -> {
            choiceFluxRemover = choiceBox.getValue();
        });

        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(choiceBox, confirmButton);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.showAndWait();
    }

    private void tapWheelWindow() {
        Stage window = new Stage();
        //Blocks interaction with the caller Scene
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Scelta");
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
        Label label = new Label("Quanti dadi vuoi spostare?");
        Button oneButton = new Button("Uno");
        Button twoButton = new Button("Due");

        //Adds everything to the layouts
        vBox.getChildren().addAll(label, hBox);
        hBox.getChildren().addAll(oneButton, twoButton);

        oneButton.setOnAction(event -> {
            choiceTapWheel = 1;
            window.close();
        });
        twoButton.setOnAction(event -> {
            choiceTapWheel = 2;
            window.close();
        });

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.showAndWait();

    }

    //tool card che intragiscono con il dice pattern : 2, 3, 4, 6, 8, 9, 11, 12
    private void getDicePositionFromDicePattern(ImageView imageView) {
        int row = GridPane.getRowIndex(imageView);
        int column = GridPane.getColumnIndex(imageView);
        System.out.println("riga: "+row);
        System.out.println("colonna:"+ column);
        if (idToolCardSelected == 0  && !diceMoved) {
            dicePatternPosition = new Position(row, column);
            System.out.println("mossa su dice pattern:" + Integer.toString(diceIndexDraftPool+1) + " " + Integer.toString(dicePatternPosition.getX()) + " " + Integer.toString(dicePatternPosition.getY()));
            VCEvent event = new PlaceDiceEvent(Integer.toString(diceIndexDraftPool + 1) + " " + Integer.toString(dicePatternPosition.getX() + 1) + " " + Integer.toString(dicePatternPosition.getY() + 1));
            setChanged();
            notifyObservers(event);
            diceChosenFromDraftPool.setEffect(null);
            enableToolCards();
            disableGridPane(dicePatternGridPane1, windowPattern1);
        } else if ((idToolCardSelected == 2 && step == 1) || (idToolCardSelected == 3 && step == 1) || (idToolCardSelected == 4 && step == 1) || (idToolCardSelected == 12 && step == 1)) {
            initialPosition = new Position(row, column);
            step++;
        } else if (idToolCardSelected == 2 && step == 2) {
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
        } else if (idToolCardSelected == 3 && step == 2) {
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
        } else if (idToolCardSelected == 4 && step == 2) {
            finalPosition = new Position(row, column);
            step++;
        } else if ((idToolCardSelected == 4 && step == 3) || (idToolCardSelected == 12 && step == 3)) {
            initialPosition2 = new Position(row, column);
            step++;
        } else if (idToolCardSelected == 4 && step == 4) {
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
        } else if (idToolCardSelected == 6) {
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
        } else if (idToolCardSelected == 8) {
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
        } else if (idToolCardSelected == 9) {
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
        } else if (idToolCardSelected == 11) {
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
        } else if (idToolCardSelected == 12 && step == 2) {
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
            } else
                step++;
        } else if (idToolCardSelected == 12 && step == 4) {
            finalPosition2 = new Position(row, column);
            step = 1;
            VCEvent event = new TapWheelEvent(Integer.toString(roundIndex + 1) + " " + Integer.toString(diceIndexRoundTrack + 1) + " " + Integer.toString(initialPosition.getX() + 1) + " " + Integer.toString(initialPosition.getY() + 1) + " " + Integer.toString(initialPosition2.getX() + 1) + " " + Integer.toString(initialPosition2.getY() + 1) + " " + Integer.toString(finalPosition.getX() + 1) + " " + Integer.toString(finalPosition.getY() + 1) + " " + Integer.toString(finalPosition2.getX() + 1) + " " + Integer.toString(finalPosition2.getY() + 1));
            System.out.println("tool card 12: " + Integer.toString(roundIndex + 1) + " " + Integer.toString(diceIndexRoundTrack + 1) + " " + Integer.toString(initialPosition.getX() + 1) + " " + Integer.toString(initialPosition.getY() + 1) + " " + Integer.toString(initialPosition2.getX() + 1) + " " + Integer.toString(initialPosition2.getY() + 1) + " " + Integer.toString(finalPosition.getX() + 1) + " " + Integer.toString(finalPosition.getY() + 1) + " " + Integer.toString(finalPosition2.getX() + 1) + " " + Integer.toString(finalPosition2.getY() + 1));
            setChanged();
            notifyObservers(event);
            handleDraftPool();
            disableGridPane(dicePatternGridPane1,windowPattern1);
            idToolCardSelected = 0;
            roundTrack.setOpacity(1);
            disableToolCards();
            toolCardSelected.setEffect(null);
        }

    }


    //tool card che interagiscono con il round track : 5, 12
    private void getDicePositionFromRoundTrack(ImageView imageView) {
        roundIndex = GridPane.getRowIndex(imageView);
        diceIndexRoundTrack = GridPane.getColumnIndex(imageView);
        System.out.println("riga" + roundIndex);
        System.out.println("colonna" + diceIndexRoundTrack);
        if (idToolCardSelected == 5) {
            VCEvent event = new LensCutterEvent(Integer.toString(diceIndexDraftPool + 1) + " " + Integer.toString(roundIndex + 1) + " " + Integer.toString(diceIndexRoundTrack + 1));
            System.out.println("tool card 5 :" + Integer.toString(diceIndexDraftPool + 1) + " " + Integer.toString(roundIndex + 1) + " " + Integer.toString(diceIndexRoundTrack + 1));
            setChanged();
            notifyObservers(event);
            diceChosenFromDraftPool.setEffect(null);
            disableGridPane(roundTrackGridPane,roundTrack);
            handleDraftPool();
            idToolCardSelected = 0;
            disableToolCards();
            toolCardSelected.setEffect(null);
        } else if (idToolCardSelected == 12) {
            tapWheelWindow();
            disableGridPane(roundTrackGridPane,roundTrack);
            enableGridPane(dicePatternGridPane1,windowPattern1);
        }

    }


    private Object getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane, int dimension) {
        return gridPane.getChildren().get(dimension * row + column);
    }


    private void addImageToImageView(String path, ImageView imageView, int height, int width) {
        File fileImage = new File(path);
        String url = "";
        try {
            url = fileImage.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("problema nel caricamento dell'immagine ");
        }
        imageView.setImage(new Image(url));
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
    }


}

