/*Class for the tiles used in game
*Author: Benjamin Ellis
*
 */
import javax.swing.*;

public class Tile extends JButton
{
    // Resource loader
    private ClassLoader loader = getClass().getClassLoader();
    private boolean pressed;//booleans to keep track of whether a tile is a bomb or has been pressed
    private boolean bomb;
    private int neighboringBombs;//neighboring bomb count used to determine new imageicon once the tile has been pressed
    private Icon tileIcon;
    private int tileIndex;//index used in determining neighbors
    // Tile back image
    private Icon back = new ImageIcon(loader.getResource("res/Back.jpg"));
    // Default constructor
    public Tile(){ }
    public Tile(int index)
    {
        tileIndex = index;
        bomb = false;
        pressed = false;
        neighboringBombs = 0;
        super.setIcon(back);
    }

    // Set the image used as the tileIcon of the card
    // Tile flipping functions
    public void neighborsBomb(){//number of neighboring bombs kept track, and titleIcon updated each time
        neighboringBombs++;
        tileIcon = new ImageIcon(loader.getResource("res/bomb"+neighboringBombs+".jpg"));
    }
    public int getNeighboringBombs(){//Returns neighboringBombs value
        return neighboringBombs;
    }
    public void revealTile() { //Tiles are revealed with appropriate icon
        tileIcon = new ImageIcon(loader.getResource("res/bomb"+neighboringBombs+".jpg"));
        this.setIcon(tileIcon);
    }
    public void setPressed(){//Pressed boolean updated when tile is pressed
        pressed = true;
    }
    public void explodeBomb(){//Image updated when player clicks on a bomb
        tileIcon = new ImageIcon(loader.getResource("res/exploded.jpg"));
        this.setIcon(tileIcon);
    }
    public int getTileIndex(){//Returns the tile index number
        return tileIndex;
    }
    public boolean isPressed(){//returns the pressed boolean
        return pressed;
    }
    public boolean isBomb(){return bomb;}//returns whether or not a tile is a bomb
    public void setBomb(){//Tile is set to be bomb
        bomb = true;
        neighboringBombs = 9;
    }
    public void reset(){//Tile reset for resetting the board or changing board size
        bomb = false;
        pressed = false;
        neighboringBombs = 0;
        super.setIcon(back);
    }
}
