package it.polimi.se2018.view.gui;

import it.polimi.se2018.events.mvevent.*;
import it.polimi.se2018.events.vcevent.*;
import it.polimi.se2018.model.Position;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.util.*;

public abstract class GameControllerAbstract extends Observable{

    @FXML protected Button skipTurnButton;
    @FXML protected Label turnLabel;

    @FXML protected AnchorPane pane;

    @FXML protected Button deleteMoveButton;

    @FXML protected ImageView publicObjectiveCard1;
    @FXML protected ImageView publicObjectiveCard2;

    @FXML protected ImageView windowPattern1;
    @FXML protected GridPane dicePatternGridPane1;

    @FXML protected GridPane draftPool;

    @FXML protected ImageView roundTrack;
    @FXML GridPane roundTrackGridPane;

    @FXML  ImageView dicePatternImage00;
    @FXML  ImageView dicePatternImage01;
    @FXML  ImageView dicePatternImage02;
    @FXML  ImageView dicePatternImage03;
    @FXML  ImageView dicePatternImage04;
    @FXML  ImageView dicePatternImage10;
    @FXML  ImageView dicePatternImage11;
    @FXML  ImageView dicePatternImage12;
    @FXML  ImageView dicePatternImage13;
    @FXML  ImageView dicePatternImage14;
    @FXML  ImageView dicePatternImage20;
    @FXML  ImageView dicePatternImage21;
    @FXML  ImageView dicePatternImage22;
    @FXML  ImageView dicePatternImage23;
    @FXML  ImageView dicePatternImage24;
    @FXML  ImageView dicePatternImage30;
    @FXML  ImageView dicePatternImage31;
    @FXML  ImageView dicePatternImage32;
    @FXML  ImageView dicePatternImage33;
    @FXML  ImageView dicePatternImage34;

    @FXML Label player1NameLabel;

    boolean imageViewsSetup = false;

    OurGridPane ourGridPane1;
    List<OurGridPane> ourGridPaneList = new ArrayList<>();

    ImageView diceChosenFromDraftPool;
    private ImageView diceChosenFromDicePattern;
    ImageView toolCardSelected;
    private Position dicePatternPosition;
    ImageView tmpImageView;

    Map<Integer, Runnable> mvEvents = new HashMap<>();
    Map<Integer, Runnable> diceIndexMap = new HashMap<>();
    private Map<Integer, Runnable> dicePositionFromDicePatternMap = new HashMap<>();
    MVEvent mvEvent;
    private int dicePatternRowPosition;
    private int dicePatternColumnPosition;

    boolean diceMoved = false;

    int idToolCardSelected = 0;

    Position initialPosition;
    Position finalPosition;
    Position initialPosition2;
    Position finalPosition2;

    List<ImageView> listDicePattern = new ArrayList<>();

    int roundIndex;
    int step = 1;

    boolean isToolCardSelected = false;

    List<ImageView> toolCardImageList = new ArrayList<>();

    int choiceTapWheel;
    int choiceGrozingPliers;
    int choiceFluxRemover;
    int diceIndexRoundTrack;
    int diceIndexDraftPool;

    final String yourTurnText = "È il tuo turno";
    final String waitText = "Attendi";


    void abstractInitialize(){

        initializeGridPaneImagesView(roundTrackGridPane, 10, 9, 39, 49);
        initializeGridPaneImagesView(dicePatternGridPane1, 4, 5, 59, 69);
        initializeGridPaneImagesView(draftPool, 1, 9, 59, 69);

        addImageToImageView("src/main/Images/Others/RoundTrack.png",roundTrack,150,776);

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

        publicObjectiveCard1.setOnMouseExited(event -> zoomOutPOC(publicObjectiveCard1));
        publicObjectiveCard2.setOnMouseExited(event -> zoomOutPOC(publicObjectiveCard2));


        skipTurnButton.setOnMouseClicked(event -> skipTurn());
        deleteMoveButton.setOnMouseClicked(event -> handleDraftPoolAndToolCards());

        for(ImageView toolCardImage : toolCardImageList) {
            toolCardImage.setOnMouseEntered(event -> zoomIn(toolCardImage));
            toolCardImage.setOnMouseExited(event -> zoomOut(toolCardImage));
        }

        initializeDraftPoolEventHandler(draftPool);
        initializeRoundTrackEventHandler(roundTrackGridPane);
        initializeDicePatternEventHandler(dicePatternGridPane1);

        createMVMapAbstract();
        createDiceIndexMap();
        createDicePositionFromDicePatternMap();

        dicePatternGridPane1.toFront();
        roundTrackGridPane.toFront();

        roundTrackGridPane.setDisable(true);
        dicePatternGridPane1.setDisable(true);
    }

    private void initializeDicePatternEventHandler(GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof ImageView) {
                ImageView tmp = (ImageView) node;
                tmp.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> getDicePositionFromDicePattern(tmp));
            }
        }
    }

    private void handleDraftPool(){
        if(diceMoved)
            disableDraftPool();
        else
            enableDraftPool();
    }

    void disableDraftPool(){
        draftPool.setDisable(true);
        setDraftPoolOpacity(0.5);
    }

    void enableDraftPool(){
        draftPool.setDisable(false);
        setDraftPoolOpacity(1);
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

    void handleDraftPoolAndToolCards(){
        handleDraftPool();
        handleToolCardsEffect();
        disableGridPane(roundTrackGridPane,roundTrack);
        disableGridPane(dicePatternGridPane1,windowPattern1);
        if(diceChosenFromDraftPool != null)
            diceChosenFromDraftPool.setEffect(null);
        if(toolCardSelected != null)
            toolCardSelected.setEffect(null);
    }

    void skipTurnAbstract(){

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
    }

    abstract void skipTurn();


    void addImageToImageView(String path, ImageView imageView, int height, int width) {
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

    void zoomIn(ImageView imageView) {
        imageView.setFitHeight(470);
        imageView.setFitWidth(357);
        imageView.toFront();
        imageView.setPreserveRatio(true);
    }

    void zoomOut(ImageView imageView) {
        imageView.setFitHeight(144);
        imageView.setFitWidth(95);
        imageView.toBack();
        imageView.setPreserveRatio(true);
    }


    void zoomInPOC(ImageView imageView){

        imageView.setLayoutY(imageView.getLayoutY()-326);
        zoomIn(imageView);
    }

    void zoomOutPOC(ImageView imageView){

        imageView.setLayoutY(imageView.getLayoutY()+326);
        zoomOut(imageView);
    }

    Object getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane, int dimension) {
        return gridPane.getChildren().get(dimension * row + column);
    }

    void initializeGridPaneImagesView(GridPane gridPane, int row, int column, int height, int width) {
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



    DropShadow setBorderGlow() {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.BLUE);
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);
        borderGlow.setHeight(45);
        return borderGlow;
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

    void disableToolCards(){
        for(ImageView toolCardImage : toolCardImageList){
            toolCardImage.setDisable(true);
            toolCardImage.setOpacity(0.5);
        }
    }

    private void enableToolCards(){
        for(ImageView toolCardImage : toolCardImageList) {
            toolCardImage.setDisable(false);
            toolCardImage.setOpacity(1);
        }
    }

    void handleToolCardsEffect(){
        if(!isToolCardSelected)
            enableToolCards();
        else
            disableToolCards();
    }

    void disableGridPane(GridPane gridPane, ImageView imageView){
        setDisableGridPane(gridPane);
        gridPane.setDisable(true);
        imageView.setOpacity(0.5);
    }

    private void setDisableGridPane(GridPane gridPane){
        for(Node node : gridPane.getChildren()){
            if(node instanceof  ImageView)
                node.setDisable(true);
        }
    }

    void enableGridPane(GridPane gridPane, ImageView imageView){
        setEnableGridPane(gridPane);
        gridPane.setDisable(false);
        imageView.setOpacity(1);
    }



    private void setEnableGridPane(GridPane gridPane){
        for(Node node : gridPane.getChildren()){
            if(node instanceof  ImageView)
                node.setDisable(false);
        }
    }


    void grozingPliersWindow() {
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

    void fluxRemoverWindow() {
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
            window.close();
        });

        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(choiceBox, confirmButton);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.showAndWait();
    }

    void tapWheelWindow() {
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

    private void createMVMapAbstract() {
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
        mvEvents.put(11, () -> {});
        mvEvents.put(12, this::handleEndTurnEvent);
        mvEvents.put(13, ()-> {});
        mvEvents.put(14, ()-> {});
        mvEvents.put(15, ()->{});
        mvEvents.put(16, this::playerSuspended);
    }



    private void showAll(MVEvent event){
        System.out.println("sono in uno showAll");
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
    }

    private void handleEndTurnEvent(){

        showEndTurn();
        turnLabel.setStyle("-fx-background-color: #ff0000;");
        Platform.runLater(()-> turnLabel.setText(waitText));
        skipTurnButton.setDisable(true);
        deleteMoveButton.setDisable(true);
    }

    private void showEndTurn(){
        Platform.runLater(() ->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Avviso");
            alert.setContentText("Turno terminato. Attendi.");

            alert.showAndWait();
        });
    }


    abstract void playerSuspended();

    abstract void createAssociationWithOurGridPane(MVEvent mvEvent1, MVEvent mvEvent2);

    abstract void updatePrivateObjectiveCards(List<String> paths);

    void showErrorAbstract(ErrorEvent errorEvent){

        Platform.runLater(()-> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setContentText(errorEvent.getMessageToDisplay());
            alert.showAndWait();
        });
    }

    abstract void showError(MVEvent mvEvent);

    abstract void updateDicePatterns(MVEvent mvEvent);

    abstract void updateFavorTokens(MVEvent mvEvent);

    abstract void updateToolCards(MVEvent mvEvent);

    private void handleActionMenu(MVEvent event){
        ActionMenuEvent actionMenuEvent = (ActionMenuEvent) event;
        diceMoved = actionMenuEvent.isDiceMoved();
        isToolCardSelected = actionMenuEvent.isToolCardUsed();
        handleDraftPoolAndToolCards();
    }

    abstract void updatePublicObjectiveCards(List<String> publicObjectiveCards);

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

    abstract void handleToolCards(int idToolCard);

    void updateDicePattern(List<String> list, GridPane gridPane, int height , int width){
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


    private void updateDraftPool(MVEvent event) {
        cleanDraftPool();
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

    private void cleanDraftPool(){

        for(int column = 0; column <9 ; column++){
            Object foundNode = getNodeByRowColumnIndex(0,column, draftPool,0 );
            if (foundNode instanceof ImageView) {
                ImageView img = (ImageView) foundNode;
                addImageToImageView(" ",img,59,69);
            }
        }
    }

    //tool card che interagiscono con il round track : 5, 12
    void getDicePositionFromRoundTrack(ImageView imageView) {
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

    void createDiceIndexMap(){

        diceIndexMap.put(0, this::putDiceOnDicePattern);
        diceIndexMap.put(1, this::grozinPliers);
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
        diceIndexMap.put(10, this::grindingStone);
        diceIndexMap.put(11, this::fluxRemoverChooseDice);
        diceIndexMap.put(12, () -> {});

    }

    private void putDiceOnDicePattern(){
        if(!diceMoved){
            disableToolCards();
            disableDraftPool();
            enableGridPane(dicePatternGridPane1,windowPattern1);
        }
    }
    private void fluxRemoverChooseDice(){

        VCEvent event = new FluxRemoverChooseDiceEvent(Integer.toString(diceIndexDraftPool + 1));
        System.out.println("tool card 11:" + Integer.toString(diceIndexDraftPool + 1));
        setChanged();
        notifyObservers(event);
        diceChosenFromDraftPool.setEffect(setBorderGlow());
        fluxRemoverWindow();
        disableDraftPool();
        enableGridPane(dicePatternGridPane1,windowPattern1);
    }

    private void grozinPliers(){
        grozingPliersWindow();
        VCEvent event = new GrozingPliersEvent(Integer.toString(diceIndexDraftPool + 1) + " " + Integer.toString(choiceGrozingPliers));
        setChanged();
        notifyObservers(event);
        diceChosenFromDraftPool.setEffect(null);
        handleDraftPool();
        disableToolCards();
        idToolCardSelected = 0;
        toolCardSelected.setEffect(null);
    }

    private void fluxBrushChooseDice(){
        VCEvent event = new FluxBrushChooseDiceEvent(Integer.toString(diceIndexDraftPool + 1));
        System.out.println("tool card6:" + Integer.toString(diceIndexDraftPool + 1));
        setChanged();
        notifyObservers(event);
        disableDraftPool();
        enableGridPane(dicePatternGridPane1,windowPattern1);
    }

    private void grindingStone(){

        VCEvent event = new GrindingStoneEvent(Integer.toString(diceIndexDraftPool + 1));
        System.out.println("tool card 10:" + Integer.toString(diceIndexDraftPool + 1));
        setChanged();
        notifyObservers(event);
        diceChosenFromDraftPool.setEffect(null);
        handleDraftPool();
        idToolCardSelected = 0;
        disableToolCards();
        toolCardSelected.setEffect(null);
    }


    void getDiceIndexFromDraftPool(ImageView imageView) {
        System.out.println("sono nella draftpool");
        imageView.setEffect(setBorderGlow());
        diceChosenFromDraftPool = imageView;
        diceIndexDraftPool = GridPane.getColumnIndex(imageView);
        System.out.println(diceIndexDraftPool+1);

        diceIndexMap.get(idToolCardSelected).run();

    }

    private void initializeRoundTrackEventHandler(GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof ImageView) {
                ImageView tmp = (ImageView) node;
                tmp.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> getDicePositionFromRoundTrack(tmp));
            }
        }
    }

    private void initializeDraftPoolEventHandler(GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof ImageView) {
                ImageView tmp = (ImageView) node;
                tmp.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> getDiceIndexFromDraftPool(tmp));
            }
        }
    }

    private void getDicePositionFromDicePattern(ImageView imageView) {
        dicePatternRowPosition = GridPane.getRowIndex(imageView);
        dicePatternColumnPosition = GridPane.getColumnIndex(imageView);

        dicePositionFromDicePatternMap.get(idToolCardSelected).run();
    }

    private void placeDiceMove(int row, int column){
        if(!diceMoved){
            dicePatternPosition = new Position(row, column);
            System.out.println("mossa su dice pattern:" + Integer.toString(diceIndexDraftPool+1) + " " + Integer.toString(dicePatternPosition.getX()) + " " + Integer.toString(dicePatternPosition.getY()));
            VCEvent event = new PlaceDiceEvent(Integer.toString(diceIndexDraftPool + 1) + " " + Integer.toString(dicePatternPosition.getX() + 1) + " " + Integer.toString(dicePatternPosition.getY() + 1));
            setChanged();
            notifyObservers(event);
            diceChosenFromDraftPool.setEffect(null);
            disableGridPane(dicePatternGridPane1, windowPattern1);
            handleToolCardsEffect();
        }
    }

    private void eglomiseBrush(int row, int column){
        if(step == 1)
            firstStep(row, column);
        else if(step == 2)
            eglomiseBrushSecondStep(row, column);
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

    private void copperFoilBurnisher(int row, int column){
        if(step == 1)
            firstStep(row, column);
        else if(step == 2)
            copperFoilBurnisherSecondStep(row, column);
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

    private void lathekin(int row, int column){
        if(step == 1)
            firstStep(row, column);
        else if(step == 2)
            lathekinSecondStep(row, column);
        else if(step == 3){
            initialPosition2 = new Position(row, column);
            step++;
        }
        else if(step == 4)
            lathekinForthStep(row, column);
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

    private void tapWheel(int row, int column){
        if(step == 1)
            firstStep(row, column);
        else if(step == 2)
            tapWheelSecondStep(row, column);
        else if(step == 3){
            initialPosition2 = new Position(row, column);
            step++;
        }
        else if(step == 4)
            tapWheelForthStep(row, column);

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

    private void firstStep(int row, int column){

        initialPosition = new Position(row, column);
        Object node = getNodeByRowColumnIndex(row,column,dicePatternGridPane1,5);
        diceChosenFromDicePattern = (ImageView) node;
        diceChosenFromDicePattern.setEffect(setBorderGlow());
        step++;
    }


    private void createDicePositionFromDicePatternMap(){

        dicePositionFromDicePatternMap.put(0, () -> placeDiceMove(dicePatternRowPosition, dicePatternColumnPosition));
        dicePositionFromDicePatternMap.put(1, ()->{});
        dicePositionFromDicePatternMap.put(2, () -> eglomiseBrush(dicePatternRowPosition, dicePatternColumnPosition));
        dicePositionFromDicePatternMap.put(3, () -> copperFoilBurnisher(dicePatternRowPosition, dicePatternColumnPosition));
        dicePositionFromDicePatternMap.put(4, () -> lathekin(dicePatternRowPosition, dicePatternColumnPosition));
        dicePositionFromDicePatternMap.put(5, () -> {});
        dicePositionFromDicePatternMap.put(6, () -> fluxBrushPlaceDice(dicePatternRowPosition, dicePatternColumnPosition));
        dicePositionFromDicePatternMap.put(7, () -> {});
        dicePositionFromDicePatternMap.put(8, () -> runnerPliers(dicePatternRowPosition, dicePatternColumnPosition));
        dicePositionFromDicePatternMap.put(9, () -> corkBackedStraighedge(dicePatternRowPosition, dicePatternColumnPosition));
        dicePositionFromDicePatternMap.put(10, () ->{});
        dicePositionFromDicePatternMap.put(11, () -> fluxRemoverPlaceDice(dicePatternRowPosition, dicePatternColumnPosition));
        dicePositionFromDicePatternMap.put(12, () -> tapWheel(dicePatternRowPosition, dicePatternColumnPosition));
    }

}

