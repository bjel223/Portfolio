/*Game logic class to keep track of ,and clear, tiles, their neighbors. Win/Loss states also determined
*Author:Benjamin Ellis
 */
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class GameLogic
{
    // Array to hold board tiles
    private ArrayList<Tile> tiles;//tiles array list, due to the need to add/remove tiles based on board size
    private int gridSize;
    private int allTiles;
    private int revealedCount;
    private int nBombs;
    private boolean exploded;
    private JFrame winFrame;
    ActionListener boardListen;

    public GameLogic(int size,int numBombs, ActionListener AL)
    {
        // Allocate and configure the game board: an arraylist of tiles
        revealedCount = 0;
        exploded = false;
        nBombs = numBombs;
        gridSize = size;
        allTiles = size*size-numBombs;
        tiles = new ArrayList<>(size*size);
        boardListen = AL;
        winFrame = new JFrame("Congratulations!");
        //Fill the tiles arraylist
        for (int i = 0; i < gridSize*gridSize; i++) {
            // Setup one tile at a time
            Tile t = new Tile(i);
            // Add them to the arraylist
            tiles.add(t);
        }
        //bombs random tiles determined to be bombs
        insertBombs(numBombs);
        for(int i = 0; i < tiles.size(); i++){//neighboringBomb counts updated for each tile;
            if(!tiles.get(i).isBomb()) {
                checkNeighbors(tiles.get(i));
            }
        }
    }
    public void insertBombs(int numBombs) {
        int rand;
        for (int i = 0; i < numBombs; i++) {//Random int found from 0 to tiles.size()-1 for placement of bombs in tiles arraylist
                    rand = (int) (Math.random() * (tiles.size()-1) + 0);
                tiles.get(rand).setBomb();
        }
    }
    public void checkNeighbors(Tile t){//A tile checks all its neighbors and updates its neighboringbombs value
        int index = t.getTileIndex();
        if(index%gridSize != gridSize-1) {//If statements here ensure bombs only reach out to neighbors they have, rather than looking for values off the board
            if(tiles.get(index+1).isBomb()){
                t.neighborsBomb();
            }
            if(index-gridSize>=0) {
                if(tiles.get(index-gridSize+1).isBomb()){
                    t.neighborsBomb();
                }
            }
            if(index+gridSize<gridSize*gridSize) {
                if(tiles.get(index+gridSize+1).isBomb()){
                    t.neighborsBomb();
                }
            }
        }
        if(index%gridSize != 0) {
            if(tiles.get(index-1).isBomb()){
                t.neighborsBomb();
            }
            if(index-gridSize>=0) {
                if(tiles.get(index-gridSize-1).isBomb()){
                    t.neighborsBomb();
                }
            }
            if(index+gridSize<gridSize*gridSize) {
                if(tiles.get(index+gridSize-1).isBomb()){
                    t.neighborsBomb();
                }
            }
        }
        if(index-gridSize>=0){
            if(tiles.get(index-gridSize).isBomb()){
                t.neighborsBomb();
            }
        }
        if(index+gridSize<gridSize*gridSize){
            if(tiles.get(index+gridSize).isBomb()){
                t.neighborsBomb();
            }
        }

    }
    public void clearNeighbors(Tile t) {//Based on neighboringbombs values, bombs are cleared from the clicked tile, until tiles neighboring bombs are reached
         int index = t.getTileIndex();
         if(t.getNeighboringBombs() == 0) {
             if (index % gridSize != gridSize - 1) {//The same if statements as in checkNeighbors, for the same reasons
                if (!tiles.get(index + 1).isPressed()) {//Tiles are checked to be pressed and pressed to prevent endless recursion
                     tiles.get(index + 1).setPressed();
                     clearNeighbors(tiles.get(index + 1));
                     tiles.get(index + 1).revealTile();
                    revealedCount++;//count of revealed tiles updated to be compared to the number revealed needed to win
                 }
                 if (index - gridSize >= 0) {
                     if (!tiles.get(index - gridSize + 1).isPressed()) {
                         tiles.get(index - gridSize + 1).setPressed();
                         clearNeighbors(tiles.get(index - gridSize + 1));
                         tiles.get(index - gridSize + 1).revealTile();
                         revealedCount++;
                     }
                 }
                 if (index + gridSize < gridSize * gridSize) {
                    if (!tiles.get(index + gridSize + 1).isPressed()) {
                         tiles.get(index + gridSize + 1).setPressed();
                         clearNeighbors(tiles.get(index + gridSize + 1));
                         tiles.get(index + gridSize + 1).revealTile();
                        revealedCount++;
                     }
                 }
             }
             if (index % gridSize != 0) {
                 if (!tiles.get(index - 1).isPressed()) {
                     tiles.get(index - 1).setPressed();
                     clearNeighbors(tiles.get(index - 1));
                     tiles.get(index - 1).revealTile();
                     revealedCount++;
                 }
                 if (index - gridSize >= 0) {
                    if (!tiles.get(index - gridSize - 1).isPressed()) {
                         tiles.get(index - gridSize - 1).setPressed();
                         clearNeighbors(tiles.get(index - gridSize - 1));
                         tiles.get(index - gridSize - 1).revealTile();
                        revealedCount++;
                     }
                 }
                 if (index + gridSize < gridSize * gridSize) {
                     if (!tiles.get(index + gridSize - 1).isPressed()) {
                         tiles.get(index + gridSize - 1).setPressed();
                         clearNeighbors(tiles.get(index + gridSize - 1));
                         tiles.get(index + gridSize - 1).revealTile();
                         revealedCount++;
                     }
                 }
             }
             if (index - gridSize >= 0) {
                if (!tiles.get(index - gridSize).isPressed()) {
                     tiles.get(index - gridSize).setPressed();
                     clearNeighbors(tiles.get(index - gridSize));
                     tiles.get(index - gridSize).revealTile();
                    revealedCount++;
                 }
             }
             if (index + gridSize < gridSize * gridSize) {
                if (!tiles.get(index + gridSize).isPressed()) {
                     tiles.get(index + gridSize).setPressed();
                     clearNeighbors(tiles.get(index + gridSize));
                     tiles.get(index + gridSize).revealTile();
                     revealedCount++;
                 }
             }
         }
         else if(!t.isPressed()) {
             t.setPressed();
             t.revealTile();
             revealedCount++;
         }
    }
    public void setDifficulty(int numBombs, int size){//size and bomb number updated based on values from the settings menu
        if(size > gridSize){
            for(int i = 0; i<size*size-gridSize*gridSize;i++){//tiles are either added to the tiles arraylist or removed depending on if the size is increasing or decreasing
                Tile t = new Tile(tiles.size());
                tiles.add(t);
            }
        }
        else if(size < gridSize){
            for(int i = 0; i<gridSize*gridSize-size*size;i++){
                tiles.remove(tiles.get(tiles.size()-1));
            }
        }
        nBombs = numBombs;
        gridSize = size;
        allTiles = gridSize*gridSize-nBombs;
    }
    public void explode(Tile t){//triggered if a bomb has been clicked.
        for(int i = 0; i < tiles.size(); i++){//all tiles are flipped
            tiles.get(i).setPressed();
            tiles.get(i).revealTile();
        }
        t.explodeBomb();//special icon assigned to the exploded bomb
        exploded = true;
    }
    public boolean isExploded(){//returns exploded boolean
        return exploded;
    }
    public boolean checkWin(){//win status checked, and victory message displayed if player has won
            if(revealedCount==allTiles) {
                JOptionPane.showMessageDialog(winFrame,"Congratulations! You avoided all the bombs!");
                return true;
            }
            else{
                return false;
            }

    }
    public void fillBoardView(JPanel view)//tiles added to a the boardview
    {
        for (Tile c : tiles) {
            view.add(c);
            c.addActionListener(boardListen);
        }
    }
    public void resetBoard()//values reset to defaults, bombs are removed from the tiles and added back randomly.
    {
        revealedCount = 0;
        exploded = false;
        for (int i = 0; i < tiles.size(); i++) {
           tiles.get(i).reset();
        }
        //bombs random tiles determined to be bombs
        insertBombs(nBombs);
        for(int i = 0; i < gridSize*gridSize; i++){//neighboringBomb counts updated for each tile;
            if(!tiles.get(i).isBomb()) {
                checkNeighbors(tiles.get(i));
            }
        }
    }
}