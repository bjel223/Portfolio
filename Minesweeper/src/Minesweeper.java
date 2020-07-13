/*Class to tie GameLogic and the optionsBar to one singular frame
 *Author: Benjamin Ellis.
 *
 *
 *
 *
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Minesweeper extends JFrame implements ActionListener
{
    //Ints for preset skill level settings.
    private static int BEGINNERSIZE = 4;
    private static int INTERMEDIATESIZE = 8;
    private static int EXPERTSIZE = 12;
    private static int BEGINNERBOMBS = 4;
    private static int INTERMEDIATEBOMBS = 15;
    private static int EXPERTBOMBS = 40;
    // Core game play objects
    private GameLogic gameBoard;
    // layout object: View of the board
    private JPanel boardView;
    private int defaultSize = BEGINNERSIZE;//beginner size and bombs set to be default;
    private int numBombs = BEGINNERBOMBS;
    private int gameTime;
    private boolean winCondition;
    private optionsBar options;
    private ActionListener difficultyListener;
    private ActionListener optionsBarListener;
    // Game timer: will be configured to trigger an event every second
    private Timer gameTimer;

    public Minesweeper()
    {
        // Call the base class constructor
        super("Memory Game");
        gameTime = 0;
        gameTimer = new Timer(1000, new ActionListener(){
            public void actionPerformed(ActionEvent TL){
                gameTime++;
                options.incrementTime(gameTime);
                repaint();
            }
        });
        winCondition = false;
        boardView = new JPanel();  // used to hold game board

        // get the content pane, onto which everything is eventually added
        Container c = getContentPane();

        // Setup the game board with tiles
        gameBoard = new GameLogic(defaultSize,numBombs, this);

        // Add the game board to the board layout area
        boardView.setLayout(new GridLayout(defaultSize, defaultSize, 0, 0));
        gameBoard.fillBoardView(boardView);
        //board added to container
        c.add(boardView, BorderLayout.CENTER);

        setSize(745, 500);
        setVisible(true);
        difficultyListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//action listener for the settings menu, allowing the size and bomb count to be changed
                if(e.getActionCommand() == "Beginner"){
                    gameBoard.setDifficulty(BEGINNERBOMBS,BEGINNERSIZE);
                    defaultSize = BEGINNERSIZE;
                    restartGame();
                }
                else if(e.getActionCommand() == "Intermediate"){
                    gameBoard.setDifficulty(INTERMEDIATEBOMBS,INTERMEDIATESIZE);
                    defaultSize = INTERMEDIATESIZE;
                    restartGame();
                }
                else if(e.getActionCommand() == "Expert"){
                    gameBoard.setDifficulty(EXPERTBOMBS,EXPERTSIZE);
                    defaultSize = EXPERTSIZE;
                    restartGame();
                }
                else if(e.getActionCommand() == "Custom"){
                    options.customDifficulty();
                    gameBoard.setDifficulty(options.getcBombs(),options.getcSize());
                    defaultSize = options.getcSize();
                    restartGame();
                }
            }
        };
        optionsBarListener = new ActionListener() {//Action listener for the options bar and its menu items
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand() == "Restart"){
                    restartGame();
                }
                else if(e.getActionCommand() == "Help"){
                    options.showHelp();
                }
                else if(e.getActionCommand() == "Quit"){
                    System.exit(0);
                }
            }
        };
        options = new optionsBar(difficultyListener,optionsBarListener);//options bar
        this.setJMenuBar(options.getOptionsMenu());
        setSize(getPreferredSize());//Size updated to preferred size
    }

    /* Handle any tile that gets clicked
     * ActionListener */
    public void actionPerformed(ActionEvent e) {
        // Get the currently clicked tile from a click event
        Tile currTile = (Tile) e.getSource();
        if(!currTile.isPressed()&&!winCondition) {//preventative if statements to avoid double presses and to stop the game when its over
            if(!gameBoard.isExploded()) {
                if (!currTile.isBomb()) {
                    gameTimer.start();
                    gameBoard.clearNeighbors(currTile);
                } else {
                    gameBoard.explode(currTile);//explode board if player hits mine
                    gameTimer.stop();//stop timer if player loses
                }
                winCondition=gameBoard.checkWin();//check after successful click if player has won
                if(winCondition){//Stop timer if player wins
                    gameTimer.stop();
                }
            }
        }
        repaint();
    }

    private void restartGame()
    {
        gameTime = 0;
        gameTimer.restart();
        gameTimer.stop();
        winCondition = false;
        // Clear the boardView and have the gameBoard generate a new layout
        boardView.removeAll();
        boardView.setLayout(new GridLayout(defaultSize,defaultSize,0,0));
        gameBoard.resetBoard();
        gameBoard.fillBoardView(boardView);
        setSize(getPreferredSize());
        repaint();
    }
}
