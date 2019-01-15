package minesweepergame;

import java.util.Random;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * The controller class for the minesweeper game
 * @author nathan.hibbetts
 */
public class Controller {
    //Holds the array of buttons that make up the playing field
    private Button[][] arr; 
    private int bombCounter; //How many bombs are placed
    private int goodFlags; //How many correct flags are placed
    private int totalFlags; //How many total flags are placed
    private boolean gameOver = false; //If the game has ended
    private boolean firstClick = true; //if it's the first guess
    private int revealed = 0; //How many tiles have been revealed
    private Image flagImage; //Flag image
    private Image badFlagImage; //Bad flag image
    private Image bombImage;    //Bomb image
    private int total = 0; //The total amount of of tiles
    private int gameSize; //The gameSize gotten from the MinesweeperGame class
    private int difficulty = 9; //The selected difficulty from the MinesweeperGame class
    
    /**
     * Empty constructor
     */
    public Controller()
    {
          
    }
    
    /**
     * Sets this board with the one from the MinesweeperGame class
     * @param a The button array
     */
    protected void setBoard(Button[][] a)
    {
        arr = a;
    }
    
    /**
     * This method sets a specified spot in the button array as either
     * a mine or a safe spot and adds to the number of bombs and tiles. 
     * It also tells each button what to do depending on if it's a bomb or safe.
     * @param x x-coordinate
     * @param y y-coordinate
     * @return a button for the UI in MinesweeperGame
     */
    protected Button setBomb(int x, int y)
    {
        switch (gameSize) //Decide how big the images are
        {
            case 13:
                flagImage = new Image(getClass().getResourceAsStream
                ("flag.png"), 30, 30, true, true); //Flag image
                badFlagImage = new Image(getClass().getResourceAsStream
                ("badflag.png"), 30, 30, true, true); //Bomb image
                bombImage = new Image(getClass().getResourceAsStream
                ("Bomb.png"), 30, 30, true, true); //Bomb image
                break;
            case 16:
                flagImage = new Image(getClass().getResourceAsStream
                ("flag.png"), 35, 30, true, true); //Flag image
                badFlagImage = new Image(getClass().getResourceAsStream
                ("badflag.png"), 35, 30, true, true); //Bomb image
                bombImage = new Image(getClass().getResourceAsStream
                ("Bomb.png"), 35, 30, true, true); //Bomb image
                break;
            case 20:
                flagImage = new Image(getClass().getResourceAsStream
                ("flag.png"), 25, 25, true, true); //Flag image
                badFlagImage = new Image(getClass().getResourceAsStream
                ("badflag.png"), 25, 25, true, true); //Bomb image
                bombImage = new Image(getClass().getResourceAsStream
                ("Bomb.png"), 25, 25, true, true); //Bomb image
                break;
            case 25:
                flagImage = new Image(getClass().getResourceAsStream
                ("flag.png"), 15, 20, true, true); //Flag image
                badFlagImage = new Image(getClass().getResourceAsStream
                ("badflag.png"), 15, 20, true, true); //Bomb image
                bombImage = new Image(getClass().getResourceAsStream
                ("Bomb.png"), 15, 20, true, true); //Bomb image
                break;
            default: flagImage = new Image(getClass().getResourceAsStream
                ("flag.png"), 25, 25, true, true); //Flag image
                badFlagImage = new Image(getClass().getResourceAsStream
                ("badflag.png"), 25, 25, true, true); //Bomb image
                bombImage = new Image(getClass().getResourceAsStream
                ("Bomb.png"), 25, 25, true, true); //Bomb image
                break;
        }
        
        ++total; //increases the total number of tiles placed
        Random ran = new Random(); //Random number generator
        //Get random number that decides if it's a bomb or safe spot
        int number = ran.nextInt(difficulty); 
        Button bt = new Button(); //Create a button
        bt.setMinSize(8, 8); //Set the size of the button
        
        if (number / 1 == 0) //If the button is decided to be a bomb
        {
            ++bombCounter; //Counts how many bombs were placed
           
            bt.setId("Bomb"); //Set the ID of the button to bomb
            bt.setText(null); //Set the text to null
            bt.setOnMouseClicked((MouseEvent event) -> {
                MouseButton button = event.getButton();
                if (button == MouseButton.SECONDARY) //RIGHT CLICK
                {
                    if (bt.getGraphic() == null && gameOver == false)
                    {
                        bt.setGraphic(new ImageView(flagImage)); //Set the image to a flag
                        ++goodFlags; //Increase the good flag counter
                        ++totalFlags;
                        ++revealed;
                        if (revealed == total && goodFlags == bombCounter)
                            gameWon();
                    }
                    else if (gameOver == false)
                    {
                        bt.setGraphic(null);
                        --goodFlags;
                        --totalFlags;
                        --revealed; //Counts how many spots were revealed
                    }
                }
                else //LEFT CLICK
                {
                    if (bt.getGraphic() == null && gameOver == false &&
                            firstClick == false && bt.getText() == null)
                    {
                        ++revealed;
                        bt.setText(testForBomb(x, y));
                        bt.setText(null);
                        bt.setGraphic(new ImageView(bombImage));
                    }
                        //If first click of the game
                    else if (gameOver == false && bt.getGraphic() == null &&
                            firstClick == true && bt.getText() == null) 
                    {
                        firstClick = false;
                        ++revealed;
                        --bombCounter;
                        bt.setId("Safe"); 
                        bt.setText(testForBomb(x, y));
                    }
                }
           });
        }
        else //If the button was decided to be safe
        {
            bt.setId("Safe"); //Put the safe ID on it
            bt.setText(null); //Set the text to null
            bt.setOnMouseClicked((MouseEvent event) -> {
                MouseButton button = event.getButton();
                if (button == MouseButton.SECONDARY) //RIGHT CLICK
                {
                    if (bt.getGraphic() == null && gameOver == false &&
                            bt.getText() == null)
                    {
                        bt.setGraphic(new ImageView(flagImage)); //Put flag image over button
                        --goodFlags; //Bad flag
                        ++totalFlags;
                        ++revealed;
                    }
                    else if (bt.getGraphic() != null && gameOver == false)
                    {
                        bt.setGraphic(null); //Take away the flag image
                        ++goodFlags; //Fixed the bad flag
                        --totalFlags;
                        --revealed;
                    }
                }
                else //If it's a left click
                {
                    if (bt.getGraphic() == null && bt.getText() == null &&
                            gameOver == false)
                    {
                        ++revealed;
                        firstClick = false; //First click was made
                        //Set the text for how many bombs are by this button
                        bt.setText(testForBomb(x, y));
                    } 
                    
                    if (bt.getText().equals("0"))
                    {
                        testZero(x, y);
                    }
                }
            });
        }
        return bt; //Return the button
    }

    /**
     * This method tests if the bomb left clicked on was a bomb
     * and counts how many bombs are next to it if it's a safe button
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the amount of bombs next to it
     */
    protected String testForBomb(int x, int y)
    {
        int counter = 0; //Counts the amount of bombs next to it
        if (arr[x][y].getId().equals("Bomb"))
            gameLost(); //Clicked on a bomb, display game over
        
        else //Check all of the squares touching this one and count if it's safe
        {
            
            if (goodFlags == bombCounter && revealed == total)
                gameWon();
             
            try
            {
                if (arr[x-1][y].getId().equals("Bomb"))
                {
                    ++counter;
                }
            }
            catch (IndexOutOfBoundsException a){}
            
            try
            {
                if (arr[x+1][y].getId().equals("Bomb"))
                {
                    ++counter;
                }
            }
            catch (IndexOutOfBoundsException b){}
            
            try
            {
                if (arr[x-1][y-1].getId().equals("Bomb"))
                {
                    ++counter;
                }
            }
            catch (IndexOutOfBoundsException c){}
            
            try
            {
                if (arr[x][y-1].getId().equals("Bomb"))
                {
                    ++counter;
                }
            }
            catch (IndexOutOfBoundsException d){}
            
            try
            {
                if (arr[x+1][y-1].getId().equals("Bomb"))
                {
                    ++counter;
                }
            }
            catch (IndexOutOfBoundsException e){}
            
            try
            {
                if (arr[x+1][y+1].getId().equals("Bomb"))
                {
                    ++counter;
                }
            }
            catch (IndexOutOfBoundsException f){}
            
            try
            {
                if (arr[x][y+1].getId().equals("Bomb"))
                {
                    ++counter;
                }
            }
            catch (IndexOutOfBoundsException g){}
            
            try
            {
                if (arr[x-1][y+1].getId().equals("Bomb"))
                {
                    ++counter;
                }
            }
            catch (IndexOutOfBoundsException h){}
            
            String count = Integer.toString(counter);
            return count;
        }
        return "-1";
    }
    
    /**
     * If player clicks on a zero spot, reveal all touching tiles and if
     * it reveals another zero, repeat on that spot.
     * @param x x-coordinate
     * @param y y-coordinate
     */
    private void testZero(int x, int y)
    {
            try 
            {   //If the text is null and the graphic is null, call testForBomb
                if (arr[x+1][y].getText() == null && arr[x+1][y].getGraphic() == null)
                {
                    arr[x + 1][y].setText(testForBomb(x + 1, y));
                    ++revealed;
                }
                
                //If the text is 0 and the Id does not equal "Safe-Checked"
                //Set the Id to "Safe-Checked" and recursively call this method again
                if (arr[x + 1][y].getText().equals("0") && !arr[x + 1][y].getId().equals("Safe-Checked"))
                {
                    arr[x + 1][y].setId("Safe-Checked");
                    testZero(x + 1, y);
                }
            }
            catch(IndexOutOfBoundsException | NullPointerException a){}
            
            try 
            {
                //If the text is null and the graphic is null, call testForBomb
                if (arr[x-1][y].getText() == null && arr[x-1][y].getGraphic() == null)
                {
                    arr[x - 1][y].setText(testForBomb(x - 1, y));
                    ++revealed;
                }
                
                //If the text is 0 and the Id does not equal "Safe-Checked"
                //Set the Id to "Safe-Checked" and recursively call this method again
                if (arr[x - 1][y].getText().equals("0") && !arr[x-1][y].getId().equals("Safe-Checked"))
                {
                    arr[x - 1][y].setId("Safe-Checked");
                    testZero(x - 1, y);
                }
            }
            catch(IndexOutOfBoundsException | NullPointerException a){}

            try 
            {
                //If the text is null and the graphic is null, call testForBomb
                if (arr[x+1][y+1].getText() == null && arr[x+1][y+1].getGraphic() == null)
                {
                    arr[x + 1][y + 1].setText(testForBomb(x + 1, y + 1));
                    ++revealed;
                }
                
                //If the text is 0 and the Id does not equal "Safe-Checked"
                //Set the Id to "Safe-Checked" and recursively call this method again
                if (arr[x + 1][y + 1].getText().equals("0") && !arr[x+1][y+1].getId().equals("Safe-Checked"))
                {
                    arr[x+1][y+1].setId("Safe-Checked");
                    testZero(x + 1, y + 1);
                }
            }
            catch(IndexOutOfBoundsException | NullPointerException a){}

            try 
            {
                //If the text is null and the graphic is null, call testForBomb
                if (arr[x-1][y+1].getText() == null && arr[x-1][y+1].getGraphic() == null)
                {
                    arr[x - 1][y + 1].setText(testForBomb(x - 1, y + 1));
                    ++revealed;
                }
                
                //If the text is 0 and the Id does not equal "Safe-Checked"
                //Set the Id to "Safe-Checked" and recursively call this method again
                if (arr[x - 1][y + 1].getText().equals("0") && !arr[x-1][y+1].getId().equals("Safe-Checked"))
                {
                    arr[x-1][y+1].setId("Safe-Checked");
                    testZero(x - 1, y + 1);
                }
            }
            catch(IndexOutOfBoundsException | NullPointerException a){}

            try 
            {
                //If the text is null and the graphic is null, call testForBomb
                if (arr[x][y+1].getText() == null && arr[x][y+1].getGraphic() == null)
                {
                    arr[x][y + 1].setText(testForBomb(x, y + 1));
                    ++revealed;
                }
                
                //If the text is 0 and the Id does not equal "Safe-Checked"
                //Set the Id to "Safe-Checked" and recursively call this method again
                if (arr[x][y + 1].getText().equals("0")&& !arr[x][y+1].getId().equals("Safe-Checked"))
                {
                    arr[x][y+1].setId("Safe-Checked");
                    testZero(x, y + 1);
                }
            }
            catch(IndexOutOfBoundsException | NullPointerException a){}

            try 
            {
                //If the text is null and the graphic is null, call testForBomb
                if (arr[x+1][y-1].getText() == null && arr[x+1][y-1].getGraphic() == null)
                {
                    arr[x + 1][y - 1].setText(testForBomb(x + 1, y - 1));
                    ++revealed;
                }
                
                //If the text is 0 and the Id does not equal "Safe-Checked"
                //Set the Id to "Safe-Checked" and recursively call this method again
                if (arr[x + 1][y - 1].getText().equals("0")&& !arr[x+1][y+1].getId().equals("Safe-Checked"))
                {
                    arr[x+1][y-1].setId("Safe-Checked");
                    testZero(x + 1, y - 1);
                }
            }
            catch(IndexOutOfBoundsException | NullPointerException a){}

            try 
            {
                //If the text is null and the graphic is null, call testForBomb
                if (arr[x-1][y-1].getText() == null && arr[x-1][y-1].getGraphic() == null)
                {
                    arr[x - 1][y - 1].setText(testForBomb(x - 1, y - 1));
                    ++revealed;
                }
                
                //If the text is 0 and the Id does not equal "Safe-Checked"
                //Set the Id to "Safe-Checked" and recursively call this method again
                if (arr[x - 1][y - 1].getText().equals("0")&& !arr[x-1][y-1].getId().equals("Safe-Checked"))
                {
                    arr[x-1][y-1].setId("Safe-Checked");
                    testZero(x - 1, y - 1);
                }
            }
            catch(IndexOutOfBoundsException | NullPointerException a){}
            

            try 
            {
                //If the text is null and the graphic is null, call testForBomb
                if (arr[x][y-1].getText() == null && arr[x][y-1].getGraphic() == null)
                {
                    arr[x][y - 1].setText(testForBomb(x, y - 1));
                    ++revealed;
                }
                
                //If the text is 0 and the Id does not equal "Safe-Checked"
                //Set the Id to "Safe-Checked" and recursively call this method again
                if (arr[x][y - 1].getText().equals("0")&& !arr[x][y-1].getId().equals("Safe-Checked"))
                {
                    arr[x][y-1].setId("Safe-Checked");
                    testZero(x, y - 1);
                }
            }
            catch(IndexOutOfBoundsException | NullPointerException a){}
            
            if (goodFlags == bombCounter && revealed == total)
            {
                //If all the flags are correct and all tiles revealed, call gameWon
                gameWon(); 
            }
    }
    
    //Returns the bombs left
    protected int getBombsLeft()
    {
        return bombCounter - totalFlags;
    }
    
    //Resets the every in the game back to starting defaults
    protected void resetGame()
    {
        total = 0;
        gameOver = false;
        firstClick = true;
        bombCounter = 0;
        goodFlags = 0;
        revealed = 0;
        totalFlags = 0;
    }
    
    //Returns if gameover has happened
    protected boolean checkGameOver()
    {
        return gameOver;
    }
    
    //Sets gameover back to false
    protected void resetGameOver()
    {
        gameOver = false;
    }
    
    //Sets firstclick back to true
    protected void resetClick()
    {
        firstClick = true;
    }
    
    //Set the bombCounter back to 0
    protected void resetBombCounter()
    {
        bombCounter = 0;
    }
    
    //Displays the you win message
    private void gameWon()
    {
        MinesweeperGame game = new MinesweeperGame();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Finished.");
        alert.setHeaderText("You Win!");
        alert.setContentText("You can play again by clicking 'File' "
                + "in the top left corner then click 'Restart'"
                + " OR press your enter key");
        alert.show();
        gameOver = true;
    }
    
    //Displays the you lost message
    private void gameLost()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Finished.");
        alert.setHeaderText("Game Over\nYou had " + (bombCounter - goodFlags) + 
                " bombs left.");
        alert.setContentText("You can play again by clicking 'File' "
                + "in the top left corner then click 'Restart' "
                + "OR press your enter key");
        alert.show();
        gameOver = true;
        revealBoard();
    }
    
    /**
     * Reveals all the spaces left on the game field if player loses
     */
    private void revealBoard()
    {
        for (int i = 0; i < gameSize; ++i)
        {
            for (int ind = 0; ind < gameSize; ++ind)
            {
                if (arr[i][ind].getId().equals("Bomb") &&
                        arr[i][ind].getGraphic() == null)
                {
                    arr[i][ind].setGraphic(new ImageView(bombImage));
                }
                
                if (arr[i][ind].getId().equals("Safe") &&
                        arr[i][ind].getGraphic() != null)
                {
                    arr[i][ind].setGraphic(new ImageView(badFlagImage));
                }
            }
        }
    }
    
    /**
     * Returns the total amount of bombs placed
     * @return bombCounter
     */
    protected int getTotalBombs()
    {
        return bombCounter;
    }
    
    /**
     * Get the gameSize from MinesweeperGame
     * @param size 
     */
    protected void setGameSize(int size)
    {
        gameSize = size;
    }
    
    /**
     * Get the difficulty from MinesweeprGame
     * @param diff 
     */
    protected void setDifficulty(int diff)
    {
        difficulty = diff;
    }
}
