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
    @FXML private ImageView publicObjectiveCard3;

    @FXML private ImageView windowPattern4;
    @FXML private ImageView windowPattern3;
    @FXML private ImageView windowPattern2;

    @FXML private GridPane dicePatternGridPane4;
    @FXML private GridPane dicePatternGridPane3;
    @FXML private GridPane dicePatternGridPane2;

    @FXML private Label toolCard1FavorTokensLabel;
    @FXML private Label toolCard2FavorTokensLabel;
    @FXML private Label toolCard3FavorTokensLabel;


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




    private VCEvent vcEvent;

    //private String eventParameters = "";

    private Map<Integer, Runnable> vcEvents = new HashMap<>();




    private int idToolCard1;
    private int idToolCard2;
    private int idToolCard3;

    private OurGridPane ourGridPane2;
    private OurGridPane ourGridPane3;
    private OurGridPane ourGridPane4;

    private final String favorTokensPath = "src/main/Images/Others/FavorToken.png";



    @FXML
    void initialize() {

        toolCardImageList.add(toolCard1);
        toolCardImageList.add(toolCard2);
        toolCardImageList.add(toolCard3);

        abstractInitialize();


        initializeGridPaneImagesView(dicePatternGridPane2, 4, 5, 29, 39);
        initializeGridPaneImagesView(dicePatternGridPane3, 4, 5, 29, 39);
        initializeGridPaneImagesView(dicePatternGridPane4, 4, 5, 29, 39);

        favorTokensPlayer1ImageView.setImage(new Image(ViewGUI.getUrlFromPath(favorTokensPath)));
        favorTokensToolCard1ImageView.setImage(new Image(ViewGUI.getUrlFromPath(favorTokensPath)));
        favorTokensToolCard2ImageView.setImage(new Image(ViewGUI.getUrlFromPath(favorTokensPath)));
        favorTokensToolCard3ImageView.setImage(new Image(ViewGUI.getUrlFromPath(favorTokensPath)));

        Image background = new Image(ViewGUI.getUrlFromPath("src/main/Images/Others/sagradaBackground.png"));
        BackgroundImage myBGI = new BackgroundImage(background, null, null,null, null);
        pane.setBackground(new Background(myBGI));

        privateObjectiveCard.setOnMouseEntered(event -> zoomIn(privateObjectiveCard));
        privateObjectiveCard.setOnMouseExited(event -> zoomOut(privateObjectiveCard));

        publicObjectiveCard3.setOnMouseEntered(event -> zoomInPOC(publicObjectiveCard3));
        publicObjectiveCard3.setOnMouseExited(event -> zoomOutPOC(publicObjectiveCard3));

        toolCard1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> useToolCard1());
        toolCard2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> useToolCard2());
        toolCard3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> useToolCard3());




    }

    void updatePublicObjectiveCards(List<String> publicObjectiveCards) {

        addImageToImageView(publicObjectiveCards.get(0), publicObjectiveCard1, 144, 95);
        addImageToImageView(publicObjectiveCards.get(1), publicObjectiveCard2, 144, 95);
        addImageToImageView(publicObjectiveCards.get(2),publicObjectiveCard3,144,95);
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

    //HA un setChanged e notify
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

    @Override
    public void update(Observable o, Object event) {
        mvEvent = (MVEvent) event;
        mvEvents.get(mvEvent.getId()).run();
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

        super.skipTurnAbstract();
        setChanged();
        notifyObservers(new SkipTurnEvent());


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


}

