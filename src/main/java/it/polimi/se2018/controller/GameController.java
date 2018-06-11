package it.polimi.se2018.controller;

//TODO: favor tokens e mostrare i dice pattern degli altri
import it.polimi.se2018.events.mvevent.*;
import it.polimi.se2018.events.vcevent.*;
import it.polimi.se2018.model.Position;
import it.polimi.se2018.view.gui.OurGridPane;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    @FXML private GridPane favorTokensGridPane;

    @FXML private Label turnLabel;

    private ImageView diceChosenFromDraftPool;
    private Position dicePatternPosition;
    private int diceIndexDraftPool;
    private int diceIndexRoundTrack;
    private VCEvent vcEvent;
    private MVEvent mvEvent;
    private String eventParameters;
    private Map<Integer, Runnable> mvEvents = new HashMap<>();
    //private Map<Integer, Runnable> vcEvents = new HashMap<>();


    private boolean isDraftPoolUsed = false;
    private boolean diceMoved = false;
    private boolean isDicePatternSelected = false;
    private boolean isToolCard1Selected = false;
    private boolean isToolCard2Selected = false;
    private boolean isToolCard3Selected = false;
    private boolean isToolCardSelected = false;
    private boolean imageViewsSetup = false;

    private int idToolCard1;
    private int idToolCard2;
    private int idToolCard3;
    private int idToolCardSelected;



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


    //TODO: quando un untente usa una toolcard io setto istoolcard used a true. Il turno successivo deve essere false

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
        //TODO: questa cosa non mi piace molto, ma con i metodi initialize se poi clicco su un'immagine vuola, non so perchè ma non prende il click

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
        /*addImagesViewToGridPane(dicePatternGridPane2, listDicePattern);
        addImagesViewToGridPane(dicePatternGridPane3, listDicePattern);
        addImagesViewToGridPane(dicePatternGridPane4, listDicePattern);*/

        publicObjectiveCard1.setOnMouseMoved(event -> zoomIn(publicObjectiveCard1));
        publicObjectiveCard2.setOnMouseMoved(event -> zoomIn(publicObjectiveCard2));
        publicObjectiveCard3.setOnMouseMoved(event -> zoomIn(publicObjectiveCard3));
        toolCard1.setOnMouseMoved(event -> zoomIn(toolCard1));
        toolCard2.setOnMouseMoved(event -> zoomIn(toolCard2));
        toolCard3.setOnMouseMoved(event -> zoomIn(toolCard3));
        privateObjectiveCard.setOnMouseMoved(event -> zoomIn(privateObjectiveCard));
        publicObjectiveCard1.setOnMouseExited(event -> zoomOut(publicObjectiveCard1));
        publicObjectiveCard2.setOnMouseExited(event -> zoomOut(publicObjectiveCard2));
        publicObjectiveCard3.setOnMouseExited(event -> zoomOut(publicObjectiveCard3));
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

    }


    public void initializeDraftPoolEventHandler(GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof ImageView) {
                ImageView tmp = (ImageView) node;
                tmp.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> getDiceIndexFromDraftPool(tmp));
            }
        }
    }

    public void addImagesViewToGridPane(GridPane gridPane, List<ImageView> imagesView) {
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


    public void initializeDicePatternEventHandler(GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof ImageView) {
                ImageView tmp = (ImageView) node;
                tmp.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> getDicePositionFromDicePattern(tmp));
            }
        }
    }

    public void initializeRoundTrackEventHandler(GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof ImageView) {
                ImageView tmp = (ImageView) node;
                tmp.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> getDicePositionFromRoundTrack(tmp));
            }
        }
    }

    //l'evento che passa le toolcard, oltre al path passa anche l'id delle toolcard, così posso usare la mappa e lanciare il metodo corretto
    public void useToolCard1() {
        isToolCard1Selected = true;
        isToolCardSelected = true;
        idToolCardSelected = idToolCard1;
        System.out.println("hai selezionato la tool card" + idToolCardSelected);
        //handleToolCards(idToolCardSelected);

    }

    public void useToolCard2() {
        //toolCard2.setEffect(setBorderGlow());
        isToolCard2Selected = true;
        isToolCardSelected = true;
        idToolCardSelected = idToolCard2;
        System.out.println("hai selezionato la tool card" + idToolCardSelected);
        //handleToolCards(idToolCardSelected);
    }

    public void useToolCard3() {
        //toolCard3.setEffect(setBorderGlow());
        isToolCard3Selected = true;
        isToolCardSelected = true;
        idToolCardSelected = idToolCard3;
        System.out.println("hai selezionato la tool card" + idToolCardSelected);
        //handleToolCards(idToolCardSelected);

    }

   /* public void ableMajorObjects(){
        publicObjectiveCard1.setDisable(false);
        publicObjectiveCard2.setDisable(false);
        publicObjectiveCard3.setDisable(false);
        skipTurnButton.setDisable(false);
        showOthersDicePatternButton.setDisable(false);
        if(!isToolCardSelected) {
            toolCard1.setDisable(false);
            toolCard2.setDisable(false);
            toolCard3.setDisable(false);
        }
        if(!isDraftPoolUsed)
            draftPool.setDisable(false);
    }

    public void disableMinorObjects(){
        favorTokensGridPane.setDisable(true);
        publicObjectiveCard1.setDisable(true);
        publicObjectiveCard2.setDisable(true);
        publicObjectiveCard3.setDisable(true);
        toolCard1.setDisable(true);
        toolCard2.setDisable(true);
        toolCard3.setDisable(true);
        skipTurnButton.setDisable(true);
        showOthersDicePatternButton.setDisable(true);
    }

    public void disableAllExceptDicePattern(){
        draftPool.setDisable(true);
        roundTrackGridPane.setDisable(true);
        for(ImageView imageView: listDicePattern)
            imageView.setDisable(false);
        disableMinorObjects();


    }

    public void disableAllExceptRoundTrack(){
        draftPool.setDisable(true);
        dicePatternGridPane.setDisable(true);
        for(ImageView imageView: listDicePattern)
            imageView.setDisable(true);
        for(ImageView imageView: listDicePattern)
            imageView.setDisable(false);
        disableMinorObjects();
    }

    public void disableAllExceptDraftPool(){
        dicePatternGridPane.setDisable(true);
        roundTrackGridPane.setDisable(true);
        for(ImageView imageView: listDicePattern)
            imageView.setDisable(true);
        disableMinorObjects();
    }

    public void handleToolCards(int idToolCard){
        if(idToolCard >= 2 && idToolCard <= 4)
            disableAllExceptDicePattern();
        else if (idToolCard == 12)
            disableAllExceptRoundTrack();
        else if (idToolCard == 7){
            VCEvent event = new GlazingHammerEvent();
            setChanged();
            notifyObservers(event);

        }
            disableAllExceptDraftPool();
    }*/


    //TODO: DEVO FARE IN MODO CHE NELLA DRAFTPOOL NON SIANO CLICCABILI IMAGEVIEW CHE NON CI DEVONO ESSERE
    private DropShadow setBorderGlow() {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.BLUE);
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);
        borderGlow.setHeight(45);
        return borderGlow;
    }

    public void initializeGridPaneImagesView(GridPane gridPane, int row, int column, int height, int width) {
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

    public void zoomIn(ImageView imageView) {
        imageView.setFitHeight(470);
        imageView.setFitWidth(357);
        imageView.toFront();
        imageView.setPreserveRatio(true);
    }

    public void zoomOut(ImageView imageView) {
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


    //TODO: decommentare
    private void createMVMap() {
        mvEvents.put(-1, ()-> {});
        mvEvents.put(1, ()-> updateDicePatterns(mvEvent));
        mvEvents.put(2, ()-> updateDraftPool(mvEvent));
        mvEvents.put(3, ()-> updateToolCards(mvEvent));
        mvEvents.put(4, ()-> updateRoundTrack(mvEvent));
        mvEvents.put(5, ()-> {});
        mvEvents.put(6, ()->handleActionMenu(mvEvent));
        mvEvents.put(7, ()-> showAll(mvEvent));
        // mvEvents.put(8, ()-> updateFavorTokens(mvEvent)); //TODO: da fare
        mvEvents.put(9, ()-> showError(mvEvent));
        mvEvents.put(10, ()-> {});
        mvEvents.put(11, ()->handleGetInput());
        mvEvents.put(12, ()-> {
                                showEndTurn(mvEvent);
                                turnLabel.setStyle("-fx-background-color: #ff0000;");
                                });//questo arriva se è finito il tempo, giusto?
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


    public void handleGetInput(){
        diceMoved = false;

    }

    public void handleActionMenu(MVEvent event){
        ActionMenuEvent actionMenuEvent = (ActionMenuEvent) event;
        diceMoved = actionMenuEvent.isDiceMoved();
        isToolCardSelected = actionMenuEvent.isToolCardUsed();
    }

    public void showAll(MVEvent event){
        if (!imageViewsSetup) {
            ShowAllEvent showAllEvent = (ShowAllEvent) event;
            updateDraftPool(showAllEvent.getDraftPool());
            updateToolCards(showAllEvent.getToolCards());
            createAssociationWithOurGridPane(showAllEvent.getSetWindowPatternsGUIEvent().getWindowPatternsGUI(),showAllEvent.getDicePatterns());
            //updateDicePatterns(showAllEvent.getDicePatterns());
            updateRoundTrack(showAllEvent.getRoundTrack());
            updatePublicObjectiveCards(showAllEvent.getPublicObjectiveCardsGUI());
            updatePrivateObjectiveCard(showAllEvent.getPrivateObjectiveCardStringGUI());
            imageViewsSetup = true;
        }
        else{
            System.out.println("sono nel secondo showAll");
            turnLabel.setStyle("-fx-background-color: #00ff19;");
        }
    }


    public int getCurrentPlayerIndex(List<String> players, String currentPlayer){
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).equals(currentPlayer))
                return i;
        }
        return -1;
    }

    public List<Integer> getRemainingPositions(List<String> players, int index){
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < players.size(); i++){
            if(i != index)
                list.add(i);
        }
        return list;
    }

    public void createAssociationWithOurGridPane(List<String> windowPatterns, MVEvent event){
        DicePatternEvent dicePatternEvent = (DicePatternEvent) event;
        List <String> players = dicePatternEvent.getPlayerNames();
        String currentPlayer = dicePatternEvent.getCurrentPlayer();
        List<List<String>> dicePatternGUI = dicePatternEvent.getDicePatternsGUI();
        int currentPlayerIndex = getCurrentPlayerIndex(players,currentPlayer);
        ourGridPane1 = new OurGridPane(dicePatternGridPane1,currentPlayer,windowPatterns.get(currentPlayerIndex));
        addImageToImageView(windowPatterns.get(currentPlayerIndex), windowPattern1,460, 520);
        updateDicePattern(dicePatternGUI.get(currentPlayerIndex),dicePatternGridPane1,59,69);
        ourGridPaneList.add(ourGridPane1);
        List<Integer> list = getRemainingPositions(players,currentPlayerIndex);
        switch (list.size()){
            case 1:
                ourGridPane2 = new OurGridPane(dicePatternGridPane2,players.get(list.get(0)), windowPatterns.get(list.get(0)));
                addImageToImageView(windowPatterns.get(list.get(0)), windowPattern2,180, 200);
                updateDicePattern(dicePatternGUI.get(0),dicePatternGridPane2,29,39);
                ourGridPaneList.add(ourGridPane2);
                break;
            case 2:
                ourGridPane2 = new OurGridPane(dicePatternGridPane2,players.get(list.get(0)), windowPatterns.get(list.get(0)));
                addImageToImageView(windowPatterns.get(list.get(0)), windowPattern2,180, 200);
                updateDicePattern(dicePatternGUI.get(0),dicePatternGridPane2,29,39);
                ourGridPaneList.add(ourGridPane2);
                ourGridPane3 = new OurGridPane(dicePatternGridPane3,players.get(list.get(1)), windowPatterns.get(list.get(1)));
                addImageToImageView(windowPatterns.get(list.get(1)), windowPattern3,180, 200);
                updateDicePattern(dicePatternGUI.get(1),dicePatternGridPane3,29,39);
                ourGridPaneList.add(ourGridPane3);
                break;
            case 3:
                ourGridPane2 = new OurGridPane(dicePatternGridPane2,players.get(list.get(0)), windowPatterns.get(list.get(0)));
                addImageToImageView(windowPatterns.get(list.get(0)), windowPattern2,180, 200);
                updateDicePattern(dicePatternGUI.get(0),dicePatternGridPane2,29,39);
                ourGridPaneList.add(ourGridPane2);
                ourGridPane3 = new OurGridPane(dicePatternGridPane3,players.get(list.get(1)), windowPatterns.get(list.get(1)));
                addImageToImageView(windowPatterns.get(list.get(1)), windowPattern3,180, 200);
                updateDicePattern(dicePatternGUI.get(1),dicePatternGridPane3,29,39);
                ourGridPaneList.add(ourGridPane3);
                ourGridPane4 = new OurGridPane(dicePatternGridPane4,players.get(list.get(2)), windowPatterns.get(list.get(2)));
                addImageToImageView(windowPatterns.get(list.get(2)), windowPattern4,180, 200);
                updateDicePattern(dicePatternGUI.get(2),dicePatternGridPane4,29,39);
                ourGridPaneList.add(ourGridPane4);
                break;
            default:
                break;
        }
    }


    public void updateWindowPattern(String path){
        addImageToImageView(path, windowPattern1,460,520);
    }

    public void skipTurn(){
        setChanged();
        notifyObservers(new SkipTurnEvent());
        turnLabel.setStyle("-fx-background-color: #ff0000;");
    }
    private void showEndTurn(MVEvent event){
        Platform.runLater(this::showEndTurnAlert);
    }

    private void showEndTurnAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Avviso");
        alert.setContentText("Turno terminato. Attendi.");

        alert.showAndWait();
    }

    public void showError(MVEvent event){
        ErrorEvent errorEvent = (ErrorEvent) event;

        Platform.runLater(()-> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setContentText(errorEvent.getMessageToDisplay());

            alert.showAndWait();
        });
    }

    public void updatePrivateObjectiveCard(String path){
        addImageToImageView(path,privateObjectiveCard,144,95);
    }

    public void updatePublicObjectiveCards(List<String> publicObjectiveCards){
        addImageToImageView(publicObjectiveCards.get(0),publicObjectiveCard1,144,95);
        addImageToImageView(publicObjectiveCards.get(1),publicObjectiveCard2,144,95);
        addImageToImageView(publicObjectiveCards.get(2),publicObjectiveCard3,144,95);
    }


    public void cleanDreaftPool(){

        for(int column = 0; column <9 ; column++){
            Object foundNode = getNodeByRowColumnIndex(0,column, draftPool,0 );
            if (foundNode instanceof ImageView) {
                ImageView img = (ImageView) foundNode;
                addImageToImageView(" ",img,59,69);
            }
        }
    }
    //TODO: decommentare e creare il metodo getDraftPoolStringGUI()
    //metodo per costruire la draft pool con il path delle immagini contenuto nell'evento
    public void updateDraftPool(MVEvent event) {
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


    public void myUpdateDraftPool(List<String> paths) {
        int column = 0;
        for (String path : paths) {
            Object foundNode = getNodeByRowColumnIndex(0, column, draftPool, 0);
            if (foundNode instanceof ImageView) {
                ImageView img = (ImageView) foundNode;
                addImageToImageView(path, img, 59, 69);
            }
            column++;
        }
    }


    //TODO: decommentare
    /*public void updateDicePatterns(MVEvent event){
        DicePatternEvent dicePatternEvent = (DicePatternEvent) event;
        for(List <String> dicePatternPath : dicePatternEvent.getDicePatternsGUI())
            updateDicePattern(dicePatternPath);
    }*/


    public void updateDicePatterns(MVEvent event) {
        DicePatternEvent dicePatternEvent = (DicePatternEvent) event;
        int currentPlayerIndex = getCurrentPlayerIndex(dicePatternEvent.getPlayerNames(), dicePatternEvent.getCurrentPlayer());
        GridPane tmp = getCurrentPlayerOurGridPane(dicePatternEvent.getCurrentPlayer()).getGridPane();
        if(tmp == dicePatternGridPane1)
            updateDicePattern(dicePatternEvent.getDicePatternsGUI().get(currentPlayerIndex), tmp, 59,69);
        else
            updateDicePattern(dicePatternEvent.getDicePatternsGUI().get(currentPlayerIndex), tmp, 29,39);

    }

    public int getCurrentPlayerIndexInOurGridPane(String currentPlayer){
        for(int i = 0; i < ourGridPaneList.size(); i++){
            if(ourGridPaneList.get(i).getPlayerName().equals(currentPlayer))
                return  i;
        }
        return -1;
    }

    public OurGridPane getCurrentPlayerOurGridPane(String currentPlayer){
        for(OurGridPane ourGridPane : ourGridPaneList){
            if (ourGridPane.getPlayerName().equals(currentPlayer))
                return ourGridPane;

        }
        return ourGridPaneList.get(0); //non dovrei mai arrivare quì
    }



    //sto supponendo che il metodo getDicePatternsStringGUI mi resituisca una lista con tutti i path, anche quelli che non sono cambiati. Se passi "null" lascia vuoto (perchè non trova nessuna immagine con questo nome)
    public void updateDicePattern(List<String> list, GridPane gridPane, int height , int width){
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

    public void myUpdateDicePattern(List<String> paths, GridPane dicePatternGridPane, int height, int width) {
        int column = 0;
        int row = 0;
        for (String path : paths) {
            Object foundNode = getNodeByRowColumnIndex(row, column, dicePatternGridPane, 5);
            if (foundNode instanceof ImageView) {
                ImageView img = (ImageView) foundNode;
                addImageToImageView(path, img, height, width);
            }
            if (column != 4)
                column++;
            else {
                row++;
                column = 0;
            }

        }
    }

    //TODO: decommentare
    public void updateRoundTrack(MVEvent event){
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

    public void myUpdateRoundTrack(List<String> paths) {
        int column = 0;
        int row = 0;
        for (String path : paths) {
            Object foundNode = getNodeByRowColumnIndex(row, column, roundTrackGridPane, 9);
            if (foundNode instanceof ImageView) {
                ImageView img = (ImageView) foundNode;
                addImageToImageView(path, img, 39, 49);
            }
            if (column != 8)
                column++;
            else {
                row++;
                column = 0;
            }

        }
    }

    //TODO: decommentare
    //suppongo che il metodo getToolCardsGUI mi passi il path e l'id della toolcard associato al path
    public void updateToolCards(MVEvent event){
        ToolCardEvent toolCardEvent = (ToolCardEvent) event;
        addImageToImageView(toolCardEvent.getToolCardsGUI().get(0),toolCard1,144,95);
        idToolCard1 = Integer.parseInt(toolCardEvent.getToolCardsGUI().get(1));
        addImageToImageView(toolCardEvent.getToolCardsGUI().get(2),toolCard2,144,95);
        idToolCard2 = Integer.parseInt(toolCardEvent.getToolCardsGUI().get(3));
        addImageToImageView(toolCardEvent.getToolCardsGUI().get(4),toolCard3,144,95);
        idToolCard3 = Integer.parseInt(toolCardEvent.getToolCardsGUI().get(5));
    }

    public void myUpdateToolCards(List<String> paths) {
        addImageToImageView(paths.get(0), toolCard1, 144, 95);
        idToolCard1 = Integer.parseInt(paths.get(1));
        addImageToImageView(paths.get(2), toolCard2, 144, 95);
        idToolCard2 = Integer.parseInt(paths.get(3));
        addImageToImageView(paths.get(4), toolCard3, 144, 95);
        idToolCard3 = Integer.parseInt(paths.get(5));
    }

    public void grozingPliersWindow() {
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


    //TODO: forse posso sostituire isToolcard1selected ecc con isToolCardSelected
    public void getDiceIndexFromDraftPool(ImageView imageView) {
        System.out.println("sono nella draftpool");
        imageView.setEffect(setBorderGlow());
        diceChosenFromDraftPool = imageView;
        diceIndexDraftPool = GridPane.getColumnIndex(imageView);
        System.out.println(diceIndexDraftPool+1);
        if ((!isToolCard1Selected && !isToolCard2Selected && !isToolCard3Selected) || (idToolCardSelected == 8 || idToolCardSelected == 9)) {
            //hai selezionato un dado nella draftpool, quindi disattivo tutto a parte il dice pattern e aspetto che ci sia un click sul dicepattern
            //diceMoved = true;
            //disableAllExceptDicePattern();
        } else if (idToolCardSelected == 1) {
            grozingPliersWindow();
            VCEvent event = new GrozingPliersEvent(Integer.toString(diceIndexDraftPool + 1) + " " + Integer.toString(choiceGrozingPliers));
            setChanged();
            notifyObservers(event);
            //ableMajorObjects();
            diceChosenFromDraftPool.setEffect(null);
        } else if (idToolCardSelected == 5) {
            //disableAllExceptRoundTrack();
        } else if (idToolCardSelected == 6) {
            VCEvent event = new FluxBrushChooseDiceEvent(Integer.toString(diceIndexDraftPool + 1));
            System.out.println("tool card6:" + Integer.toString(diceIndexDraftPool + 1));
            setChanged();
            notifyObservers(event);
            //disableAllExceptDicePattern();
            diceChosenFromDraftPool.setEffect(null);
        } else if (idToolCardSelected == 10) {
            VCEvent event = new GrindingStoneEvent(Integer.toString(diceIndexDraftPool + 1));
            System.out.println("tool card 10:" + Integer.toString(diceIndexDraftPool + 1));
            setChanged();
            notifyObservers(event);
            diceChosenFromDraftPool.setEffect(null);
        } else if (idToolCardSelected == 11) {
            VCEvent event = new FluxRemoverChooseDiceEvent(Integer.toString(diceIndexDraftPool + 1));
            System.out.println("tool card 11:" + Integer.toString(diceIndexDraftPool + 1));
            setChanged();
            notifyObservers(event);
            diceChosenFromDraftPool.setEffect(null);
            fluxRemoverWindow();
            //disableAllExceptDicePattern(); //TODO: da controllare
        }

    }


    public void fluxRemoverWindow() {
        Stage window = new Stage();
        //Blocks interaction with the caller Scene
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Scelta");
        window.setMinWidth(400);
        window.setMinHeight(225);
        window.setResizable(false);

        VBox vBox = new VBox(5);
        HBox hBox = new HBox(5);
        TextField textField = new TextField();
        textField.setPrefHeight(31);
        textField.setPrefWidth(187);

        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(30);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);

        //Sets the message and the button
        Label label = new Label("Inserire il valore del dado");
        Button confirmButton = new Button("Conferma");

        vBox.getChildren().addAll(label, textField, hBox);
        hBox.getChildren().addAll(confirmButton);

        confirmButton.setOnAction(event -> {
            if (!textField.getText().equals("")) {
                choiceFluxRemover = Integer.parseInt(textField.getText());
                window.close();
            }
        });

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.showAndWait();
    }

    public void tapWheelWindow() {
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
    public void getDicePositionFromDicePattern(ImageView imageView) {
        int row = GridPane.getRowIndex(imageView);
        int column = GridPane.getColumnIndex(imageView);
        System.out.println("riga: "+row);
        System.out.println("colonna:"+ column);
        if (!isToolCard1Selected && !isToolCard2Selected && !isToolCard3Selected && !diceMoved) {
            dicePatternPosition = new Position(row, column);
            System.out.println("mossa su dice pattern:" + Integer.toString(diceIndexDraftPool+1) + " " + Integer.toString(dicePatternPosition.getX()) + " " + Integer.toString(dicePatternPosition.getY()));
            VCEvent event = new PlaceDiceEvent(Integer.toString(diceIndexDraftPool + 1) + " " + Integer.toString(dicePatternPosition.getX() + 1) + " " + Integer.toString(dicePatternPosition.getY() + 1));
            setChanged();
            notifyObservers(event);
            diceChosenFromDraftPool.setEffect(null);
            diceMoved = true;
            //ableMajorObjects();
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
            //ableMajorObjects();
        } else if (idToolCardSelected == 3 && step == 2) {
            finalPosition = new Position(row, column);
            step = 1;
            System.out.println("tool card 3 :" + Integer.toString(initialPosition.getX() + 1) + " " + Integer.toString(initialPosition.getY() + 1) + " " + Integer.toString(finalPosition.getX() + 1) + " " + Integer.toString(finalPosition.getY() + 1));
            VCEvent event = new CopperFoilBurnisherEvent(Integer.toString(initialPosition.getX() + 1) + " " + Integer.toString(initialPosition.getY() + 1) + " " + Integer.toString(finalPosition.getX() + 1) + " " + Integer.toString(finalPosition.getY() + 1));
            setChanged();
            notifyObservers(event);
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
            //ableMajorObjects();
        } else if (idToolCardSelected == 6) {
            dicePatternPosition = new Position(row, column);
            VCEvent event = new FluxBrushPlaceDiceEvent(Integer.toString(dicePatternPosition.getX() + 1) + " " + Integer.toString(dicePatternPosition.getY() + 1));
            System.out.println("tool card 6: " + Integer.toString(dicePatternPosition.getX() + 1) + " " + Integer.toString(dicePatternPosition.getY() + 1));
            setChanged();
            notifyObservers(event);
            //ableMajorObjects();
        } else if (idToolCardSelected == 8) {
            dicePatternPosition = new Position(row, column);
            VCEvent event = new RunnerPliersEvent(Integer.toString(diceIndexDraftPool + 1) + " " + Integer.toString(dicePatternPosition.getX() + 1) + " " + Integer.toString(dicePatternPosition.getY() + 1));
            System.out.println("tool card 8: " + Integer.toString(diceIndexDraftPool + 1) + " " + Integer.toString(dicePatternPosition.getX() + 1) + " " + Integer.toString(dicePatternPosition.getY() + 1));
            setChanged();
            notifyObservers(event);
            //ableMajorObjects();
        } else if (idToolCardSelected == 9) {
            dicePatternPosition = new Position(row, column);
            VCEvent event = new CorkBakedStraightedgeEvent(Integer.toString(diceIndexDraftPool + 1) + " " + Integer.toString(dicePatternPosition.getX() + 1) + " " + Integer.toString(dicePatternPosition.getY() + 1));
            System.out.println("tool card 9 :" + Integer.toString(diceIndexDraftPool + 1) + " " + Integer.toString(dicePatternPosition.getX() + 1) + " " + Integer.toString(dicePatternPosition.getY() + 1));
            setChanged();
            notifyObservers(event);
            //ableMajorObjects();
        } else if (idToolCardSelected == 11) {
            dicePatternPosition = new Position(row, column);
            VCEvent event = new FluxRemoverPlaceDiceEvent(Integer.toString(choiceFluxRemover) + " " + Integer.toString(dicePatternPosition.getX() + 1) + " " + Integer.toString(dicePatternPosition.getY() + 1));
            System.out.println("tool card 11: " + Integer.toString(choiceFluxRemover) + " " + Integer.toString(dicePatternPosition.getX() + 1) + " " + Integer.toString(dicePatternPosition.getY() + 1));
            setChanged();
            notifyObservers(event);
            //ableMajorObjects();
        } else if (idToolCardSelected == 12 && step == 2) {
            finalPosition = new Position(row, column);
            if (choiceTapWheel == 1) {
                step = 1;
                VCEvent event = new TapWheelEvent(Integer.toString(roundIndex + 1) + " " + Integer.toString(diceIndexRoundTrack + 1) + " " + Integer.toString(initialPosition.getX() + 1) + " " + Integer.toString(initialPosition.getY() + 1) + " " + Integer.toString(finalPosition.getX() + 1) + " " + Integer.toString(finalPosition.getY() + 1));
                System.out.println("tool card 12: " + Integer.toString(roundIndex + 1) + " " + Integer.toString(diceIndexRoundTrack + 1) + " " + Integer.toString(initialPosition.getX() + 1) + " " + Integer.toString(initialPosition.getY() + 1) + " " + Integer.toString(finalPosition.getX() + 1) + " " + Integer.toString(finalPosition.getY() + 1));
                setChanged();
                notifyObservers(event);
                //ableMajorObjects();
            } else
                step++;
        } else if (idToolCardSelected == 12 && step == 4) {
            finalPosition2 = new Position(row, column);
            step = 1;
            VCEvent event = new TapWheelEvent(Integer.toString(roundIndex + 1) + " " + Integer.toString(diceIndexRoundTrack + 1) + " " + Integer.toString(initialPosition.getX() + 1) + " " + Integer.toString(initialPosition.getY() + 1) + " " + Integer.toString(initialPosition2.getX() + 1) + " " + Integer.toString(initialPosition2.getY() + 1) + " " + Integer.toString(finalPosition.getX() + 1) + " " + Integer.toString(finalPosition.getY() + 1) + " " + Integer.toString(finalPosition2.getX() + 1) + " " + Integer.toString(finalPosition2.getY() + 1));
            System.out.println("tool card 12: " + Integer.toString(roundIndex + 1) + " " + Integer.toString(diceIndexRoundTrack + 1) + " " + Integer.toString(initialPosition.getX() + 1) + " " + Integer.toString(initialPosition.getY() + 1) + " " + Integer.toString(initialPosition2.getX() + 1) + " " + Integer.toString(initialPosition2.getY() + 1) + " " + Integer.toString(finalPosition.getX() + 1) + " " + Integer.toString(finalPosition.getY() + 1) + " " + Integer.toString(finalPosition2.getX() + 1) + " " + Integer.toString(finalPosition2.getY() + 1));
            setChanged();
            notifyObservers(event);
            //ableMajorObjects();
        }

    }


    //tool card che interagiscono con il round track : 5, 12
    public void getDicePositionFromRoundTrack(ImageView imageView) {
        roundIndex = 9 - GridPane.getRowIndex(imageView);
        diceIndexRoundTrack = GridPane.getColumnIndex(imageView);
        System.out.println("riga" + roundIndex);
        System.out.println("colonna" + diceIndexRoundTrack);
        if (idToolCardSelected == 5) {
            VCEvent event = new LensCutterEvent(Integer.toString(diceIndexDraftPool + 1) + " " + Integer.toString(roundIndex + 1) + " " + Integer.toString(diceIndexRoundTrack + 1));
            System.out.println("tool card 5 :" + Integer.toString(diceIndexDraftPool + 1) + " " + Integer.toString(roundIndex + 1) + " " + Integer.toString(diceIndexRoundTrack + 1));
            setChanged();
            notifyObservers(event);
            //ableMajorObjects();
            diceChosenFromDraftPool.setEffect(null);
        } else if (idToolCardSelected == 12) {
            tapWheelWindow();
            //disableAllExceptDicePattern();
        }

    }

    //TODO: quando clicco su una toolcard, in base all'id devo lanciare un altro metodo. un metodo per ogni toolcard che lasicia solo le cose che devo premere


    public Object getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane, int dimension) {
        return gridPane.getChildren().get(dimension * row + column);
    }


    public void addImageToImageView(String path, ImageView imageView, int height, int width) {
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

    public GridPane getGridPane1(){
        return dicePatternGridPane1;
    }

    public GridPane getGridPane2(){
        return dicePatternGridPane2;
    }

}

