/*This program is a clone of the popular game "Minesweeper", developed, and designed, for CS335 at the University of Kentucky
*Author: Benjamin Ellis
* Date:10/19/18
 */
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Bombs {
    public static void main(String[] args) {
        Minesweeper M = new Minesweeper();
        M.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });
    }
}
