package minesweepergame;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Minesweeper game
 * @author nathan.hibbetts
 */
public class MinesweeperGame extends Application {
    private int time = 0; //Holds the time taken
    
    //Required for the timeline lambdas
    private final StringProperty valueProperty = new SimpleStringProperty();
    
    //Controller object
    private final Controller con = new Controller();
    
    //Timeline to update the timer
    private final Timeline timerUpdater = new Timeline();

    private final VBox vb = new VBox();
    private int gameSize = 13; //Board size is gameSize X gameSize
                                     //13 would be 13 X 13 or 169
                                     //16 would be 16 X 16 or 256
    private final int windowSizeX = 600; //Scene size
    private final int windowSizeY = 600;
    
    //Playing field UI
    private Button[][] arr = new Button[gameSize][gameSize];
    
    @Override
    public void start(Stage primaryStage) {
        HBox top = new HBox(360); //Holds the labels
        
        //Border pane that holds the menuBar and other borderpane
        BorderPane topPane = new BorderPane(); 
        
        //Borderpane that holds the top HBox and the playing field
        BorderPane borderPane = new BorderPane(); 
        
        //Label telling how many bombs are left
        Label bombsLeft = new Label("Bombs left: 0"); 
        
        //Label for the timer that shows how much time has gone by
        Label timer = new Label("0 seconds");
        
        //Create a new scene and give it CSS styling
        Scene scene = new Scene(topPane, windowSizeX, windowSizeY);
        scene.getStylesheets().add(MinesweeperGame.class.getResource("Minesweeper.css").toExternalForm());
        
         //Create a menu
        MenuBar menuBar = new MenuBar();
        final Menu fileMenu = new Menu("File");
        final Menu difficultyMenu = new Menu("Difficulties");

        //Add tabs to the menu bar
        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().add(difficultyMenu);

        //Create items to add to the menu tabs
        MenuItem restartMenu = new MenuItem("Restart");
        MenuItem easyMenu = new MenuItem("Easy");
        MenuItem normalMenu = new MenuItem("Normal");
        MenuItem hardMenu = new MenuItem("Hard");
        MenuItem expertMenu = new MenuItem("Expert");

        //Add the items to the menu tabs
        fileMenu.getItems().add(restartMenu);
        difficultyMenu.getItems().add(easyMenu);
        difficultyMenu.getItems().add(normalMenu);
        difficultyMenu.getItems().add(hardMenu);
        difficultyMenu.getItems().add(expertMenu); 
        
        //Set the restartMenu item to restart the game
         restartMenu.setOnAction(e ->{
            playAgain();
        });
        
         //Set the menu options for difficulty
         easyMenu.setOnAction(e ->{
             selectDifficulty("easy");
             primaryStage.sizeToScene();
         });
         normalMenu.setOnAction(e ->{
             selectDifficulty("normal");
             primaryStage.sizeToScene();
         });
         hardMenu.setOnAction(e ->{
             selectDifficulty("hard");
             primaryStage.sizeToScene();
         });
         expertMenu.setOnAction(e ->{
             selectDifficulty("expert");
             primaryStage.sizeToScene();
         });
        
        //Binds the time instance to the timer label
        timer.textProperty().bind(valueProperty);
        
        //Add the bombsLeft and timer labels to the hbox
        top.getChildren().add(bombsLeft);
        top.getChildren().add(timer);
        
        //Create the board
        playAgain();
        
        //Set the board
        con.setBoard(arr);
        
        //Timeline to update the ingame timer
        timerUpdater.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
            ae -> updateTime())); 
            timerUpdater.setCycleCount(Timeline.INDEFINITE);
            timerUpdater.play();
        
        //Timeline to update the bombsLeft
        Timeline bombsLeftUpdater = new Timeline();
        bombsLeftUpdater.getKeyFrames().add(new KeyFrame(Duration.millis(100),
            ae -> bombsLeft.setText("Bombs left: " + Integer.toString(con.getBombsLeft())))); 
            bombsLeftUpdater.setCycleCount(Timeline.INDEFINITE);
            bombsLeftUpdater.play();
        
        //Timeline that checks if the game ended
        Timeline gameChecker = new Timeline();
        gameChecker.getKeyFrames().add(new KeyFrame(Duration.millis(100),
            ae -> stopTime())); 
            gameChecker.setCycleCount(Timeline.INDEFINITE);
            gameChecker.play();
        
        //Borderpane that holds the playing field and timelines
        borderPane.setCenter(vb);
        borderPane.setTop(top);
        
        //Borderpane that puts the menuBar on top and game field in the middle
        topPane.setTop(menuBar);
        topPane.setCenter(borderPane);

        //If the enter key is pressed, restart the game
        scene.setOnKeyPressed(ev -> {
            if (ev.getCode() == KeyCode.ENTER){
                playAgain();
            }
        });
        
        //Set the primaryStage attributes
        primaryStage.setResizable(false);
        primaryStage.setTitle("Minesweeper");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Updates the game time
     */
    private void updateTime()
    {
        ++time;
        this.valueProperty.setValue(Integer.toString(time) + " seconds");
    }
    
    /**
     * Stops updating the game time when the game ends
     */
    private void stopTime()
    {
        if (con.checkGameOver() == true)
        {
            timerUpdater.stop();
        }
    }
    
    /**
     * Resets the game board to play again
     */
    private void playAgain()
    {
        int totalBombs = 0;
        HBox hb = new HBox();
        con.setGameSize(gameSize);
        switch (gameSize)
        {
            case 13: totalBombs = 15;
                break;
            case 16: totalBombs = 35;
                break;
            case 20: totalBombs = 80;
                break;
            case 25: totalBombs = 130;
                break;
        }
        do //Do-while the amount of bombs placed is not equal to or 
        {  //within a range of five of the totalBombs count
            hb.getChildren().clear(); //Clear all the children
            vb.getChildren().clear();
            con.resetGame(); //Reset all the values
            
            //Create a new board
            for (int i = 0; i < gameSize; ++i)
            {
                for (int ind = 0; ind < gameSize; ++ind)
                {
                    arr[i][ind] = con.setBomb(i, ind);
                    hb.getChildren().add(arr[i][ind]);
                    if (ind == gameSize - 1)
                    {
                        vb.getChildren().add(hb);
                        hb = new HBox();
                    }
                }
            }
        } while (con.getTotalBombs() > totalBombs + 5 || //Lee-way of five up or down
                con.getTotalBombs() < totalBombs - 5);   //To prevent too much memory
                                                      //From being used
        
        //Set the board
        con.setBoard(arr);
        this.valueProperty.setValue("0"); //Set the timer
        time = 0;
        timerUpdater.play(); //Start the timer timeline
    }
    
    private void selectDifficulty(String diff)
    {
        //Set the settings according to the selected difficulty
        switch (diff) {
            case "easy": //Game starts on easy
                gameSize = 13;
                arr = new Button[gameSize][gameSize];
                con.setDifficulty(9);
                break;
            case "normal":
                gameSize = 16;
                arr = new Button[gameSize][gameSize];
                con.setDifficulty(7);
                break;
            case "hard":
                gameSize = 20;
                arr = new Button[gameSize][gameSize];
                con.setDifficulty(5);
                break;
            case "expert":
                gameSize = 25;
                arr = new Button[gameSize][gameSize];
                con.setDifficulty(5);
                break;
            default:
                break;
        }
        playAgain(); //Restart the game
    }
    
    /**
     * This method returns the gameSize to the controller class
     * @return gameSize
     */
    protected int getGameSize()
    {
        return gameSize;
    }
}